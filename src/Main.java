import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";
        Parser parser = new Parser(path);
//        parser.getConnections().forEach(System.out::println);
        parser.getLinesPlusStations().forEach(System.out::println);
//        for (Map.Entry entry : parser.getLines().entrySet()) {
//            System.out.println(entry.getKey() + " => " + entry.getValue());
//        }
//        LineToJSON toJSON = new LineToJSON(parser.getLinesPlusStations());

    }
}
