# PictureOrganizer — Architecture & Design Context

> Generated: 2026-03-25 · Revised against `master` 2026-04-17
> Purpose: Reference document capturing architectural decisions, design rationale, and implementation plan for the integrity/provenance system.
>
> **Revision note (2026-04-17):** the previous version was written by scanning an older branch and carried two assumptions that do not match how the system actually developed:
> 1. That every version carries a machine-readable **reason** (`TIME_FIX`, `VIDEO_TRIM`, `EXTERNAL_EDIT`, …). In practice the system **cannot know why a file changed** — it only detects that it did. The design has therefore been simplified: any detected content change becomes a new `MediaFileVersion`. The `reason` enum is removed from the plan. `MediaFileVersion.invalid = true` has a narrower meaning than the earlier plan assumed: it flags **structural brokenness of the file itself** — the TIFF/EXIF parser cannot read the metadata, image data is undecodable, XMP is malformed. A file that merely *changed* (new hash, still parses correctly) is a new version, not an invalid one. Whether that new version is acceptable or needs to be reverted is a separate user decision (see §6).
> 2. That the DB is a **reconstructible cache** of information that lives in the files. The truth is in between: once a file passes through `importFile()` the system writes a `porg:` XMP history entry recording its original `contentHash`, and from that point the file carries its own identity. So in **steady state** — every file known to the system — files can largely reconstruct the DB. The DB's irreducible value is concentrated at two points:
     >    - **Initial ingest of legacy collections**, where the user's memory ("I remember the original was on this drive in 2019") is the only available witness. That decision is recorded in the DB during import and then propagates into the file's `porg:` history.
>    - **Offline drives**, where the user wants to query their collection without every disk attached.
     >
     >    The DB is therefore a **fast index plus a capture surface for user decisions made at ingest**. Drive-role labels like "primary vs. backup" are not treated as load-bearing: all accessible copies are equivalent; if a drive is reachable, its information can be read.
>
> These two shifts propagate throughout the document below.

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
- The **DB is a fast cross-file index plus a capture surface for decisions made at ingest**:
    - Reading 100k files per query is not realistic; the DB exists so UI queries are fast and drive-independent
    - Drives may be detached — the DB must serve queries without them
    - At first ingest of a legacy collection, the user's knowledge ("this copy is the original, that one is a duplicate I made in 2019") is recorded in the DB and — once import writes the `porg:` XMP history — propagates into the file itself
    - After that, the file carries its own identity (original `contentHash`, history chain, pairing links) and the DB is essentially a queryable cache of what the files say
- Cross-file relationships that the files themselves can carry (RAW+JPG pairing via DocumentID, per-file history, original-hash reference) are encoded in the files (via `porg:` XMP fields). A rescan of all files can largely rebuild the DB; the only things a rescan cannot recover are decisions the user made but never imported, and the history of offline drives.

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
- **On first import — EXIF backup is a retagged duplicate APP1 segment inside the JPEG itself.** The full EXIF APP1 block is copied byte-for-byte into a second APP1 segment whose identifier prefix is replaced with `BACKUPID` (or `BACKUP_EST_ID` when the EXIF values were reconstructed rather than read from the camera). This is implemented by `JPGHash.addBackupExif()`. Rationale: lives on the file itself, portable across drives, survives XMP edits, no XML escaping overhead for the opaque maker blob (which would otherwise bloat the XMP packet). `porg:ExifBackup` as an XMP field was an earlier design; it is **not** what the code does. The architectural goal ("byte-exact backup of the original EXIF attached to the file") is satisfied by the retagged-segment mechanism.
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

**Threat model — what `porg:CanonicalHash` protects against and what it does not:**

| Threat level | Scenario | Defended? |
|---|---|---|
| **T1 — Accidental corruption** | Bit rot, partial write, truncated copy, file-system glitch, naive ransomware that encrypts pixels without touching XMP | ✅ yes — canonical hash mismatch detects it |
| **T2 — Third-party editor rewrites** | Lightroom/Bridge rewrites EXIF and does not know about `porg:CanonicalHash` | ✅ yes — mismatch creates a new `MediaFileVersion`; user decides whether to accept by setting `invalid=false` |
| **T3 — Sophisticated attacker** | Attacker modifies pixels AND rewrites `porg:CanonicalHash` to match the tampered file | ❌ no — requires external signing (private key outside the file) |

T3 is explicitly out of scope. Defending against it requires a detached signature or an append-only external ledger, neither of which is compatible with "self-contained, verifiable with `exiftool + sha256sum`." Accept this limitation.

