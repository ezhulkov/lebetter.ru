package org.ohm.lebetter.spring.service.impl.imaging;

import org.ohm.lebetter.spring.service.Constants;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.ProductPhotoEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.service.ProductPhotoManager;
import org.ohm.lebetter.spring.service.ServiceManager;
import org.room13.mallcore.model.ImageAware;
import org.room13.mallcore.spring.service.DataManager;
import org.room13.mallcore.spring.service.FileData;
import org.room13.mallcore.spring.service.impl.AbstractFileHandler;
import org.room13.mallcore.spring.service.impl.imaging.ImageFileSource;
import org.room13.mallcore.spring.service.impl.imaging.ImageOperation;
import org.room13.mallcore.spring.service.impl.imaging.JcrImageFileSource;
import org.room13.mallcore.web.pojo.UploadContext;

import javax.servlet.http.HttpServletRequest;

import static org.room13.mallcore.model.ImageAware.ImageStatus.ERROR;
import static org.room13.mallcore.model.ImageAware.ImageStatus.PROCESSING;
import static org.room13.mallcore.model.ImageAware.ImageStatus.READY;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 21.10.11
 * Time: 14:22
 * To change this template use File | Settings | File Templates.
 */
public class ProductPhotoHandler extends AbstractFileHandler {


    public void init() throws Exception {
        setOriginalFileName(DataManager.FileNames.ORIGINAL_FILE.getFileName() + ".jpg");
    }


    @Override
    public UploadContext storeOriginal(UploadContext uploadContext) throws Exception {
        ProductEntity product = (ProductEntity) uploadContext.getObjectBaseEntity();
        ProductPhotoManager ppm = ((ServiceManager) serviceManager).getProductPhotoManager();

        for (FileData fileData : uploadContext.getFiles()) {
            ProductPhotoEntity photo;
            if (fileData != null && fileData.getTempFile() != null) {
                photo = new ProductPhotoEntity();
                photo.setProduct(product);
                String name = fileData.getName();
                photo.setName(name.substring(0, Math.min(63, name.length())));
                ppm.create(photo, null, (UserEntity) uploadContext.getCaller());
                fileData.setChildEntityId(photo.getId());

                serviceManager.getDataManager().writeData(photo, fileData.getTempFile(),
                                                          getOriginalFileName(), Constants.MimeTypes.JPEG);
                uploadContext.getProperties().put(DataManager.FILE_EXT, fileData.getExtentionTempFile());

                setPhotoStatus(uploadContext, PROCESSING, ppm, photo);
            }
        }
        return uploadContext;

    }

    @Override
    public void startAsyncProcessWithoutTransaction(UploadContext uploadContext) throws Exception {
        ProductEntity product = (ProductEntity) uploadContext.getObjectBaseEntity();
        ProductPhotoManager ppm = ((ServiceManager) serviceManager).getProductPhotoManager();

        for (FileData fileData : uploadContext.getFiles()) {
            if (fileData != null && fileData.getTempFile() != null) {
                ProductPhotoEntity photo = null;
                try {
                    photo = ppm.get(fileData.getChildEntityId());

                    // create operations
                    ImageOperation convp = getImageUtilLayer().createConvertOperation("jpeg");
                    ImageOperation ucpop = getImageUtilLayer().createUpdateColorProfileOperation();
                    ImageOperation resOp = getImageUtilLayer().createResizeOperation(
                            jpegQuality, Constants.JPEG_RESOLUTION,
                            largeImageProcessing, stretchSmallImages, imageResizeData.getImageResizeData());

                    // create image source
                    photo.setProduct(product);
                    String path = serviceManager.getDataManager().collapseOBEPath(photo);
                    ImageFileSource imageFileSource = new JcrImageFileSource(ImageFileSource.SourceType.IMAGE,
                                                                             Constants.MimeTypes.JPEG, getImageUtilLayer(),
                                                                             path, getOriginalFileName());

                    // perform
                    getImageUtilLayer().performBatch(path, getOriginalFileName(), imageFileSource, convp, ucpop, resOp);

                    setPhotoStatus(uploadContext, READY, ppm, photo);

                    fileData.setCode(FileData.OK);
                } catch (Exception e) {
                    if (photo != null) {
                        setPhotoStatus(uploadContext, ERROR, ppm, photo);
                    }


                }
            }
        }
    }

    protected ProductPhotoEntity setPhotoStatus(UploadContext uploadContext, ImageAware.ImageStatus status,
                                                ProductPhotoManager ppm, ProductPhotoEntity photo) {
        synchronized (photo) {
            photo = ppm.get(photo.getId());
            if (photo != null) {
                setImageStatus(photo, status, uploadContext.getCaller());
            }
        }
        return photo;
    }

    @Override
    public String formatServerResponse(UploadContext uploadContext,
                                       HttpServletRequest request) {
        Long objectId = (Long) uploadContext.getProperties().get(UploadContext.OBJECT_ID);
        String objectCode = uploadContext.getProperties().get(UploadContext.OBJECT_CODE).toString();
        return serviceManager.getDataManager().
                getDataFullURL(objectCode, objectId, "");
    }

    @Override
    public UploadContext startSyncProcess(UploadContext uploadContext) throws Exception {
        return uploadContext;
    }

    @Override
    protected boolean deleteEntityOnAsyncFail() {
        return false;
    }
}
