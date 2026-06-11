-- ============================================================
-- Student Record Management System - Database Schema
-- ============================================================

CREATE DATABASE IF NOT EXISTS student_records_db;
USE student_records_db;

-- Departments table (normalized)
CREATE TABLE IF NOT EXISTS departments (
    dept_id     INT AUTO_INCREMENT PRIMARY KEY,
    dept_name   VARCHAR(100) NOT NULL UNIQUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Students table
CREATE TABLE IF NOT EXISTS students (
    student_id   INT AUTO_INCREMENT PRIMARY KEY,
    roll_number  VARCHAR(20)  NOT NULL UNIQUE,
    first_name   VARCHAR(50)  NOT NULL,
    last_name    VARCHAR(50)  NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    phone        VARCHAR(15),
    dept_id      INT,
    year_of_study INT          CHECK (year_of_study BETWEEN 1 AND 4),
    cgpa         DECIMAL(3,2) CHECK (cgpa BETWEEN 0.00 AND 10.00),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id) ON DELETE SET NULL
);

-- ── Indexes for fast filtering & sorting ──
CREATE INDEX idx_roll    ON students(roll_number);
CREATE INDEX idx_dept    ON students(dept_id);
CREATE INDEX idx_year    ON students(year_of_study);
CREATE INDEX idx_cgpa    ON students(cgpa);

-- ── Seed data ──
INSERT INTO departments (dept_name) VALUES
    ('Computer Science & Engineering'),
    ('Electrical & Electronics Engineering'),
    ('Mechanical Engineering'),
    ('Civil Engineering'),
    ('Information Technology');

INSERT INTO students (roll_number, first_name, last_name, email, phone, dept_id, year_of_study, cgpa) VALUES
    ('CS2021001', 'Ravi',    'Kumar',   'ravi.kumar@college.edu',   '9876543210', 1, 3, 8.50),
    ('EE2021002', 'Priya',   'Sharma',  'priya.sharma@college.edu', '9876543211', 2, 3, 7.80),
    ('ME2022003', 'Arun',    'Reddy',   'arun.reddy@college.edu',   '9876543212', 3, 2, 7.20),
    ('CS2020004', 'Sneha',   'Patel',   'sneha.patel@college.edu',  '9876543213', 1, 4, 9.10),
    ('IT2023005', 'Kiran',   'Naidu',   'kiran.naidu@college.edu',  '9876543214', 5, 1, 8.00);
