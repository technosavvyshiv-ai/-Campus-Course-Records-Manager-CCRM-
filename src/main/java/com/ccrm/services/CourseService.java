package com.ccrm.services;

import com.ccrm.core.DataStore;
import com.ccrm.model.Course;
import com.ccrm.exceptions.CourseNotFoundException;
import com.ccrm.enums.Department;
import com.ccrm.enums.Semester;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for course management operations.
 * Demonstrates service layer pattern and business logic encapsulation.
 */
public class CourseService {
    private final DataStore dataStore;

    public CourseService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Creates a new course.
     * @param course The course to create
     * @return The created course
     */
    public Course createCourse(Course course) {
        dataStore.addCourse(course);
        return course;
    }

    /**
     * Retrieves a course by ID.
     * @param courseId The course ID
     * @return The course
     * @throws CourseNotFoundException if course is not found
     */
    public Course getCourseById(String courseId) throws CourseNotFoundException {
        Course course = dataStore.getCourse(courseId);
        if (course == null) {
            throw new CourseNotFoundException(courseId);
        }
        return course;
    }

    /**
     * Retrieves all courses.
     * @return List of all courses
     */
    public List<Course> getAllCourses() {
        return dataStore.getAllCourses();
    }

    /**
     * Retrieves all active courses.
     * @return List of active courses
     */
    public List<Course> getActiveCourses() {
        return dataStore.getActiveCourses();
    }

    /**
     * Updates a course.
     * @param course The course to update
     * @return The updated course
     * @throws CourseNotFoundException if course is not found
     */
    public Course updateCourse(Course course) throws CourseNotFoundException {
        Course existingCourse = getCourseById(course.getCourseId());
        dataStore.addCourse(course); // Replace existing
        return course;
    }

    /**
     * Deactivates a course.
     * @param courseId The course ID
     * @throws CourseNotFoundException if course is not found
     */
    public void deactivateCourse(String courseId) throws CourseNotFoundException {
        Course course = getCourseById(courseId);
        course.setActive(false);
    }

    /**
     * Gets courses by department.
     * @param department The department
     * @return List of courses in the department
     */
    public List<Course> getCoursesByDepartment(Department department) {
        return dataStore.getCoursesByDepartment(department.name());
    }

    /**
     * Gets courses by semester.
     * @param semester The semester
     * @return List of courses in the semester
     */
    public List<Course> getCoursesBySemester(Semester semester) {
        return dataStore.getCoursesBySemester(semester.name());
    }

    /**
     * Gets courses by instructor.
     * @param instructorId The instructor ID
     * @return List of courses taught by the instructor
     */
    public List<Course> getCoursesByInstructor(String instructorId) {
        return dataStore.getAllCourses().stream()
                .filter(course -> course.getInstructorId().equals(instructorId))
                .collect(Collectors.toList());
    }

    /**
     * Gets enrollment count for a course.
     * @param courseId The course ID
     * @return Number of enrolled students
     */
    public int getEnrollmentCount(String courseId) {
        return dataStore.getEnrollmentsByCourse(courseId).size();
    }

    /**
     * Checks if a course has available seats.
     * @param courseId The course ID
     * @param maxCapacity Maximum capacity (if applicable)
     * @return true if course has available seats
     */
    public boolean hasAvailableSeats(String courseId, int maxCapacity) {
        int currentEnrollments = getEnrollmentCount(courseId);
        return currentEnrollments < maxCapacity;
    }

    // Simplified service: explicit methods only per requirements
}
