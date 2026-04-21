# Architecture

**Analysis Date:** 2026-04-19

## Pattern Overview

**Overall:** Layered MVC desktop application (JavaFX FXML-based view + singleton Services + JPA/Hibernate DAOs)

**Key Characteristics:**
- Classic three-tier split: **UI (JavaFX/FXML)** → **Service (domain logic, singletons)** → **DAL (JPA/Hibernate DAOs + Entities)** with **DTO** objects crossing layer boundaries.
- JPA/Hibernate persistence with `SINGLE_TABLE` inheritance for media type families (generic, JPG, RAW, Video).
- Singleton service locator pattern: every service and the `JPAConnection` are accessed via static `getInstance()`.
- Background work runs through JavaFX `Task` subclasses (`ProgressLeakingTask`, `RenamingTask`) with `Platform.runLater` for UI mutations.
- Entry point bootstraps Hibernate via `persistence.xml`; UI wiring is loaded from `/fxml/*.fxml`.

## Layers

**UI (presentation):**
- Purpose: FXML scene graphs, controllers, view models (observable wrappers), view-specific helpers.
- Location: `src/main/java/org/nyusziful/pictureorganizer/UI/`
- Contains: FXML controllers (`UI/Contoller/*`), renderable view models (`UI/Model/*`), reusable JavaFX elements (`UI/Elements/*`), static UI utilities (`UI/StaticTools.java`), global singletons for progress/task (`UI/Progress.java`, `UI/CurrentTask.java`), base task types (`UI/ProgressLeakingTask.java`).
- Depends on: Service singletons, DTOs, `Main/CommonProperties`.
- Used by: `PictureOrganizer` (the JavaFX `Application`).
- Note: the directory name is literally `Contoller` (typo in code) and must be preserved.

**Service (domain / business logic):**
- Purpose: Orchestrate DAO calls, build domain DTOs, implement import/rename/compare/hash workflows.
- Location: `src/main/java/org/nyusziful/pictureorganizer/Service/`
- Contains:
  - Entity-oriented singletons: `MediaFileService`, `MediaFileVersionService`, `MediaFileInstanceService`, `MediaDirectoryService`, `MediaFileGeneralService`, `DriveService`, `FolderService`, `ImageService`.
  - Workflow sub-packages: `Service/Comparison/`, `Service/ExifUtils/`, `Service/Hash/`, `Service/Rename/`, `Service/TimeShift/`.
  - Cross-cutting helper: `Service/Services.java` (backup drive detection).
- Depends on: `DAL/DAO/*` (interfaces + Hib implementations), `DAL/Entity/*`, `DTO/*`.
- Used by: UI controllers and `UI/ProgressLeakingTask` subclasses.

**DAL (Data Access Layer):**
- Purpose: Entity mappings (JPA), DAO interfaces with Hibernate implementations, JPA bootstrap.
- Location: `src/main/java/org/nyusziful/pictureorganizer/DAL/`
- Contains:
  - Entity classes in `DAL/Entity/` (JPA `@Entity`).
  - DAO interfaces + `*ImplHib` classes in `DAL/DAO/`.
  - Connection bootstraps: `DAL/JPAConnection.java` (active), `DAL/HibConnection.java` (legacy native Hibernate), `DAL/DBConnection.java` (legacy raw JDBC).
- Depends on: Jakarta Persistence, Hibernate Core, MySQL Connector/J, `persistence.xml`.
- Used by: Services (never directly by UI).

**DTO (transfer objects):**
- Purpose: Transfer data between Service and UI without exposing managed JPA entities; aggregate query results.
- Location: `src/main/java/org/nyusziful/pictureorganizer/DTO/`
- Key DTOs: `MediafileInstanceDTO`, `Meta`, `ExifDTO`, `ImageDTO`, `FolderDTO`, `ProgressDTO`, `SummaryDTO` (interface), `DirectorySummaryDTO`, `FolderSummaryDTO`, `FileSummaryDTO`, `DirectoryVersionSummaryDTO`, `VersionDTO`.
- Depends on: DAL entities (some DTOs reference `Folder`, `MediaFileVersion`, `MediaDirectory`).

