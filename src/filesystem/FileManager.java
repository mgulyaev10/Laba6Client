package filesystem;

import org.json.JSONArray;
import shared.Troll;

import java.io.*;
import java.util.Date;
import java.util.Deque;
import java.util.Scanner;

/**
 * Класс для работы с файлами, хранящих информацию о коллекциях.
 */
public class FileManager {

    private String filePath;
    private String initDate;

    /**
     * Конструктор - создаёт менеджер файла с коллекцией.
     *
     * @param path путь к файлу с коллекцией
     */
    public FileManager(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("Файл с коллекцией не найден.");
        }
        filePath = path;
        initDate = new Date().toString();
    }

    /**
     * Метод, проверяющий, существует ли файл
     *
     * @return true, если существует, false, ксли не существует
     */
    public boolean isDefaultFileExists() {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * Записывает коллекцию, представленную в формате JSON, в файл
     *
     * @param json json строка с коллекцией.
     * @return true, если коллекция была сохранена в файл успешно, иначе - false
     */
    public boolean writeCollectionJSON(String json) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            System.err.println("Ошибка записи коллекции!");
            return false;
        }
        return true;
    }


    /**
     * Записывает коллекцию в файл
     *
     * @param collection коллекция для сохранения в файл
     * @param filePath   файл, в который нужно сохранить коллекцию
     * @return true, если коллекция была сохранена в файл успешно, иначе - false
     */
    public boolean writeCollection(Deque<Troll> collection, String filePath) {
        JSONArray array = new JSONArray();
        collection.forEach(o -> array.put(o.getJSON()));

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(array.toString());
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл коллекции!");
            return false;
        }
        return true;
    }

    /**
     * Записывает коллекцию в файл
     *
     * @param collection коллекция
     * @return true, если коллекция была сохранена в файл успешно, иначе - false
     */
    public boolean writeCollection(Deque<Troll> collection) {
        JSONArray array = new JSONArray();
        collection.forEach(o -> array.put(o.getJSON()));

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(array.toString());
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл коллекции!");
            return false;
        }
        return true;
    }

    /**
     * Меняет путь к файлу с коллекцией
     *
     * @param path путь к новому файлу с коллекцией
     */
    public void setDefaultCollectionFilePath(String path) {
        filePath = path;
    }

    public String getInitDate() {
        return initDate;
    }

    /**
     * Считывает содержимое файла
     *
     * @return строка JSON с информацией о коллекции
     */
    public String getJsonFromFile() {
        FileReader reader = null;
        try {
            reader = new FileReader(filePath);
            Scanner scanner = new Scanner(reader);
            String content = "";
            while (scanner.hasNextLine()) {
                content += scanner.nextLine();
            }

            reader.close();

            return content;
        } catch (FileNotFoundException e) {
            System.err.println("Файл с коллекцией не найден.");
        } catch (IOException e) {
            System.err.println("Ошибка закрытия файла с коллекцией.");
        }
        return null;
    }


    /**
     * Считывает содержимое файла
     *
     * @param mFilePath путь к файлу с коллекцией
     * @return строка JSON с информацией о коллекции
     */
    public String getJsonFromFile(String mFilePath) {
        FileReader reader = null;
        try {
            reader = new FileReader(mFilePath);
            Scanner scanner = new Scanner(reader);
            String content = "";
            while (scanner.hasNextLine()) {
                content += scanner.nextLine();
            }

            reader.close();

            return content;
        } catch (FileNotFoundException e) {
            System.err.println("Файл с коллекцией не найден.");
        } catch (IOException e) {
            System.err.println("Ошибка закрытия файла с коллекцией.");
        }

        return null;
    }
}

