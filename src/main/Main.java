package main;

import collection.CollectionManager;
import filesystem.FileManager;
import network.TransferPackage;
import network.User;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static final String DEFAULT_CHAR_SET = "UTF-8";

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Введите адрес и порт в соответствующем порядке!");
            System.exit(0);
        }
        if (args.length == 1) {
            System.out.println("Введите порт!");
            System.exit(0);
        }

        DatagramSocket clientSocket = new DatagramSocket();
        clientSocket.setSoTimeout(2000);

        InetAddress IPAddress = InetAddress.getByName(args[0]);

        int port = Integer.parseInt(args[1]);

        Scanner scanner = new Scanner(System.in);
        FileManager manager = new FileManager("Trolls.json");

        boolean isConnected = true;

        String line = "";

        int previousCmdId = 0;

        Selector selector = Selector.open();
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);
        SocketAddress address = new InetSocketAddress(IPAddress, port + new Random().nextInt(1000));
        datagramChannel.socket().bind(address);

        datagramChannel.register(selector, SelectionKey.OP_READ);

        ByteBuffer buff = ByteBuffer.allocate(65536);

        boolean isLogged = false;
        User user = null;
        while (true) {
            try {
                TransferPackage tpkg= null;
                if (line.length() == 0) {
                    try {
                        String input;
                        //Проверяем, авторизирован ли пользователь.
                        if (isLogged) {
                            line = scanner.nextLine();
                            input = line.split(" ")[0];

                        } else{
                            System.out.println("Для регистрации введите \"signup\", для авторизации \"signin\"");
                            input = scanner.nextLine().trim();
                            if (input.equals("signin")){
                                System.out.print("Введите логин: ");
                                String login = scanner.nextLine().trim();
                                System.out.print("Введите пароль: ");
                                String password = scanner.nextLine();
                                System.out.println();
                                user = new User(login,password);
                                line = "login";
                                tpkg = new TransferPackage(110, "login {" + user.getLogin() + "} {" + user.getPassword() + "}", null);
                            } else if (input.equals("signup")){
                                System.out.print("Введите логин: ");
                                String login = scanner.nextLine().trim();
                                System.out.print("Введите пароль: ");
                                String password = scanner.nextLine();
                                System.out.print("Введите e-mail: ");
                                String email = scanner.nextLine();
                                if(email.matches("^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*(?:aero|arpa|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|[a-z][a-z])$")){
                                    user = new User(login,password,email);
                                }
                                else{
                                    System.out.println("Ошибка: некорректный email! Попробуйте еще раз");
                                    line = "";
                                    continue;
                                }

                                line = "login";
                                tpkg = new TransferPackage(110, "login {" + user.getLogin() + "} {" + user.getPassword() + "} {" + user.getEmail() + "}", null);
                            } else{
                                System.out.println("Для работы с программой Вам необходимо зарегистрироваться или авторизироваться.");
                                line = "";
                                continue;
                            }
                        }

                        /// Блок кода разрешающий выполнение упомянутых в блоке комманд если файл не существует
                        if (!manager.isDefaultFileExists() && isLogged) {
                            switch (input) {
                                case "help":
                                case "change_def_file_path":
                                    continue;
                                default:
                                    line = "";
                                    System.out.println("Файл с коллекцией не найден!");
                                    continue;

                            }
                        }

                    } catch (NoSuchElementException e) {
                        System.err.println("Завершение работы программы.");
                        System.exit(0);
                    }

                    if (isLogged) {
                        if (line.trim().equals("load"))
                            tpkg = new TransferPackage(666, line,
                                    null, manager.getJsonFromFile().getBytes(Main.DEFAULT_CHAR_SET));
                        else
                            tpkg = new TransferPackage(666, line, null);
                    }
                } else {
                    if (previousCmdId == 6) {
                        byte[] bytes;
                        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                             ObjectOutputStream dos = new ObjectOutputStream(baos)) {
                            dos.writeObject(CollectionManager.getCollectionFromJson(manager.getJsonFromFile(line)));
                            bytes = baos.toByteArray();
                        }
                        line = "SET_PATH_IMPORT";
                        tpkg = new TransferPackage(666, line,
                                null, bytes);
                    } else {
                        if (line.trim().equals("load"))
                            tpkg = new TransferPackage(666, line,
                                    null, manager.getJsonFromFile().getBytes(Main.DEFAULT_CHAR_SET));
                        else
                            tpkg = new TransferPackage(666, line, null);
                    }
                }

                if (user!=null) {
                    if (user.getLogin().length() == 0 || findMatches("[\\?\\\\!/\\^\\%\\$\\;\\#\\'\\(\\)\\+\\=\\`\\~]", line).size() != 0) {
                        System.out.println("Неправильный ввод логина!");
                        line = "";
                        continue;
                    }
                    if (user.getPassword().length() < 8 || findMatches("[\\?\\\\!/\\^\\%\\$\\;\\#\\'\\(\\)\\+\\=\\`\\~]", line).size() != 0) {
                        System.out.println("Пароль должен быть не менее 8-ми символов и не содержать специальных символов!");
                        line = "";
                        continue;
                    }
                }

                if (user!=null){
                    tpkg.setUser(user);
                }

                //if (previousCmdId != 6) line = "";
                buff.clear();
                buff = ByteBuffer.wrap(tpkg.getBytes());
                datagramChannel.send(buff, new InetSocketAddress(IPAddress, port));

                buff.clear();
                selector.select(5000);
                ByteBuffer received = ByteBuffer.allocate(65536);

                datagramChannel.receive(received);

                TransferPackage receivedPkg = TransferPackage.restoreObject(new ByteArrayInputStream(received.array()));

                if (receivedPkg != null) {
                    if (!isConnected)
                        System.out.println("Соединение восстановлено!");
                    isConnected = true;
                    switch (receivedPkg.getId()) {
                        case 11:
                        case 2:
                        case 4:
                        case 5:
                            previousCmdId = receivedPkg.getId();
                            System.out.println(receivedPkg.getCmdData());
                            System.out.println(new String(receivedPkg.getAdditionalData(), Main.DEFAULT_CHAR_SET));
                            break;
                        case 9:
                            previousCmdId = receivedPkg.getId();
                            System.out.println(receivedPkg.getCmdData());
                            System.out.println("Завершение работы программы.");
                            System.exit(0);
                            break;
                        case 7:
                        case 3:
                        case 1:
                        case 601:
                            previousCmdId = receivedPkg.getId();
                            System.out.println(receivedPkg.getCmdData());
                            break;
                        case 6:
                            previousCmdId = receivedPkg.getId();
                            line = new String(receivedPkg.getAdditionalData(), Main.DEFAULT_CHAR_SET);
                            System.out.println(line);
                            if (new File(new String(receivedPkg.getAdditionalData(), Main.DEFAULT_CHAR_SET)).exists())
                                continue;
                            else
                                break;
                        case -1:
                            previousCmdId = receivedPkg.getId();
                            System.out.println("Ошибка: ");
                            System.out.print(receivedPkg.getCmdData());
                            if (receivedPkg.getAdditionalData() != null) {
                                System.out.print(new String(receivedPkg.getAdditionalData(), Main.DEFAULT_CHAR_SET));
                            }
                            break;
                        case 10:
                            previousCmdId = receivedPkg.getId();
                            System.out.println(receivedPkg.getCmdData());
                            String filePath = new String(receivedPkg.getAdditionalData(), Main.DEFAULT_CHAR_SET);
                            manager.setDefaultCollectionFilePath(filePath);
                            break;
                        case 101:
                            TransferPackage transferPackage = new TransferPackage(666, null, null);
                            byte[] bytes = transferPackage.getBytes();
                            clientSocket.send(new DatagramPacket(bytes, bytes.length, IPAddress, port));
                            System.out.println("Соединение с сервером восстановлено!");
                            break;
                        case 12:
                            manager.writeCollection(CollectionManager.getCollectionFromBytes(receivedPkg.getAdditionalData()));
                            System.out.println(receivedPkg.getCmdData());
                        case 110:
                            if(receivedPkg.getAdditionalData()[0] == 1){
                                isLogged = true;
                                System.out.println("Вы успешно зарегистрированы!");
                            }
                            if(receivedPkg.getAdditionalData()[0] == 2){
                                isLogged = true;
                                System.out.println("Вы успешно авторизированы!");
                            }
                            break;
                    }
                    line = "";
                } else {
                    throw new SocketTimeoutException();
                }

            } catch (SocketTimeoutException e) {
                if (isConnected)
                    System.out.println("Соединение с сервером было внезапно разорвано! Попытка соединения.") ;
                isConnected = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static List<String> findMatches(String patterStr, String text){
        Pattern pattern = Pattern.compile(patterStr);
        Matcher matcher = pattern.matcher(text);
        List<String> collection = new ArrayList<>();
        while(matcher.find()){
            collection.add(text.substring(matcher.start(), matcher.end()));
        }
        return collection;
    }
}




