# Getting Started

### Guides

This is SQLite JDBC Driver with Encryption Tester to demonstrate how to 

1. Encrypt SQLite database file
2. Decrypt SQLite database file

### Run the app

First, you must generate the right JDBC driver (jar file for Linux, dll for Windows),
and put them inside the project's /libs folder.  Create libs folder if not there.

In Linux:
```./gradlew bootRun```

In Windows:
```gradlew.bat bootRun```

You should be able to see the test result

### Run the performance test

Performance test on creating encrypted data is added to the test suite.  You can run

```./gradlew test```

to see the test result.  You must include a source database file called plaintext.db inside project root.