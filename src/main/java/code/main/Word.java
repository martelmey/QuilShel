package code.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class Word {
    private String word;
    private int syllables;

    public Response getSyllables() throws IOException {
        String url =
                "https://wordsapiv1.p.rapidapi.com/words/"
                        + word +
                        "/syllables"
                ;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "wordsapiv1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e852927068mshaf1458fd33faf58p1c06fcjsn9a05d5c4c695")
                .build();

        Response response = client.newCall(request).execute();

        return response;
    }

//    public String getUrl() {
//        return url;
//    }

//    public Response getResponse() {
//        return response;
//    }

    public Word(String word) throws IOException {
        this.word = word;

    }

    public String getWord() {
        return word;
    }
}