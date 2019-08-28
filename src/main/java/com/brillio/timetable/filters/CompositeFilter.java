package com.brillio.timetable.filters;

import com.brillio.timetable.entities.instructor.Instructor;

import java.util.ArrayList;
import java.util.List;

public class CompositeFilter implements Filter{

    List<Filter> filters = new ArrayList<>();

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    @Override
    public List<Instructor> execute(Criteria criteria, List<Instructor> instructors) {

        for (Filter filter: filters) {
            instructors = filter.execute(criteria, instructors);
        }

        return instructors;
    }
}
