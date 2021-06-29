# DataSHIELD4J

[![Build Status](https://travis-ci.com/obiba/datashield4j.svg?branch=master)](https://travis-ci.com/obiba/datashield4j)

DataSHIELD core classes for implementing the DataSHIELD platform in a data repository.

Reference implementation is [Opal](https://github.com/obiba/opal).

Learn more about [DataSHIELD](http://www.datashield.ac.uk/).

See also [DataSHIELD Interface (DSI)](https://github.com/datashield/DSI) for identifying the web services needed to implement the DataSHIELD platform.

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

* `v1` (since ds4j 1.0.0), is the original R parser, more permissive.
* `v2` (since ds4j 2.0.0), is the most recent R parser, more restrictive regarding the R subset syntax (square brackets are not allowed).

The entry point class is the [RScriptGeneratorFactory](https://github.com/obiba/datashield4j/blob/master/ds4j-r/src/main/java/org/obiba/datashield/r/expr/RScriptGeneratorFactory.java)
that will validate the submitted R code and will rewrite the function calls according to the DataSHIELD configuration
(aggregate and assign function name mapping).

What is currently NOT included (because it is too data repository specific):

* R server session management
* data assignment from database to the R session
* DataSHIELD related web services

## Usage

Available in OBiBa's Maven repository: https://obiba.jfrog.io/artifactory/libs-release-local/

```
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
