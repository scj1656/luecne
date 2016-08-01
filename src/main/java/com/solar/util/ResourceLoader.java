package com.solar.util;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceLoader {

    private static final Logger logger = LoggerFactory.getLogger(ResourceLoader.class);

    public static File getResource(String path) {
        String absolutePath = ClassLoaderUtil.getAbsolutePath(path);
        File file = new File(absolutePath);
        if (file.exists()) {
            logger.debug("Load " + absolutePath + " resource success");
        } else {
            logger.debug("Load " + absolutePath + " resource success");
        }
        return file;
    }
}
