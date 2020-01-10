package code.main;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;

import java.util.ArrayList;
import java.util.List;

public class Stanza {
    private int number;
    private List<Bar> bars = new ArrayList<>();

    private SimpleIntegerProperty simpleNumberProperty = new SimpleIntegerProperty();
    private SimpleListProperty<Bar> simpleBarsProperty = new SimpleListProperty<>();

    public Stanza() {

    }


    public int getNumber() {
        return number;
    }

    public SimpleIntegerProperty getSimpleNumberProperty() {
        return simpleNumberProperty;
    }

    public void setNumber(int number) {
        this.number = number;
        this.simpleNumberProperty.set(number);
    }

    public List<Bar> getBars() {
        return bars;
    }

    public SimpleListProperty<Bar> getSimpleBarsProperty() {
        return simpleBarsProperty;
    }

    public void setBars(List<Bar> bars) {
        this.bars = bars;
    }

//    public void setBars(ObservableList<Bar> bars) {
//        this.simpleBarsProperty.set(bars);
//    }

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