**XMP integrity — short hash for T1 detection:**

The canonical hash protects pixel data but the XMP block itself can be corrupted independently (truncated write, partial overwrite by an editor, bit rot inside the APP1 segment). A corrupted XMP can invalidate recovery even when pixels are fine.

Defence: alongside `porg:CanonicalHash`, store a short (e.g. 8-byte / 64-bit) `porg:XmpIntegrity` hash computed over the serialised `porg:` subtree with `porg:XmpIntegrity` itself zeroed. Same zero-before-hash protocol as canonical hash, same verifiability.

- Short hash is sufficient — this is an accidental-corruption detector (T1), not a security primitive. 64 bits makes collision under random corruption astronomically unlikely.
- If `porg:XmpIntegrity` fails but `porg:CanonicalHash` of the pixel data still matches: pixels are intact, only provenance metadata is damaged → the file is structurally broken at the XMP level, so new `MediaFileVersion(parent=previous, invalid=true)`; user can trigger rebuild from DB-side XMP mirror.
- If both fail and the file no longer parses: new `MediaFileVersion(parent=previous, invalid=true)`.
- If both fail but the file still parses (pixel data replaced with something valid but different): new `MediaFileVersion(parent=previous)` — `invalid=false` because the file is structurally fine; the backup gate withholds propagation until the user accepts the change.
- If `porg:XmpIntegrity` matches but `porg:CanonicalHash` fails: either T3 (out of scope) or a T2 editor rewrote pixels while leaving `porg:` alone — same handling as the previous case.

### 4. Event Sourcing for Metadata — Self-Healing EXIF

Every metadata change is stored as a **delta** (before/after per field) in XMP history, not just as an event. This enables replay-based recovery:

