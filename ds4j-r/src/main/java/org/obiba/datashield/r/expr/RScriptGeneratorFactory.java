package org.obiba.datashield.r.expr;

import org.obiba.datashield.core.DSEnvironment;
import org.obiba.datashield.r.expr.v1.RScriptGeneratorV1;
import org.obiba.datashield.r.expr.v2.RScriptGeneratorV2;

public class RScriptGeneratorFactory {

  private RScriptGeneratorFactory() {
  }

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
      throw new ParseException("Unkown R parser version: " + rParserVersion);
    }
  }
}
