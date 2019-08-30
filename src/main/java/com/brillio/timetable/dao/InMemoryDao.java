package com.brillio.timetable.dao;

import com.brillio.timetable.entities.course.Course;
import com.brillio.timetable.entities.instructor.Instructor;
import com.brillio.timetable.entities.timetable.StudentTimeTableMapping;
import com.brillio.timetable.entities.timetable.TimeTable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryDao implements IDao {

    Map<String, List<Instructor>> instructorMap = new HashMap<>();

    Map<String, Course> courseMap = new HashMap<>();

    StudentTimeTableMapping studentTimeTableMapping;

    @PostConstruct
    public void init() {
        initializeTimeTableMap();
    }

    // intialize students with 800 capacity
    private void initializeTimeTableMap() {
        studentTimeTableMapping = new StudentTimeTableMapping();
       // IntStream.range(1, 801).forEach(i -> studentTimeTableMapping.getMap().put(i, new Object()));
    }


    @Override
    public void addCourse(String courseCode, String courseName, String courseTitle, int units, int totalSeats) {

        Course course = new Course(courseCode, courseName, courseTitle, units, totalSeats);

        courseMap.put(courseCode, course);
    }

    @Override
    public Map<String, Course> getCourse() {
        return courseMap;
    }

    @Override
    public void addInstructor(String courseCode, String instructorName, List<Integer> dayOfWeek, int slots) {

        Instructor instructor = new Instructor();
        instructor.setDayOfWeek(dayOfWeek);
        instructor.setHourslot(slots);
        instructor.setCourseCode(courseCode);
        instructor.setInstructorName(instructorName);

        if (instructorMap.get(courseCode) != null) {
            List<Instructor> instructors = instructorMap.get(courseCode);
            instructors.add(instructor);
            instructorMap.put(courseCode, instructors);
        } else {
            instructorMap.put(courseCode, Arrays.asList(instructor));
        }

        List<Instructor> instructorList = instructorMap.get(courseCode);
        if (instructorList.size() > 0) {
            int seats = courseMap.get(courseCode).getCourseSeat() / instructorList.size();
            instructorList = instructorList.stream().map(instructor1 -> {
                instructor1.setNumberOfStudents(seats);
                return instructor1;
            }).collect(Collectors.toList());
        }

        instructorMap.put(courseCode, instructorList);

    }

    @Override
    public Map<String, List<Instructor>> getInstructors() {
        return instructorMap;
    }

    @Override
    public Instructor getInstructor(String courseCode, String insName) {
        List<Instructor> list = instructorMap.get(courseCode);

        if (list != null) {
            Optional<Instructor> instructor = list.stream().filter(ins -> ins.getInstructorName().equals(insName)).findAny();

            if (instructor.isPresent()) {
                return instructor.get();
            }
        }
        return null;
    }

    @Override
    public StudentTimeTableMapping getStudentTimeTable() {
        return studentTimeTableMapping;
    }

    @Override
    public void updateTimeTable(int studentId, TimeTable timeTable) {
        studentTimeTableMapping.getMap().put(studentId, timeTable);
    }

    public boolean checkIfCourse(String courseCode) {
        if (courseMap.get(courseCode) != null)
            return true;
        return false;
    }

    public Course getCourseDetail(String courseCode) {
        return courseMap.get(courseCode);
    }

    public boolean checkIfInstructorExists(String courseCode, String instructorName) {
        List<Instructor> instructors = instructorMap.get(courseCode);

        if (instructors != null) {
            Optional<Instructor> optionalInstructor = instructors.stream().filter(ins -> ins.getInstructorName().equals(instructorName)).findAny();
            if (optionalInstructor.isPresent()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public TimeTable getTimeTableForStudent(int studentId) {
        return studentTimeTableMapping.getMap().get(studentId);
    }
}
