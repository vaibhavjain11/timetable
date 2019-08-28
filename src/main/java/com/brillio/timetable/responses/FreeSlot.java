package com.brillio.timetable.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class FreeSlot {

    @JsonProperty(value = "day")
    String dayOfWeek;
    @JsonProperty("slots")
    List<Integer> slots = new ArrayList<>();

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void addSlot(int slot) {
        slots.add(slot);
    }

    @Override
    public String toString() {
        return "FreeSlot{" +
                "dayOfWeek='" + dayOfWeek + '\'' +
                ", slots=" + slots +
                '}';
    }
}
