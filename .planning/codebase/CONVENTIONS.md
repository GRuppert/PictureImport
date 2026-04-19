# Coding Conventions

**Analysis Date:** 2026-04-19

## Package Structure

**Root package:** `org.nyusziful.pictureorganizer`

**Layer packages (PascalCase, atypical for Java — Java convention is lowercase):**
- `DAL` - Data Access Layer (`DAL.DAO`, `DAL.Entity`)
- `DTO` - Data Transfer Objects
- `Main` - Application entry points and shared globals
- `Model` - Non-persistent domain models
- `Service` - Business logic grouped by domain (`Service.Comparison`, `Service.ExifUtils`, `Service.Hash`, `Service.Rename`, `Service.TimeShift`)
- `UI` - JavaFX controllers and view models (note the typo `UI.Contoller` — preserved for compatibility)

**Rule for new code:** Follow the existing capitalized package segments (`DAL`, `Service.Hash`, etc.). Do NOT introduce lowercase siblings — the inconsistency already exists and mixing would worsen it.

## Naming Patterns

**Files:**
- One public class per file; filename matches class name.
- Most classes: `PascalCase` (e.g., `MediaFileInstanceService.java`, `JPGHash.java`).
- A few tests still use `camelCase` class names (e.g., `fileRenamerTest.java`, `fileVersionTest.java`, `mediaDirectoryTest.java`). Legacy — new tests must use `PascalCase`.
- Legacy abandoned code marked with lowercase prefix: `src/main/java/org/nyusziful/pictureorganizer/DAL/Entity/m_old.java`.

**Classes:**
- `PascalCase` (e.g., `MediaFileVersion`, `CRUDDAOImpHib`).
- DAO contracts suffixed `DAO` (`MediaFileDAO`, `CRUDDAO`).
- DAO Hibernate implementations suffixed `DAOImplHib` or `ImplHib` (`MediaFileDAOImplHib`, `CRUDDAOImpHib` — note the abbreviation is inconsistent: `ImpHib` vs `ImplHib`). New implementations should use `ImplHib`.
- Services suffixed `Service` (`MediaFileInstanceService`, `DriveService`, `FolderService`).
- DTOs suffixed `DTO` (`MediafileInstanceDTO`, `FileSummaryDTO`, `VersionDTO`) — except `Meta`, `Picture`.
- Entity classes carry no suffix (`Drive`, `Folder`, `Image`, `MediaFile`).

**Methods/Fields:**
- `camelCase` everywhere (`getMediaFileVersion`, `originalVersion`, `dateStoredLocal`).
- Getters/setters mandatory for every persisted field (Hibernate convention).
- Boolean getters use `is` prefix when the type is `boolean` (`isStandalone`, `isBackup`) but `Boolean` (boxed) getters occasionally use `get` (`getInvalid`) — keep consistent within one entity.
- A few fields/accessors use `snake_case` (`camera_make`, `camera_model`, `setCamera_make` on `Image`). Treat as legacy — do NOT add more; new fields must use `camelCase`.

**Constants:**
- `UPPER_SNAKE_CASE` for true constants: `EMPTYHASH`, `UNKNOWN`, `BACKUPID`, `SKIP_ORIENTATION` in `Service/Hash/MediaFileHash.java` and `JPGHash.java`.
- Static logger fields typically named `LOG` (`MediaFileHash`, `JPGHash`, `MP4Hash`, `TIFFHash`). One file uses `logger` (`ExifReadWriteIMR`) — prefer `LOG` for new code.

## Java Version & Language Features

- Java 21 source/target (`pom.xml` lines 16-17).
- Pattern matching for `instanceof` is used (e.g., `if (!(o instanceof MediaFile other)) return false;` in `MediaFile.equals`, `MediaFileVersion.equals`, `Drive.equals`, `Folder.equals`, `MediaDirectory.equals`, `MediaFileInstance.equals`).
- Enhanced `switch` not observed — classic `switch` with `case X: ... break;` is used (see `src/test/java/org/nyusziful/pictureorganizer/Service/Backend/MediaTest.java`).
- No records; all data carriers are plain classes with public fields (`DTO/Meta.java`) or getter/setter classes.

## Code Style

**Formatting:**
- No `.editorconfig`, `.prettierrc`, Checkstyle, Spotless, or Spotbugs config detected in the repo root or `pom.xml`.
- Effective style is IntelliJ IDEA default (project uses `.idea/`): 4-space indent, braces on same line, no trailing whitespace.
- Line length: not enforced; some lines in entities and DAOs exceed 200 chars (e.g., `MediaFile.equals` line, `JPAConnection` JDBC URL concat).

