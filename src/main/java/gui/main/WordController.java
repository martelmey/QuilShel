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
    private TableView<Measure> mesTable = new TableView<Measure>();
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
//            String synonymTest = word.getSynonyms().get(i).getSynonymProperty();
//            System.out.println("\tsynonymTest:"+synonymTest);
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
        // add new Measure to Stanza
        Measure measure = new Measure(inputMeasure.getText());
        this.stanza.appendMeasure(measure);

        // Test printouts of Stanza
//        this.stanza.printMeasures();

        // create ObservableList
        ObservableList<Measure> mesTableData = FXCollections.observableArrayList();
        int count = 1;

        // iterate over stanza, add each Measure in Stanza to ObservableList
        for(int i = 0; i<this.stanza.getMeasures().size(); i++) {
            // add each Measure to ObservableList
            mesTableData.addAll(this.stanza.getMeasures().get(i));

            // column set try 1
//            mesSentenceColumn.setCellValueFactory(new PropertyValueFactory<>("simpleSentence"));
//            mesSylColumn.setCellValueFactory(new PropertyValueFactory<>("simpleSyllables"));
//            mesMeterColumn.setCellValueFactory(new PropertyValueFactory<>("simpleMeter"));

            count+=1;
        }

        // Test printouts of ObservableList
        int count2 = 1;
        for(int i = 0; i<mesTableData.size(); i++) {
            String testSimpleSentence = mesTableData.get(i).getSimpleSentence();
            int testSimpleSyllables = mesTableData.get(i).getSimpleSyllables();
            String testSimpleMeter = mesTableData.get(i).getSimpleMeter();

            System.out.println("\tmesTableData: " + "size=" + mesTableData.size() + "\n" +
                    "\titem #" + count2 + ": " + "\n" +
                    "simpleSentence: " + testSimpleSentence + "\n" +
                    "simpleSyllables: " + testSimpleSyllables + "\n" +
                    "simpleMeter: " + testSimpleMeter + "\n");

            count2+=1;
        }

        // list of Measures applied to table
        mesTable.setItems(mesTableData);

        // column set try 2
        mesSentenceColumn.setCellValueFactory(new PropertyValueFactory<>("simpleSentence"));
        mesSylColumn.setCellValueFactory(new PropertyValueFactory<>("simpleSyllables"));
        mesMeterColumn.setCellValueFactory(new PropertyValueFactory<>("simpleMeter"));

        // reset controls
        inputMeasure.clear();
//        createMeasure.setDisable(true);
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
