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
 * --------------------
 * XYErrorRenderer.java
 * --------------------
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
 * 14-Jan-2011 : Updated API docs
 * 
 * ------------- JFreeChart ---------------------------------------------
 * (C) Copyright 2006-2009, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 25-Oct-2006 : Version 1 (DG);
 * 23-Mar-2007 : Check item visibility before drawing error bars - see bug
 *               1686178 (DG);
 * 28-Jan-2009 : Added stroke options for error indicators (DG);
 *
 */

package org.afree.chart.renderer.xy;

import org.afree.util.ObjectUtilities;
import org.afree.util.PaintTypeUtilities;
import org.afree.ui.RectangleEdge;
import org.afree.chart.axis.ValueAxis;
import org.afree.data.xy.IntervalXYDataset;
import org.afree.data.Range;
import org.afree.data.xy.XYDataset;
import org.afree.data.general.DatasetUtilities;
import org.afree.chart.plot.CrosshairState;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.plot.PlotRenderingInfo;
import org.afree.chart.plot.XYPlot;
import org.afree.graphics.geom.LineShape;
import org.afree.graphics.geom.RectShape;
import org.afree.graphics.PaintType;
import org.afree.graphics.PaintUtility;
import android.graphics.Canvas;
import android.graphics.Paint;


/**
 * A line and shape renderer that can also display x and/or y-error values.
 * This renderer expects an {@link IntervalXYDataset}, otherwise it reverts
 * to the behaviour of the super class.  The example shown here is generated by
 * the <code>XYErrorRendererDemo1.java</code> program included in the
 * AFreeChart demo collection:
 * <br><br>
 * <img src="../../../../../images/XYErrorRendererSample.png"
 * alt="XYErrorRendererSample.png" />
 *
 * @since JFreeChart 1.0.3
 */
public class XYErrorRenderer extends XYLineAndShapeRenderer {

    /** For serialization. */
    static final long serialVersionUID = 5162283570955172424L;

    /** A flag that controls whether or not the x-error bars are drawn. */
    private boolean drawXError;

    /** A flag that controls whether or not the y-error bars are drawn. */
    private boolean drawYError;

    /** The length of the cap at the end of the error bars. */
    private double capLength;

    /**
     * The paint used to draw the error bars (if <code>null</code> we use the
     * series paint).
     */
    private transient PaintType errorPaintType;

    /**
     * The stroke used to draw the error bars (if <code>null</code> we use the
     * series outline stroke).
     *
     * @since JFreeChart 1.0.13
     */
    private transient float errorStroke;

    /**
     * Creates a new <code>XYErrorRenderer</code> instance.
     */
    public XYErrorRenderer() {
        super(false, true);
        this.drawXError = true;
        this.drawYError = true;
        this.errorPaintType = null;
        this.errorStroke = 0.0f;
        this.capLength = 4.0;
    }

    /**
     * Returns the flag that controls whether or not the renderer draws error
     * bars for the x-values.
     *
     * @return A boolean.
     *
     * @see #setDrawXError(boolean)
     */
    public boolean getDrawXError() {
        return this.drawXError;
    }

    /**
     * Sets the flag that controls whether or not the renderer draws error
     * bars for the x-values and, if the flag changes, sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param draw  the flag value.
     *
     * @see #getDrawXError()
     */
    public void setDrawXError(boolean draw) {
        if (this.drawXError != draw) {
            this.drawXError = draw;
//            fireChangeEvent();
        }
    }

    /**
     * Returns the flag that controls whether or not the renderer draws error
     * bars for the y-values.
     *
     * @return A boolean.
     *
     * @see #setDrawYError(boolean)
     */
    public boolean getDrawYError() {
        return this.drawYError;
    }

    /**
     * Sets the flag that controls whether or not the renderer draws error
     * bars for the y-values and, if the flag changes, sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param draw  the flag value.
     *
     * @see #getDrawYError()
     */
    public void setDrawYError(boolean draw) {
        if (this.drawYError != draw) {
            this.drawYError = draw;
//            fireChangeEvent();
        }
    }

    /**
     * Returns the length (in Canvas units) of the cap at the end of the error
     * bars.
     *
     * @return The cap length.
     *
     * @see #setCapLength(double)
     */
    public double getCapLength() {
        return this.capLength;
    }

    /**
     * Sets the length of the cap at the end of the error bars, and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param length  the length (in Canvas units).
     *
     * @see #getCapLength()
     */
    public void setCapLength(double length) {
        this.capLength = length;
//        fireChangeEvent();
    }

