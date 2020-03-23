import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;


public class Main {
    public static void main(String[] args) throws IOException {
        String path = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";
        Parser parser = new Parser(path);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Writer writer = new FileWriter("data/map.json");
        toJson toJson = new toJson(parser.getLinesPlusStationsMap(), parser.getLinesSet(), parser.getConnectionsFinal());
        gson.toJson(toJson, writer);
        writer.close();

//        FileReader reader = new FileReader("data/map.json");
//        toJson fromJson = gson.fromJson(reader, toJson.class);
//        fromJson.stations.keySet().stream().forEach(k -> System.out.println("На линии " + k + " => " + fromJson.stations.get(k).size() + " станций"));
//        reader.close();
//        JSONParser jsonParser = new JSONParser();
//        try (Reader reader = new FileReader("data/map.json")) {
//            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
//            System.out.println(jsonObject);
//            JSONArray msg = (JSONArray) jsonObject.get("stations");
//            System.out.println(msg);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
}