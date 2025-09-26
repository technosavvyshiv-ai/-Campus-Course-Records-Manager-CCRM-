package com.ccrm.exceptions;

public class EnrollmentNotFoundException extends RuntimeException {
    private final String enrollmentId;

    public EnrollmentNotFoundException(String enrollmentId) {
        super(String.format("Enrollment with ID '%s' not found", enrollmentId));
        this.enrollmentId = enrollmentId;
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }
}
