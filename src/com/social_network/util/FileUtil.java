package com.social_network.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static <T> void writeData(String path, List<T> data) {
        File file = new File(path);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                System.err.println("Không thể tạo thư mục: " + parentDir.getAbsolutePath());
                return;
            }
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi dữ liệu vào file " + path + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> readData(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<T>) obj;
            } else {
                System.err.println("Lỗi khi đọc dữ liệu từ file " + path + ": Định dạng dữ liệu không mong muốn.");
                return new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đọc dữ liệu từ file " + path + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}