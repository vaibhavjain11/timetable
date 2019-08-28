package com.brillio.timetable.responses;

import com.brillio.timetable.enums.DayOfWeek;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InstructorDetailResponse {

    String courseCode;

    String instructorName;

    List<DayOfWeek> dayOfWeeks;

    String hours;

    @JsonProperty(value = "courseCode")
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    @JsonProperty(value = "instructor_name")
    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    @JsonProperty(value = "dayOfWeeks")
    public List<DayOfWeek> getDayOfWeeks() {
        return dayOfWeeks;
    }

    public void setDayOfWeeks(List<DayOfWeek> dayOfWeeks) {
        this.dayOfWeeks = dayOfWeeks;
    }

    @JsonProperty(value = "hours")
    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}
