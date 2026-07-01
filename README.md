# 🏋️ Workout Planning & Tracking System
**BIT1053 Computer Programming | MAY 2026 | City University Malaysia**

## 📌 Project Description
A Java Swing GUI application for workout planning and tracking, aligned with
SDG 3: Good Health and Well-Being.

Users can create a profile, build a workout plan, track completed sessions,
view their history, and get fitness tips.

## 👥 Group Members & Task Assignment

| No | Name | Student ID   | Code Task                            | Report Section          |
|----|------|--------------|--------------------------------------|-------------------------|
| 1  | Alif | 202407010086 | Main.java + WorkoutGUI.java (Leader) | Implementation          |
| 2  |      |              | User.java                            | Introduction            |
| 3  |      |              | Exercise.java                        | System Description      |
| 4  | Affy | 202604010009 | WorkoutPlan.java                     | Program Design          |
| 5  |      |              | WorkoutLog.java + 15 Test Cases      | Testing & Results       |
| 6  |      |              | CalorieCalculator.java               | Discussion + Conclusion |
| 7  |      |              | InputValidator.java + Poster         | Appendix                |

## 📁 Folder Structure
```
WorkoutTrackingSystem/
├── src/
│   ├── Main.java
│   ├── WorkoutGUI.java
│   ├── User.java
│   ├── Exercise.java
│   ├── WorkoutPlan.java
│   ├── WorkoutLog.java
│   ├── CalorieCalculator.java
│   └── InputValidator.java
├── report/
├── .gitignore
└── README.md
```

## ▶️ How To Run
1. Clone the repo (or open it directly in IntelliJ via "Get from VCS")
2. Let IntelliJ index the project
3. Right-click `Main.java` → **Run 'Main.main()'**

Or from terminal:
```bash
cd src
javac *.java -d ../out
java -cp ../out Main
```

## ⚙️ Tools Used
- Java (JDK 21)
- Java Swing (GUI — built into the JDK, no external frameworks)
- IntelliJ IDEA
- GitHub

## 📋 Rules for Contributors
- Always `Git → Pull` before you start working
- Only edit **your own assigned file**
- Do not add `Scanner` or `System.out` inside your class — all input/output
  goes through `WorkoutGUI.java`
- Commit message format: `"Add User class with constructor and getters"`
- Push when done for the day
- If something breaks — message the leader before trying to fix it yourself

## ✅ Current Status
- [x] Skeleton code created and compiles successfully
- [x] GUI shell with 5 tabs (Profile, Plan, Track, History, Tips)
- [ ] Member classes implemented
- [ ] GUI wired to real classes (replace TODO sections)
- [ ] 15 test cases written and verified
- [ ] Poster designed
- [ ] Report sections written
