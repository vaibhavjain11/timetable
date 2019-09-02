package com.brillio.timetable.controller;


import com.brillio.timetable.entities.course.Course;
import com.brillio.timetable.entities.timetable.TimeTable;
import com.brillio.timetable.enums.DayOfWeek;
import com.brillio.timetable.inputrequest.AddCourse;
import com.brillio.timetable.inputrequest.AddInstructorRequest;
import com.brillio.timetable.inputrequest.UpDateTimeTableRequest;
import com.brillio.timetable.responses.AvailableCourses;
import com.brillio.timetable.responses.InstructorDetailResponse;
import com.brillio.timetable.responses.UpdateTimeTableResponse;
import com.brillio.timetable.services.FilterService;
import com.brillio.timetable.services.UpDateTimeTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
public class TimeTableController {

    public Logger LOG = LoggerFactory.getLogger(TimeTableController.class);

    @Autowired
    FilterService filterService;

    @Autowired
    UpDateTimeTableService upDateTimeTableService;

    @RequestMapping("/")
    public ResponseEntity<String> getHealth() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping(path = "/api/available_slots")
    public ResponseEntity<List<AvailableCourses>> getAvailableSlots(
            @RequestParam(value = "day", required = false) DayOfWeek day,
            @RequestParam(value = "course", required = false) String courseName) {

        LOG.info(" day : {}, course : {}", day, courseName);
        try {
            List<AvailableCourses> availableCourses = filterService.getFilteredResult(day, courseName);

            LOG.info("Available courses : {}", availableCourses);
            if (availableCourses != null && availableCourses.size() > 0) {
                return new ResponseEntity<>(availableCourses, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Collections.EMPTY_LIST, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            LOG.error("Exception {}", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }

    @RequestMapping(path = "/api/update_table", method = RequestMethod.POST)
    public ResponseEntity<UpdateTimeTableResponse> updateTimeTable(@Valid @RequestBody List<UpDateTimeTableRequest> requests) {

        LOG.info("Update table request : {}", requests);
        try {

            UpdateTimeTableResponse response = upDateTimeTableService.updateTimeTable(requests);

            LOG.info("Response : {}", response);
            if (response != null) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            LOG.error("Exception : {}", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/api/add_course", method = RequestMethod.POST)
    public ResponseEntity<String> addCourse(@Valid @RequestBody AddCourse course) {
        String response = upDateTimeTableService.addCourse(course);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(path = "/api/course", method = RequestMethod.GET)
    public ResponseEntity<Course> addCourse(@RequestParam(value = "course") String courseCode) {
        Course response = upDateTimeTableService.getCourse(courseCode);

        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(path = "/api/add_instructor", method = RequestMethod.POST)
    public ResponseEntity<String> addCourse(@Valid @RequestBody AddInstructorRequest request) {

        String response = upDateTimeTableService.addInstructor(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(path = "/api/instructor")
    public ResponseEntity<InstructorDetailResponse> getInstructor(
            @RequestParam(value = "courseCode") String courseCode,
            @RequestParam(value = "instructorName") String instructorName) {

        InstructorDetailResponse response = upDateTimeTableService.getInstructorDetail(courseCode,instructorName);

        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/api/submit/{studentId}")
    public ResponseEntity<TimeTable> submitTimeTable(@Valid @PathVariable(value = "studentId") String studentId) {

        LOG.info("Student Id : {}", studentId);
        TimeTable response = upDateTimeTableService.submitTimeTable(Integer.parseInt(studentId));

        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
