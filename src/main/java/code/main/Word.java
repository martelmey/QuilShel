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
    private String syllablesCount;
//    private int syllablesCount;
    private List<String> synonyms;
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
//                    this.meter = setMeter(urlBase_Datamuse);
                    this.meter = setMeter(word);
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
        this.syllablesCount = countNode.asText();
//        this.syllablesCount = countNode.asInt();
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
        Request rhymeRequest = new Request.Builder()
                .url(url)
                .build();
        Response rhymeResponse = client.newCall(rhymeRequest).execute();
        JsonNode rhymeRootNode = objectMapper.readTree(rhymeResponse.body().string());
        List<String> rhymes = new ArrayList<>();
        for(int i = 0; i<rhymeRootNode.size(); i++) {
            // Get nodes per result
            JsonNode result = rhymeRootNode.path(i);
            JsonNode rhymeNode = result.path("word");
            JsonNode scoreNode = result.path("score");
            JsonNode syllablesNode = result.path("numSyllables");
            String rhyme = rhymeNode.toString();
            rhyme = rhyme.replaceAll("\"", ""); // rhyme
            // skip rhyme with spaces
            if(rhyme.contains(" ")) {
                break;
            } else {
                String syllablesString = syllablesNode.toString();
                int syllables = Integer.parseInt(syllablesString); // syllables
                int length = rhyme.length(); // length
                String scoreString = scoreNode.toString();
                int score = Integer.parseInt(scoreString); // score
                // meter filter - future use
//            String meter = setMeter(rhyme);
                if (length > 1) {
                    rhymes.add(rhyme);
                }
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

    public String getWord() {
        return word;
    }

    public List<String> getRhymes() {
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
//    public int getSyllablesCount() {
//        return syllablesCount;
//    }
    public String getSyllablesCount() {
        return syllablesCount;
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
                "\tsyllablesCount = " + syllablesCount + "\n" +
                "\tsynonyms = " + synonyms + "\n" +
                "\tmeter = " + meter +
                "\n}";
    }
}
