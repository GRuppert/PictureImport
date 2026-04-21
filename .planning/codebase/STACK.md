# Technology Stack

**Analysis Date:** 2026-04-19

## Languages

**Primary:**
- Java 21 - All application code under `src/main/java/org/nyusziful/pictureorganizer/`
  - `maven.compiler.source` / `maven.compiler.target` set to `21` in `pom.xml`

**Secondary:**
- SQL (MySQL dialect) - Schema and data scripts in `src/main/resources/SQL/` and project root (e.g. `createDB.sql`, `albums.sql`, `query.sql`)
- FXML (XML-based JavaFX markup) - UI layouts in `src/main/resources/fxml/` (11 files, e.g. `main.fxml`, `comparePanel.fxml`, `timeline.fxml`)
- CSS - JavaFX styling in `src/main/resources/styles/` (`Charts.css`, `Styles.css`)
- XML - Configuration (`src/main/resources/META-INF/persistence.xml`, `src/main/resources/log4j2.xml`, `src/main/resources/old.hibernate.cfg.xml`)

## Runtime

**Environment:**
- Java SE 21 (JDK 21) - desktop JVM, packaged as fat JAR and Launch4j `.exe` wrappers for Windows
- JavaFX 23-ea+22 - UI toolkit, required on module path at runtime

**Package Manager:**
- Apache Maven (uses standard Maven layout)
- Lockfile: none (Maven uses declared versions directly; no `dependency-lock` plugin)

## Frameworks

**Core:**
- Hibernate ORM 6.4.4.Final (`org.hibernate.orm:hibernate-core`) - JPA provider, entity management, HQL
- Jakarta Persistence API 3.1.0 (`jakarta.persistence:jakarta.persistence-api`) - JPA annotations on entities under `src/main/java/org/nyusziful/pictureorganizer/DAL/Entity/`
- JavaFX 23-ea+22 (`org.openjfx:javafx-controls`, `org.openjfx:javafx-fxml`) - desktop GUI, controllers under `src/main/java/org/nyusziful/pictureorganizer/UI/Contoller/` (note the `Contoller` typo)
- Hibernate C3P0 6.4.4.Final (`org.hibernate.orm:hibernate-c3p0`) - JDBC connection pooling; configured via `src/main/resources/c3p0.properties`
- ModelMapper 3.0.0 (`org.modelmapper:modelmapper`) - entity/DTO mapping; DTOs live in `src/main/java/org/nyusziful/pictureorganizer/DTO/`

**Testing:**
- JUnit 4.13.2 (`junit:junit`) - unit test framework (test scope)
- Hamcrest Core 2.2 (`org.hamcrest:hamcrest-core`) - matcher library for assertions

**Build/Dev:**
- `maven-compiler-plugin` 3.8.1 - Java 21 source/target compilation
- `maven-surefire-plugin` 2.16 - test execution (with JavaFX classpath additions)
- `maven-assembly-plugin` (no version pinned) - builds `jar-with-dependencies` uber-JAR
- `launch4j-maven-plugin` 2.3.3 (`com.akathist.maven.plugins.launch4j`) - wraps the fat JAR into Windows `.exe` binaries (`target/hash.exe`, `target/verify.exe`)
- `javafx-maven-plugin` 0.0.8 (`org.openjfx`) - `mvn javafx:run` launcher for main class `org.nyusziful.pictureorganizer.Main.PictureOrganizer`

## Key Dependencies

