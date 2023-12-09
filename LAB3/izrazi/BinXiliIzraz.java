package izrazi;

import znakovi.Deklaracija;
import znakovi.Znak;

import static util.Util.implicitnoKastabilan;
import static util.Util.ispisiGresku;

public class BinXiliIzraz {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<bin_xili_izraz>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <bin_xili_izraz>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak binIIzraz = znak.djeca.get(0);
                if (!binIIzraz.ime.equals("<bin_i_izraz>")) {
                    System.err.println("Neispravno dijete cvora <bin_xili_izraz>: " + binIIzraz.ime + " umjesto <bin_i_izraz>");
                    System.exit(1);
                }
                if (!BinIIzraz.obradi(binIIzraz)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(binIIzraz.deklaracija);
                break;
            case 3:
                Znak binXiliIzraz = znak.djeca.get(0);
                if (!binXiliIzraz.ime.equals("<bin_xili_izraz>")) {
                    System.err.println("Neispravno dijete cvora <bin_xili_izraz>: " + binXiliIzraz.ime + " umjesto <bin_xili_izraz>");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("OP_BIN_XILI")) {
                    System.err.println("Neispravno dijete cvora <bin_xili_izraz>: " + znak.djeca.get(1).ime + " umjesto OP_BIN_XILI");
                    System.exit(1);
                }
                Znak binIIzraz1 = znak.djeca.get(2);
                if (!binIIzraz1.ime.equals("<bin_i_izraz>")) {
                    System.err.println("Neispravno dijete cvora <bin_xili_izraz>: " + binIIzraz1.ime + " umjesto <bin_i_izraz>");
                    System.exit(1);
                }
                if (!BinXiliIzraz.obradi(binXiliIzraz)) {
                    return false;
                }
                if (!implicitnoKastabilan(binXiliIzraz.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                if (!BinIIzraz.obradi(binIIzraz1)) {
                    return false;
                }
                if (!implicitnoKastabilan(binIIzraz1.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                znak.deklaracija = new Deklaracija("int", false);
                break;
            default:
                System.err.println("Neispravan broj djece cvora <bin_xili_izraz>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
