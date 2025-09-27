package com.ccrm.services;

import com.ccrm.core.DataStore;
import com.ccrm.model.Course;
import com.ccrm.exceptions.CourseNotFoundException;
import com.ccrm.interfaces.Searchable;
import com.ccrm.interfaces.Searchable.SearchCriteria;
import com.ccrm.interfaces.Searchable.Predicate;
import com.ccrm.enums.Department;
import com.ccrm.enums.Semester;
import java.util.List;
import java.util.stream.Collectors;

public class CourseService implements Searchable<Course> {
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

    // Searchable interface implementation
    @Override
    public List<Course> searchByField(String field, String value) {
        return dataStore.getAllCourses().stream()
                .filter(course -> {
                    switch (field.toLowerCase()) {
                        case "courseid":
                        case "id":
                            return course.getCourseId().toLowerCase().contains(value.toLowerCase());
                        case "coursecode":
                        case "code":
                            return course.getCourseCode().toLowerCase().contains(value.toLowerCase());
                        case "title":
                            return course.getTitle().toLowerCase().contains(value.toLowerCase());
                        case "instructorid":
                        case "instructor":
                            return course.getInstructorId().toLowerCase().contains(value.toLowerCase());
                        case "department":
                            return course.getDepartment().name().toLowerCase().contains(value.toLowerCase());
                        case "semester":
                            return course.getSemester().name().toLowerCase().contains(value.toLowerCase());
                        case "active":
                            return String.valueOf(course.isActive()).equalsIgnoreCase(value);
                        case "credits":
                        case "credithours":
                            return String.valueOf(course.getCreditHours()).equals(value);
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> searchByCriteria(SearchCriteria criteria) {
        return dataStore.getAllCourses().stream()
                .filter(course -> {
                    String fieldValue = getFieldValue(course, criteria.getField());
                    return matchesCriteria(fieldValue, criteria.getValue(), criteria.getOperator());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> filter(Predicate<Course> predicate) {
        return dataStore.getAllCourses().stream()
                .filter(predicate::test)
                .collect(Collectors.toList());
    }

   
    private String getFieldValue(Course course, String field) {
        switch (field.toLowerCase()) {
            case "courseid":
            case "id":
                return course.getCourseId();
            case "coursecode":
            case "code":
                return course.getCourseCode();
            case "title":
                return course.getTitle();
            case "instructorid":
            case "instructor":
                return course.getInstructorId();
            case "department":
                return course.getDepartment().name();
            case "semester":
                return course.getSemester().name();
            case "active":
                return String.valueOf(course.isActive());
            case "credits":
            case "credithours":
                return String.valueOf(course.getCreditHours());
            case "description":
                return course.getDescription() != null ? course.getDescription() : "";
            default:
                return "";
        }
    }

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
