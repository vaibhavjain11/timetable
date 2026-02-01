package com.brillio.timetable.service;

import com.brillio.timetable.dao.InMemoryDao;
import com.brillio.timetable.entities.course.Course;
import com.brillio.timetable.entities.instructor.Instructor;
import com.brillio.timetable.enums.DayOfWeek;
import com.brillio.timetable.responses.AvailableCourses;
import com.brillio.timetable.services.FilterService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import utils.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = FilterService.class)
public class FilterServiceTest {

    @MockBean
    InMemoryDao inMemoryDao;

    @Autowired
    FilterService filterService;

    private static Map<String, List<Instructor>> instructorMap = new HashMap<>();

    private static Map<String, Course> courseMap = new HashMap<>();

    @BeforeAll
    public static void init() {
        Instructor instructor = new Instructor();
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

        Course course1 = new Course("1001","Biology","Bio123",20, 10);
        Course course2 = new Course("1002","Chemistry","Chem123",30, 20);
        courseMap.put("1001", course1);
        courseMap.put("1002", course2);
    }

    @Test
    public void testFilteredCriteria(){

        when(inMemoryDao.getInstructors()).thenReturn(instructorMap);
        when(inMemoryDao.getCourse()).thenReturn(courseMap);

        List<AvailableCourses> availableCourses = filterService.getFilteredResult(DayOfWeek.MON,"1001");

        Assertions.assertNotNull(availableCourses);
        Assertions.assertEquals(availableCourses.size(),1);
        Assertions.assertEquals(1,availableCourses.get(0).getInstructors().size());
        Assertions.assertEquals("Vikas",availableCourses.get(0).getInstructors().get(0).getInstructorName());
    }

    @Test
    public void testFilterForEmptyList() {
        when(inMemoryDao.getInstructors()).thenReturn(instructorMap);
        when(inMemoryDao.getCourse()).thenReturn(courseMap);
        List<AvailableCourses> availableCourses = filterService.getFilteredResult(DayOfWeek.WED,"1001");

        Assertions.assertEquals(0,availableCourses.size());

    }


}