```
EXIF backup segment (raw EXIF from import, retagged BACKUPID inside the JPEG)
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

**Version creation rule (simplified):** the system does not attempt to classify *why* a file changed. Any detected content change creates a new `MediaFileVersion` linked to its parent. The history entry written into the file records the delta (what changed), not a machine-readable reason code. Where the change is judged unwanted (corruption, ransomware-like damage, failed import), the new `MediaFileVersion.invalid` flag is set — that is the only classification the system makes.

The `stEvt:action` field in the XMP history block remains a free-form string for humans reading the file externally (e.g. `"IMPORT"`, `"METADATA_UPDATE"`, `"EXIF_RESTORE"`); it is informational, not authoritative, and the code does not branch on it.

### 5. Two-Hash Semantics Across the Lifecycle

| Observed | contentHash | fullHash | `invalid`? | System response |
|---|---|---|---|---|
| Import | set | set | false | Baseline — first `MediaFileVersion` |
| GPS / caption added externally | **same** | different | false | New `MediaFileVersion(parent=previous)`; file parses fine |
| Time corrected | **same** | different | false | New `MediaFileVersion(parent=previous)` |
| EXIF pointer broken (parser fails) | **same** | different | **true** | New `MediaFileVersion(parent=previous, invalid=true)`; pixels safe, metadata unreadable; user can initiate repair |
| File renamed / moved | **same** | **same** | n/a | No new version; update `MediaFileInstance` filename/path |
| Video trim (user-initiated) | **different** | different | false | New `MediaFileVersion(parent=previous)`; new contentHash |
| Unexpected pixel change, file still parses | **different** | different | false | New `MediaFileVersion(parent=previous)`; does not propagate to backup until user accepts |
| RAW file modified, still parses | **different** | different | false | New `MediaFileVersion(parent=previous)`; the RAW should not have changed but the system doesn't know why — user reviews |
| File undecodable (truncated, corrupted pixel data) | cannot compute | cannot compute | **true** | New `MediaFileVersion(parent=previous, invalid=true)`; structural brokenness |
| Sidecar updated | n/a (RAW itself unchanged) | sidecar fullHash changes | false | New sidecar history entry; no new `MediaFileVersion` for the RAW itself unless sidecar changes the applied state |

The system does not distinguish "ransomware" from "bit rot" from "an editor I forgot I ran" — all three look identical as "contentHash changed, file still parses." The backup gate is what protects data: a new unaccepted version doesn't propagate. `invalid=true` is reserved for the narrower case where the file itself is broken enough that parsing fails.

### 6. DB as Fast Index + Ingest Capture Surface

This project is not greenfield. It ingests years of existing backups, many copies of the same files across drives, filenames that drifted over time. At the moment of first ingest, the user's memory is the only witness to facts like "the original of this was on the 2019 NAS" — bytes on disk cannot tell you that. The import flow's job is to **capture that decision once** and then **propagate it into the file** via the `porg:` XMP history, so that afterwards the file can tell you what the user told the DB.

**What the DB captures at ingest that bytes cannot:**
- Which copy the user considers the original (before the system writes that fact into the file)
- The mapping of divergent filenames on divergent drives back to a single logical `MediaFile` / `Image`
- Any per-file `invalid` decisions the user makes about detected changes
- Human-entered classification: directories, pairing intent, duplicates-to-merge

**What the files carry once imported (so a rescan can rebuild most of the DB):**
- Per-file provenance history — `porg:` XMP history with deltas
- `contentHash` — recomputable from pixels
- Reference to the file's original `contentHash` in the import history entry — so a file can identify itself as "an original" or "a derivative of hash X"
- Document identity: `xmpMM:DocumentID`, `xmpMM:OriginalDocumentID` — survives rename
- Pairing link: `porg:pairedFile` in JPG XMP / `porg:pairedJpg` in sidecar (by DocumentID)

**What DB loss actually costs in steady state (most files imported):**
- Fast query performance until a full rescan completes
- Knowledge of drives that are currently offline
- Knowledge of any decisions the user made that were never committed to file (`invalid` toggles that weren't followed by a history-rewriting action)

**Drive roles are not load-bearing.** The earlier version of this doc planned a `Drive.mediaType` and `Drive.location` classification (HDD/SSD/NAS/CLOUD, LOCAL/OFFSITE/CLOUD) to power a 3-2-1 compliance engine. In practice, "where exactly is this copy" matters less than "is it accessible, and does its `contentHash` still match." Any two accessible copies with matching hashes are interchangeable. The `Drive.backup` boolean that already exists is probably enough; a formal 3-2-1 engine is deferred until a clear use case demands it.

**Operationally:** back up the DB (it still concentrates ingest decisions and is the query surface) but do not treat its loss as catastrophic — a rescan of a reasonably complete file collection rebuilds the working knowledge.

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
        │     ├─ Parse standard EXIF/TIFF tags (MakerNote treated as opaque)
        │     │     → parse fails on standard tags → reject import, surface to user
        │     ├─ Back up raw EXIF bytes verbatim → retagged duplicate APP1 segment (BACKUPID) inside the JPEG
        │     │     (whole blob, including opaque maker sections — enables byte-level restore later)
        │     │     via JPGHash.addBackupExif()
        │     ├─ Extract: datetime, camera, GPS, orientation, shotnumber
        │     ├─ Write porg:pairedFile → DocumentID of paired RAW (if exists)
        │     ├─ Compute contentHash (pixel data only)
        │     ├─ Write XMP history entry (stEvt:action="IMPORT", informational)
        │     │     with porg:originalContentHash = <this file's contentHash>
        │     │     (so this file self-identifies as an original in any future rescan)
        │     ├─ If the user chose this copy as "the original of three duplicates I already had",
        │     │     write porg:originalContentHash = <the chosen original's hash> into any
        │     │     copies the user marks as derivatives — propagates the decision into files
        │     ├─ Zero porg:CanonicalHash, compute, write back
        │     ├─ Rename file (V6 scheme)
        │     └─ DB: Image + MediaFile + MediaFileVersion(parent=null, invalid=false) + MediaFileInstance
        │
        ├─ RAW (ARW/DNG/NEF):
        │     ├─ Compute contentHash (sensor data) → anchors the logical image
        │     ├─ Extract embedded EXIF (read-only reference, never modify RAW)
        │     ├─ Create XMP sidecar if not present:
        │     │     ├─ Write porg:RawContentHash (= contentHash at import time)
        │     │     ├─ Write porg:pairedJpg → DocumentID of paired JPG (if exists)
        │     │     └─ Write sidecar history entry (stEvt:action="IMPORT")
        │     ├─ Rename RAW + sidecar together (V6 scheme, same base name)
        │     └─ DB: Image + MediaFile + MediaFileVersion(parent=null, invalid=false) + MediaFileInstance
        │           (If a RAW's contentHash is ever observed to change after this, a new MediaFileVersion
        │            is created; invalid=true only if the RAW no longer parses — see Step 2)
        │
        └─ Video:
              ├─ Compute contentHash (frame data)
              ├─ Extract: datetime, camera, duration, GPS
              ├─ Write XMP history entry (stEvt:action="IMPORT")
              ├─ Zero porg:CanonicalHash, compute, write back
              ├─ Rename file (V6 scheme)
              └─ DB: Image + MediaFile + MediaFileVersion(parent=null, invalid=false) + MediaFileInstance
                    (duration stored on Image)
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
        │     ├─ Try to parse EXIF/XMP
        │     │     → parse fails (TIFF offsets bad, XMP malformed, etc.)
        │     │           → new MediaFileVersion(parent=previous, invalid=true)
        │     │           → surface for user review / repair
        │     ├─ Compute new contentHash
        │     ├─ contentHash SAME, fullHash DIFFERENT
        │     │     → metadata-only change (harmless)
        │     │     → diff EXIF/XMP against DB-known state
        │     │     → write XMP history: {stEvt:action="METADATA_UPDATE", delta}
        │     │     → new MediaFileVersion(parent=previous, invalid=false)
        │     │     → recompute and write porg:CanonicalHash
        │     └─ contentHash DIFFERENT, but file parses fine
        │           → pixel data changed without a user action we asked for
        │           → new MediaFileVersion(parent=previous, invalid=false)
        │           → backup gate does NOT propagate until user accepts this version
        │
        ├─ RAW modified (contentHash changed):
        │     → RAW is not supposed to change after import
        │     → if RAW no longer parses: new MediaFileVersion(parent=previous, invalid=true)
        │     → else: new MediaFileVersion(parent=previous, invalid=false), backup gate withholds propagation
        │     → surface to user; the cause (corruption, ransomware, accidental edit) is not knowable here
        │
        ├─ XMP sidecar modified (.xmp file):
        │     → Paired RAW is untouched (expected)
        │     → Diff sidecar against DB-known state
        │     → Write porg: history entry into sidecar (stEvt:action="METADATA_UPDATE")
        │     → Recompute sidecar canonicalHash
        │     → Check for conflicts: sidecar values vs paired JPG vs RAW embedded
        │     → If conflict: flag metadataStatus=DATE_CONFLICT / GPS_CONFLICT for user review
        │     → new MediaFileVersion(parent=previous, invalid=false) for the RAW logical file
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
│     ├─ Append XMP history: {stEvt:action="TIME_FIX", shiftSeconds=X, note=...}
│     ├─ Assert: contentHash unchanged
│     └─ New MediaFileVersion(parent=previous, invalid=false) — same contentHash, new fullHash
│
├─ RAW (via sidecar — RAW file never touched):
│     ├─ Write corrected datetime into sidecar XMP only
│     ├─ Preserve RAW's original datetime in sidecar as porg:RawOriginalDateTime
│     ├─ Append sidecar history: {stEvt:action="TIME_FIX", shiftSeconds=X}
│     └─ New MediaFileVersion(parent=previous, invalid=false) for the logical RAW file
│
└─ Paired RAW+JPG: apply same shift atomically to both
→ if shift differs between them (pre-existing conflict): surface to user first
```

