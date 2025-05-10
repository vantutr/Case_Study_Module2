package util;

import java.io.*;
import java.util.*;

public class FileUtil {
    public static <T> void writeData(String path, List<T> data) {
        try {
            File file = new File(path);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(data);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> List<T> readData(String path) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
            return (List<T>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
