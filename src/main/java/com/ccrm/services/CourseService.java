package com.ccrm.services;

import com.ccrm.core.DataStore;
import com.ccrm.model.Course;
import com.ccrm.exceptions.CourseNotFoundException;
import com.ccrm.enums.Department;
import com.ccrm.enums.Semester;
import java.util.List;
import java.util.stream.Collectors;


public class CourseService {
    private final DataStore dataStore;

    public CourseService() {
        this.dataStore = DataStore.getInstance();
    }

 
    public Course createCourse(Course course) {
        dataStore.addCourse(course);
        return course;
    }

   
    public Course getCourseById(String courseId) throws CourseNotFoundException {
        Course course = dataStore.getCourse(courseId);
        if (course == null) {
            throw new CourseNotFoundException(courseId);
        }
        return course;
    }

   
    public List<Course> getAllCourses() {
        return dataStore.getAllCourses();
    }

    
    public List<Course> getActiveCourses() {
        return dataStore.getActiveCourses();
    }

  
    public Course updateCourse(Course course) throws CourseNotFoundException {
        Course existingCourse = getCourseById(course.getCourseId());
        dataStore.addCourse(course); // Replace existing
        return course;
    }

  
    public void deactivateCourse(String courseId) throws CourseNotFoundException {
        Course course = getCourseById(courseId);
        course.setActive(false);
    }

   
    public List<Course> getCoursesByDepartment(Department department) {
        return dataStore.getCoursesByDepartment(department.name());
    }

    public List<Course> getCoursesBySemester(Semester semester) {
        return dataStore.getCoursesBySemester(semester.name());
    }

    public List<Course> getCoursesByInstructor(String instructorId) {
        return dataStore.getAllCourses().stream()
                .filter(course -> course.getInstructorId().equals(instructorId))
                .collect(Collectors.toList());
    }

    
    public int getEnrollmentCount(String courseId) {
        return dataStore.getEnrollmentsByCourse(courseId).size();
    }

   
    public boolean hasAvailableSeats(String courseId, int maxCapacity) {
        int currentEnrollments = getEnrollmentCount(courseId);
        return currentEnrollments < maxCapacity;
    }

    // Simplified service: explicit methods only per requirements
}
