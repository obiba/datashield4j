package org.obiba.datashield.core;

public interface DSServerRegistry {

  /**
   * Soft start of the underlying servers.
   */
  void start();

  /**
   * Soft stop of the underlying servers.
   */
  void stop();

  /**
   * Add or update a server in the registry.
   *
   * @param server
   */
  void register(DSServer server);

  /**
   * Remove a server from the registry (ignored if it does not exists).
   *
   * @param server
   */
  void unregister(DSServer server);

  /**
   * An underlying load balancing strategy will pick the next server to be used for creating a new DataSHIELD computation unit.
   *
   * @return
   */
  DSServer nexAvailableServer();

}