    /**
     * Returns the paint used to draw the error bars.  If this is
     * <code>null</code> (the default), the item paint is used instead.
     *
     * @return The paint type (possibly <code>null</code>).
     *
     * @see #setErrorPaintType(PaintType)
     */
    public PaintType getErrorPaintType() {
        return this.errorPaintType;
    }

    /**
     * Sets the paint used to draw the error bars and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param paintType  the paint (<code>null</code> permitted).
     *
     * @see #getErrorPaintType()
     */
    public void setErrorPaintType(PaintType paintType) {
        this.errorPaintType = paintType;
//        fireChangeEvent();
    }

    /**
     * Returns the stroke used to draw the error bars.  If this is 
     * <code>null</code> (the default), the item outline stroke is used 
     * instead.
     * 
     * @return The stroke (possibly <code>null</code>).
     *
     * @see #setErrorStroke(float stroke)
     * 
     * @since JFreeChart 1.0.13
     */
    public float getErrorStroke() {
        return this.errorStroke;
    }

    /**
     * Sets the stroke used to draw the error bars and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param stroke   the stroke (<code>null</code> permitted).
     *
     * @see #getErrorStroke()
     *
     * @since JFreeChart 1.0.13
     */
    public void setErrorStroke(float stroke) {
        this.errorStroke = stroke;
//        fireChangeEvent();
    }

    /**
     * Returns the range required by this renderer to display all the domain
     * values in the specified dataset.
     *
     * @param dataset  the dataset (<code>null</code> permitted).
     *
     * @return The range, or <code>null</code> if the dataset is
     *     <code>null</code>.
     */
    public Range findDomainBounds(XYDataset dataset) {
        if (dataset != null) {
            return DatasetUtilities.findDomainBounds(dataset, true);
        }
        else {
            return null;
        }
    }

    /**
     * Returns the range required by this renderer to display all the range
     * values in the specified dataset.
     *
     * @param dataset  the dataset (<code>null</code> permitted).
     *
     * @return The range, or <code>null</code> if the dataset is
     *     <code>null</code>.
     */
    public Range findRangeBounds(XYDataset dataset) {
        if (dataset != null) {
            return DatasetUtilities.findRangeBounds(dataset, true);
        }
        else {
            return null;
        }
    }

