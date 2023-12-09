import util.Util;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Map<String, Boolean>> impTablica = Util.getImpTablica();
        Map<String, Map<String, Boolean>> ekspTablica = Util.getEkspTablica();

        System.out.println("Implicitna kastabilnost:");
        for (String tip1 : impTablica.keySet()) {
            for (String tip2 : impTablica.get(tip1).keySet()) {
                System.out.println(tip1 + " -> " + tip2 + ": " + impTablica.get(tip1).get(tip2));
            }
        }

        System.out.println("\nEksplicitna kastabilnost:");
        for (String tip1 : ekspTablica.keySet()) {
            for (String tip2 : ekspTablica.get(tip1).keySet()) {
                System.out.println(tip1 + " -> " + tip2 + ": " + ekspTablica.get(tip1).get(tip2));
            }
        }
    }
}
