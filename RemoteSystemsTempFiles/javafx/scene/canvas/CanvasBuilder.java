/* 
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package javafx.scene.canvas;

/**
Builder class for javafx.scene.canvas.Canvas
@see javafx.scene.canvas.Canvas
@deprecated This class is deprecated and will be removed in the next version
* @since JavaFX 2.2
*/
@javax.annotation.Generated("Generated by javafx.builder.processor.BuilderProcessor")
@Deprecated
public class CanvasBuilder<B extends javafx.scene.canvas.CanvasBuilder<B>> extends javafx.scene.NodeBuilder<B> implements javafx.util.Builder<javafx.scene.canvas.Canvas> {
    protected CanvasBuilder() {
    }
    
    /** Creates a new instance of CanvasBuilder. */
    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    public static javafx.scene.canvas.CanvasBuilder<?> create() {
        return new javafx.scene.canvas.CanvasBuilder();
    }
    
    private int __set;
    public void applyTo(javafx.scene.canvas.Canvas x) {
        super.applyTo(x);
        int set = __set;
        if ((set & (1 << 0)) != 0) x.setHeight(this.height);
        if ((set & (1 << 1)) != 0) x.setWidth(this.width);
    }
    
    private double height;
    /**
    Set the value of the {@link javafx.scene.canvas.Canvas#getHeight() height} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B height(double x) {
        this.height = x;
        __set |= 1 << 0;
        return (B) this;
    }
    
    private double width;
    /**
    Set the value of the {@link javafx.scene.canvas.Canvas#getWidth() width} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B width(double x) {
        this.width = x;
        __set |= 1 << 1;
        return (B) this;
    }
    
    /**
    Make an instance of {@link javafx.scene.canvas.Canvas} based on the properties set on this builder.
    */
    public javafx.scene.canvas.Canvas build() {
        javafx.scene.canvas.Canvas x = new javafx.scene.canvas.Canvas();
        applyTo(x);
        return x;
    }
}
