package code.main;

import com.fasterxml.jackson.databind.JsonNode;
import gui.main.Main;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;

public class Synonym {
    private final String synonym;
    private final int syllables;
    private final String meter;
    private final String type;

    private final SimpleStringProperty synonymProperty;

    public Synonym(JsonNode result) throws IOException {
//        System.out.println("From Synonym.class: ");

        this.synonym = result.path("word").toString().replaceAll("\"", "");
        this.syllables = Main.setSyllablesCount(this.synonym);
        this.meter = Main.setMeter(this.synonym);
        this.type = Main.setType(this.synonym);

        this.synonymProperty = new SimpleStringProperty(this.synonym);
    }

    public String getSynonymProperty() {
        return synonymProperty.get();
    }

    public SimpleStringProperty synonymPropertyProperty() {
        return synonymProperty;
    }

    public String getSynonym() {
        return synonym;
    }

    public int getSyllables() {
        return syllables;
    }

    public String getMeter() {
        return meter;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
//        return this.synonym;
        return this.synonym + ", " +
                this.syllables + ", " +
                this.meter + ", " +
                this.type;
    }
}
