# PictureOrganizer — Architecture & Design Context

> Generated: 2026-03-25
> Purpose: Reference document capturing architectural decisions, design rationale, and implementation plan for the integrity/provenance system.

---

## Project Summary

A Java 21 desktop application (JavaFX GUI) for managing large personal photo/video collections across multiple storage drives. Core capabilities: deduplication, EXIF metadata handling, intelligent file renaming, versioning, and batch operations.

**Stack:** Java 21 · JavaFX 23-ea · Hibernate 6 / JPA 3 · MySQL 8 · C3P0 · Metadata-Extractor · Adobe XMP · Garmin FIT SDK · Launch4j

---

## Three Distinct Media Type Workflows

This is the most fundamental architectural split in the system. The three media types have different preservation contracts, different mutability rules, and different source-of-truth models.

### JPG / Non-RAW — File is self-contained, file is the source of truth

- The file carries **all information about itself** — pixels, metadata, provenance history, hashes
- The **filename is volatile** — it is a commodity handle for OS-level access, nothing more. Any information encoded only in the filename (e.g. a hash) is permanently lost on rename. This is why provenance must live inside the file.
- **Sidecar files are not used** for JPG. They are non-conventional, invisible to most tools, and trivially lost on copy, move, or rename. Everything goes into embedded XMP.
- The **DB is a performance cache and cross-file index**, not the source of truth:
  - Reading 100k files per query is not realistic
  - Drives may be detached — the DB must serve queries without them
  - But if the DB is lost, re-scanning all available files must be able to reconstruct it
  - What cannot be reconstructed: explicitly deleted/invalid versions (e.g. a corrupted file that was purged) — this is accepted and documented
- Cross-file relationships that matter (RAW+JPG pairing, document identity) must be **encoded in the files themselves** (via `porg:` XMP fields), not only in the DB, so they survive DB loss.

### RAW (ARW/DNG/NEF) — File is sacred, never written to directly

- RAW contains the original sensor data. It is the photographic negative. **It must never be modified after import.**
- All metadata changes (time corrections, GPS, captions, ratings) go into an **XMP sidecar file** (`.xmp` alongside the RAW file)
- The RAW file's `contentHash` is immutable by definition — it IS the original
- The RAW `contentHash` serves as the **permanent anchor identity** for the entire pair
- The sidecar XMP carries: all history deltas, all metadata corrections, canonical hash reference, pairing link to JPG
- **Backup = RAW file + sidecar file** — they must always travel together. A RAW without its sidecar loses all post-capture metadata; a sidecar without its RAW is meaningless.
- Sidecar absence is not always an error on first import (no edits yet), but must be created on first metadata operation

**The contradiction problem** — three sources may disagree for the same metadata field:

| Source | Meaning | Trust level |
|---|---|---|
| RAW embedded EXIF | What the camera recorded at capture | Immutable original, but may be wrong (clock drift, no GPS at capture time) |
| XMP sidecar | Current applied/corrected state | Authoritative for current intent |
| Paired JPG EXIF | What was embedded when JPG was generated, may have been independently edited | Informational, may diverge |

**Conflict resolution hierarchy:** `sidecar XMP > paired JPG > RAW embedded`
- Sidecar is the "applied" state — the user's expressed intent
- Paired JPG is informational and may have been independently corrected
- RAW embedded is the camera original — preserved but may be superseded
- When sources disagree beyond tolerance: flag as `metadataStatus=DATE_CONFLICT` and surface for user resolution
- The original RAW values are always preserved and never overwritten — conflict is additive, not destructive

### Video — Distinct media with time dimension

- Video has unique attributes: duration, codec, framerate, audio tracks, resolution
- Folder watcher debounce must be longer for video (large files take longer to write; editors may re-encode multiple passes)

---

## Problem Space — Root Causes & Attack Vectors

The design is driven by defending against three root-cause failure modes:

| Attack Vector                      | Root Cause                                                                                                                                     |
|------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| **EXIF corruption on write**       | EXIF uses absolute byte offsets in IFD structure. Any editor that inserts bytes without rewriting all pointers silently breaks data.           |
| **Loss of information on rename**  | If provenance data (e.g. fullHash) is encoded only in the filename, a rename destroys it permanently.                                          |
| **Ransomware / silent corruption** | Files can be encrypted or corrupted without the user noticing until recovery is needed — too late if the backup already propagated the damage. |
| **Byte rot**                      | Files stored on a phyisical device might get corrupted. Some SW/OS setup counteract this, but we want to be aware any damage.                  |

