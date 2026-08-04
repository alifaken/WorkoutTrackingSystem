import java.util.ArrayList;

/**
 * WorkoutLog.java — Member 5
 *
 * Records completed workout sessions and powers two smart features:
 * Recovery Intelligence, and Consistency Score + Smart Motivation.
 * DO NOT add Scanner or System.out here.
 */
public class WorkoutLog {

    // One completed session, stored as structured data (not a pre-formatted
    // string) so muscle groups can actually be compared for Recovery Intelligence.
    private static class SessionRecord {
        String date;
        int exerciseCount;
        int duration;
        int calories;
        ArrayList<String> muscleGroups;

        SessionRecord(String date, int exerciseCount, int duration, int calories, ArrayList<String> muscleGroups) {
            this.date = date;
            this.exerciseCount = exerciseCount;
            this.duration = duration;
            this.calories = calories;
            this.muscleGroups = muscleGroups;
        }
    }

    private ArrayList<SessionRecord> sessions;

    public WorkoutLog() {
        sessions = new ArrayList<>();
    }

    // Logs a completed session. A session can cover more than one muscle
    // group (e.g. chest + legs in the same sitting), so this takes a list.
    public void logSession(String date, int exerciseCount, int duration, int calories, ArrayList<String> muscleGroups) {
        if (muscleGroups == null) {
            muscleGroups = new ArrayList<>();
        }
        sessions.add(new SessionRecord(date, exerciseCount, duration, calories, muscleGroups));
    }

    // Returns display-ready strings for the History table.
    public ArrayList<String> getHistory() {
        ArrayList<String> formatted = new ArrayList<>();
        for (SessionRecord s : sessions) {
            formatted.add(s.date + " | Exercises: " + s.exerciseCount
                    + " | Duration: " + s.duration + " min"
                    + " | Calories: " + s.calories + " kcal");
        }
        return formatted;
    }

    // Returns each session as a row matching the History table's columns:
    // {Date, Exercises Completed, Duration (min), Calories Burned}
    public ArrayList<Object[]> getHistoryRows() {
        ArrayList<Object[]> rows = new ArrayList<>();
        for (SessionRecord s : sessions) {
            rows.add(new Object[]{s.date, s.exerciseCount + " exercises", s.duration, s.calories});
        }
        return rows;
    }

    public void clearHistory() {
        sessions.clear();
    }

    // ---------- Recovery Intelligence ----------
    // A muscle group is considered still recovering if it was trained within
    // the last 2 days. Muscle tissue generally needs roughly 48 hours to
    // recover before being trained hard again.
    private static final int RECOVERY_DAYS = 2;

    // Checks each muscle group the user is training today against how many
    // days have actually passed since that group was last trained.
    // Returns null when nothing needs a warning.
    public String getRecoveryWarning(String todayDate, ArrayList<String> todayMuscleGroups) {
        if (sessions.isEmpty() || todayMuscleGroups == null || todayDate == null) {
            return null;
        }

        for (String group : todayMuscleGroups) {
            int daysSince = getDaysSinceTrained(todayDate, group);

            // -1 means this group has never been trained before, so no warning
            if (daysSince >= 0 && daysSince < RECOVERY_DAYS) {
                if (daysSince == 0) {
                    return group + " was already trained today. Training the same muscle group "
                            + "twice in one day leaves no time for recovery.";
                }
                return group + " was trained " + daysSince + " day ago. Muscle groups usually need "
                        + "about " + RECOVERY_DAYS + " days to recover, so consider training "
                        + "something else today.";
            }
        }
        return null;
    }

    // Searches backwards through history for the most recent session that
    // trained this muscle group, then returns how many days ago that was.
    // Returns -1 if the group has never been trained.
    private int getDaysSinceTrained(String todayDate, String muscleGroup) {
        for (int i = sessions.size() - 1; i >= 0; i--) {
            SessionRecord s = sessions.get(i);
            if (s.muscleGroups.contains(muscleGroup)) {
                return daysBetween(s.date, todayDate);
            }
        }
        return -1;
    }

    // Calculates the number of days between two dates in dd/MM/yyyy format.
    // Returns a large number if either date cannot be read, so an unreadable
    // date never triggers a false warning.
    private int daysBetween(String earlierDate, String laterDate) {
        try {
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.util.Date d1 = format.parse(earlierDate);
            java.util.Date d2 = format.parse(laterDate);

            long millisecondsApart = d2.getTime() - d1.getTime();
            long millisecondsPerDay = 1000L * 60 * 60 * 24;
            return (int) (millisecondsApart / millisecondsPerDay);

        } catch (java.text.ParseException e) {
            return Integer.MAX_VALUE;
        }
    }

