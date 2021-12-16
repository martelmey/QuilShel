package gui.main;

import code.main.Rhyme;
import code.main.Synonym;
import code.main.Word;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class WordController {

    // Stages
//    private final Stage primaryStage = Main.getPrimaryStage();

    // Labels
    @FXML
    private Label wordValue;
    @FXML
    private Label syllablesCount;
    @FXML
    private Label meterString;
    @FXML
    private Label typeString;

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

    // Synonyms table & columns
    @FXML
    private TableView synTable;
    @FXML
    private TableColumn<Synonym, String> synWord;
    @FXML
    private TableColumn<Synonym, String> synSyl;
    @FXML
    private TableColumn<Synonym, String> synMeter;
    @FXML
    private TableColumn<Synonym, String> synType;

    // Rhymes table & columns
    @FXML
    private TableView rhyTable;
    @FXML
    private TableColumn<Rhyme, String> rhyWord;
    @FXML
    private TableColumn<Rhyme, String> rhySyl;
    @FXML
    private TableColumn<Rhyme, String> rhyMeter;
    @FXML
    private TableColumn<Rhyme, String> rhyType;

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

        // Test printouts
        System.out.println(word.toString());
        System.out.println("word: "+word.getWord()+"\n"+"\tsynonyms: "+word.getSynonyms());

        // Reset controls
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
        typeString.setText(word.getType());
        // Set lists
//        rhymesList.getItems().setAll(word.getRhymes());
//        synonymsList.getItems().setAll(word.getSynonyms());
        // Set tables
        synWord.setCellValueFactory(new PropertyValueFactory<>("synonym"));
        synSyl.setCellValueFactory(new PropertyValueFactory<>("syllables"));
        synMeter.setCellValueFactory(new PropertyValueFactory<>("meter"));
        synType.setCellValueFactory(new PropertyValueFactory<>("pos"));
//        synTable.getItems().add(new Synonym(word.getWord()));
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
