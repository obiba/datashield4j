package org.obiba.datashield.r.expr;

import org.obiba.datashield.core.DSEnvironment;
import org.obiba.datashield.r.expr.v1.RScriptGeneratorV1;
import org.obiba.datashield.r.expr.v2.RScriptGeneratorV2;

/**
 * Factory of @link{RScriptGenerator} objects.
 */
public class RScriptGeneratorFactory {

  private RScriptGeneratorFactory() {
  }

  /**
   * Create a new @link{RScriptGenerator} object for the provided R parser version. The script syntax and grammar
   * will be validate. The environment will drive the function calls rewrite.
   *
   * @param rParserVersion
   * @param environment
   * @param script
   * @return
   * @throws ParseException
   */
  public static RScriptGenerator make(String rParserVersion, DSEnvironment environment, String script) throws ParseException {
    if ("v1".equals(rParserVersion)) {
      try {
        return new RScriptGeneratorV1(environment, script);
      } catch (Exception e) {
        throw new ParseException(e.getMessage(), e);
      }
    } else if ("v2".equals(rParserVersion)) {
      try {
        return new RScriptGeneratorV2(environment, script);
      } catch (Exception e) {
        throw new ParseException(e.getMessage(), e);
      }
    } else {
      throw new ParseException("Unknown R parser version: " + rParserVersion);
    }
  }
}
