package code.main;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Measure {
    private final String sentence;
    private int syllables = 0;
    private String meter;

    private List<Word> words = new ArrayList<>();

    private final SimpleStringProperty simpleSentence;
    private final SimpleIntegerProperty simpleSyllables;
    private final SimpleStringProperty simpleMeter;

    public Measure(String sentence) throws IOException {
//        System.out.println("\tfrom Measure.class:");

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

        this.simpleSentence = new SimpleStringProperty(this.sentence);
        this.simpleSyllables = new SimpleIntegerProperty(this.syllables);
        this.simpleMeter = new SimpleStringProperty(this.meter);

        // Test printouts
//        System.out.println("simpleSentence: "+this.simpleSentence);
//        System.out.println("simpleSyllables: "+this.simpleSyllables);
//        System.out.println("simpleMeter: "+this.simpleMeter);
    }

    public String getSimpleSentence() {
        return simpleSentence.get();
    }

    public SimpleStringProperty simpleSentenceProperty() {
        return simpleSentence;
    }

    public int getSimpleSyllables() {
        return simpleSyllables.get();
    }

    public SimpleIntegerProperty simpleSyllablesProperty() {
        return simpleSyllables;
    }

    public String getSimpleMeter() {
        return simpleMeter.get();
    }

    public SimpleStringProperty simpleMeterProperty() {
        return simpleMeter;
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

    public List<Word> getWords() {
        return words;
    }

    @Override
    public String toString() {
        return "Measure{" +
                "sentence='" + sentence + '\'' +
                ", syllables=" + syllables +
                ", meter='" + meter + '\'' +
                ", measureProperty=" + simpleSentence +
                ", words=" + words +
                '}';
    }
}
