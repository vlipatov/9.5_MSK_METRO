import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import core.Line;
import core.Stations;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";
        Parser parser = new Parser(path);
//        parser.getConnections().forEach(System.out::println);
//        parser.getLinesPlusStations().forEach(System.out::println);
//        for (Map.Entry entry : parser.getLines().entrySet()) {
//            System.out.println(entry.getKey() + " => " + entry.getValue());
//        }
//        for (Map.Entry entry : parser.getColorsMap().entrySet()) {
//            System.out.println(entry.getKey() + " => " + entry.getValue());
//        }

        LineToJSON toJSON = new LineToJSON(parser.getLinesPlusStations(), parser.getLines());
//        for (Map.Entry entry : toJSON.getNewMap().entrySet()) {
//            new Gson().toJson(new Line(entry.getKey(), entry.getValue()), new FileWriter("data/jopa.json"));
//        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Line lines = new Line(toJSON.getLinesList());
        Stations stations = new Stations(toJSON.getLinePlusStationsMap());

        StringBuilder json = new StringBuilder();
//        json.append(gson.toJson(toJSON.getStationsMap()));
//        json.append(gson.toJson(toJSON.getLinesMap()));

        json.append(gson.toJson(stations));
        json.append(gson.toJson(lines));

        System.out.println(json);

    }
}