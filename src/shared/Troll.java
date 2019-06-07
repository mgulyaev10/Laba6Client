package shared;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Troll extends Essence implements Comparable<Troll>, Serializable {
    private boolean isSit;
    private boolean isSad;
    private List<Thing> thingsInHands = new ArrayList<>();

    /**
     * Конструктор для класса shared.shared.Troll, если HP тролля неизвестно.
     * @param age возраст тролля
     * @param name имя тролля
     */
    public Troll(int age, String name) {
        super(age, name);
        isSit = true;
        isSad = true;
    }

    /**
     * Конструктор для класса shared.shared.Troll, если HP тролля известно.
     * @param age возраст тролля
     * @param name имя тролля
     * @param HP HP тролля
     */
    public Troll(int age, String name, int HP) {
        super(age, name, HP);
        isSit = true;
        isSad = true;
    }

    /**
     * Конструктор для класса shared.shared.Troll, объект задан в формате JSON
     * @param json объект класса shared.shared.Troll, заданный в формате JSON.
     */
    public Troll(JSONObject json) {
        super(json.getInt("age"), json.getString("name"), json.getInt("HP"));
        isSit = json.getBoolean("isSit");
        isSad = json.getBoolean("isSad");
        JSONArray jsonArray = json.getJSONArray("things");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            Thing t = new Thing(object.getString("name"), Thing.Condition.valueOf(object.getString("condition")),
                    object.getInt("weight"));
            thingsInHands.add(t);
        }
    }

    /**
     * Метод, реализующий сортировку по-умолчанию для класса shared.shared.Troll.
     * @param o сравниваемый объект.
     * @return положительное число, если объект this > o , отрицательное, если this<o , 0, если this = o.
     */
    @Override
    public int compareTo(Troll o) {
        if (this.hashCode() == o.hashCode()) {
            return 0;
        } else if (this.thingsInHands.size() > o.thingsInHands.size()) {
            return 1;
        } else if (this.thingsInHands.size() < o.thingsInHands.size()) {
            return -1;
        } else if (this.getAge() > o.getAge()) {
            return 1;
        } else if (this.getAge() < o.getAge()) {
            return -1;
        } else if (this.thingsInHands.size() > o.thingsInHands.size()) {
            return 1;
        } else if (this.thingsInHands.size() < o.thingsInHands.size()) {
            return -1;
        } else
            return 0;
    }


    public void setSit(boolean b) {
        isSit = b;
    }

    public boolean isSit(){
        return isSit;
    }

    /**
     * Метод, позволяющий пополнить HP тролля с помощью объекта shared.shared.Sandwich.
     * @param s объект класса shared.shared.Sandwich, который ест тролль.
     */
    public void eat(Sandwich s) {
        if (getHP() + s.getCalorie() > 100) {
            setHP(100);
        } else {
            setHP(getHP() + s.getCalorie());
        }
        if (getHP() > 75) {
            setHungry(false);
        } else {
            setHungry(true);
        }
    }

    public void setSad(boolean b) {
        isSad = b;
    }

    public boolean isSad() {
        return isSad;
    }

    /**
     * Метод, позволяющий произнести фразу троллю.
     * @param speech фраза, сказанная троллю
     * @return true, если тролль сказал фразу, false, если тролль не может говорить.
     */
    public boolean say(String speech){
        if (isAbleToSpeak()) {
            System.out.println(this.getName() + " говорит: \"" + speech + "\"");
            return true;
        } else{
            return false;
        }
    }

    /**
     * Метод, позволяющий добавить вещь троллю.
     * @param t объект класса shared.shared.Thing, которую берёт тролль.
     */
    public void addThing(Thing t) {
        thingsInHands.add(t);
        System.out.println("Предмет " + t.getName() + " был дан троллю " + this.getName());
    }


    public List<Thing> getThingsInHands() {
        return thingsInHands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Troll temp = (Troll) o;
        if (temp.getAge() != this.getAge())
            return false;
        if (!temp.getName().equals(this.getName()))
            return false;
        if (temp.getHP() != this.getHP())
            return false;
        if (temp.thingsInHands.size() != this.thingsInHands.size())
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int rnd = 31;
        int result = 1;
        result = (rnd * result + getAge()) * rnd + getHP();
        if (isSit) {
            result = result * rnd + 9;
        } else {
            result = result * rnd + 3;
        }
        if (isSad) {
            result = result * rnd + 2;
        } else {
            result = result * rnd + 22;
        }
        if (isHungry()) {
            result = result * rnd + 3;
        } else {
            result = result * rnd + 33;
        }
        result = result * rnd + getName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Troll " + getName() + ". Возраст: " + Integer.toString(getAge()) + ". HP: " + Integer.toString(getHP())
                + ". IsHungry: " + Boolean.toString(isHungry()) +
                ". IsSit: " + Boolean.toString(isSit) + ". IsSad: " + Boolean.toString(isSad) + ". Things: "
                + thingsInHands;
    }


    /**
     * Метод, позволяющий получить объект shared.shared.Troll, записанный в формате JSON.
     * @return объект класса JSONObject, описывающий объект shared.shared.Troll.
     */
    public JSONObject getJSON() {
        JSONObject object = new JSONObject();
        object.put("age", this.getAge());
        object.put("name", this.getName());
        object.put("HP", this.getHP());
        object.put("isSit", isSit);
        object.put("isSad", isSad);
        object.put("things", thingsInHands);
        return object;
    }

}
