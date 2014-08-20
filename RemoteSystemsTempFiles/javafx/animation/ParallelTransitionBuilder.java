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

package javafx.animation;

/**
Builder class for javafx.animation.ParallelTransition
@see javafx.animation.ParallelTransition
@deprecated This class is deprecated and will be removed in the next version
* @since JavaFX 2.0
*/
@javax.annotation.Generated("Generated by javafx.builder.processor.BuilderProcessor")
@Deprecated
public final class ParallelTransitionBuilder extends javafx.animation.TransitionBuilder<javafx.animation.ParallelTransitionBuilder> implements javafx.util.Builder<javafx.animation.ParallelTransition> {
    protected ParallelTransitionBuilder() {
    }
    
    /** Creates a new instance of ParallelTransitionBuilder. */
    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    public static javafx.animation.ParallelTransitionBuilder create() {
        return new javafx.animation.ParallelTransitionBuilder();
    }
    
    private int __set;
    public void applyTo(javafx.animation.ParallelTransition x) {
        super.applyTo(x);
        int set = __set;
        if ((set & (1 << 0)) != 0) x.getChildren().addAll(this.children);
        if ((set & (1 << 1)) != 0) x.setNode(this.node);
    }
    
    private java.util.Collection<? extends javafx.animation.Animation> children;
    /**
    Add the given items to the List of items in the {@link javafx.animation.ParallelTransition#getChildren() children} property for the instance constructed by this builder.
    */
    public javafx.animation.ParallelTransitionBuilder children(java.util.Collection<? extends javafx.animation.Animation> x) {
        this.children = x;
        __set |= 1 << 0;
        return this;
    }
    
    /**
    Add the given items to the List of items in the {@link javafx.animation.ParallelTransition#getChildren() children} property for the instance constructed by this builder.
    */
    public javafx.animation.ParallelTransitionBuilder children(javafx.animation.Animation... x) {
        return children(java.util.Arrays.asList(x));
    }
    
    private javafx.scene.Node node;
    /**
    Set the value of the {@link javafx.animation.ParallelTransition#getNode() node} property for the instance constructed by this builder.
    */
    public javafx.animation.ParallelTransitionBuilder node(javafx.scene.Node x) {
        this.node = x;
        __set |= 1 << 1;
        return this;
    }
    
    /**
    Make an instance of {@link javafx.animation.ParallelTransition} based on the properties set on this builder.
    */
    public javafx.animation.ParallelTransition build() {
        javafx.animation.ParallelTransition x = new javafx.animation.ParallelTransition();
        applyTo(x);
        return x;
    }
}
