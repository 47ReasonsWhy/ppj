package izrazi;

import znakovi.Deklaracija;
import znakovi.Znak;

import static util.Util.implicitnoKastabilan;
import static util.Util.ispisiGresku;

public class LogIliIzraz {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<log_ili_izraz>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <log_ili_izraz>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak logIIzraz = znak.djeca.get(0);
                if (!logIIzraz.ime.equals("<log_i_izraz>")) {
                    System.err.println("Neispravno dijete cvora <log_ili_izraz>: " + logIIzraz.ime + " umjesto <log_i_izraz>");
                    System.exit(1);
                }
                if (!LogIIzraz.obradi(logIIzraz)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(logIIzraz.deklaracija);
                break;
            case 3:
                Znak logIliIzraz = znak.djeca.get(0);
                if (!logIliIzraz.ime.equals("<log_ili_izraz>")) {
                    System.err.println("Neispravno dijete cvora <log_ili_izraz>: " + logIliIzraz.ime + " umjesto <log_ili_izraz>");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("OP_ILI")) {
                    System.err.println("Neispravno dijete cvora <log_ili_izraz>: " + znak.djeca.get(1).ime + " umjesto OP_ILI");
                    System.exit(1);
                }
                Znak logIIzraz1 = znak.djeca.get(2);
                if (!logIIzraz1.ime.equals("<log_i_izraz>")) {
                    System.err.println("Neispravno dijete cvora <log_ili_izraz>: " + logIIzraz1.ime + " umjesto <log_i_izraz>");
                    System.exit(1);
                }
                if (!LogIliIzraz.obradi(logIliIzraz)) {
                    return false;
                }
                if (!implicitnoKastabilan(logIliIzraz.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                if (!LogIIzraz.obradi(logIIzraz1)) {
                    return false;
                }
                if (!implicitnoKastabilan(logIIzraz1.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                znak.deklaracija = new Deklaracija("int", false);
                break;
            default:
                System.err.println("Neispravan broj djece cvora <log_ili_izraz>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
