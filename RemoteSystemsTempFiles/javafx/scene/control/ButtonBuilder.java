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

package javafx.scene.control;

/**
Builder class for javafx.scene.control.Button
@see javafx.scene.control.Button
@deprecated This class is deprecated and will be removed in the next version
* @since JavaFX 2.0
*/
@javax.annotation.Generated("Generated by javafx.builder.processor.BuilderProcessor")
@Deprecated
public class ButtonBuilder<B extends javafx.scene.control.ButtonBuilder<B>> extends javafx.scene.control.ButtonBaseBuilder<B> implements javafx.util.Builder<javafx.scene.control.Button> {
    protected ButtonBuilder() {
    }
    
    /** Creates a new instance of ButtonBuilder. */
    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    public static javafx.scene.control.ButtonBuilder<?> create() {
        return new javafx.scene.control.ButtonBuilder();
    }
    
    private int __set;
    public void applyTo(javafx.scene.control.Button x) {
        super.applyTo(x);
        int set = __set;
        if ((set & (1 << 0)) != 0) x.setCancelButton(this.cancelButton);
        if ((set & (1 << 1)) != 0) x.setDefaultButton(this.defaultButton);
    }
    
    private boolean cancelButton;
    /**
    Set the value of the {@link javafx.scene.control.Button#isCancelButton() cancelButton} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B cancelButton(boolean x) {
        this.cancelButton = x;
        __set |= 1 << 0;
        return (B) this;
    }
    
    private boolean defaultButton;
    /**
    Set the value of the {@link javafx.scene.control.Button#isDefaultButton() defaultButton} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B defaultButton(boolean x) {
        this.defaultButton = x;
        __set |= 1 << 1;
        return (B) this;
    }
    
    /**
    Make an instance of {@link javafx.scene.control.Button} based on the properties set on this builder.
    */
    public javafx.scene.control.Button build() {
        javafx.scene.control.Button x = new javafx.scene.control.Button();
        applyTo(x);
        return x;
    }
}
