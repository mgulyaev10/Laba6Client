package collection;

import collection.Startable;
import main.Main;
import network.TransferPackage;
import org.json.JSONException;
import org.json.JSONObject;
import shared.Troll;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс, предназначенный для парсинга и выполнения команд.
 */
public enum Command {

    /**
     * Удаляет элемент коллекции, соответствующий json
     */
    @SuppressWarnings("unchecked")
    REMOVE((command, transferPackage, data) -> {
        try {
            StringBuilder builder = new StringBuilder();
            for (Object s : data.toArray()) builder.append(s.toString());
            String strData = builder.toString();
            JSONObject jsonObject = new JSONObject(strData);
            Troll Troll = new Troll(jsonObject);

            ArrayDeque<Troll> collection = new ArrayDeque<>();
            transferPackage.getData().sequential().collect(Collectors.toCollection(() -> collection));

            collection.remove(Troll);

            command.setData(Stream.of(new TransferPackage(1, "Команда выполнена.", collection.stream())));

            System.out.println("Команда выполнена.");
        } catch (JSONException e) {
            System.err.println("Ошибка: json-объект введён неверно.");
        }
    }),

    /**
     * Удаляет все элементы коллекции, меньшие введённого.
     */
    REMOVE_LOWER((command, transferPackage, data) -> {
        try {
            ArrayDeque<Troll> collection = new ArrayDeque<>();

            transferPackage.getData().sequential().collect(Collectors.toCollection(() -> collection));

            command.setData(Stream.of(new TransferPackage(10, "Команда выполнена.", collection.stream())));

            System.out.println("Команда выполнена.");
        } catch (JSONException e) {
            System.err.println("Ошибка: json-объект введён неверно.");
        }
    }),

    /**
     * Выводит на экран текущую коллекцию
     */
    SHOW((command, transferPackage, data) -> {
        ArrayDeque<Troll> collection = new ArrayDeque<>();
        transferPackage.getData().sequential().collect(Collectors.toCollection(() -> collection));
        StringBuilder builder = new StringBuilder();

        for (Troll p : collection) {
            builder.append(p.toString()).append("\t");
        }

        String output = builder.toString();

        command.setData(Stream.of(new TransferPackage(2, "Команда выполнена.", null, output.getBytes(Main.DEFAULT_CHAR_SET))));
        System.out.println("Команда выполнена.");
    }),

    /**
     * Очищает коллекцию.
     */
    CLEAR((command, transferPackage, data) -> {
        //
        System.out.println("Команда выполнена.");
    }),

    /**
     * Загружает коллекцию из файла на сервер.
     */
    LOAD((command, manager, data) -> {
        data = Stream.of(new TransferPackage(4, "Команда выполнена.", null, "Load collection to server".getBytes(Main.DEFAULT_CHAR_SET)));
        System.out.println("Команда выполнена.");
    }),

