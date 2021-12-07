package gui.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
}
