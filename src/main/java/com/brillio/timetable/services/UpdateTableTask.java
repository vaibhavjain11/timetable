package com.brillio.timetable.services;

import com.brillio.timetable.constants.Constants;
import com.brillio.timetable.dao.IDao;
import com.brillio.timetable.entities.instructor.Instructor;
import com.brillio.timetable.entities.instructor.TimeSlotKey;
import com.brillio.timetable.entities.timetable.Record;
import com.brillio.timetable.entities.timetable.TimeTable;
import com.brillio.timetable.enums.Status;
import com.brillio.timetable.enums.TimeSlot;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

public class UpdateTableTask implements Callable<TimeTable> {

    String code;
    String name;
    int dayOfWeek;
    int studentId;
    TimeSlot timeSlot;
    IDao inMemoryDao;

    public UpdateTableTask(Integer studentId, String courseCode, String name, int dayOfWeek, TimeSlot timeSlot, IDao inMemoryDao) {
        this.code = courseCode;
        this.name = name;
        this.dayOfWeek = dayOfWeek;
        this.studentId = studentId;
        this.inMemoryDao = inMemoryDao;
        this.timeSlot = timeSlot;
    }

    @Override
    public TimeTable call() throws Exception {

        TimeTable timeTable = inMemoryDao.getStudentTimeTable().getMap().get(studentId);
        if (timeTable != null) {
            if (timeTable.getStatus() == Status.NOT_SUBMITTED) {
                updateTask(timeTable);
            } else {
                populateErrors("Time table is frozen. You can't submit now", timeTable);
            }
        } else {
            timeTable = new TimeTable();
            updateTask(timeTable);
        }

        inMemoryDao.getStudentTimeTable().getMap().put(studentId, timeTable);
        return inMemoryDao.getStudentTimeTable().getMap().get(studentId);
    }

    private void updateTask(TimeTable timeTable) {
        List<Instructor> instructors = inMemoryDao.getInstructors().get(code);

        Optional<Instructor> instructor = instructors.stream()
                .filter(ins -> ins.getInstructorName().equals(name)).findAny();


        if (instructor.isPresent()) {
            // checks that the day should be among the configured day for instructor
            if (instructor.get().getDayOfWeek().contains(dayOfWeek) &&
                    // check that student should register in available hours slot and sum
                    // in all slots should not be more than capacity of students teacher can teach
                    checkIfInstructorHasSlotAvailable(instructor.get())) {

                Instructor ins = instructor.get();

                Record record = new Record();
                record.setDayOfWeek(dayOfWeek);
                record.setSlot(timeSlot.getValue());
                record.setCourseCode(code);
                record.setInstructorName(ins.getInstructorName());

                timeTable.setStudentId(studentId);
                timeTable.addRecord(record);
                timeTable.setUnits(timeTable.getUnits() + inMemoryDao.getCourse().get(code).getCourseUnit());
                timeTable.setStatus(Status.NOT_SUBMITTED);
                timeTable.setError(Constants.NO_ERROR);

            } else {
                populateErrors("Instructor is full packed or you requested on the day when instructor wants to chill out.. Try later", timeTable);
            }
        } else {
            populateErrors("Requested Instructor is not registered", timeTable);
        }

    }

    private boolean checkIfInstructorHasSlotAvailable(Instructor instructor) {
        if (instructor.getSlots().get(new TimeSlotKey(dayOfWeek, timeSlot.getValue())) == null)
            return true;
        if (instructor.getSlots().get(new TimeSlotKey(dayOfWeek, timeSlot.getValue())).getNumOfStudents() < instructor.getNumberOfStudents())
            return true;
        return false;
    }

    private void populateErrors(String message, TimeTable timeTable) {
        timeTable.setStudentId(studentId);
        timeTable.setError(message);
    }
}
