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

import org.junit.Assert;
import org.junit.Test;
import org.obiba.datashield.core.DSEnvironment;
import org.obiba.datashield.core.DSMethod;
import org.obiba.datashield.core.DSMethodType;
import org.obiba.datashield.core.impl.DefaultDSEnvironment;
import org.obiba.datashield.core.impl.PackagedFunctionDSMethod;
import org.obiba.datashield.r.expr.InvalidScriptException;

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
      new TestCase("Multiplication before addition", "A * B + C", "base::'+'(base::'*'(A, B), C)"),
      new TestCase("Division before subtraction", "A / B - C", "base::'-'(base::'/'(A, B), C)"),
      new TestCase("Matrix product before multiplication", "A * B %*% C", "base::'*'(A, base::'%*%'(B, C))"),
      new TestCase("Power before multiplication", "A * B ^ C", "base::'*'(A, base::'^'(B, C))"),
      new TestCase("Subtraction is left associative", "A - B + 1", "base::'+'(base::'-'(A, B), 1)"),
      new TestCase("Subtraction chain", "A - B - C", "base::'-'(base::'-'(A, B), C)"),
      new TestCase("Division is left associative", "A / B * C", "base::'*'(base::'/'(A, B), C)"),
      new TestCase("Power is right associative", "A ^ B ^ C", "base::'^'(A, base::'^'(B, C))"),
      new TestCase("Grouping overrides associativity", "A - (B + 1)", "base::'-'(A, base::'+'(B, 1))"),
      new TestCase("Operators in a function argument", "A(B - C + 1)", "dsBase::A(base::'+'(base::'-'(B, C), 1))"),
      new TestCase("Operators in a named argument", "A(x = B * C + 1)", "dsBase::A(x = base::'+'(base::'*'(B, C), 1))"),
      new TestCase("Subtraction without spaces", "A*B-C", "base::'-'(base::'*'(A, B), C)"),
      new TestCase("Subtraction of columns without spaces", "D$A-D$B*2", "base::'-'(D$A, base::'*'(D$B, 2))"),
      new TestCase("Subtraction of a group without spaces", "A-(B)", "base::'-'(A, B)"),
      new TestCase("Subtraction of a number without spaces", "1-1", "base::'-'(1, 1)"),
      new TestCase("Subtraction of a number with a space", "A -1", "base::'-'(A, 1)"),
      new TestCase("Unary minus on a symbol", "-A", "base::'-'(A)"),
      new TestCase("Unary minus on a group", "-(A + B)", "base::'-'(base::'+'(A, B))"),
      new TestCase("Unary minus on a function", "-A()", "base::'-'(dsBase::A())"),
      new TestCase("Unary minus binds tighter than multiplication", "-A * B", "base::'*'(base::'-'(A), B)"),
      new TestCase("Unary minus after a binary operator", "A * -B", "base::'*'(A, base::'-'(B))"),
      new TestCase("Power binds tighter than unary minus", "-A ^ 2", "base::'-'(base::'^'(A, 2))"),
      new TestCase("Power binds tighter than the sign of a number", "-2 ^ 2", "base::'-'(base::'^'(2, 2))"),
      new TestCase("Negative exponent", "2 ^ -1", "base::'^'(2, -1)"),
      new TestCase("Negative number as an argument", "A(-1, x = -2.5)", "dsBase::A(-1, x = -2.5)"),
      new TestCase("A range", "1:3"),
      new TestCase("A range as an argument", "A(1:3)", "dsBase::A(1:3)"),
      new TestCase("A range as an operand", "2 * 1:3", "base::'*'(2, 1:3)"),
      new TestCase("A grouped range as an operand of power", "(1:3) ^ 2", "base::'^'(1:3, 2)"),
      new TestCase("A formula with a negative intercept", "A ~ -1 + B", "A~-1+B"),
      new TestCase("A formula removing the intercept", "A ~ B - 1", "A~B-1"),
      new TestCase("A formula removing the intercept, without spaces", "A~B-1", "A~B-1"),
      new TestCase("A formula with a negative term", "A ~ B -1", "A~B-1"),
      new TestCase("A formula with an expression as response", "A-1~B", "A-1~B"),
      new TestCase("A formula with spaces in the response", "A - 1 ~ B + C", "A-1~B+C"),
      new TestCase("A formula with a grouped response", "(A + B) * 2 ~ C", "(A+B)*2~C"),
      new TestCase("A formula with a sum as response is not an addition", "A + B ~ C", "A+B~C"),
      new TestCase("A formula with a numeric range", "A ~ 2:3", "A~2:3"),
      new TestCase("A formula with a numeric range as response", "1:3^2 ~ A", "1:3^2~A"),
      new TestCase("A formula with a division", "A ~ B / C", "A~B/C"),
      new TestCase("A formula with a division as response", "A / 2 ~ B", "A/2~B"),
      new TestCase("A formula with a matrix product", "A ~ B %*% C", "A~B%*%C"),
      new TestCase("A formula with a matrix product as response", "A %*% B ~ C", "A%*%B~C"),
      new TestCase("A formula with a negative response", "-A ~ B", "-A~B"),
      new TestCase("A formula with a negative term", "A ~ -B", "A~-B"),
      new TestCase("A formula with a negative group", "A ~ -(B + C)", "A~-(B+C)"),
      new TestCase("A formula with a term subtracting a negative", "A ~ B - -C", "A~B--C"),
      new TestCase("A formula with an expression as response in a function", "glm(A-1 ~ B, poisson)", "dsBase::glm(A-1~B, poisson)"),
      new TestCase("A formula with an expression as response as named argument", "A(f = B-1~C)", "dsBase::A(f = B-1~C)"),
      new TestCase("Function with a single parameter", "A(B)", "dsBase::A(B)"),
      new TestCase("Function with a function invocation as parameter", "A(B())", "dsBase::A(dsBase::B())"),
      new TestCase("Function with multiple parameters", "A(B, C)", "dsBase::A(B, C)"),
      new TestCase("Function with multiple kinds of parameters", "A(B, C(), D, E(F(G/H)), A + B * C())", "dsBase::A(B, dsBase::C(), D, dsBase::E(dsBase::F(base::'/'(G, H))), base::'+'(A, base::'*'(B, dsBase::C())))"),
      new TestCase("Function with formula as argument", "glm(A ~ B + C:D, poisson)", "dsBase::glm(A~B+C:D, poisson)"),
      new TestCase("Function with named argument", "A(arg=x,another=y)", "dsBase::A(arg = x, another = y)"),
      new TestCase("Function with a string as argument name", "A('arg'=x)", "dsBase::A('arg' = x)"),
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

  @Test(expected = ParseException.class)
  public void test_rangeAsPowerBase() throws ParseException {
    doTest("R reads it as 1:(3^2)", "1:3^2");
  }

  @Test(expected = ParseException.class)
  public void test_rangeAsPowerExponent() throws ParseException {
    doTest("R reads it as (2^1):3", "2^1:3");
  }

  @Test(expected = ParseException.class)
  public void test_negativeRange() throws ParseException {
    doTest("R reads it as (-1):3", "-1:3");
  }

  @Test(expected = ParseException.class)
  public void test_formulaWithMinusBeforeTilde() throws ParseException {
    doTest("R reads it as A minus a one-sided formula", "A-~B");
  }

  @Test
  public void test_invalidArgumentNames() throws ParseException {
    for (String script : new String[]{"A(a+b=c)", "A(a * b = c)", "A(x-y=1)", "A(-x = 1)", "A(1 = 2)", "A(D$x = 1)", "A(B() = 1)"}) {
      try {
        doTestVisitor(script, null);
        Assert.fail("Expected an invalid script: " + script);
      } catch (InvalidScriptException e) {
        Assert.assertEquals("Invalid argument name.", e.getMessage());
      }
    }
  }

  @Test
  public void test_parseErrorListsExpectedTokens() {
    for (String script : new String[]{"A(x=)", "A(1,)", "()", "(", "~A", "*A", "1+)", "A + * B", "A(-)"}) {
      try {
        new DataShieldGrammar(new StringReader(script)).root();
        Assert.fail("Expected a parse error: " + script);
      } catch (ParseException e) {
        Assert.assertTrue(script + ": " + e.getMessage(), e.getMessage().contains("<NUMBER>") && e.getMessage().contains("<R_IDENTIFIER>"));
      }
    }
  }

  @Test
  public void test_aggregateAcceptedScripts() throws ParseException {
    String[][] scripts = {
        {"A(B, 1, 'c', -1)", "dsBase::A(B, 1, 'c', -1)"},
        {"A(x = -1, y = B(z = 1:3))", "dsBase::A(x = -1, y = dsBase::B(z = 1:3))"},
        {"A(B ~ C + D, poisson)", "dsBase::A(B~C+D, poisson)"},
        {"A(B + C)", "dsBase::A(base::'+'(B, C))"},
        {"A(-B)", "dsBase::A(base::'-'(B))"},
        {"A(1:3-1)", "dsBase::A(base::'-'(1:3, 1))"},
        {"A(x = B * 2)", "dsBase::A(x = base::'*'(B, 2))"},
        {"A(B(C / 2))", "dsBase::A(dsBase::B(base::'/'(C, 2)))"}};
    for (String[] script : scripts) {
      Assert.assertEquals(script[1], aggregateScript(script[0]));
    }
  }

  @Test
  public void test_script() throws ParseException {
    //doTestVisitor("A[,1]", "A[,1]");
  }

  private String aggregateScript(String script) throws ParseException {
    DSEnvironment environment = new DefaultDSEnvironment(DSMethodType.AGGREGATE, null) {
      @Override
      public DSMethod getMethod(String name) {
        return new PackagedFunctionDSMethod(name, "dsBase::" + name);
      }
    };
    return new RScriptGeneratorV2(environment, script).toScript();
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
    RScriptGeneratorV2 visitor = new RScriptGeneratorV2(environment, test);
    String script = visitor.toScript();
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
