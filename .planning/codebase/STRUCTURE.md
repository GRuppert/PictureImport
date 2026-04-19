# Codebase Structure

**Analysis Date:** 2026-04-19

## Directory Layout

```
PictureImport/
├── pom.xml                    # Maven build descriptor (Java 21, JavaFX 23-ea+22, Hibernate 6.4.4.Final)
├── README.md                  # Minimal readme
├── TODO, WHATSNOW             # Informal dev notes
├── *.sql                      # Loose SQL files for DB exploration/migration (createDB.sql, albums.sql, query.sql, ...)
├── DSC08806.jpg.bak           # Sample image used for hash/EXIF debugging
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/nyusziful/pictureorganizer/     # Single root package
│   │   │       ├── Main/         # JavaFX Application, CommonProperties, PresetUseCases
│   │   │       ├── UI/           # JavaFX controllers, view models, FX tasks
│   │   │       │   ├── Contoller/            # (typo preserved) FXML controllers
│   │   │       │   │   └── Rename/           # Rename-workflow controllers + view-side sets
│   │   │       │   ├── Elements/             # Reusable FX components (DateRangePicker)
│   │   │       │   └── Model/                # View models (TableViewMediaFileInstance et al.)
│   │   │       ├── Service/      # Business logic singletons
│   │   │       │   ├── Comparison/           # Compare / duplicate / listing controllers & helpers
│   │   │       │   ├── ExifUtils/            # EXIF read/write (ExifTool + metadata-extractor)
│   │   │       │   ├── Hash/                 # Custom JPG/TIFF/MP4 hashing + CLI entry points
│   │   │       │   ├── Rename/               # Rename pipeline + FileNameFactory
│   │   │       │   └── TimeShift/            # Timeline view + stripe rendering
│   │   │       ├── DAL/          # Data Access Layer
│   │   │       │   ├── DAO/                  # DAO interfaces + *ImplHib implementations
│   │   │       │   └── Entity/               # JPA @Entity classes
│   │   │       ├── DTO/          # Data transfer objects between Service and UI
│   │   │       └── Model/        # Plain (non-JPA) domain objects
│   │   └── resources/
│   │       ├── META-INF/
│   │       │   └── persistence.xml           # JPA unit mysql-picture
│   │       ├── fxml/                         # All JavaFX scene definitions
│   │       ├── styles/                       # JavaFX CSS (Styles.css, Charts.css)
│   │       ├── SQL/                          # DB seed/migration scripts shipped in the jar
│   │       ├── c3p0.properties               # Connection pool config
│   │       ├── log4j2.xml                    # Logger config
│   │       ├── old.hibernate.cfg.xml         # Legacy hibernate config (unused)
│   │       └── fit.jar                       # Bundled Garmin FIT SDK (system-scope dep)
│   └── test/
│       └── java/
│           └── org/nyusziful/pictureorganizer/
│               ├── DAL/                      # Entity + service-level tests
│               └── Service/                  # Service tests by sub-package (Backend, ExifUtils, Hash, Rename)
├── target/                    # Maven build output (.class, jar-with-dependencies, launch4j exes)
├── .planning/                 # GSD planning artifacts (this directory)
├── .idea/, .vscode/           # IDE configs
└── .git/, .gitignore
```

## Directory Purposes

**`src/main/java/org/nyusziful/pictureorganizer/Main/`:**
- Purpose: Application bootstrap and global user preferences.
- Contains: `PictureOrganizer.java` (JavaFX `Application` subclass + `main()`), `CommonProperties.java` (singleton over `java.util.prefs.Preferences`), `PresetUseCases.java`.
- Key files: `src/main/java/org/nyusziful/pictureorganizer/Main/PictureOrganizer.java`, `src/main/java/org/nyusziful/pictureorganizer/Main/CommonProperties.java`.

