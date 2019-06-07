package shared;


public class Sandwich extends Thing {
    private int calorie;
    private Taste sandwichTaste;

    //constructors
    public Sandwich() {
        super("Бутер с " + Taste.Empty.tasteToString(), Condition.Solid, 10);
        sandwichTaste = Taste.Empty;
        calorie = 10;
    }

    public Sandwich(Taste t) {
        super("Бутер с " + t.tasteToString(), Condition.Solid, 10 + t.getWeight());
        sandwichTaste = t;
        switch (t) {
            case Empty:
                calorie = 10;
                break;
            case Butter:
                calorie = 12;
                break;
            case Cheese:
                calorie = 14;
                break;
            default:
                calorie = 15;
                break;
        }
    }
    //end constructors

    public int getCalorie() {
        return calorie;
    }

    public Taste getTaste() {
        return sandwichTaste;
    }

    public static enum Taste {
        Butter("маслом", 3),
        Cheese("сыром", 5),
        Sausage("колбасой", 5),
        RedCaviar("красной икрой", 5),
        BlackCaviar("чёрной икрой", 5),
        Salmon("лососем", 7),
        Empty("ничем", 0);

        private String currentTaste;
        private int weight;

        Taste(String s, int w) {
            currentTaste = s;
            weight = w;
        }

        public String tasteToString() {
            return currentTaste;
        }

        public int getWeight() {
            return weight;
        }
    }
}
