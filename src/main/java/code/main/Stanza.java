package code.main;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;

import java.util.ArrayList;
import java.util.List;

public class Stanza {
    private int number;
    private List<Bar> bars = new ArrayList<>();

    public Stanza() {

    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Bar> getBars() {
        return bars;
    }

    public void setBars(List<Bar> bars) {
        this.bars = bars;
    }

    @Override
    public String toString() {
        String printOut;
        if (bars.size() == 1) {
            printOut = "Stanza " + number + " ( " + bars.size() + " bar )";
        } else {
            printOut = "Stanza " + number + "( " + bars.size() + " bars )";
        }
        return printOut;
    }
}