### Step 3b: Video Trim *(user-initiated)*

```
User trims video (crops in time)
│
├─ contentHash of trimmed video WILL differ from original — this is expected
├─ Parent version is kept in DB, never deleted unless user explicitly requests
├─ New MediaFileVersion(parent=previous, invalid=false) — file is structurally fine
├─ New version's contentHash is computed from trimmed content
├─ Append XMP history: {stEvt:action="VIDEO_TRIM", trimStart, trimEnd, originalDuration}
└─ Note: because the system cannot distinguish "user trimmed this" from "something else
changed the pixels", the watcher path records the same kind of new version either way.
The video-trim UI flow is what explicitly marks the new version as accepted — i.e. the
backup gate will now propagate it. Without that user confirmation, a pixel-change new
version sits there until reviewed.
```

### Step 4: Backup

```
Backup run (scheduled or manual)
│
├─ Pre-backup integrity scan (one simple rule — all three hashes must match what DB last recorded):
│     ├─ contentHash matches latest accepted MediaFileVersion?  no → skip (a new invalid version will be recorded)
│     ├─ porg:CanonicalHash matches recomputed value?            no → skip
│     ├─ porg:XmpIntegrity matches recomputed value?             no → skip
│     └─ Sidecar present alongside every RAW that has post-import history?   no → skip
│     (skipped files are NOT propagated; healthy backup copies on other drives are never overwritten)
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
1. MediaFileVersion.invalid = true on latest version  (structurally broken file — needs attention)
2. Latest version not yet accepted by user            (change detected, waiting for review)
3. lastVerifiedAt IS NULL                             (never checked)
4. lastVerifiedAt < (now − 30 days)                   (stale)

Per file:
├─ File exists? No → mark MediaFileInstance MISSING
├─ Try to parse metadata (EXIF, XMP) and decode image data
│     → parse/decode fails → new MediaFileVersion(parent=previous, invalid=true)
├─ Recompute contentHash → compare to DB's latest accepted version
├─ Read porg:CanonicalHash → recompute canonical hash → compare
├─ Hash mismatch (but file parses): new MediaFileVersion(parent=previous, invalid=false),
│     mark "awaiting user acceptance"; backup gate will withhold propagation
└─ All match: update lastVerifiedAt on MediaFileInstance
```

