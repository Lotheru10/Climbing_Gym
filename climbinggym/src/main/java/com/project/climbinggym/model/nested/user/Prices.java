package com.project.climbinggym.model.nested.user;

public class Prices {
    public Prices(){}

    private double regular;
    private double reduced;

    //GETTERS AND SETTERS
    public double getRegular() {
        return regular;
    }
    public void setRegular(double regular) {
        this.regular = regular;
    }

    public double getReduced() {
        return reduced;
    }
    public void setReduced(double reduced) {
        this.reduced = reduced;
    }
}
