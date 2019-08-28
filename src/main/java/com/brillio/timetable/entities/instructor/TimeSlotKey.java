package com.brillio.timetable.entities.instructor;

public class TimeSlotKey {

    int dayOfWeek;
    int slot;

    public TimeSlotKey(int dayOfWeek, int slot) {
        this.dayOfWeek = dayOfWeek;
        this.slot = slot;
    }

    @Override
    public int hashCode() {
        return this.dayOfWeek >> 1 * this.slot << 1;
    }

    @Override
    public boolean equals(Object obj) {
        TimeSlotKey key = (TimeSlotKey) obj;

        if (this.dayOfWeek == key.dayOfWeek && this.slot == key.slot) {
            return true;
        }
        return false;
    }
}