**`src/main/java/org/nyusziful/pictureorganizer/UI/`:**
- Purpose: JavaFX presentation layer.
- Contains: FXML controller classes, view models, custom controls, background-task base classes, `Progress`/`CurrentTask` singletons, UI-side `StaticTools`.
- Key files: `UI/Contoller/MainController.java`, `UI/Contoller/SummaryController.java`, `UI/Contoller/Rename/MediaFileTableViewController.java`, `UI/ProgressLeakingTask.java`, `UI/Progress.java`, `UI/StaticTools.java`, `UI/Model/TableViewMediaFileInstance.java`, `UI/Model/RenameTableViewMediaFileInstance.java`.
- Note: the package name is `Contoller` (missing `r`) — preserve when adding files.

**`src/main/java/org/nyusziful/pictureorganizer/Service/`:**
- Purpose: Domain services. Each entity-family has a singleton `*Service` that coordinates DAOs and wraps them in business operations.
- Sub-packages:
  - `Service/Comparison/` — file comparison, duplicates, listing (controllers loaded from `fxml/comparePanel.fxml`, `compareTableView.fxml`).
  - `Service/ExifUtils/` — `ExifService`, `ExifReadWriteIMR` (metadata-extractor), `ExifReadWriteET` (ExifTool shell).
  - `Service/Hash/` — custom hashing pipeline (`MediaFileHash`, `JPGHash`, `TIFFHash`, `MP4Hash`, plus CLI mains `HashToFile`, `VerifyHash`).
  - `Service/Rename/` — renaming pipeline (`RenameService`, `FileNameFactory`, `AnalyzingMediaFile`).
  - `Service/TimeShift/` — timeline stripes view (`TimeLine`, `Stripes`, `Picture`).
- Key singletons: `MediaFileInstanceService.java`, `MediaFileVersionService.java`, `MediaFileService.java`, `MediaDirectoryService.java`, `MediaFileGeneralService.java`, `DriveService.java`, `FolderService.java`, `ImageService.java`.

**`src/main/java/org/nyusziful/pictureorganizer/DAL/`:**
- Purpose: Persistence.
- Contains: `JPAConnection.java` (active bootstrap, selects DEV/TEST/PROD schema), `HibConnection.java` (legacy native Hibernate bootstrap), `DBConnection.java` (legacy raw JDBC).
- `DAL/DAO/`: One interface (`XxxDAO`) + one Hibernate implementation (`XxxDAOImplHib`) per entity family. Shared base: `CRUDDAO<T>` interface + `CRUDDAOImpHib<T>` abstract implementation. Marker interface `HasID`. Unused legacy local impl: `MediafileDAOImplLocal`.
- `DAL/Entity/`: JPA `@Entity` classes. `TrackingEntity` is a `@MappedSuperclass`. `m_old.java` is a legacy/abandoned class.

**`src/main/java/org/nyusziful/pictureorganizer/DTO/`:**
- Purpose: Lightweight transport objects between Service and UI; aggregate query results.
- Contains: `MediafileInstanceDTO`, `Meta`, `ExifDTO`, `ImageDTO`, `FolderDTO`, `ProgressDTO`, the `SummaryDTO` interface and implementations (`DirectorySummaryDTO`, `FolderSummaryDTO`, `FileSummaryDTO`), `DirectoryVersionSummaryDTO`, `VersionDTO`.

**`src/main/java/org/nyusziful/pictureorganizer/Model/`:**
- Purpose: Non-JPA domain helpers.
- Contains: `MediaDirectoryInstance.java` (parses `YYYY-MM-DD - YYYY-MM-DD Label` folder names), `RenameFile.java`.

**`src/main/resources/fxml/`:**
- Purpose: JavaFX scene graphs. Controller class is set with `fx:controller="org.nyusziful.pictureorganizer..."` inside each file.
- Files: `main.fxml`, `summary.fxml`, `mediaDirectoryList.fxml`, `folderList.fxml`, `tablePanel.fxml`, `mediaFileTableView.fxml`, `metaTableView.fxml`, `meta2TableView.fxml`, `comparePanel.fxml`, `compareTableView.fxml`, `timeline.fxml`.

