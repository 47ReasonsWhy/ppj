package izrazi;

import znakovi.Deklaracija;
import znakovi.Znak;

import static util.Util.implicitnoKastabilan;
import static util.Util.ispisiGresku;

public class LogIIzraz {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<log_i_izraz>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <log_i_izraz>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak binIliIzraz = znak.djeca.get(0);
                if (!binIliIzraz.ime.equals("<bin_ili_izraz>")) {
                    System.err.println("Neispravno dijete cvora <log_i_izraz>: " + binIliIzraz.ime + " umjesto <bin_ili_izraz>");
                    System.exit(1);
                }
                if (!BinIliIzraz.obradi(binIliIzraz)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(binIliIzraz.deklaracija);
                break;
            case 3:
                Znak logIIzraz = znak.djeca.get(0);
                if (!logIIzraz.ime.equals("<log_i_izraz>")) {
                    System.err.println("Neispravno dijete cvora <log_i_izraz>: " + logIIzraz.ime + " umjesto <log_i_izraz>");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("OP_I")) {
                    System.err.println("Neispravno dijete cvora <log_i_izraz>: " + znak.djeca.get(1).ime + " umjesto OP_I");
                    System.exit(1);
                }
                Znak binIliIzraz1 = znak.djeca.get(2);
                if (!binIliIzraz1.ime.equals("<bin_ili_izraz>")) {
                    System.err.println("Neispravno dijete cvora <log_i_izraz>: " + binIliIzraz1.ime + " umjesto <bin_ili_izraz>");
                    System.exit(1);
                }
                if (!LogIIzraz.obradi(logIIzraz)) {
                    return false;
                }
                if (!implicitnoKastabilan(logIIzraz.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                if (!BinIliIzraz.obradi(binIliIzraz1)) {
                    return false;
                }
                if (!implicitnoKastabilan(binIliIzraz1.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                znak.deklaracija = new Deklaracija("int", false);
                break;
            default:
                System.err.println("Neispravan broj djece cvora <log_i_izraz>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
