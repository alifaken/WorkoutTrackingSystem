import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * BIT1053 - Computer Programming
 * Workout Planning and Tracking System
 *
 * WorkoutGUI.java — Main application window
 * Coded by: [Your Name] (Leader)
 *
 * Integrates:
 *   - User.java          (Member 2)
 *   - Exercise.java      (Member 3)
 *   - WorkoutPlan.java   (Member 4)
 *   - WorkoutLog.java    (Member 5)
 *   - CalorieCalculator  (Member 6)
 *   - InputValidator     (Member 7)
 */
public class WorkoutGUI extends JFrame {

    // ─── Shared Data Objects (plug in when members finish their classes) ───
    private User currentUser;                   // Member 2
    private WorkoutPlan workoutPlan;            // Member 4
    private WorkoutLog workoutLog;              // Member 5

    // ─── Color Palette ─────────────────────────────────────────────────────
    private static final Color PRIMARY    = new Color(34, 139, 87);   // green
    private static final Color DANGER     = new Color(192, 57, 43);   // red
    private static final Color ACCENT     = new Color(52, 120, 190);  // blue
    private static final Color BG         = new Color(245, 247, 250); // light gray
    private static final Color TEXT_DARK  = new Color(30, 30, 30);

    // ─── Table Models (so we can update them later) ─────────────────────────
    private DefaultTableModel planTableModel;
    private DefaultTableModel historyTableModel;
    private JTextArea tipsArea;

    // ─── Status Bar ─────────────────────────────────────────────────────────
    private JLabel statusBar;

    public WorkoutGUI() {
        initializeFrame();
        buildUI();
    }

    // ════════════════════════════════════════════════════════════════════════
    // FRAME SETUP
    // ════════════════════════════════════════════════════════════════════════

