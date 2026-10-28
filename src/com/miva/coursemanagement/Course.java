package com.miva.coursemanagement;

import java.util.Objects;

public class Course {
    private String courseCode;
    private String courseTitle;
    private int creditUnits;

    public Course(String courseCode, String courseTitle, int creditUnits) {
        setCourseCode(courseCode);
        setCourseTitle(courseTitle);
        setCreditUnits(creditUnits);
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be empty.");
        }
        this.courseCode = courseCode.trim().toUpperCase();
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        if (courseTitle == null || courseTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be empty.");
        }
        this.courseTitle = courseTitle.trim();
    }

    public int getCreditUnits() {
        return creditUnits;
    }

    public void setCreditUnits(int creditUnits) {
        if (creditUnits <= 0) {
            throw new IllegalArgumentException("Credit units must be greater than 0.");
        }
        this.creditUnits = creditUnits;
    }

    public String toFileFormat() {
        return courseCode + "|" + courseTitle + "|" + creditUnits;
    }

    public static Course fromFileFormat(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("File line is empty.");
        }
        String[] parts = line.split("\\|");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid format. Expected 'CODE|TITLE|UNITS' but found: " + line);
        }
        String code = parts[0].trim();
        String title = parts[1].trim();
        int units;
        try {
            units = Integer.parseInt(parts[2].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid unit number in record: " + parts[2].trim());
        }
        return new Course(code, title, units);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseCode.equalsIgnoreCase(course.courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode.toUpperCase());
    }

    @Override
    public String toString() {
        return String.format("%-10s %-35s %5d units", courseCode, courseTitle, creditUnits);
    }
}