### Step 6: Recovery

```
File missing or corrupted
│
├─ Query DB: other MediaFileInstances for this MediaFile?
│     → copy from healthy backup instance
│     → verify contentHash after copy
│
├─ EXIF unparseable, pixel data OK:
│     ├─ Byte-level restore of the whole EXIF blob from the BACKUPID APP1 segment
│     │   in the same JPEG (verbatim — including opaque MakerNote bytes we don't interpret)
│     ├─ Or fallback: Image.exif from DB if the BACKUPID segment is also missing/damaged
│     └─ Create MediaFileVersion(parent=previous, invalid=false)
│           with stEvt:action="EXIF_RESTORE" in the XMP history (informational)
│
└─ File totally unreadable:
├─ contentHash cannot be computed — pixel data destroyed
├─ Restore from a MediaFileInstance on another drive whose version matches the
│   last-known-good MediaFileVersion (the latest with invalid=false)
└─ DB timeline (TrackingEntity timestamps) shows when each version first appeared —
use this to pick a pre-damage instance
```

---

## Missing Pieces — Implementation Plan

### Phase 1 — Import Pipeline *(the biggest current gap)*

Master has the provenance primitives (`XmpHistoryWriter`, `ExifService.addXmpHistoryEvent`, hash classes, entity model with `invalid` flag) but **no import orchestration and no folder watcher at all**. The GUI is the only entry point and there is no end-to-end flow that writes history, creates a `MediaFileVersion`, and places a `MediaFileInstance` in one go.

- **1a** Fix `validateAgainstBackupExif()` — the result of the byte comparison is discarded; the method effectively always returns `false`. One-line fix.
- **1b** Systematic EXIF backup on import — `addBackupExif()` exists but nothing calls it on the import path. Wire it in.
- **1c** Import service — compose `JPGHash` + `ExifService.addXmpHistoryEvent` + DAO writes into one `importFile(File)` call. Idempotent: re-importing a known contentHash is a no-op or creates a new `MediaFileInstance` only.
- **1d** Canonical hash in XMP — reserved `porg:CanonicalHash` field, zero-before-hash protocol. Extend `JPGHash` with zero-and-hash write, and a read-and-verify read path. (Must be XMP, not JPEG COMMENT — keeps one provenance channel.)
- **1e** XMP integrity short hash — `porg:XmpIntegrity` (64-bit) over the `porg:` subtree, same zero-before-hash protocol. Defends against XMP-only corruption.

### Phase 2 — Folder Watcher

Not present on master. Needed for Step 2 of the workflow.

- **2a** `java.nio.WatchService` wrapper — handle `ENTRY_CREATE`, `ENTRY_MODIFY`, `ENTRY_DELETE`
- **2b** Stability polling — file size unchanged for N seconds before processing; 3–5 s for JPG, 15–30 s for video
- **2c** Per-extension routing — JPG / RAW / sidecar / video each have their own handler (Step 2 tree)
- **2d** Batch throttling — collect events per directory before processing (Lightroom export case)
- **2e** Mount-time catch-up scan — for USB/network drives WatchService doesn't cover
- **2f** Decision rule for every detected change: compute the three hashes, compare to DB, create a new `MediaFileVersion(parent=previous)`, set `invalid` based on whether the change is acceptable (metadata-only = false; unexpected pixel change = true)

### Phase 3 — EXIF Recovery via Byte-Level Backup *(scaled back)*

Originally scoped as "TIFF/IFD pointer validator + structural corruption repair." That approach is a dead end: proprietary MakerNote structures (Sony, Canon, Nikon, Fuji, …) use undocumented offsets and custom IFDs. A generic validator can't tell a broken offset from a valid-but-unknown maker structure, and a generic "repair" would either leave maker blocks untouched (not useful) or rewrite them wrong (actively destructive).

