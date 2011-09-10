package org.ohm.lebetter;

public class Constants extends org.room13.mallcore.Constants {

    public static final String CAPTCHA_IMG = "CAPTCHA_IMG";
    public static final String KS_REDIRECT = "KS_REDIRECT";
    public static final String CAT_VALUE_STUB = "CAT_VALUE_STUB";
    public static final String SITE_ROOT_URL = "http://kindershopping.ru";

    public static class PropertyCodes {
        public static final String BRAND = "BRAND";
        public static final String SEX = "SEX";
        public static final String GIRL = "GIRL";
        public static final String BOY = "BOY";
        public static final String COLOR = "COLOR";
        public static final String SIZE = "SIZE";
        // типы товаров
        public static final String SHOES_TYPE = "S_TYPE";
        public static final String CLOTHES_TYPE = "C_TYPE";
        public static final String TOYS_TYPE = "T_TYPE";
        public static final String HATS_TYPE = "H_TYPE";
        public static final String CAR_SEAT_TYPE = "CS_TYPE";
        public static final String STROLLER_TYPE = "ST_TYPE";
        public static final String FURNITURE_TYPE = "F_TYPE";
        public static final String BEDDING_TYPE = "B_TYPE";
        public static final String SCHOOL_UNIFORM_TYPE = "SU_TYPE";
        public static final String TRANSPORT_TYPE = "TR_TYPE";
    }

    public static class Roles extends org.room13.mallcore.Constants.Roles {
        public static final String ROLE_MANAGER = "ROLE_MANAGER";
        public static final String ROLE_ORDER_MANAGER = "ROLE_ORDER_MANAGER";
        public static final String ROLE_SHOP_MANAGER = "ROLE_SHOP_MANAGER";
        public static final String ROLE_EDITOR = "ROLE_EDITOR";
    }

    public static class Actions extends org.room13.mallcore.Constants.Actions {
        public static final String ADD_SHOP = "ADD_SHOP";
        public static final String DEL_SHOP = "DEL_SHOP";
        public static final String VIEW_SHOP = "VIEW_SHOP";
        public static final String LIST_SHOP = "LIST_SHOP";
        public static final String EDIT_SHOP = "EDIT_SHOP";

        public static final String ADD_PRODUCT = "ADD_PRODUCT";
        public static final String DEL_PRODUCT = "DEL_PRODUCT";
        public static final String VIEW_PRODUCT = "VIEW_PRODUCT";
        public static final String LIST_PRODUCT = "LIST_PRODUCT";
        public static final String EDIT_PRODUCT = "EDIT_PRODUCT";

        public static final String ADD_BRAND = "ADD_BRAND";
        public static final String DEL_BRAND = "DEL_BRAND";
        public static final String VIEW_BRAND = "VIEW_BRAND";
        public static final String LIST_BRAND = "LIST_BRAND";
        public static final String EDIT_BRAND = "EDIT_BRAND";

        public static final String MANAGE_COLOR = "MANAGE_COLOR";
        public static final String ADD_COLOR = "ADD_COLOR";
        public static final String EDIT_COLOR = "EDIT_COLOR";
        public static final String DEL_COLOR = "DEL_COLOR";
        public static final String VIEW_COLOR = "VIEW_COLOR";
        public static final String LIST_COLOR = "LIST_COLOR";

        public static final String ADD_CATEGORY = "ADD_CATEGORY";
        public static final String EDIT_CATEGORY = "EDIT_CATEGORY";
        public static final String DEL_CATEGORY = "DEL_CATEGORY";
        public static final String LIST_CATEGORY = "LIST_CATEGORY";
        public static final String VIEW_CATEGORY = "VIEW_CATEGORY";

        public static final String ADD_PROPERTY = "ADD_PROPERTY";
        public static final String EDIT_PROPERTY = "EDIT_PROPERTY";
        public static final String DEL_PROPERTY = "DEL_PROPERTY";
        public static final String EDIT_LOCKED_PROPERTY = "EDIT_LOCKED_PROPERTY";
        public static final String LIST_PROPERTY = "LIST_PROPERTY";
        public static final String VIEW_PROPERTY = "VIEW_PROPERTY";

        public static final String ADD_NEWS = "ADD_NEWS";
        public static final String EDIT_NEWS = "EDIT_NEWS";
        public static final String DEL_NEWS = "DEL_NEWS";
        public static final String LIST_NEWS = "LIST_NEWS";
        public static final String VIEW_NEWS = "VIEW_NEWS";

        public static final String ADD_NEWS_CATEGORY = "ADD_NEWS_CATEGORY";
        public static final String EDIT_NEWS_CATEGORY = "EDIT_NEWS_CATEGORY";
        public static final String DEL_NEWS_CATEGORY = "DEL_NEWS_CATEGORY";
        public static final String LIST_NEWS_CATEGORY = "LIST_NEWS_CATEGORY";
        public static final String VIEW_NEWS_CATEGORY = "VIEW_NEWS_CATEGORY";


    }

    public static class FileNames {
        public static final String BRANCH_STORE_BIG = "branch_store_big.jpg";
        public static final String BRAND_SMALL = "brand_small.jpg";

        public static final String BIG_PRODUCT_PHOTO_FILE = "photo_big.jpg";
        public static final String MEDIUM_PRODUCT_PHOTO_FILE = "photo_medium.jpg";
        public static final String SMALL_PRODUCT_PHOTO_FILE = "photo_small.jpg";
        public static final String XSMALL_PRODUCT_PHOTO_FILE = "photo_xsmall.jpg";
        public static final String PRODUCT_PHOTO_FOR_SET = "for_set.png";
    }

    public static class AppCacheKeys extends org.room13.mallcore.Constants.AppCacheKeys {
        public static final String PROMO_PRODUCTS = "PROMO_PRODUCTS";
        public static final String TOP_PRODUCTS = "TOP_PRODUCTS";
        public static final String TOP_SHOPS = "TOP_SHOPS";
        public static final String SELLING_SHOPS = "TOP_SHOPS";
        public static final String TOP_BRANDS = "TOP_BRANDS";
        public static final String PRODS_IN_CAT_COUNT = "PRODS_IN_CAT_COUNT";
        public static final String PRODUCTS = "PRODUCTS";
        public static final String PROPS_FOR_SEARCH = "PROPS_FOR_SEARCH";
        public static final String ALL_READY_VALS = "ALL_READY_VALS";
        public static final String ORDER_P_COUNT = "ORDER_P_COUNT";
        public static final String ORDER_P_PRICE = "ORDER_P_PRICE";
        public static final String COLORS_BY_CODE = "COLORS_BY_CODE";
        public static final String SHOPS = "SHOPS";
        public static final String CATEGORIES_BY_NAME = "CATEGORIES_BY_NAME";
        public static final String CATEGORIES = "CATEGORIES";
        public static final String FILTER_SET_VALS = "FILTER_SET_VALS";
        public static final String FILTER_SET_ALL_VALS = "FILTER_SET_ALL_VALS";
    }
}
