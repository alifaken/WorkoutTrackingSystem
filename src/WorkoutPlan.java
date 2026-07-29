import java.util.ArrayList;

/**
 * WorkoutPlan.java — Member 4
 *
 * Holds a collection of Exercise objects that make up the user's workout plan.
 * DO NOT add Scanner or System.out here.
 *
 * TODO (Member 4):
 *   1. Add a private ArrayList<Exercise> field to store exercises
 *   2. Add a constructor that initializes the ArrayList
 *   3. Add addExercise(Exercise e)
 *   4. Add removeExercise(int index)
 *   5. Add getExercises() — returns the full ArrayList
 *   6. Add getTotalDuration() — sums up duration of all exercises
 */
public class WorkoutPlan {

    // TODO: Add your field here
    private ArrayList<Exercise> exercises;

    // TODO: Add constructor here
    public WorkoutPlan() {
        exercises = new ArrayList<>();
    }

    // TODO: Add addExercise(Exercise e)
    public void addExercise(Exercise e) {
        exercises.add(e);
    }

    // TODO: Add removeExercise(int index)
    public void removeExercise(int index) {
        if (index >= 0 && index < exercises.size()) {
            exercises.remove(index);
        }
    }

    // TODO: Add getExercises()
    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    // TODO: Add getTotalDuration()
    public int getTotalDuration() {
        int total = 0;
        for (Exercise e : exercises) {
            total += e.getDuration();
        }
        return total;
    }
}
