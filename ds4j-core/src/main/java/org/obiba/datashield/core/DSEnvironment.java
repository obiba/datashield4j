package org.obiba.datashield.core;

import java.util.List;

public interface DSEnvironment {

  /**
   * The type of operations that are performed by these methods.
   *
   * @return
   */
  DSMethodType getMethodType();

  /**
   * Get the list of available methods.
   *
   * @return
   */
  List<DSMethod> getMethods();

  /**
   * Get a specific method by its name.
   *
   * @param name
   * @return
   * @throws NoSuchDSMethodException
   */
  DSMethod getMethod(String name) throws NoSuchDSMethodException;

  /**
   * Add or update a method.
   *
   * @param method
   */
  void addOrUpdate(DSMethod method);

  /**
   * Remove the method with the given name. Silently ignored when no such method exists.
   *
   * @param name
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
