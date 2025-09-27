package com.ccrm.services;

import com.ccrm.core.DataStore;
import com.ccrm.model.Student;
import com.ccrm.exceptions.StudentNotFoundException;
import com.ccrm.interfaces.Searchable;
import com.ccrm.interfaces.Searchable.SearchCriteria;
import com.ccrm.interfaces.Searchable.Predicate;
import java.util.List;
import java.util.stream.Collectors;

public class StudentService implements Searchable<Student> {
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

    
    @Override
    public List<Student> searchByField(String field, String value) {
        return dataStore.getAllStudents().stream()
                .filter(student -> {
                    switch (field.toLowerCase()) {
                        case "id":
                            return student.getId().toLowerCase().contains(value.toLowerCase());
                        case "registrationnumber":
                        case "regno":
                            return student.getRegistrationNumber().toLowerCase().contains(value.toLowerCase());
                        case "name":
                        case "fullname":
                            return student.getFullName().toLowerCase().contains(value.toLowerCase());
                        case "email":
                            return student.getEmail().toLowerCase().contains(value.toLowerCase());
                        case "active":
                            return String.valueOf(student.isActive()).equalsIgnoreCase(value);
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> searchByCriteria(SearchCriteria criteria) {
        return dataStore.getAllStudents().stream()
                .filter(student -> {
                    String fieldValue = getFieldValue(student, criteria.getField());
                    return matchesCriteria(fieldValue, criteria.getValue(), criteria.getOperator());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> filter(Predicate<Student> predicate) {
        return dataStore.getAllStudents().stream()
                .filter(predicate::test)
                .collect(Collectors.toList());
    }

   
    private String getFieldValue(Student student, String field) {
        switch (field.toLowerCase()) {
            case "id":
                return student.getId();
            case "registrationnumber":
            case "regno":
                return student.getRegistrationNumber();
            case "name":
            case "fullname":
                return student.getFullName();
            case "email":
                return student.getEmail();
            case "active":
                return String.valueOf(student.isActive());
            case "gpa":
                return String.valueOf(student.getCurrentGPA());
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
