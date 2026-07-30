import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class WorkoutGUI extends JFrame {

    private User currentUser;                   // Member 2
    private WorkoutPlan workoutPlan;            // Member 4
    private WorkoutLog workoutLog;              // Member 5

    private static final Color PRIMARY    = new Color(34, 139, 87);
    private static final Color DANGER     = new Color(192, 57, 43);
    private static final Color ACCENT     = new Color(52, 120, 190);
    private static final Color BG         = new Color(245, 247, 250);
    private static final Color TEXT_DARK  = new Color(30, 30, 30);
    private static final Color TEXT_MUTED = new Color(110, 118, 129);
    private static final Color BORDER     = new Color(220, 224, 230);

    // Segoe UI renders far better than Arial on Windows and falls back safely elsewhere
    private static final String FONT_FAMILY = "Segoe UI";

    private DefaultTableModel planTableModel;
    private DefaultTableModel historyTableModel;
    private JTextArea tipsArea;
    private JPanel checklistPanel;
    private ArrayList<JCheckBox> sessionCheckboxes = new ArrayList<>();
    private ArrayList<Exercise> checklistExercises = new ArrayList<>();
    private int plannedDaysPerWeek = 0; // set when the user requests a recommendation; feeds Consistency Score
    private JLabel consistencyLabel;

    private JLabel statusBar;

    public WorkoutGUI() {
        initializeFrame();
        workoutPlan = new WorkoutPlan();
        workoutLog = new WorkoutLog();
        buildUI();
    }

    private void initializeFrame() {
        setTitle("FitStart");
        setSize(900, 650);
        setMinimumSize(new Dimension(800, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
        tabs.setBackground(BG);

        tabs.addTab("   Profile   ",  buildProfileTab());
        tabs.addTab("   My Plan   ",  buildWorkoutPlanTab());
        tabs.addTab("   Track   ",  buildTrackSessionTab());
        tabs.addTab("   History   ",  buildHistoryTab());
        tabs.addTab("   Tips   ",  buildTipsTab());

        tabs.addChangeListener(e -> {
            int selected = tabs.getSelectedIndex();
            if (selected == 2) {
                refreshChecklist();
            } else if (selected == 3) {
                refreshHistoryTable();
            } else if (selected == 4) {
                tipsArea.setText(getTipsText());
            }
        });

        add(tabs, BorderLayout.CENTER);

        statusBar = new JLabel("  Welcome! Please set up your profile first.");
        statusBar.setFont(new Font(FONT_FAMILY, Font.PLAIN, 12));
        statusBar.setForeground(TEXT_MUTED);
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                BorderFactory.createEmptyBorder(7, 12, 7, 12)));
        add(statusBar, BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(18, 26, 18, 26));

        JLabel appTitle = new JLabel("FitStart");
        appTitle.setFont(new Font(FONT_FAMILY, Font.BOLD, 24));
        appTitle.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Your Intelligent Workout Companion");
        subtitle.setFont(new Font(FONT_FAMILY, Font.PLAIN, 12));
        subtitle.setForeground(new Color(200, 240, 210));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 3));
        textPanel.setOpaque(false);
        textPanel.add(appTitle);
        textPanel.add(subtitle);

        header.add(textPanel, BorderLayout.WEST);
        return header;
    }

    private JPanel buildProfileTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = sectionTitle("User Profile");
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        panel.add(title, g);

        g.gridwidth = 1;
        JTextField nameField   = addFormRow(panel, g, "Full Name:",       1, "e.g. Ahmad Ali");
        JTextField ageField    = addFormRow(panel, g, "Age:",             2, "e.g. 20");
        JTextField weightField = addFormRow(panel, g, "Weight (kg):",     3, "e.g. 65");
        JTextField heightField = addFormRow(panel, g, "Height (cm):",     4, "e.g. 170");

        g.gridy = 5; g.gridx = 0;
        panel.add(styledLabel("Gender:"), g);
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female"});
        genderCombo.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        g.gridx = 1;
        panel.add(genderCombo, g);

        g.gridy = 6; g.gridx = 0;
        panel.add(styledLabel("Experience Level:"), g);
        JComboBox<String> levelCombo = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advanced"});
        levelCombo.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        g.gridx = 1;
        panel.add(levelCombo, g);

        g.gridy = 7; g.gridx = 0;
        panel.add(styledLabel("Fitness Goal:"), g);
        String[] goals = {"Weight Loss", "Muscle Gain", "General Fitness"};
        JComboBox<String> goalCombo = new JComboBox<>(goals);
        goalCombo.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        g.gridx = 1;
        panel.add(goalCombo, g);

        JLabel feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font(FONT_FAMILY, Font.ITALIC, 12));
        g.gridy = 8; g.gridx = 0; g.gridwidth = 2;
        g.anchor = GridBagConstraints.CENTER;
        panel.add(feedbackLabel, g);

        JButton saveBtn = styledButton("Save Profile", PRIMARY);
        g.gridy = 9;
        panel.add(saveBtn, g);

        saveBtn.addActionListener(e -> {
            String name   = nameField.getText().trim();
            String ageStr = ageField.getText().trim();
            String wtStr  = weightField.getText().trim();
            String htStr  = heightField.getText().trim();
            String gender = (String) genderCombo.getSelectedItem();
            String level  = (String) levelCombo.getSelectedItem();
            String goal   = (String) goalCombo.getSelectedItem();

            if (!InputValidator.isValidName(name) || ageStr.isEmpty() || wtStr.isEmpty() || htStr.isEmpty()) {
                feedbackLabel.setText("All fields are required.");
                feedbackLabel.setForeground(DANGER);
                return;
            }

            try {
                int    age    = Integer.parseInt(ageStr);
                double weight = Double.parseDouble(wtStr);
                double height = Double.parseDouble(htStr);

                if (!InputValidator.isValidAge(age))       throw new IllegalArgumentException("Age must be 1–120.");
                if (!InputValidator.isValidWeight(weight)) throw new IllegalArgumentException("Weight must be positive.");
                if (!InputValidator.isValidHeight(height)) throw new IllegalArgumentException("Height must be positive.");

                currentUser = new User(name, age, weight, height, gender, goal, level);

                String bmiText = String.format("%.1f", currentUser.getBMI());
                feedbackLabel.setText("Saved. BMI: " + bmiText + " (" + currentUser.getBMICategory() + ")");
                feedbackLabel.setForeground(PRIMARY);
                updateStatus("Profile saved — " + name + " | Goal: " + goal);

                String riskMessage = currentUser.getRiskMessage();
                if (riskMessage != null) {
                    JOptionPane.showMessageDialog(this, riskMessage,
                            "A Note on Your Profile", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                feedbackLabel.setText("Age, weight and height must be numbers.");
                feedbackLabel.setForeground(DANGER);
            } catch (IllegalArgumentException ex) {
                feedbackLabel.setText(ex.getMessage());
                feedbackLabel.setForeground(DANGER);
            }
        });

        return panel;
    }

    private JPanel buildWorkoutPlanTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(sectionTitle("My Workout Plan"), BorderLayout.NORTH);

        String[] cols = {"Exercise Name", "Muscle Group", "Sets", "Reps", "Duration (min)"};
        planTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(planTableModel);
        styleTable(table);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

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
        JButton recommendBtn = styledButton("Get Recommendation", PRIMARY);
        JLabel  errLabel  = new JLabel(" ");
        errLabel.setForeground(DANGER);
        errLabel.setFont(new Font(FONT_FAMILY, Font.ITALIC, 12));

        inputPanel.add(new JLabel("Exercise:")); inputPanel.add(exNameField);
        inputPanel.add(new JLabel("Muscle:"));   inputPanel.add(muscleCombo);
        inputPanel.add(new JLabel("Sets:"));     inputPanel.add(setsField);
        inputPanel.add(new JLabel("Reps:"));     inputPanel.add(repsField);
        inputPanel.add(new JLabel("Duration:")); inputPanel.add(durationField);
        inputPanel.add(addBtn);
        inputPanel.add(removeBtn);
        inputPanel.add(recommendBtn);
        inputPanel.add(errLabel);

        panel.add(inputPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            String exName  = exNameField.getText().trim();
            String muscle  = (String) muscleCombo.getSelectedItem();
            String setsStr = setsField.getText().trim();
            String repsStr = repsField.getText().trim();
            String durStr  = durationField.getText().trim();

            if (!InputValidator.isValidName(exName) || setsStr.isEmpty() || repsStr.isEmpty() || durStr.isEmpty()) {
                errLabel.setText("All fields required.");
                return;
            }

            try {
                int sets     = Integer.parseInt(setsStr);
                int reps     = Integer.parseInt(repsStr);
                int duration = Integer.parseInt(durStr);

                if (!InputValidator.isValidSets(sets))         throw new IllegalArgumentException("Sets must be at least 1");
                if (!InputValidator.isValidReps(reps))         throw new IllegalArgumentException("Reps must be at least 1");
                if (!InputValidator.isValidDuration(duration)) throw new IllegalArgumentException("Duration must be at least 1 min");

                Exercise ex = new Exercise(exName, muscle, sets, reps, duration);
                workoutPlan.addExercise(ex);
                planTableModel.addRow(new Object[]{exName, muscle, sets, reps, duration});

                exNameField.setText(""); setsField.setText("");
                repsField.setText(""); durationField.setText("");
                errLabel.setText(" ");
                updateStatus("Exercise added: " + exName);

            } catch (NumberFormatException ex) {
                errLabel.setText("Sets, Reps, Duration must be numbers.");
            } catch (IllegalArgumentException ex) {
                errLabel.setText(ex.getMessage());
            }
        });

        removeBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String name = (String) planTableModel.getValueAt(selectedRow, 0);
                workoutPlan.removeExercise(selectedRow);
                planTableModel.removeRow(selectedRow);
                updateStatus("Removed: " + name);
            } else {
                errLabel.setText("Select a row to remove.");
            }
        });

        recommendBtn.addActionListener(e -> {
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "Please save your profile first — the recommendation uses your goal and experience level.",
                        "Profile Needed", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String daysStr = JOptionPane.showInputDialog(this, "How many days a week can you train? (1-7)");
            if (daysStr == null) return; // cancelled

            try {
                int days = Integer.parseInt(daysStr.trim());
                if (!InputValidator.isValidDaysAvailable(days)) {
                    JOptionPane.showMessageDialog(this, "Days available must be between 1 and 7.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ArrayList<String> recommendation = workoutPlan.generateRecommendation(
                        currentUser.getFitnessGoal(), currentUser.getLevel(), days);
                plannedDaysPerWeek = days;

                StringBuilder sb = new StringBuilder("Recommended Weekly Split (" + currentUser.getFitnessGoal() + "):\n\n");
                for (String line : recommendation) {
                    sb.append(line).append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString(), "Your Recommendation", JOptionPane.INFORMATION_MESSAGE);
                updateStatus("Weekly recommendation generated for " + days + " day(s).");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel buildTrackSessionTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(sectionTitle("Track Today's Session"), BorderLayout.NORTH);

        checklistPanel = new JPanel();
        checklistPanel.setLayout(new BoxLayout(checklistPanel, BoxLayout.Y_AXIS));
        checklistPanel.setBackground(Color.WHITE);
        checklistPanel.setBorder(BorderFactory.createTitledBorder("Select exercises you completed:"));
        refreshChecklist();

        panel.add(new JScrollPane(checklistPanel), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(BG);

        JLabel calorieLabel = new JLabel("Estimated Calories Burned: -- kcal");
        calorieLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 14));
        calorieLabel.setForeground(TEXT_DARK);

        JButton calcBtn = styledButton("Calculate Calories", ACCENT);
        JButton logBtn  = styledButton("Log This Session", PRIMARY);

        bottomPanel.add(calorieLabel);
        bottomPanel.add(calcBtn);
        bottomPanel.add(logBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        calcBtn.addActionListener(e -> {
            int totalCalories = 0;
            for (int i = 0; i < sessionCheckboxes.size(); i++) {
                if (sessionCheckboxes.get(i).isSelected()) {
                    Exercise ex = checklistExercises.get(i);
                    totalCalories += CalorieCalculator.calculateCalories(ex.getMuscleGroup(), ex.getDuration());
                }
            }
            calorieLabel.setText("Estimated Calories Burned: " + totalCalories + " kcal");
        });

        logBtn.addActionListener(e -> {
            ArrayList<String> completedMuscleGroups = new ArrayList<>();
            int totalDuration = 0;
            int count = 0;
            int calories = 0;

            for (int i = 0; i < sessionCheckboxes.size(); i++) {
                if (sessionCheckboxes.get(i).isSelected()) {
                    Exercise ex = checklistExercises.get(i);
                    count++;
                    totalDuration += ex.getDuration();
                    calories += CalorieCalculator.calculateCalories(ex.getMuscleGroup(), ex.getDuration());
                    if (!completedMuscleGroups.contains(ex.getMuscleGroup())) {
                        completedMuscleGroups.add(ex.getMuscleGroup());
                    }
                }
            }

            if (count == 0) {
                JOptionPane.showMessageDialog(this, "Please check at least one completed exercise.", "Nothing logged", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Recovery Intelligence — check before this session gets added to history
            String recoveryWarning = workoutLog.getRecoveryWarning(completedMuscleGroups);
            if (recoveryWarning != null) {
                JOptionPane.showMessageDialog(this, recoveryWarning, "Recovery Check", JOptionPane.WARNING_MESSAGE);
            }

            String today = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
            workoutLog.logSession(today, count, totalDuration, calories, completedMuscleGroups);
            refreshHistoryTable();
            JOptionPane.showMessageDialog(this, "Session logged for " + today + ". Great work!", "Session Saved", JOptionPane.INFORMATION_MESSAGE);
            sessionCheckboxes.forEach(cb -> cb.setSelected(false));
            updateStatus("Session logged on " + today);
        });

        return panel;
    }

    /** Rebuilds the Track Session checklist from whatever is currently in workoutPlan. */
    private void refreshChecklist() {
        checklistPanel.removeAll();
        sessionCheckboxes.clear();
        checklistExercises.clear();

        if (workoutPlan.getExercises().isEmpty()) {
            JLabel emptyLabel = new JLabel("  No exercises in your plan yet — add some in the My Plan tab.");
            emptyLabel.setFont(new Font(FONT_FAMILY, Font.ITALIC, 13));
            checklistPanel.add(emptyLabel);
        } else {
            for (Exercise ex : workoutPlan.getExercises()) {
                JCheckBox cb = new JCheckBox("  " + ex.toString());
                cb.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
                cb.setBackground(Color.WHITE);
                sessionCheckboxes.add(cb);
                checklistExercises.add(ex);
                checklistPanel.add(cb);
            }
        }

        checklistPanel.revalidate();
        checklistPanel.repaint();
    }

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
        styleTable(table);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BG);

        consistencyLabel = new JLabel(" ");
        consistencyLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        consistencyLabel.setForeground(TEXT_DARK);
        bottomPanel.add(consistencyLabel, BorderLayout.WEST);

        JPanel clearBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        clearBtnPanel.setBackground(BG);
        JButton clearBtn = styledButton("Clear History", DANGER);
        clearBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Clear all workout history?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                workoutLog.clearHistory();
                refreshHistoryTable();
                updateStatus("History cleared.");
            }
        });
        clearBtnPanel.add(clearBtn);
        bottomPanel.add(clearBtnPanel, BorderLayout.EAST);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        refreshHistoryTable();
        return panel;
    }

    /** Rebuilds the History table from whatever is currently in workoutLog.
     *  This is the ONLY place that writes to historyTableModel — the GUI
     *  never touches the table directly anywhere else. */
    private void refreshHistoryTable() {
        historyTableModel.setRowCount(0);
        for (Object[] row : workoutLog.getHistoryRows()) {
            historyTableModel.addRow(row);
        }
        refreshConsistencyDisplay();
    }

    /** Shows Consistency Score + Smart Motivation, or a prompt if no target is set yet. */
    private void refreshConsistencyDisplay() {
        if (plannedDaysPerWeek <= 0) {
            consistencyLabel.setText("Get a recommendation in My Plan to see your consistency score.");
            return;
        }
        int score = workoutLog.getConsistencyScore(plannedDaysPerWeek);
        String motivation = workoutLog.getMotivationMessage(plannedDaysPerWeek);
        consistencyLabel.setText("Consistency: " + score + "% — " + motivation);
    }

    private JPanel buildTipsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(sectionTitle("Tips & Goals"), BorderLayout.NORTH);

        tipsArea = new JTextArea();
        tipsArea.setEditable(false);
        tipsArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        tipsArea.setLineWrap(true);
        tipsArea.setWrapStyleWord(true);
        tipsArea.setBackground(Color.WHITE);
        tipsArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        tipsArea.setText(getTipsText());

        panel.add(new JScrollPane(tipsArea), BorderLayout.CENTER);
        return panel;
    }

    private String getTipsText() {
        StringBuilder sb = new StringBuilder();
        sb.append("GENERAL HEALTH & FITNESS TIPS\n");
        sb.append("====================================\n\n");
        sb.append("HYDRATION\n   Drink at least 8 glasses of water daily.\n   Increase intake on training days.\n\n");
        sb.append("WARM UP & COOL DOWN\n   Always warm up 5–10 minutes before exercise.\n   Cool down and stretch after every session.\n\n");
        sb.append("REST & RECOVERY\n   Rest at least 1 day between training the same muscle group.\n   Aim for 7–8 hours of sleep per night.\n\n");
        sb.append("NUTRITION\n   Track your meals alongside your workouts.\n   Prioritise protein for muscle recovery.\n\n");
        sb.append("CONSISTENCY\n   Consistency beats intensity. Show up every day.\n   Track your progress to stay motivated.\n\n");
        sb.append("====================================\n");

        if (currentUser != null) {
            String goal = currentUser.getFitnessGoal();
            sb.append("TIPS FOR YOUR GOAL: ").append(goal).append("\n\n");
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
            sb.append("Save your profile to see personalized goal tips.");
        }

        return sb.toString();
    }

    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, 19));
        label.setForeground(TEXT_DARK);
        // Thin accent rule under each section heading, then padding below it
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        return label;
    }

    private JLabel styledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        return label;
    }

    private JTextField addFormRow(JPanel panel, GridBagConstraints g, String labelText, int row, String hint) {
        g.gridy = row; g.gridx = 0;
        panel.add(styledLabel(labelText), g);
        JTextField field = placeholderField(hint, 20);
        g.gridx = 1;
        panel.add(field, g);
        return field;
    }

    private JTextField placeholderField(String hint, int cols) {
        JTextField field = new JTextField(cols);
        field.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        field.setToolTipText(hint);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        return field;
    }

    /** Shared table styling so both tables look consistent. */
    private void styleTable(JTable table) {
        table.setRowHeight(32);
        table.setFont(new Font(FONT_FAMILY, Font.PLAIN, 13));
        table.setGridColor(BORDER);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(226, 240, 232));
        table.setSelectionForeground(TEXT_DARK);
        table.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(TEXT_MUTED);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER));
    }

    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font(FONT_FAMILY, Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }

    private void updateStatus(String message) {
        statusBar.setText("  " + message);
    }
}