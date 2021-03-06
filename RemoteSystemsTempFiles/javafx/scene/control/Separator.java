/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;

import javafx.css.StyleableObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.skin.SeparatorSkin;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;

/**
 * A horizontal or vertical separator line. The visual appearance of this
 * separator can be controlled via CSS. A horizontal separator occupies the
 * full horizontal space allocated to it (less padding), and a vertical
 * separator occupies the full vertical space allocated to it (less padding).
 * The {@link #halignment} and {@link #valignment} properties determine how the
 * separator is positioned in the other dimension, for example, how a horizontal
 * separator is positioned vertically within its allocated space.
 * <p>
 * The separator is horizontal (i.e. <code>isVertical() == false</code>) by default.
 * <p>
 * The style-class for this control is "separator".
 * <p>
 * The separator provides two pseudo-classes "horizontal" and "vertical" which
 * are mutually exclusive. The "horizontal" pseudo-class applies if the
 * separator is horizontal, and the "vertical" pseudo-class applies if the
 * separator is vertical.
 *
 * <p>
 * Separator sets focusTraversable to false.
 * </p>
 * @since JavaFX 2.0
 */
public class Separator extends Control {

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new horizontal separator with halignment and valignment set to their
     * respective CENTER values.
     */
    public Separator() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        // focusTraversable is styleable through css. Calling setFocusTraversable
        // makes it look to css like the user set the value and css will not 
        // override. Initializing focusTraversable by calling applyStyle with null
        // StyleOrigin ensures that css will be able to override the value.
        ((StyleableProperty)focusTraversableProperty()).applyStyle(null, Boolean.FALSE);
        
