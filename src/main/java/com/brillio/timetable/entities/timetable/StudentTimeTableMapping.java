package com.brillio.timetable.entities.timetable;

import java.util.HashMap;
import java.util.Map;

public class StudentTimeTableMapping {

    Map<Integer, TimeTable> map = new HashMap<>();

    public Map<Integer, TimeTable> getMap() {
        return map;
    }
}