---

## Core Design Decisions

### 1. Two-Hash Model (already implemented)

| Hash | What it covers | Key property |
|---|---|---|
| `contentHash` | Pixel / media data only (EXIF excluded) | Stable across all metadata edits. Identical between original and any metadata-enriched version. |
| `fullHash` | Entire file | Changes on every edit, including metadata-only changes. |

This distinction is the foundation for everything else: it lets the system know *why* a hash changed.

### 2. EXIF vs XMP — Hybrid Approach

**Do not replace EXIF with XMP.** Cameras write EXIF; many readers (OS thumbnails, display devices, phone galleries) read orientation/datetime from EXIF first or only. Fighting the ecosystem is impractical.

**Hybrid strategy:**
- **EXIF** — keep for all camera-standard fields (datetime, orientation, GPS, camera model). Readers expect it here.
- **XMP** — own namespace (`porg:`) for all provenance, history, hashes, version links. No pointer fragility (XML, self-describing, no offsets). Supported by Lightroom, Capture One, Bridge.
- **On first import** — back up raw EXIF bytes into `porg:ExifBackup` XMP field. If EXIF pointers break later, the original blob is always recoverable.
- **For RAW files (ARW/DNG/NEF)** — write XMP sidecar on first import.

### 3. The Self-Referential Hash — Canonical Form Trick

**The problem:** `fullHash` cannot be written into the file because doing so changes the file, thus changing `fullHash`.

**Impossible approaches:**
- Fixed-point hash (`H(M || H(M)) = H(M)`) — would require breaking preimage resistance. Not feasible.
- CRC fixed-point — CRC is linear over GF(2) so a suffix can be computed to target any CRC value, but CRC is not cryptographically secure.

**The solution — canonical form (how PDF signatures, PE Authenticode, and XML-DSig all solve this):**

1. Reserve N bytes in the file for `porg:CanonicalHash` XMP field, set to empty/zeros
2. Compute `H = SHA256(entire file, with that field empty)`
3. Write `H` into the field
4. **Verification:** read file → zero/empty `porg:CanonicalHash` → recompute → compare

The stored hash is "the hash of the canonical form of this file." It is:
- Deterministic and verifiable
- Self-contained (no database required)
- Survives renames, copies, drive changes
- Independently verifiable with ExifTool + sha256sum:

```bash
# Compute canonical hash
exiftool -xmp-porg:CanonicalHash="" -o - file.jpg | sha256sum

# Verify
stored=$(exiftool -s3 -xmp-porg:CanonicalHash file.jpg)
computed=$(exiftool -xmp-porg:CanonicalHash="" -o - file.jpg | sha256sum | cut -d' ' -f1)
[ "$stored" = "$computed" ] && echo OK || echo MISMATCH
```

> **ExifTool is the independent audit tool.** The project's in-process `JPGHash` parser is used for bulk operations. ExifTool is used for verification that doesn't depend on the application.

**XMP size limit caveat:** APP1 in JPEG has a 64KB limit. For long-lived files with many edits, keep only the last N history entries inline; archive older entries to the DB (linked via `xmpMM:DocumentID`).

### 4. Event Sourcing for Metadata — Self-Healing EXIF

Every metadata change is stored as a **delta** (before/after per field) in XMP history, not just as an event. This enables replay-based recovery:

```
porg:ExifBackup (raw EXIF from import)
    → apply delta 1: DateTimeOriginal shifted +1h
    → apply delta 2: GPS added (lat=47.49, lon=19.04)
    → apply delta 3: Caption set "Budapest trip"
    ─────────────────────────────────────────────
    = current expected EXIF state
```

Compare reconstructed state against actual EXIF → detect corruption → self-heal by writing reconstructed state back. **No backup drive needed for metadata recovery.**

**XMP history entry structure (per `porg:` namespace):**

