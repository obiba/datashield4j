/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.datashield.r.expr.v2;


import org.obiba.datashield.r.expr.InvalidScriptException;

import java.util.Arrays;

class DSRScriptValidator implements DataShieldGrammarVisitor {

  public DSRScriptValidator() {
  }

  public static DSRScriptValidator of(DSRScriptValidator... validators) {
    return of(Arrays.asList(validators));
  }

  public static DSRScriptValidator of(final Iterable<DSRScriptValidator> validators) {
    return new DSRScriptValidator() {
      @Override
      public void validate(SimpleNode node) throws InvalidScriptException {
        for (DSRScriptValidator validator : validators) {
          validator.validate(node);
        }
      }
    };
  }

  public void validate(SimpleNode node) throws InvalidScriptException {
    node.jjtAccept(this, null);
  }

  @Override
  public Object visit(SimpleNode node, Object data) {
    return null;
  }

  @Override
  public Object visit(ASTroot node, Object data) {
    return null;
  }

  @Override
  public Object visit(ASTBinaryOp node, Object data) {
    return null;
  }

  @Override
  public Object visit(ASTfuncCall node, Object data) {
    return null;
  }

  @Override
  public Object visit(ASTsymbol node, Object data) {
    return null;
  }

  @Override
  public Object visit(ASTstring node, Object data) {
    return null;
  }
}
