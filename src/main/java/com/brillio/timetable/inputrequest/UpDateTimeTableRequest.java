package com.brillio.timetable.inputrequest;

import com.brillio.timetable.enums.DayOfWeek;
import com.brillio.timetable.enums.TimeSlot;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpDateTimeTableRequest {

    @JsonProperty("studentId")
    Integer studentId;

    @JsonProperty(value = "course")
    String courseCode;

    @JsonProperty(value = "instructor")
    String instructor;

    @JsonProperty(value = "dayOfWeek")
    DayOfWeek dayOfWeek;

    @JsonProperty(value = "timeSlot")
    TimeSlot timeSlot;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public String toString() {
        return "UpDateTimeTableRequest{" +
                "courseCode='" + courseCode + '\'' +
                ", instructor='" + instructor + '\'' +
                ", timeSlot='" + timeSlot + '\'' +
                ", dayOfWeek=" + dayOfWeek +
                '}';
    }
}
