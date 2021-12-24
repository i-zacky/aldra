# aldra-database

## Setup

### Migrate database by Flyway

```sh
$ ./gradlew :aldra-database:flywayMigrate
```

### Generate Entity / Mapper classes by Mybatis Generator

```sh
$ ./gradlew :aldra-database:mbGenerate
```
