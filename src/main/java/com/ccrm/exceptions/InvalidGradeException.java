package com.ccrm.exceptions;

public class InvalidGradeException extends Exception {
    private final double invalidGrade;

    public InvalidGradeException(double invalidGrade) {
        super(String.format("Invalid grade: %.1f. Grade must be between 0.0 and 100.0", invalidGrade));
        this.invalidGrade = invalidGrade;
    }

    public double getInvalidGrade() {
        return invalidGrade;
    }
}
