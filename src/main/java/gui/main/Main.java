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

    private static Stage primaryStage;

    public static void main(String[] args) throws IOException {
        // Enable UI
        launch(args);

        // Main word gen test
//        Word word = new Word("renewal");
//        System.out.println(word.toString());
        // Test cases
//        Test test1 = new Test("mystery");
        /**
         * get null
         * check isDictionaryWord():
         *  - Test run w/ cli works
         **/
//        Test test2 = new Test("window");
//        System.out.println(test1.toString());
//        System.out.println(test2.toString());
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;
        //Parent root = FXMLLoader.load(getClass().getResource("/gui.main.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/gui.word.fxml"));
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
