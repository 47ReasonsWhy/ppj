package izrazi;

import znakovi.Deklaracija;
import znakovi.Tablice;
import znakovi.Znak;

import java.util.List;

import static util.Util.*;

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
                znak.jedinka = logIIzraz.jedinka;
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

                long labelCounter = Tablice.labelCounter++;
                strpajKod(znak, List.of(
                        "\t\t\tPOP\t\tR0",
                        "\t\t\tCMP\t\tR0, %D 0",
                        "\t\t\tJP_NE\tL1_" + String.format("%04X", labelCounter)
                ));

                if (!LogIIzraz.obradi(logIIzraz1)) {
                    return false;
                }
                if (!implicitnoKastabilan(logIIzraz1.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                znak.deklaracija = new Deklaracija("int", false);

                strpajKod(znak, List.of(
                        "\t\t\tPOP\t\tR0",
                        "\t\t\tCMP\t\tR0, %D 0",
                        "\t\t\tJP_NE\tL1_" + String.format("%04X", labelCounter),
                        "\t\t\tMOVE\t%D 0, R0",
                        "\t\t\tJP\t\tL2_" + String.format("%04X", labelCounter),
                        "L1_" + String.format("%04X", labelCounter) + "\t\tMOVE\t%D 1, R0",
                        "L2_" + String.format("%04X", labelCounter) + "\t\tPUSH\tR0"
                ));

                break;
            default:
                System.err.println("Neispravan broj djece cvora <log_ili_izraz>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
