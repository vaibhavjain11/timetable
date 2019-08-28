package com.brillio.timetable.entities.instructor;

public class TimeSlotEntry {

    int numOfStudents;
    int status;
    int dayOfWeek;
    int slot;

    public TimeSlotEntry(){}

    public TimeSlotEntry(int numOfStudents, int status, int dayOfWeek, int slot) {
        this.numOfStudents = numOfStudents;
        this.status = status;
        this.dayOfWeek = dayOfWeek;
        this.slot = slot;
    }

    public int getNumOfStudents() {
        return numOfStudents;
    }

    public void setNumOfStudents(int numOfStudents) {
        this.numOfStudents = numOfStudents;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
