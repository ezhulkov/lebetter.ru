package org.ohm.lebetter.spring.service.impl;

import org.room13.mallcore.spring.service.impl.SyncFileHandler;
import org.room13.mallcore.web.pojo.UploadContext;


public class StoreOnlyHandler extends SyncFileHandler {

    @Override
    public UploadContext startSyncProcess(UploadContext uploadContext) throws Exception {
        return uploadContext;
    }

    @Override
    protected boolean deleteEntityOnAsyncFail() {
        return false;
    }

    @Override
    public boolean isStoreOriginal() {
        return true;
    }


    @SuppressWarnings("PMD")
    public String getOriginalFileName() {
        return super.getOriginalFileName();
    }

    @SuppressWarnings("PMD")
    public void setOriginalFileName(String originalFileName) {
        super.setOriginalFileName(originalFileName);
    }

}
