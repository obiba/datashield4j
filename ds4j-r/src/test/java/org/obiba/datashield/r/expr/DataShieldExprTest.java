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

import org.junit.Assert;
import org.junit.Test;
import org.obiba.datashield.core.DSEnvironment;
import org.obiba.datashield.core.DSMethod;
import org.obiba.datashield.core.impl.DefaultDSEnvironment;
import org.obiba.datashield.core.impl.PackagedFunctionDSMethod;

import java.io.StringReader;

public class DataShieldExprTest {

  private static final TestCase[] tests = new TestCase[]{
      new TestCase("A symbol", "A"),
      new TestCase("A number", "1.0"),
      new TestCase("A number, small, exponential notation, US style", "1.0e-03"),
      new TestCase("A number, large, exponential notation, US style [1]", "1.0e+03"),
      new TestCase("A number, large, exponential notation, US style [2]", "1.0e3"),
      new TestCase("A number, small, exponential notation, French style", "1.0E-03"),
      new TestCase("A number, large, exponential notation, French style [1]", "1.0E+03"),
      new TestCase("A number, large, exponential notation, French style [2]", "1.0E3"),
      new TestCase("An integer", "5L"),
      new TestCase("A negative number", "-0.43151402098822"),
      new TestCase("A negative number, small, exponential notation, US style", "-1.0e-03"),
      new TestCase("A negative number, large, exponential notation, US style [1]", "-1.0e+03"),
      new TestCase("A negative number, large, exponential notation, US style [2]", "-1.0e3"),
      new TestCase("A negative number, small, exponential notation, French style", "-1.0E-03"),
      new TestCase("A negative number, large, exponential notation, French style [1]", "-1.0E+03"),
      new TestCase("A negative number, large, exponential notation, French style [2]", "-1.0E3"),
      new TestCase("A symbol, in single quote", "'A'"),
      new TestCase("A number, in single quote", "'1.0'"),
      new TestCase("A number, small, exponential notation, US style, in single quote", "'1.0e-03'"),
      new TestCase("A number, large, exponential notation, US style, in single quote [1]", "'1.0e+03'"),
      new TestCase("A number, large, exponential notation, US style, in single quote [2]", "'1.0e3'"),
      new TestCase("A number, small, exponential notation, French style, in single quote", "'1.0E-03'"),
      new TestCase("A number, large, exponential notation, French style, in single quote [1]", "'1.0E+03'"),
      new TestCase("A number, large, exponential notation, French style, in single quote [2]", "'1.0E3'"),
      new TestCase("An integer, in single quote", "'5L'"),
      new TestCase("A negative number, in single quote", "'-0.43151402098822'"),
      new TestCase("A negative number, small, exponential notation, US style, in single quote", "'-1.0e-03'"),
      new TestCase("A negative number, large, exponential notation, US style, in single quote [1]", "'-1.0e+03'"),
      new TestCase("A negative number, large, exponential notation, US style, in single quote [2]", "'-1.0e3'"),
      new TestCase("A negative number, small, exponential notation, French style, in single quote", "'-1.0E-03'"),
      new TestCase("A negative number, large, exponential notation, French style, in single quote [1]", "'-1.0E+03'"),
      new TestCase("A negative number, large, exponential notation, French style, in single quote [2]", "'-1.0E3'"),
      new TestCase("A symbol, in double quote", "\"A\""),
      new TestCase("A number, in double quote", "\"1.0\""),
      new TestCase("A number, small, exponential notation, US style, in double quote", "\"1.0e-03\""),
      new TestCase("A number, large, exponential notation, US style, in double quote [1]", "\"1.0e+03\""),
      new TestCase("A number, large, exponential notation, US style, in double quote [2]", "\"1.0e3\""),
      new TestCase("A number, small, exponential notation, French style, in double quote", "\"1.0E-03\""),
      new TestCase("A number, large, exponential notation, French style, in double quote [1]", "\"1.0E+03\""),
      new TestCase("A number, large, exponential notation, French style, in double quote [2]", "\"1.0E3\""),
      new TestCase("An integer, in double quote", "\"5L\""),
      new TestCase("A negative number, in double quote", "\"-0.43151402098822\""),
      new TestCase("A negative number, small, exponential notation, US style, in double quote", "\"-1.0e-03\""),
      new TestCase("A negative number, large, exponential notation, US style, in double quote [1]", "\"-1.0e+03\""),
      new TestCase("A negative number, large, exponential notation, US style, in double quote [2]", "\"-1.0e3\""),
      new TestCase("A negative number, small, exponential notation, French style, in double quote", "\"-1.0E-03\""),
      new TestCase("A negative number, large, exponential notation, French style, in double quote [1]", "\"-1.0E+03\""),
      new TestCase("A negative number, large, exponential notation, French style, in double quote [2]", "\"-1.0E3\""),
      new TestCase("An embedded symbol", "A$B$C.D"),
      new TestCase("A formula", "A ~ B", "A~B"),
      new TestCase("A function invocation", "A()", "dsBase::A()"),
      new TestCase("An operator on symbols", "A + B", "base::'+'(A, B)"),
      new TestCase("Operator chaining", "A + B * C", "base::'+'(A, base::'*'(B, C))"),
      new TestCase("Operator on functions", "A() + B * C", "base::'+'(dsBase::A(), base::'*'(B, C))"),
      new TestCase("A formula with operators", "A ~ B + (C * D)^4 : E %in% F", "A~B+(C*D)^4:E%in%F"),
      new TestCase("Grouping", "(A + B) * (C - D)", "base::'*'(base::'+'(A, B), base::'-'(C, D))"),
      new TestCase("Function with a single parameter", "A(B)", "dsBase::A(B)"),
      new TestCase("Function with a function invocation as parameter", "A(B())", "dsBase::A(dsBase::B())"),
      new TestCase("Function with multiple parameters", "A(B, C)", "dsBase::A(B, C)"),
      new TestCase("Function with multiple kinds of parameters", "A(B, C(), D, E(F(G/H)), A + B * C())", "dsBase::A(B, dsBase::C(), D, dsBase::E(dsBase::F(base::'/'(G, H))), base::'+'(A, base::'*'(B, dsBase::C())))"),
      new TestCase("Function with formula as argument", "glm(A ~ B + C:D, poisson)", "dsBase::glm(A~B+C:D, poisson)"),
      new TestCase("Function with named argument", "A(arg=x,another=y)", "dsBase::A(arg = x, another = y)"),
      new TestCase("Function with string argument", "A('this')", "dsBase::A('this')"),
      new TestCase("Function with double quoted string argument", "A(\"this\")", "dsBase::A(\"this\")"),
      new TestCase("Function with numerical string argument", "A('123')", "dsBase::A('123')"),
      new TestCase("Function with alphanumerical string argument and some special characters", "A('this1_that.this-that1')", "dsBase::A('this1_that.this-that1')"),
      new TestCase("Function with a list of colon separated numerical string argument", "A('1.2:1.3:-8.5')", "dsBase::A('1.2:1.3:-8.5')"),
      new TestCase("Function with a list of comma separated numerical string argument", "A('1.2,1.3,-8.5')", "dsBase::A('1.2,1.3,-8.5')"),
      new TestCase("Function with a list of semi colon separated numerical string argument", "A('1.2;1.3;-8.5')", "dsBase::A('1.2;1.3;-8.5')"),
      new TestCase("Function with data frame column name string argument", "A('D$abc')", "dsBase::A('D$abc')")};

