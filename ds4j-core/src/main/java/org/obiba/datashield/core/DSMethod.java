/*
 * Copyright (c) 2019 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.datashield.core;

/**
 * Interface to be implemented by methods that can be executed through datashield
 */
public interface DSMethod {

  /**
   * Get the name of the allowed method, to be called by the client.
   *
   * @return
   */
  String getName();

  /**
   * Whether the method is defined in a package.
   *
   * @return
   */
  boolean hasPackage();

  /**
   * Translation of the name (invoked by the client) into a server expression call.
   *
   * @param env
   * @return
   */
  String invoke(DSMethodType env);

}