Detection is already covered elsewhere — any metadata change flips `fullHash` and goes through the Step 2 watcher branch. Recovery is therefore a **byte-level** problem, not a structural one:

- **3a** Parse-success gate on standard IFDs only — treat MakerNote and unknown tags as **opaque pass-through** bytes. A file is `invalid=true` only when the standard TIFF / EXIF / XMP parsers can't read the file at all (not when a maker block looks odd).
- **3b** Recovery workflow — on `invalid=true`, the user triggers a **byte-level restore** from the BACKUPID APP1 segment inside the same JPEG (captured on first import, Step 1). The whole original EXIF blob is written back verbatim; no attempt to fix individual fields. Result: a new `MediaFileVersion(parent=previous, invalid=false)` with `stEvt:action="EXIF_RESTORE"` in the XMP history.
- **3c** `Image.exif` in DB is a secondary fallback when the BACKUPID segment itself was corrupted or stripped.

Structural validation of maker fields is explicitly **out of scope** unless and until a specific maker's format is fully reverse-engineered — at which point it becomes a per-maker plugin, not a generic phase.

### Phase 4 — ARW+JPG Pairing

- Import-time matching: same basename, same directory, same `shotnumber`
- Populate `MediaFile.mainMediaFile` (field exists, no population logic yet)
- Paired operations: time correction, backup, verification apply to pair atomically
- UI: show RAW+JPG as one logical unit
- Encode in files: `porg:pairedFile` in JPG XMP and `porg:pairedJpg` in RAW sidecar, both using `xmpMM:DocumentID`

### Phase 5 — TimeShift UI + Commit

- TimeShift UI itself is not present on master (no `TimeLine`, `Stripes`, `Picture` classes, no `TimeLineController`). Build it.
- Approved shift → `ExifService.updateExif()` + `ExifService.addXmpHistoryEvent(..., stEvt:action="TIME_FIX", ...)` → new `MediaFileVersion(parent=previous, invalid=false)`
- Apply to paired ARW simultaneously via Phase 4

### Phase 6 — Backup / Ransomware Defence *(keep simple — gate at the backup boundary)*

No anomaly heuristics, no "N files in M minutes" rules, no ransomware-note filename detectors. Two separate concepts cover the whole defence:

1. **`invalid=true`** — structurally broken file (parse or decode fails). Never propagated to backup; user must repair or restore.
2. **Unaccepted new version** — hash mismatch on a file that still parses. A new `MediaFileVersion` is created with `invalid=false`, but the backup gate withholds it until the user explicitly accepts the new state as canonical. Healthy backup copies on other drives remain untouched.

The cause of a mismatch — ransomware, bit rot, editor bug, power loss — does not matter at this layer. The response is the same: do not overwrite good backup copies until a human confirms the current on-disk state is what they want.

- **6a** DB-backed verification baseline — replace file-based `imageHash.txt` (`VerifyHash`) with scans that read from and update DB tables
- **6b** Hash-gated backup job — pre-backup scan computes all three hashes; any mismatch creates a new `MediaFileVersion` (with `invalid=true` if the file no longer parses, otherwise `invalid=false` pending user acceptance); backup skips files whose on-disk state does not match the latest user-accepted version
- **6c** Surface in the dashboard the two queues that need user attention: (a) `invalid=true` files awaiting repair, and (b) unaccepted new versions awaiting accept/reject

### Phase 7 — Multi-Copy Health *(deferred — may not be needed)*

The earlier plan here was a formal 3-2-1 compliance engine with `Drive.mediaType` and `Drive.location` enums. On reflection this is over-engineered: what actually matters is "does at least one other accessible copy of this file still match the last-known-good `contentHash`?" — a question the existing `MediaFileInstance` + `MediaFileVersion` schema already answers without drive-role classification.

Keep as a light Phase 7 until a concrete use case justifies more:

- **7a** Per-image health: count `MediaFileInstance` rows whose latest `MediaFileVersion` is `invalid=false`. One → warn, zero → critical.
- **7b** Drive `lastSeenAt` to distinguish "I haven't plugged in that external drive in 6 months" from "drive is lost".
- **7c** Only if a user actually asks for 3-2-1 compliance reporting: add `Drive.mediaType` / `Drive.location`. Not before.

### Phase 8 — Dashboard

