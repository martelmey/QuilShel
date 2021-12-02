package gui.main;

import code.main.Word;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.BreakIterator;
import java.util.*;

public class Main extends Application {

    private static Stage primaryStage;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        //launch(args);

        // Main word gen test
        //Word word = new Word("commit");
        //System.out.println(word.toString());

        // test endpoints & deserialization
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        // data muse
        Request request = new Request.Builder()
                .url("https://api.datamuse.com/words?rel_syn=wait")
                .build();
        Response response = client.newCall(request).execute();
        JsonNode rootNode = objectMapper.readTree(response.body().string());
        System.out.print(rootNode);

        Map<String,String> map = new HashMap<>();
        addKeys("", rootNode, map, new ArrayList<>());

        map.entrySet()
                .forEach(System.out::println);

//        List<String> synonyms = new ArrayList<>();
//        BreakIterator breakIterator = BreakIterator.getWordInstance();
//        breakIterator.setText(rootNode);

        // words api synonyms
//        Request request = new Request.Builder()
//                .url("https://wordsapiv1.p.rapidapi.com/words/lovely/synonyms")
//                .get()
//                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
//                .addHeader("x-rapidapi-key", "e852927068mshaf1458fd33faf58p1c06fcjsn9a05d5c4c695")
//                .build();
//        Response response = client.newCall(request).execute();
//        JsonNode rootNode = objectMapper.readTree(response.body().string());
//        JsonNode wordNode = rootNode.path("word");
//        System.out.println(rootNode);
//        System.out.println(wordNode);

//        JsonNode synonymsNode = rootNode.path("synonyms");
//        List<String> synonyms = new ArrayList<>();
//        BreakIterator breakIterator = BreakIterator.getWordInstance();
//        breakIterator.setText(synonymsNode.toString());
//        int lastIndex = breakIterator.first();
//        while(BreakIterator.DONE != lastIndex) {
//            int firstIndex = lastIndex;
//            lastIndex = breakIterator.next();
//            if(lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(synonymsNode.toString().charAt(firstIndex))) {
//                synonyms.add(synonymsNode.toString().substring(firstIndex, lastIndex));
//            }
//        }
//        System.out.println(synonyms);
    }

    public static void addKeys(String currentPath, JsonNode jsonNode, Map<String,String> map, List<Integer> suffix) {
        if(jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String,JsonNode>> iter = objectNode.fields();
            String pathPrefix = currentPath.isEmpty() ? "" : currentPath + "-";
            while(iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                addKeys(pathPrefix + entry.getKey(), entry.getValue(), map, suffix);
            }
        } else if(jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for(int i = 0; i < arrayNode.size(); i++) {
                suffix.add(i + 1);
                addKeys(currentPath, arrayNode.get(i), map, suffix);
                if(i + 1 < arrayNode.size()) {
                    suffix.remove(arrayNode.size() - 1);
                }
            }
        } else if(jsonNode.isValueNode()) {
            if(currentPath.contains("-")) {
                for(int i = 0; i < suffix.size(); i++) {
                    currentPath += "-" + suffix.get(i);
                }
                suffix = new ArrayList<>();
            }
            ValueNode valueNode = (ValueNode) jsonNode;
            map.put(currentPath, valueNode.asText());
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/gui.main.fxml"));
        primaryStage.setTitle("QuilShel");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

//    @Override
//    public void init() {
//        try {
//            PoemData.getInstance().loadPoem();
//        } catch (IOException e) {
//            System.out.println(e.getMessage());;
//        }
//    }

//    @Override
//    public void stop() {
//        try {
//            PoemData.getInstance().serialize();
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//    }

}
