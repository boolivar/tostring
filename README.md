# ToString Agent

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