- **Pre-computed status** on `Image` (or companion `ImageStatus` table) — never compute at render time:
  - `backupStatus`: `COMPLIANT | NEEDS_COPY | STALE_COPY | NO_BACKUP`
  - `integrityStatus`: `OK | CORRUPTED | UNVERIFIED | STALE_CHECK`
  - `duplicateStatus`: `UNIQUE | CONTENT_DUPLICATE | FILE_DUPLICATE`
  - `metadataStatus`: `OK | EXIF_BROKEN | MISSING_DATE | DATE_SUSPICIOUS`
  - `reviewStatus`: derived from `MediaFileVersion.invalid` on the latest version (`CLEAN | NEEDS_REVIEW`)
  - `lastVerifiedAt`, `lastBackupAt`
- Summary counters → category drill-down (paginated, indexed column queries) → file list with actions
- Date heatmap: X = year/month, Y = status category, colour = severity — reveals patterns like "all 2021 files unverified"
- Duplicate detection: `GROUP BY contentHash HAVING COUNT(*) > 1` — O(n), already indexed

### Phase Dependency Order

```
Phase 1 (import pipeline) ───► BACKUPID EXIF segment captured per file
│
├─── Phase 2 (folder watcher) ─┬──► Phase 3 (EXIF byte-level restore)
│                              │    (triggered when watcher marks invalid=true)
│                              │
├─── Phase 4 (ARW+JPG pairing) ├──► Phase 5 (TimeShift UI + commit)
│                              │
└──────────────────────────────┴──► Phase 6 (hash-gated backup)
│
┌─────────┴──────────┐
Phase 7               Phase 8
(3-2-1)              (Dashboard)
```

---

## What Exists Today on `master` vs What's Needed

Verified against `master` HEAD (`b986987`, 2026-04-17). Entity class and method references are exact.

| Capability | Exists on master | Missing / Next step |
|---|---|---|
| Content-aware hashing (pixel only, MD5) | ✅ `JPGHash`, `TIFFHash`, `MP4Hash` via `MediaFileHash.getHash()` | — |
| EXIF backup embedding | ✅ `JPGHash.addBackupExif()` | No import-path caller wires it in |
| EXIF parsing (standard tags) | Partial — `TIFFHash` parses IFD tags | Parse-success gate at import (standard tags only); treat MakerNote as opaque; byte-level restore from the BACKUPID APP1 segment on failure |
| EXIF backup embedding | ✅ `JPGHash.addBackupExif()` — retagged duplicate APP1 segment (BACKUPID / BACKUP_EST_ID) inside the JPEG itself | Wire it into the import pipeline (Phase 1) |
| `validateAgainstBackupExif()` | ✅ but buggy — result of comparison is discarded, effectively always `false` | One-line fix |
| `MediaFileVersion` entity | ✅ with `parent`, `filehash`, `size`, `dateStored*`, **`invalid`** (Boolean), `mediaFile` FK | Wire `invalid` into backup gate + dashboard; no `reason` enum by design |
| `MediaFile.mainMediaFile` (pairing link) | ✅ field + FK | No population logic; no `porg:pairedFile` in files yet |
| `Image` entity with `valid` flag | ✅ | — |
| `Drive` entity | ✅ with `description`, `volumeSN`, **`backup`** (boolean) | No `mediaType` / `location` enums — Phase 7 |
| XMP history writer | ✅ **`XmpHistoryWriter`** + `ExifService.addXmpHistoryEvent()` writing `porg:` deltas, `stEvt:action`, `porg:previousCanonicalHash`; unit-tested | — |
| `MetadataChange` delta record | ✅ `(field, from, to)` → JSON | — |
| XMP read | ✅ `ExifReadWriteIMR` | — |
| EXIF write via ExifTool | ✅ `ExifReadWriteET` | — |
| Canonical hash (`porg:CanonicalHash`) | ❌ | Reserved XMP field + zero-before-hash protocol |
| XMP integrity short hash (`porg:XmpIntegrity`, T1 defence) | ❌ | 64-bit hash over `porg:` subtree, zero-before-hash |
| `JPAConnection` with TEST/DEV/PROD modes | ✅ `JPAConnection.DBMode` + `setMode()` | No `resetInstance()` yet — needed for test isolation |
| Persistence units | ✅ `mysql-picture` (single PU) | No H2 test unit; no Liquibase changelogs (schema is `hbm2ddl` only) |
| Tests: hash, EXIF read/write, `XmpHistoryWriter`, image/media services | ✅ dedicated suites under `src/test/` | End-to-end import test; folder-watch test |
| Comparison UI (`Service/Comparison/`) | ✅ ComparePanel, CompareTableView, Duplicate, Listing, MetaChanges | — |
| Summary UI (`SummaryController`, `SummaryDTO`, `DirectorySummaryDTO`, `FolderSummaryDTO`) | ✅ tree views exist | Not wired to provenance/integrity status |
| TimeShift UI | ❌ — no `TimeLine` / `Stripes` / `Picture` / `TimeLineController` on master | Build UI + commit path for time corrections (Phase 5) |
| Import service / pipeline | ❌ — no `ImportService` on master | Phase 1c |
| Folder watcher | ❌ — no `FolderWatchService` or WatchService usage on master | Phase 2 |
| RAW immutability enforcement | ❌ | Watcher detects any RAW change → new `MediaFileVersion(parent=previous)`; `invalid=true` only if the RAW no longer parses; backup gate withholds propagation until user acceptance either way |
| XMP sidecar management | Partial — `RAWMediaFileInstance.XMPattached` flag + `.xmp` copy-on-rename in `AnalyzingMediaFile` | Create on first edit; keep in sync with RAW; include in backup as atomic unit |
| RAW/sidecar/JPG conflict detection | ❌ | Date, GPS, caption reconciliation with priority hierarchy |
| `porg:pairedFile` cross-reference | ❌ | Encode pairing by DocumentID in files (Phase 4) |
| Video trim workflow | ❌ | New `MediaFileVersion` on trim; duration on version |
| Hash-gated backup | ❌ | Phase 6b — pre-backup scan + skip invalid versions |
| `VerifyHash` — DB integration | ❌ — still file-based (`imageHash.txt`, `main()` entry point only) | Phase 6a |
| 3-2-1 compliance | Partial — instances tracked per drive, `Drive.backup` boolean | `Drive.mediaType`, `Drive.location`, policy query (Phase 7) |
| Pre-computed status columns | ❌ | `ImageStatus` table + background scanner (Phase 8) |
| Dashboard | ❌ | Summary + drill-down + heatmap UI (Phase 8) |

