package shared;

public enum Season {
    Winter("Холодно, зима на улице."),
    Spring("Весна, с каждым днём теплеет. Сессия близко."),
    Summer("Лето, лучшая пора."),
    Autumn("На улице осень, листья падают с деревьев.");

    private String currentSeason;

    Season(String s) {
        currentSeason = s;
    }

    public String russianSeason() {
        return currentSeason;
    }
}
