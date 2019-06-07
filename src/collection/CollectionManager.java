package collection;

import org.json.JSONArray;
import org.json.JSONObject;
import shared.Troll;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

/**
 * Класс, с помощью которого организуется управление коллекцией
 */
public class CollectionManager {
    /**
     * Метод, получающий коллекцию из строки в формате json
     *
     * @return Queue\<Troll\>
     */
    public static Queue<Troll> getCollectionFromJson(String json) {
        Deque<Troll> trolls = new ArrayDeque<>();
        if (json.length() == 0) {
            System.err.println("Файл с коллекцией пуст.");
        } else {
            JSONArray array = new JSONArray(json);
            array.forEach(o -> trolls.add(new Troll((JSONObject) o)));
        }
        return trolls;
    }

    /**
     * Метод, который десереализует коллекцию.
     *
     * @param bytes массив байтов
     * @return Deque\<Troll\> коллекция троллей.
     */
    @SuppressWarnings("unchecked")
    public static Deque<Troll> getCollectionFromBytes(byte[] bytes) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Deque<Troll>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка: не удалось сериализовать коллекцию");
        }
        return null;
    }

}