**`src/main/resources/styles/`:**
- Purpose: JavaFX CSS stylesheets attached to scenes.
- Files: `Charts.css` (applied globally in `PictureOrganizer.start`), `Styles.css`.

**`src/main/resources/SQL/`:**
- Purpose: SQL assets shipped with the jar (schema helpers, seed dumps).
- Files: `schema.sql`, `Drive20231217.sql`, `EmplyTables20231217.sql`, `VersionCleanUpHelper.sql`, `duplicateSchema.sql`.

**`src/main/resources/META-INF/`:**
- Purpose: Jakarta Persistence configuration.
- Key file: `persistence.xml` (unit name `mysql-picture`, provider `org.hibernate.jpa.HibernatePersistenceProvider`, explicit `<class>` list for all entities, `hibernate.hbm2ddl.auto=validate`).

**`src/test/java/org/nyusziful/pictureorganizer/`:**
- Purpose: JUnit 4 tests.
- Sub-packages mirror `main`: `DAL/` (entity/service tests), `Service/Backend/`, `Service/ExifUtils/`, `Service/Hash/`, `Service/Rename/`.
- Key files: `DAL/ExifBlobTest.java`, `DAL/ImageServiceTest.java`, `DAL/MediaDirectoryServiceTest.java`, `Service/ExifUtils/ExifReadWriteTest.java`, `Service/Hash/HashTest.java`, `Service/Hash/JPGHashTest.java`, `Service/Rename/fileRenamerTest.java`, `Service/Rename/mediaDirectoryTest.java`.

**`target/`:**
- Purpose: Maven build output (generated).
- Contains: compiled classes, `*-jar-with-dependencies.jar`, `hash.exe` / `verify.exe` (launch4j), `classes/fxml/*` copies of FXML.
- Committed: No.

## Key File Locations

**Entry Points:**
- `src/main/java/org/nyusziful/pictureorganizer/Main/PictureOrganizer.java` — JavaFX main class (also declared in `pom.xml:18`).
- `src/main/java/org/nyusziful/pictureorganizer/Service/Hash/HashToFile.java` — Hasher CLI (wrapped as `target/hash.exe` by launch4j).
- `src/main/java/org/nyusziful/pictureorganizer/Service/Hash/VerifyHash.java` — Verifier CLI (`target/verify.exe`).
- `src/main/resources/fxml/main.fxml` — root FXML loaded at startup.

**Configuration:**
- `pom.xml` — Maven coords, dependencies, plugins.
- `src/main/resources/META-INF/persistence.xml` — JPA unit + MySQL credentials.
- `src/main/resources/c3p0.properties` — connection pool tuning.
- `src/main/resources/log4j2.xml` — logging.
- `.gitignore` — ignores `target/`, IDE files.

**Core Logic:**
- `src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/CRUDDAOImpHib.java` — generic CRUD.
- `src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/MediaGeneralDAOImplHib.java` — native-SQL aggregates for the Summary / Version trees.
- `src/main/java/org/nyusziful/pictureorganizer/Service/MediaFileInstanceService.java` — orchestrates file import/sync/reorg.
- `src/main/java/org/nyusziful/pictureorganizer/Service/MediaFileGeneralService.java` — facade for directory/version summaries.
- `src/main/java/org/nyusziful/pictureorganizer/Service/MediaFileVersionService.java` — version-tree mutations.
- `src/main/java/org/nyusziful/pictureorganizer/UI/Contoller/MainController.java` — top-level UI wiring and task launcher.
- `src/main/java/org/nyusziful/pictureorganizer/UI/Contoller/SummaryController.java` — Collision tree + Version tree.

**Testing:**
- `src/test/java/org/nyusziful/pictureorganizer/` — JUnit 4 tests.
- `pom.xml:40` — `maven-surefire-plugin` configuration.

## Naming Conventions

