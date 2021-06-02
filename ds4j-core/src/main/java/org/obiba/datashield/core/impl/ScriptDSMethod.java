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

import java.util.Objects;
import org.obiba.datashield.core.DSMethod;
import org.obiba.datashield.core.DSMethodType;

/**
 * A DataSHIELD method defined by a script.
 */
public class ScriptDSMethod implements DSMethod {

  private String name;

  private String script;

  public ScriptDSMethod() {

  }

  public ScriptDSMethod(String name, String script) {
    this.name = name;
    this.script = script;
  }

  @Override
  public String getName() {
    return name;
  }

  public String getScript() {
    return script;
  }

  @Override
  public boolean hasPackage() {
    return false;
  }

  @Override
  public String invoke(DSMethodType env) {
    return env.symbol() + "$" + getName();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScriptDSMethod that = (ScriptDSMethod) o;
    return Objects.equals(name, that.name) &&
        Objects.equals(script, that.script);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, script);
  }
}
