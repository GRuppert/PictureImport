# Codebase Concerns

**Analysis Date:** 2026-04-19

## Tech Debt

**Version tree — missing parent setting for Versions:**
- Issue: Collision tree was introduced with basic function but the logic to assign `parent` on `MediaFileVersion` rows is incomplete (explicit commit message `93caa14 Collision tree basic function, setting parent for Versions is missing`). `MediaGeneralDAOImplHib.loadDirectoryVersions(int)` builds the version tree by reading `parent_id` from the DB, but the UI-initiated flow that populates these parents still contains heuristic fallback logic that silently skips non-unique matches.
- Files: `src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/MediaGeneralDAOImplHib.java:119-232`, `src/main/java/org/nyusziful/pictureorganizer/UI/Contoller/SummaryController.java:194-225`, `src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/MediaFileVersionDAOImplHib.java:146-160`
- Impact: Collision detection runs but many version rows stay parentless in the DB, so the version tree renders incomplete or wrong ancestry; `versionTree` on the Summary screen shows root nodes but not full trees.
- Fix approach: Audit the `setAsParent` menu action in `SummaryController.SummaryTreeCell`; normalise the "multiple versions in parent directory" early-return on lines 199 and 207 into an explicit user prompt; backfill historical rows with a migration that walks `media_file_version` ordered by `date_stored`.

**"no clue" commit b6bc686 is effectively unreviewed:**
- Issue: Commit `b6bc686 no clue` (2025-11-30) makes 109 insertions / 44 deletions across the version-loading DAO and the summary view — the author themselves did not understand why the change was needed. It rewrites `MediaGeneralDAOImplHib.loadDirectoryVersions` and extends `VersionDTO` / `SummaryController`.
- Files: `src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/MediaGeneralDAOImplHib.java`, `src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/MediaFileVersionDAOImplHib.java`, `src/main/java/org/nyusziful/pictureorganizer/DTO/VersionDTO.java`, `src/main/java/org/nyusziful/pictureorganizer/UI/Contoller/SummaryController.java`, `WHATSNOW`
- Impact: Any regression in the version tree is hard to bisect — the commit has no rationale and the surrounding behaviour is already fragile (see above).
- Fix approach: Walk through the diff, add a regression test for `loadDirectoryVersions(int)` against a fixture DB, rewrite as a series of smaller commits with intent in the message.

**`PresetUseCases.java` is a god-file:**
- Issue: 898 lines, mixes UI dialogs (`JOptionPane`, `JProgressBar`), use-case orchestration, file-system scanning and DB access. Much of the body is commented-out scratch code; line 297 constructs an `AnalyzingMediaFile media = null` and immediately calls `media.getProcessing()` which guarantees `NullPointerException`.
- Files: `src/main/java/org/nyusziful/pictureorganizer/Main/PresetUseCases.java:296-307`
- Impact: The use-case class cannot be entered without NPE; it is also the single largest file in the code base and blocks refactoring of the Main package.
- Fix approach: Restore `new AnalyzingMediaFile(next)` (commented on line 298) or remove the unreachable branch; split use-cases into one-file-per-scenario.

**`DBConnection` is obsolete but still compiled and callable:**
- Issue: `DBConnection` contains raw-JDBC SQL string concatenation (classic SQL-injection shape), hard-coded credentials, a dead `main(String[])`, and commented-out CRUD. JPA is used everywhere else; this class is a trap for new contributors.
- Files: `src/main/java/org/nyusziful/pictureorganizer/DAL/DBConnection.java:1-166` (especially lines 32, 93, 148, 153, 157)
- Impact: Any code path that reaches `getConnection()` opens a MySQL socket to a stale LAN address with plain-text creds, bypassing all Hibernate safeguards and caching.
- Fix approach: Delete the class; keep only the single legitimate consumer (if any) rewritten through `JPAConnection`/`MediaGeneralDAOImplHib` pattern.

