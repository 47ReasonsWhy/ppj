package izrazi;

import znakovi.Deklaracija;
import znakovi.Znak;

import java.util.List;

import static util.Util.*;

public class BinIliIzraz {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<bin_ili_izraz>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <bin_ili_izraz>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak binXiliIzraz = znak.djeca.get(0);
                if (!binXiliIzraz.ime.equals("<bin_xili_izraz>")) {
                    System.err.println("Neispravno dijete cvora <bin_ili_izraz>: " + binXiliIzraz.ime + " umjesto <bin_xili_izraz>");
                    System.exit(1);
                }
                if (!BinXiliIzraz.obradi(binXiliIzraz)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(binXiliIzraz.deklaracija);
                znak.jedinka = binXiliIzraz.jedinka;
                break;
            case 3:
                Znak binIliIzraz = znak.djeca.get(0);
                if (!binIliIzraz.ime.equals("<bin_ili_izraz>")) {
                    System.err.println("Neispravno dijete cvora <bin_ili_izraz>: " + binIliIzraz.ime + " umjesto <bin_ili_izraz>");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("OP_BIN_ILI")) {
                    System.err.println("Neispravno dijete cvora <bin_ili_izraz>: " + znak.djeca.get(1).ime + " umjesto OP_BIN_ILI");
                    System.exit(1);
                }
                Znak binXiliIzraz1 = znak.djeca.get(2);
                if (!binXiliIzraz1.ime.equals("<bin_xili_izraz>")) {
                    System.err.println("Neispravno dijete cvora <bin_ili_izraz>: " + binXiliIzraz1.ime + " umjesto <bin_xili_izraz>");
                    System.exit(1);
                }
                if (!BinIliIzraz.obradi(binIliIzraz)) {
                    return false;
                }
                if (!implicitnoKastabilan(binIliIzraz.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                if (!BinXiliIzraz.obradi(binXiliIzraz1)) {
                    return false;
                }
                if (!implicitnoKastabilan(binXiliIzraz1.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                znak.deklaracija = new Deklaracija("int", false);

                strpajKod(znak, List.of(
                        "\t\t\tPOP\t\tR1",
                        "\t\t\tPOP\t\tR0",
                        "\t\t\tOR\t\tR0, R1, R0",
                        "\t\t\tPUSH\tR0"
                ));

                break;
            default:
                System.err.println("Neispravan broj djece cvora <bin_ili_izraz>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
