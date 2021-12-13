package code.main;

import com.fasterxml.jackson.databind.JsonNode;
import gui.main.Main;

import java.io.IOException;

public class Synonym {
    private String synonym;
    private int syllables;
    private String meter;

    // future: declare noun, verb, etc ... for sorting & filtering
    private String part;

    public Synonym(JsonNode result) throws IOException {
//        System.out.println("From Synonym.class: ");

        this.synonym = result.path("word").toString().replaceAll("\"", "");
        this.syllables = Main.setSyllablesCount(this.synonym);
        this.meter = Main.setMeter(this.synonym);

//        System.out.println("\tthis.synonym: "+this.synonym);
//        System.out.println("\tthis.syllables: "+this.syllables);
//        System.out.println("\tmeter: "+this.meter);
    }

    @Override
    public String toString() {
        return this.synonym;
    }
}
