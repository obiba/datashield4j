package org.obiba.datashield.core.impl;

import org.obiba.datashield.core.DSServer;
import org.obiba.datashield.core.DSServerRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class DefaultDSServerRegistry implements DSServerRegistry {

  private final List<DSServer> servers = new ArrayList<>();

  private int nextServerPos = 0;

  @Override
  public void start() {
    servers.stream().filter(s -> !s.isRunning()).forEach(DSServer::start);
  }

  @Override
  public void stop() {
    servers.stream().filter(DSServer::isRunning).forEach(DSServer::stop);
  }

  @Override
  public void register(DSServer server) {
    if (!servers.contains(server)) servers.add(server);
  }

  @Override
  public void unregister(DSServer server) {
    servers.remove(server);
  }

  /**
   * Sequential server allocation.
   *
   * @return
   */
  @Override
  public DSServer nextAvailableServer() {
    if (servers.isEmpty()) throw new NoSuchElementException("No server is available");
    DSServer nextServer;
    if (nextServerPos >= servers.size())
      nextServerPos = 0;
    nextServer = servers.get(nextServerPos);
    nextServerPos++;
    return nextServer;
  }

  protected List<DSServer> getServers() {
    return servers;
  }

}