**Model (non-JPA domain helpers):**
- Purpose: Lightweight domain objects not tied to persistence.
- Location: `src/main/java/org/nyusziful/pictureorganizer/Model/`
- Contains: `MediaDirectoryInstance.java` (parses `YYYY-MM-DD - YYYY-MM-DD Label` folder names into dates), `RenameFile.java`.

**Main (bootstrap):**
- Purpose: JavaFX application entry point and user preferences.
- Location: `src/main/java/org/nyusziful/pictureorganizer/Main/`
- Contains: `PictureOrganizer.java` (extends `javafx.application.Application`), `CommonProperties.java` (singleton backed by `java.util.prefs.Preferences`), `PresetUseCases.java`.

## Data Flow

**Application startup:**

1. `PictureOrganizer.main` → `Application.launch(args)`.
2. `PictureOrganizer.start(Stage)` sets `JPAConnection.setMode(DBMode.DEV)` (see `src/main/java/org/nyusziful/pictureorganizer/Main/PictureOrganizer.java:29`).
3. Loads FXML `/fxml/main.fxml`, which instantiates `UI.Contoller.MainController`.
4. `MainController.initialize` reads `CommonProperties`, attaches progress bindings to `Progress.getInstance()`, and embeds `/fxml/summary.fxml` + `/fxml/mediaDirectoryList.fxml` into the `BorderPane`.
5. First DAO call lazily builds the JPA `EntityManagerFactory` (unit `mysql-picture` from `src/main/resources/META-INF/persistence.xml`).

**Import / rename workflow:**

1. User clicks a button in `main.fxml` → `MainController.handle*ButtonAction`.
2. Directories are chosen via `UI/StaticTools.getDir` (JavaFX `DirectoryChooser`).
3. A `ReadTask`/`ReorganizatorTask`/`SyncTask` (inner classes in `MainController`, all `ProgressLeakingTask` subclasses) is started on a new thread.
4. The task calls `MediaFileInstanceService.getInstance()` to walk files, hash, and build `MediafileInstanceDTO` objects.
5. `MediaFileInstanceService` in turn calls `MediaDAOImplHib`, `MediaFileInstanceDAOImplHib`, `FolderService`, `DriveService`, and `ExifService`.
6. DTOs are wrapped into `RenameTableViewMediaFileInstance` view models and added to `MediaFileSet.getDataModel()` via `Platform.runLater`.
7. `Progress.getInstance()` updates the `ProgressIndicator` and `AreaChart` in the bottom bar.

**Summary / version-tree rendering (SummaryController):**

1. `SummaryController.initialize` calls `MediaFileGeneralService.getInstance().loadDirectoryVersionStatus()`.
2. Service delegates to `MediaGeneralDAOImplHib.loadDirectoryVersionStatus`, which runs a native SQL aggregate over `media_file_version` / `media_file` / `folder`.
3. Each row becomes a `DirectorySummaryDTO` that classifies ids into `"Collision"` or `"NoCollision"` buckets.
4. The directory tree (`TreeView<SummaryDTO> summaryTree` — the "Collision tree") is populated on demand: expanding a `DirectorySummaryDTO` fetches `FolderSummaryDTO`s via `loadDirectoryVersionStatus(Collection<Integer> mediaFileVersionIds)` which computes `MATCH / DISTINCT / MIX` between folders sharing versions (see `FolderSummaryDTO.compareWith`).
5. Selecting an item triggers `updateVTree`, which calls `MediaFileGeneralService.loadDirectoryVersions(mediaDirectoryId)` → `MediaGeneralDAOImplHib.loadDirectoryVersions` to build a `VersionDTO` forest (the "Version tree", rooted in `TreeView<VersionDTO> versionTree`) from the parent/child graph of `MediaFileVersion` entities.
6. Context menus in `SummaryTreeCell` invoke `MediaFileVersionService.setAsOriginal / setAsInvalid / setParent` to mutate version state.

**State Management:**
- Global app state: `Main/CommonProperties` (paths, time zone, COPY/MOVE mode, picture set) persisted via `java.util.prefs.Preferences`.
- Progress: `UI/Progress` singleton bound bidirectionally to UI `ProgressIndicator` and `AreaChart` series.
- Data model for tables: `UI/Contoller/Rename/MediaFileSet` holds an `ObservableList<TableViewMediaFileInstance>` consumed by `mediaFileTableView.fxml`.
- Persistence context: `JPAConnection` holds a single `EntityManager` per instance; `CRUDDAOImpHib` opens a transaction per call unless `batch=true` is passed.

