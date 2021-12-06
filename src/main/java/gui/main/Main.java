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
        BreakIterator breakIterator = BreakIterator.getWordInstance();

        // data muse
        Request request = new Request.Builder()
                .url("https://api.datamuse.com/words?ml=flower&qe=ml&md=r&max=5")
                .build();
        Response response = client.newCall(request).execute();

        // as JsonNode
        JsonNode rootNode = objectMapper.readTree(response.body().string());
        JsonNode resultOne = rootNode.path(0);
        JsonNode synNode = resultOne.path("word");
        JsonNode tagsNode = resultOne.path("tags");
        int proIndx = tagsNode.size()-1;
        JsonNode proNode = tagsNode.path(proIndx);

        // iterate proNode, get ints
        // v1
//        breakIterator.setText(proNode.toString());
//        int lastIndex = breakIterator.first();
//        while(BreakIterator.DONE != lastIndex) {
//            int firstIndex = lastIndex;
//            lastIndex = breakIterator.next();
//            if(lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(proNode.toString().charAt(firstIndex))) {
//                System.out.println(proNode.toString().substring(firstIndex, lastIndex));
//            }
//        }
        // v2
        String meter = proNode.toString().replaceAll("[^\\d]", "");

        //System.out.println(rootNode);
        //System.out.println(rootNode.get(0));
        //System.out.println(resultOne);
        //System.out.println(tagsNode);
        //System.out.println(tagsNode.size());
        System.out.println(synNode);
        System.out.println(proNode);
        System.out.println(meter);

//        System.out.println(rootNode.get("word").get(0));
//        for(int i = 0; i < rootNode.size(); i++) {
//            System.out.println(rootNode.get(i));
//        }

        // as ArrayNode
        // testCreatingKeyValues()
//        ArrayNode arrayNode = (ArrayNode) rootNode;
//        for(int i = 0; i < arrayNode.size(); i++) {
//            System.out.println(arrayNode.get(i));
//        }

//        List<String> synonyms = new ArrayList<>();
//        BreakIterator breakIterator = BreakIterator.getWordInstance();
//        breakIterator.setText(rootNode);
    }

    public static void addKeys(String currentPath, JsonNode jsonNode, Map<String, String> map) {
        if(jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = currentPath.isEmpty() ? "" : currentPath;
            while(iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                addKeys(pathPrefix + entry.getKey(), entry.getValue(), map);
            }
        } else if(jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for(int i = 0; i < arrayNode.size(); i++) {
                addKeys(currentPath + "[" + i + "]", arrayNode.get(i), map);
            }
        } else if(jsonNode.isValueNode()) {
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
