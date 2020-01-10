package code.main;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;

import java.util.ArrayList;
import java.util.List;

public class Bar {
    private int number;
    private List<Measure> measures = new ArrayList<>();

    private SimpleIntegerProperty simpleNumberProperty = new SimpleIntegerProperty();
    private SimpleListProperty<Measure> simpleMeasuresProperty = new SimpleListProperty<>();

    public Bar() {

    }

    public List<Measure> getMeasures() {
        return measures;
    }

    public SimpleListProperty<Measure> getSimpleMeasuresProperty() {
        return simpleMeasuresProperty;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
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

    @Override
    public String toString() {
        String message;
        if (measures.size() == 1) {
            message = "Bar " + number + " ( " + measures.size() + " measure )";
        } else {
            message = "Bar " + number + " ( " + measures.size() + " measures )";
        }
        return message;
    }
}
