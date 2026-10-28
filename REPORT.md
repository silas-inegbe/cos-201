# MIVA OPEN UNIVERSITY
## Department of Computer Science
### COS 201: Programming I — Mid-Semester Lab Assessment Report
**Project Title:** Student Course Management (Mini-System) \\n
**Student Name:** SILAS INEGBE \\n
**Matriculation Number:** 2025/B/CSC/0543 \\n
**Date:** 28 October 2026 \\n
---

### 1. What the System Does

For this mid-semester assessment, I designed and implemented a Student Course Management System in Java. The goal of the project is to provide students with a straightforward tool to track and organize the courses they register for in a given semester.

When running the application, students can input each course's code (such as COS201), the course title, and the number of credit units assigned to it. The system stores these courses in memory during the session and provides options to view the entire list in a neat table, search for an individual course by its code, calculate the total credit units registered so far, and save the list to a text file. The next time the student opens the application, they can reload their saved courses from the file without having to re-enter them manually.

To make the program flexible and user-friendly, I implemented both an organized text-based console menu and a graphical user interface (GUI) using Java Swing, allowing the user to interact with the system through whichever interface they prefer.

---

### 2. How Each Major Feature Works

The application is structured into separate classes to keep the data, business logic, and user interface properly separated:

1. **Course (`Course.java`):**
   This class serves as the blueprint for an individual course. It holds three private fields: `courseCode`, `courseTitle`, and `creditUnits`. When a course is created or updated, its setters automatically trim any unnecessary whitespace from the text and convert the course code into uppercase so that searches remain consistent. It also contains validation logic to ensure credit units are positive numbers, and provides helper methods to format course records for file storage (`toFileFormat`) and parse them back (`fromFileFormat`).

2. **CourseManager (`CourseManager.java`):**
   This class acts as the central controller for the data. It maintains an internal `ArrayList<Course>` and provides all the core operations:
   - **Adding Courses:** Before adding a course, it checks whether another course with the same code already exists (case-insensitively). If it finds a match, it prevents the duplicate from being added.
   - **Viewing Courses:** It returns an unmodifiable view of the course list so the interface can display them safely without accidentally modifying the collection directly.
   - **Searching Courses:** It looks up a course matching a given code using both an iterative loop and a recursive traversal method.
   - **Computing Total Units:** It computes the sum of credit units across all registered courses using standard iteration, and also verifies the calculation using a recursive helper method.
   - **Saving and Loading:** It writes records to a text file (`data/courses.txt`) where each course is saved as `CODE|TITLE|UNITS`. When loading, it reads the file line by line, splits each line at the delimiter, validates the tokens, and loads only valid and non-duplicate entries.

3. **User Interface Layer (`Main.java` and `CourseManagementGUI.java`):**
   `Main.java` runs the console menu in an active `while` loop, asking the user to choose an option from 1 to 8 using a `switch-case` statement. The GUI version (`CourseManagementGUI.java`) presents the same operations through desktop cards, form text boxes, and a table view, updating totals in real time whenever a course is added, loaded, or removed.

---

### 3. Challenges Encountered and Error Handling

During development and testing, I ran into several common Java pitfalls and edge cases that required deliberate handling:

- **The `Scanner` Input Skipping Issue:**
  Early on in testing the console menu, I noticed that calling `scanner.nextInt()` followed by `scanner.nextLine()` caused the program to immediately skip the course title prompt. This happens because `nextInt()` reads only the number and leaves the trailing newline character in the input buffer, which the subsequent `nextLine()` immediately consumes.
  *How I handled it:* Instead of mixing scanner methods, I read all inputs as complete strings using `scanner.nextLine().trim()` and parsed integers explicitly with `Integer.parseInt()`. I wrapped these in helper methods (`readIntegerInput` and `readPositiveInteger`) with `try-catch` blocks so that if a user enters letters or symbols where a number is expected, the program displays a clear error message and prompts them again instead of crashing with an `InputMismatchException`.

- **Invalid and Duplicate Course Inputs:**
  Users could easily enter negative units, zero units, or accidentally type in a course they had already added. I handled this by having the `Course` constructor throw an `IllegalArgumentException` whenever `creditUnits <= 0` or if fields are left blank. In the manager class, I wrote an `isCodeExists()` check that inspects the existing list before accepting a new course, notifying the user if a duplicate code is detected.

- **File Reading and Corrupted Data:**
  When loading from an external file, the file might not exist yet, or someone might have manually edited the text file and introduced a corrupted line (such as text where a unit number should be, or missing pipe delimiters).
  *How I handled it:* I wrapped the file-loading logic inside a `try-catch` block for `IOException`. Inside the reading loop, each line is parsed individually inside its own inner `try-catch` block. If an individual line is corrupted, the program logs a brief warning indicating which line number was skipped and continues reading the rest of the file instead of terminating abruptly.

---

### 4. Where Recursion Was Applied

The project guidelines require demonstrating the use of recursion in the program. Rather than forcing recursion where it does not make sense, I applied it in two areas of the `CourseManager` class:

1. **Recursive Course Search (`searchByCodeRecursive`):**
   To search for a course without relying only on an iterative `for` loop, I created a recursive search method that inspects elements sequentially:
   - **Base Case 1:** If the current `index` reaches or exceeds the size of the list (`index >= courses.size()`), it means the entire list has been checked without finding a match, so the method returns `null`.
   - **Base Case 2:** If the course at the current index has a course code matching the search query (`equalsIgnoreCase`), the method returns that `Course` object immediately.
   - **Recursive Step:** If the course at the current index does not match, the method calls itself with `index + 1`, moving forward to inspect the next item.

2. **Recursive Credit Unit Calculation (`computeTotalUnitsRecursive`):**
   I also implemented recursion to compute the total semester credit units:
   - **Base Case:** When `index >= courses.size()`, the method returns `0`.
   - **Recursive Step:** Otherwise, it takes the credit units of the current course (`courses.get(index).getCreditUnits()`) and adds it to the result of calling the method on the next index (`index + 1`).

In the console application (Option 4) and in the GUI audit dialog, both the iterative loop total and the recursive function total are displayed side by side to verify that both algorithms arrive at the exact same total.

---

### 5. Summary

Building this project helped reinforce the core principles of Object-Oriented Programming in Java, specifically encapsulation, separation of concerns, and robust exception handling. By accounting for edge cases such as invalid scanner inputs and corrupted files, and by verifying data through both iterative and recursive methods, the system operates reliably and meets all the requirements laid out in the assessment brief.
