# External Integrations

**Analysis Date:** 2026-04-19

## APIs & External Services

**External CLI tools (invoked via `ProcessBuilder`):**
- **ExifTool** (Phil Harvey's `exiftool`) - must be on system `PATH`
  - Used for: reading/writing EXIF, XMP, IPTC metadata; extracting thumbnails; renaming-friendly tag rewrites
  - Entry points:
    - `src/main/java/org/nyusziful/pictureorganizer/Service/ExifUtils/ExifReadWriteET.java` (lines 28, 51, 141, 149, 156, 212 — batch/streaming pipeline, chunk size 100)
    - `src/main/java/org/nyusziful/pictureorganizer/Service/TimeShift/StaticTools.java` (line 35 — thumbnail extraction `exiftool -b -ThumbnailImage -w thmb/%d%f_thmb.jpg .`)
    - `src/main/java/org/nyusziful/pictureorganizer/Main/PresetUseCases.java` (lines 283, 389–492 — ad-hoc/demo invocations)
  - No authentication required

**Embedded SDKs (no network):**
- **drewnoakes/metadata-extractor** 2.19.0 - native Java EXIF/XMP/IPTC reading (alternative path to ExifTool)
  - `src/main/java/org/nyusziful/pictureorganizer/Service/ExifUtils/ExifReadWriteIMR.java` (IMR = "in-memory reader")
  - `src/main/java/org/nyusziful/pictureorganizer/Service/ExifUtils/ExifService.java`
  - `src/main/java/org/nyusziful/pictureorganizer/Service/Comparison/Duplicate.java`
- **Adobe XMP Core** 6.1.11 - XMP packet parsing (`com.adobe.internal.xmp.*` imports in `ExifReadWriteIMR.java`)
- **Garmin FIT SDK** 2.0.20.16 - shipped as `src/main/resources/fit.jar` (system-scoped dependency); no live `import com.garmin` in source — currently dormant/future integration for fitness-device timestamps

**No HTTP/REST clients detected.** No `OkHttp`, `Retrofit`, `Apache HttpClient`, `RestTemplate`, or JDK `HttpClient` usages found. The application is fully offline aside from JDBC to MySQL.

## Data Storage

**Databases:**
- **MySQL 8.0** (primary, production-like)
  - JDBC URL template (built at runtime in `src/main/java/org/nyusziful/pictureorganizer/DAL/JPAConnection.java:30`): `jdbc:mysql://127.0.0.1:3306/{test|dev|}pictureorganizer?useUnicode=yes&characterEncoding=UTF-8`
  - Schemas: `pictureorganizer` (PROD), `devpictureorganizer` (DEV), `testpictureorganizer` (TEST); switched by `JPAConnection.DBMode` enum (`TEST`, `DEV`, `PROD`)
  - Driver: `com.mysql.cj.jdbc.Driver` (`mysql-connector-java:8.0.29`)
  - JPA provider: Hibernate 6.4.4.Final via persistence unit `mysql-picture` in `src/main/resources/META-INF/persistence.xml`
  - Dialect: `org.hibernate.dialect.MySQLDialect`
  - Schema policy: `hibernate.hbm2ddl.auto=validate` — Hibernate does NOT auto-create or migrate; schema must pre-exist
  - Connection pool: C3P0 (`org.hibernate.orm:hibernate-c3p0`), tuned in `src/main/resources/c3p0.properties` (unreturned-connection timeout 2s, debug stack traces enabled)
  - Session bootstrap: `src/main/java/org/nyusziful/pictureorganizer/DAL/HibConnection.java` (singleton `SessionFactory` built via `StandardServiceRegistryBuilder().configure()`)
  - EntityManager bootstrap: `src/main/java/org/nyusziful/pictureorganizer/DAL/JPAConnection.java` (singleton `EntityManagerFactory`; injects `jdbc.url` with mode-dependent schema prefix)
  - Credentials are HARDCODED in `src/main/resources/META-INF/persistence.xml:32-33` as `picture` / `picture` — committed to git (see CONCERNS analysis)
  - Legacy Hibernate config retained at `src/main/resources/old.hibernate.cfg.xml` (references old IP `192.168.0.54` and second-level cache `EhCacheRegionFactory`)

- **MS Access (legacy, dead code)** via UCanAccess driver
  - `src/main/java/org/nyusziful/pictureorganizer/DAL/DBConnection.java:14-25` opens `jdbc:ucanaccess://e:/Pictures.accdb` through `net.ucanaccess.jdbc.UcanaccessDriver`
  - UCanAccess is NOT declared in `pom.xml` — this path would fail at runtime; appears abandoned

- **Direct JDBC (legacy, dead code)** to `jdbc:mysql://192.168.0.54:3306/picture` in `DBConnection.java:32` — bypasses Hibernate; retained for historical reference

**Entities (23 JPA classes under `src/main/java/org/nyusziful/pictureorganizer/DAL/Entity/`):**
- Core: `Drive`, `Folder`, `MediaDirectory`, `MediaFile`, `MediaFileInstance`, `MediaFileVersion`, `Image`, `Media`, `MetaData`
- Subtype trees: `JPGMedia*`, `RAWMedia*`, `VideoMedia*` variants of Media/MediaFile/Version/Instance
- Tables referenced: `drive`, `folder`, `media_directory`, `media_file`, `media_file_version`, `media_file_instance`, `media_image`, `image`, `meta_data`, `media_file_old`

**Schema artefacts (SQL files):**
- `createDB.sql` - full DDL dump for `pictureorganizer` schema
- `src/main/resources/SQL/schema.sql` - `CREATE SCHEMA devpictureorganizer ... utf8mb4_0900_as_cs`
- `src/main/resources/SQL/Drive20231217.sql` - seed/restore of `drive` table (MySQL 8.0.19 dump)
- `src/main/resources/SQL/EmplyTables20231217.sql` - empty-table bootstrap
- `src/main/resources/SQL/duplicateSchema.sql` - clones `pictureorganizer` tables into `devpictureorganizer`
- `src/main/resources/SQL/VersionCleanUpHelper.sql` - maintenance queries for versions
- Project-root scripts: `albums.sql`, `check_migration.sql`, `creating_media_files.sql`, `duplicates.sql`, `migration_of_media_file.sql`, `non-hashed_duplicates.sql`, `original_names.sql`, `query.sql`

**File Storage:**
- **Local filesystem** - the application's core domain (it organises pictures)
  - Input/output directories configured via JavaFX UI and persisted in Java `Preferences` (per-user registry/plist), keys `InputDirectory`/`OutputDirectory` in `src/main/java/org/nyusziful/pictureorganizer/Main/CommonProperties.java`
  - Copy/move semantics: `WriteMethod.MOVE` default, overwrite flag (`CommonProperties.overwrite`)
  - File I/O primarily via `java.nio.file.*`, `java.io.File`, and `org.apache.commons.io.*` helpers (used in 8 source files)
  - Hash/segment readers manipulate raw bytes via custom `RandomAccessByteStream` / `BufferedRandomAccessFile` in `src/main/java/org/nyusziful/pictureorganizer/Service/Hash/`

**Caching:**
- **C3P0** JDBC connection pool (see above)
- **Hibernate second-level cache + query cache** - configured in the legacy `old.hibernate.cfg.xml` with `org.hibernate.cache.ehcache.EhCacheRegionFactory`; NOT enabled in the active `persistence.xml`
- No in-process object cache (no Caffeine/Ehcache/Guava cache on classpath)

## Authentication & Identity

**Auth Provider:**
- None. The app is a single-user desktop application; no login, SSO, OAuth, or token system.
- The only credentials in the codebase are the MySQL DB user (`picture` / `picture`), hardcoded in `src/main/resources/META-INF/persistence.xml`.

## Monitoring & Observability

**Error Tracking:**
- None (no Sentry, Rollbar, Crashlytics, or equivalent)
- Errors are primarily written to logs or `System.out`; `HibConnection.java:35` swallows startup exceptions with `System.out.println(e.getMessage())`

**Logs:**
- **Log4j 2** 2.20.0 via SLF4J bridge (`log4j-slf4j2-impl`)
- Config: `src/main/resources/log4j2.xml`
- Appenders:
  - `STDOUT` console appender (pattern `%-5p | %d | [%t] %C{2} (%F:%L) - %m%n`)
  - `FILE` rolling file appender - writes to `../pictureOrganizer/logs/logAllfile.log`, 19.5 MB rotation, up to 10 retained files
- Verbose Hibernate loggers enabled at `debug`/`trace`: `org.hibernate.SQL`, `org.hibernate.orm.jdbc.bind`, `org.hibernate.stat`, `org.hibernate.SQL_SLOW`, `org.hibernate.cache`, `com.mchange` (c3p0)
- Root level: `INFO`

## CI/CD & Deployment

**Hosting:**
- None - desktop application distributed as Windows `.exe` (Launch4j) or fat JAR

**CI Pipeline:**
- None detected - no `.github/workflows/`, `.gitlab-ci.yml`, `Jenkinsfile`, `bitbucket-pipelines.yml`, or `azure-pipelines.yml`
- Build happens locally via `mvn package` which produces:
  - `target/pictureOrganizer-3.0-SNAPSHOT.jar`
  - `target/pictureOrganizer-3.0-SNAPSHOT-jar-with-dependencies.jar`
  - `target/hash.exe` (Launch4j console wrapper over `Service.Hash.HashToFile`)
  - `target/verify.exe` (Launch4j GUI wrapper over `Service.Hash.VerifyHash`)

## Environment Configuration

**Required env vars:**
- None (app reads no `System.getenv(...)`)

**Required external state:**
- MySQL 8.0 running on `127.0.0.1:3306` with user `picture`/`picture` and schema `devpictureorganizer` populated (since `PictureOrganizer.start` hard-sets `DBMode.DEV`)
- `exiftool` binary on `PATH`
- Writable parent directory `../pictureOrganizer/logs/` relative to CWD (Log4j file appender)
- JavaFX runtime modules available (JDK 21 no longer bundles them; supply via Maven plugin or `--module-path`)

**Secrets location:**
- `.env` files: none found
- Credentials committed in plaintext in `src/main/resources/META-INF/persistence.xml` (`picture`/`picture`)
- No secret manager / keystore integration

## Webhooks & Callbacks

**Incoming:**
- None - no HTTP server, no listener ports opened

**Outgoing:**
- None - no outbound HTTP calls

**Inter-process:**
- Outbound `ProcessBuilder` invocations of `exiftool` (see ExifTool section above)

---

*Integration audit: 2026-04-19*