---

## Key Invariants to Enforce

1. **Any detected content change creates a new `MediaFileVersion`** — linked to the previous one via `parent`. The system does not classify *why* a file changed; it records *that* it did. There is no `reason` enum.
2. **`MediaFileVersion.invalid = true` means the file is structurally broken** — EXIF/TIFF parser fails, XMP malformed, image data undecodable. It is a property of the bytes on disk, not a judgement about whether a change was wanted. A file that merely changed (new hash, still parses) is a new version with `invalid=false` — what gates its propagation to backup is **user acceptance**, not the invalid flag.
3. **RAW files should not change after import** — if one does, the system creates a new `MediaFileVersion(parent=previous)` and the backup gate withholds propagation. `invalid=true` is set only if the RAW no longer parses. The cause of the change is not knowable from the file alone.
4. **RAW and its sidecar are backed up as an atomic unit** — backing up one without the other is an error if the sidecar has post-import history.
5. **`porg:CanonicalHash` must be zeroed before computing the canonical hash** — enforced in `JPGHash` segment parsing; same protocol for sidecar XMP.
6. **`porg:XmpIntegrity` must be zeroed before computing the XMP short hash** — same zero-before-hash protocol, scoped to the `porg:` subtree only; detects XMP-block corruption independently of pixel hash.
7. **Every file modification the system performs writes an XMP history entry** — so external observers (including a future rescan) can follow the chain from the BACKUPID EXIF segment (the pre-modification baseline) forward.
8. **Backup propagation is gated by "matches latest user-accepted version"** — not simply by hash match. A hash mismatch creates a new `MediaFileVersion` (with `invalid=true` only if the file no longer parses). Whether that new version propagates to backup depends on whether the user has accepted it as canonical. Healthy backup copies on other drives are never overwritten by an unaccepted or invalid version. The cause of the mismatch is irrelevant at this layer.
9. **Pairing links are stored in both files AND in the DB** — `porg:pairedFile` in JPG XMP + `porg:pairedJpg` in sidecar, using `xmpMM:DocumentID` (not filename) so they survive renames. The DB also stores the pairing (`MediaFile.mainMediaFile`) so the decision is queryable without reading every file.
10. **The DB captures user decisions at ingest and propagates them into files** — which copy is the original, which arrivals are the same logical image, explicit duplicate-merges. The import flow writes these decisions into the file's `porg:` XMP history (notably `porg:originalContentHash`) so that after a round of ingest, the files themselves carry the decision. In steady state a rescan can rebuild most of the DB; what rescan cannot rebuild is information about drives that are currently offline and decisions that were never committed to file. The DB is therefore backed up like the files but its loss is recoverable, not catastrophic.
