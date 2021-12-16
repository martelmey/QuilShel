package code.main;

import com.fasterxml.jackson.databind.JsonNode;
import gui.main.Main;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;

public class Rhyme {
    private final String rhyme;
    private final int syllables;
    private final String meter;
    private final String type;

    private final SimpleStringProperty rhymeProperty;

//    public Rhyme(JsonNode result, String rhymeString) throws IOException {
    public Rhyme(JsonNode result) throws IOException {
//        System.out.println("From Rhyme.class:");

        this.rhyme = result.path("word").toString().replaceAll("\"", "");
//        this.rhyme = rhymeString;
        this.syllables = Main.setSyllablesCount(this.rhyme);
        this.meter = Main.setMeter(this.rhyme);
        this.type = Main.setType(this.rhyme);

        this.rhymeProperty = new SimpleStringProperty(this.rhyme);
    }

    public String getRhymeProperty() {
        return rhymeProperty.get();
    }

    public SimpleStringProperty rhymePropertyProperty() {
        return rhymeProperty;
    }

    public String getRhyme() {
        return rhyme;
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
//        return this.rhyme;
        return this.rhyme + ", " +
                this.syllables + ", " +
                this.meter +  ", " +
                this.type;
    }
}
