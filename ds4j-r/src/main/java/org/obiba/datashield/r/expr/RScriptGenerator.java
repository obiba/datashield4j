package org.obiba.datashield.r.expr;

import java.util.Map;

/**
 * Validate syntax and grammar of the submitted script and rewrite script using the DataSHIELD configuration,
 * before execution in the R server.
 */
public interface RScriptGenerator {

  /**
   * Rewrite the submitted script into its server side equivalent: function calls are mapped to internal functions.
   *
   * @return
   */
  String toScript();

  Map<String, String> getMappedFunctions();

}