    /**
     * Выводит информацию о коллекции.
     */
    INFO((command, transferPackage, data) -> {
        ByteArrayOutputStream byteObject = new ByteArrayOutputStream();
        ArrayDeque<Troll> collection = new ArrayDeque<>();

        transferPackage.getData().sequential().collect(Collectors.toCollection(() -> collection));

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteObject);
            objectOutputStream.writeObject(data);
            objectOutputStream.flush();
            objectOutputStream.close();
            byteObject.close();
            data = Stream.of(new TransferPackage(5, "Команда выполнена.", null,
                    String.format(
                            "Тип коллекции: %s \nТип элементов коллекции: %s\nДата инициализации: %s\nКоличество элементов: %s\nРазмер: %s байт\n",
                            collection.getClass().getName(),
                            "Clothes.Troll", new Date().toString(), collection.size(), byteObject.toByteArray().length
                    ).getBytes(Main.DEFAULT_CHAR_SET)));

        } catch (IOException e) {
            data = Stream.of(new TransferPackage(-1, "Команда выполнена.", null,
                    "Ошибка при определении размера памяти коллекции.".getBytes(Main.DEFAULT_CHAR_SET)));
        }
        System.out.println("Команда выполнена.");
    }),

    /**
     * Дополняет коллекцию объектами, лежащими в файле, указанном в аргументе команды.
     */
    IMPORT((command, transferPackage, data) -> {
        String path = "";
        for (Object s : data.toArray()) path += s.toString();
        data = Stream.of(new TransferPackage(601, "Команда выполнена.", null, path.getBytes(Main.DEFAULT_CHAR_SET)));
    }),

    /**
     * Эта команда вызывается, когда сервер запрашивает содержимое файла, указанного в аргументе команды IMPORT.
     */
    SET_PATH_IMPORT((command, transferPackage, data) -> {
        Deque<Troll> collection = new ArrayDeque<>();
        transferPackage.getData().sequential().collect(Collectors.toCollection(() -> collection));
        data = Stream.of(new TransferPackage(-1, "Команда выполнена.", collection.stream()));
    }),

    /**
     * Добавляет в коллекцию объект, заданный в формате json
     */
    ADD((command, transferPackage, data) -> {
        try {
            StringBuilder builder = new StringBuilder();
            for (Object s : data.toArray()) builder.append(s.toString());
            String strData = builder.toString();
            JSONObject jsonObject = new JSONObject(strData);

            Troll troll = new Troll(jsonObject);

            ArrayDeque<Troll> collection = new ArrayDeque<>();
            transferPackage.getData().sequential().collect(Collectors.toCollection(() -> collection));
            collection.add(troll);

            data = Stream.of(new TransferPackage(7, "Команда выполнена.", collection.stream()));
            System.out.println("Команда выполнена.");
        } catch (JSONException e) {
            data = Stream.of(new TransferPackage(-1, "Команда выполнена.", null, "Ошибка: json-объект введён неверно.".getBytes(Main.DEFAULT_CHAR_SET)));
        }
    }),

    /**
     * Завершает выполнение программы.
     */
    EXIT((command, transferPackage, data) -> {
        command.setData(Stream.of(new TransferPackage(9, "Команда выполнена.", null, "null".getBytes(Main.DEFAULT_CHAR_SET))));
        System.out.println("Команда выполнена.");
    }),

    /**
     * Выводит список доступных команд.
     */
    HELP((command, transferPackage, data) -> {
        command.setData(Stream.of(new TransferPackage(10, "Команда выполнена.", null,
                ("\"remove_last\": удалить последний элемент из коллекции\n" +
                        "\"remove {element}\": удалить элемент из коллекции по его значению\n" +
                        "\"clear\": очистить коллекцию\n" +
                        "\"info\": вывести в стандартный поток вывода информацию о коллекции " +
                        "(тип, дата инициализации, количество элементов и т.д.)\n" +
                        "\"remove_lower {element}\": удалить из коллекции все элементы, меньшие, чем заданный\n" +
                        "\"add {element}\": добавить новый элемент в коллекцию\n" +
                        "\"show\": вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                        "\"help\": получить информацию о доступных командах\n" +
                        "\"exit\": выйти из программы\n" +
                        "\"import {path}\": добавить к коллекции объекты из файла\n" +
                        "\"load\": загрузить в коллекцию объекты из файла с перезаписью\n" +
                        "\"save\": сохранить объекты коллекции в файл\n" +
                        "Пример корректного JSON-объекта:\n" +
                        "{\"isSad\":true,\"name\":\"Петя\",\"HP\":100,\"things\":[{\"condition\":\"Solid\"," +
                        "\"name\":\"Соль\",\"weight\":20}],\"isSit\":true,\"age\":10}\n\n"
                ).getBytes())));
    }),

    /**
     * Меняет пусть к файлу с коллекцией
     */
    CHANGE_DEF_FILE_PATH(((command, transferPackage, data) -> {
        String strData = "";
        for (Object s : data.toArray()) strData += s.toString();
        data = Stream.of(new TransferPackage(10, "Команда выполнена.", null, strData.getBytes(Main.DEFAULT_CHAR_SET)));
    }));


    /**
     * Тело выполняемой команды.
     */
    private Startable cmd;
    /**
     * Данные, с которыми оперирует команда.
     */
    private Stream data;

    Command(Startable cmd) {
        this.cmd = cmd;
    }

    private void setData(Stream data) {
        this.data = data;
    }

}
