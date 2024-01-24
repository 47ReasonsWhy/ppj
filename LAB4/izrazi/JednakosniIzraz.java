package izrazi;

import znakovi.Deklaracija;
import znakovi.Tablice;
import znakovi.Znak;

import java.util.List;

import static util.Util.*;

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
                znak.jedinka = odnosniIzraz.jedinka;
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

                int index = List.of("OP_EQ", "OP_NEQ").indexOf(znak.djeca.get(1).ime);
                strpajKod(znak, List.of(
                        "\t\t\tPOP\t\tR1",
                        "\t\t\tPOP\t\tR0",
                        "\t\t\tCMP\t\tR0, R1",
                        "\t\t\tJP_" + List.of("EQ", "NE").get(index) + "\tL_" + String.format("%04X", Tablice.labelCounter),
                        "\t\t\tMOVE\t%D 0, R0",
                        "\t\t\tJP\t\tL_" + String.format("%04X", Tablice.labelCounter) + "_END",
                        "L_" + String.format("%04X", Tablice.labelCounter) + "\t\tMOVE\t%D 1, R0",
                        "L_" + String.format("%04X", Tablice.labelCounter++) + "_END\tPUSH\tR0"
                ));

                break;
            default:
                System.err.println("Neispravan broj djece cvora <jednakosni_izraz>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
