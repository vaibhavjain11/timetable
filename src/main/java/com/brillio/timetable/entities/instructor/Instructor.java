package com.brillio.timetable.entities.instructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Instructor{

    private String  courseCode;
    private String instructorName;
    private List<Integer> dayOfWeek;
    private int hourslot;
    Map<TimeSlotKey,TimeSlotEntry> timeSlotMap = new HashMap<>();
    private int numberOfStudents;

    public Instructor(){}

    public Instructor(String  courseCode, String name, List<Integer> dayOfWeek, int hourslot) {
        this.courseCode = courseCode;
        this.instructorName = name;
        this.dayOfWeek = dayOfWeek;
        this.hourslot = hourslot;
    }

    public void setCourseCode(String  courseCode) {
        this.courseCode = courseCode;
    }

    public String  getCourseCode() {
        return courseCode;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public List<Integer> getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(List<Integer> dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getHourslot() {
        return hourslot;
    }

    public void setHourslot(int hourslot) {
        this.hourslot = hourslot;
    }

//    public void fillSlot(int dayOfWeek, int hour) {
//        slots[dayOfWeek][hour] = 1;
//    }
//
//    public int getSlotStatus(int dayofWeek, int hour) {
//        return slots[dayofWeek][hour];
//    }

    public int getSlotStatus(int dayOfWeek, int hour) {
        TimeSlotKey key = new TimeSlotKey(dayOfWeek, hour);
        if (timeSlotMap.get(key) != null) {
            return timeSlotMap.get(key).getStatus();
        }
        return 0;
    }

    public void fillSlot(int dayOfWeek, int hour) {
        TimeSlotKey key = new TimeSlotKey(dayOfWeek, hour);
        TimeSlotEntry entry;
        if (timeSlotMap.get(key) != null) {
            entry = timeSlotMap.get(key);
            entry.setNumOfStudents(entry.getNumOfStudents()+1);

        } else {
            entry = new TimeSlotEntry();
            entry.setNumOfStudents(1);
            entry.setDayOfWeek(dayOfWeek);
            entry.setSlot(hour);
            entry.setStatus(1);
        }

        timeSlotMap.put(key, entry);
    }


    public TimeSlotEntry getTimeSlotEntry(int dayOfWeek, int hour) {
        return timeSlotMap.get(new TimeSlotKey(dayOfWeek, hour));
    }

    public Map<TimeSlotKey, TimeSlotEntry> getSlots() {
        return timeSlotMap;
    }

    public void updateEntryMap(Map<TimeSlotKey, TimeSlotEntry> map) {
        timeSlotMap = map;
    }


    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    @Override
    public int hashCode() {
        return this.courseCode.hashCode() + this.instructorName.hashCode();
    }
}
