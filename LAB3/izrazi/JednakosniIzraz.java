package izrazi;

import znakovi.Deklaracija;
import znakovi.Znak;

import java.util.List;

import static util.Util.implicitnoKastabilan;
import static util.Util.ispisiGresku;

public class JednakosniIzraz {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<jednakosni_izraz>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <jednakosni_izraz>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak odnosniIzraz = znak.djeca.get(0);
                if (!odnosniIzraz.ime.equals("<odnosni_izraz>")) {
                    System.err.println("Neispravno dijete cvora <jednakosni_izraz>: " + odnosniIzraz.ime + " umjesto <odnosni_izraz>");
                    System.exit(1);
                }
                if (!OdnosniIzraz.obradi(odnosniIzraz)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(odnosniIzraz.deklaracija);
                break;
            case 3:
                Znak jednakosniIzraz = znak.djeca.get(0);
                if (!jednakosniIzraz.ime.equals("<jednakosni_izraz>")) {
                    System.err.println("Neispravno dijete cvora <jednakosni_izraz>: " + jednakosniIzraz.ime + " umjesto <jednakosni_izraz>");
                    System.exit(1);
                }
                if (!List.of("OP_EQ", "OP_NEQ").contains(znak.djeca.get(1).ime)) {
                    System.err.println("Neispravno dijete cvora <jednakosni_izraz>: " + znak.djeca.get(1).ime + " umjesto OP_EQ ili OP_NEQ");
                    System.exit(1);
                }
                Znak odnosniIzraz1 = znak.djeca.get(2);
                if (!odnosniIzraz1.ime.equals("<odnosni_izraz>")) {
                    System.err.println("Neispravno dijete cvora <jednakosni_izraz>: " + odnosniIzraz1.ime + " umjesto <odnosni_izraz>");
                    System.exit(1);
                }
                if (!JednakosniIzraz.obradi(jednakosniIzraz)) {
                    return false;
                }
                if (!implicitnoKastabilan(jednakosniIzraz.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                if (!OdnosniIzraz.obradi(odnosniIzraz1)) {
                    return false;
                }
                if (!implicitnoKastabilan(odnosniIzraz1.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                znak.deklaracija = new Deklaracija("int", false);
                break;
            default:
                System.err.println("Neispravan broj djece cvora <jednakosni_izraz>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
