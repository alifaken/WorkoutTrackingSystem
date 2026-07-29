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
    // Compares today's muscle groups against the most recently logged
    // session. Returns null when there's nothing to warn about.
    public String getRecoveryWarning(ArrayList<String> todayMuscleGroups) {
        if (sessions.isEmpty() || todayMuscleGroups == null) {
            return null;
        }
        SessionRecord lastSession = sessions.get(sessions.size() - 1);
        for (String group : todayMuscleGroups) {
            if (lastSession.muscleGroups.contains(group)) {
                return "You trained " + group + " last session too. Consider a rest day for "
                        + group + " before training it again.";
            }
        }
        return null;
    }

    // ---------- Consistency Score ----------
    // plannedSessions is supplied by the caller (e.g. days available from
    // the user's plan) so this class stays simple and independently testable.
    public int getConsistencyScore(int plannedSessions) {
        if (plannedSessions <= 0) {
            return 0;
        }
        int score = (int) Math.round((sessions.size() * 100.0) / plannedSessions);
        return Math.min(score, 100);
    }

    // ---------- Smart Motivation ----------
    // Generates a message based on the consistency score band.
    public String getMotivationMessage(int plannedSessions) {
        if (sessions.isEmpty()) {
            return "You haven't logged a session yet. Let's get started!";
        }
        int score = getConsistencyScore(plannedSessions);
        if (score >= 90) {
            return "Excellent! You're right on track with your weekly goal. Keep it up!";
        } else if (score >= 60) {
            return "Good progress — you're mostly keeping up with your plan.";
        } else {
            return "You've missed a few sessions recently. Small steps are better than stopping completely.";
        }
    }
}