package izrazi;

import znakovi.Deklaracija;
import znakovi.Znak;

import static util.Util.implicitnoKastabilan;
import static util.Util.ispisiGresku;

public class BinIIzraz {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<bin_i_izraz>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <bin_i_izraz>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak jednakosniIzraz = znak.djeca.get(0);
                if (!jednakosniIzraz.ime.equals("<jednakosni_izraz>")) {
                    System.err.println("Neispravno dijete cvora <bin_i_izraz>: " + jednakosniIzraz.ime + " umjesto <jednakosni_izraz>");
                    System.exit(1);
                }
                if (!JednakosniIzraz.obradi(jednakosniIzraz)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(jednakosniIzraz.deklaracija);
                break;
            case 3:
                Znak binIIzraz = znak.djeca.get(0);
                if (!binIIzraz.ime.equals("<bin_i_izraz>")) {
                    System.err.println("Neispravno dijete cvora <bin_i_izraz>: " + binIIzraz.ime + " umjesto <bin_i_izraz>");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("OP_BIN_I")) {
                    System.err.println("Neispravno dijete cvora <bin_i_izraz>: " + znak.djeca.get(1).ime + " umjesto OP_BIN_I");
                    System.exit(1);
                }
                Znak jednakosniIzraz1 = znak.djeca.get(2);
                if (!jednakosniIzraz1.ime.equals("<jednakosni_izraz>")) {
                    System.err.println("Neispravno dijete cvora <bin_i_izraz>: " + jednakosniIzraz1.ime + " umjesto <jednakosni_izraz>");
                    System.exit(1);
                }
                if (!BinIIzraz.obradi(binIIzraz)) {
                    return false;
                }
                if (!implicitnoKastabilan(binIIzraz.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                if (!JednakosniIzraz.obradi(jednakosniIzraz1)) {
                    return false;
                }
                if (!implicitnoKastabilan(jednakosniIzraz1.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                znak.deklaracija = new Deklaracija("int", false);
                break;
            default:
                System.err.println("Neispravan broj djece cvora <bin_i_izraz>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
