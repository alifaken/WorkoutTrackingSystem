/**
 * CalorieCalculator.java — Member 6 */

import java.util.ArrayList;

public class CalorieCalculator {

    public static int calculateCalories(String muscleGroup, int durationMinutes) {

        if (muscleGroup == null || durationMinutes <= 0) {
            return 0;
        }

        int calorieRate;


        switch (muscleGroup.trim().toLowerCase()) {

            case "cardio":
                calorieRate = 10;
                break;

            case "full body":
                calorieRate = 8;
                break;

            case "legs":
                calorieRate = 7;
                break;

            case "chest":
            case "back":
            case "shoulders":
                calorieRate = 6;
                break;

            case "biceps":
            case "triceps":
            case "core":
                calorieRate = 5;
                break;

            default:
                calorieRate = 0;
                break;
        }

        return durationMinutes * calorieRate;
    }

    public static int calculateTotalCalories(ArrayList<Exercise> exercises) {

        if (exercises == null) {
            return 0;
        }

        int totalCalories = 0;

        for (Exercise exercise : exercises) {

            if (exercise != null) {
                totalCalories += calculateCalories(
                        exercise.getMuscleGroup(),
                        exercise.getDuration()
                );
            }
        }

        return totalCalories;
    }
}