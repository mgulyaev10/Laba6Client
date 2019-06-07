package shared;

import java.io.Serializable;

public class Thing implements Serializable {
    private Condition condition;
    private String name;
    private int weight;

    public Thing() {
        name = "Неизвестная вещь";
        condition = Condition.Solid;
    }

    public Thing(String name, Condition condition, int weight) {
        this.name = name;
        this.condition = condition;
        this.weight = weight;
    }

    public Condition getCondition() {
        return condition;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        Thing temp = (Thing) obj;
        if (!temp.getName().equals(this.getName()))
            return false;
        if (this.getCondition() != temp.getCondition())
            return false;
        if (this.getWeight() != temp.getWeight())
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Thing " + name + ". Condition is " + condition.toString() + ". Weight = " + weight;
    }

    public enum Condition implements Serializable {
        Liquid,
        Solid,
        Gaseous;
    }
}
