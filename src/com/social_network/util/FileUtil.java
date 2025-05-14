package com.social_network.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static <T> void writeData(String path, List<T> data) throws IOException {
        if (data == null) {
            throw new IllegalArgumentException("Dữ liệu không được null");
        }

        File file = new File(path);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Không thể tạo thư mục: " + parentDir.getAbsolutePath());
            }
        }

        // Sao lưu file nếu tồn tại
        File backupFile = new File(path + ".bak");
        if (file.exists()) {
            Files.copy(file.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        } catch (IOException e) {
            // Khôi phục từ bản sao lưu nếu ghi thất bại
            if (backupFile.exists()) {
                Files.copy(backupFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            throw new IOException("Lỗi khi ghi dữ liệu vào file " + path + ": " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> readData(String path) throws IOException, ClassNotFoundException {
        File file = new File(path);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<T>) obj;
            } else {
                throw new IOException("Định dạng dữ liệu không mong muốn trong file: " + path);
            }
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
    }
}