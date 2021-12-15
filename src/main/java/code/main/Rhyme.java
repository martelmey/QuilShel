package code.main;

import com.fasterxml.jackson.databind.JsonNode;
import gui.main.Main;

import java.io.IOException;

public class Rhyme {
    private String rhyme;
    private int syllables;
    private int score;
    private String meter;
    private String pos;

    public Rhyme(JsonNode result, String rhymeString) throws IOException {
//        System.out.println("From Rhyme.class:");

        this.rhyme = rhymeString;

        this.syllables = Main.setSyllablesCount(this.rhyme);
//        System.out.println("\trhyme: "+this.rhyme);

        JsonNode scoreNode = result.path("score");
        this.score = Integer.parseInt(scoreNode.toString());
//        System.out.println("\tscore: "+this.score);

//        this.syllables = Main.setSyllablesCount(this.rhyme);

//        System.out.println("\tsyllables: "+this.syllables);

        this.meter = Main.setMeter(this.rhyme);
        this.pos = Main.setPOS(this.rhyme);

//        System.out.println("\tmeter: "+this.meter);
//        System.out.println(toString());
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

    public String getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return rhyme;
    }
}
