package shared;

import java.io.Serializable;

/**
 * Абстрактный класс, содержащий общие признаки shared.shared.Essence.
 */
public abstract class Essence implements Serializable {

    private int age;
    private String name;
    private int HP;
    private boolean hungry;
    private boolean ableToSpeak;

    /**
     * Конструктор по-умолчанию не предусмотрен.
     */
    private Essence(){

    }


    /**
     * Конструктор для класса shared.shared.Essence, если HP shared.shared.Essence неизвестно.
     * @param age возраст shared.shared.Essence
     * @param name имя shared.shared.Essence
     */
    public Essence(int age, String name) {
        this.age = age;
        this.name = name;
        HP = 100;
        hungry = false;
        ableToSpeak = true;
    }


    /**
     * Конструктор для класса shared.shared.Essence, если HP shared.shared.Essence известно.
     * @param age возраст shared.shared.Essence
     * @param name имя shared.shared.Essence
     * @param HP HP shared.shared.Essence
     */
    public Essence(int age, String name, int HP) {
        this.age = age;
        this.name = name;
        this.HP = HP;
        if (HP != 100) {
            hungry = true;
        } else {
            hungry = false;
        }
        ableToSpeak = true;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Метод, позволяющий повысить возраст shared.shared.Essence на 1.
     */
    public void happyBirthday() {
        age++;
        System.out.println(name + ", с Днём рождения! Персонажу исполнилось " + age);
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int hp) {
        HP = hp;
    }


    public void setHungry(boolean b) {
        hungry = b;
    }

    public boolean isHungry() {
        return hungry;
    }

    public boolean isAbleToSpeak() {
        return ableToSpeak;
    }

    public void setAbleToSpeak(boolean ableToSpeak) {
        this.ableToSpeak = ableToSpeak;
    }
}
