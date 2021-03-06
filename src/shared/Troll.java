package shared;

import database.DBConst;
import database.Queryable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


public class Troll extends Essence implements Comparable<Troll>, Serializable, Queryable {
    private boolean isSit;
    private boolean isSad;
    private List<Thing> thingsInHands = new ArrayList<>();
    private OffsetDateTime initDate;

    /**
     * Конструктор для класса shared.shared.Troll, если HP тролля неизвестно.
     *
     * @param age  возраст тролля
     * @param name имя тролля
     */
    public Troll(int age, String name) {
        super(age, name);
        isSit = true;
        isSad = true;
        initDate = OffsetDateTime.now(ZoneId.of("Europe/Moscow"));
    }

    /**
     * Конструктор для класса shared.shared.Troll, если HP тролля известно.
     *
     * @param age  возраст тролля
     * @param name имя тролля
     * @param HP   HP тролля
     */
    public Troll(int age, String name, int HP) {
        super(age, name, HP);
        isSit = true;
        isSad = true;
        initDate = OffsetDateTime.now(ZoneId.of("Europe/Moscow"));
    }

    /**
     * Конструктор для класса shared.shared.Troll, объект задан в формате JSON
     *
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
        initDate = OffsetDateTime.now(ZoneId.of("Europe/Moscow"));
    }

    public Troll(String name, int age, int HP, boolean isSit, boolean isSad, OffsetDateTime time) {
        super(age, name, HP);
        this.isSit = isSit;
        this.isSad = isSad;
        initDate = time;
    }

    /**
     * Метод, реализующий сортировку по-умолчанию для класса shared.shared.Troll.
     *
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

    public boolean isSit() {
        return isSit;
    }

    public void setSit(boolean b) {
        isSit = b;
    }

    /**
     * Метод, позволяющий пополнить HP тролля с помощью объекта shared.shared.Sandwich.
     *
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

    public boolean isSad() {
        return isSad;
    }

    public void setSad(boolean b) {
        isSad = b;
    }

    public OffsetDateTime getInitDate() {
        return initDate;
    }

    /**
     * Метод, позволяющий произнести фразу троллю.
     *
     * @param speech фраза, сказанная троллю
     * @return true, если тролль сказал фразу, false, если тролль не может говорить.
     */
    public boolean say(String speech) {
        if (isAbleToSpeak()) {
            System.out.println(this.getName() + " говорит: \"" + speech + "\"");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Метод, позволяющий добавить вещь троллю.
     *
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
        return temp.thingsInHands.size() == this.thingsInHands.size();
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
        result = result * rnd + getName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Troll " + getName() + ". Возраст: " + getAge() + ". HP: " + getHP()
                + ". IsHungry: " + isHungry() +
                ". IsSit: " + isSit + ". IsSad: " + isSad + ". Things: "
                + thingsInHands + " . Дата создания: "+initDate.toString();
    }


    /**
     * Метод, позволяющий получить объект shared.shared.Troll, записанный в формате JSON.
     *
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

    @Override
    public String getInsertSqlQuery() {
        return String.format("INSERT INTO " + DBConst.TROLLS_TABLE + " VALUES (ID_TO_REPLACE, '%s',%s,%s,%s,%s,'%s','USER_TO_REPLACE');",
                getName(), getAge(), getHP(), isSit, isSad, initDate);
    }

    @Override
    public String getDeleteSqlQuery() {
        return "DELETE FROM " + DBConst.TROLLS_TABLE + "WHERE id=" + hashCode() + " AND " + DBConst.TROLL_USER + "=" + "'USER_TO_REPLACE');";
    }
}
