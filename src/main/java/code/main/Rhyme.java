package code.main;

import com.fasterxml.jackson.databind.JsonNode;
import gui.main.Main;

import java.io.IOException;

public class Rhyme {
    private String rhyme;
    private int syllables;
    private int score;
    private String meter;

    // future: declare noun, verb, etc ... for sorting & filtering
    private String part;

    public Rhyme(JsonNode result, String rhymeString) throws IOException {
//        System.out.println("From Rhyme.class:");

        this.rhyme = rhymeString;

//        System.out.println("\trhyme: "+this.rhyme);

        JsonNode scoreNode = result.path("score");
        this.score = Integer.parseInt(scoreNode.toString());
//        System.out.println("\tscore: "+this.score);

//        this.syllables = Main.setSyllablesCount(this.rhyme);

//        System.out.println("\tsyllables: "+this.syllables);

        this.meter = Main.setMeter(this.rhyme);

//        System.out.println("\tmeter: "+this.meter);
//        System.out.println(toString());
    }

    @Override
    public String toString() {
        return rhyme;
    }
}
