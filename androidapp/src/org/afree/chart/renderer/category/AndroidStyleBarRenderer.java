/* ===========================================================
 * AFreeChart : a free chart library for Android(tm) platform.
 *              (based on JFreeChart and JCommon)
 * ===========================================================
 *
 * (C) Copyright 2010, by Icom Systech Co., Ltd.
 * (C) Copyright 2000-2008, by Object Refinery Limited and Contributors.
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
 * -----------------
 * AndroidStyleBarRenderer.java
 * -----------------
 * (C) Copyright 2010, by Icom Systech Co., Ltd.
 *
 * Original Author:  Niwano Masayoshi (for Icom Systech Co., Ltd);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 19-Nov-2010 : Version 0.0.1 (NM);
 */

package org.afree.chart.renderer.category;

import org.afree.ui.GradientShaderFactory;
import org.afree.ui.RectangleEdge;
import org.afree.graphics.geom.RectShape;
import org.afree.graphics.GradientColor;
import org.afree.graphics.PaintType;
import org.afree.graphics.PaintUtility;
import org.afree.graphics.SolidColor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

public class AndroidStyleBarRenderer extends GradientBarPainter {

    /**
     * 
     */
    private static final long serialVersionUID = 8340446511164134612L;

    /**
     * Paints a single bar instance.
     * 
     * @param canvas
     *            the graphics target.
     * @param renderer
     *            the renderer.
     * @param row
     *            the row index.
     * @param column
     *            the column index.
     * @param bar
     *            the bar
     * @param base
     *            indicates which side of the RectShape is the base of the bar.
     */
    public void paintBar(Canvas canvas, BarRenderer renderer, int row, int column,
            RectShape bar, RectangleEdge base) {
        PaintType itemPaintType = renderer.getItemPaintType(row, column);
        Paint itemPaint = PaintUtility.createPaint(Paint.ANTI_ALIAS_FLAG, itemPaintType);

        if (itemPaintType instanceof SolidColor) {
            SolidColor col = (SolidColor)itemPaintType;
            itemPaint.setColor(col.getColor());
        } else if (itemPaintType instanceof GradientColor) {
            GradientShaderFactory t = renderer.getGradientPaintTransformer();
            Shader shader = t.create((GradientColor)itemPaintType, bar);
            itemPaint.setShader(shader);
        }
        
        if (base == RectangleEdge.TOP || base == RectangleEdge.BOTTOM) {
            
            bar.fill(canvas, itemPaint);

        } else if (base == RectangleEdge.LEFT || base == RectangleEdge.RIGHT) {

            bar.fill(canvas, itemPaint);
        }

        // draw the outline...
        if (renderer.isDrawBarOutline()
        /* && state.getBarWidth() > renderer.BAR_OUTLINE_WIDTH_THRESHOLD */) {
            PaintType paintType = renderer.getItemOutlinePaintType(row, column);
            float stroke = renderer.getItemOutlineStroke(row, column);
            if (stroke != 0.0f && paintType != null) {
                Paint paint = PaintUtility.createPaint(paintType, stroke, 
                        renderer.getItemOutlineEffect(row, column));
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(stroke);
                bar.draw(canvas, paint);
            }
        }

    }

    /**
     * Paints a single bar instance.
     * 
     * @param canvas
     *            the graphics target.
     * @param renderer
     *            the renderer.
     * @param row
     *            the row index.
     * @param column
     *            the column index.
     * @param bar
     *            the bar
     * @param base
     *            indicates which side of the RectShape is the base of the bar.
     * @param pegShadow
     *            peg the shadow to the base of the bar?
     */
    public void paintBarShadow(Canvas canvas, BarRenderer renderer, int row,
            int column, RectShape bar, RectangleEdge base,
            boolean pegShadow) {

    }
}
