package com.brillio.timetable.service;

import com.brillio.timetable.constants.Constants;
import com.brillio.timetable.dao.InMemoryDao;
import com.brillio.timetable.entities.course.Course;
import com.brillio.timetable.entities.instructor.Instructor;
import com.brillio.timetable.entities.timetable.Record;
import com.brillio.timetable.entities.timetable.StudentTimeTableMapping;
import com.brillio.timetable.entities.timetable.TimeTable;
import com.brillio.timetable.enums.DayOfWeek;
import com.brillio.timetable.enums.Status;
import com.brillio.timetable.enums.TimeSlot;
import com.brillio.timetable.inputrequest.UpDateTimeTableRequest;
import com.brillio.timetable.responses.StudentRecord;
import com.brillio.timetable.responses.UpdateTimeTableResponse;
import com.brillio.timetable.services.UpDateTimeTableService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import utils.Utils;

import java.util.*;
import java.util.stream.IntStream;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = UpDateTimeTableService.class)
public class UpdateTimetableServiceTest {


    @MockBean
    InMemoryDao inMemoryDao;

    @Autowired
    UpDateTimeTableService upDateTimeTableService;

    private Map<String, List<Instructor>> instructorMap = new HashMap<>();

    private Map<String, Course> courseMap = new HashMap<>();

    private  Map<Integer, TimeTable> studentMap;

    @Mock
    private StudentTimeTableMapping studentTimeTableMapping;
    private Instructor instructor;
    private Instructor instructor2;

    @BeforeEach
    public void init() {
        instructor = new Instructor();
        instructor.setDayOfWeek(Arrays.asList(Utils.convertDayOfWeekToInt(DayOfWeek.MON)));
        instructor.setInstructorName("Vikas");
        instructor.setNumberOfStudents(0);
        instructor.setHourslot(10);
        instructor.setCourseCode("1001");
        instructorMap.put("1001", Arrays.asList(instructor));

        instructor2 = new Instructor();
        instructor2.setDayOfWeek(Arrays.asList(Utils.convertDayOfWeekToInt(DayOfWeek.MON),
                Utils.convertDayOfWeekToInt(DayOfWeek.TUE)));
        instructor2.setInstructorName("Puneet");
        instructor2.setNumberOfStudents(0);
        instructor2.setHourslot(12);
        instructor2.setCourseCode("1002");
        instructorMap.put("1002", Arrays.asList(instructor2));

        Course course1 = new Course("1001","Biology","Bio123",20, 10);
        Course course2 = new Course("1002","Chemistry","Chem123",30, 40);
        courseMap.put("1001", course1);
        courseMap.put("1002", course2);

        studentMap = new HashMap<>();

        IntStream.range(1, 801).forEach(i -> studentMap.put(i, null));

    }

    @Test
    public void testUpdateTimetable() {

        when(inMemoryDao.getStudentTimeTable()).thenReturn(studentTimeTableMapping);
        when(studentTimeTableMapping.getMap()).thenReturn(studentMap);
        when(inMemoryDao.getInstructors()).thenReturn(instructorMap);
        when(inMemoryDao.getCourse()).thenReturn(courseMap);

        UpDateTimeTableRequest upDateTimeTableRequest = new UpDateTimeTableRequest();

        upDateTimeTableRequest.setCourseCode("1001");
        upDateTimeTableRequest.setDayOfWeek(DayOfWeek.MON);
        upDateTimeTableRequest.setInstructor("Vikas");
        upDateTimeTableRequest.setStudentId(1);
        upDateTimeTableRequest.setTimeSlot(TimeSlot.TWELEVE);

        List<UpDateTimeTableRequest> list = new ArrayList<>();

        list.add(upDateTimeTableRequest);

        UpdateTimeTableResponse updateTimeTableResponse = upDateTimeTableService.updateTimeTable(list);

        Assertions.assertNotNull(updateTimeTableResponse);
        Assertions.assertNotNull(updateTimeTableResponse.getStudentRecords());
        Assertions.assertEquals(1,updateTimeTableResponse.getStudentRecords().size());

        List<StudentRecord> studentRecordList = updateTimeTableResponse.getStudentRecords();

        Assertions.assertNotNull(studentRecordList.get(0).getRecords());
        Assertions.assertEquals(1,studentRecordList.get(0).getRecords().size());

        Record record = studentRecordList.get(0).getRecords().get(0);
        Assertions.assertEquals(TimeSlot.TWELEVE.getValue(),record.getSlot());
        Assertions.assertEquals("1001", record.getCourseCode());
        Assertions.assertEquals(Utils.convertDayOfWeekToInt(DayOfWeek.MON),record.getDayOfWeek());
    }

