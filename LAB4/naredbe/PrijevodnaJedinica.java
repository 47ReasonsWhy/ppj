package naredbe;

import znakovi.Znak;

public class PrijevodnaJedinica {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<prijevodna_jedinica>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <prijevodna_jedinica>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak vanjskaDeklaracija = znak.djeca.get(0);
                if (!vanjskaDeklaracija.ime.equals("<vanjska_deklaracija>")) {
                    System.err.println("Pokrenuta obrada pogresnog cvora: " + vanjskaDeklaracija.ime + " umjesto <vanjska_deklaracija>");
                    System.exit(1);
                }
                if (!VanjskaDeklaracija.obradi(vanjskaDeklaracija)) {
                    return false;
                }
                break;
            case 2:
                Znak prijevodnaJedinica = znak.djeca.get(0);
                if (!prijevodnaJedinica.ime.equals("<prijevodna_jedinica>")) {
                    System.err.println("Pokrenuta obrada pogresnog cvora: " + prijevodnaJedinica.ime + " umjesto <prijevodna_jedinica>");
                    System.exit(1);
                }
                Znak vanjskaDeklaracija1 = znak.djeca.get(1);
                if (!vanjskaDeklaracija1.ime.equals("<vanjska_deklaracija>")) {
                    System.err.println("Pokrenuta obrada pogresnog cvora: " + vanjskaDeklaracija1.ime + " umjesto <vanjska_deklaracija>");
                    System.exit(1);
                }
                if (!PrijevodnaJedinica.obradi(prijevodnaJedinica)) {
                    return false;
                }
                if (!VanjskaDeklaracija.obradi(vanjskaDeklaracija1)) {
                    return false;
                }
                break;
            default:
                System.err.println("Neispravan broj djece cvora <prijevodna_jedinica>: " + znak.djeca.size() + " umjesto 1 ili 2");
                System.exit(1);
        }
        return true;
    }
}
