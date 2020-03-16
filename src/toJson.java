import core.Line;
import java.util.ArrayList;
import java.util.TreeMap;

public class toJson {
    private TreeMap<String, ArrayList<String>> stations;
    private ArrayList<Line> lines;

    public toJson(TreeMap stations, ArrayList lines) {
        this.stations = stations;
        this.lines = lines;
    }

}
