import java.lang.reflect.Array;
import java.util.*;

class LineToJSON {
    private String lineName;
    private String lineNumber;
    private String tempLineNumber;
    private String stationName;
    ArrayList<Object> linesList = new ArrayList<>();
    TreeMap<String, ArrayList<String>> linePlusStationsMap = new TreeMap<>();

    public LineToJSON(List<String> list, TreeMap<String, String> linesMap) {
        ArrayList<String> stations = new ArrayList<>();
        for (String line : list) {
            int begin = line.indexOf("[") + 1;
            int end = line.indexOf("]");
            lineNumber = line.substring(begin, end);
            stationName = line.substring(line.lastIndexOf(">") + 2);
            if (lineNumber.charAt(0) == '0') {
                lineNumber = lineNumber.substring(1);
            }
            if (lineNumber.equals(tempLineNumber) || tempLineNumber == null) {
                stations.add(stationName);
            } else {
                stations.clear();
                stations.add(stationName);
            }
            linePlusStationsMap.put(lineNumber, new ArrayList<>(stations));
            tempLineNumber = lineNumber;
        }
/**----------------------------------------------------------------------------------**/
        for (Map.Entry entry : linesMap.entrySet()) {
            linesList.add(new Line(entry.getKey(), entry.getValue()));
        }

    }

    public HashMap<String, ArrayList> getLinesMap() {
        HashMap<String, ArrayList> linesMap = new HashMap<>();
        linesMap.put("lines", linesList);
        return linesMap;
    }


    public HashMap<String, Map> getStationsMap() {
        HashMap<String, Map> stationsMap = new HashMap<>();
        stationsMap.put("stations", linePlusStationsMap);
        stationsMap.put("connections", linePlusStationsMap);
        return stationsMap;
    }

    public class Line {
        Object number;
        Object name;

        public Line(Object number, Object name) {
            this.number = number;
            this.name = name;
        }
    }
}