## Key Abstractions

**Version tree:**
- Purpose: Represents successive edits of the same logical image across folders. Each `MediaFileVersion` row has a `parent_id` pointing to the previous version of the same `MediaFile`; the root of a chain is the "original" version (`parent == null`, `isOriginal()` returns `true`). A `NULL parent_id` is the intended state for an original, not a missing link.
- Entity: `src/main/java/org/nyusziful/pictureorganizer/DAL/Entity/MediaFileVersion.java` (self-referencing `@ManyToOne parent`).
- Parent linkage: **Manual curation, not auto-inference.** The import path creates `MediaFileVersion` rows without assigning `parent`; the only code path that writes `parent_id` for a non-original is the "Set as Parent" context-menu action in `SummaryController.SummaryTreeCell` (`src/main/java/org/nyusziful/pictureorganizer/UI/Contoller/SummaryController.java:194-225`), which walks sibling tree items and calls `MediaFileVersionService.setParent`. A `FileSummaryDTO`-level variant of this menu is stubbed but not yet wired.
- View DTO: `src/main/java/org/nyusziful/pictureorganizer/DTO/VersionDTO.java` — a node groups a set of `Folder`s that host the same version bundle; `specificVersions` (currently populated only on root nodes) lists versions unique to that folder set; DTO-level parent/children are rebuilt in `MediaGeneralDAOImplHib.loadDirectoryVersions` (`src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/MediaGeneralDAOImplHib.java:119`), which runs two passes: first building root `VersionDTO`s from `parent_id IS NULL` rows, then attaching child `VersionDTO`s by walking `versionToParent` and a final reconciliation pass that matches remaining orphans to any node whose version map contains their parent MFV.
- Rendering: `SummaryController.versionTree` with `VersionTreeCell` (`src/main/java/org/nyusziful/pictureorganizer/UI/Contoller/SummaryController.java:239`); tree expansion uses `addVTreeLevel` recursion over `VersionDTO.getChildren()`, which is populated reciprocally by `VersionDTO.setParent`.
- Mutation API: `MediaFileVersionService.setAsOriginal`, `setAsInvalid`, `setParent` → `MediaFileVersionDAOImplHib`; `setParent` uses `MediaFileVersion.isAncestor` as a cycle-detection guard. See `.planning/codebase/CONCERNS.md` for known issues in this feature area.

**Collision tree:**
- Purpose: Highlights `MediaDirectory`s where the same logical `MediaFile` exists with more than one non-merged version (i.e., version conflict across backup folders).
- Detection: SQL in `MediaGeneralDAOImplHib.loadDirectoryVersionStatus()` — groups by `media_file_id` with `count(distinct mfv.id) > 1` → bucket `"Collision"`, else `"NoCollision"`.
- DTO: `DirectorySummaryDTO.idsMap` keys `"Collision"` / `"NoCollision"` with sets of media-file ids; `DirectoryVersionSummaryDTO` is a simpler variant split by `collisionVersionIds` / `noCollisionVersionIds`.
- Rendering: `SummaryController.summaryTree` (the left-hand collision tree). Only directories with a non-empty `"Collision"` set are lazily expandable; expansion populates `FolderSummaryDTO` children via `loadDirectoryVersionStatus(Collection<Integer>)` (`src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/MediaGeneralDAOImplHib.java:72`), which cross-compares folders using `FolderSummaryDTO.compareWith` returning `MATCH` / `DISTINCT` / `MIX`.
- Resolution actions: `Set as original`, `Set as invalid`, `Set as Parent` menu items on folder/file cells (`SummaryController.SummaryTreeCell.updateItem`).

