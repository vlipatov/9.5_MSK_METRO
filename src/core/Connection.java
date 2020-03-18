package core;

import java.util.TreeSet;

public class Connection {
    private TreeSet<Station> stations;

    public TreeSet<Station> getStations() {
        return stations;
    }

    public Connection(TreeSet<Station> stations) {
        this.stations = stations;
    }

}
