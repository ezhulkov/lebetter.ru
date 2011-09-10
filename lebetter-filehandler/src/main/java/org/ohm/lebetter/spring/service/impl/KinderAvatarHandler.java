package org.ohm.lebetter.spring.service.impl;

import org.room13.mallcore.Constants;
import org.room13.mallcore.model.ImageAware;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.service.FileData;
import org.room13.mallcore.spring.service.GenericManager;
import org.room13.mallcore.spring.service.impl.AvatarHandler;
import org.room13.mallcore.spring.service.impl.imaging.ImageFileSource;
import org.room13.mallcore.spring.service.impl.imaging.ImageOperation;
import org.room13.mallcore.spring.service.impl.imaging.JcrImageFileSource;
import org.room13.mallcore.web.pojo.UploadContext;

import java.util.List;

import static org.room13.mallcore.Constants.JPEG_RESOLUTION;

public class KinderAvatarHandler extends AvatarHandler {
    @Override
    public UploadContext startSyncProcess(UploadContext uploadContext) throws Exception {

        FileData fileData;

        ObjectBaseEntity obe = uploadContext.getObjectBaseEntity();
        GenericManager gm = serviceManager.getGenericManagerMap().get(obe.getEntityCode());

        List<FileData> list = uploadContext.getFiles();
        fileData = list.get(0);
        if (fileData != null && fileData.getTempFile() != null) {

            // create operations
            ImageOperation resOp = getImageUtilLayer().createResizeOperation(
                    jpegQuality, JPEG_RESOLUTION, largeImageProcessing,
                    stretchSmallImages, imageResizeData);

            // create image source
            String path = serviceManager.getDataManager().collapseOBEPath(obe);
            ImageFileSource imageFileSource = new JcrImageFileSource(ImageFileSource.SourceType.IMAGE,
                    Constants.MimeTypes.JPEG, getImageUtilLayer(),
                    path, getOriginalFileName());

            // perform
            getImageUtilLayer().performBatch(path, getOriginalFileName(),
                    imageFileSource, resOp);

            synchronized (obe) {
                obe = gm.get(obe.getId());
                setImageStatus(obe, ImageAware.ImageStatus.READY, uploadContext.getCaller());
            }
            fileData.setCode(FileData.OK);
        }

        return uploadContext;
    }

}
