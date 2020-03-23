import core.Line;
import core.Station;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Parser {
    private List<String> linesPlusStations = new ArrayList<>();
    private TreeMap<String, String> lines = new TreeMap<>();
    private TreeSet<String> stations = new TreeSet<>();
    private List<String> allConnections = new ArrayList<>();
    private TreeSet<Line> linesSet = new TreeSet<>();
    private AtomicReference<String> templine = new AtomicReference<>();
    private AtomicReference<String> templineName = new AtomicReference<>();
    private AtomicReference<String> tempStation = new AtomicReference<>();


    public Parser(String path) throws IOException {
        Document doc = Jsoup.connect(path).maxBodySize(0).get();
        Elements elements = doc.select("td");

        for (Element element : elements) {
            String lineName = element.getElementsByTag("span").attr("title");
            String lineColor = element.attr("style");
            String station = element.getElementsByTag("a").attr("title");
            String connection = element.getElementsByTag("span").attr("title");
            int index = station.indexOf("(");
            if (station.equals("Троице-Лыково (техническая платформа)")) {
                break;
            }
            if (lineName.matches("[А-яёЁ\\-\\s]+\\s+линия") || lineName.matches("[А-яёЁ\\-\\s]+\\s+монорельс")
                    || lineName.matches("[А-яёЁ\\-\\s]+\\s+кольцо")) {
                String lineNumber = element.getElementsByTag("span").get(0).text();
                templine.set(lineNumber);
                templineName.set(lineName);
                if (lineColor.contains("background:")) {
                    lines.put(lineNumber, lineName + " [" + lineColor.substring(11) + "]");
                    linesSet.add(new Line(lineNumber, lineName, lineColor.substring(11)));
                }
                if (element.getElementsByTag("span").attr("style").contains("display")
                        && lineNumber.equals("011А")) {
                    lines.put(lineNumber, lineName + " [" +
                            element.getElementsByTag("span").attr("style").substring(8) + "]");
                    linesSet.add(new Line(lineNumber, lineName, element.getElementsByTag("span").attr("style").substring(8)));
                }
                if (lineNumber.equals("8А") && lineColor.contains("background:background-color: ")) {
                    lines.put(lineNumber, lineName + " [" + lineColor.substring(29, 36) + "]");
                    linesSet.add(new Line(lineNumber, lineName, lineColor.substring(29, 36)));
                }
            }

            if (station.contains("станция метро") || station.contains("станция монорельса") || station.contains("станция МЦК")
                    || station.matches("Шаболовская") || station.matches("Полежаевская") || station.matches("Фонвизинская")) {

                if (station.contains("(")) {
                    station = station.substring(0, index - 1);
                }
                if (station.contains("Переход")) {
                    continue;
                }
                tempStation.set(station);
                stations.add(station);
                linesPlusStations.add(templineName + " [" + templine + "]" + "\t" + "=>" + "\t" + station);
            }
            if (connection.contains("Переход") || lineName.contains("пересадка")) {
                for (Element connections : element.getElementsByTag("span")) {
                    if (!connections.getElementsByTag("span").text().isEmpty()) {
                        allConnections.add("[" + templine + "] " + tempStation + " => "
                                + "[" + connections.text() + "] "
                                + trimmer(connections.nextElementSibling().attr("title")));
                    }
                }
            }
        }
    }

    /**                  Метод для получения формирования линий и массивов станций твынес в геттер                    */
    public TreeMap<String, ArrayList<String>> getLinesPlusStationsMap() {
        String lineNumber;
        String tempLineNumber = null;
        String stationName;
        ArrayList<String> stations = new ArrayList<>();
        TreeMap<String, ArrayList<String>> linesPlusStationsMap = new TreeMap<>();
        for (String line : linesPlusStations) {
            int begin = line.indexOf("[") + 1;
            int end = line.indexOf("]");
            lineNumber = line.substring(begin, end);
            stationName = line.substring(line.lastIndexOf(">") + 2);
            for (Line lane : linesSet) {
                if (lineNumber.equals(lane.getNumber())) {
                    lane.addStation(new Station(stationName, lane));
                }
            }
            if (lineNumber.charAt(0) == '0') {
                lineNumber = lineNumber.substring(1);
            }
            if (lineNumber.equals(tempLineNumber) || tempLineNumber == null) {
                stations.add(stationName);
            } else {
                stations.clear();
                stations.add(stationName);
            }
            linesPlusStationsMap.put(lineNumber, new ArrayList<>(stations));
            tempLineNumber = lineNumber;
        }
        return linesPlusStationsMap;
    }

    public TreeSet<Line> getLinesSet() {
        return linesSet;
    }

    /**                  Метод для получения пересадок для записи в JSON вынес в геттер                               */
    public HashSet<TreeSet<Station>> getConnectionsFinal() {
        String stationName;
        String lineNumber;
        String secondStationName;
        String secondLineNumber;
        TreeSet<Station> stations = new TreeSet<>();
        ArrayList<TreeSet<Station>> connections = new ArrayList<>();
        HashSet<TreeSet<Station>> connectionsFinal;

        for (String connection : allConnections) {
            int firstBracketStart = connection.indexOf("[") + 1;
            int firstBracketEnd = connection.indexOf("]");
            int lastBracketStart = connection.lastIndexOf("[") + 1;
            int lastBracketEnd = connection.lastIndexOf("]");

            stationName = connection.substring(firstBracketEnd + 2, lastBracketStart - 5);
            lineNumber = connection.substring(firstBracketStart, firstBracketEnd);
            secondStationName = connection.substring(lastBracketEnd + 2);
            secondLineNumber = connection.substring(lastBracketStart, lastBracketEnd);

            for (Line line : linesSet) {
                if (lineNumber.equals(line.getNumber())) {
                    for (Station station : line.getStations())
                    {
                        if(stationName.equals(station.getName()))
                            stations.add(station);
                    }
                    for (Line secondLine : linesSet) {
                        if (secondLineNumber.equals(secondLine.getNumber())) {
                            for (Station secondStation : secondLine.getStations())
                            {
                                if(secondStationName.equals(secondStation.getName()))
                                    stations.add(secondStation);
                            }
                        }
                    }
                    connections.add(new TreeSet<>(stations));
                    stations.clear();
                }
            }
        }

        ArrayList<TreeSet<Station>> connectionsCopy = new ArrayList<>(connections);

        for (TreeSet<Station> oldTreeSet : connections) {
            for (Station oldStation : oldTreeSet) {
                for (TreeSet<Station> newTreeSet : connectionsCopy) {
                    if (newTreeSet.contains(oldStation)) {
                        newTreeSet.addAll(oldTreeSet);
                    }
                }
            }
        }
        connectionsFinal = new HashSet<>(connectionsCopy);

        return connectionsFinal;
    }
    /**                  Метод для форматирования текста в описании пересадки                                         */
    public static String trimmer(String string) {
        String newString = null;
        if (string.contains("Переход")) {
            newString = string.substring(19);
        } else if (string.contains("пересадка")) {
            newString = string.substring(41);
        }
        if (newString.contains(" Московского центрального кольца")) {
            newString = newString.substring(0, newString.length() - 32);
        }
        if (newString.contains("Большой кольцевой линии")) {
            newString = newString.substring(0, newString.length() - 24);
        }
        if (newString.contains("линии")) {
            newString = newString.substring(0, newString.lastIndexOf(" "));
            newString = newString.substring(0, newString.lastIndexOf(" "));
        }
        if (newString.contains("(станция метро)")) {
            newString = newString.substring(0, newString.length() - 16);
        }
        if (newString.contains("Московского монорельса"))
            newString = newString.substring(0, newString.length() - 23);

        return newString;
    }
}