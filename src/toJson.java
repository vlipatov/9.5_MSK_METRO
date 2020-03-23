import com.sun.source.tree.Tree;
import core.Connection;
import core.Line;
import core.Station;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class toJson {
    TreeMap<String, ArrayList<String>> stations;
    private TreeSet<Line> lines;
    private HashSet<TreeSet<Station>> connections;

    public toJson(TreeMap stations, TreeSet lines, HashSet connections) {
        this.stations = stations;
        this.lines = lines;
        this.connections = connections;
    }
    public TreeMap<String, ArrayList<String>> getStations() {
        return stations;
    }

}
