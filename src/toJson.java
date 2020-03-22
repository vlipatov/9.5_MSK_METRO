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
    private TreeSet<Line> lines;
    private HashSet<TreeSet<Station>> connections;

    public toJson(TreeMap stations, TreeSet lines, HashSet connections) {
        this.stations = stations;
        this.lines = lines;
        this.connections = connections;
    }

}