        // initialize pseudo-class state
        pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, true);
    }

    /**
     * Creates a new separator with halignment and valignment set to their respective CENTER
     * values. The direction of the separator is specified by the vertical property.
     *
     * @param orientation Specifies whether the Separator instance is initially
     *      vertical or horizontal.
     */
    public Separator(Orientation orientation) {
        this();
        setOrientation(orientation);
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * The orientation of the {@code Separator} can either be horizontal
     * or vertical.
     */
    private ObjectProperty<Orientation> orientation = 
        new StyleableObjectProperty<Orientation>(Orientation.HORIZONTAL) {

            @Override protected void invalidated() {
                final boolean isVertical = (get() == Orientation.VERTICAL);
                pseudoClassStateChanged(VERTICAL_PSEUDOCLASS_STATE,    isVertical);
                pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, !isVertical);
            }

            @Override 
            public CssMetaData<Separator,Orientation> getCssMetaData() {
                return StyleableProperties.ORIENTATION;
            }

            @Override
            public Object getBean() {
                return Separator.this;
            }

            @Override
            public String getName() {
                return "orientation";
            }
        };
    public final void setOrientation(Orientation value) { orientation.set(value); }
    public final Orientation getOrientation() { return orientation.get(); }
    public final ObjectProperty<Orientation> orientationProperty() { return orientation; }

    /**
     * For vertical separators, specifies the horizontal position of the
     * separator line within the separator control's space. Ignored for
     * horizontal separators.
     */
    private ObjectProperty<HPos> halignment;

    public final void setHalignment(HPos value) {
        halignmentProperty().set(value);
    }

    public final HPos getHalignment() {
        return halignment == null ? HPos.CENTER : halignment.get();
    }

    public final ObjectProperty<HPos> halignmentProperty() {
        if (halignment == null) {
            halignment = new StyleableObjectProperty<HPos>(HPos.CENTER) {

                @Override
                public Object getBean() {
                    return Separator.this;
                }

                @Override
                public String getName() {
                    return "halignment";
                }

                @Override
                public CssMetaData<Separator,HPos> getCssMetaData() {
                    return StyleableProperties.HALIGNMENT;
                }
                
            };
        }
        return halignment;
    }

    /**
     * For horizontal separators, specifies the vertical alignment of the
     * separator line within the separator control's space. Ignored for
     * vertical separators.
     */
    private ObjectProperty<VPos> valignment;
    public final void setValignment(VPos value) {
        valignmentProperty().set(value);
    }

    public final VPos getValignment() {
        return valignment == null ? VPos.CENTER : valignment.get();
    }

    public final ObjectProperty<VPos> valignmentProperty() {
        if (valignment == null) {
            valignment = new StyleableObjectProperty<VPos>(VPos.CENTER) {

                @Override
                public Object getBean() {
                    return Separator.this;
                }

                @Override
                public String getName() {
                    return "valignment";
                }

                @Override
                public CssMetaData<Separator,VPos> getCssMetaData() {
                    return StyleableProperties.VALIGNMENT;
                }
                
            };
        }
        return valignment;
    }

    /** {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new SeparatorSkin(this);
    }

    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "separator";

    private static final String PSEUDO_CLASS_VERTICAL = "vertical";
    private static final String PSEUDO_CLASS_HORIZONTAL = "horizontal";

    private static class StyleableProperties {
        private static final CssMetaData<Separator,Orientation> ORIENTATION = 
                new CssMetaData<Separator,Orientation>("-fx-orientation",
                new EnumConverter<Orientation>(Orientation.class),
                Orientation.HORIZONTAL) {

            @Override
            public Orientation getInitialValue(Separator node) {
                // A vertical Separator should remain vertical 
                return node.getOrientation();
            }

            @Override
            public boolean isSettable(Separator n) {
                return n.orientation == null || !n.orientation.isBound();
            }

            @Override
            public StyleableProperty<Orientation> getStyleableProperty(Separator n) {
                return (StyleableProperty<Orientation>)n.orientationProperty();
            }
        };
        
        private static final CssMetaData<Separator,HPos> HALIGNMENT = 
                new CssMetaData<Separator,HPos>("-fx-halignment",
                new EnumConverter<HPos>(HPos.class),
                HPos.CENTER) {

            @Override
            public boolean isSettable(Separator n) {
                return n.halignment == null || !n.halignment.isBound();
            }

            @Override
            public StyleableProperty<HPos> getStyleableProperty(Separator n) {
                return (StyleableProperty<HPos>)n.halignmentProperty();
            }
        };
        
        private static final CssMetaData<Separator,VPos> VALIGNMENT = 
                new CssMetaData<Separator,VPos>("-fx-valignment",
                new EnumConverter<VPos>(VPos.class),
                VPos.CENTER){

            @Override
            public boolean isSettable(Separator n) {
                return n.valignment == null || !n.valignment.isBound();
            }

            @Override
            public StyleableProperty<VPos> getStyleableProperty(Separator n) {
                return (StyleableProperty<VPos>)n.valignmentProperty();
            }
        };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                new ArrayList<CssMetaData<? extends Styleable, ?>>(Control.getClassCssMetaData());
            styleables.add(ORIENTATION);
            styleables.add(HALIGNMENT);
            styleables.add(VALIGNMENT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    /**
     * @return The CssMetaData associated with this class, which may include the
     * CssMetaData of its super classes.
     * @since JavaFX 8.0
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return Separator.StyleableProperties.STYLEABLES;
    }

    /**
     * RT-19263
     * @treatAsPrivate implementation detail
     * @deprecated This is an experimental API that is not intended for general use and is subject to change in future versions
     * @since JavaFX 8.0
     */
    @Deprecated
    @Override protected List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
    private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");
    
    /**
      * Most Controls return true for focusTraversable, so Control overrides
      * this method to return true, but Separator returns false for
      * focusTraversable's initial value; hence the override of the override. 
      * This method is called from CSS code to get the correct initial value.
      * @treatAsPrivate implementation detail
      */
    @Deprecated @Override
    protected /*do not make final*/ Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
    }
    
}
