# DataSHIELD4J

[![Build Status](https://travis-ci.com/obiba/datashield4j.svg?branch=master)](https://travis-ci.com/obiba/datashield4j)

DataSHIELD core classes for implementing the DataSHIELD platform in a data repository.

Reference implementation is [Opal](https://www.obiba.org/pages/products/opal/).

Learn more about [DataSHIELD](https://www.datashield.org/).

See also [DataSHIELD Interface (DSI)](https://datashield.github.io/DSI/) for identifying the web services needed to implement the DataSHIELD platform.

## DataSHIELD Core

This package defines the core classes of the DataSHIELD configuration, mainly:

* methods (the allowed functions, either package specific or scripted)
* method types (either assignment, or aggregation)
* options (the options set at session start)

These classes are not bound to a specific implementation language, despite only R is used at the moment.

## DataSHIELD R

Each DataSHIELD request must be parsed to identify and resolve the function calls and the allowed language elements. This 
package provides an R parser, based on a subset of the R language. This parser is to be used to secure the access to the 
data and to apply the DataSHIELD configuration before forwarding the request to the R server session.

The R parser comes with several versions (which R parser version to be used must be specified):

| Parser Version | Description |
| -------------- | ----------- |
| `v1` |  The original R parser (since ds4j 1.0.0), used until [Opal](https://github.com/obiba/opal) 4.1. |
| `v2` |  The most recent R parser (since ds4j 2.0.0), more restrictive regarding the R subset syntax (square brackets are not allowed). Default R parser since [Opal](https://github.com/obiba/opal) 4.2. |

What is currently NOT included (because it is too data repository specific):

* R server session management
* data assignment from database to the R session
* DataSHIELD related web services

## Usage

### Maven

Available in OBiBa's Maven repository: https://obiba.jfrog.io/artifactory/libs-release-local/

```xml
  <dependency>
    <groupId>org.obiba.datashield</groupId>
    <artifactId>ds4j-core</artifactId>
    <version>${ds4j.version}</version>
  </dependency>
  <dependency>
    <groupId>org.obiba.datashield</groupId>
    <artifactId>ds4j-r</artifactId>
    <version>${ds4j.version}</version>
  </dependency>
```

### Java

You can use the helper classes to define your `DSConfiguration` and store it in a file or a database. 

The entry point class is the [RScriptGeneratorFactory](https://github.com/obiba/datashield4j/blob/master/ds4j-r/src/main/java/org/obiba/datashield/r/expr/RScriptGeneratorFactory.java)
that will validate the submitted R code and will rewrite the function calls according to the DataSHIELD configuration
(aggregate and assign function name mapping).

Then use the `DSConfiguration` when receiving a R script request. Example code:

```java
// * rParserVersion: one of v1 (legacy) or v2 (recommended)
// * dsEnvironment: a DSEnvironment object that defines the allowed function calls
//   and the mapping of these functions to internal ones
// * script: the submitted R script
RScriptGenerator rScriptGenerator = RScriptGeneratorFactory.make(rParserVersion, dsEnvironment, script);

// script was validated, rewrite it with internal functions
String scriptToExec = rScriptGenerator.toScript();
```
