package code.main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gui.main.Main;
import okhttp3.OkHttpClient;
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

    private List<String> synonyms;
    private List<Synonym> synonyms2;

//    private List<String> meter;
    private String meter;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Word(String word) throws IOException {
        if (allLetters(word)) {
            if (word.length() > 1) {
                word = word.toLowerCase();
                if (isDictionaryWord(word)) {
                    this.word = word;
                    String urlBase_WordsAPI = "https://wordsapiv1.p.rapidapi.com/words/" + word;
//                    String urlBase_Datamuse = "https://api.datamuse.com/words?ml=" + word + "&qe=ml&md=r&max=5";

                    this.syllables = setSyllables(urlBase_WordsAPI + "/syllables");

                    this.rhymes = setRhymes("https://api.datamuse.com/words?rel_rhy=" + word);

                    this.synonyms = setSynonyms(urlBase_WordsAPI + "/synonyms");
                    this.synonyms2 = setSynonyms2("https://api.datamuse.com/words?rel_syn=" + word);

                    this.meter = Main.setMeter(word);
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
    private boolean allLetters(String word) {
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
        Response response = client.newCall(rhymeRequest).execute();
        JsonNode rootNode = objectMapper.readTree(response.body().string());
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
                Rhyme rhyme = new Rhyme(result, rhymeString);
                rhymes.add(rhyme);
            }
        }
        return rhymes;
    }

    public String setMeter(String word) throws IOException {
        Request request = new Request.Builder()
                .url("https://api.datamuse.com/words?ml=" + word + "&qe=ml&md=r&max=5")
                .build();
        Response response = client.newCall(request).execute();
        JsonNode rootNode = objectMapper.readTree(response.body().string());
        JsonNode resultOne = rootNode.path(0);
        JsonNode tagsNode = resultOne.path("tags");
        int proIndx = tagsNode.size()-1;
        JsonNode proNode = tagsNode.path(proIndx);
        String meterString = proNode.toString().replaceAll("[^\\d]", "");
        List<String> meterList = new ArrayList<>();
        for(int i = 0; i < meterString.length(); i++) {
            char charValue = meterString.charAt(i);
            int intValue = Character.getNumericValue(charValue);
            if(intValue==1) {
                meterList.add("/");
            } else {
                meterList.add("*");
            }
        }
        String meter = meterList.toString().replaceAll(",", "");
        meter = meter.replaceAll("\\[","");
        meter = meter.replaceAll("\\]","");
        meter = meter.replaceAll("\\s", "");
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
        for(int i = 0; i<synonymsNode.size(); i++) {
            String synonym = synonymsNode.path(i).toString();
            synonyms.add(synonym.replaceAll("\"", ""));
        }
        return synonyms;
    }

    public List<Synonym> setSynonyms2(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        JsonNode rootNode = objectMapper.readTree(response.body().string());
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

    public List<String> getSynonyms() {
        return synonyms;
    }

    public String getMeter() {
        return meter;
    }

    @Override
    public String toString() {
        return "Word {" +
                "\n\tword = " + word + "\n" +
                "\trhymes = " + rhymes + "\n" +
                "\tsyllables = " + syllables + "\n" +
                "\tsyllablesCount = " + syllablesCountInt + "\n" +
                "\tsynonyms = " + synonyms + "\n" +
                "\tmeter = " + meter +
                "\n}";
    }
}
