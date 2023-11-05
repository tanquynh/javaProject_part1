package ra.repository;

import ra.model.Entity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static ra.constant.Contant.FilePath.COMMON_PATH;

public class FileRepo<T extends Entity, ID extends Number> {

    private File file;

    public FileRepo() {
    }

    public FileRepo(String filePath) {
        File dataDir = new File(COMMON_PATH);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        this.file = new File(COMMON_PATH + filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi tạo file");
        }

    }

    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(this.file);
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
            list = (List<T>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
        return list;
    }

    public void saveToFile(List<T> t) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(this.file);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(t);
            outputStream.close();
        } catch (IOException e) {
            System.err.println("Có lỗi xảy ra khi ghi file");
        }
    }

    public void save(T t) {
        List<T> e = findAll();
        int index = -1;

        for (int i = 0; i < e.size(); i++) {
            if (e.get(i).getId().equals(t.getId())) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            e.set(index, t);
        } else {
            e.add(t);
        }
        saveToFile(e);
    }


    public T findById(ID id) {
        List<T> list = findAll();
        for (T t : list) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return  null;
    }

    public int findByIndex(ID id) {
        List<T> list = findAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public int autoInc() {
        List<T> list = findAll();
        int maxId = list.isEmpty() ? 0 : list.get(list.size() - 1).getId().intValue();
        return maxId + 1;
    }


}

