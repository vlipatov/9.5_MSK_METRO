package core;

import javax.print.attribute.standard.NumberOfInterveningJobs;

public class Line {
    Object lines;
    Object stations;
    Object color;
    Object number;
    Object name;

    public Line(Object number, Object name, Object color) {
        this.number = number;
        this.name = name;
        this.color = color;
    }
    public Line(Object lines) {
        this.lines = lines;
    }

}