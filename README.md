Campus Course & Records Manager (CCRM) Project

The Campus Course & Records Manager (CCRM) is a console-based Java application designed to manage student and course data in higher education institutions. It provides functionality for student records, course management, enrollments, grades, backups, and system reports all built with Java SE and object-oriented design principles.

1. Evolution of Java
•	1995: Java 1.0 — “Write Once, Run Anywhere” philosophy introduced.
•	1997: Java 1.1 — Inner classes, JDBC support.
•	1998: Java 2 — Platforms: J2SE, J2EE, J2ME.
•	2004: Java 5 — Generics, enums, enhanced for-loop.
•	2014: Java 8 — Lambdas, Streams API.
•	2017: Java 9 — Java Platform Module System (JPMS).
•	2018: Java 11 — LTS release, modular JDK.
•	2023: Java 21 — Virtual threads, enhanced pattern matching.

2. Java Platforms: SE vs EE vs ME
Platform	Target Environment	Key Features	Use Case
Java SE	Desktop, servers, embedded	Core APIs, JVM, JRE, libraries	Command-line tools, desktop apps, fundamental server apps (CCRM uses this)
Java EE	Enterprise, multi-tier	Servlets, JSP, JPA, EJB, web services	Large-scale, secure enterprise apps (e-commerce, banking)
Java ME	Mobile/embedded	Lightweight subset of SE	Mobile apps, IoT, set-top boxes

3. Java Architecture: JDK, JRE, JVM
JDK (Java Development Kit): Complete toolkit — compiler, debugger, JRE.
JRE (Java Runtime Environment): Runtime environment with JVM + libraries (for running apps).
JVM (Java Virtual Machine): Executes Java bytecode → machine code (ensures portability).

Flow: Source Code (.java) → JDK Compiler → Bytecode (.class) → JVM → Machine-specific execution.
4. Installing Java on Windows
1.	Download JDK from Oracle Downloads.
2.	Run the installer and follow instructions.
3.	Configure Environment Variables:
   - JAVA_HOME = C:\Program Files\Java\jdk-21
   - Update Path → %JAVA_HOME%\bin
4.	Verify installation with commands:
   java -version
   javac -version
5.	Screenshot: docs/screenshots/Screenshot 2025-09-22 215525.png
5. Setting up the Project in Eclipse IDE
6.	Launch Eclipse → File > New > Java Project.
7.	Enter project name (e.g., CCRM).
8.	Create packages:
   - edu.ccrm.domain
   - edu.ccrm.service
   - edu.ccrm.io
   - edu.ccrm.util
   - edu.ccrm.cli
9.	Add classes under packages.
10.	Right-click Main.java → Run As > Java Application.
11.	Screenshot: docs/screenshots/Screenshot 2025-09-22 215638.png
docs/screenshots/Screenshot 2025-09-22 220224.png
docs/screenshots/Screenshot 2025-09-22 220340.png
docs/screenshots/Screenshot 2025-09-22 220406.png

6. Project Requirements Mapping
Concept	Implementation	Description
Encapsulation	model/Student.java	Private fields + getters/setters
Inheritance	Person → Student, Instructor	Shared attributes
Abstraction	Person (abstract methods)	Must be implemented in subclasses
Polymorphism	Person reference holds Student/Instructor	Flexible object handling
Singleton	core/DataStore.java	One instance for data access
Builder	builders/CourseBuilder.java	Build complex objects
Exceptions	exceptions/*.java	Custom exception handling
NIO.2	utils/FileUtils.java	File import/export
Streams	services/StudentService.java	Filtering, mapping
Enums	enums/Grade.java	Strongly-typed constants

7. Enabling Assertions
Run with assertions enabled:
java -ea edu.ccrm.cli.Main
