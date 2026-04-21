# Testing Patterns

**Analysis Date:** 2026-04-19

## Test Framework

**Runner:**
- JUnit 4.13.2 (`pom.xml` lines 204-209, test scope).
- No JUnit 5 (Jupiter) on the classpath.
- No TestNG.

**Assertion Library:**
- `org.junit.Assert` static imports (`import static org.junit.Assert.*;`).
- Hamcrest Core 2.2 is declared in `pom.xml` lines 210-215 but not explicitly used — only classic `assertEquals` / `assertTrue` / `assertNull` / `assertNotEquals` / `fail` calls are found.
- One test imports `org.apache.logging.log4j.core.util.Assert` from Log4j (`src/test/java/org/nyusziful/pictureorganizer/Service/Backend/MediaTest.java:4`) for `Assert.isNonEmpty(...)` — unusual choice; do not copy into new tests.

**Build plugin:**
- Maven Surefire Plugin 2.16 configured in `pom.xml` lines 38-47 with `additionalClasspathElement` pointing at `${java.home}/lib/jfxrt.jar`. The version is very old (current is 3.x) and `jfxrt.jar` no longer exists in Java 21 JDKs — the plugin block is effectively legacy configuration that does not block compilation.

**Mocking Framework:**
- **None.** No Mockito, PowerMock, EasyMock, or JMock dependency; no `@Mock` or `Mockito.` references anywhere in `src/`. Tests hit real collaborators (filesystem, MySQL test schema, file parsers).

**Coverage Tools:**
- **None.** No JaCoCo, Cobertura, or Surefire-report plugin in `pom.xml`. No coverage threshold enforced.

## Run Commands

```bash
mvn test                          # Run all tests (reads src/test/java)
mvn -Dtest=HashTest test          # Run a single test class
mvn -Dtest=HashTest#testHash test # Run a single test method (JUnit 4 syntax via Surefire)
mvn package                        # Compiles, runs tests, builds jar-with-dependencies + Launch4j exes
```

## Test File Organization

**Location:**
- Standard Maven layout: `src/test/java/<package>/<TestClass>.java`.
- Mirror the main package structure (tests for `org.nyusziful.pictureorganizer.Service.Hash` live in `src/test/java/org/nyusziful/pictureorganizer/Service/Hash/`).
- Extra directory `Service/Backend/` exists only in the test tree — used for integration-style tests (`MediaTest.java`).

**Naming:**
- Most classes: `<ClassUnderTest>Test.java` (`HashTest`, `JPGHashTest`, `ExifReadTest`, `ExifWriteTest`, `ExifReadWriteTest`, `ExifPreReadTest`, `ExifBlobTest`, `ImageServiceTest`, `MediaDirectoryServiceTest`, `MediaTest`).
- Three legacy `camelCase` class names still present: `fileRenamerTest.java`, `fileVersionTest.java`, `mediaDirectoryTest.java` in `src/test/java/org/nyusziful/pictureorganizer/Service/Rename/`. Rename when touching; do NOT introduce new `camelCase` test classes.
- Methods: `testXxx` (`testFullHash`, `testGetFileName`, `testReadMeta`). A few drop the prefix (`scanJPG`, `writeToDB`, `writeTest`).

**Directory layout:**
```
src/test/java/org/nyusziful/pictureorganizer/
├── DAL/
│   ├── ExifBlobTest.java
│   ├── ImageServiceTest.java
│   └── MediaDirectoryServiceTest.java
└── Service/
    ├── Backend/
    │   └── MediaTest.java
    ├── ExifUtils/
    │   ├── ExifPreReadTest.java
    │   ├── ExifReadTest.java
    │   ├── ExifReadWriteTest.java
    │   └── ExifWriteTest.java
    ├── Hash/
    │   ├── HashTest.java
    │   └── JPGHashTest.java
    └── Rename/
        ├── fileRenamerTest.java
        ├── fileVersionTest.java
        └── mediaDirectoryTest.java
```

**Test resource directory:** `src/test/resources/` does NOT exist in git. Test files are loaded via `this.getClass().getClassLoader().getResource(filename)` and rely on files being present on disk at the expected classpath location. Running tests fresh on a new machine will fail for most classes until resources are provisioned. See `.planning/codebase/CONCERNS.md` when generated.

**Total test classes:** 13 Java files in `src/test/java` (vs. 141 in `src/main/java` — ~9% coverage by file count, far less by method count).

## Test Structure

### Parameterised pattern (dominant style)

Seven of the thirteen test classes use JUnit 4 `@RunWith(Parameterized.class)` with a nested `static class TestData` carrier:

