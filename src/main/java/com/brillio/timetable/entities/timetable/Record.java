package com.brillio.timetable.entities.timetable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Record {

    @JsonProperty(value = "courseCode")
    String  courseCode;

    @JsonProperty(value = "instructorName")
    String instructorName;

    @JsonProperty(value = "dayOfWeek")
    int dayOfWeek;

    @JsonProperty(value = "slot")
    int slot;

    public Record(){}

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorName() {
        return instructorName;
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

    @Override
    public String toString() {
        return "Record{" +
                "courseCode='" + courseCode + '\'' +
                ", instructorName='" + instructorName + '\'' +
                ", dayOfWeek=" + dayOfWeek +
                ", slot=" + slot +
                '}';
    }
}
