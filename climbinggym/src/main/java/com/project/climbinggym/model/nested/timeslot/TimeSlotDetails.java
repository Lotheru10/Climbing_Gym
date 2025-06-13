package com.project.climbinggym.model.nested.timeslot;

public class TimeSlotDetails {
    public TimeSlotDetails(){}

    private SlotInfo morning = new SlotInfo(20);
    private SlotInfo noon = new SlotInfo(30);
    private SlotInfo evening = new SlotInfo(20);

    //GETTERS AND SETTERS

    public SlotInfo getMorning() {
        return morning;
    }
    public void setMorning(SlotInfo morning) {
        this.morning = morning;
    }

    public SlotInfo getNoon() {
        return noon;
    }
    public void setNoon(SlotInfo noon) {
        this.noon = noon;
    }

    public SlotInfo getEvening() {
        return evening;
    }
    public void setEvening(SlotInfo evening) {
        this.evening = evening;
    }
}
