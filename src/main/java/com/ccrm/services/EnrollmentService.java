package com.ccrm.services;

import com.ccrm.core.DataStore;
import com.ccrm.model.Enrollment;
import com.ccrm.model.Student;
import com.ccrm.model.Course;
import com.ccrm.exceptions.*;
 
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for enrollment and grading operations.
 * Demonstrates service layer pattern and business logic encapsulation.
 */
public class EnrollmentService {
    private final DataStore dataStore;

    public EnrollmentService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Enrolls a student in a course.
     * @param studentId The student ID
     * @param courseId The course ID
     * @return The created enrollment
     * @throws StudentNotFoundException if student is not found
     * @throws CourseNotFoundException if course is not found
     * @throws MaxCreditLimitExceededException if credit limit would be exceeded
     */
    public Enrollment enrollStudent(String studentId, String courseId) 
            throws StudentNotFoundException, CourseNotFoundException, MaxCreditLimitExceededException {
        
        Student student = dataStore.getStudent(studentId);
        if (student == null) {
            throw new StudentNotFoundException(studentId);
        }
        
        Course course = dataStore.getCourse(courseId);
        if (course == null) {
            throw new CourseNotFoundException(courseId);
        }
        
        if (!student.isActive() || !course.isActive()) {
            throw new IllegalStateException("Cannot enroll inactive student or in inactive course");
        }
        
        // Check if already enrolled
        boolean alreadyEnrolled = dataStore.getEnrollmentsByStudent(studentId).stream()
                .anyMatch(enrollment -> enrollment.getCourseId().equals(courseId) && enrollment.isActive());
        
        if (alreadyEnrolled) {
            throw new IllegalStateException("Student is already enrolled in this course");
        }
        
        // Check credit limit
        int currentCredits = dataStore.calculateStudentCredits(studentId);
        if (currentCredits + course.getCreditHours() > DataStore.getMaxCreditsPerSemester()) {
            throw new MaxCreditLimitExceededException(
                currentCredits + course.getCreditHours(), 
                DataStore.getMaxCreditsPerSemester()
            );
        }
        
        // Create enrollment
        String enrollmentId = UUID.randomUUID().toString();
        Enrollment enrollment = new Enrollment(enrollmentId, studentId, courseId);
        dataStore.addEnrollment(enrollment);
        
        // Update student's enrolled courses
        student.enrollInCourse(courseId);
        
        return enrollment;
    }

    /**
     * Unenrolls a student from a course.
     * @param studentId The student ID
     * @param courseId The course ID
     * @throws StudentNotFoundException if student is not found
     * @throws CourseNotFoundException if course is not found
     * @throws EnrollmentNotFoundException if enrollment is not found
     */
    public void unenrollStudent(String studentId, String courseId) 
            throws StudentNotFoundException, CourseNotFoundException, EnrollmentNotFoundException {
        
        Student student = dataStore.getStudent(studentId);
        if (student == null) {
            throw new StudentNotFoundException(studentId);
        }
        
        Course course = dataStore.getCourse(courseId);
        if (course == null) {
            throw new CourseNotFoundException(courseId);
        }
        
        List<Enrollment> enrollments = dataStore.getEnrollmentsByStudent(studentId);
        Enrollment enrollment = enrollments.stream()
                .filter(e -> e.getCourseId().equals(courseId) && e.isActive())
                .findFirst()
                .orElse(null);
        
        if (enrollment == null) {
            throw new EnrollmentNotFoundException("No active enrollment found for student " + studentId + " in course " + courseId);
        }
        
        enrollment.withdraw();
        student.unenrollFromCourse(courseId);
    }

