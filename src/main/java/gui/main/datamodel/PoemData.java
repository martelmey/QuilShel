package gui.main.datamodel;

import code.main.Bar;
import code.main.Measure;
import code.main.Poem;
import code.main.Stanza;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.collections.FXCollections;

import java.io.*;
import java.util.List;

public class PoemData {
    private static PoemData instance = new PoemData();

    private static final String FILENAME = "poem.json";
    private String title = "title";
    private String author = "author";

    private List<Stanza> stanzas;
    private List<Bar> bars;
    private List<Measure> measures;

    // Static instance
    public static PoemData getInstance() {
        return instance;
    }

    // Constructor
    public PoemData() {
        stanzas = FXCollections.observableArrayList();
        bars = FXCollections.observableArrayList();
        measures = FXCollections.observableArrayList();
    }

    // Methods
    public Poem loadPoem() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream fileInputStream = new FileInputStream(FILENAME);
        Poem poem = mapper.readValue(fileInputStream, Poem.class);
        fileInputStream.close();
        this.title = poem.getTitle();
        this.author = poem.getAuthor();
        this.stanzas = poem.getStanzas();
        return poem;
    }

    public Poem loadFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream fileInputStream = new FileInputStream(file);
        Poem poem = mapper.readValue(fileInputStream, Poem.class);
        fileInputStream.close();
        this.title = poem.getTitle();
        this.author = poem.getAuthor();
        this.stanzas = poem.getStanzas();
        return poem;
    }

    public void savePoem(Poem poem) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String json = mapper.writeValueAsString(poem);
        FileOutputStream fileOutputStream = new FileOutputStream(FILENAME);
        mapper.writeValue(fileOutputStream, json);
        fileOutputStream.close();
    }

    // Getters & Setters

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public List<Stanza> getStanzas() {
        return stanzas;
    }

    public List<Bar> getBars() {
        return bars;
    }

    public List<Measure> getMeasures() {
        return measures;
    }

    public void addStanza(Stanza stanza) {
        stanzas.add(stanza);
    }

    public void deleteStanza(Stanza stanza) {
        stanzas.remove(stanza);
    }

    public void addBar(Bar bar) {
        bars.add(bar);
    }

    public void deleteBar(Bar bar) {
        bars.remove(bar);
    }

    public void addMeasure(Measure measure) {
        measures.add(measure);
    }

    public void deleteMeasure(Measure measure) {
        measures.remove(measure);
    }
}
