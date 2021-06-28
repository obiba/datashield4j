package org.obiba.datashield.core.impl;

import org.obiba.datashield.core.DSEnvironment;
import org.obiba.datashield.core.DSMethod;
import org.obiba.datashield.core.DSMethodType;
import org.obiba.datashield.core.NoSuchDSMethodException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultDSEnvironment implements DSEnvironment {

  private DSMethodType type;

  private List<DefaultDSMethod> methods;

  public DefaultDSEnvironment() {
    this(DSMethodType.AGGREGATE, null);
  }

  public DefaultDSEnvironment(DSMethodType type) {
    this(type, null);
  }

  public DefaultDSEnvironment(DSMethodType type, List<DefaultDSMethod> methods) {
    this.type = type;
    this.methods = methods == null ? new ArrayList<>() : methods;
  }

  public void setMethodType(String typeName) {
    this.type = DSMethodType.valueOf(typeName);
  }

  @Override
  public DSMethodType getMethodType() {
    return type;
  }

  @Override
  public List<DSMethod> getMethods() {
    return Collections.unmodifiableList(methods);
  }

  @Override
  public void addOrUpdate(DSMethod method) {
    for (DSMethod m : getMethods()) {
      if (m.getName().equals(method.getName())) {
        methods.remove(m);
        break;
      }
    }
    methods.add((DefaultDSMethod) method);
  }

  @Override
  public DSMethod getMethod(String name) {
    return methods.stream().filter(ds -> ds.getName().equals(name))
        .findFirst().orElseThrow(() -> new NoSuchDSMethodException(type, name));
  }

  @Override
  public void removeMethod(String name) {
    for (DSMethod m : getMethods()) {
      if (m.getName().equals(name)) {
        methods.remove(m);
        break;
      }
    }
  }

  @Override
  public boolean hasMethod(String name) {
    return methods.stream().anyMatch(ds -> ds.getName().equals(name));
  }
}