**`MediafileDAOImplLocal` is a stub:**
- Issue: Entire class body (`save(MediaFile)`) is commented out; only the class shell remains.
- Files: `src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/MediafileDAOImplLocal.java:9-32`
- Impact: "Local offline save" feature does not exist despite the class suggesting it does.
- Fix approach: Decide whether local CSV fallback is still a requirement; delete the file or implement it behind a feature flag.

**Package typo `UI.Contoller` (should be `Controller`):**
- Issue: Misspelled package directory propagates through every import, FXML `fx:controller=` reference, and commit; renaming now touches ~15 files plus 11 FXML files.
- Files: `src/main/java/org/nyusziful/pictureorganizer/UI/Contoller/`, every `src/main/resources/fxml/*.fxml`
- Impact: Cosmetic but permanent friction; autocomplete and grep both stumble.
- Fix approach: Do a single atomic IDE refactor; update FXML `fx:controller` attributes simultaneously.

**Commented-out code blocks treated as history:**
- Issue: 20+ files carry large `/* ... */` blocks kept as documentation of previous implementations (e.g. `JPGMediaFileInstance.java:18-90`, `SummaryController.java:33-62`, `AnalyzingMediaFile.java:35-...`, `RenameService.java:190-...`).
- Files: See Grep results in the analysis — affects the whole DAL/Service/UI layers.
- Impact: Readers cannot distinguish dead code from living code; search results are polluted.
- Fix approach: Delete the blocks; git history preserves them if ever needed.

**`@DiscriminatorValue("DEF")` on the abstract-like base entity:**
- Issue: `MediaFileVersion` is a concrete JPA entity that acts as base for JPG/RAW/Video subclasses but uses the discriminator value `DEF` — rows with unrecognised `file_type` end up as plain `MediaFileVersion` silently.
- Files: `src/main/java/org/nyusziful/pictureorganizer/DAL/Entity/MediaFileVersion.java:12-17`
- Impact: Loss of type-specific behaviour (exif backup, hash strategy) when data is malformed.
- Fix approach: Remove the discriminator default, add `@DiscriminatorOptions(force=true)` or throw on load for unknown types.

## Known Bugs

**NPE in `PresetUseCases.readFileMeta` loop:**
- Symptoms: `NullPointerException` inside the directory walker at line 299.
- Files: `src/main/java/org/nyusziful/pictureorganizer/Main/PresetUseCases.java:297-302`
- Trigger: Running the "process folder" preset path that chunks ExifTool output.
- Workaround: None — the branch is unreachable without a runtime crash.

**Foreign-key failure on media persist (logged in TODO):**
- Symptoms: `Cannot add or update a child row: a foreign key constraint fails - Media persist` when processing `DEV H:\Photos\KékB\Andaluzia\a6500\DCIM\100MSDCF\D5C01928.ARW`.
- Files: TODO line 20; surface is in `src/main/java/org/nyusziful/pictureorganizer/Service/MediaFileInstanceService.java` (persist flow) and `src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/MediaFileInstanceDAOImplHib.java`.
- Trigger: Specific ARW file path noted in TODO; probably caused by `Media` being persisted before its parent `MediaFileVersion` has an assigned id.
- Workaround: Manual DB insert of the missing parent row.

**Broken datasets under "Szétszedni" (TODO file):**
- Symptoms: Two photo tree paths (`T:\G\Pictures\Photos\V4\Gabus\2007-*`, `Photos\Új\4-6-21 Genfersee\go\DCIM\100GOPRO\GH010280.MP4`, `Snowdriftambri\D5C07651.ARW`) are called out as corrupt / need manual splitting.
- Files: `TODO:2-9`
- Trigger: Import of these specific directories.
- Workaround: Skip them manually; no tooling to detect these automatically yet.

**`MediaFileVersion.equals` may collide for broken files:**
- Symptoms: `equals` returns true for two versions if either id is `-1` and `filehash + size` match, "TODO for broken files this might be not ok".
- Files: `src/main/java/org/nyusziful/pictureorganizer/DAL/Entity/MediaFileVersion.java:102-106`
- Trigger: Two unsaved versions of a broken (empty-hash) file of the same length.
- Workaround: Save to DB before comparing.

