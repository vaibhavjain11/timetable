package com.brillio.timetable.responses;

import com.brillio.timetable.entities.timetable.Record;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StudentRecord {

    @JsonProperty(value = "studentId")
    String studentId;

    @JsonProperty("records")
    List<Record> records;

    @JsonProperty(value = "error")
    String error;

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "StudentRecord{" +
                "studentId='" + studentId + '\'' +
                ", records=" + records +
                ", error='" + error + '\'' +
                '}';
    }
}
