import java.util.*;

public class SimEnka {
    public static TreeSet<Integer> calcEpsEnv(Map<Integer, TreeSet<Integer>> epsilonPrijelazi, TreeSet<Integer> currStates) {
        if (epsilonPrijelazi == null || currStates == null || currStates.isEmpty()) return currStates;

        boolean notDone = true;
        while (notDone) {
            TreeSet<Integer> epsEnvStates = new TreeSet<>(currStates);
            for (Integer state : currStates) {
                if (epsilonPrijelazi.containsKey(state)) {
                    epsEnvStates.addAll(epsilonPrijelazi.get(state));
                }
            }
            if (epsEnvStates.equals(currStates)) notDone = false;
            else currStates = epsEnvStates;
        }
        return currStates;
    }
}