```java
@RunWith(Parameterized.class)
public class HashTest {
    static class TestData {
        String filename;
        String fullHash;
        String Hash;
        public TestData(String filename, String fullHash, String Hash) { ... }
    }
    private static final TestData[] TESTS = new TestData[] {
        new TestData("20160627_183440_GT-I9195I-20160627_173440.jpg",
                     "ab75c83fa44195b2f27a7a47cbabe852",
                     "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"),
        // ...
    };

    String filename; String fullHash; String Hash;

    @Parameters
    public static List<TestData> data() { return Arrays.asList(TESTS); }

    public HashTest(TestData data) {
        this.filename = data.filename;
        this.fullHash = data.fullHash;
        this.Hash = data.Hash;
    }

    @Test
    public void testFullHash() {
        File file = new File(this.getClass().getClassLoader()
                             .getResource(filename).getFile());
        String result = MediaFileHash.getFullHash(file);
        assertEquals("MD5 of file(" + filename + ") counted: " + result
                     + " awaited: " + fullHash, fullHash, result);
    }
}
```

Source: `src/test/java/org/nyusziful/pictureorganizer/Service/Hash/HashTest.java`.

Classes following the pattern:
- `Service/Hash/HashTest.java`
- `Service/Hash/JPGHashTest.java`
- `Service/ExifUtils/ExifReadTest.java`
- `Service/ExifUtils/ExifReadWriteTest.java`
- `Service/Rename/fileRenamerTest.java`
- `Service/Rename/fileVersionTest.java`
- `Service/Rename/mediaDirectoryTest.java`

### Plain JUnit test pattern

```java
public class ExifPreReadTest {
    @Test
    public void testReadMeta() {
        System.out.println("readMeta");
        String fileName = "D5C03339_Exif231.ARW";
        ClassLoader classLoader = ExifReadWriteIMR.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        try {
            ArrayList<String[]> strings = ExifReadWriteIMR.readMeta(file);
            // ...
            assertTrue((strings.size() == 0 && strings2.size() == 0));
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

Source: `src/test/java/org/nyusziful/pictureorganizer/Service/ExifUtils/ExifPreReadTest.java`.

Note: **exceptions are caught and `printStackTrace()`-logged, not re-thrown** — the test can silently pass if the exception happens before an assertion. Prefer `@Test(expected = ...)` or let the test method throw.

### Assertion style

- Rich failure messages almost always supplied:

    ```java
    assertEquals("MD5 of file(" + filename + ") counted: " + result
                 + " awaited: " + fullHash, fullHash, result);
    ```

    Copy this style for new tests so failures in parameterised runs identify the data row.
- Comparisons of complex objects frequently go through `toString()` for simplicity (see `fileVersionTest.testGetV`, `ExifReadWriteTest.testExifToMeta`). Do this only when the `toString` pattern is stable (see `.planning/codebase/CONVENTIONS.md` toString section).

## Integration Tests (DB-backed)

**Location:** `src/test/java/org/nyusziful/pictureorganizer/DAL/` and `src/test/java/org/nyusziful/pictureorganizer/Service/Backend/`.

**Pattern — switch to TEST database and truncate tables in `@Before`:**

```java
public class MediaDirectoryServiceTest {
    private static MediaDirectoryService mediaDirectoryService;

    @Before
    public void setup() {
        JPAConnection.setMode(JPAConnection.DBMode.TEST);
        EntityManager entityManager = JPAConnection.getInstance().getEntityManager();
        entityManager.createNativeQuery(
            "TRUNCATE TABLE testpictureorganizer.media_directory"
        ).executeUpdate();
        mediaDirectoryService = MediaDirectoryService.getInstance();
    }

    @Test
    public void writeTest() {
        MediaDirectory test = new MediaDirectory();
        test.setFirstDate(LocalDate.of(1982, 10, 24));
        // ...
        mediaDirectoryService.persistMediaDirectory(test);
        assertEquals(test, mediaDirectoryService.getMediaDirectoryById(test.getId()));
        mediaDirectoryService.deleteMediaDirectory(test);
        assertNull(mediaDirectoryService.getMediaDirectoryById(test.getId()));
    }
}
```

Source: `src/test/java/org/nyusziful/pictureorganizer/DAL/MediaDirectoryServiceTest.java`.

**Rules for DB-backed tests:**
- **Always** call `JPAConnection.setMode(JPAConnection.DBMode.TEST)` in `@Before` — otherwise the test hits the production schema (`JPAConnection.java:28-32` constructs the JDBC URL by prefixing `test` / `dev` / empty to `pictureorganizer`).
- The schema name is `testpictureorganizer` (note `test` + `pictureorganizer`). Reference it fully-qualified in native queries: `TRUNCATE TABLE testpictureorganizer.media_file_instance`.
- Truncate in dependency order (see `MediaTest.switchToTestDB` lines 28-36): `media_file_instance` → `media_image` → `media_file_version` → `media_file` → `image` → `folder`.
- Tests currently require a **running local MySQL** at `jdbc:mysql://127.0.0.1:3306/testpictureorganizer` with credentials `picture` / `picture` from `persistence.xml`. No in-memory / H2 / Testcontainers alternative.

