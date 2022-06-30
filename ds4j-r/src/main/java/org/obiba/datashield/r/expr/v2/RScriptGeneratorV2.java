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


import org.obiba.datashield.core.DSEnvironment;
import org.obiba.datashield.core.DSMethod;
import org.obiba.datashield.core.DSMethodType;
import org.obiba.datashield.r.expr.RScriptGenerator;

import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Generates RScript from a DataSHIELD script.
 */
public class RScriptGeneratorV2 implements DataShieldGrammarVisitor, RScriptGenerator {

  private final DSEnvironment environment;

  private final SimpleNode scriptAst;

  private final Map<String, String> mappedFunctions = new LinkedHashMap<>();

  public RScriptGeneratorV2(DSEnvironment environment, String script) throws ParseException {
    this.environment = environment;
    DSRScriptValidator validator = environment.getMethodType() != null && environment.getMethodType().equals(DSMethodType.AGGREGATE) ?
        DSRScriptValidator.of(new FirstNodeInvokesFunctionValidator(), new NoBinaryOpsValidator()) :
        new DSRScriptValidator();
    this.scriptAst = new DataShieldGrammar(new StringReader(script)).root();
    validator.validate(scriptAst);
  }

  public String toScript() {
    StringBuilder sb = new StringBuilder();
    scriptAst.jjtAccept(this, sb);
    return sb.toString();
  }
  @Override
  public Object visit(ASTsymbol node, Object data) {
    StringBuilder sb = (StringBuilder) data;
    sb.append(node.value);
    return data;
  }

  @Override
  public Object visit(ASTstring node, Object data) {
    StringBuilder sb = (StringBuilder) data;
    sb.append(node.value);
    return data;
  }

  @Override
  public Object visit(ASTfuncCall node, Object data) {
    StringBuilder sb = (StringBuilder) data;
    sb.append(findMethod(node.value.toString()).invoke(environment.getMethodType())).append("(");
    visitChildren(node, sb);
    sb.append(")");
    return sb;
  }

  @Override
  public Object visit(ASTBinaryOp node, Object data) {
    StringBuilder sb = (StringBuilder) data;
    if ("=".equals(node.value) && (node.jjtGetParent() instanceof ASTfuncCall)) {
      node.jjtGetChild(0).jjtAccept(this, sb);
      sb.append(" = ");
      node.jjtGetChild(1).jjtAccept(this, sb);
    } else {
      sb.append("base::'").append(node.value).append("'");
      sb.append("(");
      visitChildren(node, sb);
      sb.append(")");
    }
    return sb;
  }

  @Override
  public Object visit(ASTroot node, Object data) {
    StringBuilder sb = (StringBuilder) data;
    node.childrenAccept(this, data);
    return sb;
  }

  @Override
  public Object visit(SimpleNode node, Object data) {
    return node.childrenAccept(this, data);
  }

  private void visitChildren(Node node, StringBuilder sb) {
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      Node child = node.jjtGetChild(i);
      if (i > 0) sb.append(", ");
      child.jjtAccept(this, sb);
    }
  }

  private DSMethod findMethod(String name) {
    DSMethod method = environment.getMethod(name);
    mappedFunctions.put(name, method.invoke(environment.getMethodType()));
    return method;
  }

  @Override
  public Map<String, String> getMappedFunctions() {
    if (mappedFunctions.isEmpty()) toScript();
    return mappedFunctions;
  }
}
