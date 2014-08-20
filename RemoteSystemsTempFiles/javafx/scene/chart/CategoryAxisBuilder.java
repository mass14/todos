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

package javafx.scene.chart;

/**
Builder class for javafx.scene.chart.CategoryAxis
@see javafx.scene.chart.CategoryAxis
@deprecated This class is deprecated and will be removed in the next version
* @since JavaFX 2.0
*/
@javax.annotation.Generated("Generated by javafx.builder.processor.BuilderProcessor")
@Deprecated
public final class CategoryAxisBuilder extends javafx.scene.chart.AxisBuilder<java.lang.String, javafx.scene.chart.CategoryAxisBuilder> {
    protected CategoryAxisBuilder() {
    }
    
    /** Creates a new instance of CategoryAxisBuilder. */
    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    public static javafx.scene.chart.CategoryAxisBuilder create() {
        return new javafx.scene.chart.CategoryAxisBuilder();
    }
    
    private int __set;
    public void applyTo(javafx.scene.chart.CategoryAxis x) {
        super.applyTo(x);
        int set = __set;
        if ((set & (1 << 0)) != 0) x.setCategories(this.categories);
        if ((set & (1 << 1)) != 0) x.setEndMargin(this.endMargin);
        if ((set & (1 << 2)) != 0) x.setGapStartAndEnd(this.gapStartAndEnd);
        if ((set & (1 << 3)) != 0) x.setStartMargin(this.startMargin);
    }
    
    private javafx.collections.ObservableList<java.lang.String> categories;
    /**
    Set the value of the {@link javafx.scene.chart.CategoryAxis#getCategories() categories} property for the instance constructed by this builder.
    */
    public javafx.scene.chart.CategoryAxisBuilder categories(javafx.collections.ObservableList<java.lang.String> x) {
        this.categories = x;
        __set |= 1 << 0;
        return this;
    }
    
    private double endMargin;
    /**
    Set the value of the {@link javafx.scene.chart.CategoryAxis#getEndMargin() endMargin} property for the instance constructed by this builder.
    */
    public javafx.scene.chart.CategoryAxisBuilder endMargin(double x) {
        this.endMargin = x;
        __set |= 1 << 1;
        return this;
    }
    
    private boolean gapStartAndEnd;
    /**
    Set the value of the {@link javafx.scene.chart.CategoryAxis#isGapStartAndEnd() gapStartAndEnd} property for the instance constructed by this builder.
    */
    public javafx.scene.chart.CategoryAxisBuilder gapStartAndEnd(boolean x) {
        this.gapStartAndEnd = x;
        __set |= 1 << 2;
        return this;
    }
    
    private double startMargin;
    /**
    Set the value of the {@link javafx.scene.chart.CategoryAxis#getStartMargin() startMargin} property for the instance constructed by this builder.
    */
    public javafx.scene.chart.CategoryAxisBuilder startMargin(double x) {
        this.startMargin = x;
        __set |= 1 << 3;
        return this;
    }
    
    /**
    Make an instance of {@link javafx.scene.chart.CategoryAxis} based on the properties set on this builder.
    */
    public javafx.scene.chart.CategoryAxis build() {
        javafx.scene.chart.CategoryAxis x = new javafx.scene.chart.CategoryAxis();
        applyTo(x);
        return x;
    }
}
