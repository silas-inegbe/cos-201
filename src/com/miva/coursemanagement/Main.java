package com.miva.coursemanagement;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String DEFAULT_FILE_PATH = "data/courses.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static final CourseManager manager = new CourseManager();

    public static void main(String[] args) {
        if (args.length > 0 && (args[0].equalsIgnoreCase("--gui") || args[0].equalsIgnoreCase("-g"))) {
            CourseManagementGUI.launchGUI();
            return;
        }

        printBanner();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readIntegerInput("Select an option (1-8): ", 1, 8);
            System.out.println();

            switch (choice) {
                case 1:
                    handleAddCourse();
                    break;
                case 2:
                    handleViewAllCourses();
                    break;
                case 3:
                    handleSearchCourse();
                    break;
                case 4:
                    handleComputeTotalUnits();
                    break;
                case 5:
                    handleSaveToFile();
                    break;
                case 6:
                    handleLoadFromFile();
                    break;
                case 7:
                    running = handleExit();
                    break;
                case 8:
                    System.out.println("Launching Graphical User Interface (GUI)...");
                    CourseManagementGUI.launchGUI();
                    break;
                default:
                    System.out.println("Invalid selection. Please choose an option from 1 to 8.");
            }
            System.out.println();
        }

        scanner.close();
    }

    private static void printBanner() {
        System.out.println("==================================================================");
        System.out.println("                     MIVA OPEN UNIVERSITY                         ");
        System.out.println("       COS 201: PROGRAMMING I - MID-SEMESTER ASSESSMENT           ");
        System.out.println("             Student Course Management System                     ");
        System.out.println("==================================================================");
    }

    private static void printMenu() {
        System.out.println("------------------------- MAIN MENU ------------------------------");
        System.out.println("  1. Add Course");
        System.out.println("  2. View All Courses");
        System.out.println("  3. Search Course by Code");
        System.out.println("  4. Compute Total Units");
        System.out.println("  5. Save to File");
        System.out.println("  6. Load from File");
        System.out.println("  7. Exit Program");
        System.out.println("  8. Launch Graphical User Interface (GUI)");
        System.out.println("------------------------------------------------------------------");
    }

    private static void handleAddCourse() {
        System.out.println(">>> 1. ADD NEW COURSE");

        String code = readNonEmptyString("Enter Course Code (e.g. COS201): ").toUpperCase();

        if (manager.isCodeExists(code)) {
            System.out.println("[Error] A course with code '" + code + "' already exists!");
            return;
        }

        String title = readNonEmptyString("Enter Course Title (e.g. Programming I): ");
        int units = readPositiveInteger("Enter Credit Units (e.g. 1 - 6): ");

        try {
            Course course = new Course(code, title, units);
            if (manager.addCourse(course)) {
                System.out.println("[Success] Course '" + course.getCourseCode() + " - " + course.getCourseTitle()
                        + " (" + course.getCreditUnits() + " Units)' recorded successfully.");
            } else {
                System.out.println("[Error] Could not add course. Duplicate code detected.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[Error] Validation failed: " + e.getMessage());
        }
    }

    private static void handleViewAllCourses() {
        System.out.println(">>> 2. RECORDED COURSES");
        List<Course> courses = manager.getAllCourses();

        if (courses.isEmpty()) {
            System.out.println("No courses registered yet. Please add a course or load from a file.");
            return;
        }

        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-5s %-12s %-38s %-6s%n", "#", "COURSE CODE", "COURSE TITLE", "UNITS");
        System.out.println("------------------------------------------------------------------");

        int index = 1;
        for (Course course : courses) {
            System.out.printf("%-5d %-12s %-38s %-6d%n",
                    index++,
                    course.getCourseCode(),
                    truncate(course.getCourseTitle(), 36),
                    course.getCreditUnits());
        }

        System.out.println("------------------------------------------------------------------");
        System.out.printf("Total Courses: %d | Total Credit Units: %d%n",
                manager.getCourseCount(), manager.computeTotalUnits());
        System.out.println("------------------------------------------------------------------");
    }

    private static void handleSearchCourse() {
        System.out.println(">>> 3. SEARCH COURSE BY CODE");

        if (manager.getCourseCount() == 0) {
            System.out.println("No courses currently stored to search.");
            return;
        }

        String searchCode = readNonEmptyString("Enter Course Code to search: ").toUpperCase();
        Course found = manager.searchByCodeRecursive(searchCode);

        if (found != null) {
            System.out.println("[Found] Course Details:");
            System.out.println("  • Code:         " + found.getCourseCode());
            System.out.println("  • Title:        " + found.getCourseTitle());
            System.out.println("  • Credit Units: " + found.getCreditUnits());
        } else {
            System.out.println("[Not Found] No course found with code: " + searchCode);
        }
    }

    private static void handleComputeTotalUnits() {
        System.out.println(">>> 4. COMPUTE TOTAL CREDIT UNITS");

        int count = manager.getCourseCount();
        if (count == 0) {
            System.out.println("No courses registered yet. Total units: 0.");
            return;
        }

        int totalIterative = manager.computeTotalUnits();
        int totalRecursive = manager.computeTotalUnitsRecursive();

        System.out.println("--------------------------------------------------");
        System.out.println("Total Registered Courses : " + count);
        System.out.println("Computed Units (Loop)    : " + totalIterative + " units");
        System.out.println("Computed Units (Recursion): " + totalRecursive + " units");
        System.out.println("--------------------------------------------------");

        if (totalIterative == totalRecursive) {
            System.out.println("Verification Status      : PASSED (Iterative == Recursive)");
        }
    }

    private static void handleSaveToFile() {
        System.out.println(">>> 5. SAVE COURSES TO FILE");

        if (manager.getCourseCount() == 0) {
            System.out.println("Warning: There are no courses in memory to save.");
            String proceed = promptDefault("Do you still want to write an empty file? (y/N): ", "n");
            if (!proceed.equalsIgnoreCase("y")) {
                System.out.println("Save operation cancelled.");
                return;
            }
        }

        String path = promptDefault("Enter target file path (Press [Enter] for '" + DEFAULT_FILE_PATH + "'): ",
                DEFAULT_FILE_PATH);

        try {
            manager.saveToFile(path);
            System.out.println("[Success] Successfully saved " + manager.getCourseCount()
                    + " course(s) to '" + path + "'.");
        } catch (IOException e) {
            System.out.println("[Error] Failed to save file: " + e.getMessage());
        }
    }

    private static void handleLoadFromFile() {
        System.out.println(">>> 6. LOAD COURSES FROM FILE");

        String path = promptDefault("Enter source file path (Press [Enter] for '" + DEFAULT_FILE_PATH + "'): ",
                DEFAULT_FILE_PATH);

        try {
            int loaded = manager.loadFromFile(path);
            System.out.println("[Success] Loaded " + loaded + " new course(s) from '" + path + "'.");
            System.out.println("Current total courses in memory: " + manager.getCourseCount());
        } catch (IOException e) {
            System.out.println("[Error] Failed to load courses: " + e.getMessage());
        }
    }

    private static boolean handleExit() {
        System.out.println(">>> 7. EXIT PROGRAM");
        String confirm = promptDefault("Are you sure you want to exit? (Y/n): ", "y");
        if (confirm.equalsIgnoreCase("y")) {
            System.out.println("Thank you for using the MIVA Course Management System. Goodbye!");
            return false;
        }
        return true;
    }

    private static int readIntegerInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("[Input Error] Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("[Input Error] Invalid number format. Please enter digits only.");
            }
        }
    }

    private static int readPositiveInteger(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
                System.out.println("[Input Error] Value must be greater than zero.");
            } catch (NumberFormatException e) {
                System.out.println("[Input Error] Invalid number format. Please enter a valid positive number.");
            }
        }
    }

    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("[Input Error] Input cannot be blank. Please try again.");
        }
    }

    private static String promptDefault(String prompt, String defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return defaultValue;
        }
        return input;
    }

    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}
