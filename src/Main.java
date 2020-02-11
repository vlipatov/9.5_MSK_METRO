import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.toMap;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

        Document doc = Jsoup.connect(path).maxBodySize(0).get();

        Elements elements = doc.select("td");
        List<String> titles = new ArrayList<>();
        TreeMap<String, String> lines = new TreeMap<>();
        elements.forEach(element -> {
                String title = element.getElementsByTag("span").attr("title");
                if (title.matches("[А-яёЁ\\-\\s]+\\s+линия") || title.matches("[А-яёЁ\\-\\s]+\\s+монорельс")
                        || title.matches("[А-яёЁ\\-\\s]+\\s+кольцо")) {
                    lines.put(element.getElementsByTag("span").get(0).text(),title);
//                    System.out.println(element.getElementsByTag("span").text());
                }
//            System.out.println(element.getElementsByTag("span").text());

        });
        for (Map.Entry entry : lines.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
    }
}
