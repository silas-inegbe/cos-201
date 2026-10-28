# COS201 Programming I: Mid-Semester Lab Assessment Project
## Student Course Management (Mini-System)

**Institution:** MIVA Open University
**Course Code:** COS 201 — Programming I
**Implementation Language:** Java (JDK 17+)
**Project Type:** Object-Oriented Console Application

---

## 1. Project Overview

The **Student Course Management System** is a modular Java desktop application designed to assist students in tracking and managing their semester courses. The system provides intuitive tools to register courses, inspect recorded entries, search specific courses by course code, calculate cumulative credit units, and persist data across sessions using flat-file storage.

The design strictly implements Object-Oriented Programming (OOP) paradigms, incorporates recursive problem-solving, validates user inputs resiliently, and handles standard file I/O operations through robust exception handling.

---

## 2. Project Requirements & Specifications

### 2.1 Core Functionality
- **Add Course:** Register new courses with course code, title, and credit units.
- **Manageable Internal Structure:** Store active course records in memory using Java Collections (`ArrayList<Course>`).
- **Display Courses:** Format and display all stored courses in a clean tabular view.
- **Search Course by Code:** Locate a specific course using its unique alphanumeric course code (case-insensitive).
- **Compute Total Credit Units:** Calculate the cumulative credit units for all registered courses.
- **Save to File:** Export all in-memory courses to a persistent text file (`data/courses.txt`).
- **Load from File:** Import previously saved course records into memory, skipping corrupt entries and avoiding duplicates.

### 2.2 Program Design & OOP Principles
- **Encapsulation:** The `Course` class encapsulates course attributes with private fields, validation within constructors, and clean getters/setters.
- **Separation of Concerns:**
  - `Course`: Data model and record serialization.
  - `CourseManager`: Business logic, list operations, file I/O, iterative & recursive algorithms.
  - `Main`: Presentation layer, user input validation, and interactive menu dispatching.
- **String Processing:** Automatic trimming, case-folding (converting course codes to uppercase), validation against blank entries.
- **Control Flow:** Structured `switch-case` decision structures embedded within an application loop.

### 2.3 Additional Technical Features
- **Iteration:** Deterministic loops (`for`, `for-each`, `while`) for list rendering, file parsing, and input loops.
- **Recursion:** Dual recursive algorithms:
  1. *Recursive Search:* `searchByCodeRecursive(String code)` recursively traverses the course list to locate a course by code.
  2. *Recursive Unit Summation:* `computeTotalUnitsRecursive()` recursively computes cumulative credit units.
- **Exception Handling:** Explicit `try-catch` blocks protecting against `NumberFormatException`, `InputMismatchException`, `IllegalArgumentException`, `FileNotFoundException`, and `IOException`.
- **User Interface:** Clean, console interface designed with typography hierarchy and clear separation lines.

---

## 3. Required Operations / Menu Architecture

As specified in the assessment guidelines, the system implements the 7 core operations:

| Option | Operation | Description |
| :---: | :--- | :--- |
| **1** | **Add Course** | Prompts for Code, Title, and Units; validates inputs and prevents duplicates. |
| **2** | **View All Courses** | Outputs a structured, formatted table showing all registered courses and totals. |
| **3** | **Search Course by Code** | Case-insensitive search powered by recursive traversal. |
| **4** | **Compute Total Units** | Displays cumulative credit units, verifying loop sum against recursive sum. |
| **5** | **Save to File** | Serializes current course list to disk (`data/courses.txt` or custom path). |
| **6** | **Load from File** | Reads persisted courses, validates each line, and appends unique records. |
| **7** | **Exit Program** | Prompts for confirmation and terminates cleanly. |

---

## 4. Environment & Prerequisites

### 4.1 System Requirements
- **Operating System:** Windows 10/11, macOS, or Linux.
- **Java Development Kit (JDK):** JDK 17 or higher (tested with Temurin-17 OpenJDK).
- **Terminal:** PowerShell, Windows Command Prompt, or Bash.
- **IDE / Editor (Optional):** Visual Studio Code, IntelliJ IDEA, Eclipse, or NetBeans.

### 4.2 Verified Environment
- Compiler: `javac 17.0.17`
- Runtime: `openjdk 17.0.17`

---

## 5. Project Directory Structure

```text
cos-201/
├── .vscode/
│   ├── launch.json              # VS Code debug launcher (GUI & Console)
│   ├── settings.json            # VS Code Java project paths
│   └── tasks.json               # VS Code build & run tasks (Ctrl+Shift+B)
├── bin/                         # Compiled bytecode class files
│   └── com/miva/coursemanagement/
│       ├── Course.class
│       ├── CourseManager.class
│       ├── CourseManagementGUI.class
│       └── Main.class
├── data/
│   └── courses.txt              # Persistent flat-file storage (sample included)
├── src/
│   └── com/miva/coursemanagement/
│       ├── Course.java          # Model class with validation & file serialization
│       ├── CourseManager.java   # Business logic, recursive search/count & file I/O
│       ├── CourseManagementGUI.java # Graphical User Interface (Apple HIG principles)
│       └── Main.java            # Console UI & application launcher
├── build.bat                    # One-click Windows batch compiler
├── run.bat                      # One-click Windows console launcher
├── run-gui.bat                  # One-click Windows GUI launcher
├── run.ps1                      # PowerShell console launcher
├── run-gui.ps1                  # PowerShell GUI launcher
├── PROJECT.md                   # Complete project specification and guide (this file)
└── REPORT.md                    # Ready-to-submit 1–2 page assessment report
```

---

## 6. How to Build and Run

### Launching the Graphical User Interface (GUI)
You can launch the GUI using any of these options:
- **PowerShell:**
  ```powershell
  .\run-gui.ps1
  ```
