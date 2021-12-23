package code.main;

import com.fasterxml.jackson.databind.JsonNode;
import gui.main.Main;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class Word {
    private String word;

    private List<Rhyme> rhymes;

    private List<String> syllables;

    // Set by constructor, not Main.setSyllablesCount
    // for GUI Label restrictions
    private String syllablesCountString;
    private int syllablesCountInt;

//    private List<String> synonyms;
    private List<Synonym> synonyms;

//    private List<String> meter;
    private String meter;

    private String type;

    public Word(String word) throws IOException {
        if (Main.allLetters(word)) {
            if (word.length() > 1) {
                word = word.toLowerCase();
                if (Main.isDictionaryWord(word)) {
                    this.word = word;
                    this.syllables = setSyllables(Main.URL_BASE_WORDS_API + word + Main.END_WORDS_API_SYLLABLES);
//                    this.syllablesCountInt = Main.setSyllablesCount(word);

                    this.rhymes = setRhymes(Main.URL_BASE_DATAMUSE + Main.END_DATAMUSE_RHYMES + word);

//                    this.synonyms = setSynonyms(urlBase_WordsAPI + "/synonyms");
                    this.synonyms = setSynonyms(Main.URL_BASE_DATAMUSE + Main.END_DATAMUSE_SYNONYMS + word);

                    this.meter = Main.setMeter(word);
                    this.type = Main.setType(word);
                }
            }
        }
    }

    // required for Label syllablesCount String req'
    // cannot use Main.getSyllables(): it returns int
    private List<String> setSyllables(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader(Main.HEAD_RAPID_HOST, Main.HEAD_WORDS_API)
                .addHeader(Main.HEAD_RAPID_KEY, Main.HEAD_WORDS_API_KEY)
                .build();
        Response response = Main.CLIENT.newCall(request).execute();
        JsonNode rootNode = Main.MAPPER.readTree(response.body().string());
        JsonNode syllablesNode = rootNode.path("syllables");
        JsonNode countNode = syllablesNode.path("count");

        /**
         * TESTING
         * int syllablesCount
         * - or -
         * String syllablesCount
         */
        this.syllablesCountString = countNode.asText();
        this.syllablesCountInt = countNode.asInt();

        JsonNode listNode = syllablesNode.path("list");
        List<String> syllables = new ArrayList<>();
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(listNode.toString());
        int lastIndex = breakIterator.first();
        while(BreakIterator.DONE != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();
            if(lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(listNode.toString().charAt(firstIndex))) {
                syllables.add(listNode.toString().substring(firstIndex, lastIndex));
            }
        }
        return syllables;
    }

    public List<Rhyme> setRhymes(String url) throws IOException {
        Request rhymeRequest = new Request.Builder()
                .url(url)
                .build();
        Response response = Main.CLIENT.newCall(rhymeRequest).execute();
        JsonNode rootNode = Main.MAPPER.readTree(response.body().string());
        List<Rhyme> rhymes = new ArrayList<>();
        for(int i = 0; i<rootNode.size(); i++) {
            JsonNode result = rootNode.path(i);
            JsonNode rhymeNode = result.path("word");
            String rhymeString = rhymeNode.toString().replaceAll("\"", "");
            if(rhymeString.contains(" ")) {
                break;
            } else if (rhymeString.length()<2) {
                break;
            } else {
                Rhyme rhyme = new Rhyme(result);
//                Rhyme rhyme = new Rhyme(result, rhymeString);
                rhymes.add(rhyme);
            }
        }
        return rhymes;
    }

    public List<Synonym> setSynonyms(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = Main.CLIENT.newCall(request).execute();
        JsonNode rootNode = Main.MAPPER.readTree(response.body().string());
        List<Synonym> synonyms = new ArrayList<>();
        for(int i = 0; i<rootNode.size(); i++) {
            JsonNode result = rootNode.path(i);
            Synonym synonym = new Synonym(result);
            synonyms.add(synonym);
        }
        return synonyms;
    }

    public String getWord() {
        return word;
    }

    public List<Rhyme> getRhymes() {
        return rhymes;
    }

    public List<String> getSyllables() {
        return syllables;
    }

    /**
     * TESTING
     * int syllablesCount
     * - or -
     * String syllablesCount
     */
    public int getSyllablesCountInt() {
        return syllablesCountInt;
    }
    public String getSyllablesCountString() {
        return syllablesCountString;
    }

    public List<Synonym> getSynonyms() {
        return synonyms;
    }

    public String getMeter() {
        return meter;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Word {" +
                "\n\tword = " + word + "\n" +
                "\trhymes = " + rhymes + "\n" +
//                "\tsyllables = " + syllables + "\n" +
                "\tsyllablesCount = " + syllablesCountInt + "\n" +
                "\tsynonyms = " + synonyms + "\n" +
                "\tmeter = " + meter + "\n" +
                "\tpart of speech = " + type +
                "\n}";
    }
}
