package code.main;

import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Measure {
    private final String sentence;
    private int syllables = 0;
    private  String meter;

    private final SimpleStringProperty measureProperty;

//    private final SimpleStringProperty measureProperty;
    private List<Word> words = new ArrayList<>();

    public Measure(String sentence) throws IOException {
        System.out.println("\tfrom Measure.class:");

        this.sentence = sentence;

        // populate List<Word>
        String[] wordList = this.sentence.split("\\s+");
        for (String s : wordList) {
            Word word = new Word(s);
            this.words.add(word);

//            System.out.println("Word " + count + ": " + s);
        }

        // iterate List<Word>, increment this.syllables per Word
        for(int i = 0; i< Objects.requireNonNull(this.words).size(); i++) {
            this.syllables += this.words.get(i).getSyllablesCountInt();
        }

        // iterate List<Word>, get Measure fields
//        int count = 1;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i< Objects.requireNonNull(this.words).size(); i++) {
//            String m = this.words.get(i).getMeter();
            sb.append(this.words.get(i).getMeter()).append(" ");

//            System.out.println("Meter for word " + count + ": " + m);
//            System.out.println("Meter string is now: " + sb + "\n");
//            count+=1;
            this.meter = sb.toString();
        }

//        System.out.println("Measure: "+this.sentence + "\n" +
//                "Syllables: "+this.syllables + "\n" +
//                "Meter: "+this.meter);

        this.measureProperty = new SimpleStringProperty(this.sentence);
    }


    public SimpleStringProperty measurePropertyProperty() {
        return measureProperty;
    }

    private List<String> pullWords(String sentence) {
        List<String> words = new ArrayList<>();
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(sentence);
        int lastIndex = breakIterator.first();

        while (BreakIterator.DONE != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();

            if (lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(sentence.charAt(firstIndex))) {
                words.add(sentence.substring(firstIndex, lastIndex));
            }
        }
        return words;
    }

    private void fetchSemantics() {
        String url = "https://wordsapiv1.p.rapidapi.com/words/";
        List<String> pulledWords = pullWords(sentence);

        for (String word : pulledWords) {

        }
    }

    public String getSentence() {
        return sentence;
    }

    public int getSyllables() {
        return syllables;
    }

    public String getMeter() {
        return meter;
    }

    public String getMeasureProperty() {
        return measureProperty.get();
    }

    public List<Word> getWords() {
        return words;
    }

    @Override
    public String toString() {
        return "Measure{" +
                "sentence='" + sentence + '\'' +
                ", syllables=" + syllables +
                ", meter='" + meter + '\'' +
                ", measureProperty=" + measureProperty +
                ", words=" + words +
                '}';
    }
}
