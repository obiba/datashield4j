/*
 * Copyright (c) 2019 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.datashield.core.impl;

import org.obiba.datashield.core.DSConfiguration;
import org.obiba.datashield.core.DSEnvironment;
import org.obiba.datashield.core.DSMethodType;
import org.obiba.datashield.core.DSOption;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultDSConfiguration implements DSConfiguration {

  private String rParserVersion;

  private List<DSEnvironment> environments = new ArrayList<>();

  private final Map<String, String> options = new HashMap<>();

  @Override
  public String getRParserVersion() {
    return rParserVersion == null ? "v1" : rParserVersion;
  }

  @Override
  public synchronized DSEnvironment getEnvironment(DSMethodType type) {
    for (DSEnvironment environment : environments) {
      if (environment.getMethodType() == type) return environment;
    }
    DSEnvironment e = makeEnvironment(type);
    environments.add(e);
    return e;
  }

  protected DSEnvironment makeEnvironment(DSMethodType type) {
    return new DefaultDSEnvironment(type);
  }

  @Override
  public Iterable<DSOption> getOptions() {
    return options.keySet().stream().map(k -> new DefaultDSOption(k, options.get(k)))
        .collect(Collectors.toList());
  }

  @Override
  public boolean hasOption(String name) {
    return options.containsKey(name);
  }

  @Override
  public boolean hasOptions() {
    return !options.isEmpty();
  }

  @Override
  public DSOption getOption(String name) {
    if (options.containsKey(name)) {
      return new DefaultDSOption(name, options.get(name));
    }

    throw new NoSuchElementException(name + " option does not exists");
  }

  @Override
  public void addOrUpdateOption(String name, String value) {
    addOption(name, value, true);
  }

  @Override
  public void removeOption(String name) {
    if (hasOption(name)) options.remove(name);
  }

  protected void addOption(String name, String value, boolean overwrite) {
    if (!overwrite && hasOption(name)) return;
    options.put(name, value);
  }

}
