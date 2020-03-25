import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.UnaryOperator;


public class Main {
    public static void main(String[] args) throws IOException {
        String path = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

        Parser parser = new Parser(path);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Writer writer = new FileWriter("data/map.json");
        toJson toJSON = new toJson(parser.getLinesPlusStationsMap(), parser.getLinesSet(), parser.getConnectionsFinal());
        gson.toJson(toJSON, writer);
        writer.close();


        FileReader fileReader = new FileReader("data/map.json");
        toJson fromJSON = gson.fromJson(fileReader, toJson.class);

        fromJSON.stations.keySet().stream().forEach(k -> System.out.println("На линии " + k + " - " + fromJSON.stations.get(k).size() + " станций"));
        fileReader.close();

    }

    public static HashMap<String, UnaryOperator<String>> getTrims() {
        HashMap<String, UnaryOperator<String>> trims = new HashMap<>();
        trims.put("(станция метро)", s -> s.substring(0, s.length() - 16));
        trims.put("Московского монорельса",  s -> s.substring(0, s.length() - 23));
        trims.put("линии",  s -> {
            s.substring(0, s.lastIndexOf(" "));
            s.substring(0, s.lastIndexOf(" "));
            return s;
        });
        trims.put("Большой кольцевой линии",  s -> s.substring(0, s.length() - 24));
        trims.put(" Московского центрального кольца",  s -> s.substring(0, s.length() - 32));
        trims.put("пересадка",  s -> s.substring(41));
        trims.put("Переход",  s -> s.substring(19));
        return trims;
    }

}