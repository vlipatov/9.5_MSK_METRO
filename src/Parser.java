import core.Connection;
import core.Line;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Parser {
    private List<String> linesPlusStations = new ArrayList<>();
    private TreeMap<String, String> lines = new TreeMap<>();
    private TreeSet<String> stations = new TreeSet<>();
    private List<String> allConnections = new ArrayList<>();
    private ArrayList<Line> linesList = new ArrayList<>();
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
                }
                if (element.getElementsByTag("span").attr("style").contains("display")
                        && lineNumber.equals("011А")) {
                    lines.put(lineNumber, lineName + " [" +
                            element.getElementsByTag("span").attr("style").substring(8) + "]");
                }
                if(lineNumber.equals("8А") && lineColor.contains("background:background-color: ")) {
                    lines.put(lineNumber, lineName + " [" + lineColor.substring(29,36) + "]");
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

    public ArrayList<Line> getLinesList() {

        for (Map.Entry entry : lines.entrySet()) {
            int first = entry.getValue().toString().indexOf("[");
            int last = entry.getValue().toString().lastIndexOf("]");
            String lineName = entry.getValue().toString().substring(0,first-1);
            String color = entry.getValue().toString().substring(first+1,last);
            linesList.add(new Line((String) entry.getKey(), lineName, color));
        }
        return linesList;
    }

    public List<Connection> getAllConnections() {
        String tempStationName = null;
        String stationName;
        for(String connection : allConnections) {
            int begin = connection.indexOf("]");
            int end = connection.lastIndexOf("[");
            stationName = allConnections.get(0).substring(begin, end);
            if (stationName.equals(templineName) || )
        }

        return allConnections;
    }

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