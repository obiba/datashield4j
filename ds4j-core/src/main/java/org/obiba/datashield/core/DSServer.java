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
 * Represents a DataSHIELD server, where computation happens.
 */
public interface DSServer {

  /**
   * Start the remote server (if such feature is supported).
   */
  void start();

  /**
   * Stop the remote server (if such feature is supported).
   */
  void stop();

  /**
   * Sanity check.
   *
   * @return true if the server is ready.
   */
  boolean isRunning();

  /**
   * Make a new session for the given user name.
   *
   * @param user
   * @return
   */
  DSSession newSession(String user);

  /**
   * Silently close the session and destroy any associated resources.
   *
   * @param session
   */
  void closeSession(DSSession session);

}
