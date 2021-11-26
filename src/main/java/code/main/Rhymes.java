package code.main;

public class Rhymes {
    private String word;
    private String[] all;

    public Rhymes() {
    }

    public Rhymes(String word, String[] all) {
        this.word = word;
        this.all = all;
    }

    public String[] getAll() {
        return all;
    }

    public void setAll(String[] all) {
        this.all = all;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
