package com.ccrm.exceptions;


public class CourseNotFoundException extends RuntimeException {
    private final String courseId;

    public CourseNotFoundException(String courseId) {
        super(String.format("Course with ID '%s' not found", courseId));
        this.courseId = courseId;
    }

    public String getCourseId() {
        return courseId;
    }
}
