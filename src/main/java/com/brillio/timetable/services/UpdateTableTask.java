package com.brillio.timetable.services;

import com.brillio.timetable.constants.Constants;
import com.brillio.timetable.dao.IDao;
import com.brillio.timetable.entities.instructor.Instructor;
import com.brillio.timetable.entities.instructor.TimeSlotKey;
import com.brillio.timetable.entities.timetable.Record;
import com.brillio.timetable.entities.timetable.TimeTable;
import com.brillio.timetable.enums.Status;
import com.brillio.timetable.enums.TimeSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

public class UpdateTableTask implements Callable<TimeTable> {

    private final Logger LOG = LoggerFactory.getLogger(UpdateTableTask.class);
    String code;
    String name;
    int dayOfWeek;
    int studentId;
    TimeSlot timeSlot;
    IDao inMemoryDao;
    ReentrantLock lock;

    public UpdateTableTask(Integer studentId, String courseCode, String name, int dayOfWeek, TimeSlot timeSlot, IDao inMemoryDao, ReentrantLock lock) {
        this.code = courseCode;
        this.name = name;
        this.dayOfWeek = dayOfWeek;
        this.studentId = studentId;
        this.inMemoryDao = inMemoryDao;
        this.timeSlot = timeSlot;
        this.lock = lock;
    }


    @Override
    public TimeTable call() throws Exception {

        TimeTable timeTable = inMemoryDao.getStudentTimeTable().getMap().get(studentId);
        if (timeTable != null) {
            if (timeTable.getStatus() == Status.NOT_SUBMITTED) {
                updateTask(studentId);
            } else {
                populateErrors("Time table is frozen. You can't submit now", timeTable);
            }
        } else {
            updateTask(studentId);
        }

        return inMemoryDao.getStudentTimeTable().getMap().get(studentId);
    }

    private void updateTask(int studentId) {

        TimeTable timeTable = inMemoryDao.getStudentTimeTable().getMap().get(studentId);
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

                // Multiple entries for same student may come in different thread which have same time table
                // if timetable is null, then we will have to block all thread which have null timetable
                // but if timetable is not null then we can block only those thread which are trying to update that table
                if (timeTable == null) {
                    LOG.info("Time table is null, using lock for student {}", studentId);
                    lock.lock();
                    timeTable = inMemoryDao.getStudentTimeTable().getMap().get(studentId);
                    if (timeTable == null) {
                        timeTable = new TimeTable();
                        updateTable(timeTable, ins);
                    } else {

                        LOG.info("Time table is not null in lock process for student {}", studentId);
                        updateTable(timeTable, ins);
                    }
                    inMemoryDao.getStudentTimeTable().getMap().put(studentId, timeTable);
                    lock.unlock();
                } else {
                    synchronized (timeTable) {
                        LOG.info("Time table is not null in synchronized for student id {}", studentId);
                        updateTable(timeTable, ins);
                        inMemoryDao.getStudentTimeTable().getMap().put(studentId, timeTable);
                    }
                }

            } else {
                timeTable = populateErrors("Instructor is full packed or you requested on the day when instructor wants to chill out.. Try later", timeTable);
                inMemoryDao.getStudentTimeTable().getMap().put(studentId, timeTable);
            }
        } else {
            timeTable = populateErrors("Requested Instructor is not registered", timeTable);
            inMemoryDao.getStudentTimeTable().getMap().put(studentId, timeTable);
        }
    }

    private void updateTable(TimeTable timeTable, Instructor ins) {
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
    }

    private boolean checkIfInstructorHasSlotAvailable(Instructor instructor) {
        if (instructor.getSlots().get(new TimeSlotKey(dayOfWeek, timeSlot.getValue())) == null)
            return true;
        if (instructor.getSlots().get(new TimeSlotKey(dayOfWeek, timeSlot.getValue())).getNumOfStudents() < instructor.getNumberOfStudents())
            return true;
        return false;
    }

    private TimeTable populateErrors(String message, TimeTable timeTable) {
        if (timeTable == null) {
            timeTable = new TimeTable();
        }
        timeTable.setStudentId(studentId);
        timeTable.setError(message);
        return timeTable;
    }
}
