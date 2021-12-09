package gui.main;

import code.main.Test;
import code.main.Word;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class WordController {

    // Stages
    private final Stage primaryStage = Main.getPrimaryStage();

    // Labels
    @FXML
    private Label wordValue;
    @FXML
    private Label syllablesCount;
    @FXML
    private Label meterString;

    // Inputs
    @FXML
    private TextField inputWord;
    @FXML
    private Button createWord;

    @FXML
    public void initialize() {
        createWord.setDisable(true);
    }
    /**
     * onButtonClicked
     * create Word object
     * from contents of inputWord
     */
    @FXML
    public void onButtonClicked() throws IOException {
        /**
         * TESTS
         * 1. remove whitespace from inputWord.getText();
         */
        String s = inputWord.getText().replaceAll("\\s", "");
        Word word = new Word(s);
//        Test word = new Test(s);
        System.out.println(word.toString());
        inputWord.clear();
        createWord.setDisable(true);
        // Set info labels
        wordValue.setText(s);
        /**
         * DEPENDS on
         * syllablesCount.type() = String in Words.class
         * due to (Label) syllablesCount.setText(String string)
         * & on getMeter() in Word.class
         * returning String instead of List<String>
         */
        syllablesCount.setText(word.getSyllablesCount());
        meterString.setText(word.getMeter());
    }
    /**
     * handleKeyReleased
     * enable createWord button
     * if inputWord field is not empty
     */
    @FXML
    public void handleKeyReleased() {
        String text = inputWord.getText();
        boolean disableButtons = text.isEmpty() || text.trim().isEmpty();
        createWord.setDisable(disableButtons);
    }
}