**SessionFactory build failure swallowed:**
- Symptoms: If Hibernate bootstrap fails in `HibConnection`, only `System.out.println(e.getMessage())` runs and the singleton ends up with `sessionFactory == null`; subsequent `getCurrentSession()` NPE.
- Files: `src/main/java/org/nyusziful/pictureorganizer/DAL/HibConnection.java:25-37`
- Trigger: Mis-configured `persistence.xml`, missing DB, wrong driver.
- Workaround: Run through JPA path (`JPAConnection`) instead; still the stale `HibConnection` singleton is left in broken state for the process lifetime.

**`JPAConnection.getEntityManager` may NPE during transaction begin:**
- Symptoms: If `entityManager.getTransaction()` returns `null` the code still calls `.isActive()` on the next line (short-circuit reversed), triggering NPE only if the previous call returned null — defensive code that does not actually defend.
- Files: `src/main/java/org/nyusziful/pictureorganizer/DAL/JPAConnection.java:49-56`
- Trigger: Closed EM interacting with JTA.
- Workaround: None documented.

**XML-entity typo in JDBC URL:**
- Symptoms: `&amp&characterEncoding=UTF-8` (missing `;` and not a real entity) is passed to the MySQL driver; the driver parses it permissively but the `characterEncoding` parameter is effectively never applied.
- Files: `src/main/java/org/nyusziful/pictureorganizer/DAL/JPAConnection.java:30`
- Trigger: Every connection in `PROD` mode — PROD mode uses the empty-prefix schema `pictureorganizer` with the malformed URL.
- Workaround: Use DEV mode (set in `PictureOrganizer.start`).

## Security Considerations

**Plaintext DB credentials committed to repo:**
- Risk: User `picture` / password `picture` for MySQL baked into VCS; additional variant `nappali`/`nappali` sitting in commented-out block for `192.168.178.10`.
- Files: `src/main/resources/META-INF/persistence.xml:27-33`, `src/main/resources/old.hibernate.cfg.xml:11-14`, `src/main/java/org/nyusziful/pictureorganizer/DAL/DBConnection.java:32`
- Current mitigation: None — `.gitignore` does not exclude these files.
- Recommendations: Move credentials to a `local.properties` (git-ignored) or environment variables, read via `System.getProperty`; delete `old.hibernate.cfg.xml` entirely; remove hard-coded credentials from `DBConnection`.

**SQL injection vectors in `DBConnection` and `ImageDAOImplHib.merge`:**
- Risk: String-concatenated SQL including user-derived filenames and paths (no parameterisation).
- Files: `src/main/java/org/nyusziful/pictureorganizer/DAL/DBConnection.java:93, 148, 153, 157`, `src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/ImageDAOImplHib.java:97-99`
- Current mitigation: Values are internal ints/hashes in the `ImageDAOImplHib.merge` call, but `DBConnection.getFileID`/`saveFile` take `filename` and `path` from disk which can contain quotes.
- Recommendations: Replace with `createNativeQuery(...).setParameter(...)` or prepared statements; delete the dead legacy class.

**Hard-coded absolute filesystem paths in code:**
- Risk: `e:/Pictures.accdb`, `e:\\temp2.jpg`, `E:\\Work\\Testfiles\\...`, `T:\\G\\Pictures\\...` in production source and TODO notes couple the application to one author's workstation.
- Files: `src/main/java/org/nyusziful/pictureorganizer/DAL/DBConnection.java:19`, `src/main/java/org/nyusziful/pictureorganizer/DAL/Entity/JPGMediaFileInstance.java:58` (commented), `src/test/java/org/nyusziful/pictureorganizer/DAL/ExifBlobTest.java:14`, `src/test/java/org/nyusziful/pictureorganizer/Service/Backend/MediaTest.java`, `TODO:2-4`
- Current mitigation: None.
- Recommendations: Move to `CommonProperties`; parameterise tests with temp directories.

