package org.obiba.datashield.core;

/**
 * A DataSHIELD session is a user specific computation unit on the DataSHIELD server.
 */
public interface DSSession {

  /**
   * Get the user name for which the session has been created.
   *
   * @return
   */
  String getUser();

  /**
   * Get the DataSHIELD server where the session lives.
   *
   * @return
   */
  DSServer getServer();
}
