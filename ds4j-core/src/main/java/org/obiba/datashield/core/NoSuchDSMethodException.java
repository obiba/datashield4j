/*
 * Copyright (c) 2018 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.datashield.core;

/**
 * Thrown when a method is not found, which is a major failure that should be reported to the end user.
 */
public class NoSuchDSMethodException extends RuntimeException {

  private static final long serialVersionUID = -1295544695602633502L;

  private final DSMethodType type;

  private final String name;

  public NoSuchDSMethodException(DSMethodType type, String name) {
    super("No such DataSHIELD '" + type + "' method with name: " + name);
    this.type = type;
    this.name = name;
  }

  public DSMethodType getType() {
    return type;
  }

  public String getName() {
    return name;
  }
}