  @Test
  public void test_testCases() throws ParseException {
    for (TestCase testCase : tests) {
      doTest(testCase.getMsg(), testCase);
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
  public void test_openSquareBracketString() throws ParseException {
    doTest("Function with opening square bracket in string argument", "A('this[that')");
  }

  @Test(expected = TokenMgrError.class)
  public void test_closeSquareBracketString() throws ParseException {
    doTest("Function with closing square bracket in string argument", "A('this]that')");
  }

  @Test(expected = TokenMgrError.class)
  public void test_arraySquareBracketString() throws ParseException {
    doTest("A subset symbol", "A[2, 1]");
  }

  @Test(expected = TokenMgrError.class)
  public void test_arrayRangeSquareBracketString() throws ParseException {
    doTest("A subset symbol with range", "A[2, 1:2]");
  }

  @Test(expected = TokenMgrError.class)
  public void test_rowSubsetSquareBracketString() throws ParseException {
    doTest("An open row subset symbol", "A[,1]");
  }

  @Test(expected = TokenMgrError.class)
  public void test_openColumnSquareBracketString() throws ParseException {
    doTest("An open column subset symbol", "A[1,]");
  }

  @Test(expected = TokenMgrError.class)
  public void test_symbolRangeSquareBracketString() throws ParseException {
    doTest("A subset symbol with range", "A[,1:2]");
  }

  @Test(expected = TokenMgrError.class)
  public void test_functionAllSquareBracketString() throws ParseException {
    doTest("A subset symbol with function call", "D[,func(D[,1])]");
  }

  @Test(expected = TokenMgrError.class)
  public void test_emptySubsetSquareBracketString() throws ParseException {
    doTest("An empty subset symbol", "A[]");
  }

  @Test(expected = TokenMgrError.class)
  public void test_almostEmptySubsetSquareBracketString() throws ParseException {
    doTest("An almost empty subset symbol", "A[,]");
  }

  @Test(expected = TokenMgrError.class)
  public void test_subsetValueSquareBracketString() throws ParseException {
    doTest("A subset value symbol", "A[[1]]");
  }

  @Test(expected = TokenMgrError.class)
  public void test_funcSubsetParamSquareBracketString() throws ParseException {
    doTest("Function with a subset parameter", "A(B[,2])");
  }

  @Test(expected = TokenMgrError.class)
  public void test_funcSubsetAndSimplePAramSquareBracketString() throws ParseException {
    doTest("Function with a subset parameter and a simple parameter", "A(B[,2], C)");
  }

  @Test(expected = TokenMgrError.class)
  public void test_funcDataframeIndexSquareBracketString() throws ParseException {
    doTest("Function with data frame index string argument", "A('D[123]')");
  }

  @Test(expected = TokenMgrError.class)
  public void test_backslashInString() throws ParseException {
    doTest("Function with backslash in string argument", "A('this\\that')");
  }

  @Test(expected = TokenMgrError.class)
  public void test_operatorInString() throws ParseException {
    doTest("Function with operator in string argument", "A('this*that')");
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

  @Test
  public void test_script() throws ParseException {
    //doTestVisitor("A[,1]", "A[,1]");
  }

  private void doTest(String msg, String test) throws ParseException {
    doTest(msg, new TestCase(msg, test));
  }

  private void doTest(String msg, TestCase testCase) throws ParseException {
    System.out.println(msg + ": " + testCase.getTest());
    DataShieldGrammar g = new DataShieldGrammar(new StringReader(testCase.getTest()));
    SimpleNode expr = g.root();
    expr.dump("");
    doTestVisitor(testCase.getTest(), testCase.getExpected());
  }

  private void doTestVisitor(String test, String expected) throws ParseException {
    DSEnvironment environment = new DefaultDSEnvironment(null) {
      @Override
      public DSMethod getMethod(String name) {
        return new PackagedFunctionDSMethod(name, "dsBase::" + name);
      }
    };
    RScriptGenerator visitor = new RScriptGenerator(environment);
    DataShieldGrammar g = new DataShieldGrammar(new StringReader(test));
    String script = visitor.toScript(g.root());
    Assert.assertEquals(expected, script);
  }

  private static class TestCase {
    private final String msg;
    private final String test;
    private final String expected;

    private TestCase(String msg, String test) {
      this.msg = msg;
      this.test = test;
      this.expected = test;
    }

    private TestCase(String msg, String test, String expected) {
      this.msg = msg;
      this.test = test;
      this.expected = expected;
    }

    public String getMsg() {
      return msg;
    }

    public String getTest() {
      return test;
    }

    public String getExpected() {
      return expected;
    }
  }
}
