/*
 * Copyright (c) 2018 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.datashield.core.impl;

import org.obiba.datashield.core.DSMethod;
import org.obiba.datashield.core.DSMethodType;

/**
 * A DataSHIELD method declared by a package.
 */
public class PackagedFunctionDSMethod implements DSMethod {

  private String name;

  private String function;

  /**
   * Package name that defined this method.
   */
  private String pack;

  /**
   * Version of the package.
   */
  private String version;

  public PackagedFunctionDSMethod() {
  }

  public PackagedFunctionDSMethod(String name, String function) {
    this(name, function, null, null);
  }

  public PackagedFunctionDSMethod(String name, String function, String pack, String version) {
    this.name = name;
    this.function = function;
    this.pack = pack;
    this.version = version;
  }

  @Override
  public String getName() {
    return name;
  }

  public String getFunction() {
    return function;
  }

  public boolean hasPackage() {
    return pack != null;
  }

  public String getPackage() {
    return pack;
  }

  public String getVersion() {
    return version;
  }

  @Override
  public String invoke(DSMethodType env) {
    return getFunction();
  }
}
