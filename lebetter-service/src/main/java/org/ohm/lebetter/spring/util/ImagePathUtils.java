package org.ohm.lebetter.spring.util;

import org.room13.mallcore.log.RMLogger;

import java.io.File;

public final class ImagePathUtils {
    private static final RMLogger log = new RMLogger(ImagePathUtils.class);

    public static String url2path(String url, String ftpUrl, String ftpDir) {
        ftpUrl = ftpUrl.endsWith("/") ?
                ftpUrl : ftpUrl + "/";

        if (!url.startsWith(ftpUrl)) {
            log.debug("Image is not on kindershopping server. Download before processing. \n" +
                    "Ftp url is " + ftpUrl + " and image url is " + url);
            // Если изображение лежит не у нас, пропускаем
            return null;
        }

        String path = url.substring(ftpUrl.length());

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        ftpDir = ftpDir.endsWith(File.separator) ?
                ftpDir : ftpDir + File.separator;

        return ftpDir + path.replaceAll("\\/", File.separator);
    }

}
