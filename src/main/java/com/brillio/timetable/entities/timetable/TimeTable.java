package com.brillio.timetable.entities.timetable;

import com.brillio.timetable.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class TimeTable {

    @JsonProperty(value = "studentId")
    int studentId;

    @JsonProperty(value = "timetableEntries")
    List<Record> records = new ArrayList<>();

    @JsonProperty(value = "units")
    int units;

    @JsonProperty(value = "error")
    String error;

    @JsonProperty(value = "status")
    Status status;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void addRecord(Record record) {
        records.add(record);
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public int getUnits() {
        return units;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return studentId;
    }

    @Override
    public boolean equals(Object obj) {
        TimeTable timeTable = (TimeTable)obj;

        if (this.studentId == timeTable.studentId)
            return true;
        return false;
    }
}
