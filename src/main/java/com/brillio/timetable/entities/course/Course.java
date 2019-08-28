package com.brillio.timetable.entities.course;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Course{

    private String courseCode;

    private String courseName;

    private String courseTitle;

    private int courseUnits;

    private int totalseats;

    public Course(String code,String name, String title, int units, int totalseats) {
        this.courseCode = code;
        this.courseName = name;
        this.courseTitle = title;
        this.courseUnits = units;
        this.totalseats = totalseats;
    }


    @JsonProperty(value = "courseCode")
    public String  getCourseCode() {
        return this.courseCode;
    }

    public void setCourseCode(String  code) {
        this.courseCode = code;
    }


    @JsonProperty(value = "courseName")
    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseName(String name) {
        this.courseName = name;
    }

    @JsonProperty(value = "title")
    public String getCourseTitle() {
        return this.courseTitle;
    }

    public void setCourseTitle(String title) {
        this.courseTitle = title;
    }


    @JsonProperty(value = "units")
    public int getCourseUnit() {
        return this.courseUnits;
    }

    public void setCourseUnit(int units) {
        this.courseUnits = units;
    }

    @JsonProperty(value = "seats")
    public int getCourseSeat() {
        return this.totalseats;
    }

    public void setCourseSeat(int seats) {
        this.totalseats = seats;
    }
}
