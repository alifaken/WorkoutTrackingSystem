# FitStart — An Intelligent Workout Companion for Beginner University Students
**BIT1053 Computer Programming | MAY 2026 | City University Malaysia**

## Project Description
A Java Swing GUI application that helps beginner university students build safe,
consistent exercise habits — aligned with SDG 3: Good Health and Well-Being.

FitStart does not just record workouts. It interprets them: recommending balanced
weekly splits, warning against overtraining, measuring consistency, and analysing
weekly progress to suggest what to change next.

## Group Members & Task Assignment

| No | Name     | Student ID   | Code Task                              | Report Section          |
|----|----------|--------------|----------------------------------------|-------------------------|
| 1  | Alif     | 202407010086 | Main.java + WorkoutGUI.java (Leader)   | Implementation          |
| 2  | Bernara  | 202504010021 | User.java                              | Introduction            |
| 3  | Wajhi    | 202605010424 | Exercise.java                          | System Description      |
| 4  | Affy     | 202604010009 | WorkoutPlan.java + Poster + flowchart  | Program Design          |
| 5  | Ihab     | 202605010503 | WorkoutLog.java + 15 Test Cases        | Testing & Results       |
| 6  | Shalini  | 202409010170 | CalorieCalculator.java                 | Discussion + Conclusion |
| 7  | Ali      | 202509010008 | InputValidator.java                    | Appendix                |

## Features

The application is organised into five tabs:

| Tab | What it does |
|---|---|
| **Profile** | Enter personal details; calculates BMI and BMR |
| **Workout Planner** | Build a plan manually, or generate a recommended weekly split |
| **Today's Workout** | Tick off completed exercises; estimates calories burned |
| **Progress** | Session history, consistency score, and weekly analysis |
| **Smart Coach** | General fitness tips plus advice tailored to your goal |

### The five smart features

| Feature | Lives in | What it does |
|---|---|---|
| Beginner Risk Detector | `User` | Combines BMI and goal to show an educational caution when the pairing may warrant care |
| Adaptive Recommendation | `WorkoutPlan` | Generates a weekly split from goal, experience level, and days available |
| Recovery Intelligence | `WorkoutLog` | Warns if a muscle group was trained within the last 2 days |
| Consistency Score + Motivation | `WorkoutLog` | Scores completed vs planned sessions and responds with a fitting message |
| Weekly Progress Analysis | `WorkoutLog` | Reviews the last 7 days for session frequency and muscle group balance |

> Health-related messages in FitStart are educational only and are not medical advice.

## Folder Structure
```
WorkoutTrackingSystem/
├── src/
│   ├── Main.java              (Leader)   — entry point
│   ├── WorkoutGUI.java        (Leader)   — interface and integration
│   ├── User.java              (Member 2) — profile, BMI/BMR, risk detector
│   ├── Exercise.java          (Member 3) — a single exercise
│   ├── WorkoutPlan.java       (Member 4) — plan storage + recommendations
│   ├── WorkoutLog.java        (Member 5) — history + 3 smart features
│   ├── CalorieCalculator.java (Member 6) — calorie estimation
│   └── InputValidator.java    (Member 7) — all validation rules
├── report/
├── .gitignore
└── README.md
```

## How To Run
1. Clone the repo (or open it directly in IntelliJ via "Get from VCS")
2. Let IntelliJ index the project
3. Right-click `Main.java` → **Run 'Main.main()'**

Or from terminal:
```bash
cd src
javac *.java -d ../out
java -cp ../out Main
```

### First time opening in IntelliJ after cloning
A fresh clone has no IntelliJ configuration, since `.idea/` is intentionally
gitignored. If you see "output path is not specified" or "Cannot find main class":

1. Right-click the `src` folder → **Mark Directory as → Sources Root**
2. **File → Project Structure → Project** → confirm a Project SDK is selected
3. **File → Project Structure → Modules → Paths** → select *Inherit project compile output path*
4. **Build → Rebuild Project**, then run `Main.java`

This is normal one-time setup, not a problem with the code.

## Tools Used
- Java (JDK 21+)
- Java Swing (GUI — built into the JDK, no external frameworks)
- IntelliJ IDEA
- GitHub

Confirmed with the course instructor that a Java Swing GUI application satisfies
the execution requirement, without separate command-line execution.

## Rules for Contributors
- Always `Git → Pull` before you start working
- Only edit **your own assigned file**
- Do not add `Scanner` or `System.out` inside your class — all input/output
  goes through `WorkoutGUI.java`
- Commit message format: `"Add User class with constructor and getters"`
- Push when done for the day
- If something breaks — message the leader before trying to fix it yourself

**If a push is rejected with "email privacy restrictions":**
GitHub → Settings → Emails → uncheck *Block command line pushes that expose my email*.

## Current Status
- [x] All 8 classes implemented and compiling cleanly
- [x] GUI fully wired — all five tabs working with real data
- [x] Beginner Risk Detector
- [x] Adaptive Workout Recommendation
- [x] Recovery Intelligence (date-based)
- [x] Consistency Score + Smart Motivation
- [x] Weekly Progress Analysis
- [x] Poster designed
- [ ] 15 test cases written and verified
- [ ] GUI screenshots captured
- [ ] Report sections written