

# **Campus Course & Records Manager (CCRM)**

This is a full-featured, console-based Java application built to help you manage core data for a higher education institution. Think of it as a central system for handling student and course information, managing enrollments and grades, and keeping all your data safe with robust file operations.

## ** Project Overview**

The Campus Course & Records Manager (CCRM) project was a hands-on exercise in applying foundational and advanced Java programming concepts. It’s a great way to showcase skills in object-oriented programming, design patterns, modern Java APIs, and file handling.

### **Key Features**

* **Student Management**: Easily create, list, update, or even deactivate student records.  
* **Course Management**: Manage courses and assign instructors to them.  
* **Enrollment & Grading**: Handle student enrollments, enforce credit limits, and calculate student GPAs.  
* **File Operations**: Import and export data in CSV format using modern NIO.2 file operations.  
* **Backup System**: Automatically create timestamped backups to protect your data.  
* **System Reports**: Generate comprehensive reports to give you a clear overview of the system's data.

---

## ** The Evolution of Java**

Java has a rich history of constantly evolving to meet modern development needs. Here’s a quick look at some of its most important milestones:

* **1995: Java 1.0** \- The original release that introduced the "Write Once, Run Anywhere" (WORA) philosophy.  
* **1997: Java 1.1** \- Introduced important additions like inner classes and JDBC for database connectivity.  
* **1998: Java 2 (J2SE, J2EE, J2ME)** \- A major update that established the three main platforms: Standard, Enterprise, and Micro.  
* **2004: Java 5 (J2SE 5.0)** \- A huge release that brought us generics, enums, and the enhanced for-loop, making coding much easier.  
* **2014: Java 8** \- A true game-changer with the introduction of lambda expressions and the Streams API, which enabled a more functional programming style.  
* **2017: Java 9** \- Introduced the Java Platform Module System (JPMS) to help modularize the JDK.  
* **2018: Java 11** \- The first Long-Term Support (LTS) release under the new six-month cadence, which removed some legacy modules.  
* **2023: Java 21** \- The most recent LTS release, featuring modern concurrency with virtual threads and enhanced pattern matching.

---

## ** Java Platform Comparison**

Here’s a simple breakdown of the different Java platforms and what they're used for:

| Platform | Target Environment | Key Features | Use Case |
| :---- | :---- | :---- | :---- |
| **Java SE** (Standard Edition) | Desktop, Servers, Embedded Systems | Core Java APIs, including JVM, JRE, and essential libraries. | Building desktop applications, command-line tools, and fundamental server-side applications. This project is built using Java SE. |
| **Java EE** (Enterprise Edition) | Large-scale, Multi-tiered Applications | APIs for servlets, JSPs, EJB, JPA, and web services. | Developing robust, scalable, and secure enterprise applications, such as e-commerce platforms and banking systems. |
| **Java ME** (Micro Edition) | Mobile, Embedded Devices | A subset of Java SE APIs optimized for resource-constrained devices. | Creating applications for mobile phones, set-top boxes, and other IoT devices. |

---

## ** Java Architecture: JDK, JRE, and JVM**

The Java platform is built on three key components that all work together to compile and run your Java applications:

* **JDK (Java Development Kit)**: This is your complete developer's toolkit. It includes everything you need to write, compile, and debug a Java program, such as the javac compiler and a debugger.  
* **JRE (Java Runtime Environment)**: If you just want to run a Java program, you only need the JRE. It's the runtime environment that includes the JVM and the necessary class libraries.  
* **JVM (Java Virtual Machine)**: This is the core of the Java platform. It's a virtual machine that executes Java bytecode. When you compile your code, the JVM translates it into machine-specific code, which is what allows a Java program to run on any device with a JVM.

---

## ** Getting Started**

#### **Prerequisites**

* Java 8 or higher  
* Any Java IDE (like Eclipse, IntelliJ IDEA, or VS Code)  
* Command-line access

#### **Installation & Setup**

1. **Clone the project** by running this command in your terminal:  
   Bash  
   git clone \<repository-url\>  
   cd campus-course-records-manager

2. **Compile the project** by running:  
   Bash  
   javac \-d . src/main/java/com/ccrm/\*.java src/main/java/com/ccrm/\*\*/\*.java

3. **Run the application** with:  
   Bash  
   java com.ccrm.CampusCourseRecordsManager

---

## ** Setting up the Project in Eclipse IDE**

1. **Launch Eclipse** and select **File \> Import...**  
2. Choose **Git \> Projects from Git (with smart import)** and click **Next**.  
3. Select **Clone URI** and paste the project's repository URL. Click **Next**.  
4. Choose the main branch and click **Next**.  
5. Select the local directory where you want the project to be stored and click **Next**.  
6. Eclipse will automatically detect that it's a Java project. Just click **Finish**.

With the project now in your workspace, you can run it by right-clicking on CampusCourseRecordsManager.java in the Package Explorer and selecting **Run As \> Java Application**.

---

## ** Project Requirements Mapping**

Here’s a quick reference for where you can find each key concept implemented in the code:

| Technical Concept | Implementation Location | Description |
| :---- | :---- | :---- |
| **Encapsulation** | model/Person.java, model/Student.java, model/Course.java | Private fields with public getters and setters for controlled data access. |
| **Inheritance** | model/Person.java \-\> model/Student.java, model/Instructor.java | The abstract Person class is extended by Student and Instructor to share common properties. |
| **Abstraction** | model/Person.java | The Person class defines abstract methods (getRole()) that its subclasses must implement. |
| **Polymorphism** | model/Person.java | A Person reference can hold either a Student or an Instructor object, allowing for flexible code. |
| **Singleton Pattern** | core/DataStore.java | Ensures only a single instance of DataStore exists to manage all application data centrally. |
| **Builder Pattern** | builders/CourseBuilder.java, builders/TranscriptBuilder.java | Provides a step-by-step approach to construct complex objects. |
| **Custom Exceptions** | exceptions/MaxCreditLimitExceededException.java, etc. | Application-specific exceptions are used for robust error handling. |
| **NIO.2** | utils/FileUtils.java, utils/BackupUtils.java | Demonstrates modern file operations using the Path and Files APIs. |
| **Streams** | core/DataStore.java, services/StudentService.java | Used for declarative data processing, filtering, and mapping. |
| **Date/Time API** | model/Student.java, model/Enrollment.java | Utilizes LocalDate for handling dates without time zones. |
| **Enums** | enums/Semester.java, enums/Grade.java | Provides type-safe constants with associated data and methods. |
| **Recursion** | utils/BackupUtils.java | You can see recursive directory traversal and operations here. |

---

## ** Enabling Assertions**

To enable assertions, you just need to use the \-ea or \--enableassertions flag when you run the Java application from the command line. This is super helpful for debugging and for internal consistency checks.

**Sample Command:**

Bash

java \-ea com.ccrm.CampusCourseRecordsManager  