```xml
<stEvt:action>METADATA_UPDATE</stEvt:action>
<stEvt:when>2025-07-14T18:23:00+02:00</stEvt:when>
<stEvt:ver>2.0</stEvt:ver>
<porg:changes>
  <rdf:Bag>
    <rdf:li>{"f":"DateTimeOriginal","from":"2025:01:15 14:23:00","to":"2025:01:15 15:23:00"}</rdf:li>
    <rdf:li>{"f":"GPSLatitude","from":null,"to":"47.4979"}</rdf:li>
    <rdf:li>{"f":"contentHash","from":>a3f9...,"to":"b7d2..."}</rdf:li>
  </rdf:Bag>
</porg:changes>
<porg:previousCanonicalHash>b7d2...</porg:previousCanonicalHash>
```

**Version event types:** `IMPORT` · `METADATA_UPDATE` · `TIME_FIX` · `GPS_ADD` · `EXTERNAL_EDIT` · `REPAIR` · `INTEGRITY_CHECK` · `BACKUP`

### 5. Two-Hash Semantics Across the Lifecycle

| Event | contentHash | fullHash | Meaning |
|---|---|---|---|
| Import | set | set | Baseline |
| GPS / caption added | **same** | different | Metadata enriched, pixels intact |
| Time corrected | **same** | different | Metadata corrected, pixels intact |
| Ransomware | **different** | different | Pixel data destroyed — do not back up |
| EXIF pointer broken | **same** | different | Structural damage, pixels safe, can repair |
| File renamed | **same** | **same** | No content change, canonical hash still valid |
| Video trim | **different** | different | Intentional pixel-level edit — `VIDEO_TRIM` reason, not suspicious |
| RAW file (any state) | **immutable** | immutable | RAW is never written — contentHash is permanent anchor |
| Sidecar updated | n/a | sidecar fullHash changes | RAW unchanged; sidecar carries its own canonical hash |

### 6. DB as Reconstructible Cache

The DB is authoritative for **performance and cross-file queries** (drives may be detached, 100k file scans are not realistic at query time). But it must be reconstructible from files alone.

**What the DB stores that is NOT in the files (computed/derived state):**
- `backupStatus`, `integrityStatus`, `ransomwareStatus`, `lastVerifiedAt` — recalculated on rescan
- 3-2-1 compliance aggregate — recalculated from `MediaFileInstance` records
- Drive mount state, `lastSeenAt` — re-established on reconnect

**What must be encoded IN the files to survive DB loss:**
- All provenance history and deltas (`porg:` XMP history)
- `contentHash`, `porg:CanonicalHash` — embedded in file/sidecar
- Document identity: `xmpMM:DocumentID`, `xmpMM:OriginalDocumentID` — survives rename
- Pairing link: `porg:pairedFile` in JPG XMP → canonical reference to its RAW sibling (by DocumentID, not filename)
- Reverse link: `porg:pairedJpg` in RAW sidecar → canonical reference to its JPG

**What is intentionally unrecoverable after DB loss:**
- Explicitly deleted versions (corrupted files that were purged) — accepted and documented
- Verification timestamps older than the last rescan

---

## Workflow — Full Image Lifecycle

### Step 1: Import from Device *(manual / user-initiated)*

Import is intentionally **triggered manually by the user** for visibility and control. The user decides when to ingest from a device.

```
User initiates import (SD card / camera USB)
        │
        ▼
  Scan for files — detect pairs (same basename, same dir, same shotnumber)
        │
        ├─ Already known? (contentHash in DB) → skip / flag as duplicate location
        │
        ├─ JPG / non-RAW:
        │     ├─ Parse EXIF → validate IFD offsets within file bounds
        │     ├─ Back up raw EXIF bytes → write into porg:ExifBackup in XMP
        │     ├─ Extract: datetime, camera, GPS, orientation, shotnumber
        │     ├─ Write porg:pairedFile → DocumentID of paired RAW (if exists)
        │     ├─ Compute contentHash (pixel data only)
        │     ├─ Write XMP history entry (action=IMPORT)
        │     ├─ Zero porg:CanonicalHash, compute, write back
        │     ├─ Rename file (V6 scheme)
        │     └─ DB: Image + MediaFile + MediaFileVersion(parent=null, sealed) + MediaFileInstance
        │
        ├─ RAW (ARW/DNG/NEF):
        │     ├─ Compute contentHash (sensor data) → this is the permanent immutable anchor
        │     ├─ Extract embedded EXIF (read-only reference, never modify RAW)
        │     ├─ Create XMP sidecar if not present:
        │     │     ├─ Write porg:RawContentHash (= contentHash, immutable)
        │     │     ├─ Write porg:pairedJpg → DocumentID of paired JPG (if exists)
        │     │     └─ Write sidecar history entry (action=IMPORT)
        │     ├─ Rename RAW + sidecar together (V6 scheme, same base name)
        │     └─ DB: Image + MediaFile + MediaFileVersion(parent=null, sealed) + MediaFileInstance
        │           (RAW MediaFileVersion.contentHash never changes after this point)
        │
        └─ Video:
              ├─ Compute contentHash (frame data)
              ├─ Extract: datetime, camera, duration, GPS
              ├─ Write XMP history entry (action=IMPORT)
              ├─ Zero porg:CanonicalHash, compute, write back
              ├─ Rename file (V6 scheme)
              └─ DB: Image + MediaFile + MediaFileVersion(parent=null, sealed, duration=X) + MediaFileInstance
```

