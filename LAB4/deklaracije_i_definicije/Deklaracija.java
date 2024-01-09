package deklaracije_i_definicije;

import izrazi.ImeTipa;
import znakovi.Znak;

public class Deklaracija {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<deklaracija>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <deklaracija>");
            System.exit(1);
        }
        if (znak.djeca.size() != 3) {
            System.err.println("Neispravan broj djece cvora <deklaracija>: " + znak.djeca.size() + " umjesto 3");
            System.exit(1);
        }
        Znak imeTipa = znak.djeca.get(0);
        if (!imeTipa.ime.equals("<ime_tipa>")) {
            System.err.println("Neispravno ime djeteta cvora <deklaracija>: " + imeTipa.ime + " umjesto <ime_tipa>");
            System.exit(1);
        }
        Znak listaInitDeklaratora = znak.djeca.get(1);
        if (!listaInitDeklaratora.ime.equals("<lista_init_deklaratora>")) {
            System.err.println("Neispravno ime djeteta cvora <deklaracija>: " + listaInitDeklaratora.ime + " umjesto <lista_init_deklaratora>");
            System.exit(1);
        }
        if (!znak.djeca.get(2).ime.equals("TOCKAZAREZ")) {
            System.err.println("Neispravno ime djeteta cvora <deklaracija>: " + znak.djeca.get(2).ime + " umjesto TOCKAZAREZ");
            System.exit(1);
        }
        if (!ImeTipa.obradi(imeTipa)) {
            return false;
        }
        listaInitDeklaratora.deklaracija = new znakovi.Deklaracija(imeTipa.deklaracija.tip, false);
        return ListaInitDeklaratora.obradi(listaInitDeklaratora);
    }
}
