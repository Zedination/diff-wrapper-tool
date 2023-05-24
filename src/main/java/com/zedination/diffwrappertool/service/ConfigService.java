package com.zedination.diffwrappertool.service;

import java.io.*;
import java.util.Properties;

public class ConfigService {
    private ConfigService() {
    }

    public static ConfigService getInstance() {
        return ConfigService.SingletonHelper.INSTANCE;
    }

    private static class SingletonHelper {
        private static final ConfigService INSTANCE = new ConfigService();

    }

    public String readConfig(String configName) {
        try {
            Properties props = new Properties();
            String userPath = System.getProperty("user.home");
            FileInputStream in = new FileInputStream(userPath + "/AppData/Local/Diff Wrapper/config.xml");
            props.loadFromXML(in);
            return props.getProperty(configName);
        } catch (Exception e) {
            return "";
        }
    }

    public void saveConfig(String configName, String configValue) {
        Properties props = new Properties();
        // Set the properties to be saved
        props.setProperty(configName, configValue);

        // Write the file
        try {
            String userPath = System.getProperty("user.home");
            File configFile = new File(userPath + "/AppData/Local/Diff Wrapper/config.xml");
            FileOutputStream out = new FileOutputStream(configFile);
            props.storeToXML(out,"Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
