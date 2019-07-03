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

When a DataSHIELD request is received, it must be parsed to identify and resolve the function calls and the allowed language elements. This package provides a R parser, based on a subset of the R language. This parser is to be used to secure the access to the data and to apply the DataSHIELD configuration before forwarding the request to the R server session.

What is currently NOT included (because it is too data repository specific):

* R server session management
* data assignment from database to the R session
* DataSHIELD related web services

## Usage

Available in OBiBa's Maven repository: https://dl.bintray.com/obiba/maven

```
<dependency>
	<groupId>org.obiba.datashield</groupId>
	<artifactId>ds4j-core</artifactId>
	<version>${ds4j.version}</version>
  <groupId>org.obiba.datashield</groupId>
	<artifactId>ds4j-r</artifactId>
	<version>${ds4j.version}</version>
</dependency>
```
