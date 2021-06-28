/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.datashield.r.expr.v1;

import org.obiba.datashield.r.expr.InvalidScriptException;

class NoBinaryOpsValidator extends DSRScriptValidator {

  @Override
  public Object visit(ASTBinaryOp node, Object data) {
    throw new InvalidScriptException("Binary operators are not allowed.");
  }

}
