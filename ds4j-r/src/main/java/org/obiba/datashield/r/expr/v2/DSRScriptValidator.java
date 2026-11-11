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
    return node.childrenAccept(this, data);
  }

  @Override
  public Object visit(ASTBinaryOp node, Object data) {
    if (isNamedArgument(node) && !isArgumentName(node.jjtGetChild(0))) {
      throw new InvalidScriptException("Invalid argument name.");
    }
    // Ranges are limited to numbers, as R's ':' operator would also accept symbols and function calls.
    if (":".equals(node.jjtGetValue()) && !(isNumeric(node.jjtGetChild(0)) && isNumeric(node.jjtGetChild(1)))) {
      throw new InvalidScriptException("Range operands must be numbers.");
    }
    return node.childrenAccept(this, data);
  }

  @Override
  public Object visit(ASTfuncCall node, Object data) {
    return node.childrenAccept(this, data);
  }

  @Override
  public Object visit(ASTsymbol node, Object data) {
    return null;
  }

  @Override
  public Object visit(ASTstring node, Object data) {
    return null;
  }

  static boolean isNamedArgument(ASTBinaryOp node) {
    return "=".equals(node.jjtGetValue()) && node.jjtGetParent() instanceof ASTfuncCall;
  }

  // A symbol is either a name, starting with a letter, or a number.
  private static boolean isNumeric(Node node) {
    if (node instanceof ASTsymbol) return !((ASTsymbol) node).jjtGetValue().toString().matches("[a-zA-Z].*");
    if (!(node instanceof ASTBinaryOp)) return false;
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      if (!isNumeric(node.jjtGetChild(i))) return false;
    }
    return true;
  }

  // R only accepts a name or a string there: a symbol node can also hold a number or a column (D$A).
  private static boolean isArgumentName(Node node) {
    return node instanceof ASTstring
        || node instanceof ASTsymbol && ((ASTsymbol) node).jjtGetValue().toString().matches("[a-zA-Z][\\w.]*");
  }
}
