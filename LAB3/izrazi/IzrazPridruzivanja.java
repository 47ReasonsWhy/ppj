package izrazi;

import znakovi.Deklaracija;
import znakovi.Znak;

import static util.Util.implicitnoKastabilan;
import static util.Util.ispisiGresku;

public class IzrazPridruzivanja {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<izraz_pridruzivanja>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <izraz_pridruzivanja>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak logIliIzraz = znak.djeca.get(0);
                if (!logIliIzraz.ime.equals("<log_ili_izraz>")) {
                    System.err.println("Neispravno dijete cvora <izraz_pridruzivanja>: " + logIliIzraz.ime + " umjesto <log_ili_izraz>");
                    System.exit(1);
                }
                if (!LogIliIzraz.obradi(logIliIzraz)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(logIliIzraz.deklaracija);
                break;
            case 3:
                Znak postfiksIzraz = znak.djeca.get(0);
                if (!postfiksIzraz.ime.equals("<postfiks_izraz>")) {
                    System.err.println("Neispravno dijete cvora <izraz_pridruzivanja>: " + postfiksIzraz.ime + " umjesto <postfiks_izraz>");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("OP_PRIDRUZI")) {
                    System.err.println("Neispravno dijete cvora <izraz_pridruzivanja>: " + znak.djeca.get(1).ime + " umjesto OP_PRIDRUZI");
                    System.exit(1);
                }
                Znak izrazPridruzivanja = znak.djeca.get(2);
                if (!izrazPridruzivanja.ime.equals("<izraz_pridruzivanja>")) {
                    System.err.println("Neispravno dijete cvora <izraz_pridruzivanja>: " + izrazPridruzivanja.ime + " umjesto <izraz_pridruzivanja>");
                    System.exit(1);
                }
                if (!PostfiksIzraz.obradi(postfiksIzraz)) {
                    return false;
                }
                if (!postfiksIzraz.deklaracija.l_izraz) {
                    ispisiGresku(znak);
                    return false;
                }
                if (!IzrazPridruzivanja.obradi(izrazPridruzivanja)) {
                    return false;
                }
                if (!implicitnoKastabilan(izrazPridruzivanja.deklaracija.tip, postfiksIzraz.deklaracija.tip)) {
                    ispisiGresku(znak);
                    return false;
                }
                znak.deklaracija = new Deklaracija(postfiksIzraz.deklaracija.tip, false);
                break;
            default:
                System.err.println("Neispravan broj djece cvora <izraz_pridruzivanja>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
