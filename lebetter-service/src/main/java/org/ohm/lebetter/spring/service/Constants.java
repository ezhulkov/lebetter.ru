package org.ohm.lebetter.spring.service;

public class Constants extends org.room13.mallcore.Constants {

    public static final String CAT_VALUE_STUB = "CAT_VALUE_STUB";
    public static final String SITE_ROOT_URL = "http://lebetter.ru";

    public static class Roles extends org.room13.mallcore.Constants.Roles {
        public static final String ROLE_MANAGER = "ROLE_MANAGER";
        public static final String ROLE_DEALER = "ROLE_DEALER";
        public static final String ROLE_HUMAN = "ROLE_HUMAN";
    }

    public static class Actions extends org.room13.mallcore.Constants.Actions {
        public static final String ADD_USER = "ADD_USER";
        public static final String EDIT_USER = "EDIT_USER";
        public static final String DEL_USER = "DEL_USER";
        public static final String ADD_PRODUCT = "ADD_PRODUCT";
        public static final String EDIT_PRODUCT = "EDIT_PRODUCT";
        public static final String DEL_PRODUCT = "DEL_PRODUCT";
    }

    public static enum FileNames {

        XSMALL_PHOTO("xsmall_photo.jpg"),
        SMALL_PHOTO("small_photo.jpg"),
        MEDIUM_PHOTO("medium_photo.jpg"),
        BIG_PHOTO("big_photo.jpg"),
        BIG_PHOTO1("big1_photo.jpg"),
        BIG_PHOTO2("big2_photo.jpg"),
        ORIGINAL("original.jpg");

        protected String fileName;

        FileNames(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }

        @Override
        public String toString() {
            return fileName != null ? fileName : super.toString();
        }
    }

}
