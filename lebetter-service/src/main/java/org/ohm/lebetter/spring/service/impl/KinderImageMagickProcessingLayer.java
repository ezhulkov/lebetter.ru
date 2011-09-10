package org.ohm.lebetter.spring.service.impl;

import org.ohm.lebetter.spring.service.KinderImageLayer;
import org.room13.mallcore.spring.service.impl.LocalImageMagickProcessingLayer;
import org.room13.mallcore.spring.service.impl.imaging.MagickImageWrapper;

public class KinderImageMagickProcessingLayer
        extends LocalImageMagickProcessingLayer
        implements KinderImageLayer<MagickImageWrapper> {

    public KinderImageMagickProcessingLayer() throws Exception {
        super();
    }

}
