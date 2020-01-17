package code.main;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public class Poem {
    private String title;
    private String author;
    private List<Stanza> stanzas = new ArrayList<>();

    public Poem() {

    }

    public Poem(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Stanza> getStanzas() {
        return stanzas;
    }

    public void setStanzas(List<Stanza> stanzas) {
        this.stanzas = stanzas;
    }

    @Override
    public String toString() {
        return "Poem{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", stanzas=" + stanzas +
                '}';
    }
}
