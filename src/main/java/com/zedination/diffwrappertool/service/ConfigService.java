package com.zedination.diffwrappertool.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    public void copyBundleHtmlDiff() {
        String zipFilePath = "/bundle.zip";
        String userPath = System.getProperty("user.home");
        String destinationFolderPath = userPath + "/AppData/Local/Diff Wrapper";
        try {
            if (!new File(destinationFolderPath + "/bundle.zip").exists()) {
                // Đọc tệp tin zip từ resources
                InputStream inputStream = getClass().getResourceAsStream(zipFilePath);

                // Tạo thư mục đích nếu nó không tồn tại
                Files.createDirectories(Paths.get(destinationFolderPath));

                // Sao chép tệp tin zip vào thư mục đích
                Path destinationPath = Paths.get(destinationFolderPath + zipFilePath);
                Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);

                // Giải nén tệp tin zip
                unzip(destinationPath.toString(), destinationFolderPath);

                System.out.println("Sao chép và giải nén tệp tin thành công!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void unzip(String zipFilePath, String destinationFolderPath) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                String filePath = destinationFolderPath + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    // Tạo thư mục cha nếu nó không tồn tại
                    new File(new File(filePath).getParent()).mkdirs();

                    // Ghi tệp tin từ zip vào đích
                    FileOutputStream outputStream = new FileOutputStream(filePath);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zipInputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    outputStream.close();
                }
                zipInputStream.closeEntry();
                entry = zipInputStream.getNextEntry();
            }
        }
    }

}