**Media type inheritance hierarchy:**
- Purpose: Single-table inheritance split across four parallel families.
- Pattern: Each family has an abstract-ish base + typed subclasses sharing a table via `@DiscriminatorColumn("file_type")`.
- Tables: `media_file` (base `MediaFile` + `JPGMediaFile`, `RAWMediaFile`, `VideoMediaFile`), `media_file_version` (`MediaFileVersion` + JPG/RAW/Video variants), `media_file_instance` (`MediaFileInstance` + variants), `media_image` (`Media` + `JPGMedia`, `RAWMedia`, `VideoMedia`).
- Concept: `MediaFile` = logical file identity; `MediaFileVersion` = a specific content hash/edit; `MediaFileInstance` = a physical copy of a version located at `Folder + filename` on a `Drive`; `Media` = image payload descriptor (orientation, GPS, keywords).
- Discriminator dispatch: Services use `supportedRAWFileType` / `supportedJPGFileType` / `supportedVideoFileType` (`UI/StaticTools`) to pick the correct subclass in `MediaFileService.createMediaFile` and `MediaFileVersionService.createMediaFileVersion`.

**Singleton service locator:**
- Pattern: `public static XxxService getInstance()` with lazy init on each service class.
- Examples: `MediaFileGeneralService.getInstance()` (`src/main/java/org/nyusziful/pictureorganizer/Service/MediaFileGeneralService.java:26`), `MediaFileVersionService.getInstance()`, `MediaFileService.getInstance()`, `MediaFileInstanceService.getInstance()`, `MediaDirectoryService.getInstance()`, `JPAConnection.getInstance()`, `Progress.getInstance()`, `CurrentTask.getInstance()`, `CommonProperties.getInstance()`.

**Generic CRUD DAO:**
- Interface: `CRUDDAO<T>` (`src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/CRUDDAO.java`) — `getAll`, `getById`, `getByIds`, `persist`, `merge`, `delete`, overloaded with `boolean batch`, plus `flush`, `close`.
- Impl: `CRUDDAOImpHib<T>` (`src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/CRUDDAOImpHib.java`) — resolves `entityBeanType` via reflection from the generic superclass parameter and delegates to the shared `JPAConnection` `EntityManager`. Commits per call unless `batch` is true.
- Concrete DAOs extend it: e.g., `MediaFileDAOImplHib extends CRUDDAOImpHib<MediaFile> implements MediaFileDAO`.

**Tracking entity superclass:**
- `TrackingEntity` (`src/main/java/org/nyusziful/pictureorganizer/DAL/Entity/TrackingEntity.java`) — `@MappedSuperclass` adding `credate`, `creator`, `upddate`, `updater` stamped via `@PrePersist` / `@PreUpdate` using `COMPUTERNAME` env var.

**Progress / background task base classes:**
- `UI/ProgressLeakingTask<V>` extends `javafx.concurrent.Task<V>`; overloads `updateProgress(int, int)`.
- `UI/Model/RenamingTask<V>` extends `ProgressLeakingTask`, carrying a `Collection<File> directories`.
- Subclasses are defined as inner classes in `MainController` (`ReadTask`, `ReorganizatorTask`, `SyncTask`).

## Entry Points

**JavaFX application (primary):**
- Location: `src/main/java/org/nyusziful/pictureorganizer/Main/PictureOrganizer.java`
- Triggers: `main(String[] args)` → `launch(args)`; Maven plugin `javafx-maven-plugin` also uses this (`<mainClass>` in `pom.xml:18`).
- Responsibilities: Set `JPAConnection` mode, load `main.fxml`, size the primary stage, call `JPAConnection.shutdown()` + `CommonProperties.save()` in `stop()`.

**Hasher CLI (launch4j executable):**
- Location: `src/main/java/org/nyusziful/pictureorganizer/Service/Hash/HashToFile.java`
- Triggers: `pom.xml:78` launch4j execution "Hasher-exe" → `target/hash.exe`.
- Responsibilities: Compute hashes for files and write them to an output file (headless).

**Verifier CLI (launch4j executable):**
- Location: `src/main/java/org/nyusziful/pictureorganizer/Service/Hash/VerifyHash.java`
- Triggers: `pom.xml:107` launch4j execution "Verify-exe" → `target/verify.exe` (GUI header).
- Responsibilities: Re-hash files and verify against stored hashes.

**Ad-hoc `main` harnesses (debug):**
- `MediaGeneralDAOImplHib.main` (`src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/MediaGeneralDAOImplHib.java:241`) drives version-tree loading against the DEV DB.
- `MediaFileGeneralService.main`, `DBConnection.main`, `DriveService.main`, `StaticTools.main` — smoke-test helpers, not production entry points.

