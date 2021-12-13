package gui.main;

import code.main.Word;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    // Lists
    @FXML
    private ListView<String> rhymesList;
    @FXML
    private ListView<String> synonymsList;

    // Tables
    @FXML
    private TableView synTable;
    @FXML
    private TableColumn synWord;
    @FXML
    private TableColumn synMeter;
    @FXML
    private TableView rhyTable;
    @FXML
    private TableColumn rhyWord;
    @FXML
    private TableColumn rhyMeter;

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
         */
        syllablesCount.setText(word.getSyllablesCountString());
        meterString.setText(word.getMeter());
        // Set lists
//        rhymesList.getItems().setAll(word.getRhymes());
//        synonymsList.getItems().setAll(word.getSynonyms());
        // Set tables
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
