import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class LineToJSON {
    private String lineName;
    private String lineNumber;
    private String tempLineNumber;
    private String stationName;

    public LineToJSON(List<String> list) {
        TreeMap<String, ArrayList<String>> newMap = new TreeMap<>();
        ArrayList<String> stations = new ArrayList<>();
        for (String line : list) {
            int begin = line.indexOf("[") + 1;
            int end = line.indexOf("]");
            lineNumber = line.substring(begin, end);
            if(lineNumber.equals("11")) System.out.println("jopa");
            stationName = line.substring(line.lastIndexOf(">") + 2);
            if (lineNumber.equals(tempLineNumber) || tempLineNumber == null) {
                stations.add(stationName);
            } else stations.clear();

            newMap.put(lineNumber, new ArrayList<>(stations));
            tempLineNumber = lineNumber;
        }
        for (Map.Entry entry : newMap.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
    }
}

