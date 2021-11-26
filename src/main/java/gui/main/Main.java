package gui.main;

import code.main.Word;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private static Stage primaryStage;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {
        //launch(args);
        // Syllables test
//        Word word = new Word("inevitable",1);
//        System.out.println(word.getWord());
        //System.out.println(word.getSyllables());
        // Rhymes test

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://wordsapiv1.p.rapidapi.com/words/inevitable/rhymes")
                .get()
                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e852927068mshaf1458fd33faf58p1c06fcjsn9a05d5c4c695")
                .build();
        Response response = client.newCall(request).execute();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.body().string());
        JsonNode rhymesNode = rootNode.path("rhymes");
        JsonNode allNode = rhymesNode.path("all");

        List<String> rhymes = new ArrayList<>();

        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(allNode.toString());
        int lastIndex = breakIterator.first();
        while(BreakIterator.DONE != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();
            if(lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(allNode.toString().charAt(firstIndex))) {
                rhymes.add(allNode.toString().substring(firstIndex, lastIndex));
            }
        }
        System.out.println(rhymes.toString());
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
