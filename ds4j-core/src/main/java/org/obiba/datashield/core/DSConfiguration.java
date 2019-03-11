/*
 * Copyright (c) 2019 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.datashield.core;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * The configuration is a set of methods and options.
 */
public interface DSConfiguration {

  /**
   * Get the configured methods for the provided type.
   *
   * @param type
   * @return
   */
  DSEnvironment getEnvironment(DSMethodType type);

  Iterable<DSOption> getOptions();

  boolean hasOption(String name);

  boolean hasOptions();

  DSOption getOption(String name) throws NoSuchElementException;

  void addOrUpdateOption(String name, String value);

  void removeOption(String name);

}
