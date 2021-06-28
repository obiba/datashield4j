package org.obiba.datashield.core.impl;

import org.obiba.datashield.core.DSMethod;
import org.obiba.datashield.core.DSMethodType;

import java.util.Objects;

public class DefaultDSMethod implements DSMethod {

  private String name;

  private String function;

  /**
   * Package name that defined this method.
   */
  private String pack;

  /**
   * Version of the package.
   */
  private String version;

  public DefaultDSMethod() {
  }

  public DefaultDSMethod(String name, String function) {
    this.name = name;
    this.function = function;
  }

  public DefaultDSMethod(String name, String function, String pack, String version) {
    this.name = name;
    this.function = function;
    this.pack = pack;
    this.version = version;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFunction() {
    return function;
  }

  public void setFunction(String function) {
    this.function = function;
  }

  public boolean hasPackage() {
    return pack != null;
  }

  public void setPackage(String pack) {
    this.pack = pack;
  }

  public String getPackage() {
    return pack;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }

  @Override
  public String invoke(DSMethodType env) {
    if (hasPackage())
      return getFunction();
    return env.symbol() + "$" + getName();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DefaultDSMethod that = (DefaultDSMethod) o;
    return Objects.equals(name, that.name) &&
        Objects.equals(function, that.function) &&
        Objects.equals(pack, that.pack) &&
        Objects.equals(version, that.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, function, pack, version);
  }

}
