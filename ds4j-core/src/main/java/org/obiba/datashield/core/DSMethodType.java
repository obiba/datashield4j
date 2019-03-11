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
 * There are two kind of operations on the R server side: aggregation (extraction of summary data) and assignment (assign
 * and expression or a dataset to a R symbol).
 */
public enum DSMethodType {

  AGGREGATE, ASSIGN;

  public String symbol() {
    return "." + toString();
  }
}
