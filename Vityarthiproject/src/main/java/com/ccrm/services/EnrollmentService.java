package com.ccrm.services;

import com.ccrm.core.DataStore;
import com.ccrm.model.Enrollment;
import com.ccrm.model.Student;
import com.ccrm.model.Course;
import com.ccrm.exceptions.*;
import com.ccrm.interfaces.Searchable;
import com.ccrm.interfaces.Searchable.SearchCriteria;
import com.ccrm.interfaces.Searchable.Predicate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EnrollmentService implements Searchable<Enrollment> {
    private final DataStore dataStore;

    public EnrollmentService() {
        this.dataStore = DataStore.getInstance();
    }

    
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
        
      
        student.enrollInCourse(courseId);
        
        return enrollment;
    }

    
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

    
    public List<Enrollment> getStudentEnrollments(String studentId) throws StudentNotFoundException {
        Student student = dataStore.getStudent(studentId);
        if (student == null) {
            throw new StudentNotFoundException(studentId);
        }
        
        return dataStore.getEnrollmentsByStudent(studentId);
    }

  
    public List<Enrollment> getCourseEnrollments(String courseId) throws CourseNotFoundException {
        Course course = dataStore.getCourse(courseId);
        if (course == null) {
            throw new CourseNotFoundException(courseId);
        }
        
        return dataStore.getEnrollmentsByCourse(courseId);
    }

    
    public List<Enrollment> getActiveStudentEnrollments(String studentId) throws StudentNotFoundException {
        return getStudentEnrollments(studentId).stream()
                .filter(Enrollment::isActive)
                .collect(Collectors.toList());
    }

    
    public List<Enrollment> getCompletedStudentEnrollments(String studentId) throws StudentNotFoundException {
        return getStudentEnrollments(studentId).stream()
                .filter(Enrollment::isCompleted)
                .collect(Collectors.toList());
    }

    public double calculateStudentGPA(String studentId) throws StudentNotFoundException {
        Student student = dataStore.getStudent(studentId);
        if (student == null) {
            throw new StudentNotFoundException(studentId);
        }
        
        return dataStore.calculateStudentGPA(studentId);
    }

   
    public int getStudentCreditLoad(String studentId) throws StudentNotFoundException {
        Student student = dataStore.getStudent(studentId);
        if (student == null) {
            throw new StudentNotFoundException(studentId);
        }
        
        return dataStore.calculateStudentCredits(studentId);
    }

    // Searchable interface implementation
    @Override
    public List<Enrollment> searchByField(String field, String value) {
        return dataStore.getAllEnrollments().stream()
                .filter(enrollment -> {
                    switch (field.toLowerCase()) {
                        case "enrollmentid":
                        case "id":
                            return enrollment.getEnrollmentId().toLowerCase().contains(value.toLowerCase());
                        case "studentid":
                        case "student":
                            return enrollment.getStudentId().toLowerCase().contains(value.toLowerCase());
                        case "courseid":
                        case "course":
                            return enrollment.getCourseId().toLowerCase().contains(value.toLowerCase());
                        case "active":
                            return String.valueOf(enrollment.isActive()).equalsIgnoreCase(value);
                        case "completed":
                            return String.valueOf(enrollment.isCompleted()).equalsIgnoreCase(value);
                        case "grade":
                            return enrollment.getLetterGrade() != null && 
                                   enrollment.getLetterGrade().name().toLowerCase().contains(value.toLowerCase());
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Enrollment> searchByCriteria(SearchCriteria criteria) {
        return dataStore.getAllEnrollments().stream()
                .filter(enrollment -> {
                    String fieldValue = getFieldValue(enrollment, criteria.getField());
                    return matchesCriteria(fieldValue, criteria.getValue(), criteria.getOperator());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Enrollment> filter(Predicate<Enrollment> predicate) {
        return dataStore.getAllEnrollments().stream()
                .filter(predicate::test)
                .collect(Collectors.toList());
    }

    /**
     * Gets the value of a field from an enrollment object.
     * @param enrollment The enrollment object
     * @param field The field name
     * @return The field value as string
     */
    private String getFieldValue(Enrollment enrollment, String field) {
        switch (field.toLowerCase()) {
            case "enrollmentid":
            case "id":
                return enrollment.getEnrollmentId();
            case "studentid":
            case "student":
                return enrollment.getStudentId();
            case "courseid":
            case "course":
                return enrollment.getCourseId();
            case "active":
                return String.valueOf(enrollment.isActive());
            case "completed":
                return String.valueOf(enrollment.isCompleted());
            case "grade":
                return enrollment.getLetterGrade() != null ? enrollment.getLetterGrade().name() : "";
            case "numericgrade":
                return String.valueOf(enrollment.getNumericGrade());
            default:
                return "";
        }
    }

    /**
     * Checks if a field value matches the criteria.
     * @param fieldValue The field value
     * @param searchValue The search value
     * @param operator The search operator
     * @return true if matches
     */
    private boolean matchesCriteria(String fieldValue, String searchValue, Searchable.SearchOperator operator) {
        switch (operator) {
            case EQUALS:
                return fieldValue.equalsIgnoreCase(searchValue);
            case CONTAINS:
                return fieldValue.toLowerCase().contains(searchValue.toLowerCase());
            case STARTS_WITH:
                return fieldValue.toLowerCase().startsWith(searchValue.toLowerCase());
            case ENDS_WITH:
                return fieldValue.toLowerCase().endsWith(searchValue.toLowerCase());
            case GREATER_THAN:
                try {
                    return Double.parseDouble(fieldValue) > Double.parseDouble(searchValue);
                } catch (NumberFormatException e) {
                    return false;
                }
            case LESS_THAN:
                try {
                    return Double.parseDouble(fieldValue) < Double.parseDouble(searchValue);
                } catch (NumberFormatException e) {
                    return false;
                }
            default:
                return false;
        }
    }
}
