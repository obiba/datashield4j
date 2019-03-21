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

  /**
   * Get the options.
   *
   * @return
   */
  Iterable<DSOption> getOptions();

  /**
   * Verify a option exists by its name.
   *
   * @param name
   * @return
   */
  boolean hasOption(String name);

  /**
   * Verify if there are any options.
   *
   * @return
   */
  boolean hasOptions();

  /**
   * Get a specific option.
   *
   * @param name
   * @return
   * @throws NoSuchElementException
   */
  DSOption getOption(String name) throws NoSuchElementException;

  /**
   * Add or update an option.
   *
   * @param name
   * @param value
   */
  void addOrUpdateOption(String name, String value);

  /**
   * Remove an option with the given name.Silently ignored when no such option exists.
   *
   * @param name
   */
  void removeOption(String name);

}
