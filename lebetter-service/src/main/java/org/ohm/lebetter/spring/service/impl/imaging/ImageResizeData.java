package org.ohm.lebetter.spring.service.impl.imaging;

import org.ohm.lebetter.spring.service.Constants.FileNames;
import org.room13.mallcore.util.ImageUtil;

import java.awt.*;
import java.lang.reflect.Field;


public class ImageResizeData extends org.room13.mallcore.spring.service.impl.imaging.ImageResizeData {

    public ImageResizeData(String packed) throws NoSuchFieldException, IllegalAccessException {
        super(null, (Dimension) null, false, ImageUtil.ResizeStrategy.INSCRIBED);
        String[] data = packed.split("\\;");

        Field fileNameField = FileNames.class.getField(data[0]);
        fileNameField.setAccessible(true);
        this.fileName = (String) fileNameField.get(null);


        this.rectSize = getFileSize(data[1]);
        this.smartCrop = data[2] == null ? false : Boolean.parseBoolean(data[2]);
        this.resizeStrategy = data[3] == null ?
                ImageUtil.ResizeStrategy.INSCRIBED :
                ImageUtil.ResizeStrategy.valueOf(data[3]);
    }

    public ImageResizeData(String fileName, Dimension rectSize,
                           boolean smartCrop, ImageUtil.ResizeStrategy resizeStrategy) {
        super(fileName, rectSize, smartCrop, resizeStrategy);
    }

    public ImageResizeData(String fileName, String rectSize,
                           boolean smartCrop, ImageUtil.ResizeStrategy resizeStrategy) {
        super(fileName, rectSize, smartCrop, resizeStrategy);
    }
}