**Packages:**
- Root: `org.nyusziful.pictureorganizer`.
- Sub-packages use PascalCase, not the conventional lowercase (`Main`, `UI`, `Service`, `DAL`, `DTO`, `Model`, `UI.Contoller`, `UI.Model`, `UI.Elements`, `Service.ExifUtils`, `Service.Hash`, `Service.Rename`, `Service.TimeShift`, `Service.Comparison`, `DAL.DAO`, `DAL.Entity`).
- **Preserve the `Contoller` typo** in `UI.Contoller` — it is spelled that way everywhere including inside `main.fxml`.

**Files / Classes:**
- Entities: singular noun, PascalCase (`MediaFile`, `MediaFileVersion`, `MediaFileInstance`, `Folder`, `Drive`). Subtype variants prefix the media kind (`JPGMediaFile`, `RAWMediaFileVersion`, `VideoMediaFileInstance`, `JPGMedia`, `RAWMedia`).
- DAO interfaces: `<Entity>DAO` (e.g., `MediaFileVersionDAO`). Implementations: `<Entity>DAOImplHib` (e.g., `MediaFileVersionDAOImplHib`). Shared base: `CRUDDAO<T>`, `CRUDDAOImpHib<T>` (note: `Imp`, not `Impl`).
- Services: `<Entity>Service`, always singleton with `public static <Entity>Service getInstance()`.
- DTOs: suffix `DTO` (`MediafileInstanceDTO`, `DirectorySummaryDTO`, `FolderSummaryDTO`, `VersionDTO`). Exceptions: `Meta`, `MetaProp`.
- FXML controllers: suffix `Controller` (`MainController`, `SummaryController`, `MediaFileTableViewController`, `TablePanelController`).
- Background tasks: inner classes of a controller or standalone classes ending in `Task` (`ReadTask`, `SyncTask`, `ReorganizatorTask`, `RenamingTask`, `ProgressLeakingTask`).
- Static-helper classes: `StaticTools` — note this name is reused in three packages (`UI`, `Service.Comparison`, `Service.Rename`, `Service.TimeShift`); always import-qualify to disambiguate.

**FXML files:** lowerCamelCase (`main.fxml`, `summary.fxml`, `mediaDirectoryList.fxml`, `mediaFileTableView.fxml`, `comparePanel.fxml`). Loaded via `getClass().getResource("/fxml/<name>.fxml")`.

**SQL files:** freeform; resource SQL lives under `src/main/resources/SQL/` and ad-hoc SQL sits at the project root (`query.sql`, `createDB.sql`, `albums.sql`, `duplicates.sql`, etc.).

**Methods:** camelCase. Getters follow JavaBean conventions (required for JPA + JavaFX property binding). Boolean getters often use `is*` or `get*` interchangeably.

**Database tables / columns:** snake_case via `@Table(name="media_file_version")` and `@Column(name="date_stored_utc")`. Single-table inheritance uses `@DiscriminatorColumn(name = "file_type")` with values `DEF`, plus `JPG`/`RAW`/`VIDEO` on subclasses.

## Where to Add New Code

**New entity + persistence:**
1. Create `<Name>.java` under `src/main/java/org/nyusziful/pictureorganizer/DAL/Entity/` with JPA annotations; extend `TrackingEntity` if you want creator/updater audit columns.
2. Register it in `src/main/resources/META-INF/persistence.xml` (the unit uses an explicit `<class>` list — classes not listed are invisible to Hibernate).
3. Create `<Name>DAO.java` (interface extending `CRUDDAO<Name>`) and `<Name>DAOImplHib.java` (`extends CRUDDAOImpHib<Name> implements <Name>DAO`) under `src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/`.
4. Because `hbm2ddl.auto=validate`, ship a corresponding DDL change via the existing SQL scripts (`createDB.sql` or a new file in `src/main/resources/SQL/`).