    /**
     * Draws the visual representation for one data item.
     *
     * @param canvas  the graphics output target.
     * @param state  the renderer state.
     * @param dataArea  the data area.
     * @param info  the plot rendering info.
     * @param plot  the plot.
     * @param domainAxis  the domain axis.
     * @param rangeAxis  the range axis.
     * @param dataset  the dataset.
     * @param series  the series index.
     * @param item  the item index.
     * @param crosshairState  the crosshair state.
     * @param pass  the pass index.
     */
    public void drawItem(Canvas canvas, XYItemRendererState state,
            RectShape dataArea, PlotRenderingInfo info, XYPlot plot,
            ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset,
            int series, int item, CrosshairState crosshairState, int pass) {

        if (pass == 0 && dataset instanceof IntervalXYDataset
                && getItemVisible(series, item)) {
            IntervalXYDataset ixyd = (IntervalXYDataset) dataset;
            PlotOrientation orientation = plot.getOrientation();
            if (this.drawXError) {
                // draw the error bar for the x-interval
                double x0 = ixyd.getStartXValue(series, item);
                double x1 = ixyd.getEndXValue(series, item);
                double y = ixyd.getYValue(series, item);
                RectangleEdge edge = plot.getDomainAxisEdge();
                double xx0 = domainAxis.valueToJava2D(x0, dataArea, edge);
                double xx1 = domainAxis.valueToJava2D(x1, dataArea, edge);
                double yy = rangeAxis.valueToJava2D(y, dataArea,
                        plot.getRangeAxisEdge());
                LineShape line;
                LineShape cap1 = null;
                LineShape cap2 = null;
                double adj = this.capLength / 2.0;
                if (orientation == PlotOrientation.VERTICAL) {
                    line = new LineShape(xx0, yy, xx1, yy);
                    cap1 = new LineShape(xx0, yy - adj, xx0, yy + adj);
                    cap2 = new LineShape(xx1, yy - adj, xx1, yy + adj);
                }
                else {  // PlotOrientation.HORIZONTAL
                    line = new LineShape(yy, xx0, yy, xx1);
                    cap1 = new LineShape(yy - adj, xx0, yy + adj, xx0);
                    cap2 = new LineShape(yy - adj, xx1, yy + adj, xx1);
                }
                
                PaintType paintType;
                float stroke;
                
                if (this.errorPaintType != null) {
//                    canvas.setPaint(this.errorPaint);
                    paintType = this.errorPaintType;
                }
                else {
//                    canvas.setPaint(getItemPaint(series, item));
                    paintType = getItemPaintType(series, item);
                }
                
                if (this.errorStroke != 0.0f) {
//                    canvas.setStroke(this.errorStroke);
                    stroke = this.errorStroke;
                }
                else {
//                    canvas.setStroke(getItemStroke(series, item));
                    stroke = getItemStroke(series, item);
                }
                
                Paint paint = PaintUtility.createPaint(
                        Paint.ANTI_ALIAS_FLAG,
                        paintType,
                        stroke,
                        getItemEffect(series, item));
                line.draw(canvas, paint);
                cap1.draw(canvas, paint);
                cap2.draw(canvas, paint);
//                canvas.draw(line);
//                canvas.draw(cap1);
//                canvas.draw(cap2);
            }
            if (this.drawYError) {
                // draw the error bar for the y-interval
                double y0 = ixyd.getStartYValue(series, item);
                double y1 = ixyd.getEndYValue(series, item);
                double x = ixyd.getXValue(series, item);
                RectangleEdge edge = plot.getRangeAxisEdge();
                double yy0 = rangeAxis.valueToJava2D(y0, dataArea, edge);
                double yy1 = rangeAxis.valueToJava2D(y1, dataArea, edge);
                double xx = domainAxis.valueToJava2D(x, dataArea,
                        plot.getDomainAxisEdge());
                LineShape line;
                LineShape cap1 = null;
                LineShape cap2 = null;
                double adj = this.capLength / 2.0;
                if (orientation == PlotOrientation.VERTICAL) {
                    line = new LineShape(xx, yy0, xx, yy1);
                    cap1 = new LineShape(xx - adj, yy0, xx + adj, yy0);
                    cap2 = new LineShape(xx - adj, yy1, xx + adj, yy1);
                }
                else {  // PlotOrientation.HORIZONTAL
                    line = new LineShape(yy0, xx, yy1, xx);
                    cap1 = new LineShape(yy0, xx - adj, yy0, xx + adj);
                    cap2 = new LineShape(yy1, xx - adj, yy1, xx + adj);
                }
                
                PaintType paintType;
                float stroke;
                
                if (this.errorPaintType != null) {
//                  canvas.setPaint(this.errorPaint);
                  paintType = this.errorPaintType;
                }
                else {
//                  canvas.setPaint(getItemPaint(series, item));
                  paintType = getItemPaintType(series, item);
                }
                if (this.errorStroke != 0.0f) {
//                  canvas.setStroke(this.errorStroke);
                    stroke = this.errorStroke;
                }
                else {
//                  canvas.setStroke(getItemStroke(series, item));
                    stroke = getItemStroke(series, item);
                }
                
                Paint paint = PaintUtility.createPaint(
                        Paint.ANTI_ALIAS_FLAG,
                        paintType,
                        stroke,
                        getItemEffect(series, item));
                
                line.draw(canvas, paint);
                cap1.draw(canvas, paint);
                cap2.draw(canvas, paint);
//                canvas.draw(line);
//                canvas.draw(cap1);
//                canvas.draw(cap2);
            }
        }
        super.drawItem(canvas, state, dataArea, info, plot, domainAxis, rangeAxis,
                dataset, series, item, crosshairState, pass);
    }

    /**
     * Tests this instance for equality with an arbitrary object.
     *
     * @param obj  the object (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof XYErrorRenderer)) {
            return false;
        }
        XYErrorRenderer that = (XYErrorRenderer) obj;
        if (this.drawXError != that.drawXError) {
            return false;
        }
        if (this.drawYError != that.drawYError) {
            return false;
        }
        if (this.capLength != that.capLength) {
            return false;
        }
        if (!PaintTypeUtilities.equal(this.errorPaintType, that.errorPaintType)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.errorStroke, that.errorStroke)) {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the input stream.
     *
     * @throws IOException  if there is an I/O error.
     * @throws ClassNotFoundException  if there is a classpath problem.
     */
    // TODO: implement readObject
//    private void readObject(ObjectInputStream stream)
//            throws IOException, ClassNotFoundException {
//        stream.defaultReadObject();
//        this.errorPaint = SerialUtilities.readPaint(stream);
//        this.errorStroke = SerialUtilities.readStroke(stream);
//    }

    /**
     * Provides serialization support.
     *
     * @param stream  the output stream.
     *
     * @throws IOException  if there is an I/O error.
     */
    // TODO: implement writeObject
//    private void writeObject(ObjectOutputStream stream) throws IOException {
//        stream.defaultWriteObject();
//        SerialUtilities.writePaint(this.errorPaint, stream);
//        SerialUtilities.writeStroke(this.errorStroke, stream);
//    }

}
