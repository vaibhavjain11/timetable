package com.brillio.timetable.inputrequest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddCourse {

    @JsonProperty(value = "courseCode")
    private String courseCode;

    @JsonProperty(value = "courseName")
    private String courseName;

    @JsonProperty(value = "title")
    private String courseTitle;

    @JsonProperty(value = "units")
    private int courseUnits;

    @JsonProperty(value = "seats")
    private int totalseats;

    public String  getCourseCode() {
        return this.courseCode;
    }

    public void setCourseCode(String  code) {
        this.courseCode = code;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseName(String name) {
        this.courseName = name;
    }

    public String getCourseTitle() {
        return this.courseTitle;
    }

    public void setCourseTitle(String title) {
        this.courseTitle = title;
    }

    public int getCourseUnit() {
        return this.courseUnits;
    }

    public void setCourseUnit(int units) {
        this.courseUnits = units;
    }

    public int getCourseSeat() {
        return this.totalseats;
    }

    public void setCourseSeat(int seats) {
        this.totalseats = seats;
    }
}
