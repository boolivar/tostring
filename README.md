# ToString Agent

[![CI](https://github.com/boolivar/tostring/actions/workflows/ci.yml/badge.svg)](https://github.com/boolivar/tostring/actions/workflows/ci.yml)
[![GitHub Release](https://img.shields.io/github/v/release/boolivar/tostring?sort=semver)](https://github.com/boolivar/tostring/releases/latest)

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=boolivar_tostring&metric=coverage)](https://sonarcloud.io/summary/new_code?id=boolivar_tostring)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=boolivar_tostring&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=boolivar_tostring)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=boolivar_tostring&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=boolivar_tostring)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=boolivar_tostring&metric=bugs)](https://sonarcloud.io/summary/new_code?id=boolivar_tostring)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=boolivar_tostring&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=boolivar_tostring)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=boolivar_tostring&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=boolivar_tostring)

## Overview
The `ToString Agent` is a Java instrumentation agent designed to enhance `toString()` method representation in Java applications.

It dynamically instruments Java classes during runtime to provide informative string representations for objects.

## Getting Started

### Prerequisites
- Java 8 or higher.
- Java application to instrument.

### How to Use
Using the `ToString Agent` is straightforward. You simply need to add the `-javaagent` option when starting your Java application.

#### Steps:
1. Build or download the `ToString Agent` JAR file.
2. Run your Java application with the `-javaagent` option pointing to the JAR file.

```bash
java -javaagent:path/to/tostring-agent.jar -jar your-application.jar
```

Replace path/to/tostring-agent.jar with the actual path to the agent JAR file.

That's it! The agent will automatically instrument your classes at runtime.

## Agent arguments

You can control instrumentation by supplying arguments to the agent, separated by commas:

- **If no arguments are given**, all eligible classes without a `toString()` method will be instrumented.

- **Class name regex (optional):** A regular expression for class names to include.

- **--force (optional):** If supplied, the agent will instrument classes even if they already declare a `toString()` method (normally, it only adds a `toString()` if absent).

### Examples

Instrument all classes, regardless of existing `toString()`:
```bash
java -javaagent:path/to/tostring-agent.jar=--force -jar your-application.jar
```

Instrument only `my.app.dto.Model` class:
```bash
java -javaagent:path/to/tostring-agent.jar=my\.app\.dto\.Model -jar your-application.jar
```

Instrument only classes under `my.app.dto` and override existing `toString()` methods:
```bash
java -javaagent:path/to/tostring-agent.jar=my\.app\.dto\..*,--force -jar your-application.jar
```
