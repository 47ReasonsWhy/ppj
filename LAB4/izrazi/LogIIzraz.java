package izrazi;

import znakovi.Deklaracija;
import znakovi.Tablice;
import znakovi.Znak;

import java.util.List;

import static util.Util.*;

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
                znak.jedinka = binIliIzraz.jedinka;
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

                strpajKod(znak, List.of(
                        "\t\t\tPOP\t\tR1",
                        "\t\t\tPOP\t\tR0",
                        "\t\t\tCMP\t\tR0, %D 0",
                        "\t\t\tJP_EQ\t" + "L_" + String.format("%04X", Tablice.labelCounter),
                        "\t\t\tCMP\t\tR1, %D 0",
                        "\t\t\tJP_EQ\t" + "L_" + String.format("%04X", Tablice.labelCounter),
                        "\t\t\tMOVE\t%D 1, R0",
                        "\t\t\tJP\t\t" + "L_" + String.format("%04X", Tablice.labelCounter) + "_END",
                        "L_" + String.format("%04X", Tablice.labelCounter) + "\t\tMOVE\t%D 0, R0",
                        "L_" + String.format("%04X", Tablice.labelCounter++) + "_END\t\tPUSH\tR0"
                ));

                break;
            default:
                System.err.println("Neispravan broj djece cvora <log_i_izraz>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
