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
 * A profile is meant to restrict the access of a user or a group of users to a set of methods and R servers.
 */
public interface DSProfile {

  /**
   * Get unique profile name.
   *
   * @return
   */
  String getName();

  /**
   * Get the DataSHIELD servers associated to the profile.
   *
   * @return
   */
  DSServerRegistry getServerRegistry();

  /**
   * Get the configuration (options and methods) associated to the profile.
   *
   * @return
   */
  DSConfiguration getConfiguration();
}
