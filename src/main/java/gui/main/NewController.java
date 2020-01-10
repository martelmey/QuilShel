package gui.main;

import code.main.Poem;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class NewController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    public Poem processResults() throws IOException {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();

        Poem poem = new Poem(title, author);
        return poem;
    }
}
