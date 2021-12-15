package code.main;

import com.fasterxml.jackson.databind.JsonNode;
import gui.main.Main;

import java.io.IOException;

public class Synonym {
    private String synonym;
    private int syllables;
    private String meter;
    private String pos;

    public Synonym(JsonNode result) throws IOException {
//        System.out.println("From Synonym.class: ");

        this.synonym = result.path("word").toString().replaceAll("\"", "");
        this.syllables = Main.setSyllablesCount(this.synonym);
        this.meter = Main.setMeter(this.synonym);
        this.pos = Main.setPOS(this.synonym);

//        System.out.println("\tthis.synonym: "+this.synonym);
//        System.out.println("\tthis.syllables: "+this.syllables);
//        System.out.println("\tmeter: "+this.meter+"\n");
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

    public String getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return this.synonym;
    }
}