**FXML controllers (secondary entry points from the scene graph):**
- `UI.Contoller.MainController` — `main.fxml`
- `UI.Contoller.SummaryController` — `summary.fxml`
- `UI.Contoller.MetaTableViewController` — `metaTableView.fxml`
- `UI.Contoller.Meta2TableViewController` — `meta2TableView.fxml`
- `UI.Contoller.Rename.MediaFileTableViewController` — `mediaFileTableView.fxml`
- `UI.Contoller.Rename.TablePanelController` — `tablePanel.fxml`
- `UI.Contoller.Rename.DirectoryViewController` — `folderList.fxml`
- `UI.Contoller.Rename.MediaDirectoryViewController` — `mediaDirectoryList.fxml`
- `Service.Comparison.ComparePanelController` — `comparePanel.fxml`
- `Service.Comparison.CompareTableViewController` — `compareTableView.fxml`
- `Service.TimeShift.TimeLineController` — `timeline.fxml`

## Error Handling

**Strategy:** Mixed; most flows catch `Exception` and either `printStackTrace()` or route through `UI/StaticTools.errorOut(String, Exception)` which opens a Swing `JOptionPane`.

**Patterns:**
- DAO layer: try/commit/catch-rollback/finally-close — see `CRUDDAOImpHib.getAll` (`src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/CRUDDAOImpHib.java:30-53`). Rollback failures are silently swallowed (commented `log.error`).
- UI tasks: `task.setOnFailed(a -> { task.getException().printStackTrace(); progressIndicator.progressProperty().unbind(); resetAction(); })` (`MainController.syncFolders`, `reorganizeFolder`, `ReadFolders`).
- File selection dialogs: failure returns `null` and the action is aborted silently (`MainController.handleImportFromButtonAction`).
- Schema validation is enforced: `persistence.xml` sets `hibernate.hbm2ddl.auto=validate`; a startup schema mismatch aborts `PictureOrganizer.start`.

## Cross-Cutting Concerns

**Logging:**
- Config: `src/main/resources/log4j2.xml` — SLF4J over Log4j2, `STDOUT` + rolling `FILE` appender under `../pictureOrganizer/logs/`.
- Hibernate categories (`org.hibernate.SQL`, `org.hibernate.orm.jdbc.bind`, `org.hibernate.stat`, `org.hibernate.SQL_SLOW`, `org.hibernate.cache`) are set to `debug`/`trace` against `STDOUT`.
- Application code mostly uses `System.out.println` / `e.printStackTrace()` rather than SLF4J loggers; `Service/ExifUtils/ExifReadWriteIMR` is one of the few classes that instantiates `LoggerFactory.getLogger(...)`.

**Validation:**
- Hibernate schema validation (`hbm2ddl.auto=validate`) on startup.
- Directory-name validation for `MediaDirectoryInstance` throws `IllegalArgumentException` when the folder name does not match `YYYY-MM-DD - YYYY-MM-DD Label` (`Model/MediaDirectoryInstance.java:36`).
- File-type whitelist enforced in `UI/StaticTools.supported*FileType`.

**Authentication:**
- Not applicable (single-user desktop app). MySQL credentials are embedded in `src/main/resources/META-INF/persistence.xml` (`user=picture`, `password=picture`).

**Concurrency:**
- All heavy I/O runs on worker threads via `ProgressLeakingTask` + `new Thread(task).start()`.
- UI mutations wrapped in `Platform.runLater`.
- `JPAConnection` is a non-thread-safe singleton sharing a single `EntityManager` across threads — a known fragility the services work around by calling `flush()` / `close()` on commit boundaries.

**Configuration:**
- `src/main/resources/META-INF/persistence.xml` — JPA unit `mysql-picture`, MySQL dialect, `hbm2ddl.auto=validate`, entity list, credentials.
- `src/main/resources/c3p0.properties` — connection pool tuning (`unreturnedConnectionTimeout=2`, stack traces enabled).
- `src/main/resources/log4j2.xml` — logger config.
- `Main/CommonProperties` — user preferences via `Preferences.userNodeForPackage(PictureOrganizer.class)`; persists `InputDirectory` and `OutputDirectory` only.
- `JPAConnection.DBMode` (`TEST` / `DEV` / `PROD`) selects the DB name prefix on the JDBC URL (e.g., `devpictureorganizer`).

---

*Architecture analysis: 2026-04-19*
