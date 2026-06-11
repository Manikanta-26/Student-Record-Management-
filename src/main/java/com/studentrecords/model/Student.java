package com.studentrecords.model;

/**
 * Model class representing a Student entity.
 * Maps to the `students` table in the database.
 */
public class Student {

    private int    studentId;
    private String rollNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int    deptId;
    private String deptName;   // joined from departments table
    private int    yearOfStudy;
    private double cgpa;

    // ── Constructors ──────────────────────────────────────────────

    public Student() {}

    public Student(String rollNumber, String firstName, String lastName,
                   String email, String phone, int deptId, int yearOfStudy, double cgpa) {
        this.rollNumber  = rollNumber;
        this.firstName   = firstName;
        this.lastName    = lastName;
        this.email       = email;
        this.phone       = phone;
        this.deptId      = deptId;
        this.yearOfStudy = yearOfStudy;
        this.cgpa        = cgpa;
    }

    // ── Getters & Setters ─────────────────────────────────────────

    public int    getStudentId()    { return studentId; }
    public void   setStudentId(int studentId) { this.studentId = studentId; }

    public String getRollNumber()   { return rollNumber; }
    public void   setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getFirstName()    { return firstName; }
    public void   setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName()     { return lastName; }
    public void   setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName()     { return firstName + " " + lastName; }

    public String getEmail()        { return email; }
    public void   setEmail(String email) { this.email = email; }

    public String getPhone()        { return phone; }
    public void   setPhone(String phone) { this.phone = phone; }

    public int    getDeptId()       { return deptId; }
    public void   setDeptId(int deptId) { this.deptId = deptId; }

    public String getDeptName()     { return deptName; }
    public void   setDeptName(String deptName) { this.deptName = deptName; }

    public int    getYearOfStudy()  { return yearOfStudy; }
    public void   setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public double getCgpa()         { return cgpa; }
    public void   setCgpa(double cgpa) { this.cgpa = cgpa; }

    @Override
    public String toString() {
        return String.format("Student[id=%d, roll=%s, name=%s, dept=%s, year=%d, cgpa=%.2f]",
                studentId, rollNumber, getFullName(), deptName, yearOfStudy, cgpa);
    }
}
