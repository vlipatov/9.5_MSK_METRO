package core;

public class Station implements Comparable<Station> {
    private String line;
    private String station;

    public String getLine() {
        return line;
    }

    public Station(String line, String station) {
        this.line = line;
        this.station = station;
    }

    public String getStation() {
        return station;
    }

    @Override
    public int compareTo(Station stn) {
        int lineComparison = line.compareTo(stn.getLine());
        if (lineComparison != 0) {
            return lineComparison;
        }
        return station.compareToIgnoreCase(stn.getStation());
    }

    @Override
    public boolean equals(Object obj) {
        return compareTo((Station) obj) == 0;
    }

    @Override
    public String toString() {
        return station;
    }

}
