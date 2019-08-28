package com.brillio.timetable.dao;

import com.brillio.timetable.entities.course.Course;
import com.brillio.timetable.entities.instructor.Instructor;
import com.brillio.timetable.entities.timetable.StudentTimeTableMapping;
import com.brillio.timetable.entities.timetable.TimeTable;

import java.util.List;
import java.util.Map;

public interface IDao {

    void addCourse(String courseCode, String courseName, String courseTitle, int units, int totalSeats);

    Map<String, Course> getCourse();

    void addInstructor(String courseCode, String instructorName, List<Integer> dayOfWeek, int slots);

    Map<String , List<Instructor>> getInstructors();

    Instructor getInstructor(String courseCode, String insName);

    StudentTimeTableMapping getStudentTimeTable();

    void updateTimeTable(int studentId, TimeTable timeTable);


    TimeTable getTimeTableForStudent(int studentId);
}
