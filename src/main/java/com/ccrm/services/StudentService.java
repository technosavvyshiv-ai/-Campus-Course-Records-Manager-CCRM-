package com.ccrm.services;

import com.ccrm.core.DataStore;
import com.ccrm.model.Student;
import com.ccrm.exceptions.StudentNotFoundException;
import java.util.List;
import java.util.List;

public class StudentService {
    private final DataStore dataStore;

    public StudentService() {
        this.dataStore = DataStore.getInstance();
    }

    
    public Student createStudent(Student student) {
        dataStore.addStudent(student);
        return student;
    }

  
    public Student getStudentById(String studentId) throws StudentNotFoundException {
        Student student = dataStore.getStudent(studentId);
        if (student == null) {
            throw new StudentNotFoundException(studentId);
        }
        return student;
    }

   
    public List<Student> getAllStudents() {
        return dataStore.getAllStudents();
    }

    public List<Student> getActiveStudents() {
        return dataStore.getActiveStudents();
    }

   
    public Student updateStudent(Student student) throws StudentNotFoundException {
        Student existingStudent = getStudentById(student.getId());
        dataStore.addStudent(student); // Replace existing
        return student;
    }

    public void deactivateStudent(String studentId) throws StudentNotFoundException {
        Student student = getStudentById(studentId);
        student.setActive(false);
    }

    public double calculateStudentGPA(String studentId) throws StudentNotFoundException {
        Student student = getStudentById(studentId);
        return dataStore.calculateStudentGPA(studentId);
    }

    
    public int getStudentCreditLoad(String studentId) throws StudentNotFoundException {
        Student student = getStudentById(studentId);
        return dataStore.calculateStudentCredits(studentId);
    }

    
    public boolean canEnrollAdditionalCredits(String studentId, int additionalCredits) throws StudentNotFoundException {
        Student student = getStudentById(studentId);
        int currentCredits = getStudentCreditLoad(studentId);
        return (currentCredits + additionalCredits) <= DataStore.getMaxCreditsPerSemester();
    }

}
