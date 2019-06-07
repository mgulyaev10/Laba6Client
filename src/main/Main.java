package main;

import collection.CollectionManager;
import filesystem.FileManager;
import network.TransferPackage;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

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
        System.out.println("Введите help, чтобы увидеть список доступных команд.");
        while (true) {
            try {
                TransferPackage tpkg;
                if (line.length() == 0) {
                    try {
                        String input;
                        line = scanner.nextLine();
                        input = line.split(" ")[0];

                        /// Блок кода разрешающий выполнение упомянутых в блоке комманд если файл не существует
                        if (!manager.isDefaultFileExists()) {
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

                    if (line.trim().equals("load"))
                        tpkg = new TransferPackage(666, line,
                                null, manager.getJsonFromFile().getBytes(Main.DEFAULT_CHAR_SET));
                    else
                        tpkg = new TransferPackage(666, line, null);
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
                if (previousCmdId != 6) line = "";
                buff.clear();
                buff = ByteBuffer.wrap(tpkg.getBytes());
                datagramChannel.send(buff, new InetSocketAddress(IPAddress, port));

                buff.clear();
                selector.select(3000);
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
                            System.out.println();
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
                    }
                } else {
                    throw new SocketTimeoutException();
                }
                line = "";

            } catch (SocketTimeoutException e) {
                if (isConnected)
                    System.out.println("Соединение с сервером было внезапно разорвано! Попытка соединения.");
                isConnected = false;
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }


}




