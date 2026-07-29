/**
 * Exercise.java — Member 3
 *
 * Represents a single exercise within a workout plan.
 * DO NOT add Scanner or System.out here — all input/output is handled by WorkoutGUI.java
 */
public class Exercise {

    private String name;
    private String muscleGroup;
    private int sets;
    private int reps;
    private int duration;  // in minutes

    // Constructor
    public Exercise(String name, String muscleGroup, int sets, int reps, int duration) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.sets = sets;
        this.reps = reps;
        this.duration = duration;
    }

    // ---------- Getters ----------

    public String getName() {
        return name;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public int getDuration() {
        return duration;
    }

    // ---------- Setters ----------

    public void setName(String name) {
        this.name = name;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    // ---------- Display ----------

    // One clean, readable line summarizing the exercise —
    // used when this exercise is shown in the GUI table or checklist.
    @Override
    public String toString() {
        return name + " (" + muscleGroup + ") — " + sets + " sets x " + reps + " reps, " + duration + " min";
    }
}