**Linting:**
- None. No static analysis plugin is configured in `pom.xml`.
- `org.jetbrains:annotations` 23.1.0 is on the classpath but `@Nullable` / `@NotNull` are not widely used; nullability is implicit.

## Import Organization

**Observed order (IntelliJ default):**
1. Third-party / standard-library packages (`jakarta.persistence.*`, `java.util.*`, `org.apache.commons.io.*`).
2. Project packages (`org.nyusziful.pictureorganizer.*`).
3. Static imports at the bottom (`import static org.junit.Assert.*;`, `import static java.lang.Boolean.TRUE;`).

No wildcard suppression; `jakarta.persistence.*` wildcard is common in entities (e.g., `MediaFile.java` line 5). Do NOT convert to specific imports — matches project style.

**Path Aliases:** Not applicable (Java).

## Error Handling

**DAO transaction pattern (used throughout `src/main/java/org/nyusziful/pictureorganizer/DAL/DAO/`):**

```java
EntityManager entityManager = jpaConnection.getEntityManager();
EntityTransaction transaction = entityManager.getTransaction();
try {
    // ... entityManager.persist / find / merge / remove ...
    if (!batch) transaction.commit();
} catch (RuntimeException e) {
    try {
        transaction.rollback();
    } catch (RuntimeException rbe) {
//        log.error("Couldn't roll back transaction", rbe);  // commented out everywhere
    }
    throw e;
} finally {
    if (entityManager != null && !batch) {
        entityManager.close();
    }
}
```

See `CRUDDAOImpHib.java` lines 30-52 (getAll), 116-138 (persist), 145-167 (merge), 189-208 (delete).

**Conventions:**
- Every DAO method has a non-batch overload (`method(args)`) that delegates to the batch-capable variant (`method(args, boolean batch)`). When `batch == true` the caller owns the transaction/close lifecycle.
- Caller-catches `RuntimeException`; no custom exception hierarchy.
- Rollback failures are silently swallowed (log line commented). This is a known debt — see `.planning/codebase/CONCERNS.md` when generated.
- Throwing `new RuntimeException("more than one Mediafile for ...")` as the escape-hatch for unexpected DB state (`MediaFileDAOImplHib.java` line 78).

**Application-level:**
- Checked exceptions (`IOException`, `ImageProcessingException`, `NoSuchAlgorithmException`) are commonly caught, `e.printStackTrace()` invoked, and execution continues — e.g., `ExifPreReadTest.java` lines 54-58; `Main/PictureOrganizer.java` start method declares `throws Exception`.
- Prefer wrapping printStackTrace calls in logger calls for new code (see Logging section).

## Logging

**Framework stack:**
- SLF4J API 2.0.5 (`org.slf4j:slf4j-api`) — used in application code.
- Log4j2 2.20.0 via `log4j-slf4j2-impl` bridge (`pom.xml` lines 141-160).
- Configured by `src/main/resources/log4j2.xml`.

**Logger declaration pattern (preferred):**

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPGHash implements Hasher {
    private static final Logger LOG = LoggerFactory.getLogger(JPGHash.class);
    ...
}
```

Files following the pattern: `Service/Hash/JPGHash.java:30`, `Service/Hash/MP4Hash.java:30`, `Service/Hash/MediaFileHash.java:56`, `Service/Hash/TIFFHash.java:28`, `Service/ExifUtils/ExifReadWriteIMR.java:41` (uses non-final `logger`).

**Log4j2 config (`src/main/resources/log4j2.xml`):**
- Two appenders: `STDOUT` (console) and `FILE` (rolling `logAllfile.log` under `../pictureOrganizer/logs`, 19500 KB rotation, 10 files kept).
- Pattern: `%-5p | %d{yyyy-MM-dd HH:mm:ss} | [%t] %C{2} (%F:%L) - %m%n`.
- Root level: `info` -> STDOUT + FILE.
- **Hibernate-specific loggers** (recently added per commit `81751c8 Debug Hibernate logging`):
  - `org.hibernate.SQL` at `debug` -> STDOUT only
  - `org.hibernate.orm.jdbc.bind` at `trace` -> STDOUT only
  - `org.hibernate.stat` at `trace` -> STDOUT only
  - `org.hibernate.SQL_SLOW` at `trace` -> STDOUT only
  - `org.hibernate.cache` at `trace` -> STDOUT only
  - `com.mchange` at `debug` -> STDOUT + FILE (c3p0 pool diagnostics)
- These are DEBUG-level by default; loggers should be raised to `warn` before production packaging.

**Anti-pattern — `System.out.println` is still pervasive:**
- 83 occurrences across 20 files in `src/main/java` (grep `System.out.println`).
- Heaviest offenders: `Main/PresetUseCases.java` (41), `Service/Comparison/ComparePanelController.java` (5), `Service/ExifUtils/ExifReadWriteIMR.java` (3).
- `Main/PictureOrganizer.java` and most service classes still mix `System.out` with no logger.
- Rule for new code: use `LOG.info/debug/warn/error`. Do not add new `System.out.println` calls.

## Comments

**Observed:**
- Most files carry an auto-generated NetBeans header: `/* To change this license header, choose License Headers in Project Properties. ... @author gabor */`. Leave existing headers in place; do NOT add new ones to new files.
- Large commented-out code blocks are common (dead `main` methods with hardcoded Windows paths in `JPGHash`, `MediaFileHash`, `ExifService`; alternative constructors and query variants in entities and DAOs). Remove when touching the file rather than leaving in new files.
- `//TODO` comments (34 occurrences across 20 files in `src/`) mark known follow-ups. When adding new TODOs include a short description, e.g., `//TODO for broken files this might be not ok` (`MediaFileVersion.java:105`).

