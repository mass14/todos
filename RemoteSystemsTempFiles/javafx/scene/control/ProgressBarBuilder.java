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
Builder class for javafx.scene.control.ProgressBar
@see javafx.scene.control.ProgressBar
@deprecated This class is deprecated and will be removed in the next version
* @since JavaFX 2.0
*/
@javax.annotation.Generated("Generated by javafx.builder.processor.BuilderProcessor")
@Deprecated
public class ProgressBarBuilder<B extends javafx.scene.control.ProgressBarBuilder<B>> extends javafx.scene.control.ProgressIndicatorBuilder<B> {
    protected ProgressBarBuilder() {
    }
    
    /** Creates a new instance of ProgressBarBuilder. */
    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    public static javafx.scene.control.ProgressBarBuilder<?> create() {
        return new javafx.scene.control.ProgressBarBuilder();
    }
    
    /**
    Make an instance of {@link javafx.scene.control.ProgressBar} based on the properties set on this builder.
    */
    public javafx.scene.control.ProgressBar build() {
        javafx.scene.control.ProgressBar x = new javafx.scene.control.ProgressBar();
        applyTo(x);
        return x;
    }
}
