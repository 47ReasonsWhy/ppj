package znakovi;

import java.util.LinkedList;

public class Znak {
    public String ime;
    public Zavrsnost zavrsnost;
    public Znak roditelj;
    public LinkedList<Znak> djeca;
    public String linija;
    public String jedinka;
    public Deklaracija deklaracija;
    public Tablice tablice;
    public int br_elem;

    public Znak(String ime, Zavrsnost zavrsnost, Znak roditelj) {
        if (zavrsnost == Zavrsnost.ZAVRSNI) {
            String[] split = ime.split(" ");
            this.ime = split[0];
            this.linija = split[1];
            this.jedinka = ime.substring(this.ime.length() + 1 + this.linija.length() + 1);
        } else {
            this.ime = ime;
        }
        this.zavrsnost = zavrsnost;
        this.roditelj = roditelj;
        this.djeca = new LinkedList<>();
    }

    public void dodajDijete(Znak dijete) {
        this.djeca.add(dijete);
    }
}