- **Batch / Command Prompt:**
  ```cmd
  .\run-gui.bat
  ```
- **Java CLI:**
  ```bash
  java -cp bin com.miva.coursemanagement.CourseManagementGUI
  ```
- **From the Console Menu:** Select option `8` in the console menu.

### Launching the Console Interface
- **PowerShell:**
  ```powershell
  .\run.ps1
  ```
- **Batch / Command Prompt:**
  ```cmd
  .\run.bat
  ```
- **Java CLI:**
  ```bash
  java -cp bin com.miva.coursemanagement.Main
  ```

### Method 4: Visual Studio Code
1. Open the `cos-201` folder in VS Code.
2. Press `Ctrl + Shift + B` to execute the build task.
3. Open `src/com/miva/coursemanagement/Main.java` and click **Run** / press `F5`.

---

## 7. Technical Implementation Details

### 7.1 Class Structure

#### `Course.java`
- **Fields:**
  - `private String courseCode`: Unique identifier (e.g., `"COS201"`).
  - `private String courseTitle`: Course description (e.g., `"Programming I"`).
  - `private int creditUnits`: Integer weighting (e.g., `3`).
- **Encapsulation:** Setters sanitize input by calling `.trim()` and `.toUpperCase()`. Rejects null or empty strings and credit units $\le 0$.
- **File Serialization:**
  - `toFileFormat()` produces pipe-separated string: `CODE|TITLE|UNITS`.
  - `fromFileFormat(String line)` safely parses lines and converts types.

#### `CourseManager.java`
- **Internal Storage:** `List<Course> courses = new ArrayList<>()`.
- **Duplicate Prevention:** `isCodeExists(String code)` verifies codes case-insensitively before insertion.
- **Recursive Search Algorithm:**
  ```java
  private Course searchRecursiveHelper(String code, int index) {
      if (index >= courses.size()) return null; // Base Case 1: not found
      Course current = courses.get(index);
      if (current.getCourseCode().equalsIgnoreCase(code)) return current; // Base Case 2: match
      return searchRecursiveHelper(code, index + 1); // Recursive Step
  }
  ```
- **Recursive Unit Summation Algorithm:**
  ```java
  private int computeUnitsRecursiveHelper(int index) {
      if (index >= courses.size()) return 0; // Base Case: end reached
      return courses.get(index).getCreditUnits() + computeUnitsRecursiveHelper(index + 1); // Recursive Step
  }
  ```
- **File Persistence:**
  - `saveToFile(String filePath)`: Utilizes `BufferedWriter` with try-with-resources.
  - `loadFromFile(String filePath)`: Utilizes `BufferedReader`, parses records line by line, ignores comment headers (`#`) and empty lines, and skips malformed records with warning logs without crashing.

#### `Main.java`
- Houses the presentation logic and terminal formatting.
- Features helper input validators:
  - `readIntegerInput(String prompt, int min, int max)`: Prevents letters or out-of-range inputs from crashing the menu loop.
  - `readPositiveInteger(String prompt)`: Ensures credit units are valid integers $> 0$.
  - `readNonEmptyString(String prompt)`: Rejects empty or whitespace-only inputs.
  - `promptDefault(String prompt, String defaultValue)`: Allows pressing `[Enter]` to accept default file path `data/courses.txt`.

---

## 8. Persistence Format Specification

Stored in plain text using pipe-delimited records (`data/courses.txt`):
```text
# MIVA Open University - Sample Course Dataset
# Format: COURSE_CODE|COURSE_TITLE|CREDIT_UNITS
COS201|Programming I|3
COS203|Discrete Mathematics|3
MTH201|Linear Algebra|3
GST201|Nigerian Peoples and Culture|2
PHY201|General Physics III|3
GST203|Philosophy, Logic and Human Existence|2
```

---

## 9. Submission Deliverables Checklist

Per the MIVA assessment brief (Pages 4–5 of the assessment PDF):

- [x] **1. All Source Code Files:**
  - [Course.java](file:///c:/Users/PC/Desktop/Client/cos-201/src/com/miva/coursemanagement/Course.java)
  - [CourseManager.java](file:///c:/Users/PC/Desktop/Client/cos-201/src/com/miva/coursemanagement/CourseManager.java)
  - [CourseManagementGUI.java](file:///c:/Users/PC/Desktop/Client/cos-201/src/com/miva/coursemanagement/CourseManagementGUI.java)
  - [Main.java](file:///c:/Users/PC/Desktop/Client/cos-201/src/com/miva/coursemanagement/Main.java)
- [x] **2. Working Environment & Automation Scripts:**
  - [build.bat](file:///c:/Users/PC/Desktop/Client/cos-201/build.bat)
  - [run.bat](file:///c:/Users/PC/Desktop/Client/cos-201/run.bat)
  - [run-gui.bat](file:///c:/Users/PC/Desktop/Client/cos-201/run-gui.bat)
  - [run.ps1](file:///c:/Users/PC/Desktop/Client/cos-201/run.ps1)
  - [run-gui.ps1](file:///c:/Users/PC/Desktop/Client/cos-201/run-gui.ps1)
  - [.vscode/tasks.json](file:///c:/Users/PC/Desktop/Client/cos-201/.vscode/tasks.json)
- [x] **3. Data Storage:**
  - [data/courses.txt](file:///c:/Users/PC/Desktop/Client/cos-201/data/courses.txt)
- [x] **4. Short Assessment Report (1–2 Pages):**
  - [REPORT.md](file:///c:/Users/PC/Desktop/Client/cos-201/REPORT.md)
- [ ] **5. Program Execution Screenshot:**
  - Run `run.bat`, execute options 6 (load), 2 (view), 3 (search), 4 (units), and capture a screenshot for your submission.

