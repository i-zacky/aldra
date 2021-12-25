# aldra-api

## Setup

### Generate Controller / DTO classes by OpenAPI Generator

```sh
$ ./gradlew :aldra-api:oaGenerate

# the following steps will no longer be necessary
$ ./gradlew :aldra-api:spotlessApply
```

## Launch Application

### Gradle 

```sh
$ ./gradlew :aldra-api:bootRun
```

### Docker

```sh
$ ./gradlew :aldra-api:bootBuildImage --imageName {repository:tag}
$ docker run {repository:tag} --name aldra-api -p 8080:8080 \
    --network aldra \
    -e DB_JDBC_DRIVER='org.postgresql.Driver' \
    -e DB_JDBC_URL='jdbc:postgresql://aldra-db:5432/aldra_dev' \
    -e DB_USER='{username}' \
    -e DB_PASSWORD='{password}'
```