**JavaDoc:**
- Sparse and inconsistent. `ExifService.readFileMeta` has proper JavaDoc (`src/main/java/org/nyusziful/pictureorganizer/Service/ExifUtils/ExifService.java:29-34`). Most public methods are undocumented.
- Add JavaDoc to new public service methods; it is not required for getters/setters.

## Hibernate Entity Patterns

All entities live in `src/main/java/org/nyusziful/pictureorganizer/DAL/Entity/`.

**Common structure (see `MediaFile.java`, `MediaFileVersion.java`, `MediaFileInstance.java`):**

```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "<snake_case_table>")
@DiscriminatorColumn(name = "file_type")
@DiscriminatorValue("DEF")
public class MediaFile extends TrackingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    protected int id = -1;

    // ... @ManyToOne / @OneToOne / @Column fields ...

    public MediaFile() {
        // this form used by Hibernate
    }
    // ... parameterised constructor ...
}
```

**Rules:**
- Every entity has a protected/public no-arg constructor with the comment `// this form used by Hibernate`. Preserve this comment — it documents intent.
- `id` is a primitive `int` initialised to `-1`; this sentinel is used by `equals` to mean "not yet persisted" (`getId() == -1 || other.getId() == -1`).
- Column names are `snake_case` (database-driven): `name="media_directory_id"`, `name="original_version_id"`. Table names follow the same convention: `media_file`, `media_file_version`, `media_file_instance`.
- Discriminator column `file_type` is used for SINGLE_TABLE inheritance on `MediaFile`, `MediaFileVersion`, `MediaFileInstance`, `Media` — with `"DEF"` on the base and extension-specific values on subtypes (`@DiscriminatorValue("JPG")` in `JPGMediaFile.java:7`, also `RAW`, `VID`).
- Base class `TrackingEntity` (`src/main/java/org/nyusziful/pictureorganizer/DAL/Entity/TrackingEntity.java`) is `@MappedSuperclass` providing `credate`, `creator`, `upddate`, `updater` populated via `@PrePersist` / `@PreUpdate`, using `System.getenv("COMPUTERNAME")` for the creator — Windows-specific.
- Unique constraints are declared on `@Table`: see `Folder` (drive_id + path), `MediaFileInstance` (folder_id + filename), `Image` (odid + type).
- Enums are persisted as strings: `@Enumerated(EnumType.STRING) @Column(length=5, name="media_type", columnDefinition="varchar")` in `Media.java:28-30`.
- `@Transient` for derived / path-resolved fields (`Folder.javaPath`, `MediaFileInstance.filePath`, `MediaDirectory.conflicting`, `Image.dateTaken` vs. stored `dateTakenString`).
- Persistence unit `mysql-picture` is declared in `src/main/resources/META-INF/persistence.xml`; every entity class must be registered there — there is no `autoDetectAnnotatedClasses`.
- `hibernate.hbm2ddl.auto` is `validate` (commit `1150cf4 Schema Validation`) — schema changes must be applied via SQL scripts in `src/main/resources/SQL/` and project-root `*.sql` files.

