package shared;


import java.util.ArrayList;

public class Place {
    private int degree;
    private Weather weather;
    private ArrayList<Thing> environment = new ArrayList<>();
    private boolean cold;
    private boolean wind;
    private Season season;

    public enum Weather {
        Sunny("Ярко светит солнце."),
        Rainy("Капает дождь."),
        Snowy("Падает снег."),
        Cloudy("На улице облачно.");

        private String currentWeather;

        Weather(String s) {
            currentWeather = s;
        }

        public String russianWeather() {
            return currentWeather;
        }
    }


    public Place() {
        degree = 15;
        weather = Weather.Cloudy;
        cold = false;
        wind = false;
        season = Season.Autumn;
    }

    public Place(int degree, Season s, Weather w) {
        this.degree = degree;
        weather = w;
        season = s;
        if (degree < 20) {
            if (degree < 15) {
                cold = true;
                wind = true;
            } else {
                cold = false;
                wind = true;
            }
        } else {
            cold = false;
            wind = false;
        }
    }

    public void setDegree(int d) {
        degree = d;
    }

    public void addEnvironment(Thing e) {
        environment.add(e);
    }

    public String coldOrWarm() {
        if (cold) {
            return "Холодно.";
        } else {
            return "Тепло.";
        }
    }

    public int getDegree() {
        return degree;
    }

    public ArrayList<Thing> getEnvironment() {
        return environment;
    }

    public Weather getStateWeather() {
        return weather;
    }

    public Season getSeason() {
        return season;
    }

}