    // ---------- Consistency Score ----------
    // Counts only sessions from the last 7 days, so the score reflects THIS
    // week's consistency rather than everything ever logged.
    // plannedSessions is supplied by the caller (the user's days-available
    // setting) so this class stays simple and independently testable.
    public int getConsistencyScore(String todayDate, int plannedSessions) {
        if (plannedSessions <= 0) {
            return 0;
        }
        int completedThisWeek = getSessionsInLastWeek(todayDate).size();
        int score = (int) Math.round((completedThisWeek * 100.0) / plannedSessions);
        return Math.min(score, 100);
    }

    // ---------- Weekly Progress Analysis ----------
    // Looks at the last 7 days only, and combines two checks:
    //   1. How many sessions were completed compared to the weekly target
    //   2. Whether any muscle group is over- or under-trained
    // Returns a list of plain-language suggestions for the user.
    public ArrayList<String> getWeeklyAnalysis(String todayDate, int plannedDaysPerWeek) {
        ArrayList<String> suggestions = new ArrayList<>();
        ArrayList<SessionRecord> recent = getSessionsInLastWeek(todayDate);

        if (recent.isEmpty()) {
            suggestions.add("No sessions logged in the last 7 days. Start with one short workout to build momentum.");
            return suggestions;
        }

        // --- Check 1: session frequency against the weekly target ---
        int completed = recent.size();
        if (plannedDaysPerWeek > 0) {
            if (completed > plannedDaysPerWeek) {
                suggestions.add("You completed " + completed + " sessions against a target of "
                        + plannedDaysPerWeek + ". You are exceeding your weekly goal - make sure you are still resting enough.");
            } else if (completed == plannedDaysPerWeek) {
                suggestions.add("You hit your target of " + plannedDaysPerWeek + " sessions this week. Well done.");
            } else {
                int missed = plannedDaysPerWeek - completed;
                suggestions.add("You completed " + completed + " of " + plannedDaysPerWeek
                        + " planned sessions. Try to fit in " + missed + " more next week.");
            }
        }

        // --- Check 2: muscle group balance ---
        ArrayList<String> trainedGroups = new ArrayList<>();
        ArrayList<Integer> groupCounts = new ArrayList<>();

        for (SessionRecord s : recent) {
            for (String group : s.muscleGroups) {
                int index = trainedGroups.indexOf(group);
                if (index == -1) {
                    trainedGroups.add(group);
                    groupCounts.add(1);
                } else {
                    groupCounts.set(index, groupCounts.get(index) + 1);
                }
            }
        }

        // Flag any group trained 3 or more times in one week as too frequent
        for (int i = 0; i < trainedGroups.size(); i++) {
            if (groupCounts.get(i) >= 3) {
                suggestions.add(trainedGroups.get(i) + " was trained " + groupCounts.get(i)
                        + " times this week. Consider spreading your training across other muscle groups.");
            }
        }

        // Point out a major group that has been skipped entirely
        String[] majorGroups = {"Chest", "Back", "Legs"};
        for (String major : majorGroups) {
            if (!trainedGroups.contains(major)) {
                suggestions.add(major + " was not trained at all this week. Adding it would give you a more balanced routine.");
            }
        }

        if (suggestions.isEmpty()) {
            suggestions.add("Your training looks well balanced this week. Keep it up.");
        }

        return suggestions;
    }

    // Collects sessions logged within the last 7 days of the given date.
    private ArrayList<SessionRecord> getSessionsInLastWeek(String todayDate) {
        ArrayList<SessionRecord> recent = new ArrayList<>();
        for (SessionRecord s : sessions) {
            int daysAgo = daysBetween(s.date, todayDate);
            if (daysAgo >= 0 && daysAgo < 7) {
                recent.add(s);
            }
        }
        return recent;
    }

    // ---------- Smart Motivation ----------
    // Generates a message based on the consistency score band.
    public String getMotivationMessage(String todayDate, int plannedSessions) {
        if (sessions.isEmpty()) {
            return "You haven't logged a session yet. Let's get started!";
        }
        if (getSessionsInLastWeek(todayDate).isEmpty()) {
            return "No sessions logged this week yet. One short workout is enough to restart the habit.";
        }
        int score = getConsistencyScore(todayDate, plannedSessions);
        if (score >= 90) {
            return "Excellent! You're right on track with your weekly goal. Keep it up!";
        } else if (score >= 60) {
            return "Good progress — you're mostly keeping up with your plan.";
        } else {
            return "You've missed a few sessions recently. Small steps are better than stopping completely.";
        }
    }
}