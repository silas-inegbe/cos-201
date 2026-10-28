package com.miva.coursemanagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourseManager {
    private final List<Course> courses;

    public CourseManager() {
        this.courses = new ArrayList<>();
    }

    public boolean addCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null.");
        }
        if (isCodeExists(course.getCourseCode())) {
            return false;
        }
        return courses.add(course);
    }

    public boolean isCodeExists(String courseCode) {
        if (courseCode == null) return false;
        String cleanCode = courseCode.trim();
        for (Course c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(cleanCode)) {
                return true;
            }
        }
        return false;
    }

    public List<Course> getAllCourses() {
        return Collections.unmodifiableList(courses);
    }

    public int getCourseCount() {
        return courses.size();
    }

    public Course searchByCode(String courseCode) {
        if (courseCode == null) return null;
        String cleanCode = courseCode.trim();
        for (Course c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(cleanCode)) {
                return c;
            }
        }
        return null;
    }

    public Course searchByCodeRecursive(String courseCode) {
        if (courseCode == null || courses.isEmpty()) {
            return null;
        }
        return searchRecursiveHelper(courseCode.trim(), 0);
    }

    private Course searchRecursiveHelper(String code, int index) {
        if (index >= courses.size()) {
            return null;
        }
        Course current = courses.get(index);
        if (current.getCourseCode().equalsIgnoreCase(code)) {
            return current;
        }
        return searchRecursiveHelper(code, index + 1);
    }

    public int computeTotalUnits() {
        int total = 0;
        for (Course c : courses) {
            total += c.getCreditUnits();
        }
        return total;
    }

    public int computeTotalUnitsRecursive() {
        if (courses.isEmpty()) {
            return 0;
        }
        return computeUnitsRecursiveHelper(0);
    }

    private int computeUnitsRecursiveHelper(int index) {
        if (index >= courses.size()) {
            return 0;
        }
        return courses.get(index).getCreditUnits() + computeUnitsRecursiveHelper(index + 1);
    }

    public void saveToFile(String filePath) throws IOException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Course course : courses) {
                writer.write(course.toFileFormat());
                writer.newLine();
            }
        }
    }

    public int loadFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found at specified path: " + filePath);
        }

        int addedCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                try {
                    Course course = Course.fromFileFormat(line);
                    if (addCourse(course)) {
                        addedCount++;
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Warning [Line " + lineNumber + "]: Skipped invalid record -> " + e.getMessage());
                }
            }
        }
        return addedCount;
    }

    public void clearCourses() {
        courses.clear();
    }
}
