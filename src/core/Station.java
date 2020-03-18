package core;

public class Station implements Comparable<Station>{
    private String line;
    private String station;

    public Station(String line, String station) {
        this.line = line;
        this.station = station;
    }

    public String getStation() {
        return station;
    }

    public int compareTo(Station p){
        if (station.equals(p.getStation())) {
            return 0;
        }
        else return 1;
    }
}