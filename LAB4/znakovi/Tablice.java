package znakovi;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Tablice {
    public Map<String, Deklaracija> tablicaDeklaracija;
    public Map<String, Integer> tablicaIndeksaVarijabli;
    public Map<String, String> tablicaDefiniranihFunkcija;

    public Tablice tablicaUgnjezdjujucegBloka;
    public List<Tablice> tabliceUgnjezdjenihBlokova;

    public List<String> kodGlobalnihInicijalizacija;
    public List<String> generiraniKod;
    public List<String> kodGlobalnihDeklaracija;

    public Map<String, Integer> stackOffset;

    public static Long varCounter;
    public static Long labelCounter;

    public static Boolean usingMUL;
    public static Boolean usingDIV;
    public static Boolean usingMOD;

    public Tablice(Map<String, Deklaracija> tablicaDeklaracija,
                   Map<String, Integer> tablicaIndeksaVarijabli,
                   Map<String, String> tablicaDefiniranihFunkcija,
                   Tablice tablicaUgnjezdjujucegBloka) {
        this.tablicaDeklaracija = tablicaDeklaracija;
        this.tablicaIndeksaVarijabli = tablicaIndeksaVarijabli;
        this.tablicaDefiniranihFunkcija = tablicaDefiniranihFunkcija;
        this.tablicaUgnjezdjujucegBloka = tablicaUgnjezdjujucegBloka;
        this.tabliceUgnjezdjenihBlokova = new LinkedList<>();
        if (tablicaUgnjezdjujucegBloka != null) {
            tablicaUgnjezdjujucegBloka.tabliceUgnjezdjenihBlokova.add(this);
        }

        if (tablicaUgnjezdjujucegBloka == null) {
            this.kodGlobalnihInicijalizacija = new LinkedList<>();
            this.generiraniKod = new LinkedList<>();
            this.kodGlobalnihDeklaracija = new LinkedList<>();
            this.stackOffset = new HashMap<>();
            varCounter = 0L;
            labelCounter = 0L;
            this.usingMUL = false;
            this.usingDIV = false;
            this.usingMOD = false;
        } else {
            this.kodGlobalnihInicijalizacija = tablicaUgnjezdjujucegBloka.kodGlobalnihInicijalizacija;
            this.generiraniKod = tablicaUgnjezdjujucegBloka.generiraniKod;
            this.kodGlobalnihDeklaracija = tablicaUgnjezdjujucegBloka.kodGlobalnihDeklaracija;
            this.stackOffset = tablicaUgnjezdjujucegBloka.stackOffset;
            this.usingMUL = tablicaUgnjezdjujucegBloka.usingMUL;
            this.usingDIV = tablicaUgnjezdjujucegBloka.usingDIV;
            this.usingMOD = tablicaUgnjezdjujucegBloka.usingMOD;
        }
    }
}
