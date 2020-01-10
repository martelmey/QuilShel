package gui.main;

import code.main.Bar;
import code.main.Measure;
import code.main.Poem;
import code.main.Stanza;
import gui.main.datamodel.PoemData;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Controller {

    // Stages
    private Stage primaryStage = Main.getPrimaryStage();

    // Labels
    @FXML
    private Label titleLabel;

    // Menu
    @FXML
    private Menu mainMenu;
    @FXML
    private MenuItem mainMenuOpen;

    // ListViews
    @FXML
    private ListView<Stanza> stanzaListView;

    @FXML
    private ListView<Bar> barListView;

    @FXML
    private ListView<Measure> measureListView;

    // Data
    private PoemData data;

    // BorderPane
    @FXML
    private BorderPane startPage;

    public void load(Poem poem) {
        String footer = poem.getTitle() + " by " + poem.getAuthor();
        titleLabel.setText(footer);
        ObservableList<Stanza> stanzas = FXCollections.observableArrayList(data.getStanzas());
        stanzaListView.setItems(stanzas);

        stanzaListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Stanza>() {
            @Override
            public void changed(ObservableValue<? extends Stanza> observable, Stanza oldValue, Stanza newValue) {
                if (newValue != null) {
                    Stanza stanza = stanzaListView.getSelectionModel().getSelectedItem();
                    ObservableList<Bar> bars = FXCollections.observableArrayList(stanza.getBars());
                    barListView.setItems(bars);

                    barListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Bar>() {
                        @Override
                        public void changed(ObservableValue<? extends Bar> observable, Bar oldValue, Bar newValue) {
                            if (newValue != null) {
                                Bar bar = barListView.getSelectionModel().getSelectedItem();
                                ObservableList<Measure> measures = FXCollections.observableArrayList(bar.getMeasures());
                                measureListView.setItems(measures);
                            }
                        }
                    });
                }
            }
        });
    }

    public void initialize() throws IOException {
//        data = new PoemData();
//        Poem poem = data.loadPoem();
//        load(poem);
    }

    // Save - UNFINISHED
    @FXML
    public void showSaveWindow() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save poem.json File");
        fileChooser.showSaveDialog(primaryStage);
    }

    // Open - UNFINISHED
    @FXML
    public void showOpenPoemWindow() throws IOException {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open poem.json File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        File file = fileChooser.showOpenDialog(primaryStage);

        mainMenuOpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    try {
                        Poem poem = data.loadFile(file);
                        load(poem);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // New - DONE
    @FXML
    public void showNewPoemWindow() throws IOException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(startPage.getScene().getWindow());
        dialog.setTitle("New Blank Poem");
        dialog.setHeaderText("Enter a title and author to start a blank poem");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("gui.new.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            NewController controller = fxmlLoader.getController();
            Poem poem = controller.processResults();
            load(poem);
        }
    }

    @FXML
    public void handleExit() {
        Platform.exit();
    }
}