**`MediaTest` end-to-end pattern (the most comprehensive test):**
- `@Before switchToTestDB` truncates, creates a temp dir via `Files.createTempDirectory("JavaUnitTest")`, and copies test resources from the `RAWJPG` classpath folder into it.
- `@After checkReadOnly` verifies via `Files.mismatch` that the service layer did not mutate the source files.
- Uses `switch (mediaFileInstance.getFilename())` to branch assertions per test file — `ARWJPG1.JPG`, `WB1_1.ARW`, `BROKEN1.JPG`, etc.
- Mutates files mid-test (`rotate(...)` copies a pre-rotated JPG over a working copy) to assert version tracking.
- Source: `src/test/java/org/nyusziful/pictureorganizer/Service/Backend/MediaTest.java`.

## Test Data / Fixtures

**No dedicated factory / fixture layer.**

- Parameterised tests embed literal data arrays inside the test class (`TESTS` static constant).
- Integration tests copy real image files off the classpath into a temp directory.
- Expected values (hashes, EXIF metadata) are hard-coded from prior known-good runs — **golden-master testing**.
- There is no "resource checker" — missing resources fail late with `NullPointerException` on `.getFile()` (`getClass().getClassLoader().getResource(filename).getFile()` pattern, e.g., `HashTest.java:82`).

**Resource filenames reference:**
Filenames encode `V{nameVersion}_{picSet}{date}({tz})({weekday})-{hash}-{origHash}-{originalName}` (decoded by `FileNameFactory.getV`). When adding new fixtures, mirror this format so `fileVersionTest` round-trips correctly.

## Coverage

**Requirements:** None enforced.

**View coverage:** No built-in. To measure locally, add JaCoCo ad-hoc:
```bash
mvn org.jacoco:jacoco-maven-plugin:0.8.12:prepare-agent test org.jacoco:jacoco-maven-plugin:0.8.12:report
```
(Output lands under `target/site/jacoco/index.html`.)

## Test Types

**Unit tests (pure):** `HashTest`, `JPGHashTest`, `fileRenamerTest`, `fileVersionTest`, `mediaDirectoryTest` — no DB or network; only filesystem reads of bundled binaries.

**Integration tests (DB required):** `ImageServiceTest`, `MediaDirectoryServiceTest`, `MediaTest`, `ExifBlobTest`.

**EXIF library cross-check:** `ExifPreReadTest` compares `ExifReadWriteIMR.readMeta` against `readMetaNew` for regression between two implementations.

**E2E / UI tests:** None. JavaFX controllers under `src/main/java/org/nyusziful/pictureorganizer/UI/` have no TestFX coverage.

## Common Patterns

**Async testing:** Not applicable — the production code uses `javafx.concurrent.Task` (`ProgressLeakingTask`) but no tests exercise the async path.

**Error testing:**
- Negative assertions use `assertNull(result)` (e.g., `mediaDirectoryTest.testMediaDirectory` checks `assertNull(result)` when the folder name does not match the expected date-range format).
- `IllegalArgumentException` is caught and swallowed in-test to verify the constructor short-circuits, then `assertNull` confirms no state. This is a convention — prefer `@Test(expected = IllegalArgumentException.class)` in new tests.

**Classpath resource loading (boilerplate — copy exactly):**
```java
File file = new File(this.getClass().getClassLoader()
                     .getResource(filename).getFile());
```
or
```java
ClassLoader classLoader = ExifReadWriteIMR.class.getClassLoader();
URL resource = classLoader.getResource(fileName);
File file = new File(resource.getFile());
```

## Ignored / Disabled Tests

Tests currently marked `@Ignore` (grep across test files):
- `MediaDirectoryServiceTest.getAllTest`
- `ImageServiceTest.testBiDirectional`
- `ExifReadWriteTest.testGetExif`, `testUpdateExif`, `testReadMeta`
- `ExifReadTest.testReadMeta`

Commented-out `@Test` (even more hidden):
- `ExifReadTest.testGetExif` (prefix `//`)
- `ExifWriteTest.testUpdateExif` (prefix `//`)
- `JPGHashTest.readDigest` (prefix `//`)

New tests should not be committed in `@Ignore`ed / commented-out state unless an issue is linked explaining why.

## What to Do When Adding a New Test

1. Match naming: `<ClassUnderTest>Test.java` in a mirror of the main package path.
2. If the subject takes a handful of file inputs, use the `@RunWith(Parameterized.class)` + `static class TestData` + `static final TestData[] TESTS` pattern.
3. If the subject hits Hibernate, start `@Before` with `JPAConnection.setMode(JPAConnection.DBMode.TEST);` and truncate relevant tables via `entityManager.createNativeQuery("TRUNCATE TABLE testpictureorganizer.<t>").executeUpdate();`.
4. Load binary fixtures via `this.getClass().getClassLoader().getResource(name).getFile()`. Place new fixtures under `src/test/resources/` (even though the directory is not yet tracked in git — create it and commit the fixture with it).
5. Provide rich `assertEquals` messages that include both the input identifier (filename) and the actual vs. expected values — essential in parameterised output.
6. Do **not** catch checked exceptions with `e.printStackTrace()`; either rethrow (`throws ...`) or use `@Test(expected = ...)`.
7. Do **not** add `System.out.println` calls; use `LOG` (SLF4J) if diagnostics are required.

---

*Testing analysis: 2026-04-19*
