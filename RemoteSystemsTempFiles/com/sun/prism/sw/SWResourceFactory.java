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

package com.sun.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.prism.MediaFrame;
import com.sun.prism.Mesh;
import com.sun.prism.MeshView;
import com.sun.prism.PhongMaterial;
import com.sun.prism.PixelFormat;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.ResourceFactory;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.Texture.Usage;
import com.sun.prism.Texture.WrapMode;
import com.sun.prism.impl.BaseResourceFactory;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.impl.VertexBuffer;
import com.sun.prism.impl.shape.BasicRoundRectRep;
import com.sun.prism.impl.shape.BasicShapeRep;
import com.sun.prism.shape.ShapeRep;

final class SWResourceFactory
    extends BaseResourceFactory
        implements ResourceFactory {

    private static final ShapeRep theRep = new BasicShapeRep();
    private static final ShapeRep rectRep = new BasicRoundRectRep();

    private Screen screen;
    private final SWContext context;

    public SWResourceFactory(Screen screen) {
        this.screen = screen;
        this.context = new SWContext(this);
    }

    public TextureResourcePool getTextureResourcePool() {
        return SWTexturePool.instance;
    }

    public Screen getScreen() {
        return screen;
    }

    SWContext getContext() {
        return context;
    }
    
    @Override public void dispose() {
        context.dispose();
    }

    @Override public ShapeRep createArcRep() {
        return theRep;
    }
    
    @Override public ShapeRep createEllipseRep() {
        return theRep;
    }
    
    @Override public ShapeRep createRoundRectRep() {
        return rectRep;
    }
    
    @Override public ShapeRep createPathRep() {
        return theRep;
    }
            
    @Override public VertexBuffer createVertexBuffer(int maxQuads) {
        throw new UnsupportedOperationException("createVertexBuffer:unimp");
    }

    
    @Override public Presentable createPresentable(PresentableState pState) {
        if (PrismSettings.debug) {
            System.out.println("+ SWRF.createPresentable()");
        }
        return new SWPresentable(pState, this);
    }

    public int getRTTWidth(int w, WrapMode wrapMode) {
        return w;
    }

    public int getRTTHeight(int h, WrapMode wrapMode) {
        return h;
    }

    @Override
    public boolean isCompatibleTexture(Texture tex) {
        return tex instanceof SWTexture;
    }

    @Override public RTTexture createRTTexture(int width, int height, WrapMode wrapMode, boolean antiAliasing) {
        return createRTTexture(width, height, wrapMode);
    }

    @Override public RTTexture createRTTexture(int width, int height,
                                               WrapMode wrapMode)
    {
        SWTexturePool pool = SWTexturePool.instance;
        long size = pool.estimateRTTextureSize(width, height, false);
        if (!pool.prepareForAllocation(size)) {
            return null;
        }
        return new SWRTTexture(this, width, height);
    }

    @Override public int getMaximumTextureSize() {
        return Integer.MAX_VALUE;
    }

    @Override public boolean isFormatSupported(PixelFormat format) {
        switch (format) {
            case BYTE_RGB:
            case BYTE_GRAY:
            case INT_ARGB_PRE:
            case BYTE_BGRA_PRE:
                return true;
            case BYTE_ALPHA:
            case BYTE_APPLE_422:
            case MULTI_YCbCr_420:
            case FLOAT_XYZW:
            default:
                return false;
        }
    }

    @Override public Texture createTexture(MediaFrame vdb) {
        return new SWArgbPreTexture(this, WrapMode.CLAMP_TO_EDGE, vdb.getWidth(), vdb.getHeight());
    }
            
    @Override public Texture createTexture(PixelFormat formatHint,
                                           Usage usageHint,
                                           WrapMode wrapMode,
                                           int w, int h)
    {
        SWTexturePool pool = SWTexturePool.instance;
        long size = pool.estimateTextureSize(w, h, formatHint);
        if (!pool.prepareForAllocation(size)) {
            return null;
        }
        return SWTexture.create(this, formatHint, wrapMode, w, h);
    }

    public PhongMaterial createPhongMaterial() {
        throw new UnsupportedOperationException("Not supported yet.");
}

    public MeshView createMeshView(Mesh mesh) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Mesh createMesh() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