**Raw SQL dump files at repo root:**
- Risk: `albums.sql`, `check_migration.sql`, `createDB.sql`, `creating_media_files.sql`, `duplicates.sql`, `migration_of_media_file.sql`, `non-hashed_duplicates.sql`, `original_names.sql`, `query.sql` sit at the project root — unclear which are schema-of-record vs ad-hoc throwaways. `original_names.sql` (26 KB) likely contains real user data (filenames from the author's private photo library).
- Files: repo root `*.sql`; compare with `src/main/resources/SQL/` which has the live schema pieces `schema.sql`, `Drive20231217.sql`, `EmplyTables20231217.sql`, `VersionCleanUpHelper.sql`, `duplicateSchema.sql`.
- Current mitigation: None — files are committed.
- Recommendations: Move dev-helper SQL to `docs/sql/` or outside the repo; scrub `original_names.sql` for PII before it stays in git history.

**Binary blob `DSC08806.jpg.bak` (3.4 MB) committed at root:**
- Risk: Personal photo in the repo; likely bloats clones and could contain private EXIF metadata.
- Files: `DSC08806.jpg.bak`
- Current mitigation: None.
- Recommendations: Remove and add to `.gitignore`; purge from history if it is actually private.

## Performance Bottlenecks

**`EAGER` fetch on `MediaFileVersion` relations:**
- Problem: Three `FetchType.EAGER` associations on `MediaFileVersion` (`parent`, `mediaFile`, `media` via `@OneToMany`). Loading one version triggers a cascade of queries for every sibling version via the collection.
- Files: `src/main/java/org/nyusziful/pictureorganizer/DAL/Entity/MediaFileVersion.java:29-56`
- Cause: Default eager mapping; combined with the tree-walking `loadDirectoryVersions` that calls `getById` per node, this is an N+1 generator.
- Improvement path: Switch to `LAZY` and explicit `JOIN FETCH` in the version-tree query; consider DTO projection for `VersionDTO` building.

**`MediaGeneralDAOImplHib.loadDirectoryVersions` is O(n²) over folders and versions:**
- Problem: Three nested passes over `versionToParent.keySet()` and `folders[] × folders[]` pairwise compares (`loadDirectoryVersionStatus`).
- Files: `src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/MediaGeneralDAOImplHib.java:72-116`, `119-232`
- Cause: Algorithm builds collision graph by pairwise set comparisons.
- Improvement path: Build an inverted index from folder set -> versions once; sort versions by folder-set signature.

**Per-entity transaction commit / close on every DAO call:**
- Problem: `CRUDDAOImpHib` commits and closes the EntityManager for every single `getById`, `persist`, `merge`, `delete` unless the caller passes `batch=true`. Hibernate caches are invalidated constantly.
- Files: `src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/CRUDDAOImpHib.java:30-52, 61-81, 120-138, 146-167, 190-209`
- Cause: Implementation predates any unit-of-work abstraction.
- Improvement path: Move transaction boundary up to the service/controller; reuse a single `EntityManager` per UI action; the `batch=true` escape-hatch pattern is already there but the default is wrong.

**`c3p0.unreturnedConnectionTimeout=2` (seconds):**
- Problem: Any operation longer than 2 s loses its connection with a stack-trace dump (`debugUnreturnedConnectionStackTraces=true`).
- Files: `src/main/resources/c3p0.properties:3`
- Cause: Kept from debug session.
- Improvement path: Bump to 60 s or remove; disable stack-trace dumping in production.

**Hibernate SQL logging left at DEBUG/TRACE:**
- Problem: `org.hibernate.SQL` at `debug`, `org.hibernate.orm.jdbc.bind` and `org.hibernate.stat` at `trace`, `com.mchange` at `DEBUG`. Every parameter bind is written to stdout and to the `logs/logAllfile.log` rolling file.
- Files: `src/main/resources/log4j2.xml:22-40` (matches the WHATSNOW capture of prod-like trace logs)
- Cause: Recent debug session left on.
- Improvement path: Switch root loggers back to `INFO`/`WARN` after the Hibernate v6 upgrade stabilises.

## Fragile Areas

**Version tree code paths in `SummaryController` and `MediaGeneralDAOImplHib`:**
- Files: `src/main/java/org/nyusziful/pictureorganizer/UI/Contoller/SummaryController.java:119-225`, `src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/MediaGeneralDAOImplHib.java:72-232`, `src/main/java/org/nyusziful/pictureorganizer/DTO/VersionDTO.java`
- Why fragile: Algorithm relies on `NULL` parent_id semantics, folder set comparison by reference equality, silent early-return when multiple versions collide. Rewritten in the un-reviewed `b6bc686` commit.
- Safe modification: Do not touch without first adding a golden-data integration test against a known DB snapshot.
- Test coverage: Zero — no tests touch these classes.

**`JPAConnection` singleton + `DBMode` static switch:**
- Files: `src/main/java/org/nyusziful/pictureorganizer/DAL/JPAConnection.java:14-32`
- Why fragile: `setMode` only affects the URL via string concatenation at constructor time; once the singleton is built, switching mode has no effect. `PictureOrganizer.start` sets `DEV`, tests set `TEST`, but the constructor runs lazily — ordering bugs are easy to introduce.
- Safe modification: Always set mode before any `JPAConnection.getInstance()` call; or refactor to take mode as constructor argument.
- Test coverage: None.

**Exif write path `ExifReadWriteIMR` / `ExifReadWriteET`:**
- Files: `src/main/java/org/nyusziful/pictureorganizer/Service/ExifUtils/ExifReadWriteIMR.java`, `src/main/java/org/nyusziful/pictureorganizer/Service/ExifUtils/ExifReadWriteET.java`
- Why fragile: Mixes native `metadata-extractor` parsing with `exiftool` external process calls; chunk size 100 with "single files have a different output format" caveat (line 28 of `ET`). TODO comments littered throughout (`ExifReadWriteIMR.java:170` "thumbnail meta should be separated").
- Safe modification: Add an integration test per format (JPG, ARW, DNG, MP4, MTS) before any refactor.
- Test coverage: `ExifReadTest`, `ExifReadWriteTest`, `ExifWriteTest` all contain `@Ignore` or `fail("The test case is a prototype.")`.

**`TIFFHash` with "kills it" TODO on MakerNote:**
- Files: `src/main/java/org/nyusziful/pictureorganizer/Service/Hash/TIFFHash.java:225-237`
- Why fragile: Hash computation recurses into a fixed whitelist of SubIFD tag ids; the MakerNote branch is marked as crashing; a `System.out.println("!!!!!!!!!!")` debug print remains on line 237.
- Safe modification: Feed known-hash golden files through `JPGHashTest`/`HashTest` on every change; remove the debug `println`.
- Test coverage: Partial (`src/test/java/org/nyusziful/pictureorganizer/Service/Hash/`).

**`PresetUseCases` as a 898-line orchestrator:**
- Files: `src/main/java/org/nyusziful/pictureorganizer/Main/PresetUseCases.java`
- Why fragile: Mix of Swing (`JProgressBar`, `JOptionPane`) and JavaFX UI, direct `System.out.println` progress reporting, contains the NPE branch at line 297.
- Safe modification: Extract each `public static void ...` method into its own use-case class first; add smoke tests.
- Test coverage: None.

**`HibConnection` leftover next to `JPAConnection`:**
- Files: `src/main/java/org/nyusziful/pictureorganizer/DAL/HibConnection.java`, imported by `src/main/java/org/nyusziful/pictureorganizer/Main/PictureOrganizer.java:10`
- Why fragile: Two independent session lifecycles; only `JPAConnection` is really used, but `HibConnection` import is still present and its singleton may be constructed accidentally.
- Safe modification: Delete `HibConnection` together with the stale import.
- Test coverage: None.

## Scaling Limits

**Single global `EntityManager` per singleton:**
- Current capacity: Works for one JavaFX user driving one action at a time.
- Limit: No multi-threaded access; the codebase already has `ProgressLeakingTask` running in the JavaFX Application Thread, so any background indexing will race.
- Scaling path: EM-per-task pattern; move to `EntityManagerFactory.createEntityManager()` in each task, commit and close inside the task.

**In-memory directory scan:**
- Current capacity: `Files.find(path, Integer.MAX_VALUE, ...)` (`MediaFileInstanceService.reOrganizeFilesInSubFolders`) loads all paths into `HashSet<Path>`.
- Limit: Tested only against local photo drives; large NAS trees will blow the heap.
- Scaling path: Stream-process with `forEach`; commit per N files.

**`original_names.sql` 26 KB seed:**
- Current capacity: Starter data for the author's own collection.
- Limit: Anyone else adopting the tool imports someone else's filenames.
- Scaling path: Ship empty schema-only SQL; move example data under `examples/`.

## Dependencies at Risk

**`mysql-connector-java 8.0.29` (groupId `mysql`):**
- Risk: `mysql:mysql-connector-java` was deprecated in favour of `com.mysql:mysql-connector-j` starting with 8.0.31.
- Impact: Security/CVE updates stop; new Maven Central uploads go to the new coordinate.
- Migration plan: Change `<groupId>` to `com.mysql`, `<artifactId>` to `mysql-connector-j`, bump to 8.4.x.

**`javafx 23-ea+22` (early-access build):**
- Risk: EA (early-access) artefact pinned in `pom.xml`; not a stable release.
- Impact: Artefact can be purged from Maven Central; builds become irreproducible.
- Migration plan: Pin to a stable LTS `javafx 21` matching `maven.compiler.target=21`; the README already references `javafx-sdk-21`.

**`junit 4.13.2`:**
- Risk: JUnit 4 is legacy; new Hibernate/JPA tooling assumes JUnit 5.
- Impact: `@Ignore`/`@Test` annotations, `Assert.assertEquals` API; migration to modern `@ParameterizedTest` etc. is blocked.
- Migration plan: Introduce `junit-jupiter` side-by-side via `junit-vintage-engine`, then migrate tests file-by-file.

**`com.garmin.fit 2.0.20.16` as `system` scope with `systemPath`:**
- Risk: Bundled `src/main/resources/fit.jar` with `<scope>system</scope>` is a Maven anti-pattern deprecated in 3.x, unsupported in 4.x.
- Impact: Builds break on Maven 4; jar is invisible to transitive resolution.
- Migration plan: Install to a local repo, or use the official Garmin SDK Maven artefact if one appears.

**`slf4j-api 2.0.5` + `log4j 2.20.0`:**
- Risk: Log4j 2.20.0 predates several 2024 CVEs and feature additions (2.23+).
- Impact: Security patches missed.
- Migration plan: Bump to `log4j 2.24.x` with `log4j-slf4j2-impl`.

**Hibernate `6.4.4.Final`:**
- Risk: 6.4 is no longer the current line (6.6+/7.x exist); combined with the MySQL driver deprecation, the stack is a step behind.
- Impact: Bug fixes and performance improvements missed; the eager-fetch issues above are partly addressable with 6.5+.
- Migration plan: Bump `hibernate.version` to 6.6.x; run the existing integration tests.

## Missing Critical Features

**Local offline save / upload queue:**
- Problem: `DBConnection.uploadLocalChanges` is an empty stub (`//TODO need to implement as a background task`). The application silently requires a reachable MySQL.
- Blocks: Offline operation; any multi-drive workflow where the user is not on LAN.

**New-version creation on exif backup:**
- Problem: `JPGMediaFileInstance.addExifbackup` is fully commented out with note `//TODO this should be a new Version`.
- Blocks: Version tracking when the app itself rewrites EXIF; the feature the version tree was built for is half-wired.

**Re-organise existing files in subfolders:**
- Problem: `MediaFileInstanceService.reOrganizeFilesInSubFolders` body is commented out (line 236 onwards) with `//TODO this is missing`.
- Blocks: Bulk clean-up of existing library after initial import.

**JPG backup-exif "use it" path:**
- Problem: `JPGHash.addBackupExif(File, boolean)` carries `//TODO use it` — it exists but nothing calls it.
- Blocks: Backup-exif provenance chain.

**Parent-setting for Versions (per commit history):**
- Problem: Called out in two separate commits; see Tech Debt section above.
- Blocks: Collision resolution workflow.

**Hash thumbnail separation:**
- Problem: `ExifReadWriteIMR` line 170: `//TODO thumbnail meta should be separated` — thumbnails inflate hashes.
- Blocks: Accurate dedup between full-res and thumbnail-only files.

## Test Coverage Gaps

**Zero coverage of DAL persistence logic:**
- What's not tested: `CRUDDAOImpHib`, `MediaFileDAOImplHib`, `MediaGeneralDAOImplHib`, `MediaFileVersionDAOImplHib`, `ImageDAOImplHib.merge`, `FolderDAOImplHib`, `DriveDAOImplHib`, `MediaDirectoryDAOImplHib`.
- Files: `src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/*.java`
- Risk: Every tech-debt/bug listed above in the version-tree area is only verifiable at runtime against a live MySQL.
- Priority: High.

**Exif tests are prototypes:**
- What's not tested: `ExifReadTest.getTest`, `ExifReadWriteTest.copyXmpTest`/`copyExifTest` all end with `fail("The test case is a prototype.")` or `@Ignore`. `ExifBlobTest` is commented out.
- Files: `src/test/java/org/nyusziful/pictureorganizer/Service/ExifUtils/ExifReadTest.java:101,117`, `ExifReadWriteTest.java:110,120,126,134,140,149`, `ExifWriteTest.java:29`, `src/test/java/org/nyusziful/pictureorganizer/DAL/ExifBlobTest.java:10-23`
- Risk: Exif read/write is the core of every import workflow.
- Priority: High.

**Summary / Version tree has no tests:**
- What's not tested: `SummaryController`, `VersionDTO`, `MediaGeneralDAOImplHib.loadDirectoryVersions`.
- Files: `src/main/java/org/nyusziful/pictureorganizer/UI/Contoller/SummaryController.java`, `src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/MediaGeneralDAOImplHib.java`
- Risk: The feature whose commits ("no clue", "setting parent missing") explicitly admit uncertainty has no safety net.
- Priority: High.

**No JavaFX UI tests:**
- What's not tested: All 11 FXML screens, every controller in `UI/Contoller/` and `UI/Contoller/Rename/`.
- Files: `src/main/java/org/nyusziful/pictureorganizer/UI/**`, `src/main/resources/fxml/*.fxml`
- Risk: Regressions surface only on manual click-through.
- Priority: Medium (add TestFX harness).

**Hash tests do not cover the TIFF MakerNote edge case:**
- What's not tested: The `//TODO kills it` branch at `TIFFHash.java:225-226` for tag 37500 (MakerNote).
- Files: `src/test/java/org/nyusziful/pictureorganizer/Service/Hash/HashTest.java`, `JPGHashTest.java`
- Risk: Hash divergence between runs when a RAW file carries a large MakerNote.
- Priority: Medium.

**Absolute-path test fixtures:**
- What's not tested (reliably): `ExifBlobTest`, `MediaTest`, `HashTest`, `ExifReadTest` hard-code `E:\\Work\\Testfiles\\...` paths.
- Files: `src/test/java/org/nyusziful/pictureorganizer/**`
- Risk: Tests pass only on the author's machine; CI cannot run.
- Priority: High — blocks any CI enablement.

**No error-path coverage:**
- What's not tested: 37 `printStackTrace()` call sites, 17 `catch (RuntimeException e)` blocks in DAO layer, `SessionFactory` build failure in `HibConnection`.
- Files: see Grep counts in analysis.
- Risk: Silent failure modes.
- Priority: Medium.

---

*Concerns audit: 2026-04-19*
