package model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//journal that records workouts, exercises
public class GymJournal {

    private ArrayList<OpenWorkout> workoutHistory;
    private HashMap<String, WorkoutExerciseSet> personalBests;
    private HashMap<String, Double> oneRepMaxes;
    private HashMap<String, Routine> routineHashMap;

    // MODIFIES: this
    // EFFECTS: initializes hashmaps and arraylist
    public GymJournal() {
        workoutHistory = new ArrayList<>();
        personalBests = new HashMap<>();
        oneRepMaxes = new HashMap<>();
        routineHashMap = new HashMap<>();
    }

    // MODIFIES: this
    // EFFECTS: adds workout to workout history
    public void addWorkout(OpenWorkout openWorkout) {
        this.workoutHistory.add(openWorkout);
        updateExercises(openWorkout);
    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS:
    private void updateExercises(OpenWorkout workout) {
        HashMap<String, WorkoutExerciseSet> heaviestSets = workout.heaviestSets();
        updatePersonalBests(heaviestSets);
        updateOneRepMaxes(heaviestSets);
    }

    // REQUIRES: heaviestSet to not be null
    // MODIFIES: this
    // EFFECTS: updates personal bests, if any weights in heaviest set are more than
    // corresponding weight in personal best or if weight is the same, but with more reps
    private void updatePersonalBests(HashMap<String, WorkoutExerciseSet> heaviestSets) {
        for (Map.Entry<String, WorkoutExerciseSet> set : heaviestSets.entrySet()) {
            WorkoutExerciseSet currentPB = personalBests.get(set.getKey());
            if (currentPB != null) {
                if (set.getValue().getWeight() > currentPB.getWeight()) {
                    personalBests.put(set.getKey(), set.getValue());
                } else if (set.getValue().getWeight() == currentPB.getWeight()) {
                    if (set.getValue().getReps() > currentPB.getReps()) {
                        personalBests.put(set.getKey(), set.getValue());
                    }
                }
            } else {
                personalBests.put(set.getKey(), set.getValue());
            }
        }
    }

    // REQUIRES: heaviestSet to not be null
    // MODIFIES: this
    // EFFECTS: updates oneRepMaxes if a set from heaviest sets has a heavier 1 rep max
    private void updateOneRepMaxes(HashMap<String, WorkoutExerciseSet> heaviestSets) {
        for (Map.Entry<String, WorkoutExerciseSet> set : heaviestSets.entrySet()) {
            double possibleRM = set.getValue().calculateOneRepMax();
            if (oneRepMaxes.get(set.getKey()) != null) {
                double currentRM = oneRepMaxes.get(set.getKey());
                if (possibleRM > currentRM) {
                    oneRepMaxes.put(set.getKey(), possibleRM);
                }
            } else {
                oneRepMaxes.put(set.getKey(), possibleRM);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: adds routine to routineHashMap
    public void addRoutine(Routine routine) {
        this.routineHashMap.put(routine.getName(), routine);
    }

    // EFFECTS: returns routine corresponding to string key
    public Routine findRoutine(String string) {
        return routineHashMap.get(string);
    }

    // EFFECTS: returns string of all routines in routineHashMap
    public String routineString() {
        String result = "";
        for (HashMap.Entry<String, Routine> routine : routineHashMap.entrySet()) {
            result = result.concat(routine.getValue().toString() + "\n");
        }
        return result;
    }

    // EFFECTS: returns first num elements of workoutHistory as string
    //          if num > workoutHistory size, returns workoutHistory string
    public String workoutString(int num) {
        String result = "";
        if (num > workoutHistory.size()) {
            for (OpenWorkout workout : workoutHistory) {
                result = result.concat(workout.toString() + "\n");
            }
        } else {
            for (int i = 0; i < num; i++) {
                result = result.concat(workoutHistory.get(i).toString() + "\n");
            }
        }
        return result;
    }

    public String displayOneRepMaxes() {
        String result = "";
        String title = "ONE REP MAXES\n-------------\n";
        for (Map.Entry oneRepMax : oneRepMaxes.entrySet()) {
            result = result.concat(oneRepMax.getKey() + ": " + oneRepMax.getValue() + " lbs\n");
        }
        return title.concat(result);
    }

    public String displayPersonalBests() {
        String result = "";
        String title = "PERSONAL BESTS\n--------------\n";
        for (HashMap.Entry<String, WorkoutExerciseSet> personalBest : personalBests.entrySet()) {
            result = result.concat(personalBest.getKey() + ": "
                    + personalBest.getValue().getReps() + " reps,"
                    + personalBest.getValue().getWeight() + " lbs\n");
        }
        return title.concat(result);
    }
}
