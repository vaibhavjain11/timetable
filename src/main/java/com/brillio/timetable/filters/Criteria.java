package com.brillio.timetable.filters;

public class Criteria {

    private int dayOfWeek;
    private String courseCode;
    private int timeSlot;

    private Criteria(CriteriaBuilder criteriaBuilder) {
        this.dayOfWeek = criteriaBuilder.dayOfWeek;
        this.courseCode = criteriaBuilder.courseCode;
        this.timeSlot = criteriaBuilder.timeSlot;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public static class CriteriaBuilder{

         int dayOfWeek;
         String courseCode;
         int timeSlot;

         public CriteriaBuilder setDayOfWeek(int dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
            return this;
         }

         public CriteriaBuilder setCourseCode(String courseCode) {
             this.courseCode = courseCode;
             return this;
         }

         public CriteriaBuilder setTimeSlot(int slot) {
             this.timeSlot = slot;
             return this;
         }

         public Criteria build() {
             Criteria criteria = new Criteria(this);
             return criteria;
         }
    }
}
