package com.brillio.timetable.controller;

import com.brillio.timetable.application.TimetableApplication;
import com.brillio.timetable.dao.InMemoryDao;
import com.brillio.timetable.entities.course.Course;
import com.brillio.timetable.entities.instructor.Instructor;
import com.brillio.timetable.entities.timetable.Record;
import com.brillio.timetable.entities.timetable.TimeTable;
import com.brillio.timetable.enums.DayOfWeek;
import com.brillio.timetable.enums.Status;
import com.brillio.timetable.enums.TimeSlot;
import com.brillio.timetable.inputrequest.AddCourse;
import com.brillio.timetable.responses.AvailableCourses;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;
import utils.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TimetableApplication.class, ServletWebServerFactoryAutoConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TimetableControllerIntegrationTest {

    private static Instructor instructor;
    private static Map<Integer, TimeTable> studentMap;
    @LocalServerPort
    int port;

    @MockBean
    InMemoryDao inMemoryDao;

    TestRestTemplate testRestTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    private static Map<String, List<Instructor>> instructorMap = new HashMap<>();

    private static Map<String, Course> courseMap = new HashMap<>();

    @BeforeClass
    public static void init() {
        instructor = new Instructor();
        instructor.setDayOfWeek(Arrays.asList(Utils.convertDayOfWeekToInt(DayOfWeek.MON)));
        instructor.setInstructorName("Vikas");
        instructor.setNumberOfStudents(0);
        instructor.setHourslot(10);
        instructor.setCourseCode("1001");
        instructorMap.put("1001", Arrays.asList(instructor));

        Instructor instructor2 = new Instructor();
        instructor2.setDayOfWeek(Arrays.asList(Utils.convertDayOfWeekToInt(DayOfWeek.MON),
                Utils.convertDayOfWeekToInt(DayOfWeek.TUE)));
        instructor2.setInstructorName("Puneet");
        instructor2.setNumberOfStudents(0);
        instructor2.setHourslot(12);
        instructor2.setCourseCode("1002");
        instructorMap.put("1002", Arrays.asList(instructor2));

        Course course1 = new Course("1001", "Biology", "Bio123", 50, 10);
        Course course2 = new Course("1002", "Chemistry", "Chem123", 30, 20);
        courseMap.put("1001", course1);
        courseMap.put("1002", course2);

        studentMap = new HashMap<>();

        IntStream.range(1, 801).forEach(i -> studentMap.put(i, null));

    }

    @Test
    public void testCreateCourse() {
        AddCourse course = new AddCourse();
        course.setCourseCode("1001");
        course.setCourseName("Biology");
        course.setCourseSeat(10);
        course.setCourseTitle("Bio123");
        course.setCourseUnit(20);

        HttpEntity<AddCourse> entity = new HttpEntity<>(course, headers);

        ResponseEntity<String> response = testRestTemplate.exchange(
                createURLWithPort("/api/add_course"), HttpMethod.POST, entity, String.class);

        String expected = "Course Added";
        Assert.assertEquals(expected, response.getBody());
    }

    @Test
    public void testGetAvailableSlots() {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/api/available_slots"))
                .queryParam("day", "TUE")
                .queryParam("courseName", "1001");

        Map<String, Object> params = new HashMap<>();
        params.put("day", "TUE");
        params.put("course", "1001");

        when(inMemoryDao.getInstructors()).thenReturn(instructorMap);
        when(inMemoryDao.getCourse()).thenReturn(courseMap);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<AvailableCourses[]> responseEntity = testRestTemplate.exchange(builder.toUriString(),
                HttpMethod.GET,
                entity,
                AvailableCourses[].class);

        AvailableCourses[] availableCourses = responseEntity.getBody();

        Assert.assertEquals(1, availableCourses.length);

    }

    @Test
    public void testSubmitTimeTable() {

        when(inMemoryDao.getInstructors()).thenReturn(instructorMap);
        when(inMemoryDao.getCourse()).thenReturn(courseMap);
        TimeTable timeTable = new TimeTable();

        Record record1 = new Record();
        record1.setSlot(TimeSlot.TWELEVE.getValue());
        record1.setDayOfWeek(Utils.convertDayOfWeekToInt(DayOfWeek.MON));
        record1.setInstructorName("Vikas");
        record1.setCourseCode("1001");
        timeTable.addRecord(record1);
        timeTable.setStatus(Status.NOT_SUBMITTED);
        timeTable.setStudentId(1);
        timeTable.setUnits(50);


        when(inMemoryDao.getTimeTableForStudent(1)).thenReturn(timeTable);
        when(inMemoryDao.getInstructor("1001", "Vikas")).thenReturn(instructor);

        UriComponentsBuilder builder1 = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/api/submit/1"));

        HttpEntity<Integer> entity1 = new HttpEntity<>(headers);

        ResponseEntity<TimeTable> response = testRestTemplate.exchange(
                builder1.toUriString(), HttpMethod.GET, entity1, TimeTable.class);

        Assert.assertNotNull(response.getBody());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}
