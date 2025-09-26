# Campus Course & Records Manager (CCRM)

## How to Run
- JDK 17+
- Build: Maven (or run from IDE)
- Main class: `edu.ccrm.Main`

## Evolution of Java (high-level)
- 1995: Java 1.0
- 2004: Generics (Java 5)
- 2014: Lambdas/Streams (Java 8)
- 2017+: 6-month releases (9+)

## Java ME vs SE vs EE
- ME: Embedded/mobile profile
- SE: Desktop/CLI libraries (this project)
- EE: Enterprise stack (Servlets, JPA, etc.)

## JDK, JRE, JVM
- JVM: Runtime engine
- JRE: JVM + standard libs
- JDK: JRE + compiler/tools

## Windows Install Steps
1. Download JDK 17+
2. Set JAVA_HOME and PATH
3. Verify: `java -version`

## Eclipse Setup
1. New Java Project → Import sources
2. Set main class `edu.ccrm.Main`
3. Run

## Assertions
- Enable with `-ea`. The app uses an assertion in `Main`.

## Mapping (spec → code)
- Singleton: `edu.ccrm.config.AppConfig`
- Builder: `edu.ccrm.domain.Course.Builder`
- OOP (Encap/Inheritance/Abstraction/Polymorphism): `Person`, `Student`, `Instructor`, services
- Enums: `edu.ccrm.domain.enums.Grade`, `Semester`
- Streams/NIO.2/DateTime: `io.*`, `util.RecursionUtils`, `BackupService`
- CLI & control flow: `edu.ccrm.cli.Cli`
- Arrays demo: `Cli.manageCourses` option g
- Labeled break: `Cli.manageStudents` (label in loop)
- Interface default conflict: `util.NamedCompare`, `util.SecondaryCompare`, `util.Comparators`

## Screenshots
- Place screenshots under `screenshots/` (JDK verify, IDE run, program output, exports/backups)



