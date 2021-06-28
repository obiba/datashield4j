package org.obiba.datashield.r.expr;

/**
 * Parse exception adapter.
 */
public class ParseException extends Exception {

  public ParseException(String message) {
    super(message);
  }

  public ParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
