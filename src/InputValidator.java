public class InputValidator {

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isValidAge(int age) {
        return age >= 1 && age <= 120;
    }

    public static boolean isValidWeight(double weight) {
        return weight > 0;
    }

    public static boolean isValidHeight(double height) {
        return height > 0;
    }

    public static boolean isValidSets(int sets) {
        return sets >= 1;
    }

    public static boolean isValidReps(int reps) {
        return reps >= 1;
    }

    public static boolean isValidDuration(int duration) {
        return duration >= 1;
    }

    public static boolean isValidDaysAvailable(int days) {
        return days >= 1 && days <= 7;
    }
}