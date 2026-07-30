import java.util.ArrayList;

public class WorkoutPlan {
    private ArrayList<Exercise> exercises;

    public WorkoutPlan() {
        exercises = new ArrayList<>();
    }

    public void addExercise(Exercise e) {
        exercises.add(e);
    }

    public void removeExercise(int index) {
        if (index >= 0 && index < exercises.size()) {
            exercises.remove(index);
        }
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public int getTotalDuration() {
        int total = 0;
        for (Exercise e : exercises) {
            total += e.getDuration();
        }
        return total;
    }

    // ---------- Adaptive Workout Recommendation ----------
    // Suggests a weekly training split based on goal, experience level, and
    // how many days the user has available. Training days are spread across
    // the week rather than clustered, giving muscle groups recovery time.
    // Beginners are capped at 4 training days regardless of availability,
    // since new lifters generally need more recovery between sessions —
    // extra days become explicit rest days instead of being silently ignored.
    public ArrayList<String> generateRecommendation(String goal, String level, int daysAvailable) {
        ArrayList<String> plan = new ArrayList<>();

        int effectiveDays = daysAvailable;
        if (level.equalsIgnoreCase("Beginner") && daysAvailable > 4) {
            effectiveDays = 4;
        }

        String[] schedule = getDaySchedule(daysAvailable);
        String[] splitBlocks = getSplitBlocks(goal);

        for (int i = 0; i < schedule.length; i++) {
            if (i < effectiveDays) {
                // There are only 4 split blocks but the user may train up to 7 days.
                // The remainder operator (%) wraps the index back to the start of the
                // array once it passes the end, so the blocks repeat in a cycle.
                int blockIndex = i % splitBlocks.length;
                plan.add(schedule[i] + ": " + splitBlocks[blockIndex]);
            } else {
                plan.add(schedule[i] + ": Rest / Recovery (capped for beginners)");
            }
        }
        return plan;
    }

    // Spreads training days across the week instead of clustering them.
    private String[] getDaySchedule(int daysAvailable) {
        switch (daysAvailable) {
            case 1: return new String[]{"Monday"};
            case 2: return new String[]{"Monday", "Thursday"};
            case 3: return new String[]{"Monday", "Wednesday", "Friday"};
            case 4: return new String[]{"Monday", "Tuesday", "Thursday", "Friday"};
            case 5: return new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
            case 6: return new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            default: return new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        }
    }

    // Muscle group pairings per goal, cycled across however many training
    // days are actually scheduled.
    private String[] getSplitBlocks(String goal) {
        switch (goal) {
            case "Muscle Gain":
                return new String[]{"Chest + Triceps", "Back + Biceps", "Legs + Shoulders", "Core + Full Body"};
            case "Weight Loss":
                return new String[]{"Cardio + Full Body", "Legs + Core", "Cardio + Upper Body", "Full Body"};
            default: // General Fitness
                return new String[]{"Full Body", "Cardio + Core", "Upper Body", "Lower Body"};
        }
    }
}