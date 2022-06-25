# aldra-database

## Setup

### Migrate database by Flyway

```sh
$ ./gradlew :database:flywayMigrate
```

### Generate Entity / Mapper classes by Mybatis Generator

```sh
$ ./gradlew :database:mbGenerate
```
