package com.brillio.timetable.filters;

import com.brillio.timetable.entities.instructor.Instructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DayOfWeekFilter implements Filter {

    @Override
    public List<Instructor> execute(Criteria criteria, List<Instructor> instructors) {

        return instructors.stream().filter(instructor -> instructor.getDayOfWeek().contains(criteria.getDayOfWeek()))
                .map(instructor -> {
                    instructor.setDayOfWeek(Arrays.asList(criteria.getDayOfWeek()));
//                    Instructor filteredInstructor = new Instructor(instructor.getCourseCode(), instructor.getInstructorName(),instructor.getDayOfWeek(), instructor.getHourslot());
//                    filteredInstructor.setDayOfWeek(Arrays.asList(criteria.getDayOfWeek()));
                    return instructor;
                }).collect(Collectors.toList());
    }
}
