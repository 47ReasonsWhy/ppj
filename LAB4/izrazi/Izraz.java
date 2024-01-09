package izrazi;

import znakovi.Deklaracija;
import znakovi.Znak;

import static util.Util.implicitnoKastabilan;
import static util.Util.ispisiGresku;

public class Izraz {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<izraz>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <izraz>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak izrazPridruzivanja = znak.djeca.get(0);
                if (!izrazPridruzivanja.ime.equals("<izraz_pridruzivanja>")) {
                    System.err.println("Neispravno dijete cvora <izraz>: " + izrazPridruzivanja.ime + " umjesto <izraz_pridruzivanja>");
                    System.exit(1);
                }
                if (!IzrazPridruzivanja.obradi(izrazPridruzivanja)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(izrazPridruzivanja.deklaracija);
                znak.jedinka = izrazPridruzivanja.jedinka;
                break;
            case 3:
                Znak izraz = znak.djeca.get(0);
                if (!izraz.ime.equals("<izraz>")) {
                    System.err.println("Neispravno dijete cvora <izraz>: " + izraz.ime + " umjesto <izraz>");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("ZAREZ")) {
                    System.err.println("Neispravno dijete cvora <izraz>: " + znak.djeca.get(1).ime + " umjesto ZAREZ");
                    System.exit(1);
                }
                Znak izrazPridruzivanja1 = znak.djeca.get(2);
                if (!izrazPridruzivanja1.ime.equals("<izraz_pridruzivanja>")) {
                    System.err.println("Neispravno dijete cvora <izraz>: " + izrazPridruzivanja1.ime + " umjesto <izraz_pridruzivanja>");
                    System.exit(1);
                }
                if (!Izraz.obradi(izraz)) {
                    return false;
                }
                if (!IzrazPridruzivanja.obradi(izrazPridruzivanja1)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(izrazPridruzivanja1.deklaracija.tip, false);
                break;
            default:
                System.err.println("Neispravan broj djece cvora <izraz>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
