package znakovi;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Tablice {
    public Map<String, Deklaracija> tablicaDeklaracija;
    public Map<String, String> tablicaDefiniranihFunkcija;
    public Tablice tablicaUgnjezdjujucegBloka;
    public List<Tablice> tabliceUgnjezdjenihBlokova;

    public Tablice(Map<String, Deklaracija> tablicaDeklaracija, Map<String, String> tablicaDefiniranihFunkcija, Tablice tablicaUgnjezdjujucegBloka) {
        this.tablicaDeklaracija = tablicaDeklaracija;
        this.tablicaDefiniranihFunkcija = tablicaDefiniranihFunkcija;
        this.tablicaUgnjezdjujucegBloka = tablicaUgnjezdjujucegBloka;
        this.tabliceUgnjezdjenihBlokova = new LinkedList<>();
        if (tablicaUgnjezdjujucegBloka != null) {
            tablicaUgnjezdjujucegBloka.tabliceUgnjezdjenihBlokova.add(this);
        }
    }
}
