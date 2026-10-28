package com.miva.coursemanagement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CourseManagementGUI extends JFrame {

    private static final Color BG_APP = new Color(245, 245, 247);
    private static final Color BG_CARD = Color.WHITE;
    private static final Color ACCENT_BLUE = new Color(0, 113, 227);
    private static final Color TEXT_PRIMARY = new Color(29, 29, 31);
    private static final Color TEXT_SECONDARY = new Color(110, 110, 115);
    private static final Color BORDER_SUBTLE = new Color(229, 229, 234);
    private static final Color ACCENT_GREEN = new Color(52, 199, 89);

    private final CourseManager manager;
    private DefaultTableModel tableModel;
    private JTable courseTable;

    private JTextField txtCode;
    private JTextField txtTitle;
    private JTextField txtUnits;
    private JTextField txtSearch;

    private JLabel lblTotalCourses;
    private JLabel lblTotalUnits;
    private JLabel lblStatus;

    public CourseManagementGUI(CourseManager manager) {
        this.manager = manager;
        initUI();
        refreshTable();
    }

    private void initUI() {
        setTitle("MIVA Open University — Student Course Management");
        setSize(1000, 700);
        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BG_APP);
        setLayout(new BorderLayout(0, 0));

        add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel mainContainer = new JPanel(new BorderLayout(20, 20));
        mainContainer.setBackground(BG_APP);
        mainContainer.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel leftPanel = createLeftControlPanel();
        JPanel rightPanel = createRightTablePanel();

        mainContainer.add(leftPanel, BorderLayout.WEST);
        mainContainer.add(rightPanel, BorderLayout.CENTER);

        add(mainContainer, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_CARD);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_SUBTLE),
                new EmptyBorder(16, 24, 16, 24)
        ));

        JPanel titleBox = new JPanel(new GridLayout(2, 1, 0, 4));
        titleBox.setOpaque(false);

        JLabel lblTitle = new JLabel("Student Course Management System");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(TEXT_PRIMARY);

        JLabel lblSub = new JLabel("COS 201: Programming I — Mid-Semester Lab Assessment (MIVA Open University)");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(TEXT_SECONDARY);

        titleBox.add(lblTitle);
        titleBox.add(lblSub);

        JPanel actionBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionBox.setOpaque(false);

        JButton btnLoad = createSecondaryButton("Load File", 38);
        btnLoad.addActionListener(e -> handleLoadFile());

        JButton btnSave = createSecondaryButton("Save File", 38);
        btnSave.addActionListener(e -> handleSaveFile());

        JButton btnCompute = createSecondaryButton("Audit Units (Recursion)", 38);
        btnCompute.addActionListener(e -> handleComputeUnitsDialog());

        actionBox.add(btnLoad);
        actionBox.add(btnSave);
        actionBox.add(btnCompute);

        header.add(titleBox, BorderLayout.WEST);
        header.add(actionBox, BorderLayout.EAST);
        return header;
    }

    private JPanel createLeftControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(320, 0));

        JPanel addCard = createCardPanel("Add New Course");
        addCard.setLayout(new GridLayout(0, 1, 0, 8));

        addCard.add(createFieldLabel("Course Code:"));
        txtCode = createStyledTextField();
        addCard.add(txtCode);

        addCard.add(createFieldLabel("Course Title:"));
        txtTitle = createStyledTextField();
        addCard.add(txtTitle);

        addCard.add(createFieldLabel("Credit Units:"));
        txtUnits = createStyledTextField();
        addCard.add(txtUnits);

        JButton btnAdd = createPrimaryButton("Add Course", 44);
        btnAdd.addActionListener(e -> handleAddCourse());
        addCard.add(Box.createVerticalStrut(4));
        addCard.add(btnAdd);

        JPanel searchCard = createCardPanel("Search by Code");
        searchCard.setLayout(new GridLayout(0, 1, 0, 8));

        searchCard.add(createFieldLabel("Enter Course Code:"));
        txtSearch = createStyledTextField();
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleSearchCourse();
                }
            }
        });
        searchCard.add(txtSearch);

        JButton btnSearch = createSecondaryButton("Search (Recursive)", 40);
        btnSearch.addActionListener(e -> handleSearchCourse());
        searchCard.add(btnSearch);

        panel.add(addCard);
        panel.add(Box.createVerticalStrut(16));
        panel.add(searchCard);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createRightTablePanel() {
        JPanel card = createCardPanel("Registered Courses");
        card.setLayout(new BorderLayout(0, 12));

        String[] columns = {"#", "Course Code", "Course Title", "Credit Units"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        courseTable = new JTable(tableModel);
        courseTable.setRowHeight(36);
        courseTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        courseTable.setGridColor(BORDER_SUBTLE);
        courseTable.setShowHorizontalLines(true);
        courseTable.setShowVerticalLines(false);
        courseTable.setSelectionBackground(new Color(230, 240, 255));
        courseTable.setSelectionForeground(TEXT_PRIMARY);

        JTableHeader th = courseTable.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 12));
        th.setForeground(TEXT_SECONDARY);
        th.setBackground(BG_CARD);
        th.setPreferredSize(new Dimension(0, 36));
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_SUBTLE));

        courseTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        courseTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        courseTable.getColumnModel().getColumn(2).setPreferredWidth(320);
        courseTable.getColumnModel().getColumn(3).setPreferredWidth(90);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        courseTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        courseTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        courseTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_SUBTLE));
        scrollPane.getViewport().setBackground(BG_CARD);

        JPanel summaryBar = new JPanel(new BorderLayout());
        summaryBar.setOpaque(false);
        summaryBar.setBorder(new EmptyBorder(8, 4, 4, 4));

        lblTotalCourses = new JLabel("Total Courses: 0");
        lblTotalCourses.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalCourses.setForeground(TEXT_PRIMARY);

        lblTotalUnits = new JLabel("Total Credit Units: 0 units");
        lblTotalUnits.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalUnits.setForeground(ACCENT_BLUE);

        summaryBar.add(lblTotalCourses, BorderLayout.WEST);
        summaryBar.add(lblTotalUnits, BorderLayout.EAST);

        card.add(scrollPane, BorderLayout.CENTER);
        card.add(summaryBar, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_CARD);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_SUBTLE),
                new EmptyBorder(8, 20, 8, 20)
        ));

        lblStatus = new JLabel("Ready.");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(TEXT_SECONDARY);

        JLabel lblHint = new JLabel("Recursive search & unit summation active");
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHint.setForeground(TEXT_SECONDARY);

        bar.add(lblStatus, BorderLayout.WEST);
        bar.add(lblHint, BorderLayout.EAST);
        return bar;
    }

    private void handleAddCourse() {
        String code = txtCode.getText().trim().toUpperCase();
        String title = txtTitle.getText().trim();
        String unitsStr = txtUnits.getText().trim();

        if (code.isEmpty() || title.isEmpty() || unitsStr.isEmpty()) {
            showError("All fields (Code, Title, and Units) are required.");
            return;
        }

        if (manager.isCodeExists(code)) {
            showError("Course code '" + code + "' already exists! Duplicates are not allowed.");
            return;
        }

        int units;
        try {
            units = Integer.parseInt(unitsStr);
            if (units <= 0) {
                showError("Credit units must be a positive number greater than 0.");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Invalid unit format. Please enter a valid whole number.");
            return;
        }

        try {
            Course course = new Course(code, title, units);
            if (manager.addCourse(course)) {
                refreshTable();
                clearAddForm();
                setStatus("Course '" + code + "' added successfully.", ACCENT_GREEN);
            }
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void handleSearchCourse() {
        String query = txtSearch.getText().trim().toUpperCase();
        if (query.isEmpty()) {
            showError("Please enter a course code to search.");
            return;
        }

        Course found = manager.searchByCodeRecursive(query);

        if (found != null) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 1).toString().equalsIgnoreCase(query)) {
                    courseTable.setRowSelectionInterval(i, i);
                    courseTable.scrollRectToVisible(courseTable.getCellRect(i, 0, true));
                    break;
                }
            }
            JOptionPane.showMessageDialog(this,
                    "Course Found (via Recursive Traversal):\n\n" +
                            "• Course Code: " + found.getCourseCode() + "\n" +
                            "• Course Title: " + found.getCourseTitle() + "\n" +
                            "• Credit Units: " + found.getCreditUnits() + "\n",
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE);
            setStatus("Found course: " + found.getCourseCode(), ACCENT_BLUE);
        } else {
            showError("No course found with code: '" + query + "'");
            setStatus("Search missed for: " + query, Color.RED);
        }
    }

    private void handleComputeUnitsDialog() {
        int iterative = manager.computeTotalUnits();
        int recursive = manager.computeTotalUnitsRecursive();
        int count = manager.getCourseCount();

        String message = String.format(
                "Course Audit Summary:\n\n" +
                        "• Total Registered Courses: %d\n" +
                        "• Iterative Loop Sum      : %d units\n" +
                        "• Recursive Function Sum  : %d units\n\n" +
                        "Status: %s",
                count, iterative, recursive,
                (iterative == recursive ? "VERIFIED (Both algorithms match)" : "MISMATCH")
        );

        JOptionPane.showMessageDialog(this, message, "Credit Units Audit", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleSaveFile() {
        JFileChooser fileChooser = new JFileChooser("data");
        fileChooser.setSelectedFile(new File("data/courses.txt"));
        int res = fileChooser.showSaveDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                manager.saveToFile(file.getAbsolutePath());
                setStatus("Saved " + manager.getCourseCount() + " courses to: " + file.getName(), ACCENT_GREEN);
                JOptionPane.showMessageDialog(this, "Courses saved successfully to " + file.getName());
            } catch (IOException ex) {
                showError("Failed to save: " + ex.getMessage());
            }
        }
    }

    private void handleLoadFile() {
        JFileChooser fileChooser = new JFileChooser("data");
        fileChooser.setSelectedFile(new File("data/courses.txt"));
        int res = fileChooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                int added = manager.loadFromFile(file.getAbsolutePath());
                refreshTable();
                setStatus("Loaded " + added + " new course(s) from " + file.getName(), ACCENT_GREEN);
                JOptionPane.showMessageDialog(this,
                        "Successfully loaded " + added + " course(s).\nTotal courses in memory: " + manager.getCourseCount(),
                        "Load Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                showError("Failed to load file: " + ex.getMessage());
            }
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Course> list = manager.getAllCourses();
        int i = 1;
        for (Course c : list) {
            tableModel.addRow(new Object[]{
                    i++,
                    c.getCourseCode(),
                    c.getCourseTitle(),
                    c.getCreditUnits()
            });
        }

        int totalCourses = manager.getCourseCount();
        int totalUnits = manager.computeTotalUnitsRecursive();
        lblTotalCourses.setText("Total Courses: " + totalCourses);
        lblTotalUnits.setText("Total Credit Units: " + totalUnits + " units");
    }

    private void clearAddForm() {
        txtCode.setText("");
        txtTitle.setText("");
        txtUnits.setText("");
        txtCode.requestFocus();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Notice", JOptionPane.WARNING_MESSAGE);
    }

    private void setStatus(String text, Color color) {
        lblStatus.setText(text);
        lblStatus.setForeground(color);
    }

    private JPanel createCardPanel(String title) {
        JPanel card = new JPanel();
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_SUBTLE, 1),
                new EmptyBorder(16, 16, 16, 16)
        ));
        return card;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setPreferredSize(new Dimension(0, 36));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return tf;
    }

    private JButton createPrimaryButton(String text, int height) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(ACCENT_BLUE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, height));
        return btn;
    }

    private JButton createSecondaryButton(String text, int height) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(TEXT_PRIMARY);
        btn.setBackground(new Color(240, 240, 243));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_SUBTLE),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width, height));
        return btn;
    }

    public static void launchGUI() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            CourseManager m = new CourseManager();
            try {
                File f = new File("data/courses.txt");
                if (f.exists()) {
                    m.loadFromFile(f.getAbsolutePath());
                }
            } catch (Exception ignored) {
            }
            new CourseManagementGUI(m).setVisible(true);
        });
    }

    public static void main(String[] args) {
        launchGUI();
    }
}
