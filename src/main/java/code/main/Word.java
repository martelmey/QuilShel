package code.main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class Word {
    private String word;
    private List<String> rhymes;
    private List<String> syllables;
    private int syllablesCount;
    private List<String> synonyms;
    private List<String> meter;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Word(String word) throws IOException {
        if (isLetter(word)) {
            if (word.length() > 1) {
                word = word.toLowerCase();
                if (isDictionaryWord(word)) {
                    this.word = word;
                    String urlBase_WordsAPI = "https://wordsapiv1.p.rapidapi.com/words/" + word;
                    String urlBase_Datamuse = "https://api.datamuse.com/words?ml=" + word + "&qe=ml&md=r&max=5";
                    this.syllables = setSyllables(urlBase_WordsAPI + "/syllables");
                    this.rhymes = setRhymes(urlBase_WordsAPI + "/rhymes");
                    this.synonyms = setSynonyms(urlBase_WordsAPI + "/synonyms");
                    this.meter = setMeter(urlBase_Datamuse);
                }
            }
        }
    }

    // test
    private boolean isDictionaryWord(String word) throws IOException {
        Request request = new Request.Builder()
                .url("https://wordsapiv1.p.rapidapi.com/words/" + word)
                .get()
                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e852927068mshaf1458fd33faf58p1c06fcjsn9a05d5c4c695")
                .build();
        Response response = client.newCall(request).execute();
        JsonNode rootNode = objectMapper.readTree(response.body().string());
        JsonNode wordNode = rootNode.path("word");
        String compareString = wordNode.toString();
        compareString = compareString.replaceAll("\"", "");
        return compareString.equals(word);
    }
    // test
    private boolean isLetter(String word) {
        char[] letters = word.toCharArray();
        for(char c : letters) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    private List<String> setSyllables(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e852927068mshaf1458fd33faf58p1c06fcjsn9a05d5c4c695")
                .build();
        Response response = client.newCall(request).execute();
        JsonNode rootNode = objectMapper.readTree(response.body().string());
        JsonNode syllablesNode = rootNode.path("syllables");
        JsonNode countNode = syllablesNode.path("count");
        this.syllablesCount = countNode.asInt();
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

    public List<String> setRhymes(String url) throws IOException {
        /**
         * TESTS
         * 1. top results only to avoid inaccuracies
         * 2. dedup
         * 3. check score of each rhyme, cut rares
         */
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e852927068mshaf1458fd33faf58p1c06fcjsn9a05d5c4c695")
                .build();
        Response response = client.newCall(request).execute();
        JsonNode rootNode = objectMapper.readTree(response.body().string());
        JsonNode rhymesNode = rootNode.path("rhymes");
        JsonNode allNode = rhymesNode.path("all");
        List<String> rhymes = new ArrayList<>();
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(allNode.toString());
        int lastIndex = breakIterator.first();
        while(BreakIterator.DONE != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();
            if(lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(allNode.toString().charAt(firstIndex))) {
                rhymes.add(allNode.toString().substring(firstIndex, lastIndex));
            }
        }
        return rhymes;
    }

    public List<String> setMeter(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        JsonNode rootNode = objectMapper.readTree(response.body().string());
        JsonNode resultOne = rootNode.path(0);
        JsonNode tagsNode = resultOne.path("tags");
        int proIndx = tagsNode.size()-1;
        JsonNode proNode = tagsNode.path(proIndx);
        String meterString = proNode.toString().replaceAll("[^\\d]", "");
        List<String> meter = new ArrayList<>();
        for(int i = 0; i < meterString.length(); i++) {
            char charValue = meterString.charAt(i);
            int intValue = Character.getNumericValue(charValue);
            if(intValue==1) {
                meter.add("/");
            } else {
                meter.add("*");
            }
        }
        return meter;
    }

    public List<String> setSynonyms(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e852927068mshaf1458fd33faf58p1c06fcjsn9a05d5c4c695")
                .build();
        Response response = client.newCall(request).execute();
        JsonNode rootNode = objectMapper.readTree(response.body().string());
        JsonNode synonymsNode = rootNode.path("synonyms");
        List<String> synonyms = new ArrayList<>();
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(synonymsNode.toString());
        int lastIndex = breakIterator.first();
        while(BreakIterator.DONE != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();
            if(lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(synonymsNode.toString().charAt(firstIndex))) {
                synonyms.add(synonymsNode.toString().substring(firstIndex, lastIndex));
            }
        }
        return synonyms;
    }

    public String getWord() {
        return word;
    }

    public List<String> getRhymes() {
        return rhymes;
    }

    public List<String> getSyllables() {
        return syllables;
    }

    public int getSyllablesCount() {
        return syllablesCount;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public List<String> getMeter() {
        return meter;
    }

    @Override
    public String toString() {
        return "Word {" +
                "\n\tword = " + word + "\n" +
                "\trhymes = " + rhymes + "\n" +
                "\tsyllables = " + syllables + "\n" +
                "\tsyllablesCount = " + syllablesCount + "\n" +
                "\tsynonyms = " + synonyms + "\n" +
                "\tmeter = " + meter +
                "\n}";
    }
}
