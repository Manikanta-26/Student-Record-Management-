# Student Record Management System

A full-stack CRUD web application built with **Java, JDBC, MySQL, and HTML/CSS** for managing student records. Covers the complete software development lifecycle from UI to database layer.

---

## Features

- **Create** — Add new student records with full input validation
- **Read** — View all students, search by name, filter by department or year
- **Update** — Edit existing student information
- **Delete** — Remove records with confirmation
- **Reports** — Average CGPA by department, student count per year
- **Data Integrity** — Exception handling and input validation throughout

---

## Tech Stack

| Layer     | Technology                         |
|-----------|------------------------------------|
| Language  | Java 17                            |
| Database  | MySQL 8.x                          |
| DB Access | JDBC (mysql-connector-java)        |
| Front-end | HTML5, CSS3, Vanilla JavaScript    |
| Server    | Apache Tomcat 10.x                 |
| Build     | Maven (or manual classpath)        |

---

## Project Structure

```
student-record-management/
├── sql/
│   └── schema.sql                  ← DB schema + seed data
├── src/main/java/com/studentrecords/
│   ├── model/
│   │   └── Student.java            ← Entity / POJO
│   ├── dao/
│   │   └── StudentDAO.java         ← All CRUD + report queries
│   ├── util/
│   │   ├── DBConnection.java       ← JDBC connection manager
│   │   └── Validator.java          ← Input validation
│   └── servlet/
│       └── StudentServlet.java     ← HTTP request handler
└── src/main/webapp/
    ├── index.html                  ← Main UI
    ├── css/style.css               ← Stylesheet
    ├── js/app.js                   ← Frontend logic
    └── WEB-INF/web.xml             ← Servlet config
```

---

## Setup & Run

### 1. Prerequisites
- Java 17+
- MySQL 8.x running locally
- Apache Tomcat 10.x
- MySQL Connector/J JAR ([download](https://dev.mysql.com/downloads/connector/j/))

### 2. Database Setup
```sql
-- In MySQL Workbench or CLI:
mysql -u root -p < sql/schema.sql
```

### 3. Configure DB credentials
Edit `src/main/java/com/studentrecords/util/DBConnection.java`:
```java
private static final String USER     = "root";          // your MySQL user
private static final String PASSWORD = "your_password"; // your MySQL password
```

### 4. Build & Deploy
```bash
# Compile (add mysql-connector-java.jar and servlet-api.jar to classpath)
javac -cp ".;lib/*" src/main/java/com/studentrecords/**/*.java -d out/

# Deploy to Tomcat
cp -r out/ $TOMCAT_HOME/webapps/student-records/
cp -r src/main/webapp/* $TOMCAT_HOME/webapps/student-records/
```

### 5. Run
Start Tomcat and open: `http://localhost:8080/student-records/`

---

## Database Schema

```
departments
├── dept_id    (PK, AUTO_INCREMENT)
└── dept_name  (VARCHAR, UNIQUE)

students
├── student_id   (PK, AUTO_INCREMENT)
├── roll_number  (UNIQUE)
├── first_name, last_name
├── email        (UNIQUE)
├── phone
├── dept_id      (FK → departments)
├── year_of_study (1–4)
├── cgpa          (0.00–10.00)
└── created_at, updated_at
```

---

## Key Design Decisions

- **Normalized schema** — departments extracted into a separate table, linked via FK
- **PreparedStatements throughout** — prevents SQL injection
- **Validator utility** — all input validated before any DB operation
- **Exception handling** — `SQLIntegrityConstraintViolationException` caught for duplicate entries
- **Modular DAO pattern** — database logic separated from servlet/controller logic

---

## Screenshots

> Add screenshots of the running app here after deployment.

---

## Author

**Appasi Manikanta** — [github.com/Manikanta-26](https://github.com/Manikanta-26)
