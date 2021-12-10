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

public class Test {

    /**
     * Check diff w/ Word.class
     * update if needed
     * for accurate environment
     */

    private String word;
    private List<String> rhymes;
    private List<String> syllables;
    private int syllablesCount;
    private List<String> synonyms;
    private List<String> meter;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Test(String word) throws IOException {
        /**
         * TESTS
         * 1. letters only [done]
         * 2. >=2 letters [done]
         * 3. make lowercase [done]
         * 4. is dictionary word [done]
         */
        if (allLetters(word)) {
            if (word.length() > 1) {
                word = word.toLowerCase();
                if (isDictionaryWord(word)) {
                    this.word = word;
                    String urlBase_WordsAPI = "https://wordsapiv1.p.rapidapi.com/words/" + word;
                    String urlBase_Datamuse = "https://api.datamuse.com/words?ml=" + word + "&qe=ml&md=r&max=5";
                    this.syllables = setSyllables(urlBase_WordsAPI + "/syllables");

                    // testing: filter rhymes results
                    this.rhymes = setRhymes(urlBase_WordsAPI + "/rhymes");
                    setRhymes2("https://api.datamuse.com/words?rel_rhy=" + word);

                    this.synonyms = setSynonyms(urlBase_WordsAPI + "/synonyms");
                    this.meter = setMeter(urlBase_Datamuse);
                }
            }
        }
    }

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

    public void setRhymes2(String url) throws IOException {
        /**
         * 1. hi-score only [done]
         * 2. cannot have spaces or symbols [done]
         * 3. .length() > 1 [done]
         * 4. dedup
         */
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        JsonNode rootNode = objectMapper.readTree(response.body().string());

        List<String> rhymes = new ArrayList<>();

        for(int i = 0; i<2; i++) {
            // Get fields
            JsonNode result = rootNode.path(i);
            JsonNode rhymeNode = result.path("word");
            JsonNode scoreNode = result.path("score");
            JsonNode syllablesNode = result.path("numSyllables");

            // Printout {nodes}
//            System.out.println(result);
//            System.out.println(rhymeNode);
//            System.out.println(scoreNode);
//            System.out.println(syllablesNode);

            // Tests
            String rhyme = rhymeNode.toString();
            rhyme = rhyme.replaceAll("\"", "");
            String scoreString = scoreNode.toString();
            int score = Integer.parseInt(scoreString);
            String syllablesString = syllablesNode.toString();
            int syllables = Integer.parseInt(syllablesString);
            int length = rhyme.length();

            // Printout {tests}
            System.out.println(rhyme);
            System.out.println(score);
            System.out.println(syllables);
            System.out.println("length of rhyme: " + length);

            /**
             * Score filter
             * May need further adjustment
             * as Measure.class is developed
             */
            if(score > 100 && length > 1) {
                rhymes.add(rhyme.replaceAll("\\s", ""));
            }

        }
        // Printout {return}
        System.out.println(rhymes);

//        System.out.println(rootNode.size());
//        JsonNode resultOne = rootNode.path(0);
//        JsonNode score = resultOne.path("score");

//        System.out.println("dataMuse: " + rootNode);
//        System.out.println("dataMuse: " + resultOne);
//        System.out.println(score);
    }

    public List<String> setRhymes(String url) throws IOException {
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

//        System.out.println("wordsAPI: " + allNode);

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

    public List<String> getRhymes() {
        return rhymes;
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
