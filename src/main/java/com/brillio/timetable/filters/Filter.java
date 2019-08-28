package com.brillio.timetable.filters;

import com.brillio.timetable.entities.instructor.Instructor;

import java.util.List;

public interface Filter {

    public List<Instructor> execute(Criteria criteria, List<Instructor> instructors);
}