    /**
     * Records a grade for an enrollment.
     * @param studentId The student ID
     * @param courseId The course ID
     * @param numericGrade The numeric grade (0-100)
     * @throws StudentNotFoundException if student is not found
     * @throws CourseNotFoundException if course is not found
     * @throws EnrollmentNotFoundException if enrollment is not found
     * @throws InvalidGradeException if grade is invalid
     */
    public void recordGrade(String studentId, String courseId, double numericGrade) 
            throws StudentNotFoundException, CourseNotFoundException, 
                   EnrollmentNotFoundException, InvalidGradeException {
        
        Student student = dataStore.getStudent(studentId);
        if (student == null) {
            throw new StudentNotFoundException(studentId);
        }
        
        Course course = dataStore.getCourse(courseId);
        if (course == null) {
            throw new CourseNotFoundException(courseId);
        }
        
        List<Enrollment> enrollments = dataStore.getEnrollmentsByStudent(studentId);
        Enrollment enrollment = enrollments.stream()
                .filter(e -> e.getCourseId().equals(courseId) && e.isActive())
                .findFirst()
                .orElse(null);
        
        if (enrollment == null) {
            throw new EnrollmentNotFoundException("No active enrollment found for student " + studentId + " in course " + courseId);
        }
        
        if (numericGrade < 0 || numericGrade > 100) {
            throw new InvalidGradeException(numericGrade);
        }
        
        enrollment.recordGrade(numericGrade);
        
        // Update student's GPA
        double newGPA = dataStore.calculateStudentGPA(studentId);
        student.setCurrentGPA(newGPA);
    }

    /**
     * Gets all enrollments for a student.
     * @param studentId The student ID
     * @return List of enrollments
     * @throws StudentNotFoundException if student is not found
     */
    public List<Enrollment> getStudentEnrollments(String studentId) throws StudentNotFoundException {
        Student student = dataStore.getStudent(studentId);
        if (student == null) {
            throw new StudentNotFoundException(studentId);
        }
        
        return dataStore.getEnrollmentsByStudent(studentId);
    }

    /**
     * Gets all enrollments for a course.
     * @param courseId The course ID
     * @return List of enrollments
     * @throws CourseNotFoundException if course is not found
     */
    public List<Enrollment> getCourseEnrollments(String courseId) throws CourseNotFoundException {
        Course course = dataStore.getCourse(courseId);
        if (course == null) {
            throw new CourseNotFoundException(courseId);
        }
        
        return dataStore.getEnrollmentsByCourse(courseId);
    }

    /**
     * Gets active enrollments for a student.
     * @param studentId The student ID
     * @return List of active enrollments
     * @throws StudentNotFoundException if student is not found
     */
    public List<Enrollment> getActiveStudentEnrollments(String studentId) throws StudentNotFoundException {
        return getStudentEnrollments(studentId).stream()
                .filter(Enrollment::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Gets completed enrollments for a student.
     * @param studentId The student ID
     * @return List of completed enrollments
     * @throws StudentNotFoundException if student is not found
     */
    public List<Enrollment> getCompletedStudentEnrollments(String studentId) throws StudentNotFoundException {
        return getStudentEnrollments(studentId).stream()
                .filter(Enrollment::isCompleted)
                .collect(Collectors.toList());
    }

    /**
     * Calculates student's GPA.
     * @param studentId The student ID
     * @return The calculated GPA
     * @throws StudentNotFoundException if student is not found
     */
    public double calculateStudentGPA(String studentId) throws StudentNotFoundException {
        Student student = dataStore.getStudent(studentId);
        if (student == null) {
            throw new StudentNotFoundException(studentId);
        }
        
        return dataStore.calculateStudentGPA(studentId);
    }

    /**
     * Gets student's current credit load.
     * @param studentId The student ID
     * @return Current credit hours
     * @throws StudentNotFoundException if student is not found
     */
    public int getStudentCreditLoad(String studentId) throws StudentNotFoundException {
        Student student = dataStore.getStudent(studentId);
        if (student == null) {
            throw new StudentNotFoundException(studentId);
        }
        
        return dataStore.calculateStudentCredits(studentId);
    }

    // Simplified: removed generic search/criteria helpers
}