**Critical:**
- `com.drewnoakes:metadata-extractor` 2.19.0 - EXIF/XMP/IPTC metadata extraction from images and video (JPEG, TIFF, PNG, HEIF, MP4, QuickTime, AVI, BMP, GIF); used heavily in `src/main/java/org/nyusziful/pictureorganizer/Service/ExifUtils/ExifReadWriteIMR.java`
- `com.adobe.xmp:xmpcore` 6.1.11 - XMP metadata parsing (Adobe XMP Toolkit); imported in `ExifReadWriteIMR.java`
- `commons-io:commons-io` 2.11.0 - filesystem utilities (file copy, hashing helpers, directory walking); used in 8+ classes including `MediaFileHash.java`, `Picture.java`, `MediaFileInstance.java`
- `com.univocity:univocity-parsers` 2.9.1 - CSV parsing; used in `src/main/java/org/nyusziful/pictureorganizer/Service/Comparison/StaticTools.java`
- `mysql:mysql-connector-java` 8.0.29 - MySQL JDBC driver (also declared via Hibernate dialect `org.hibernate.dialect.MySQLDialect`)

**Infrastructure:**
- `org.slf4j:slf4j-api` 2.0.5 - logging facade
- `org.apache.logging.log4j:log4j-core` 2.20.0 - log implementation (plus `log4j-api` and `log4j-slf4j2-impl` bridge)
- `org.jetbrains:annotations` 23.1.0 - `@NotNull` / `@Nullable` authoring annotations
- `com.garmin.fit:FIT` 2.0.20.16 - Garmin FIT SDK, declared as **system-scope** with `systemPath` pointing at `src/main/resources/fit.jar` (no Maven-central resolution); currently no `import com.garmin` found in code — the jar is shipped but appears unused by live Java sources

## Configuration

**Environment:**
- No `.env` file; no environment variable reading detected
- Java `Preferences` API (`Preferences.userNodeForPackage(PictureOrganizer.class)`) persists user-selectable input/output directories in `src/main/java/org/nyusziful/pictureorganizer/Main/CommonProperties.java` under keys `InputDirectory` / `OutputDirectory`
- Runtime DB mode toggled in code via `JPAConnection.setMode(DBMode.DEV|TEST|PROD)` (set to `DEV` inside `PictureOrganizer.start` at `src/main/java/org/nyusziful/pictureorganizer/Main/PictureOrganizer.java:29`)

**Build:**
- `pom.xml` (Maven project descriptor)
- `src/main/resources/META-INF/persistence.xml` (JPA persistence unit `mysql-picture`)
- `src/main/resources/log4j2.xml` (Log4j 2 appenders + loggers; file appender path `../pictureOrganizer/logs/logAllfile.log`)
- `src/main/resources/c3p0.properties` (Hibernate C3P0 connection-pool tuning)
- `src/main/resources/old.hibernate.cfg.xml` (legacy Hibernate config — retained but replaced by `persistence.xml`)
- `.gitignore` excludes `/target/`, `/logs/`, `/.idea/`, `*.iml`, `nbactions.xml`, `nb-configuration.xml`

## Platform Requirements

**Development:**
- JDK 21 on `PATH`
- Maven 3.x
- JavaFX SDK present separately at runtime for IDE launches — per `README.md`: `--module-path "E:\Work\JAVA\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml`
- Local MySQL 8.0 instance reachable at `127.0.0.1:3306` with schemas `pictureorganizer`, `devpictureorganizer`, `testpictureorganizer` (database name is prefix-switched by `JPAConnection` mode)
- ExifTool CLI installed on `PATH` — invoked via `ProcessBuilder` in `Service/ExifUtils/ExifReadWriteET.java` and `Service/TimeShift/StaticTools.java`

**Production:**
- Windows desktop (primary target — Launch4j produces `target/hash.exe` and `target/verify.exe`)
- Fat JAR alternative: `target/${project.build.finalName}-jar-with-dependencies.jar` (cross-platform if JavaFX runtime supplied)
- Main class: `org.nyusziful.pictureorganizer.Main.PictureOrganizer`
- Auxiliary CLI entry points: `org.nyusziful.pictureorganizer.Service.Hash.HashToFile` (Hasher console app), `org.nyusziful.pictureorganizer.Service.Hash.VerifyHash` (Verifier GUI app)

---

*Stack analysis: 2026-04-19*
