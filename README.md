# Albrecht Drais

Project Template by Spring-boot & MyBatis

## Overview

### aldra-api

RESTful API application (use Spring-boot)

### aldra-batch

Batch application (use Spring Batch)

### aldra-common

Common implementations for aldra-api / aldra-batch

### aldra-database

Database implementation (use MyBatis)

## Requirements

### System / Tools

- Java11
- Docker
- direnv

## Setup

### Activate direnv

```sh
$ cp .envrc.origin .envrc
$ direnv allow
```

### Launch middleware

```sh
$ docker-compose build
$ docker-compose up -d
```
