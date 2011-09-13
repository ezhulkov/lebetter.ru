package org.ohm.lebetter;

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

    public static class FileNames {
        public static final String BIG_PRODUCT_PHOTO_FILE = "photo_big.jpg";
        public static final String MEDIUM_PRODUCT_PHOTO_FILE = "photo_medium.jpg";
        public static final String SMALL_PRODUCT_PHOTO_FILE = "photo_small.jpg";
    }

}
