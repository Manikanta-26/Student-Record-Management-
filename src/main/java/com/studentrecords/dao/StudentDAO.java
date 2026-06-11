package com.studentrecords.dao;

import com.studentrecords.model.Student;
import com.studentrecords.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Student entity.
 * Handles all CRUD operations and reporting queries against the MySQL database.
 */
public class StudentDAO {

    // ── CREATE ────────────────────────────────────────────────────

    /**
     * Inserts a new student record into the database.
     * @return true if insertion was successful
     */
    public boolean addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (roll_number, first_name, last_name, email, phone, dept_id, year_of_study, cgpa) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getRollNumber());
            ps.setString(2, student.getFirstName());
            ps.setString(3, student.getLastName());
            ps.setString(4, student.getEmail());
            ps.setString(5, student.getPhone());
            ps.setInt   (6, student.getDeptId());
            ps.setInt   (7, student.getYearOfStudy());
            ps.setDouble(8, student.getCgpa());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Duplicate roll number or email: " + e.getMessage(), e);
        }
    }

    // ── READ (single) ─────────────────────────────────────────────

    /**
     * Fetches a single student by their primary key ID.
     */
    public Student getStudentById(int studentId) throws SQLException {
        String sql = "SELECT s.*, d.dept_name FROM students s "
                   + "LEFT JOIN departments d ON s.dept_id = d.dept_id "
                   + "WHERE s.student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return mapRow(rs);
            return null;
        }
    }

    /**
     * Fetches a student by roll number.
     */
    public Student getStudentByRoll(String rollNumber) throws SQLException {
        String sql = "SELECT s.*, d.dept_name FROM students s "
                   + "LEFT JOIN departments d ON s.dept_id = d.dept_id "
                   + "WHERE s.roll_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, rollNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return mapRow(rs);
            return null;
        }
    }

    // ── READ (all / filtered / sorted) ───────────────────────────

    /**
     * Returns all student records joined with department name.
     */
    public List<Student> getAllStudents() throws SQLException {
        String sql = "SELECT s.*, d.dept_name FROM students s "
                   + "LEFT JOIN departments d ON s.dept_id = d.dept_id "
                   + "ORDER BY s.roll_number ASC";
        return fetchList(sql);
    }

    /**
     * Filters students by department.
     */
    public List<Student> getStudentsByDept(int deptId) throws SQLException {
        String sql = "SELECT s.*, d.dept_name FROM students s "
                   + "LEFT JOIN departments d ON s.dept_id = d.dept_id "
                   + "WHERE s.dept_id = ? ORDER BY s.roll_number ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deptId);
            return mapResultSet(ps.executeQuery());
        }
    }

    /**
     * Filters students by year of study.
     */
    public List<Student> getStudentsByYear(int year) throws SQLException {
        String sql = "SELECT s.*, d.dept_name FROM students s "
                   + "LEFT JOIN departments d ON s.dept_id = d.dept_id "
                   + "WHERE s.year_of_study = ? ORDER BY s.cgpa DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            return mapResultSet(ps.executeQuery());
        }
    }

    /**
     * Searches students by partial name (case-insensitive).
     */
    public List<Student> searchByName(String keyword) throws SQLException {
        String sql = "SELECT s.*, d.dept_name FROM students s "
                   + "LEFT JOIN departments d ON s.dept_id = d.dept_id "
                   + "WHERE LOWER(CONCAT(s.first_name,' ',s.last_name)) LIKE ? "
                   + "ORDER BY s.first_name ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            return mapResultSet(ps.executeQuery());
        }
    }

    /**
     * Returns top N students sorted by CGPA descending.
     */
    public List<Student> getTopStudents(int limit) throws SQLException {
        String sql = "SELECT s.*, d.dept_name FROM students s "
                   + "LEFT JOIN departments d ON s.dept_id = d.dept_id "
                   + "ORDER BY s.cgpa DESC LIMIT ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            return mapResultSet(ps.executeQuery());
        }
    }

    // ── REPORTING QUERIES ─────────────────────────────────────────

    /**
     * Returns average CGPA grouped by department — used in reports.
     */
    public List<String> getAvgCgpaByDept() throws SQLException {
        String sql = "SELECT d.dept_name, ROUND(AVG(s.cgpa), 2) AS avg_cgpa, COUNT(s.student_id) AS total_students "
                   + "FROM students s JOIN departments d ON s.dept_id = d.dept_id "
                   + "GROUP BY d.dept_name ORDER BY avg_cgpa DESC";

        List<String> report = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            report.add(String.format("%-45s %10s %10s", "Department", "Avg CGPA", "Students"));
            report.add("-".repeat(67));
            while (rs.next()) {
                report.add(String.format("%-45s %10.2f %10d",
                        rs.getString("dept_name"),
                        rs.getDouble("avg_cgpa"),
                        rs.getInt("total_students")));
            }
        }
        return report;
    }

    /**
     * Returns student count per year of study.
     */
    public List<String> getCountByYear() throws SQLException {
        String sql = "SELECT year_of_study, COUNT(*) AS total FROM students GROUP BY year_of_study ORDER BY year_of_study";

        List<String> report = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            report.add(String.format("%-15s %10s", "Year", "Count"));
            report.add("-".repeat(27));
            while (rs.next()) {
                report.add(String.format("%-15d %10d",
                        rs.getInt("year_of_study"),
                        rs.getInt("total")));
            }
        }
        return report;
    }

    // ── UPDATE ────────────────────────────────────────────────────

    /**
     * Updates an existing student record.
     * @return true if at least one row was updated
     */
    public boolean updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET first_name=?, last_name=?, email=?, phone=?, "
                   + "dept_id=?, year_of_study=?, cgpa=? WHERE student_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getPhone());
            ps.setInt   (5, student.getDeptId());
            ps.setInt   (6, student.getYearOfStudy());
            ps.setDouble(7, student.getCgpa());
            ps.setInt   (8, student.getStudentId());

            return ps.executeUpdate() > 0;
        }
    }

    // ── DELETE ────────────────────────────────────────────────────

    /**
     * Deletes a student by their ID.
     * @return true if the record was deleted
     */
    public boolean deleteStudent(int studentId) throws SQLException {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            return ps.executeUpdate() > 0;
        }
    }

    // ── HELPERS ───────────────────────────────────────────────────

    private List<Student> fetchList(String sql) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return mapResultSet(rs);
        }
    }

    private List<Student> mapResultSet(ResultSet rs) throws SQLException {
        List<Student> list = new ArrayList<>();
        while (rs.next()) list.add(mapRow(rs));
        return list;
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setStudentId  (rs.getInt   ("student_id"));
        s.setRollNumber (rs.getString("roll_number"));
        s.setFirstName  (rs.getString("first_name"));
        s.setLastName   (rs.getString("last_name"));
        s.setEmail      (rs.getString("email"));
        s.setPhone      (rs.getString("phone"));
        s.setDeptId     (rs.getInt   ("dept_id"));
        s.setDeptName   (rs.getString("dept_name"));
        s.setYearOfStudy(rs.getInt   ("year_of_study"));
        s.setCgpa       (rs.getDouble("cgpa"));
        return s;
    }
}
