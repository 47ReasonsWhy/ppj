package util;

import znakovi.Deklaracija;
import znakovi.Tablice;
import znakovi.Zavrsnost;
import znakovi.Znak;

import java.util.*;

public class Util {
    private static Map<String, Map<String, Boolean>> impTablica;
    private static Map<String, Map<String, Boolean>> ekspTablica;
    private static boolean initialized = false;

    public static Map<String, Map<String, Boolean>> getImpTablica() {
        if (!initialized) {
            init();
            initialized = true;
        }
        return Collections.unmodifiableMap(impTablica);
    }

    public static Map<String, Map<String, Boolean>> getEkspTablica() {
        if (!initialized) {
            init();
            initialized = true;
        }
        return Collections.unmodifiableMap(ekspTablica);
    }

    public static Deklaracija pronadjiDeklaraciju(Znak znak, String jedinka) {
        if (znak == null) {
            return null;
        }
        Tablice tablice = znak.tablice;
        while (tablice != null) {
            if (tablice.tablicaDeklaracija.containsKey(jedinka)) {
                return tablice.tablicaDeklaracija.get(jedinka);
            }
            tablice = tablice.tablicaUgnjezdjujucegBloka;
        }
        return null;
    }

    public static void ispisiGresku(Znak znak) {
        System.out.println(
                znak.ime + " ::= " +
                znak.djeca.stream()
                        .map(z -> z.ime + (
                                z.zavrsnost == Zavrsnost.NEZAVRSNI ? "" :
                                "(" + z.linija + "," + z.jedinka + ")"
                        ))
                        .reduce((a, b) -> a + " " + b)
                        .orElse("")
        );
    }

    public static boolean implicitnoKastabilan(String tip1, String tip2) {
        if (!initialized) {
            init();
            initialized = true;
        }
        if (impTablica.containsKey(tip1) && impTablica.get(tip1).containsKey(tip2)) {
            return impTablica.get(tip1).get(tip2);
        } else {
            return false;
        }
    }

    public static boolean eksplicitnoKastabilan(String tip1, String tip2) {
        if (!initialized) {
            init();
            initialized = true;
        }
        if (ekspTablica.containsKey(tip1) && ekspTablica.get(tip1).containsKey(tip2)) {
            return ekspTablica.get(tip1).get(tip2);
        } else {
            return false;
        }
    }
    public static void init() {
        impTablica = new HashMap<>();

        impTablica.put("char", new HashMap<>());
        impTablica.put("int", new HashMap<>());
        impTablica.put("const(char)", new HashMap<>());
        impTablica.put("const(int)", new HashMap<>());
        impTablica.put("niz(char)", new HashMap<>());
        impTablica.put("niz(int)", new HashMap<>());
        impTablica.put("niz(const(char))", new HashMap<>());
        impTablica.put("niz(const(int))", new HashMap<>());

        impTablica.get("char").put("char", true);
        impTablica.get("int").put("int", true);
        impTablica.get("const(char)").put("const(char)", true);
        impTablica.get("const(int)").put("const(int)", true);
        impTablica.get("niz(char)").put("niz(char)", true);
        impTablica.get("niz(int)").put("niz(int)", true);
        impTablica.get("niz(const(char))").put("niz(const(char))", true);
        impTablica.get("niz(const(int))").put("niz(const(int))", true);

        impTablica.get("const(char)").put("char", true);
        impTablica.get("const(int)").put("int", true);
        impTablica.get("char").put("const(char)", true);
        impTablica.get("int").put("const(int)", true);

        impTablica.get("char").put("int", true);

        impTablica.get("niz(char)").put("niz(const(char))", true);
        impTablica.get("niz(int)").put("niz(const(int))", true);

        // izracunaj tranzitivno zatvorenje implicitne tablice
        izracunajTranzitivnoOkruzenje(impTablica);

        // eksplicitna tablica
        ekspTablica = new HashMap<>(impTablica);
        ekspTablica.replaceAll((t, v) -> new HashMap<>(impTablica.get(t)));
        ekspTablica.get("int").put("char", true);

        // izracunaj tranzitivno zatvorenje eksplicitne tablice
        izracunajTranzitivnoOkruzenje(ekspTablica);
    }

    private static void izracunajTranzitivnoOkruzenje(Map<String, Map<String, Boolean>> tablica) {
        boolean promjena = true;
        while (promjena) {
            promjena = false;
            for (String tip1_ : tablica.keySet()) {
                for (String tip2_ : tablica.keySet()) {
                    for (String tip3_ : tablica.keySet()) {
                        if (tablica.get(tip1_).containsKey(tip2_) &&
                            tablica.get(tip2_).containsKey(tip3_) &&
                            !tablica.get(tip1_).containsKey(tip3_)) {
                                tablica.get(tip1_).put(tip3_, true);
                                promjena = true;
                        }
                    }
                }
            }
        }
    }

    public static Map.Entry<Integer, Integer> splitIntConst(String s) {
        int x;
        try {
            x = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.err.println("Nece se nikada dogoditi.");
            System.exit(1);
            return new AbstractMap.SimpleEntry<>(0, 0);
        }
        int xhi = x >> 20;
        int xlo = x & 0xFFFFF;
        return new AbstractMap.SimpleEntry<>(xhi, xlo);
    }

    public static void strpajKod(Znak z, List<String> kod) {
        if (z == null || kod == null) {
            // ne bi se trebalo dogoditi
            System.err.println("strpajKod() pozvan s null argumentom.");
            return;
        }
        boolean global = true;
        Znak zz = z;
        while (zz != null) {
            if (zz.ime.equals("<definicija_funkcije>")) {
                global = false;
                break;
            }
            zz = zz.roditelj;
        }

        List<String> listaKamoStrpatiKod;
        if (global) {
            listaKamoStrpatiKod = z.tablice.kodGlobalnihInicijalizacija;
        } else {
            listaKamoStrpatiKod = z.tablice.generiraniKod;
        }

        listaKamoStrpatiKod.addAll(kod);
    }
}