**After pair detection, resolve date conflicts immediately at import:**
```
Paired RAW+JPG found:
  ├─ Compare DateTimeOriginal: RAW embedded vs JPG embedded
  ├─ If within tolerance (e.g. <2s): use RAW value as canonical, note in sidecar
  └─ If diverged: flag metadataStatus=DATE_CONFLICT, surface to user before completing import
```

### Step 2: Adding Metadata — GPS, Captions, Ratings *(auto-detected via folder watcher)*

External editors (Lightroom, Windows Explorer, etc.) are not aware of PictureOrganizer. A **folder watcher** (`java.nio.WatchService`) detects file changes automatically.

```
ENTRY_MODIFY detected by WatchService
        │
        ├─ Debounce: wait 3–5s for write to settle (longer for video: 15–30s)
        │   (editors write temp files, rename, delete — multiple events per save)
        ├─ Known media extension? No → ignore (.xmp sidecar changes handled separately below)
        │
        ├─ JPG modified:
        │     ├─ Compute new contentHash
        │     ├─ contentHash SAME, fullHash DIFFERENT
        │     │     → metadata-only change
        │     │     → diff EXIF/XMP against DB-known state
        │     │     → write XMP history: {action=EXTERNAL_EDIT, agent=Unknown, delta}
        │     │     → new MediaFileVersion(reason=EXTERNAL_EDIT, parent=previous)
        │     │     → recompute and write porg:CanonicalHash
        │     │     → assert contentHash == original contentHash (safeguard)
        │     └─ contentHash DIFFERENT
        │           → pixel data changed — flag SUSPICIOUS
        │           → if sealed original: RANSOMWARE_CANDIDATE
        │           → do NOT auto-create version — require user confirmation
        │
        ├─ RAW modified:
        │     → RAW contentHash must NEVER change after import
        │     → ANY change to a RAW file = CORRUPTION or RANSOMWARE_CANDIDATE
        │     → immediate flag, no version created, alert user
        │
        ├─ XMP sidecar modified (.xmp file):
        │     → Paired RAW is untouched (expected)
        │     → Diff sidecar against DB-known state
        │     → Write porg: history entry into sidecar (action=EXTERNAL_EDIT)
        │     → Recompute sidecar canonicalHash
        │     → Check for conflicts: sidecar values vs paired JPG vs RAW embedded
        │     → If conflict: flag metadataStatus=DATE_CONFLICT / GPS_CONFLICT
        │     → new MediaFileVersion(reason=EXTERNAL_EDIT) for the RAW logical file
        │
        ├─ ENTRY_DELETE
        │     ├─ RAW deleted: CRITICAL — mark MISSING, alert (may be ransomware)
        │     ├─ Sidecar deleted: flag — post-capture metadata lost, attempt restore from DB
        │     └─ JPG deleted: mark MediaFileInstance as MISSING (never delete from DB)
        │
        └─ ENTRY_CREATE
              → known contentHash? → duplicate, link it
              → else: surface to user for manual import decision
              → if .xmp appears alongside known RAW: associate as its sidecar

**Folder watcher edge cases:**

| Problem | Solution |
|---|---|
| USB/network drives — WatchService unreliable | Polling fallback: scan on mount, then every N minutes |
| Drive not mounted | On mount detection, run catch-up scan |
| Lightroom batch export (1000 files) | Throttle: collect events per directory, process as batch |
| Editor temp files (`.tmp`, `~file.jpg`) | Process only after file is stable (size unchanged for 2s) |
| Large file — read before write completes | Retry with exponential backoff |

### Step 3: Time Correction — Clock Drift / Timezone Fix *(auto-detected, applied manually)*

Detection is automatic (TimeShift UI visualises drift). Approval and commit is manual.

```
User approves shift in TimeShift UI
        │
        ├─ JPG:
        │     ├─ Write corrected datetime to EXIF DateTimeOriginal, DateTimeDigitized
        │     ├─ Preserve original in porg:OriginalDateTime (never lost, never overwritten)
        │     ├─ Append XMP history: {action=TIME_FIX, shiftSeconds=X, reason=...}
        │     ├─ Assert: contentHash unchanged
        │     └─ New MediaFileVersion(reason=TIME_FIX), same contentHash, new fullHash
        │
        ├─ RAW (via sidecar — RAW file never touched):
        │     ├─ Write corrected datetime into sidecar XMP only
        │     ├─ Preserve RAW's original datetime in sidecar as porg:RawOriginalDateTime
        │     ├─ Append sidecar history: {action=TIME_FIX, shiftSeconds=X}
        │     └─ New MediaFileVersion(reason=TIME_FIX) for the logical RAW file
        │
        └─ Paired RAW+JPG: apply same shift atomically to both
              → if shift differs between them (pre-existing conflict): surface to user first
