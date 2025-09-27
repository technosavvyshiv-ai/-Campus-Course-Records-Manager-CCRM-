package com.ccrm.exceptions;


public class StudentNotFoundException extends RuntimeException {
    private final String studentId;

    public StudentNotFoundException(String studentId) {
        super(String.format("Student with ID '%s' not found", studentId));
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }
}
