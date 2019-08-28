package com.brillio.timetable.services;

import com.brillio.timetable.dao.InMemoryDao;
import com.brillio.timetable.entities.instructor.Instructor;
import com.brillio.timetable.entities.instructor.TimeSlotEntry;
import com.brillio.timetable.entities.instructor.TimeSlotKey;
import com.brillio.timetable.enums.DayOfWeek;
import com.brillio.timetable.enums.TimeSlot;
import com.brillio.timetable.filters.CourseCodeFilter;
import com.brillio.timetable.filters.Criteria;
import com.brillio.timetable.filters.DayOfWeekFilter;
import com.brillio.timetable.filters.CompositeFilter;
import com.brillio.timetable.responses.AvailableCourses;
import com.brillio.timetable.responses.FreeSlot;
import com.brillio.timetable.responses.InstructorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FilterService {

    @Autowired
    InMemoryDao inMemoryDao;

    public List<AvailableCourses> getFilteredResult(DayOfWeek dayOfWeek, String courseCode) {

        int day = -1, timeSlot = -1;

        if (dayOfWeek != null) {
            day = Utils.convertDayOfWeekToInt(dayOfWeek);
        }

        Criteria criteria = new Criteria.CriteriaBuilder()
                                .setCourseCode(courseCode)
                                .setDayOfWeek(day)
                                .setTimeSlot(timeSlot)
                                .build();


        CompositeFilter compositeFilter = new CompositeFilter();
        if (courseCode != null) {
            compositeFilter.addFilter(new CourseCodeFilter());
        }
        if(day != -1) {
            compositeFilter.addFilter(new DayOfWeekFilter());
        }

        List<Instructor> instructors = inMemoryDao.getInstructors().entrySet().stream().map(Map.Entry::getValue)
                                        .flatMap(list -> list.stream()).collect(Collectors.toList());
        List<Instructor> instructorList = compositeFilter.execute(criteria,instructors);

        return buildResponse(instructorList);
    }

    private List<AvailableCourses> buildResponse(List<Instructor> instructorList) {

        List<AvailableCourses> availableCourses = new ArrayList<>();

        Map<String, List<Instructor>> map =
                instructorList.stream().collect(Collectors.groupingBy(instructor -> instructor.getCourseCode()));

        return map.keySet().stream().map(course -> {
            AvailableCourses availableCourse = new AvailableCourses();
            availableCourse.setCourseName(inMemoryDao.getCourse().get(course).getCourseName());

            List<Instructor> list = map.get(course);

            List<InstructorResponse> instructorResponses = list.stream().map(instructor -> buildInstructorResponse(instructor)).collect(Collectors.toList());
            availableCourse.setInstructors(instructorResponses);
            return availableCourse;
        }).collect(Collectors.toList());
    }

    private InstructorResponse buildInstructorResponse(Instructor instructor) {
        InstructorResponse instructorResponse = new InstructorResponse();

        instructorResponse.setInstructorName(instructor.getInstructorName());

        for (int dayofWeek : instructor.getDayOfWeek()) {
            FreeSlot freeSlot = new FreeSlot();
            freeSlot.setDayOfWeek(Utils.convertIntToDayOfWeek(dayofWeek).name());
            calculateFreeSlots(instructor, freeSlot, dayofWeek);
            instructorResponse.addFreeSlots(freeSlot);
        }

        return instructorResponse;
    }

    // slot filtering
    private void calculateFreeSlots(Instructor instructor, FreeSlot freeSlot, int dayofWeek) {
        Map<TimeSlotKey, TimeSlotEntry> timeSlotEntryMap = instructor.getSlots();

        TimeSlotKey key = new TimeSlotKey(dayofWeek, TimeSlot.NINE.getValue());
        if (checkForNumberOfStudents(key, timeSlotEntryMap, instructor)) {
            freeSlot.addSlot(TimeSlot.NINE.getValue());
        }

        key = new TimeSlotKey(dayofWeek, TimeSlot.TEN.getValue());
        if (checkForNumberOfStudents(key, timeSlotEntryMap, instructor)) {
            freeSlot.addSlot(TimeSlot.TEN.getValue());
        }

        key = new TimeSlotKey(dayofWeek, TimeSlot.ELEVEN.getValue());
        if (checkForNumberOfStudents(key, timeSlotEntryMap, instructor)) {
            freeSlot.addSlot(TimeSlot.ELEVEN.getValue());
        }

        key = new TimeSlotKey(dayofWeek, TimeSlot.TWELEVE.getValue());
        if (checkForNumberOfStudents(key, timeSlotEntryMap, instructor)) {
            freeSlot.addSlot(TimeSlot.TWELEVE.getValue());
        }

        key = new TimeSlotKey(dayofWeek, TimeSlot.THIRTEEN.getValue());
        if (checkForNumberOfStudents(key, timeSlotEntryMap, instructor)) {
            freeSlot.addSlot(TimeSlot.THIRTEEN.getValue());
        }

        key = new TimeSlotKey(dayofWeek, TimeSlot.FOURTEEN.getValue());
        if (checkForNumberOfStudents(key, timeSlotEntryMap, instructor)) {
            freeSlot.addSlot(TimeSlot.FOURTEEN.getValue());
        }

        key = new TimeSlotKey(dayofWeek, TimeSlot.FIFTEEN.getValue());
        if (checkForNumberOfStudents(key, timeSlotEntryMap, instructor)) {
            freeSlot.addSlot(TimeSlot.FIFTEEN.getValue());
        }

        key = new TimeSlotKey(dayofWeek, TimeSlot.SIXTEEN.getValue());
        if (checkForNumberOfStudents(key, timeSlotEntryMap, instructor)) {
            freeSlot.addSlot(TimeSlot.SIXTEEN.getValue());
        }

    }

    private boolean checkForNumberOfStudents(TimeSlotKey key, Map<TimeSlotKey, TimeSlotEntry> timeSlotEntryMap, Instructor instructor) {
        if (timeSlotEntryMap.get(key) == null || (timeSlotEntryMap.get(key) != null
                && timeSlotEntryMap.get(key).getNumOfStudents() < instructor.getNumberOfStudents()))
            return true;
        return false;
    }
}
