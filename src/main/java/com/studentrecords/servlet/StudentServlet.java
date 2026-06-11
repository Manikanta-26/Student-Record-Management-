package com.studentrecords.servlet;

import com.studentrecords.dao.StudentDAO;
import com.studentrecords.model.Student;
import com.studentrecords.util.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Main servlet handling all CRUD operations for student records.
 * Maps to /students URL pattern.
 */
@WebServlet("/students")
public class StudentServlet extends HttpServlet {

    private final StudentDAO dao = new StudentDAO();

    // ── GET: list / view / search ─────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {

                case "view": {
                    int id = Integer.parseInt(req.getParameter("id"));
                    Student student = dao.getStudentById(id);
                    req.setAttribute("student", student);
                    req.getRequestDispatcher("/view.html").forward(req, resp);
                    break;
                }

                case "edit": {
                    int id = Integer.parseInt(req.getParameter("id"));
                    Student student = dao.getStudentById(id);
                    req.setAttribute("student", student);
                    req.getRequestDispatcher("/edit.html").forward(req, resp);
                    break;
                }

                case "delete": {
                    int id = Integer.parseInt(req.getParameter("id"));
                    boolean deleted = dao.deleteStudent(id);
                    req.setAttribute("message", deleted ? "Student deleted successfully." : "Student not found.");
                    resp.sendRedirect("students?action=list");
                    break;
                }

                case "search": {
                    String keyword = req.getParameter("q");
                    List<Student> results = dao.searchByName(keyword != null ? keyword : "");
                    req.setAttribute("students", results);
                    req.setAttribute("search", keyword);
                    req.getRequestDispatcher("/index.html").forward(req, resp);
                    break;
                }

                case "report": {
                    List<String> cgpaReport  = dao.getAvgCgpaByDept();
                    List<String> yearReport  = dao.getCountByYear();
                    req.setAttribute("cgpaReport", cgpaReport);
                    req.setAttribute("yearReport", yearReport);
                    req.getRequestDispatcher("/report.html").forward(req, resp);
                    break;
                }

                default: {
                    List<Student> students = dao.getAllStudents();
                    req.setAttribute("students", students);
                    req.getRequestDispatcher("/index.html").forward(req, resp);
                }
            }
        } catch (SQLException e) {
            req.setAttribute("error", "Database error: " + e.getMessage());
            req.getRequestDispatcher("/error.html").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect("students?action=list");
        }
    }

    // ── POST: add / update ────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        String roll      = req.getParameter("roll_number");
        String firstName = req.getParameter("first_name");
        String lastName  = req.getParameter("last_name");
        String email     = req.getParameter("email");
        String phone     = req.getParameter("phone");
        int    year      = parseIntOrDefault(req.getParameter("year_of_study"), 1);
        double cgpa      = parseDoubleOrDefault(req.getParameter("cgpa"), 0.0);
        int    deptId    = parseIntOrDefault(req.getParameter("dept_id"), 1);

        // ── Validate input before touching the DB ──
        String validationError = Validator.validateStudent(roll, firstName, lastName, email, phone, year, cgpa);
        if (validationError != null) {
            req.setAttribute("error", validationError);
            req.getRequestDispatcher("add.html").forward(req, resp);
            return;
        }

        Student student = new Student(roll, firstName, lastName, email, phone, deptId, year, cgpa);

        try {
            if ("update".equals(action)) {
                int id = parseIntOrDefault(req.getParameter("student_id"), -1);
                student.setStudentId(id);
                boolean updated = dao.updateStudent(student);
                req.setAttribute("message", updated ? "Student updated successfully." : "Update failed.");
            } else {
                boolean added = dao.addStudent(student);
                req.setAttribute("message", added ? "Student added successfully." : "Failed to add student.");
            }
            resp.sendRedirect("students?action=list");

        } catch (SQLException e) {
            req.setAttribute("error", "Database error: " + e.getMessage());
            req.getRequestDispatcher("/error.html").forward(req, resp);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────

    private int parseIntOrDefault(String value, int def) {
        try { return Integer.parseInt(value); } catch (Exception e) { return def; }
    }

    private double parseDoubleOrDefault(String value, double def) {
        try { return Double.parseDouble(value); } catch (Exception e) { return def; }
    }
}
