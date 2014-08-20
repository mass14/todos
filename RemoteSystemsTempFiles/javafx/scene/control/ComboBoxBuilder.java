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
Builder class for javafx.scene.control.ComboBox
@see javafx.scene.control.ComboBox
@deprecated This class is deprecated and will be removed in the next version
* @since JavaFX 2.1
*/
@javax.annotation.Generated("Generated by javafx.builder.processor.BuilderProcessor")
@Deprecated
public class ComboBoxBuilder<T, B extends javafx.scene.control.ComboBoxBuilder<T, B>> extends javafx.scene.control.ComboBoxBaseBuilder<T, B> implements javafx.util.Builder<javafx.scene.control.ComboBox<T>> {
    protected ComboBoxBuilder() {
    }
    
    /** Creates a new instance of ComboBoxBuilder. */
    @SuppressWarnings({"deprecation", "rawtypes", "unchecked"})
    public static <T> javafx.scene.control.ComboBoxBuilder<T, ?> create() {
        return new javafx.scene.control.ComboBoxBuilder();
    }
    
    private int __set;
    public void applyTo(javafx.scene.control.ComboBox<T> x) {
        super.applyTo(x);
        int set = __set;
        if ((set & (1 << 0)) != 0) x.setButtonCell(this.buttonCell);
        if ((set & (1 << 1)) != 0) x.setCellFactory(this.cellFactory);
        if ((set & (1 << 2)) != 0) x.setConverter(this.converter);
        if ((set & (1 << 3)) != 0) x.setItems(this.items);
        if ((set & (1 << 4)) != 0) x.setSelectionModel(this.selectionModel);
        if ((set & (1 << 5)) != 0) x.setVisibleRowCount(this.visibleRowCount);
    }
    
    private javafx.scene.control.ListCell<T> buttonCell;
    /**
    Set the value of the {@link javafx.scene.control.ComboBox#getButtonCell() buttonCell} property for the instance constructed by this builder.
    * @since JavaFX 2.2
    */
    @SuppressWarnings("unchecked")
    public B buttonCell(javafx.scene.control.ListCell<T> x) {
        this.buttonCell = x;
        __set |= 1 << 0;
        return (B) this;
    }
    
    private javafx.util.Callback<javafx.scene.control.ListView<T>,javafx.scene.control.ListCell<T>> cellFactory;
    /**
    Set the value of the {@link javafx.scene.control.ComboBox#getCellFactory() cellFactory} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B cellFactory(javafx.util.Callback<javafx.scene.control.ListView<T>,javafx.scene.control.ListCell<T>> x) {
        this.cellFactory = x;
        __set |= 1 << 1;
        return (B) this;
    }
    
    private javafx.util.StringConverter<T> converter;
    /**
    Set the value of the {@link javafx.scene.control.ComboBox#getConverter() converter} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B converter(javafx.util.StringConverter<T> x) {
        this.converter = x;
        __set |= 1 << 2;
        return (B) this;
    }
    
    private javafx.collections.ObservableList<T> items;
    /**
    Set the value of the {@link javafx.scene.control.ComboBox#getItems() items} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B items(javafx.collections.ObservableList<T> x) {
        this.items = x;
        __set |= 1 << 3;
        return (B) this;
    }
    
    private javafx.scene.control.SingleSelectionModel<T> selectionModel;
    /**
    Set the value of the {@link javafx.scene.control.ComboBox#getSelectionModel() selectionModel} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B selectionModel(javafx.scene.control.SingleSelectionModel<T> x) {
        this.selectionModel = x;
        __set |= 1 << 4;
        return (B) this;
    }
    
    private int visibleRowCount;
    /**
    Set the value of the {@link javafx.scene.control.ComboBox#getVisibleRowCount() visibleRowCount} property for the instance constructed by this builder.
    */
    @SuppressWarnings("unchecked")
    public B visibleRowCount(int x) {
        this.visibleRowCount = x;
        __set |= 1 << 5;
        return (B) this;
    }
    
    /**
    Make an instance of {@link javafx.scene.control.ComboBox} based on the properties set on this builder.
    */
    public javafx.scene.control.ComboBox<T> build() {
        javafx.scene.control.ComboBox<T> x = new javafx.scene.control.ComboBox<T>();
        applyTo(x);
        return x;
    }
}
