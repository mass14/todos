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

package javafx.scene;

/**
Builder class for javafx.scene.ImageCursor
@see javafx.scene.ImageCursor
@deprecated This class is deprecated and will be removed in the next version
* @since JavaFX 2.0
*/
@javax.annotation.Generated("Generated by javafx.builder.processor.BuilderProcessor")
@Deprecated
public class ImageCursorBuilder<B extends javafx.scene.ImageCursorBuilder<B>> implements javafx.util.Builder<javafx.scene.ImageCursor> {
    protected ImageCursorBuilder() {
    }
    
    /** Creates a new instance of ImageCursorBuilder. */
    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    public static javafx.scene.ImageCursorBuilder<?> create() {
        return new javafx.scene.ImageCursorBuilder();
    }
    
    private double hotspotX;
    /**
    Set the value of the {@link javafx.scene.ImageCursor#getHotspotX() hotspotX} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B hotspotX(double x) {
        this.hotspotX = x;
        return (B) this;
    }
    
    private double hotspotY;
    /**
    Set the value of the {@link javafx.scene.ImageCursor#getHotspotY() hotspotY} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B hotspotY(double x) {
        this.hotspotY = x;
        return (B) this;
    }
    
    private javafx.scene.image.Image image;
    /**
    Set the value of the {@link javafx.scene.ImageCursor#getImage() image} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B image(javafx.scene.image.Image x) {
        this.image = x;
        return (B) this;
    }
    
    /**
    Make an instance of {@link javafx.scene.ImageCursor} based on the properties set on this builder.
    */
    public javafx.scene.ImageCursor build() {
        javafx.scene.ImageCursor x = new javafx.scene.ImageCursor(this.image, this.hotspotX, this.hotspotY);
        return x;
    }
}