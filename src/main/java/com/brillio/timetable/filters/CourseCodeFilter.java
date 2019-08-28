package com.brillio.timetable.filters;

import com.brillio.timetable.entities.instructor.Instructor;

import java.util.List;
import java.util.stream.Collectors;

public class CourseCodeFilter implements Filter {
    @Override
    public List<Instructor> execute(Criteria criteria, List<Instructor> instructors) {

        return instructors.stream().filter(instructor -> instructor.getCourseCode().equals(criteria.getCourseCode()))
                .collect(Collectors.toList());
    }
}
