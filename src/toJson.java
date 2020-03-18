import com.sun.source.tree.Tree;
import core.Connection;
import core.Line;
import core.Station;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class toJson {
    private TreeMap<String, ArrayList<String>> stations;
    private ArrayList<Line> lines;
    private HashSet<HashSet<Station>> connections;

    public toJson(TreeMap stations, ArrayList lines, HashSet connections) {
        this.stations = stations;
        this.lines = lines;
        this.connections = connections;
    }

}
