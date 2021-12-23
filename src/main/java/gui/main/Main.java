package gui.main;

import code.main.Measure;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main extends Application {
    // Cache
    private static final String CACHE_DIR_STRING = "C:\\Users\\MXANA\\Desktop\\cache";
    private static final File CACHE_DIR = new File(CACHE_DIR_STRING);
    // GUI
    private static Stage primaryStage;
    // API
    private static final int CACHE_SIZE = 100 * 1024 * 1024; // 100 mb
    public static Cache CACHE = new Cache(CACHE_DIR, CACHE_SIZE);
    public static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .cache(CACHE)
            .build();
    public static final ObjectMapper MAPPER = new ObjectMapper();
    // Endpoints
    public static final String URL_BASE_WORDS_API = "https://wordsapiv1.p.rapidapi.com/words/";
    public static final String URL_BASE_DATAMUSE = "https://api.datamuse.com/words?";
    public static final String END_WORDS_API_SYLLABLES = "/syllables";
    public static final String END_DATAMUSE_RHYMES = "rel_rhy=";
    public static final String END_DATAMUSE_SYNONYMS = "rel_syn=";
    // Headers
    public static final String HEAD_RAPID_HOST = "x-rapidapi-host";
    public static final String HEAD_WORDS_API = "wordsapiv1.p.rapidapi.com";
    public static final String HEAD_RAPID_KEY = "x-rapidapi-key";
    public static final String HEAD_WORDS_API_KEY = "e852927068mshaf1458fd33faf58p1c06fcjsn9a05d5c4c695";

    public static void main(String[] args) throws IOException {
        // Enable UI
//        launch(args);
        Measure test = new Measure("travesty of justice");
//        System.out.println(test);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;
//        Parent root = FXMLLoader.load(getClass().getResource("/gui.main.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("/gui.word2.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/gui.measure.fxml"));
        primaryStage.setTitle("QuilShel");
        primaryStage.setScene(new Scene(root, 800, 1000));
        primaryStage.show();
    }

    // get part of speech for word
    public static String setType(String word) throws IOException {
        Request request = new Request.Builder()
                .url(URL_BASE_DATAMUSE + "ml=" + word + "&qe=ml&md=p&max=1")
                .build();
        Response response = CLIENT.newCall(request).execute();
        JsonNode rootNode = MAPPER.readTree(response.body().string());
        JsonNode resultNode = rootNode.path(0);
        JsonNode tagsNode = resultNode.path("tags");
        JsonNode typeNode = tagsNode.path(tagsNode.size()-1);

//        System.out.println(tagsNode);
//        System.out.println(tagsNode.size()-1);
//        System.out.println(typeNode);

        String type = typeNode.toString().replaceAll("\"", "");
        // handle unknown value returned
        if(type.equals("u")) {
            type = "n/a";
        }
//        System.out.println(type);
        return type;
    }

    public static String setMeter(String word) throws IOException {
        Request request = new Request.Builder()
                .url(URL_BASE_DATAMUSE + "ml=" + word + "&qe=ml&md=r&max=5")
                .build();
        Response response = CLIENT.newCall(request).execute();
        JsonNode rootNode = MAPPER.readTree(response.body().string());
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
//        System.out.println("\nFrom Main.setSyllablesCount: ");
//        System.out.println("\tURL: "+urlBase_WordsAPI + word + end_WordsAPI_Syllables);

        int count = 0;

        if(word.contains(" ")) {
            String[] words = word.split("\\s+");

            for(int i = 0; i<words.length; i++) {

                Request request = new Request.Builder()
                        .url(URL_BASE_WORDS_API + words[i] + END_WORDS_API_SYLLABLES)
                        .get()
                        .addHeader("x-rapidapi-host", HEAD_WORDS_API)
                        .addHeader("x-rapidapi-key", HEAD_WORDS_API_KEY)
                        .build();
                Response response = CLIENT.newCall(request).execute();
                JsonNode rootNode = MAPPER.readTree(response.body().string());
                JsonNode syllablesNode = rootNode.path("syllables");
                JsonNode countNode = syllablesNode.path("count");

//                System.out.println("\tword: "+words[i]);
//                System.out.println("\turl: "+urlBase_WordsAPI + words[i] + end_WordsAPI_Syllables);
//                System.out.println("\tsyllables for " + words[i] + ": " + countNode.asInt());

                if(countNode.asInt()==0) {
                    count+=1;
                } else {
                    count+=countNode.asInt();
                }
            }

        } else {
            Request request = new Request.Builder()
                    .url(URL_BASE_WORDS_API + word + END_WORDS_API_SYLLABLES)
                    .get()
                    .addHeader("x-rapidapi-host", HEAD_WORDS_API)
                    .addHeader("x-rapidapi-key", HEAD_WORDS_API_KEY)
                    .build();
            Response response = CLIENT.newCall(request).execute();
            JsonNode rootNode = MAPPER.readTree(response.body().string());
            JsonNode syllablesNode = rootNode.path("syllables");
            JsonNode countNode = syllablesNode.path("count");

            count+=countNode.asInt();
        }

//        System.out.println("\tcount for " + word + ": " + count);
        return count;
    }

    // test
    public static boolean isDictionaryWord(String word) throws IOException {
        Request request = new Request.Builder()
                .url("https://wordsapiv1.p.rapidapi.com/words/" + word)
                .get()
                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e852927068mshaf1458fd33faf58p1c06fcjsn9a05d5c4c695")
                .build();
        Response response = CLIENT.newCall(request).execute();
        JsonNode rootNode = MAPPER.readTree(response.body().string());
        JsonNode wordNode = rootNode.path("word");
        String compareString = wordNode.toString();
        compareString = compareString.replaceAll("\"", "");
        return compareString.equals(word);
    }
    // test
    public static boolean allLetters(String word) {
        char[] letters = word.toCharArray();
        for(char c : letters) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
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
