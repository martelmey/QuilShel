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
    private List<String> rhymes = new ArrayList<>();

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Word(String word, int mode) throws IOException {
        this.word = word;
        String urlBase = "https://wordsapiv1.p.rapidapi.com/words/" + word;
        String urlFinal;
        switch(mode) {
            case 0:
                urlFinal = urlBase + "/syllables";
                //this.syllables = setSyllables(word, urlFinal);
                break;
            case 1:
                urlFinal = urlBase + "/rhymes";
                this.rhymes = setRhymes(word, urlFinal);
                break;
        }
    }

    public Syllables setSyllables(String word, String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e852927068mshaf1458fd33faf58p1c06fcjsn9a05d5c4c695")
                .build();
        Response response = client.newCall(request).execute();
        return objectMapper.readValue(response.body().string(), Syllables.class);
    }

    public List<String> setRhymes(String word, String url) throws IOException {
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

    public List<String> getRhymes() {
        return rhymes;
    }

    //    public String getSyllables() {
//        return syllables.toString();
//    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

//    @Override
//    public String toString() {
//        return "Word{" +
//                "word='" + word + '\'' +
//                ", syllables=" + syllables +
//                '}';
//    }
}
