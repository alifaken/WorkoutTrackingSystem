/**
 * User.java — Member 2
 *
 * Stores the user's personal profile information.
 * DO NOT add Scanner or System.out here — all input/output is handled by WorkoutGUI.java
 */
public class User {

    private String name;
    private int age;
    private double weight;       // in kg
    private double height;       // in cm
    private String gender;       // "Male" or "Female"
    private String fitnessGoal;  // "Weight Loss", "Muscle Gain", "General Fitness"
    private String level;        // "Beginner", "Intermediate", "Advanced"

    // Constructor
    public User(String name, int age, double weight, double height,
                String gender, String fitnessGoal, String level) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.gender = gender;
        this.fitnessGoal = fitnessGoal;
        this.level = level;
    }

    // ---------- Getters ----------

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public String getGender() {
        return gender;
    }

    public String getFitnessGoal() {
        return fitnessGoal;
    }

    public String getLevel() {
        return level;
    }

    // ---------- Setters ----------

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFitnessGoal(String fitnessGoal) {
        this.fitnessGoal = fitnessGoal;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    // ---------- Calculations ----------

    // BMI = weight(kg) / height(m)^2
    public double getBMI() {
        double heightInMeters = height / 100;
        return weight / (heightInMeters * heightInMeters);
    }

    // BMI category based on WHO standard
    public String getBMICategory() {
        double bmi = getBMI();
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25.0) {
            return "Normal";
        } else if (bmi < 30.0) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    // BMR = calories burned at rest (Mifflin-St Jeor formula)
    public double getBMR() {
        double bmr = (10 * weight) + (6.25 * height) - (5 * age);

        // The Mifflin-St Jeor formula adds 5 for males and subtracts 161 for females
        if (gender.equalsIgnoreCase("Male")) {
            bmr = bmr + 5;
        } else {
            bmr = bmr - 161;
        }

        return bmr;
    }

    // Builds a formatted profile summary as a String.
    // WorkoutGUI.java can drop this into a JLabel/JTextArea (or print it)
    // without User.java doing any I/O of its own.
    public String getProfileSummary() {
        double roundedBMI = Math.round(getBMI() * 10) / 10.0;
        return "Name   : " + name + "\n"
                + "Age    : " + age + "\n"
                + "Weight : " + weight + " kg\n"
                + "Height : " + height + " cm\n"
                + "Gender : " + gender + "\n"
                + "Goal   : " + fitnessGoal + "\n"
                + "Level  : " + level + "\n"
                + "BMI    : " + roundedBMI + " (" + getBMICategory() + ")";
    }

    // ---------- Beginner Risk Detector ----------
    // Educational flag only — NOT medical advice.
    // Returns null when no caution is needed.
    public String getRiskMessage() {
        String category = getBMICategory();

        if (category.equals("Underweight") && fitnessGoal.equalsIgnoreCase("Weight Loss")) {
            return "Based on your BMI and goal, consider consulting a fitness professional "
                    + "before pursuing further weight loss. Building strength or maintaining a "
                    + "healthy weight may be more suitable.";
        }

        if (category.equals("Obese") && level.equalsIgnoreCase("Beginner")) {
            return "Based on your BMI, starting with lower-intensity activity and consulting "
                    + "a fitness professional is recommended before beginning an intense training program.";
        }

        return null;
    }
}