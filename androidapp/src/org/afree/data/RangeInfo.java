/* ===========================================================
 * AFreeChart : a free chart library for Android(tm) platform.
 *              (based on JFreeChart and JCommon)
 * ===========================================================
 *
 * (C) Copyright 2010, by Icom Systech Co., Ltd.
 * (C) Copyright 2000-2009, by Object Refinery Limited and Contributors.
 *
 * Project Info:
 *    AFreeChart: http://code.google.com/p/afreechart/
 *    JFreeChart: http://www.jfree.org/jfreechart/index.html
 *    JCommon   : http://www.jfree.org/jcommon/index.html
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * [Android is a trademark of Google Inc.]
 *
 * --------------
 * RangeInfo.java
 * --------------
 * 
 * (C) Copyright 2010, by Icom Systech Co., Ltd.
 *
 * Original Author:  shiraki  (for Icom Systech Co., Ltd);
 * Contributor(s):   Sato Yoshiaki ;
 *                   Niwano Masayoshi;
 *
 * Changes (from 19-Nov-2010)
 * --------------------------
 * 19-Nov-2010 : port JFreeChart 1.0.13 to Android as "AFreeChart"
 * 
 * ------------- JFreeChart ---------------------------------------------
 * (C) Copyright 2000-2009, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes (from 18-Sep-2001)
 * --------------------------
 * 18-Sep-2001 : Added standard header and fixed DOS encoding problem (DG);
 * 15-Nov-2001 : Moved to package com.jrefinery.data.* (DG);
 *               Updated Javadoc comments (DG);
 * 22-Apr-2002 : Added getValueRange() method (DG);
 * 17-Nov-2004 : Replaced getMinimumRangeValue() --> getRangeLowerBound(),
 *               getMaximumRangeValue() --> getRangeUpperBound(),
 *               getValueRange() --> getRangeBounds().
 * 11-Jan-2005 : Removed deprecated code in preparation for the 1.0.0
 *               release (DG);
 *
 */

package org.afree.data;

/**
 * An interface (optional) that can be implemented by a dataset to assist in
 * determining the minimum and maximum values. See also {@link DomainInfo}.
 */
public interface RangeInfo {

    /**
     * Returns the minimum y-value in the dataset.
     * 
     * @param includeInterval
     *            a flag that determines whether or not the y-interval is taken
     *            into account.
     * 
     * @return The minimum value.
     */
    public double getRangeLowerBound(boolean includeInterval);

    /**
     * Returns the maximum y-value in the dataset.
     * 
     * @param includeInterval
     *            a flag that determines whether or not the y-interval is taken
     *            into account.
     * 
     * @return The maximum value.
     */
    public double getRangeUpperBound(boolean includeInterval);

    /**
     * Returns the range of the values in this dataset's range.
     * 
     * @param includeInterval
     *            a flag that determines whether or not the y-interval is taken
     *            into account.
     * 
     * @return The range (or <code>null</code> if the dataset contains no
     *         values).
     */
    public Range getRangeBounds(boolean includeInterval);

}
