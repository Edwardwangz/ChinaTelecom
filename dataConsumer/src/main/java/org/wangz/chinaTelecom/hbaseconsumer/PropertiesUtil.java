package org.wangz.chinaTelecom.hbaseconsumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static Properties properties;
    static{
        try {
            InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream("kafka.properties");
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //返回成员变量，Properties集合
    public static Properties getProperties() {
        return properties;
    }

    //返回具体的某一个值
    public static String getProp(String key){
        return properties.getProperty(key);
    }

}
