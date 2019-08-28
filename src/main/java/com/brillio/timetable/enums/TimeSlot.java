package com.brillio.timetable.enums;

public enum TimeSlot {
    NINE(9),
    TEN(10),
    ELEVEN(11),
    TWELEVE(12),
    THIRTEEN(13),
    FOURTEEN(14),
    FIFTEEN(15),
    SIXTEEN(16);

    int value;
    TimeSlot(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
