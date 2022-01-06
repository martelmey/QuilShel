package gui.main;

import code.main.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML
    private TextField inputMeasure;
    @FXML
    private Button createMeasure;

    // Lists
    @FXML
    private ListView<String> rhymesList;
    @FXML
    private ListView<String> synonymsList;

    // Synonyms table & columns
    @FXML
    private TableView<Synonym> synTable;
    @FXML
    private TableColumn<Synonym, String> synWordColumn;
    @FXML
    private TableColumn<Synonym, Integer> synSylColumn;
    @FXML
    private TableColumn<Synonym, String> synMeterColumn;
    @FXML
    private TableColumn<Synonym, String> synTypeColumn;

    // Rhymes table & columns
    @FXML
    private TableView<Rhyme> rhyTable;
    @FXML
    private TableColumn<Rhyme, String> rhyWordColumn;
    @FXML
    private TableColumn<Rhyme, String> rhySylColumn;
    @FXML
    private TableColumn<Rhyme, String> rhyMeterColumn;
    @FXML
    private TableColumn<Rhyme, String> rhyTypeColumn;

    // Measures table & columns
    @FXML
    private TableView<Measure> mesTable;
    @FXML
    private TableColumn<Measure, String> mesSentenceColumn;
    @FXML
    private TableColumn<Measure, Integer> mesSylColumn;
    @FXML
    private TableColumn<Measure, String> mesMeterColumn;

    private Stanza stanza;

    @FXML
    public void initialize() {
        createWord.setDisable(true);
        // re-enable once isDictionaryWord() is improved
//        createMeasure.setDisable(true);
        this.stanza = new Stanza();
    }
    /**
     * onWordButtonClicked
     * create Word object
     * from contents of inputWord
     */
    @FXML
    public void onWordButtonClicked() throws IOException {
        /**
         * TESTS
         * 1. remove whitespace from inputWord.getText();
         */
        String s = inputWord.getText().replaceAll("\\s", "");
        Word word = new Word(s);

        // Test printouts
//        System.out.println(word.toString());
//        System.out.println("word: "+word.getWord()+"\n"+"\tsynonyms: "+word.getSynonyms());
//        System.out.println(word.getSynonyms().size());

        // Test recursion of Synonym objects
        ObservableList<Synonym> synTableData = FXCollections.observableArrayList();
        for(int i = 0; i<word.getSynonyms().size(); i++) {
            // Testing
//            String synonym = word.getSynonyms().get(i).getSynonym();
//            System.out.println(synonym);
            synWordColumn.setCellValueFactory(new PropertyValueFactory<Synonym, String>("synonymProperty"));
            synTableData.addAll(word.getSynonyms().get(i));
        }
        synTable.setItems(synTableData);

        // Test recursion of Rhyme objects
        ObservableList<Rhyme> rhyTableData = FXCollections.observableArrayList();
        for(int i = 0; i<word.getRhymes().size(); i++) {
            // Testing
//            String rhyme = word.getRhymes().get(i).getRhyme();
//            System.out.println("Rhyme: " + rhyme);
            rhyWordColumn.setCellValueFactory(new PropertyValueFactory<Rhyme, String>("rhymeProperty"));
            rhyTableData.addAll(word.getRhymes().get(i));
        }
        rhyTable.setItems(rhyTableData);

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

        // Set lists - legacy
//        rhymesList.getItems().setAll(word.getRhymes());
//        synonymsList.getItems().setAll(word.getSynonyms());

        // Synonym table
        synWordColumn.setCellValueFactory(new PropertyValueFactory<>("synonym"));
        synSylColumn.setCellValueFactory(new PropertyValueFactory<>("syllables"));
        synMeterColumn.setCellValueFactory(new PropertyValueFactory<>("meter"));
        synTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        // Rhyme table
        rhyWordColumn.setCellValueFactory(new PropertyValueFactory<>("rhyme"));
        rhySylColumn.setCellValueFactory(new PropertyValueFactory<>("syllables"));
        rhyMeterColumn.setCellValueFactory(new PropertyValueFactory<>("meter"));
        rhyTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    @FXML
    public void onMeasureButtonClicked() throws IOException {
        Measure measure = new Measure(inputMeasure.getText());
        this.stanza.appendMeasure(measure);
        this.stanza.printMeasures();

        ObservableList<Measure> mesTableData = FXCollections.observableArrayList();
        for(int i = 0; i<this.stanza.getMeasures().size(); i++) {
            String measureTest = this.stanza.getMeasures().get(i).getMeasureProperty();
            System.out.println("measureTest: "+measureTest);
//            mesSentenceColumn.setCellValueFactory(new PropertyValueFactory<Measure, String>("measureProperty"));
//            mesTableData.addAll(this.stanza.getMeasures().get(i));
        }
//        mesTable.setItems(mesTableData);

//        mesSentenceColumn.setCellValueFactory(new PropertyValueFactory<Measure, String>("measureProperty"));
//        mesTableData.addAll(measure);
//        mesTable.setItems(mesTableData);
    }

    /**
     * handleKeyReleased
     * enable createWord button
     * if inputWord field is not empty
     * enable createMeasure button
     * if inputMeasure has at least 1 dictionary word
     */
    @FXML
    public void handleKeyReleased() throws IOException {
        String wordText = inputWord.getText();
        boolean disableWordButton = wordText.isEmpty() || wordText.trim().isEmpty();
        createWord.setDisable(disableWordButton);

        // re-enable below
        // once improved Main.isDictionaryWord() is done
//        String measureText = inputMeasure.getText();
//        boolean disableMeasureButton =! Main.isDictionaryWord(measureText);
//        createMeasure.setDisable(disableMeasureButton);
    }
}
