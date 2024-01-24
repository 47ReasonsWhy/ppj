package izrazi;

import znakovi.Deklaracija;
import znakovi.Tablice;
import znakovi.Znak;

import java.util.List;

import static util.Util.*;

public class MultiplikativniIzraz {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<multiplikativni_izraz>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <multiplikativni_izraz>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak castIzraz = znak.djeca.get(0);
                if (!castIzraz.ime.equals("<cast_izraz>")) {
                    System.err.println("Neispravno dijete cvora <multiplikativni_izraz>: " + castIzraz.ime + " umjesto <cast_izraz>");
                    System.exit(1);
                }
                if (!CastIzraz.obradi(castIzraz)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(castIzraz.deklaracija);
                znak.jedinka = castIzraz.jedinka;
                break;
            case 3:
                Znak multiplikativniIzraz = znak.djeca.get(0);
                if (!multiplikativniIzraz.ime.equals("<multiplikativni_izraz>")) {
                    System.err.println("Neispravno dijete cvora <multiplikativni_izraz>: " + multiplikativniIzraz.ime + " umjesto <multiplikativni_izraz>");
                    System.exit(1);
                }
                if (!List.of("OP_PUTA", "OP_DIJELI", "OP_MOD").contains(znak.djeca.get(1).ime)) {
                    System.err.println("Neispravno dijete cvora <multiplikativni_izraz>: " + znak.djeca.get(1).ime + " umjesto OP_PUTA, OP_DIJELI ili OP_MOD");
                    System.exit(1);
                }
                Znak castIzraz1 = znak.djeca.get(2);
                if (!castIzraz1.ime.equals("<cast_izraz>")) {
                    System.err.println("Neispravno dijete cvora <multiplikativni_izraz>: " + castIzraz1.ime + " umjesto <cast_izraz>");
                    System.exit(1);
                }
                if (!MultiplikativniIzraz.obradi(multiplikativniIzraz)) {
                    return false;
                }
                if (!implicitnoKastabilan(multiplikativniIzraz.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                if (!CastIzraz.obradi(castIzraz1)) {
                    return false;
                }
                if (!implicitnoKastabilan(castIzraz1.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                znak.deklaracija = new Deklaracija("int", false);

                switch (znak.djeca.get(1).ime) {
                    case "OP_PUTA":
                        strpajKod(znak, List.of(
                                "\t\t\tCALL\tMUL",
                                "\t\t\tADD\t\tR7, 8, R7",
                                "\t\t\tPUSH\tR6"
                        ));
                        Tablice.usingMUL = true;
                        break;
                    case "OP_DIJELI":
                        strpajKod(znak, List.of(
                                "\t\t\tCALL\tDIV",
                                "\t\t\tADD\t\tR7, 8, R7",
                                "\t\t\tPUSH\tR6"
                        ));
                        Tablice.usingDIV = true;
                        break;
                    case "OP_MOD":
                        strpajKod(znak, List.of(
                                "\t\t\tCALL\tMOD",
                                "\t\t\tADD\t\tR7, 8, R7",
                                "\t\t\tPUSH\tR6"
                        ));
                        Tablice.usingMOD = true;
                        break;
                }

                break;
            default:
                System.err.println("Neispravan broj djece cvora <multiplikativni_izraz>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
