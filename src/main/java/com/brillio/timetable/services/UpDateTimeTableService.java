package com.brillio.timetable.services;

import com.brillio.timetable.constants.Constants;
import com.brillio.timetable.dao.InMemoryDao;
import com.brillio.timetable.entities.course.Course;
import com.brillio.timetable.entities.instructor.Instructor;
import com.brillio.timetable.entities.instructor.TimeSlotEntry;
import com.brillio.timetable.entities.instructor.TimeSlotKey;
import com.brillio.timetable.entities.timetable.Record;
import com.brillio.timetable.entities.timetable.TimeTable;
import com.brillio.timetable.enums.DayOfWeek;
import com.brillio.timetable.enums.Status;
import com.brillio.timetable.inputrequest.AddCourse;
import com.brillio.timetable.inputrequest.AddInstructorRequest;
import com.brillio.timetable.inputrequest.UpDateTimeTableRequest;
import com.brillio.timetable.responses.InstructorDetailResponse;
import com.brillio.timetable.responses.StudentRecord;
import com.brillio.timetable.responses.UpdateTimeTableResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.Utils;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UpDateTimeTableService {


    @Autowired
    InMemoryDao inMemoryDao;

    ReentrantLock lock = new ReentrantLock();

    private static final Logger LOG = LoggerFactory.getLogger(UpDateTimeTableService.class.getName());


    /**
     * It is returning the response with time table of student
     *
     * @param upDateTimeTableRequests
     * @return UpdateTimeTableResponse
     */
    public UpdateTimeTableResponse updateTimeTable(List<UpDateTimeTableRequest> upDateTimeTableRequests) {

        ExecutorService executors = Executors.newFixedThreadPool(10);

        List<UpdateTableTask> taskList = upDateTimeTableRequests.stream()
                .map(upDateTimeTableRequest -> new UpdateTableTask(upDateTimeTableRequest.getStudentId(), upDateTimeTableRequest.getCourseCode(),
                        upDateTimeTableRequest.getInstructor(),
                        Utils.convertDayOfWeekToInt(upDateTimeTableRequest.getDayOfWeek()),
                        upDateTimeTableRequest.getTimeSlot(), inMemoryDao, lock)
                ).collect(Collectors.toList());

        List<Future<TimeTable>> futures = Collections.EMPTY_LIST;
        try {
            futures = executors.invokeAll(taskList);
        } catch (InterruptedException e) {

        }

        Set<TimeTable> timeTables = new HashSet<>();
        for (Future future : futures) {

            try {
                TimeTable timeTable = (TimeTable) future.get();
                timeTables.add(timeTable);
            } catch (InterruptedException e) {
                LOG.error("Exception {}", e);
            } catch (ExecutionException e) {
                LOG.error("Exception {}", e);
            }

        }

        return buildResponse(timeTables);

    }

    private UpdateTimeTableResponse buildResponse(Set<TimeTable> timeTables) {

        UpdateTimeTableResponse updateTimeTableResponse = new UpdateTimeTableResponse();
        List<StudentRecord> studentRecords = timeTables.stream().map(buildStudentRecord).collect(Collectors.toList());
        updateTimeTableResponse.setStudentRecords(studentRecords);
        return updateTimeTableResponse;

    }


    /**
     * @param studentId - Student who wants to submit his/her timetable
     * @return - Success ot failure
     */
    public TimeTable submitTimeTable(int studentId) {
        int flag = 0;
        TimeTable timeTable = inMemoryDao.getTimeTableForStudent(studentId);
        if (timeTable == null) {
            timeTable = new TimeTable();
            timeTable.setError("Timetable doesn't exists for student");
        }
        if (timeTable.getStatus() == Status.SUBMITTED) {
            timeTable.setError("Table is already submitted");
        }
        if (timeTable.getUnits() < 40) {
            timeTable.setError("Total units aren't 40");
        } else {
            List<Record> records = timeTable.getRecords();
            // Clashes on same time slot should also be prompted in an individual
            //timetable. e.g. selecting 2 instructors for the same time slot.
            Map<TimeSlotKey, String> mapForSameTimeClash = new HashMap<>();
            for (Record record : records) {
                flag = updateInstructorTimeSlot(record, mapForSameTimeClash);
            }
            if (flag == 1) {
                timeTable.setError("Same time slot can't be used again in one time table");
            }
            if (flag == 2) {
                timeTable.setError("Some of the slots are full so timetable can't be submitted");
            }
            timeTable.setStatus(Status.SUBMITTED);
            inMemoryDao.updateTimeTable(timeTable.getStudentId(), timeTable);
        }

        return timeTable;
    }

    private int updateInstructorTimeSlot(Record record, Map<TimeSlotKey, String> mapForSameTimeClash) {
        Instructor instructor = inMemoryDao.getInstructor(record.getCourseCode(), record.getInstructorName());
        Map<TimeSlotKey, TimeSlotEntry> timeSlotEntryMap = instructor.getSlots();
        TimeSlotKey key = new TimeSlotKey(record.getDayOfWeek(), record.getSlot());
        if (mapForSameTimeClash.get(key) == null) {
            mapForSameTimeClash.put(key, instructor.getInstructorName());
        } else {
            return 1;
        }
        TimeSlotEntry entry = timeSlotEntryMap.get(key);

        // In case 2 student request is coming for same instructor, then multiple thread will access the same instructor object.
        // To avoid inconsistent in entries of slot lock on instructor
        synchronized (instructor) {
            if (entry == null) {
                entry = new TimeSlotEntry();
                entry.setNumOfStudents(1);
                entry.setStatus(1);
                entry.setSlot(record.getSlot());
                entry.setDayOfWeek(record.getDayOfWeek());

            } else {
                if (entry.getNumOfStudents() == instructor.getNumberOfStudents()) {
                    return 2;
                }
                // Increasing the count of students who submitted their timetable
                entry.setNumOfStudents(entry.getNumOfStudents() + 1);
            }
        }
        timeSlotEntryMap.put(key, entry);
        return 0;
    }

    Function<TimeTable, StudentRecord> buildStudentRecord = (timeTable) -> {

        StudentRecord studentRecord = new StudentRecord();

        studentRecord.setStudentId(String.valueOf(timeTable.getStudentId()));
        if (timeTable.getError().equals(Constants.NO_ERROR)) {
            studentRecord.setRecords(timeTable.getRecords());
        } else {
            studentRecord.setError(timeTable.getError());
        }
        return studentRecord;
    };

    /**
     * @param course - Course needs to be added in persistence layer
     * @return Suucess or failure
     */
    public String addCourse(AddCourse course) {

        if (inMemoryDao.checkIfCourse(course.getCourseCode())) {
            return "Course Exists";
        } else {
            inMemoryDao.addCourse(course.getCourseCode(), course.getCourseName(), course.getCourseTitle(), course.getCourseUnit(), course.getCourseSeat());
            return "Course Added";
        }
    }

    /**
     * @param courseCode
     * @return
     */
    public Course getCourse(String courseCode) {

        if (inMemoryDao.checkIfCourse(courseCode)) {
            return inMemoryDao.getCourseDetail(courseCode);
        } else {
            return null;
        }
    }

    public String addInstructor(AddInstructorRequest request) {

        List<Integer> list = request.getDayOfWeeks().stream()
                .map(d -> Utils.convertDayOfWeekToInt(d)).collect(Collectors.toList());

        if (!inMemoryDao.checkIfCourse(request.getCourseCode())) {
            return "Course doesn't exists";
        }

        if (!inMemoryDao.checkIfInstructorExists(request.getCourseCode(), request.getInstructorName())) {
            inMemoryDao.addInstructor(request.getCourseCode(), request.getInstructorName(), list, request.getHours());
            return "Instructor Added";
        } else {
            return "Instructor Exists";
        }
    }

    public InstructorDetailResponse getInstructorDetail(String courseCode, String insName) {

        Instructor instructor = inMemoryDao.getInstructor(courseCode, insName);

        if (instructor == null) {
            return null;
        } else {
            InstructorDetailResponse instructorDetailResponse = new InstructorDetailResponse();

            instructorDetailResponse.setCourseCode(instructor.getCourseCode());
            instructorDetailResponse.setInstructorName(instructor.getInstructorName());
            List<DayOfWeek> dayOfWeeks = instructor.getDayOfWeek().stream().map(d -> Utils.convertIntToDayOfWeek(d))
                    .collect(Collectors.toList());
            instructorDetailResponse.setDayOfWeeks(dayOfWeeks);
            instructorDetailResponse.setHours(String.valueOf(instructor.getHourslot()));
            return instructorDetailResponse;
        }

    }
}
