import java.util.*;
import java.util.stream.Collectors;

public class Automat {
    public Integer br_stanja = 1;
    public Integer pocetno_stanje = 0;
    public Map<Integer, List<String>> prihvatljiva_stanja = new TreeMap<>();
    public Map<Integer, Map<Character, TreeSet<Integer>>> prijelazi = new TreeMap<>();

    public Map<Integer, TreeSet<Integer>> epsilon_prijelazi = new TreeMap<>();

    public Integer novo_stanje() {
        return br_stanja++;
    }

    public void dodaj_prijelaz(Integer lijevo_stanje, Integer desno_stanje, Character znak) {
        if (!prijelazi.containsKey(lijevo_stanje)) {
            prijelazi.put(lijevo_stanje, new TreeMap<>());
        }
        if (!prijelazi.get(lijevo_stanje).containsKey(znak)) {
            prijelazi.get(lijevo_stanje).put(znak, new TreeSet<>());
        }
        prijelazi.get(lijevo_stanje).get(znak).add(desno_stanje);
    }

    public void dodaj_epsilon_prijelaz(Integer lijevo_stanje, Integer desno_stanje) {
        if (!epsilon_prijelazi.containsKey(lijevo_stanje)) {
            epsilon_prijelazi.put(lijevo_stanje, new TreeSet<>());
        }
        epsilon_prijelazi.get(lijevo_stanje).add(desno_stanje);
    }

    public String epsilonPrijelaziString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Integer stanje : epsilon_prijelazi.keySet()) {
            sb.append(stanje).append("=[");
            sb.append(epsilon_prijelazi.get(stanje).stream().map(String::valueOf).collect(Collectors.joining(",")));
            sb.append("], ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("}");
        return sb.toString();
    }

    public String prijelaziString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Integer stanje : prijelazi.keySet()) {
            sb.append(stanje).append("={");
            for (Character znak : prijelazi.get(stanje).keySet()) {
                sb.append(znak).append("->[");
                sb.append(prijelazi.get(stanje).get(znak).stream().map(String::valueOf).collect(Collectors.joining(",")));
                sb.append("],");
            }
            sb.delete(sb.length() - 1, sb.length());
            sb.append("}, ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("}");
        return sb.toString();
    }

    public String prihvatljivaStanjaString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Integer stanje : prihvatljiva_stanja.keySet()) {
            sb.append(stanje).append("=[");
            sb.append(String.join("; ", prihvatljiva_stanja.get(stanje)));
            sb.append("], ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return  "br_stanja: " + br_stanja + "\n" +
                "pocetno_stanje: " + pocetno_stanje + "\n" +
                "prihvatljiva_stanja: " + prihvatljivaStanjaString() + "\n" +
                "prijelazi: " + prijelaziString() + "\n" +
                "epsilon_prijelazi: " + epsilonPrijelaziString();
    }
}
