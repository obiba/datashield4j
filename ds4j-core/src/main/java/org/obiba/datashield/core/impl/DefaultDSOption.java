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

import org.obiba.datashield.core.DSOption;

import java.util.Objects;

public class DefaultDSOption implements DSOption {

  private String name;

  private String value;

  public DefaultDSOption() {

  }

  public DefaultDSOption(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DefaultDSOption that = (DefaultDSOption) o;
    return Objects.equals(name, that.name) &&
        Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, value);
  }

  @Override
  public String toString() {
    return getName() + "=" + getValue();
  }
}
