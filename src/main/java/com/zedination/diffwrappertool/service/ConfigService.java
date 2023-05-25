package com.zedination.diffwrappertool.service;

import com.zedination.diffwrappertool.constant.Constant;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.prefs.Preferences;

public class ConfigService {
    private ConfigService() {
    }

    public static ConfigService getInstance() {
        return ConfigService.SingletonHelper.INSTANCE;
    }

    private static class SingletonHelper {
        private static final ConfigService INSTANCE = new ConfigService();

    }

    public void registerWindowsContextMenu() {
//        // Tạo registry key cho ứng dụng JavaFX
//        String keyPath = "Software\\Classes\\Directory\\shell\\Diff Wrapper Tool";
//        Preferences preferences = Preferences.userRoot().node(keyPath);
//        // Thiết lập giá trị cho key
//        preferences.put("MUIVerb", "Open with Diff Wrapper Tool");
//        preferences.put("Icon", "C:\\Users\\Admin\\AppData\\Local\\Diff Wrapper\\diff-wrapper-tool.png");
//
//        // Tạo registry key cho command của ứng dụng
//        Preferences commandPreferences = preferences.node("command");
//        commandPreferences.put("", "\"C:\\Program Files\\Diff Wrapper Tool\\Diff Wrapper Tool.exe\" \"%V\"");
        try {
            if (!"1".equals(readConfig("registry"))) {
                // Đường dẫn và tên key
                String keyPath = "HKCU\\Software\\Classes\\Directory\\shell\\Diff Wrapper Tool";

                // Thiết lập các giá trị
                String muiVerb = "Open with Diff Wrapper Tool";
                String iconPath = "C:\\Users\\Admin\\AppData\\Local\\Diff Wrapper\\" + Constant.icon;

                // Tạo lệnh reg add context menu folder
                String[] command1 = {"reg", "add", keyPath, "/ve", "/d", muiVerb, "/f"};
                String[] command2 = {"reg", "add", keyPath, "/v", "Icon", "/d", iconPath, "/f"};
//            String[] command3 = {"reg", "add", keyPath + "\\command", "/ve", "/d", executablePath + " \"%V\"", "/f"};
                String[] command3 = {"cmd.exe", "/c", "reg add \"HKEY_CURRENT_USER\\Software\\Classes\\Directory\\shell\\Diff Wrapper Tool\\command\" /ve /d \"\\\"C:\\Program Files\\Diff Wrapper Tool\\Diff Wrapper Tool.exe\\\" \\\"%V\\\"\" /f"};

                // Tạo lệnh reg add context menu folder (background)
                String[] command4 = {"reg", "add", "HKCU\\Software\\Classes\\Directory\\Background\\shell\\Diff Wrapper Tool", "/ve", "/d", muiVerb, "/f"};
                String[] command5 = {"reg", "add", "HKCU\\Software\\Classes\\Directory\\Background\\shell\\Diff Wrapper Tool", "/v", "Icon", "/d", iconPath, "/f"};
                String[] command6 = {"cmd.exe", "/c", "reg add \"HKEY_CURRENT_USER\\Software\\Classes\\Directory\\Background\\shell\\Diff Wrapper Tool\\command\" /ve /d \"\\\"C:\\Program Files\\Diff Wrapper Tool\\Diff Wrapper Tool.exe\\\" \\\"%V\\\"\" /f"};

                // Thực thi lệnh
                ProcessBuilder pb1 = new ProcessBuilder(command1);
                ProcessBuilder pb2 = new ProcessBuilder(command2);
                ProcessBuilder pb3 = new ProcessBuilder(command3);

                ProcessBuilder pb4 = new ProcessBuilder(command4);
                ProcessBuilder pb5 = new ProcessBuilder(command5);
                ProcessBuilder pb6 = new ProcessBuilder(command6);

                pb1.start();
                pb2.start();
                pb3.start();

                pb4.start();
                pb5.start();
                pb6.start();

                System.out.println("Đã thêm mục vào context menu của Windows Explorer.");
                saveConfig("registry", "1");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
