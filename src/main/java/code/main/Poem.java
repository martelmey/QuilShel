package code.main;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public class Poem {
    private String title;
    private String author;
    private List<Stanza> stanzas = new ArrayList<>();

    private SimpleStringProperty simpleTitleProperty = new SimpleStringProperty("");
    private SimpleStringProperty simpleAuthorProperty = new SimpleStringProperty("");
    private SimpleListProperty<Stanza> simpleStanzaProperty = new SimpleListProperty<>();

    public Poem() {

    }

    public Poem(String title, String author) {
        this.title = title;
        this.author = author;

        this.simpleTitleProperty.set(title);
        this.simpleAuthorProperty.set(author);
    }

    public String getTitle() {
        return title;
    }

    public SimpleStringProperty getSimpleTitleProperty() {
        return simpleTitleProperty;
    }

    public void setTitle(String title) {
        this.title = title;
        this.simpleTitleProperty.set(title);
    }

    public String getAuthor() {
        return author;
    }

    public SimpleStringProperty getSimpleAuthorProperty() {
        return simpleAuthorProperty;
    }

    public void setAuthor(String author) {
        this.author = author;
        this.simpleAuthorProperty.set(author);
    }

    public List<Stanza> getStanzas() {
        return stanzas;
    }

    public SimpleListProperty<Stanza> getSimpleStanzaProperty() {
        return simpleStanzaProperty;
    }

    public void setStanzas(List<Stanza> stanzas) {
        this.stanzas = stanzas;
    }

//    public void setStanzas(ObservableList<Stanza> stanzas) {
//        this.simpleStanzaProperty.set(stanzas);
//    }

    @Override
    public String toString() {
        return "Poem{" +
                "simpleTitleProperty=" + simpleTitleProperty +
                ", simpleAuthorProperty=" + simpleAuthorProperty +
                ", simpleStanzaProperty=" + simpleStanzaProperty +
                '}';
    }
}
