package code.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class Word {
    private String word;
    private Syllables syllables;
    private Rhymes rhymes;

    /**
     * mode = 0 is syllables
     * mode = 1 is rhymes
     */

    public Word(String word, int mode) throws IOException {
        this.word = word;
        String urlBase = "https://wordsapiv1.p.rapidapi.com/words/" + word;
        String urlFinal;
        switch(mode) {
            case 0:
                urlFinal = urlBase + "/syllables";
                this.syllables = setSyllables(word, urlFinal);
                break;
            case 1:
                urlFinal = urlBase + "/rhymes";
                this.rhymes = setRhymes(word, urlFinal);
                break;
        }
    }

    public Syllables setSyllables(String word, String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e852927068mshaf1458fd33faf58p1c06fcjsn9a05d5c4c695")
                .build();
        Response response = client.newCall(request).execute();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body().string(), Syllables.class);
    }

    public Rhymes setRhymes(String word, String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e852927068mshaf1458fd33faf58p1c06fcjsn9a05d5c4c695")
                .build();
        Response response = client.newCall(request).execute();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body().string(), Rhymes.class);
    }

    public String getSyllables() {
        return syllables.toString();
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", syllables=" + syllables +
                '}';
    }
}
