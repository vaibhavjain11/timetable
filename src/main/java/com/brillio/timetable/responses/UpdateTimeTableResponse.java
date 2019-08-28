package com.brillio.timetable.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UpdateTimeTableResponse {

    @JsonProperty(value = "students")
    List<StudentRecord> studentRecords;

    public List<StudentRecord> getStudentRecords() {
        return studentRecords;
    }

    public void setStudentRecords(List<StudentRecord> studentRecords) {
        this.studentRecords = studentRecords;
    }

    @Override
    public String toString() {
        return "UpdateTimeTableResponse{" +
                "studentRecords=" + studentRecords +
                '}';
    }
}
