package gui.main;

import code.main.Test;
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
    // GUI
    private static Stage primaryStage;
    // API
    private static OkHttpClient client = new OkHttpClient();
    private static ObjectMapper objectMapper = new ObjectMapper();
    // Endpoints
    public static String urlBase_WordsAPI = "https://wordsapiv1.p.rapidapi.com/words/";
    public static String urlBase_Datamuse = "https://api.datamuse.com/words?";
    public static String end_WordsAPI_Syllables = "/syllables";
    // Headers
    public static String rapidHostHeader = "wordsapiv1.p.rapidapi.com";
    public static String rapidKeyHeader = "e852927068mshaf1458fd33faf58p1c06fcjsn9a05d5c4c695";

    public static void main(String[] args) throws IOException {
        // Enable UI
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;
//        Parent root = FXMLLoader.load(getClass().getResource("/gui.main.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/gui.word2.fxml"));
        primaryStage.setTitle("QuilShel");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static String setMeter(String word) throws IOException {
        Request request = new Request.Builder()
                .url(urlBase_Datamuse + "ml=" + word + "&qe=ml&md=r&max=5")
                .build();
        Response response = client.newCall(request).execute();
        JsonNode rootNode = objectMapper.readTree(response.body().string());
        JsonNode resultOne = rootNode.path(0);
        JsonNode tagsNode = resultOne.path("tags");
        int proIndx = tagsNode.size()-1;
        JsonNode proNode = tagsNode.path(proIndx);
        String meterString = proNode.toString().replaceAll("[^\\d]", "");
        List<String> meterList = new ArrayList<>();
        for(int i = 0; i < meterString.length(); i++) {
            char charValue = meterString.charAt(i);
            int intValue = Character.getNumericValue(charValue);
            if(intValue==1) {
                meterList.add("/");
            } else {
                meterList.add("*");
            }
        }
        String meter = meterList.toString().replaceAll(",", "");
        meter = meter.replaceAll("\\[","");
        meter = meter.replaceAll("\\]","");
        meter = meter.replaceAll("\\s", "");
        return meter;
    }

    // count syllables for whole sentence, return int
    public static int setSyllablesCount(String word) throws IOException {
        System.out.println("\nFrom Main.setSyllablesCount: ");
//        System.out.println("\tURL: "+urlBase_WordsAPI + word + end_WordsAPI_Syllables);

        int count = 0;

        if(word.contains(" ")) {
            String[] words = word.split("\\s+");

            for(int i = 0; i<words.length; i++) {

                Request request = new Request.Builder()
                        .url(urlBase_WordsAPI + words[i] + end_WordsAPI_Syllables)
                        .get()
                        .addHeader("x-rapidapi-host", rapidHostHeader)
                        .addHeader("x-rapidapi-key", rapidKeyHeader)
                        .build();
                Response response = client.newCall(request).execute();
                JsonNode rootNode = objectMapper.readTree(response.body().string());
                JsonNode syllablesNode = rootNode.path("syllables");
                JsonNode countNode = syllablesNode.path("count");

                System.out.println("\tword: "+words[i]);
                System.out.println("\turl: "+urlBase_WordsAPI + words[i] + end_WordsAPI_Syllables);
                System.out.println("\tsyllables for " + words[i] + ": " + countNode.asInt());

                if(countNode.asInt()==0) {
                    count+=1;
                } else {
                    count+=countNode.asInt();
                }
            }

        } else {
            Request request = new Request.Builder()
                    .url(urlBase_WordsAPI + word + end_WordsAPI_Syllables)
                    .get()
                    .addHeader("x-rapidapi-host", rapidHostHeader)
                    .addHeader("x-rapidapi-key", rapidKeyHeader)
                    .build();
            Response response = client.newCall(request).execute();
            JsonNode rootNode = objectMapper.readTree(response.body().string());
            JsonNode syllablesNode = rootNode.path("syllables");
            JsonNode countNode = syllablesNode.path("count");

            count+=countNode.asInt();
        }

        System.out.println("\tcount for " + word + ": " + count);
        return count;
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
