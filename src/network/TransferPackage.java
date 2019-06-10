package network;

import shared.Troll;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс, описывающий сообщение, которое отправляется и приходит с сервера
 */
public class TransferPackage implements Serializable {

    private int id;
    private String cmdData;
    private transient Stream<Troll> data;
    private byte[] additionalData;
    private User mUser;

    public TransferPackage(int id, String cmdData, Stream<Troll> data) {
        this.id = id;
        this.cmdData = cmdData;
        this.data = data;
    }

    public TransferPackage(int id, String cmdData, Stream<Troll> data, byte[] additionalData) {
        this.id = id;
        this.cmdData = cmdData;
        this.data = data;
        this.additionalData = additionalData;
    }

    public static TransferPackage restoreObject(InputStream inputStream) {
        try (ObjectInputStream ois = new ObjectInputStream(inputStream)) {
            Object obj = ois.readObject();
            return (TransferPackage) obj;

        } catch (Exception e) {
            System.err.println("Подождите воссоединения с сервером.");
        }

        return null;
    }

    public byte[] getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(byte[] additionalData) {
        this.additionalData = additionalData;
    }

    public String getCmdData() {
        return cmdData;
    }

    public Stream<Troll> getData() {
        return data;
    }

    public void setData(Stream<Troll> data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (getData() != null) {
            ArrayList<Troll> list = new ArrayList<>();
            getData().sequential().collect(Collectors.toCollection(() -> list));
            out.writeObject(list);
        } else
            out.writeObject(null);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        Object obj = in.readObject();
        ArrayList<Troll> list;
        if (obj != null) {
            list = (ArrayList<Troll>) obj;
            this.data = list.stream();
        } else {
            this.data = null;
        }
    }

    public byte[] getBytes() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
            bos.close();
            return bos.toByteArray();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}
