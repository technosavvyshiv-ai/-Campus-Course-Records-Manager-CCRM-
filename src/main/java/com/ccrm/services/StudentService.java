package com.ccrm.services;

import com.ccrm.core.DataStore;
import com.ccrm.model.Student;
import com.ccrm.exceptions.StudentNotFoundException;
import java.util.List;
import java.util.List;

/**
 * Service class for student management operations.
 * Demonstrates service layer pattern and business logic encapsulation.
 */
public class StudentService {
    private final DataStore dataStore;

    public StudentService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Creates a new student.
     * @param student The student to create
     * @return The created student
     */
    public Student createStudent(Student student) {
        dataStore.addStudent(student);
        return student;
    }

    /**
     * Retrieves a student by ID.
     * @param studentId The student ID
     * @return The student
     * @throws StudentNotFoundException if student is not found
     */
    public Student getStudentById(String studentId) throws StudentNotFoundException {
        Student student = dataStore.getStudent(studentId);
        if (student == null) {
            throw new StudentNotFoundException(studentId);
        }
        return student;
    }

    /**
     * Retrieves all students.
     * @return List of all students
     */
    public List<Student> getAllStudents() {
        return dataStore.getAllStudents();
    }

    /**
     * Retrieves all active students.
     * @return List of active students
     */
    public List<Student> getActiveStudents() {
        return dataStore.getActiveStudents();
    }

    /**
     * Updates a student.
     * @param student The student to update
     * @return The updated student
     * @throws StudentNotFoundException if student is not found
     */
    public Student updateStudent(Student student) throws StudentNotFoundException {
        Student existingStudent = getStudentById(student.getId());
        dataStore.addStudent(student); // Replace existing
        return student;
    }

    /**
     * Deactivates a student.
     * @param studentId The student ID
     * @throws StudentNotFoundException if student is not found
     */
    public void deactivateStudent(String studentId) throws StudentNotFoundException {
        Student student = getStudentById(studentId);
        student.setActive(false);
    }

    /**
     * Calculates student's GPA.
     * @param studentId The student ID
     * @return The calculated GPA
     * @throws StudentNotFoundException if student is not found
     */
    public double calculateStudentGPA(String studentId) throws StudentNotFoundException {
        Student student = getStudentById(studentId);
        return dataStore.calculateStudentGPA(studentId);
    }

    /**
     * Gets student's current credit load.
     * @param studentId The student ID
     * @return Current credit hours
     * @throws StudentNotFoundException if student is not found
     */
    public int getStudentCreditLoad(String studentId) throws StudentNotFoundException {
        Student student = getStudentById(studentId);
        return dataStore.calculateStudentCredits(studentId);
    }

    /**
     * Checks if student can enroll in additional credits.
     * @param studentId The student ID
     * @param additionalCredits Credits to add
     * @return true if enrollment is allowed
     * @throws StudentNotFoundException if student is not found
     */
    public boolean canEnrollAdditionalCredits(String studentId, int additionalCredits) throws StudentNotFoundException {
        Student student = getStudentById(studentId);
        int currentCredits = getStudentCreditLoad(studentId);
        return (currentCredits + additionalCredits) <= DataStore.getMaxCreditsPerSemester();
    }

    // Simplified service: explicit methods only per requirements
}
