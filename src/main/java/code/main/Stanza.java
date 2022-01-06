package code.main;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;

import java.util.ArrayList;
import java.util.List;

public class Stanza {
//    private int number;
    private List<Measure> measures = new ArrayList<>();

    public Stanza() {
    }

    public void appendMeasure(Measure measure) {
//        System.out.println("\tfrom Stanza.class:" + "\n" +
//                "New measure: "+measure.getSentence());
        this.measures.add(measure);
//        System.out.println("Measure list: "+"\n\t"+this.measures.toString());
    }

    public List<Measure> getMeasures() {
        return measures;
    }

    public void printMeasures() {
        System.out.println("\tprinting Stanza: ");
        int count = 1;
        for(int i = 0; i<this.measures.size(); i++) {
            System.out.println("Measure "+count+": "+this.measures.get(i).getSentence());
            count+=1;
        }
    }
}
