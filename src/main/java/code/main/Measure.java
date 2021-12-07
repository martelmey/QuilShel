package code.main;

import javafx.beans.property.SimpleStringProperty;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class Measure {
    private String sentence;

    public Measure() {

    }

    public Measure(String sentence) {
        this.sentence = sentence;
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

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    @Override
    public String toString() {
        return sentence;
    }
}
