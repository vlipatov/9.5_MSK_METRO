import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";
        Document doc = Jsoup.connect(path).maxBodySize(0).get();
        Elements elements = doc.select("td");
        List<String> info = new ArrayList<>();
        TreeMap<String, String> lines = new TreeMap<>();
        TreeSet<String> stations = new TreeSet<>();
        List<String> allConnections = new ArrayList<>();

        AtomicInteger i = new AtomicInteger();
        AtomicReference<String> templine = new AtomicReference<>();
        AtomicReference<String> templineName = new AtomicReference<>();
        AtomicReference<String> tempStation = new AtomicReference<>();

        for (Element element : elements) {
            String lineName = element.getElementsByTag("span").attr("title");
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
                lines.put(lineNumber, lineName);
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
                info.add(templineName + " [" + templine + "]" + "\t" + "=>" + "\t" + station);
//                        System.out.println(templineName+" ["+templine+"]"+"\t\t"+"=>"+"\t"+station);
            }
            if (connection.contains("Переход") || lineName.contains("пересадка")) {
                for (Element connections : element.getElementsByTag("span")) {
                    if (!connections.getElementsByTag("span").text().isEmpty()) {
                        allConnections.add("[" + templine + "] " + tempStation + " => "
                                + "[" + connections.text() + "] "
                                + connections.nextElementSibling().attr("title"));
                    }
                }
            }
        }
        for (String connection : allConnections) {
            int index = connection.lastIndexOf(">");
            for (String station : stations){
                if(connection.substring(index).contains(station))
                {
                    if(connection.contains("Переход на станцию "))
                    {
                        connection.substring(26);
                    }
                }
            }
            connection
        }
            int size = info.size();
            System.out.println(size);
            allConnections.forEach(System.out::println);
//        for (int incorrectStringNumber = size; incorrectStringNumber > 269; incorrectStringNumber--) {
//            info.remove(incorrectStringNumber - 1);
//        }
//        info.forEach(System.out::println);
//        System.out.println(info.size());
//        System.out.println(i);
//        for (Map.Entry entry : lines.entrySet()) {
//            System.out.println(entry.getKey() + " => " + entry.getValue());
//        }
        }
    }

