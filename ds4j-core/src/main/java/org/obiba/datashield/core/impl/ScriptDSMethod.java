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
  public String invoke(DSMethodType env) {
    return env.symbol() + "$" + getName();
  }

}