    @Test
    public void testUpdateTimetableErrorMessageForInstructorNotExists() {

        when(inMemoryDao.getStudentTimeTable()).thenReturn(studentTimeTableMapping);
        when(studentTimeTableMapping.getMap()).thenReturn(studentMap);
        when(inMemoryDao.getInstructors()).thenReturn(instructorMap);
        when(inMemoryDao.getCourse()).thenReturn(courseMap);

        UpDateTimeTableRequest upDateTimeTableRequest = new UpDateTimeTableRequest();

        upDateTimeTableRequest.setCourseCode("1001");
        upDateTimeTableRequest.setDayOfWeek(DayOfWeek.MON);
        upDateTimeTableRequest.setInstructor("Brillio");
        upDateTimeTableRequest.setStudentId(1);
        upDateTimeTableRequest.setTimeSlot(TimeSlot.TWELEVE);

        List<UpDateTimeTableRequest> list = new ArrayList<>();

        list.add(upDateTimeTableRequest);

        UpdateTimeTableResponse updateTimeTableResponse = upDateTimeTableService.updateTimeTable(list);

        Assertions.assertNotNull(updateTimeTableResponse);
        Assertions.assertNotNull(updateTimeTableResponse.getStudentRecords());
        Assertions.assertEquals(1,updateTimeTableResponse.getStudentRecords().size());

        Assertions.assertEquals("Requested Instructor is not registered",updateTimeTableResponse.getStudentRecords().get(0).getError());
    }

    @Test
    public void testSubmitTimeTableErrorForNotHavingFourtyUnits() {

        TimeTable timeTable = new TimeTable();

        Record record1 = new Record();
        record1.setSlot(TimeSlot.TWELEVE.getValue());
        record1.setDayOfWeek(Utils.convertDayOfWeekToInt(DayOfWeek.MON));
        record1.setInstructorName("Vikas");
        record1.setCourseCode("1001");
        timeTable.addRecord(record1);
        timeTable.setStatus(Status.NOT_SUBMITTED);

        timeTable.setStudentId(1);
        timeTable.setUnits(20);

        when(inMemoryDao.getInstructor("1001","Vikas")).thenReturn(instructor);
        when(inMemoryDao.getTimeTableForStudent(1)).thenReturn(timeTable);

        TimeTable actualTimeTable = upDateTimeTableService.submitTimeTable(1);

        Assertions.assertNotNull(actualTimeTable);
        Assertions.assertEquals(courseMap.get("1001").getCourseUnit() , actualTimeTable.getUnits());
        Assertions.assertEquals("Total units aren't 40", actualTimeTable.getError());

    }

    @Test
    public void testSubmitTimeTableErrorSameTimeSlotMoreThanOnce() {

        TimeTable timeTable = new TimeTable();

        Record record1 = new Record();
        record1.setSlot(TimeSlot.TWELEVE.getValue());
        record1.setDayOfWeek(Utils.convertDayOfWeekToInt(DayOfWeek.MON));
        record1.setInstructorName("Vikas");
        record1.setCourseCode("1001");
        timeTable.addRecord(record1);
        timeTable.setStatus(Status.NOT_SUBMITTED);


        Record record2 = new Record();
        record2.setSlot(TimeSlot.TWELEVE.getValue());
        record2.setDayOfWeek(Utils.convertDayOfWeekToInt(DayOfWeek.MON));
        record2.setInstructorName("Puneet");
        record2.setCourseCode("1002");
        timeTable.addRecord(record2);
        timeTable.setStatus(Status.NOT_SUBMITTED);

        timeTable.setStudentId(1);
        timeTable.setUnits(50);

        when(inMemoryDao.getInstructor("1001","Vikas")).thenReturn(instructor);
        when(inMemoryDao.getInstructor("1002","Puneet")).thenReturn(instructor2);
        when(inMemoryDao.getTimeTableForStudent(1)).thenReturn(timeTable);

        TimeTable actualTimeTable = upDateTimeTableService.submitTimeTable(1);

        Assertions.assertNotNull(actualTimeTable);
        Assertions.assertEquals("Same time slot can't be used again in one time table", actualTimeTable.getError());

    }

}
