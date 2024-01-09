package izrazi;

import util.Util;
import znakovi.Deklaracija;
import znakovi.Znak;

import java.util.List;
import java.util.Map;

import static util.Util.*;

public class AditivniIzraz {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<aditivni_izraz>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <aditivni_izraz>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak multiplikativniIzraz = znak.djeca.get(0);
                if (!multiplikativniIzraz.ime.equals("<multiplikativni_izraz>")) {
                    System.err.println("Neispravno dijete cvora <aditivni_izraz>: " + multiplikativniIzraz.ime + " umjesto <multiplikativni_izraz>");
                    System.exit(1);
                }
                if (!MultiplikativniIzraz.obradi(multiplikativniIzraz)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(multiplikativniIzraz.deklaracija);
                znak.jedinka = multiplikativniIzraz.jedinka;
                break;
            case 3:
                Znak aditivniIzraz = znak.djeca.get(0);
                if (!aditivniIzraz.ime.equals("<aditivni_izraz>")) {
                    System.err.println("Neispravno dijete cvora <aditivni_izraz>: " + aditivniIzraz.ime + " umjesto <aditivni_izraz>");
                    System.exit(1);
                }
                if (!List.of("PLUS", "MINUS").contains(znak.djeca.get(1).ime)) {
                    System.err.println("Neispravno dijete cvora <aditivni_izraz>: " + znak.djeca.get(1).ime + " umjesto PLUS ili MINUS");
                    System.exit(1);
                }
                Znak multiplikativniIzraz1 = znak.djeca.get(2);
                if (!multiplikativniIzraz1.ime.equals("<multiplikativni_izraz>")) {
                    System.err.println("Neispravno dijete cvora <aditivni_izraz>: " + multiplikativniIzraz1.ime + " umjesto <multiplikativni_izraz>");
                    System.exit(1);
                }
                if (!AditivniIzraz.obradi(aditivniIzraz)) {
                    return false;
                }
                if (!implicitnoKastabilan(aditivniIzraz.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                if (!MultiplikativniIzraz.obradi(multiplikativniIzraz1)) {
                    return false;
                }
                if (!implicitnoKastabilan(multiplikativniIzraz1.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                znak.deklaracija = new Deklaracija("int", false);

                strpajKod(znak, List.of(
                        "\t\t\tPOP\t\tR1",
                        "\t\t\tPOP\t\tR0",
                        "\t\t\t" + (znak.djeca.get(1).ime.equals("PLUS") ? "ADD" : "SUB") + "\t\tR0, R1, R0",
                        "\t\t\tPUSH\tR0"
                ));

                break;
            default:
                System.err.println("Neispravan broj djece cvora <aditivni_izraz>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