**equals / hashCode pattern (all persisted entities):**

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MediaFile other)) return false;
    return getId() == other.getId()
        || ((getId() == -1 || other.getId() == -1)
            && /* business key comparison */);
}

@Override
public int hashCode() {
    return Objects.hash(/* stable business key */);
}
```

See `MediaFile.java:47-57`, `MediaFileVersion.java:102-111`, `Drive.java:60-69`, `Folder.java:76-86`, `MediaDirectory.java:76-86`, `MediaFileInstance.java:113-125`. New entities MUST follow this pattern — the `id == -1` fallback lets unsaved and saved instances compare meaningfully.

## toString Convention (recently unified — commit `be4cf0d toString unified`)

14 classes override `toString()` (grep: `@Override public String toString()` across `src/main/java`):

- `DAL/Entity/Drive.java`
- `DAL/Entity/Folder.java`
- `DAL/Entity/Image.java`
- `DAL/Entity/MediaDirectory.java`
- `DAL/Entity/MediaFileInstance.java`
- `DAL/Entity/MediaFileVersion.java`
- `DTO/DirectorySummaryDTO.java`
- `DTO/FileSummaryDTO.java`
- `DTO/FolderSummaryDTO.java`
- `DTO/Meta.java`
- `DTO/VersionDTO.java`
- `Model/MediaDirectoryInstance.java`
- `Service/Hash/ImageFileDirectory.java`
- `Service/Hash/JPEGSegment.java`

**Two acceptable shapes:**

1. **Debug-friendly key=value (for DTOs and entities with multiple business fields):**

    ```java
    @Override
    public String toString() {
        return "MediaFileVersion{" +
                "id=" + id +
                ", parent=" + parent +
                ", invalid=" + getInvalid() +
                '}';
    }
    ```

    (`MediaFileVersion.java:200-207`, also used in `Drive.java:42-49`, `Image.java:133-141`.)

2. **Human-readable composite (for path-like / displayable entities):**

    ```java
    @Override
    public String toString() {
        return folder + "\\" + filename;   // MediaFileInstance.java:152-155
    }

    @Override
    public String toString() {
        return firstDate + " - " + lastDate + (label.isEmpty() ? "" : " " + label);
    }   // MediaDirectory.java:31-34
    ```

**Rules:**
- Entities and DTOs should override `toString`. Services, DAOs, and controllers should not.
- Keep field order consistent: `id` first, then business-key fields, then state flags.
- Do NOT include lazy-loaded collections or back-references (would trigger loads / infinite recursion).
- `Drive.toString()` still labels itself `"DriveDTO{..."` (legacy). Don't copy that header into new entities — use the real class name.

## Function Design

**Size:**
- No enforced limit. Service methods can reach 100+ lines (e.g., `MediaFileInstanceService.readMediaFilesFromFolderInternal`). Consider splitting when adding new logic, but matching local style is acceptable for small additions.

**Parameters:**
- DAO methods expose both plain and `boolean batch`-suffixed overloads (see `CRUDDAO` interface). Add both when extending a DAO.
- Services occasionally accept `ProgressDTO` / `ProgressLeakingTask` for UI feedback — keep as the last parameter when present.

**Return values:**
- Collections returned as `List<T>` / `Collection<T>` / `Set<T>` directly (no `Optional`, no defensive copies).
- `null` is a legitimate "not found" signal from DAO `getById` and service lookups — callers must null-check.

## Module Design

**Exports:**
- No `module-info.java`; the project runs on the classpath, not the module path (despite JavaFX being modular). Do NOT add `module-info.java` without coordinating with Launch4j / assembly config in `pom.xml`.

**Singletons:**
- Common pattern: `private static X instance; public static X getInstance() { ... }`. Used by `JPAConnection`, `HibConnection`, `MediaFileInstanceService`, `MediaDirectoryService`, `MediaFileService`, `MediaFileVersionService`, `CommonProperties`. Matches existing style — use for new service-layer classes that hold DAO handles.
- Note: `DriveService`, `FolderService`, `ImageService` are instantiated via `new` from within other services (`MediaFileInstanceService` constructor lines 42-48). Inconsistent but accepted.

**Barrel / aggregate classes:**
- `Service/Services.java` aggregates service accessors. Use it when adding a new cross-cutting service.

## Jakarta Persistence Imports

- Project uses `jakarta.persistence.*` (JPA 3.1, Jakarta EE 9+), not `javax.persistence.*`. Do NOT import `javax.persistence` — it will not resolve.

---

*Convention analysis: 2026-04-19*
