package com.brillio.timetable.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class InstructorResponse {

    @JsonProperty(value = "instructorName")
    String instructorName;

    @JsonProperty(value = "freeSlots")
    List<FreeSlot> freeSlots = new ArrayList<>();


    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void addFreeSlots(FreeSlot freeSlots) {
        this.freeSlots.add(freeSlots);
    }

    public List<FreeSlot> getFreeSlots() {
        return freeSlots;
    }

    @Override
    public String toString() {
        return "InstructorResponse{" +
                "instructorName='" + instructorName + '\'' +
                ", freeSlots=" + freeSlots +
                '}';
    }
}
