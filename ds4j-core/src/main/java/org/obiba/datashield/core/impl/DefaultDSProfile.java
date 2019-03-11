package org.obiba.datashield.core.impl;

import org.obiba.datashield.core.DSConfiguration;
import org.obiba.datashield.core.DSProfile;
import org.obiba.datashield.core.DSServerRegistry;

public class DefaultDSProfile implements DSProfile {

  private DSServerRegistry serverRegistry;

  private DSConfiguration configuration;

  @Override
  public String getName() {
    return null;
  }

  public void setServerRegistry(DSServerRegistry serverRegistry) {
    this.serverRegistry = serverRegistry;
  }

  @Override
  public DSServerRegistry getServerRegistry() {
    return null;
  }

  public void setConfiguration(DSConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public DSConfiguration getConfiguration() {
    return null;
  }
}
