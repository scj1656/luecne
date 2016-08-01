package com.solar.common.config;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.util.ResourceLoader;

public class SolarConfig {

    private static final Logger     logger       = LoggerFactory.getLogger(SolarConfig.class);

    private Document                document;

    private String                  name;

    private String                  version;

    private String                  indexDir;

    private String                  type;

    private String                  openMode;

    private static final String     DEFAULT_PATH = "solar.xml";

    public static final SolarConfig DEFAULT      = new SolarConfig(DEFAULT_PATH);

    public SolarConfig(String path) {
        if (path == null) {
            this.document = new DefaultDocument();
            return;
        }
        File file = ResourceLoader.getResource(path);
        SAXReader reader = new SAXReader();
        try {
            this.document = reader.read(file);
        } catch (DocumentException e) {
            logger.error("SAX reader can't read file , please check it ", e);
        }

        this.name = document.selectSingleNode("solar/core/name").getText();
        this.version = document.selectSingleNode("solar/core/version").getText();
        this.indexDir = document.selectSingleNode("solar/code/index/dir").getText();
        this.type = document.selectSingleNode("solar/code/index/type").getText();
        this.openMode = document.selectSingleNode("solar/code/index/openmode").getText();
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIndexDir() {
        return indexDir;
    }

    public void setIndexDir(String indexDir) {
        this.indexDir = indexDir;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpenMode() {
        return openMode;
    }

    public void setOpenMode(String openMode) {
        this.openMode = openMode;
    }

}
