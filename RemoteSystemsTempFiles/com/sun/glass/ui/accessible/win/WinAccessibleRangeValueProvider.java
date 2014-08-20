/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
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
package com.sun.glass.ui.accessible.win;

import com.sun.javafx.accessible.providers.RangeValueProvider;
import com.sun.javafx.accessible.utils.PatternIds;
/**
 *
 * @author paru
 */
public final class WinAccessibleRangeValueProvider extends WinAccessibleBasePatternProvider {

    /**
     * A class static block that initializes the JNI method IDs.
     */
    static {
        _initIDs();
    }
    
    native private static void _initIDs();
    native protected long _createAccessible(long nativeBaseProvider);
    native private void _destroyAccessible(long nativeAccessible);
    
    private long nativeAccessible;  // The native accessible
    private double storeValue = -1;  // TODO: add description
    private double smallChange = -1;  // TODO: add description
    
    /**
     * Downcall to create the native accessible.  This will be used when firing
     * events or when destroying the native accessible.
     * 
     * @param node          the related FX node object.
     * @param provider  the base provider which this pattern provider is chained to.
     */
    public WinAccessibleRangeValueProvider(Object node, WinAccessibleBaseProvider provider) {
        super(node);
        nativeAccessible = _createAccessible(provider.getNativeAccessible());
    }
    
    /**
     * Get the native accessible
     * 
     * @return the native accessible
     */
    @Override
    long getNativeAccessible() {
        return nativeAccessible;
    }
    
    // Downcalls
    
    /**
     * Destroy the native accessible
     * 
     */
    @Override
    public void destroyAccessible() {
        _destroyAccessible(nativeAccessible);
    }

    ////////////////////////////////////
    //
    // Start of upcalls from native code
    //
    ////////////////////////////////////
    
    // Note:
    //   These upcalls are from a native UIA implementation.  This code translates
    //   the upcalls to the UIA-like implementation used in the JavaFX accessibility 
    //   implementation.
    
    /**
     * Get the value
     * 
     * @return the value 
     */
    private double getValue() {
        if (storeValue == -1) {
            storeValue = ((RangeValueProvider)node).getValue();
        }
        double currentValue = ((RangeValueProvider)node).getValue();
        smallChange = Math.abs(storeValue - currentValue);
        return ((RangeValueProvider)node).getValue();
    }
    
    /**
     * Set the value
     */
    private void setValue() {
        // do nothing: not sure we want to setValue on the slider?
    }
    
    /**
     * Get the Minimum value
     * 
     * @return the minimum value 
     */
    private double getMinimum() {
        return ((RangeValueProvider)node).getMinimum();
    }
    
    /**
     * Get the Maximum value
     * 
     * @return the maximum value 
     */
    private double getMaximum() {
        return ((RangeValueProvider)node).getMaximum();
    }
    
    /**
     * Determine if the control's value is read only
     * 
     * @return whether or not the control's value is read only
     */
    private boolean getIsReadOnly() {
        return ((RangeValueProvider)node).isReadOnly();
    }
    
    /**
     * TODO: Add Description
     * 
     * @return TODO: Add description
     */
    private double getSmallChange() {
        return smallChange;
    }
    
    // TODO: Add getLargeChange?

    /**
     * Return the ID of the pattern supported by this class
     */
    @Override
    public int getPatternId() {
        return PatternIds.RANGE_VALUE;
    }
    
    //////////////////////////////////
    //
    // End of upcalls from native code
    //
    //////////////////////////////////

}
