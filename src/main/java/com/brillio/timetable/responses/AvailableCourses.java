package com.brillio.timetable.responses;

import com.brillio.timetable.entities.instructor.Instructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AvailableCourses {

    @JsonProperty("course")
    String courseName;
    @JsonProperty("instructors")
    List<InstructorResponse> instructors;

    public void setInstructors(List<InstructorResponse> instructors) {
        this.instructors = instructors;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<InstructorResponse> getInstructors() {
        return instructors;
    }

    public String getCourseName() {
        return courseName;
    }

    @Override
    public String toString() {
        return "AvailableCourses{" +
                "courseName='" + courseName + '\'' +
                ", instructors=" + instructors +
                '}';
    }
}
