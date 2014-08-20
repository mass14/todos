/*
 * Copyright (c) 2008, 2013, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.scenario.effect;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;

/**
 * An effect that returns a cropped version of the input.
 */
public class Crop extends CoreEffect {

    // TODO: This class should go away once we fix RT-1347...

    /**
     * Constructs a new {@code Crop} effect which crops the output of
     * the specified source {@code Effect} to the bounds of the default
     * input.
     *
     * @param source the input {@code Effect} for image data
     */
    public Crop(Effect source) {
        this(source, DefaultInput);
    }

    /**
     * Constructs a new {@code Crop} effect to crop the output of the
     * specified source {@code Effect} to the bounds of the specified
     * bounds {@code Effect}.
     *
     * @param source the input {@code Effect} for image data
     * @param boundsInput the input {@code Effect} that controls bounds
     */
    public Crop(Effect source, Effect boundsInput) {
        super(source, boundsInput);
        updatePeerKey("Crop");
    }

    /**
     * Returns the input for this {@code Effect}.
     *
     * @return the input for this {@code Effect}
     */
    public final Effect getInput() {
        return getInputs().get(0);
    }

    /**
     * Sets the source input for this {@code Effect} to a specific
     * {@code Effect} or to the default input if {@code input} is
     * {@code null}.
     *
     * @param input the input for this {@code Effect}
     */
    public void setInput(Effect input) {
        setInput(0, input);
    }

    /**
     * Returns the bounds input for this {@code Effect}.
     *
     * @return the bounds input for this {@code Effect}
     */
    public final Effect getBoundsInput() {
        return getInputs().get(1);
    }

    /**
     * Sets the bounds input for this {@code Effect}.
     * Sets the bounds input for this {@code Effect} to a specific
     * {@code Effect} or to the default input if {@code input} is
     * {@code null}.
     *
     * @param input the bounds input for this {@code Effect}
     */
    public void setBoundsInput(Effect input) {
        setInput(1, input);
    }

    private Effect getBoundsInput(Effect defaultInput) {
        return getDefaultedInput(1, defaultInput);
    }

    @Override
    public BaseBounds getBounds(BaseTransform transform, Effect defaultInput) {
        return getBoundsInput(defaultInput).getBounds(transform, defaultInput);
    }

    /**
     * Transform the specified point {@code p} from the coordinate space
     * of the primary content input to the coordinate space of the effect
     * output.
     * In essence, this method asks the question "Which output coordinate
     * is most affected by the data at the specified coordinate in the
     * primary source input?"
     * <p>
     * The {@code Crop} effect delegates this operation to its primary
     * (non-bounds) input, or the {@code defaultInput} if the primary
     * input is {@code null}.
     *
     * @param p the point in the coordinate space of the primary content
     *          input to be transformed
     * @param defaultInput the default input {@code Effect} to be used in
     *                     all cases where a filter has a null input
     * @return the transformed point in the coordinate space of the result
     */
    @Override
    public Point2D transform(Point2D p, Effect defaultInput) {
        return getDefaultedInput(0, defaultInput).transform(p, defaultInput);
    }

    /**
     * Transform the specified point {@code p} from the coordinate space
     * of the output of the effect into the coordinate space of the
     * primary content input.
     * In essence, this method asks the question "Which source coordinate
     * contributes most to the definition of the output at the specified
     * coordinate?"
     * <p>
     * The {@code Crop} effect delegates this operation to its primary
     * (non-bounds) input, or the {@code defaultInput} if the primary
     * input is {@code null}.
     *
     * @param p the point in the coordinate space of the result output
     *          to be transformed
     * @param defaultInput the default input {@code Effect} to be used in
     *                     all cases where a filter has a null input
     * @return the untransformed point in the coordinate space of the
     *         primary content input
     */
    @Override
    public Point2D untransform(Point2D p, Effect defaultInput) {
        return getDefaultedInput(0, defaultInput).untransform(p, defaultInput);
    }

    @Override
    public ImageData filter(FilterContext fctx,
                            BaseTransform transform,
                            Rectangle outputClip,
                            Object renderHelper,
                            Effect defaultInput)
    {
        Effect input1 = getDefaultedInput(1, defaultInput);
        BaseBounds cropBounds = input1.getBounds(transform, defaultInput);
        Rectangle cropRect = new Rectangle(cropBounds);
        cropRect.intersectWith(outputClip);
        Effect input0 = getDefaultedInput(0, defaultInput);
        ImageData id = input0.filter(fctx, transform, cropRect, null, defaultInput);
        if (!id.validate(fctx)) {
            return new ImageData(fctx, null, null);
        }
        ImageData ret = filterImageDatas(fctx, transform, cropRect, id);
        id.unref();
        return ret;
    }

    @Override
    protected Rectangle getInputClip(int inputIndex,
                                     BaseTransform transform,
                                     Rectangle outputClip)
    {
        // RT-27564
        // TODO: Since we also crop to the "crop input" and since cropping
        // is a form of clipping, we could further restrict the bounds we
        // ask from the content input here...
        return outputClip;
    }

    @Override
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override
    public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
        Effect di0 = getDefaultedInput(0, defaultInput);
        DirtyRegionContainer drc = di0.getDirtyRegions(defaultInput, regionPool);
        Effect di1 = getDefaultedInput(1, defaultInput);
        BaseBounds cropBounds = di1.getBounds(BaseTransform.IDENTITY_TRANSFORM, defaultInput);
        for (int i = 0; i < drc.size(); i++) {
            drc.getDirtyRegion(i).intersectWith(cropBounds);
            if (drc.checkAndClearRegion(i)) {
                --i;
            }
        }

        return drc;
    }
}