```

### Step 3b: Video Trim *(user-initiated)*

```
User trims video (crops in time)
        │
        ├─ contentHash of trimmed video WILL differ from original — this is expected
        ├─ Original version is preserved (sealed, never deleted unless user explicitly requests)
        ├─ New MediaFileVersion(reason=VIDEO_TRIM, trimStart=Xs, trimEnd=Ys, duration=Z)
        ├─ New version's contentHash is computed from trimmed content
        ├─ Append XMP history: {action=VIDEO_TRIM, trimStart, trimEnd, originalDuration}
        └─ DB: duration stored on MediaFileVersion (not on Image — it's version-specific)
```

### Step 4: Backup

```
Backup run (scheduled or manual)
        │
        ├─ Pre-backup integrity scan:
        │     ├─ JPG/Video: recompute contentHash, compare to DB sealed original
        │     ├─ RAW: recompute contentHash — if changed: CORRUPTION (RAW is always sealed)
        │     ├─ Sidecar: verify sidecar is present alongside every RAW
        │     ├─ Any sealed original contentHash changed → RANSOMWARE_FLAG, abort directory
        │     └─ >N files in directory flagged simultaneously → hold entire backup, alert user
        │
        ├─ Copy files to target drive(s):
        │     ├─ JPG/Video: copy file
        │     └─ RAW: copy RAW file + sidecar together (fail if sidecar missing and RAW has history)
        │
        ├─ Verify copy: recompute contentHash on destination, compare to source
        ├─ DB: new MediaFileInstance(drive=backupDrive) for each copied file
        ├─ Check 3-2-1 compliance: ≥3 instances, ≥2 media types, ≥1 offsite?
        └─ Report: X new, Y updated, Z compliance gaps remaining
```

### Step 5: Routine Verification *(background, continuous)*

```
Background scanner — priority queue ordered by:
  1. ransomwareStatus = SUSPICIOUS       (immediate)
  2. integrityStatus  = CORRUPTED        (high)
  3. lastVerifiedAt   IS NULL            (never checked)
  4. lastVerifiedAt   < (now − 30 days)  (stale)

Per file:
  ├─ File exists? No → MISSING
  ├─ Recompute contentHash → compare to DB
  ├─ Read porg:CanonicalHash → recompute canonical hash → compare
  ├─ Mismatch: flag, update ImageStatus
  └─ Match: update lastVerifiedAt
```

### Step 6: Recovery

```
File missing or corrupted
        │
        ├─ Query DB: other MediaFileInstances for this MediaFile?
        │     → copy from healthy backup instance
        │     → verify contentHash after copy
        │
        ├─ EXIF corrupted, pixel data OK:
        │     ├─ Replay XMP history deltas from porg:ExifBackup
        │     │   → reconstruct expected EXIF state without touching backup drive
        │     ├─ Or: read Image.exif from DB
        │     └─ Restore EXIF, create MediaFileVersion(reason=REPAIR)
        │
        └─ File totally unreadable (ransomware):
              ├─ contentHash will fail — pixel data destroyed
              ├─ Restore from backup copy predating infection
              └─ DB timeline (TrackingEntity timestamps) shows exact infection date
```

---

## Missing Pieces — Implementation Plan

### Phase 1 — Self-Contained Provenance Chain *(foundation)*

- **1a** Fix `validateAgainstBackupExif()` — always returns `false` (one-liner bug)
- **1b** `XmpHistoryWriter` — appends to `xmpMM:History` with `porg:` delta fields
- **1c** Version event framework — add `reason` enum to `MediaFileVersion`; seal originals with immutability flag
- **1d** Canonical hash — reserved `porg:CanonicalHash` XMP field, zero-before-hash protocol in `JPGHash`

### Phase 2 — EXIF Integrity Hardening

- **2a** TIFF/IFD pointer validator — extend `TIFFHash.scan()` to validate all offsets are within file bounds
- **2b** Systematic EXIF backup on import — call `addBackupExif()` for every JPEG (currently not called on import path); XMP sidecar for RAW
- **2c** Corruption repair workflow — detect broken pointers → restore from `porg:ExifBackup` or `Image.exif` in DB → `MediaFileVersion(reason=REPAIR)`

### Phase 3 — ARW+JPG Pairing

- Import-time matching: same basename, same directory, same `shotnumber`
- Populate `MediaFile.mainMediaFile` (field exists, no population logic yet)
- Paired operations: time correction, backup, ransomware scan apply to pair atomically
- UI: show RAW+JPG as one logical unit

### Phase 4 — TimeShift Commit *(wire up existing UI)*

- `TimeLineController` is currently empty — implement commit path
- Approved shift → `ExifService.updateExif()` → `XmpHistoryWriter.addEvent(TIME_FIX)` → new `MediaFileVersion`
- Apply to paired ARW simultaneously

### Phase 5 — Ransomware Detection

- **5a** DB-integrated baseline — replace file-based `imageHash.txt` with DB-backed scan records
- **5b** Anomaly rules: >N files in directory changed `contentHash` in short window; sealed original content changed; extension changed; ransomware note filenames
- **5c** Quarantine workflow — `quarantine=true` on `MediaFileInstance`; backup propagation skips quarantined instances; UI review queue

### Phase 6 — 3-2-1 Compliance

- **6a** Enrich `Drive` entity: add `mediaType` enum (`HDD/SSD/NAS/CLOUD/OPTICAL/TAPE`) and `location` enum (`LOCAL/OFFSITE/CLOUD`)
- **6b** Compliance query: per `Image`, count `MediaFileInstance` grouped by `drive.location` and `drive.mediaType`
- **6c** Drive status: `lastSeenAt` distinguishes "expected offsite" from "drive missing"

### Phase 7 — Dashboard

- **Pre-computed status** on `Image` (or companion `ImageStatus` table) — never compute at render time:
  - `backupStatus`: `COMPLIANT | NEEDS_COPY | STALE_COPY | NO_BACKUP`
  - `integrityStatus`: `OK | CORRUPTED | UNVERIFIED | STALE_CHECK`
  - `duplicateStatus`: `UNIQUE | CONTENT_DUPLICATE | FILE_DUPLICATE`
  - `metadataStatus`: `OK | EXIF_BROKEN | MISSING_DATE | DATE_SUSPICIOUS`
  - `ransomwareStatus`: `CLEAN | SUSPICIOUS | QUARANTINED`
  - `lastVerifiedAt`, `lastBackupAt`
- Summary counters → category drill-down (paginated, indexed column queries) → file list with actions
- Date heatmap: X = year/month, Y = status category, colour = severity — reveals patterns like "all 2021 files unverified"
- Duplicate detection: `GROUP BY contentHash HAVING COUNT(*) > 1` — O(n), already indexed

### Phase Dependency Order

```
Phase 1 (provenance chain)
    │
    ├─── Phase 2 (EXIF integrity)
    │
    ├─── Phase 3 (ARW+JPG pairing)
    │         │
    └─────────┴──► Phase 4 (TimeShift commit)
                        │
              ┌─────────┴──────────┐
         Phase 5               Phase 6
       (Ransomware)            (3-2-1)
              └─────────┬──────────┘
                    Phase 7
                  (Dashboard)
```

---

## What Exists Today vs What's Needed

| Capability | Exists | Missing |
|---|---|---|
| Content-aware hashing (pixel only) | ✅ `JPGHash`, `TIFFHash`, `MP4Hash` | — |
| EXIF backup embedding | ✅ `addBackupExif()` | Not called on import path |
| EXIF pointer validation | Partial (`TIFFHash` parses IFD) | Offset bounds checking; repair path |
| Version/parent chain | ✅ `MediaFileVersion.parent` | `reason` field; immutability seal; `VIDEO_TRIM` with trimStart/trimEnd/duration |
| XMP read | ✅ `ExifReadWriteIMR` reads `xmpMM:*` | — |
| XMP write / history | ❌ | `XmpHistoryWriter`, delta logging |
| Canonical hash | ❌ | Reserved field + zero-before-hash protocol |
| `validateAgainstBackupExif()` | ✅ (buggy) | Fix: always returns `false` |
| TimeShift detection UI | ✅ `TimeLine`, `Stripes`, `Picture` | `TimeLineController` is empty |
| TimeShift commit to disk | ❌ | JPG: write EXIF + XMP history; RAW: write sidecar only |
| ARW+JPG pairing | Partial (`mainMediaFile` field) | Import-time matching; `porg:pairedFile` links in files; conflict detection |
| RAW immutability enforcement | ❌ | Any RAW file modification = immediate CORRUPTION flag |
| XMP sidecar management | ❌ | Create on first edit; keep in sync with RAW; backup as unit |
| RAW/sidecar/JPG conflict detection | ❌ | Date, GPS, caption reconciliation with priority hierarchy |
| DB reconstruction from files | ❌ | Re-scan all files → rebuild Image/MediaFileVersion graph from `porg:` XMP |
| `porg:pairedFile` cross-reference | ❌ | Encode pairing by DocumentID in files (not just DB) |
| Video trim workflow | ❌ | `VIDEO_TRIM` version reason; trimStart/trimEnd; duration per version |
| Folder watcher | ❌ | `WatchService` integration; RAW vs sidecar vs JPG handling; video debounce |
| Ransomware detection | Partial (manual `VerifyHash`) | Automated scanning, anomaly rules, quarantine |
| 3-2-1 compliance | Partial (instances tracked) | `Drive.mediaType/location`, policy engine |
| Pre-computed status columns | ❌ | `ImageStatus` table + background scanner |
| Dashboard | ❌ | Summary + drill-down + heatmap UI |

---

## Key Invariants to Enforce

1. **`contentHash` of a new `MediaFileVersion` must equal `contentHash` of its parent** — unless the version reason is explicitly `VIDEO_TRIM` or `PIXEL_EDIT` (rare, requires confirmation)
2. **Sealed originals (`MediaFileVersion.isOriginal() == true`) must never have their `contentHash` change** — any detected change is a `RANSOMWARE_CANDIDATE`
3. **RAW files are always sealed — their `contentHash` is immutable** — any change detected after import is `CORRUPTION`, regardless of reason
4. **RAW and its sidecar are backed up as an atomic unit** — backing up one without the other is an error if the sidecar has post-import history
5. **`porg:CanonicalHash` must be zeroed before computing the canonical hash** — enforced in `JPGHash` segment parsing; same protocol for sidecar XMP
6. **Every file write goes through the version event framework** — no direct file modification without a corresponding `MediaFileVersion` record and XMP history entry
7. **Backup propagation is gated by pre-backup integrity scan** — no automatic propagation of files with changed `contentHash` on sealed originals
8. **Pairing links are stored in both files** — `porg:pairedFile` in JPG XMP + `porg:pairedJpg` in sidecar, using `xmpMM:DocumentID` (not filename) so they survive renames
9. **The DB is a cache** — every piece of information needed for recovery must be derivable from scanning files alone; the DB may be rebuilt from scratch at any time
