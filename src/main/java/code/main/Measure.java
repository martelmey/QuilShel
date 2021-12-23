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
//    private final String meter;

//    private final SimpleStringProperty measureProperty;
    private List<Word> words = new ArrayList<>();

    public Measure(String sentence) throws IOException {
        System.out.println("\tfrom Measure.class:");

        this.sentence = sentence;
        // populate List<Word>
        String[] wordList = this.sentence.split("\\s+");
        for (String s : wordList) {
            Word word = new Word(s);
//            System.out.println(word);
            this.words.add(word);
        }
        // iterate List<Word>, get Measure fields
        for(int i = 0; i< Objects.requireNonNull(this.words).size(); i++) {
            this.syllables += this.words.get(i).getSyllablesCountInt();
//            System.out.println(this.words.get(i));
        }
        System.out.println(this.syllables);
//        System.out.println("Measure constructor: "+this.sentence);
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

    @Override
    public String toString() {
        return sentence;
    }
}
