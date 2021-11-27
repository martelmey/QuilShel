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
    private final String word;
    private final List<String> rhymes;
    private final List<String> syllables;
    private int syllablesCount;
    private List<String> synonyms;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Word(String word) throws IOException {
        this.word = word;
        String urlBase = "https://wordsapiv1.p.rapidapi.com/words/" + word;
        this.syllables = setSyllables(urlBase + "/syllables");
        this.rhymes = setRhymes(urlBase + "/rhymes");
        this.synonyms = setSynonyms(urlBase + "/synonyms");
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

    @Override
    public String toString() {
        return "Word {" +
                "\n\tword = " + word + "\n" +
                "\trhymes = " + rhymes + "\n" +
                "\tsyllables = " + syllables + "\n" +
                "\tsyllablesCount = " + syllablesCount + "\n" +
                "\tsynonyms = " + synonyms +
                "\n}";
    }
}
