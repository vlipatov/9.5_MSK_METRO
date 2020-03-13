import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Parser {
    List<String> linesPlusStations = new ArrayList<>();
    TreeMap<String, String> lines = new TreeMap<>();
    TreeSet<String> stations = new TreeSet<>();
    TreeMap<String, String> colorsMap = new TreeMap<>();

    List<String> allConnections = new ArrayList<>();
    AtomicInteger i = new AtomicInteger();
    AtomicReference<String> templine = new AtomicReference<>();
    AtomicReference<String> templineName = new AtomicReference<>();
    AtomicReference<String> tempStation = new AtomicReference<>();

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
//                    colorsMap.put(lineNumber, lineColor.substring(11));
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
                i.getAndIncrement();
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

    public TreeMap<String, String> getColorsMap() {
        return colorsMap;
    }

    public List<String> getLinesPlusStations() {
        return linesPlusStations;
    }

    public TreeMap<String, String> getLines() {
        return lines;
    }

    public TreeSet<String> getStations() {
        return stations;
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