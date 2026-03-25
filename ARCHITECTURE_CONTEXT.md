# PictureOrganizer — Architecture & Design Context

> Generated: 2026-03-25
> Purpose: Reference document capturing architectural decisions, design rationale, and implementation plan for the integrity/provenance system.

---

## Project Summary

A Java 21 desktop application (JavaFX GUI) for managing large personal photo/video collections across multiple storage drives. Core capabilities: deduplication, EXIF metadata handling, intelligent file renaming, versioning, and batch operations.

**Stack:** Java 21 · JavaFX 23-ea · Hibernate 6 / JPA 3 · MySQL 8 · C3P0 · Metadata-Extractor · Adobe XMP · Garmin FIT SDK · Launch4j

---

## Problem Space — Root Causes & Attack Vectors

The design is driven by defending against three root-cause failure modes:

| Attack Vector | Root Cause |
|---|---|
| **EXIF corruption on write** | EXIF uses absolute byte offsets in IFD structure. Any editor that inserts bytes without rewriting all pointers silently breaks data. |
| **Loss of information on rename** | If provenance data (e.g. fullHash) is encoded only in the filename, a rename destroys it permanently. |
| **Ransomware / silent corruption** | Files can be encrypted or corrupted without the user noticing until recovery is needed — too late if the backup already propagated the damage. |

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
<stEvt:softwareAgent>PictureOrganizer/2.0</stEvt:softwareAgent>
<porg:changes>
  <rdf:Bag>
    <rdf:li>{"f":"DateTimeOriginal","from":"2025:01:15 14:23:00","to":"2025:01:15 15:23:00"}</rdf:li>
    <rdf:li>{"f":"GPSLatitude","from":null,"to":"47.4979"}</rdf:li>
  </rdf:Bag>
</porg:changes>
<porg:contentHash>a3f9...</porg:contentHash>
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

---

## Workflow — Full Image Lifecycle

### Step 1: Import from Device *(manual / user-initiated)*

Import is intentionally **triggered manually by the user** for visibility and control. The user decides when to ingest from a device.

```
User initiates import (SD card / camera USB)
        │
        ▼
  Scan for files
        │
        ├─ Already known? (contentHash in DB) → skip / flag as duplicate location
        │
        └─ New file:
              ├─ Parse EXIF → check pointer integrity (validate IFD offsets within file bounds)
              ├─ Back up raw EXIF bytes → write into porg:ExifBackup in XMP
              ├─ Extract: datetime, camera, GPS, orientation, shotnumber
              ├─ Detect ARW+JPG pair (same basename, same directory, same shotnumber)
              ├─ Compute contentHash (pixel data only)
              ├─ Write initial XMP history entry (action=IMPORT, agent, when)
              ├─ Zero porg:CanonicalHash, compute canonical hash, write back
              ├─ Rename file (existing V6 scheme)
              ├─ DB: Image + MediaFile + MediaFileVersion(parent=null) + MediaFileInstance
              └─ Mark 3-2-1 status: 1/3 copies — backup needed
```

### Step 2: Adding Metadata — GPS, Captions, Ratings *(auto-detected via folder watcher)*

External editors (Lightroom, Windows Explorer, etc.) are not aware of PictureOrganizer. A **folder watcher** (`java.nio.WatchService`) detects file changes automatically.

```
ENTRY_MODIFY detected by WatchService
        │
        ├─ Debounce: wait 3–5s for write to settle
        │   (editors write temp files, rename, delete — multiple events per save)
        ├─ Known media extension? No → ignore
        ├─ Compute new contentHash
        │
        ├─ contentHash SAME, fullHash DIFFERENT
        │     → metadata-only change (external editor wrote EXIF/XMP)
        │     → read new EXIF, diff against DB-known state
        │     → write XMP history: {action=EXTERNAL_EDIT, agent=Unknown, delta}
        │     → new MediaFileVersion(reason=EXTERNAL_EDIT, parent=previous)
        │     → recompute and write new porg:CanonicalHash
        │     → assert contentHash == original contentHash (safeguard)
        │
        ├─ contentHash DIFFERENT
        │     → pixel data changed — flag SUSPICIOUS
        │     → if sealed original: RANSOMWARE_CANDIDATE
        │     → do NOT auto-create version — require user confirmation
        │
        ├─ ENTRY_DELETE
        │     → mark MediaFileInstance as MISSING (never delete from DB)
        │
        └─ ENTRY_CREATE
              → known contentHash? → duplicate, link it
              → else: surface to user for manual import decision
```

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
        ├─ Write corrected datetime to EXIF DateTimeOriginal, DateTimeDigitized
        ├─ Preserve original in porg:OriginalDateTime (never lost)
        ├─ Append XMP history: {action=TIME_FIX, shiftSeconds=X, reason="clock drift"}
        ├─ Apply same shift to paired ARW (if exists)
        ├─ Assert: contentHash unchanged
        └─ New MediaFileVersion(reason=TIME_FIX), same contentHash, new fullHash
```

### Step 4: Backup

```
Backup run (scheduled or manual)
        │
        ├─ Pre-backup integrity scan:
        │     ├─ Recompute contentHash for all files pending backup
        │     ├─ Sealed original and contentHash changed → RANSOMWARE_FLAG, abort directory
        │     └─ >N files in directory flagged simultaneously → hold entire backup, alert user
        │
        ├─ Copy files to target drive(s)
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
| Version/parent chain | ✅ `MediaFileVersion.parent` | `reason` field; immutability seal |
| XMP read | ✅ `ExifReadWriteIMR` reads `xmpMM:*` | — |
| XMP write / history | ❌ | `XmpHistoryWriter`, delta logging |
| Canonical hash | ❌ | Reserved field + zero-before-hash protocol |
| `validateAgainstBackupExif()` | ✅ (buggy) | Fix: always returns `false` |
| TimeShift detection UI | ✅ `TimeLine`, `Stripes`, `Picture` | `TimeLineController` is empty |
| TimeShift commit to disk | ❌ | Write EXIF, create version, XMP history |
| ARW+JPG pairing | Partial (`mainMediaFile` field) | Import-time matching logic |
| Folder watcher | ❌ | `WatchService` integration |
| Ransomware detection | Partial (manual `VerifyHash`) | Automated scanning, anomaly rules, quarantine |
| 3-2-1 compliance | Partial (instances tracked) | `Drive.mediaType/location`, policy engine |
| Pre-computed status columns | ❌ | `ImageStatus` table + background scanner |
| Dashboard | ❌ | Summary + drill-down + heatmap UI |

---

## Key Invariants to Enforce

1. **`contentHash` of a new `MediaFileVersion` must equal `contentHash` of its parent** — unless the version reason is explicitly `PIXEL_EDIT` (which should be rare and require confirmation)
2. **Sealed originals (`MediaFileVersion.isOriginal() == true`) must never have their `contentHash` change** — any detected change is a `RANSOMWARE_CANDIDATE`
3. **`porg:CanonicalHash` must be zeroed before computing the canonical hash** — enforced in `JPGHash` segment parsing
4. **Every file write goes through the version event framework** — no direct file modification without a corresponding `MediaFileVersion` record and XMP history entry
5. **Backup propagation is gated by pre-backup integrity scan** — no automatic propagation of files with changed `contentHash` on sealed originals