    private void initializeFrame() {
        setTitle("FitStart");
        setSize(900, 650);
        setMinimumSize(new Dimension(800, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center on screen
        getContentPane().setBackground(BG);
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Header
        add(buildHeader(), BorderLayout.NORTH);

        // Tabbed panels (main content)
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 13));
        tabs.setBackground(BG);

        tabs.addTab("  👤 Profile  ",  buildProfileTab());
        tabs.addTab("  📋 My Plan  ",  buildWorkoutPlanTab());
        tabs.addTab("  ✅ Track    ",  buildTrackSessionTab());
        tabs.addTab("  📊 History  ",  buildHistoryTab());
        tabs.addTab("  💡 Tips     ",  buildTipsTab());

        // Refresh tips whenever the user switches to that tab, so it reflects
        // whatever profile is currently saved (or the fallback prompt if none yet)
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 4) {
                tipsArea.setText(getTipsText());
            }
        });

        add(tabs, BorderLayout.CENTER);

        // Status bar at bottom
        statusBar = new JLabel("  Welcome! Please set up your profile first.");
        statusBar.setFont(new Font("Arial", Font.ITALIC, 12));
        statusBar.setForeground(Color.GRAY);
        statusBar.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        add(statusBar, BorderLayout.SOUTH);
    }

    // ════════════════════════════════════════════════════════════════════════
    // HEADER
    // ════════════════════════════════════════════════════════════════════════

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel appTitle = new JLabel("🏋️  FitStart");
        appTitle.setFont(new Font("Arial", Font.BOLD, 18));
        appTitle.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Your Intelligent Workout Companion");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 11));
        subtitle.setForeground(new Color(200, 240, 210));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(appTitle);
        textPanel.add(subtitle);

        header.add(textPanel, BorderLayout.WEST);
        return header;
    }

    // ════════════════════════════════════════════════════════════════════════
    // TAB 1 — USER PROFILE
    // Integrates: User.java (Member 2) + InputValidator.java (Member 7)
    // ════════════════════════════════════════════════════════════════════════

    private JPanel buildProfileTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        // Section title
        JLabel title = sectionTitle("User Profile");
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        panel.add(title, g);

        // Fields
        g.gridwidth = 1;
        JTextField nameField   = addFormRow(panel, g, "Full Name:",       1, "e.g. Ahmad Ali");
        JTextField ageField    = addFormRow(panel, g, "Age:",             2, "e.g. 20");
        JTextField weightField = addFormRow(panel, g, "Weight (kg):",     3, "e.g. 65");
        JTextField heightField = addFormRow(panel, g, "Height (cm):",     4, "e.g. 170");

        // Gender dropdown — required by User's constructor (also drives BMR calculation)
        g.gridy = 5; g.gridx = 0;
        panel.add(styledLabel("Gender:"), g);
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female"});
        genderCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        g.gridx = 1;
        panel.add(genderCombo, g);

        // Experience level dropdown — required by User's constructor (also feeds Risk Detector)
        g.gridy = 6; g.gridx = 0;
        panel.add(styledLabel("Experience Level:"), g);
        JComboBox<String> levelCombo = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advanced"});
        levelCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        g.gridx = 1;
        panel.add(levelCombo, g);

        // Fitness goal dropdown — wording matches User.java's fitnessGoal values exactly
        g.gridy = 7; g.gridx = 0;
        panel.add(styledLabel("Fitness Goal:"), g);
        String[] goals = {"Weight Loss", "Muscle Gain", "General Fitness"};
        JComboBox<String> goalCombo = new JComboBox<>(goals);
        goalCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        g.gridx = 1;
        panel.add(goalCombo, g);

        // Feedback label
        JLabel feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        g.gridy = 8; g.gridx = 0; g.gridwidth = 2;
        g.anchor = GridBagConstraints.CENTER;
        panel.add(feedbackLabel, g);

        // Save button
        JButton saveBtn = styledButton("Save Profile", PRIMARY);
        g.gridy = 9;
        panel.add(saveBtn, g);

        // ── Action: Save Profile ────────────────────────────────────────────
        saveBtn.addActionListener(e -> {
            String name   = nameField.getText().trim();
            String ageStr = ageField.getText().trim();
            String wtStr  = weightField.getText().trim();
            String htStr  = heightField.getText().trim();
            String gender = (String) genderCombo.getSelectedItem();
            String level  = (String) levelCombo.getSelectedItem();
            String goal   = (String) goalCombo.getSelectedItem();

            // TODO: Replace with InputValidator.validateName(name) etc (Member 7)
            if (name.isEmpty() || ageStr.isEmpty() || wtStr.isEmpty() || htStr.isEmpty()) {
                feedbackLabel.setText("⚠  All fields are required.");
                feedbackLabel.setForeground(DANGER);
                return;
            }

            try {
                int    age    = Integer.parseInt(ageStr);
                double weight = Double.parseDouble(wtStr);
                double height = Double.parseDouble(htStr);

                if (age <= 0 || age > 120) throw new IllegalArgumentException("Age must be 1–120.");
                if (weight <= 0)           throw new IllegalArgumentException("Weight must be positive.");
                if (height <= 0)           throw new IllegalArgumentException("Height must be positive.");

                currentUser = new User(name, age, weight, height, gender, goal, level);

                String bmiText = String.format("%.1f", currentUser.getBMI());
                feedbackLabel.setText("✅  Saved! BMI: " + bmiText + " (" + currentUser.getBMICategory() + ")");
                feedbackLabel.setForeground(PRIMARY);
                updateStatus("Profile saved — " + name + " | Goal: " + goal);

                // Beginner Risk Detector — educational only, not medical advice
                String riskMessage = currentUser.getRiskMessage();
                if (riskMessage != null) {
                    JOptionPane.showMessageDialog(this, riskMessage,
                            "A Note on Your Profile", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                feedbackLabel.setText("⚠  Age, weight and height must be numbers.");
                feedbackLabel.setForeground(DANGER);
            } catch (IllegalArgumentException ex) {
                feedbackLabel.setText("⚠  " + ex.getMessage());
                feedbackLabel.setForeground(DANGER);
            }
        });

        return panel;
    }

    // ════════════════════════════════════════════════════════════════════════
    // TAB 2 — WORKOUT PLAN
    // Integrates: WorkoutPlan.java (Member 4) + Exercise.java (Member 3)
    // ════════════════════════════════════════════════════════════════════════

    private JPanel buildWorkoutPlanTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(sectionTitle("My Workout Plan"), BorderLayout.NORTH);

        // Table
        String[] cols = {"Exercise Name", "Muscle Group", "Sets", "Reps", "Duration (min)"};
        planTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(planTableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Input row at bottom
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        inputPanel.setBackground(BG);

        JTextField exNameField    = placeholderField("Exercise name", 12);
        JComboBox<String> muscleCombo = new JComboBox<>(new String[]{
                "Chest", "Back", "Shoulders", "Biceps", "Triceps",
                "Legs", "Core", "Cardio", "Full Body"
        });
        JTextField setsField      = placeholderField("Sets", 4);
        JTextField repsField      = placeholderField("Reps", 4);
        JTextField durationField  = placeholderField("Min", 4);

        JButton addBtn    = styledButton("+ Add", ACCENT);
        JButton removeBtn = styledButton("Remove", DANGER);
        JLabel  errLabel  = new JLabel(" ");
        errLabel.setForeground(DANGER);
        errLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        inputPanel.add(new JLabel("Exercise:")); inputPanel.add(exNameField);
        inputPanel.add(new JLabel("Muscle:"));   inputPanel.add(muscleCombo);
        inputPanel.add(new JLabel("Sets:"));     inputPanel.add(setsField);
        inputPanel.add(new JLabel("Reps:"));     inputPanel.add(repsField);
        inputPanel.add(new JLabel("Duration:")); inputPanel.add(durationField);
        inputPanel.add(addBtn);
        inputPanel.add(removeBtn);
        inputPanel.add(errLabel);

        panel.add(inputPanel, BorderLayout.SOUTH);

        // ── Action: Add Exercise ────────────────────────────────────────────
        addBtn.addActionListener(e -> {
            String exName  = exNameField.getText().trim();
            String muscle  = (String) muscleCombo.getSelectedItem();
            String setsStr = setsField.getText().trim();
            String repsStr = repsField.getText().trim();
            String durStr  = durationField.getText().trim();

            // TODO: Replace with InputValidator methods (Member 7)
            if (exName.isEmpty() || setsStr.isEmpty() || repsStr.isEmpty() || durStr.isEmpty()) {
                errLabel.setText("⚠ All fields required.");
                return;
            }

            try {
                int sets     = Integer.parseInt(setsStr);
                int reps     = Integer.parseInt(repsStr);
                int duration = Integer.parseInt(durStr);

                if (sets <= 0)     throw new IllegalArgumentException("Sets must be ≥ 1");
                if (reps <= 0)     throw new IllegalArgumentException("Reps must be ≥ 1");
                if (duration <= 0) throw new IllegalArgumentException("Duration must be ≥ 1 min");

                // TODO: Replace with Exercise + WorkoutPlan objects (Members 3 & 4)
                // Exercise ex = new Exercise(exName, muscle, sets, reps, duration);
                // workoutPlan.addExercise(ex);
                planTableModel.addRow(new Object[]{exName, muscle, sets, reps, duration});

                // Clear inputs
                exNameField.setText(""); setsField.setText("");
                repsField.setText(""); durationField.setText("");
                errLabel.setText(" ");
                updateStatus("Exercise added: " + exName);

            } catch (NumberFormatException ex) {
                errLabel.setText("⚠ Sets, Reps, Duration must be numbers.");
            } catch (IllegalArgumentException ex) {
                errLabel.setText("⚠ " + ex.getMessage());
            }
        });

        // ── Action: Remove Selected ─────────────────────────────────────────
        removeBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String name = (String) planTableModel.getValueAt(selectedRow, 0);
                // TODO: workoutPlan.removeExercise(selectedRow);
                planTableModel.removeRow(selectedRow);
                updateStatus("Removed: " + name);
            } else {
                errLabel.setText("⚠ Select a row to remove.");
            }
        });

        return panel;
    }

    // ════════════════════════════════════════════════════════════════════════
    // TAB 3 — TRACK SESSION
    // Integrates: WorkoutLog.java (Member 5) + CalorieCalculator.java (Member 6)
    // ════════════════════════════════════════════════════════════════════════

    private JPanel buildTrackSessionTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(sectionTitle("Track Today's Session"), BorderLayout.NORTH);

        // Checklist area — each exercise from the plan gets a checkbox
        JPanel checklistPanel = new JPanel();
        checklistPanel.setLayout(new BoxLayout(checklistPanel, BoxLayout.Y_AXIS));
        checklistPanel.setBackground(Color.WHITE);
        checklistPanel.setBorder(BorderFactory.createTitledBorder("Select exercises you completed:"));

        // Sample placeholders — will be replaced by WorkoutPlan data
        String[] sampleExercises = {"Bench Press", "Pull Ups", "Squats"};
        ArrayList<JCheckBox> checkboxes = new ArrayList<>();
        for (String ex : sampleExercises) {
            JCheckBox cb = new JCheckBox("  " + ex);
            cb.setFont(new Font("Arial", Font.PLAIN, 13));
            cb.setBackground(Color.WHITE);
            checkboxes.add(cb);
            checklistPanel.add(cb);
        }
        // TODO: Dynamically build from workoutPlan.getExercises() (Member 4)

        panel.add(new JScrollPane(checklistPanel), BorderLayout.CENTER);

        // Bottom row
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(BG);

        JLabel calorieLabel = new JLabel("Estimated Calories Burned: -- kcal");
        calorieLabel.setFont(new Font("Arial", Font.BOLD, 14));
        calorieLabel.setForeground(TEXT_DARK);

        JButton calcBtn = styledButton("Calculate Calories", ACCENT);
        JButton logBtn  = styledButton("Log This Session ✅", PRIMARY);

        bottomPanel.add(calorieLabel);
        bottomPanel.add(calcBtn);
        bottomPanel.add(logBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // ── Action: Calculate Calories ──────────────────────────────────────
        calcBtn.addActionListener(e -> {
            // TODO: Replace with CalorieCalculator.calculate(exercises) (Member 6)
            int dummyCalories = 0;
            for (JCheckBox cb : checkboxes) {
                if (cb.isSelected()) dummyCalories += 50; // placeholder
            }
            calorieLabel.setText("Estimated Calories Burned: " + dummyCalories + " kcal");
        });

        // ── Action: Log Session ─────────────────────────────────────────────
        logBtn.addActionListener(e -> {
            long count = checkboxes.stream().filter(JCheckBox::isSelected).count();
            if (count == 0) {
                JOptionPane.showMessageDialog(this, "Please check at least one completed exercise.", "Nothing logged", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // TODO: workoutLog.logSession(completedExercises, calories) (Member 5)
            String today = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
            historyTableModel.addRow(new Object[]{today, count + " exercises", "--", "--"});
            JOptionPane.showMessageDialog(this, "Session logged for " + today + "! Great work! 💪", "Session Saved", JOptionPane.INFORMATION_MESSAGE);
            checkboxes.forEach(cb -> cb.setSelected(false));
            updateStatus("Session logged on " + today);
        });

        return panel;
    }

    // ════════════════════════════════════════════════════════════════════════
    // TAB 4 — HISTORY
    // Integrates: WorkoutLog.java (Member 5)
    // ════════════════════════════════════════════════════════════════════════

    private JPanel buildHistoryTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(sectionTitle("Workout History"), BorderLayout.NORTH);

        String[] cols = {"Date", "Exercises Completed", "Duration (min)", "Calories Burned"};
        historyTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(historyTableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        // TODO: Load history from workoutLog.getHistory() on tab open (Member 5)

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(BG);
        JButton clearBtn = styledButton("Clear History", DANGER);
        clearBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Clear all workout history?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                historyTableModel.setRowCount(0);
                // TODO: workoutLog.clearHistory() (Member 5)
                updateStatus("History cleared.");
            }
        });
        bottomPanel.add(clearBtn);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    // ════════════════════════════════════════════════════════════════════════
    // TAB 5 — TIPS & GOALS
    // Integrates: User.java fitness goal (Member 2)
    // ════════════════════════════════════════════════════════════════════════

    private JPanel buildTipsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(sectionTitle("Tips & Goals"), BorderLayout.NORTH);

        tipsArea = new JTextArea();
        tipsArea.setEditable(false);
        tipsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        tipsArea.setLineWrap(true);
        tipsArea.setWrapStyleWord(true);
        tipsArea.setBackground(Color.WHITE);
        tipsArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        tipsArea.setText(getTipsText());

        panel.add(new JScrollPane(tipsArea), BorderLayout.CENTER);
        return panel;
    }

    /** Builds the Tips text, adding goal-specific advice once a profile exists. */
    private String getTipsText() {
        StringBuilder sb = new StringBuilder();
        sb.append("🏋️  General Health & Fitness Tips\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
        sb.append("💧 Hydration\n   Drink at least 8 glasses of water daily.\n   Increase intake on training days.\n\n");
        sb.append("🌡  Warm Up & Cool Down\n   Always warm up 5–10 minutes before exercise.\n   Cool down and stretch after every session.\n\n");
        sb.append("💤 Rest & Recovery\n   Rest at least 1 day between training the same muscle group.\n   Aim for 7–8 hours of sleep per night.\n\n");
        sb.append("🥗 Nutrition\n   Track your meals alongside your workouts.\n   Prioritise protein for muscle recovery.\n\n");
        sb.append("📈 Consistency\n   Consistency beats intensity. Show up every day.\n   Track your progress to stay motivated.\n\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        if (currentUser != null) {
            String goal = currentUser.getFitnessGoal();
            sb.append("🎯 Tips for your goal: ").append(goal).append("\n\n");
            if (goal.equalsIgnoreCase("Weight Loss")) {
                sb.append("   Combine cardio with resistance training to preserve muscle while losing fat.\n");
                sb.append("   Aim for a moderate calorie deficit rather than an extreme one.\n");
            } else if (goal.equalsIgnoreCase("Muscle Gain")) {
                sb.append("   Prioritise progressive overload — gradually increase weight or reps over time.\n");
                sb.append("   Make sure protein intake and recovery support muscle growth.\n");
            } else {
                sb.append("   Mix strength, cardio, and flexibility work for balanced fitness.\n");
                sb.append("   Focus on consistency over intensity.\n");
            }
        } else {
            sb.append("📌  Save your profile to see personalized goal tips.");
        }

        return sb.toString();
    }

    // ════════════════════════════════════════════════════════════════════════
    // HELPER METHODS — UI Components
    // ════════════════════════════════════════════════════════════════════════

    /** Returns a styled section title label */
    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(TEXT_DARK);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        return label;
    }

    /** Returns a standard form label */
    private JLabel styledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        return label;
    }

    /** Adds a label + text field row to a GridBagLayout panel, returns the field */
    private JTextField addFormRow(JPanel panel, GridBagConstraints g, String labelText, int row, String hint) {
        g.gridy = row; g.gridx = 0;
        panel.add(styledLabel(labelText), g);
        JTextField field = placeholderField(hint, 20);
        g.gridx = 1;
        panel.add(field, g);
        return field;
    }

    /** Creates a styled text field with hint text */
    private JTextField placeholderField(String hint, int cols) {
        JTextField field = new JTextField(cols);
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setToolTipText(hint);
        return field;
    }

    /** Creates a styled button */
    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        return btn;
    }

    /** Updates the bottom status bar */
    private void updateStatus(String message) {
        statusBar.setText("  " + message);
    }
}