package com.brillio.timetable.inputrequest;

import com.brillio.timetable.enums.DayOfWeek;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AddInstructorRequest {

    String courseCode;

    String instructorName;

    List<DayOfWeek> dayOfWeeks;

    int hours;

    public String getCourseCode() {
        return courseCode;
    }

    @JsonProperty(value = "courseCode")
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getInstructorName() {
        return instructorName;
    }

    @JsonProperty(value = "instructorName")
    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public List<DayOfWeek> getDayOfWeeks() {
        return dayOfWeeks;
    }


    @JsonProperty(value = "dayOfWeeks")
    public void setDayOfWeeks(List<DayOfWeek> dayOfWeeks) {
        this.dayOfWeeks = dayOfWeeks;
    }

    public int getHours() {
        return hours;
    }

    @JsonProperty(value = "hours")
    public void setHours(int hours) {
        this.hours = hours;
    }
}
