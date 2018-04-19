package com.mmall.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties props;

    static {
        String fileName = "mmall.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(Properties.class.getClassLoader().getResourceAsStream(fileName)));
        } catch (IOException e) {
            logger.error("配置文件读取异常",e);
        }

    }

    public static String getProperty(String key) {
        String value = props.getProperty(key.trim());
        if (value == null){
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key,String defaultValue) {
        String value = props.getProperty(key, defaultValue);
        if (value == null) {
            return defaultValue;
        }
        return value.trim();
    }
}
