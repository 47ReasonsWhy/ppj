package naredbe;

import deklaracije_i_definicije.DefinicijaFunkcije;
import deklaracije_i_definicije.Deklaracija;
import znakovi.Znak;

public class VanjskaDeklaracija {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<vanjska_deklaracija>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <vanjska_deklaracija>");
            System.exit(1);
        }
        if (znak.djeca.size() != 1) {
            System.err.println("Neispravan broj djece cvora <vanjska_deklaracija>: " + znak.djeca.size() + " umjesto 1");
            System.exit(1);
        }
        switch (znak.djeca.get(0).ime) {
            case "<definicija_funkcije>":
                if (!DefinicijaFunkcije.obradi(znak.djeca.get(0))) {
                    return false;
                }
                break;
            case "<deklaracija>":
                if (!Deklaracija.obradi(znak.djeca.get(0))) {
                    return false;
                }
                break;
            default:
                System.err.println("Neispravno ime djeteta cvora <vanjska_deklaracija>: " + znak.djeca.get(0).ime + " umjesto <definicija_funkcije> ili <deklaracija>");
                System.exit(1);
        }
        return true;
    }
}
