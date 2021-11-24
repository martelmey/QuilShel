package code.main;

import java.util.Arrays;

public class Syllables {
    private String word;
    private int count;
    private String[] list;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Syllables{" +
                "word='" + word + '\'' +
                ", count=" + count +
                ", list=" + Arrays.toString(list) +
                '}';
    }
}
