package com.ccrm;

import java.util.*;
import java.io.*;
import java.time.LocalDate;

/**
 * Simple Campus Course & Records Manager
 * Basic implementation meeting core requirements
 */
public class SimpleCampusManager {
    private static Scanner scanner = new Scanner(System.in);
    private static List<Student> students = new ArrayList<>();
    private static List<Course> courses = new ArrayList<>();
    private static List<Enrollment> enrollments = new ArrayList<>();
    
    public static void main(String[] args) {
        System.out.println("=== Simple Campus Course & Records Manager ===");
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1: addStudent(); break;
                case 2: viewStudents(); break;
                case 3: addCourse(); break;
                case 4: viewCourses(); break;
                case 5: enrollStudent(); break;
                case 6: recordGrade(); break;
                case 7: viewTranscript(); break;
                case 8: exportData(); break;
                case 9: importData(); break;
                case 0: running = false; break;
                default: System.out.println("Invalid choice!");
            }
        }
    }
    
    private static void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Add Student");
        System.out.println("2. View Students");
        System.out.println("3. Add Course");
        System.out.println("4. View Courses");
        System.out.println("5. Enroll Student");
        System.out.println("6. Record Grade");
        System.out.println("7. View Transcript");
        System.out.println("8. Export Data");
        System.out.println("9. Import Data");
        System.out.println("0. Exit");
    }
    
    private static void addStudent() {
        System.out.println("\n=== ADD STUDENT ===");
        String id = getStringInput("Student ID: ");
        String name = getStringInput("Full Name: ");
        String email = getStringInput("Email: ");
        
        Student student = new Student(id, name, email);
        students.add(student);
        System.out.println("Student added successfully!");
    }
    
    private static void viewStudents() {
        System.out.println("\n=== ALL STUDENTS ===");
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            for (Student student : students) {
                System.out.println(student);
            }
        }
    }
    
    private static void addCourse() {
        System.out.println("\n=== ADD COURSE ===");
        String id = getStringInput("Course ID: ");
        String title = getStringInput("Course Title: ");
        int credits = getIntInput("Credit Hours: ");
        
        Course course = new Course(id, title, credits);
        courses.add(course);
        System.out.println("Course added successfully!");
    }
    
    private static void viewCourses() {
        System.out.println("\n=== ALL COURSES ===");
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            for (Course course : courses) {
                System.out.println(course);
            }
        }
    }
    
    private static void enrollStudent() {
        System.out.println("\n=== ENROLL STUDENT ===");
        String studentId = getStringInput("Student ID: ");
        String courseId = getStringInput("Course ID: ");
        
        Student student = findStudent(studentId);
        Course course = findCourse(courseId);
        
        if (student != null && course != null) {
            Enrollment enrollment = new Enrollment(studentId, courseId);
            enrollments.add(enrollment);
            System.out.println("Student enrolled successfully!");
        } else {
            System.out.println("Student or course not found!");
        }
    }
    
    private static void recordGrade() {
        System.out.println("\n=== RECORD GRADE ===");
        String studentId = getStringInput("Student ID: ");
        String courseId = getStringInput("Course ID: ");
        double grade = getDoubleInput("Grade (0-100): ");
        
        Enrollment enrollment = findEnrollment(studentId, courseId);
        if (enrollment != null) {
            enrollment.setGrade(grade);
            System.out.println("Grade recorded successfully!");
        } else {
            System.out.println("Enrollment not found!");
        }
    }
    
    private static void viewTranscript() {
        System.out.println("\n=== STUDENT TRANSCRIPT ===");
        String studentId = getStringInput("Student ID: ");
        
        Student student = findStudent(studentId);
        if (student != null) {
            System.out.println("\nStudent: " + student.getName());
            System.out.println("Email: " + student.getEmail());
            System.out.println("\nCourses:");
            
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getStudentId().equals(studentId)) {
                    Course course = findCourse(enrollment.getCourseId());
                    if (course != null) {
                        System.out.println(course.getTitle() + " - Grade: " + enrollment.getGrade());
                    }
                }
            }
        } else {
            System.out.println("Student not found!");
        }
    }
    
    private static void exportData() {
        try {
            PrintWriter writer = new PrintWriter("students.csv");
            writer.println("ID,Name,Email");
            for (Student student : students) {
                writer.println(student.getId() + "," + student.getName() + "," + student.getEmail());
            }
            writer.close();
            System.out.println("Data exported to students.csv");
        } catch (IOException e) {
            System.out.println("Export failed: " + e.getMessage());
        }
    }
    
    private static void importData() {
        try {
            Scanner fileScanner = new Scanner(new File("students.csv"));
            fileScanner.nextLine(); // Skip header
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    Student student = new Student(parts[0], parts[1], parts[2]);
                    students.add(student);
                }
            }
            fileScanner.close();
            System.out.println("Data imported successfully!");
        } catch (IOException e) {
            System.out.println("Import failed: " + e.getMessage());
        }
    }
    
    // Helper methods
    private static Student findStudent(String id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }
    
    private static Course findCourse(String id) {
        for (Course course : courses) {
            if (course.getId().equals(id)) {
                return course;
            }
        }
        return null;
    }
    
    private static Enrollment findEnrollment(String studentId, String courseId) {
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudentId().equals(studentId) && 
                enrollment.getCourseId().equals(courseId)) {
                return enrollment;
            }
        }
        return null;
    }
    
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}

// Simple model classes
class Student {
    private String id;
    private String name;
    private String email;
    
    public Student(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    
    @Override
    public String toString() {
        return "Student[ID: " + id + ", Name: " + name + ", Email: " + email + "]";
    }
}

class Course {
    private String id;
    private String title;
    private int credits;
    
    public Course(String id, String title, int credits) {
        this.id = id;
        this.title = title;
        this.credits = credits;
    }
    
    public String getId() { return id; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    
    @Override
    public String toString() {
        return "Course[ID: " + id + ", Title: " + title + ", Credits: " + credits + "]";
    }
}

class Enrollment {
    private String studentId;
    private String courseId;
    private double grade;
    
    public Enrollment(String studentId, String courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.grade = 0.0;
    }
    
    public String getStudentId() { return studentId; }
    public String getCourseId() { return courseId; }
    public double getGrade() { return grade; }
    public void setGrade(double grade) { this.grade = grade; }
}
