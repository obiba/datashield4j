package org.obiba.datashield.core;

import java.util.List;

public interface DSEnvironment {

  DSMethodType getMethodType();

  List<DSMethod> getMethods();

  DSMethod getMethod(String name);

  void addMethod(DSMethod method);

  /**
   * Remove the method with the given name.
   *
   * @param name
   * @throws NoSuchDSMethodException
   */
  void removeMethod(String name);

  /**
   * Check if there is a method with the given name.
   *
   * @param name
   * @return
   */

  boolean hasMethod(String name);


}
