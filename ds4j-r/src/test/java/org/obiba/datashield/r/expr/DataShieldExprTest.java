/*
 * Copyright (c) 2018 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.datashield.r.expr;

import org.junit.Test;

import java.io.StringReader;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataShieldExprTest {

  private static final Map<String, String> tests = Collections.unmodifiableMap(Stream.of(
      new AbstractMap.SimpleEntry<>("A symbol", "A"),
      new AbstractMap.SimpleEntry<>("A number", "1.0"),
      new AbstractMap.SimpleEntry<>("An integer", "5L"),
      new AbstractMap.SimpleEntry<>("A negative number", "-0.43151402098822"),
      new AbstractMap.SimpleEntry<>("An embedded symbol", "A$B$C.D"),
      new AbstractMap.SimpleEntry<>("A subset symbol", "A[2,1]"),
      new AbstractMap.SimpleEntry<>("An open row subset symbol", "A[,1]"),
      new AbstractMap.SimpleEntry<>("An open column subset symbol", "A[1,]"),
      new AbstractMap.SimpleEntry<>("A subset symbol with range", "A[,1:2]"),
      new AbstractMap.SimpleEntry<>("A subset symbol with function call", "D[,func(D[,1])]"),
      new AbstractMap.SimpleEntry<>("A subset symbol with spaces", "A[ , 1 ]"),
      new AbstractMap.SimpleEntry<>("An empty subset symbol", "A[]"),
      new AbstractMap.SimpleEntry<>("An almost empty subset symbol", "A[,]"),
      new AbstractMap.SimpleEntry<>("A subset value symbol", "A[[1]]"),
      new AbstractMap.SimpleEntry<>("A formula", "A ~ B"),
      new AbstractMap.SimpleEntry<>("A function invocation", "A()"),
      new AbstractMap.SimpleEntry<>("An operator on symbols", "A + B"),
      new AbstractMap.SimpleEntry<>("Operator chaining", "A + B * C"),
      new AbstractMap.SimpleEntry<>("Operator on functions", "A() + B * C"),
      new AbstractMap.SimpleEntry<>("A formula with operators", "A ~ B + (C * D)^4 : E %in% F"),
      new AbstractMap.SimpleEntry<>("Grouping", "(A + B) * (C - D)"),
      new AbstractMap.SimpleEntry<>("Function with a single parameter", "A(B)"),
      new AbstractMap.SimpleEntry<>("Function with a subset parameter", "A(B[,2])"),
      new AbstractMap.SimpleEntry<>("Function with a subset parameter and a simple parameter", "A(B[,2], C)"),
      new AbstractMap.SimpleEntry<>("Function with a function invocation as parameter", "A(B())"),
      new AbstractMap.SimpleEntry<>("Function with multiple parameters", "A(B, C)"),
      new AbstractMap.SimpleEntry<>("Function with multiple kinds of parameters", "A(B, C(), D, E(F(G/H)), A + B * C())"),
      new AbstractMap.SimpleEntry<>("Function with formula as argument", "glm(A ~ B + C:D, poisson)"),
      new AbstractMap.SimpleEntry<>("Function with named argument", "A(arg=x,another=y)"),
      new AbstractMap.SimpleEntry<>("Function with string argument", "A('this')"),
      new AbstractMap.SimpleEntry<>("Function with double quoted string argument", "A(\"this\")"),
      new AbstractMap.SimpleEntry<>("Function with numerical string argument", "A('123')"),
      new AbstractMap.SimpleEntry<>("Function with alphanumerical string argument and some special characters", "A('this1_that.this-that1')"),
      new AbstractMap.SimpleEntry<>("Function with a list of colon separated numerical string argument", "A('1.2:1.3:-8.5')"),
      new AbstractMap.SimpleEntry<>("Function with a list of comma separated numerical string argument", "A('1.2,1.3,-8.5')"),
      new AbstractMap.SimpleEntry<>("Function with a list of semi colon separated numerical string argument", "A('1.2;1.3;-8.5')"),
      new AbstractMap.SimpleEntry<>("Function with data frame column name string argument", "A('D$abc')"),
      new AbstractMap.SimpleEntry<>("Function with data frame index string argument", "A('D[123]')"))
      .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));

  @Test
  public void test_testCases() throws ParseException {
    for (String msg : tests.keySet()) {
      doTest(msg, tests.get(msg));
    }

  }

  @Test(expected = TokenMgrError.class)
  public void test_funcCallInString() throws ParseException {
    doTest("Function with function call in string argument", "A('this(that)')");
  }

  @Test(expected = TokenMgrError.class)
  public void test_spaceInString() throws ParseException {
    doTest("Function with space in string argument", "A(\"this that\")");
  }

  @Test(expected = TokenMgrError.class)
  public void test_slashInString() throws ParseException {
    doTest("Function with slash in string argument", "A('this/that')");
  }

  @Test(expected = TokenMgrError.class)
  public void test_backslashInString() throws ParseException {
    doTest("Function with backslash in string argument", "A('this\\that')");
  }

  @Test(expected = TokenMgrError.class)
  public void test_operatorInString() throws ParseException {
    doTest("Function with operator in string argument", "A('this+that')");
  }

  @Test(expected = TokenMgrError.class)
  public void test_equalInString() throws ParseException {
    doTest("Function with equal in string argument", "A('this=that')");
  }

  @Test(expected = TokenMgrError.class)
  public void test_assignInString() throws ParseException {
    doTest("Function with operator in string argument", "A('this<-that')");
  }

  @Test(expected = TokenMgrError.class)
  public void test_mixedQuoteInString1() throws ParseException {
    doTest("Function with mixed quotes in string argument", "A('this\")");
  }

  @Test(expected = TokenMgrError.class)
  public void test_mixedQuoteInString2() throws ParseException {
    doTest("Function with mixed quotes in string argument", "A(\"that')");
  }

  private void doTest(String msg, String test) throws ParseException {
    System.out.println(msg + ": " + test);
    DataShieldGrammar g = new DataShieldGrammar(new StringReader(test));
    SimpleNode expr = g.root();
    expr.dump("");
  }

  @Test
  public void test_visitor() throws ParseException {
    DataShieldGrammarVisitor visitor = new DataShieldGrammarVisitor() {

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
        sb.append(node.value).append("( ");
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
          Node child = node.jjtGetChild(i);
          if (i > 0) sb.append(' ');
          child.jjtAccept(this, sb);
        }
        sb.append(" )");
        return sb;
      }

      @Override
      public Object visit(ASTsubsetCall node, Object data) {
        StringBuilder sb = (StringBuilder) data;
        sb.append(node.value).append("[ ");
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
          Node child = node.jjtGetChild(i);
          if (i > 0) sb.append(' ');
          child.jjtAccept(this, sb);
        }
        sb.append(" ]");
        return sb;
      }

      @Override
      public Object visit(ASTBinaryOp node, Object data) {
        StringBuilder sb = (StringBuilder) data;
        sb.append(node.value).append("( ");
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
          Node child = node.jjtGetChild(i);
          if (i > 0) sb.append(' ');
          child.jjtAccept(this, sb);
        }
        sb.append(" )");
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
    };

    String test = "A %*% (B(C ~ A + (G+D)^2 : H, C * D()) * 1/F)";
    DataShieldGrammar g = new DataShieldGrammar(new StringReader(test));
    StringBuilder b = (StringBuilder) g.root().jjtAccept(visitor, new StringBuilder());
    System.out.println(test + " --> " + b);

  }
}