**New service:**
- Add `<Name>Service.java` under `src/main/java/org/nyusziful/pictureorganizer/Service/` (or a sub-package if it fits `Comparison` / `ExifUtils` / `Hash` / `Rename` / `TimeShift`).
- Implement the singleton pattern: private constructor, `private static <Name>Service instance`, `public static <Name>Service getInstance()`.
- Inject DAO via `new <Name>DAOImplHib()` in the constructor (DAOs are created per-service, not shared).

**New DTO:**
- Add `<Name>DTO.java` (or a plain object if it's not for the UI boundary) under `src/main/java/org/nyusziful/pictureorganizer/DTO/`.
- If it participates in a `TreeView` cell, implement `SummaryDTO` so it plays with `SummaryController.SummaryTreeCell`.

**New FXML view + controller:**
- FXML: add `src/main/resources/fxml/<name>.fxml` with `fx:controller="org.nyusziful.pictureorganizer.UI.Contoller.<Name>Controller"` (mind the `Contoller` typo).
- Controller: add `src/main/java/org/nyusziful/pictureorganizer/UI/Contoller/<Name>Controller.java` implementing `javafx.fxml.Initializable`.
- Load from another controller with `new FXMLLoader(getClass().getResource("/fxml/<name>.fxml")).load()`.

**New view model:**
- Put observable-wrapped models under `src/main/java/org/nyusziful/pictureorganizer/UI/Model/`.
- If it feeds the main media table, implement `TableViewMediaFileInstance`.

**New background task:**
- Subclass `UI/ProgressLeakingTask<V>` (or `UI/Model/RenamingTask<V>` if it processes a `Collection<File>`).
- Start it from a controller with `new Thread(task).start()`; bind `progressIndicator.progressProperty()` to `task.progressProperty()` and wrap UI mutations in `Platform.runLater`.

**New CLI executable:**
- Add the `main` class under `src/main/java/org/nyusziful/pictureorganizer/Service/Hash/` (by existing precedent) or create a new sub-package.
- Register a `launch4j-maven-plugin` execution block in `pom.xml` mirroring the existing `Hasher-exe` / `Verify-exe` entries (see `pom.xml:64-128`).

**Utility helpers:**
- File/type helpers belong in `UI/StaticTools.java` (already houses `supportedFileType`, `defaultImportDirectories`, `getDir`, etc.).
- Service-specific helpers go into the sub-package's own `StaticTools.java`.

**Tests:**
- Mirror the `main` package under `src/test/java/org/nyusziful/pictureorganizer/...` using JUnit 4.
- Tests often touch the live DEV database — call `JPAConnection.setMode(JPAConnection.DBMode.DEV)` before exercising DAOs (pattern from `MediaGeneralDAOImplHib.main`).

## Special Directories

**`target/`:**
- Purpose: Maven output (compiled classes, shaded jar, launch4j exes, resource copies).
- Generated: Yes.
- Committed: No (covered by `.gitignore`).

**`.planning/`:**
- Purpose: GSD planning artifacts including `.planning/codebase/` (this mapping output).
- Generated: Yes (by GSD commands).
- Committed: Yes (project convention).

**`.idea/`, `.vscode/`:**
- Purpose: IDE project metadata.
- Generated: Yes.
- Committed: `.idea/` is partially committed; see `.gitignore`.

**Project-root SQL files (`query.sql`, `albums.sql`, `createDB.sql`, `duplicates.sql`, `migration_of_media_file.sql`, `non-hashed_duplicates.sql`, `original_names.sql`, `check_migration.sql`, `creating_media_files.sql`):**
- Purpose: Ad-hoc queries and migration scripts kept outside the jar classpath.
- Generated: No (hand-written).
- Committed: Yes.

**`DSC08806.jpg.bak`:**
- Purpose: Sample image (~3.4 MB) used as fixture for hash / EXIF tests at the project root.
- Committed: Yes.

**`src/main/resources/fit.jar`:**
- Purpose: Garmin FIT SDK declared as `system`-scope dependency in `pom.xml:197-202`; packaged with the jar-with-dependencies.
- Committed: Yes.

---

*Structure analysis: 2026-04-19*
