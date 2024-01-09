package izrazi;

import znakovi.Deklaracija;
import znakovi.Tablice;
import znakovi.Znak;

import java.util.LinkedList;
import java.util.List;

import static util.Util.*;

public class UnarniIzraz {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<unarni_izraz>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <unarni_izraz>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak postfiksIzraz = znak.djeca.get(0);
                if (!postfiksIzraz.ime.equals("<postfiks_izraz>")) {
                    System.err.println("Neispravno dijete cvora <unarni_izraz>: " + postfiksIzraz.ime + " umjesto <postfiks_izraz>");
                    System.exit(1);
                }
                if (!PostfiksIzraz.obradi(postfiksIzraz)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(postfiksIzraz.deklaracija);
                znak.jedinka = postfiksIzraz.jedinka;
                break;
            case 2:
                switch (znak.djeca.get(1).ime) {
                    case "<unarni_izraz>":
                        if (!List.of("OP_INC", "OP_DEC").contains(znak.djeca.get(0).ime)) {
                            System.err.println("Neispravno dijete cvora <unarni_izraz>: " + znak.djeca.get(0).ime + " umjesto OP_INC ili OP_DEC");
                            System.exit(1);
                        }
                        Znak unarniIzraz = znak.djeca.get(1);
                        if (!UnarniIzraz.obradi(unarniIzraz)) {
                            return false;
                        }
                        Deklaracija deklaracija = unarniIzraz.deklaracija;
                        if (!deklaracija.l_izraz || !implicitnoKastabilan(deklaracija.tip, "int")) {
                            ispisiGresku(znak);
                            return false;
                        }
                        znak.deklaracija = new Deklaracija("int", false);
                        break;
                    case "<cast_izraz>":
                        if (!znak.djeca.get(0).ime.equals("<unarni_operator>")) {
                            System.err.println("Neispravno dijete cvora <unarni_izraz>: " + znak.djeca.get(0).ime + " umjesto <unarni_operator>");
                            System.exit(1);
                        }
                        Znak castIzraz = znak.djeca.get(1);
                        if (!CastIzraz.obradi(castIzraz)) {
                            return false;
                        }
                        Deklaracija deklaracija1 = castIzraz.deklaracija;
                        if (!implicitnoKastabilan(deklaracija1.tip, "int")) {
                            ispisiGresku(znak);
                            return false;
                        }
                        znak.deklaracija = new Deklaracija("int", false);

                        List<String> op = new LinkedList<>(List.of("\t\t\tPOP\tR0"));
                        switch (znak.djeca.get(0).djeca.get(0).ime) {
                            case "PLUS":
                                break;
                            case "MINUS":
                                op.addAll(List.of(
                                        "\t\t\tXOR\t\tR0, %D -1, R0",
                                        "\t\t\tADD\t\tR0, %D 1, R0"
                                ));
                                break;
                            case "OP_TILDA":
                                op.add(
                                        "\t\t\tXOR\t\tR0, %D -1, R0"
                                );
                                break;
                            case "OP_NEG":
                                int labelIndex = Tablice.labelCounter.intValue();
                                op.addAll(List.of(
                                        "\t\t\tCMP\t\tR0, 0",
                                        "\t\t\tJP_Z\t\tNEG_" + String.format("%04X", labelIndex),
                                        "\t\t\tMOVE\t%D 0, R0",
                                        "\t\t\tJP\t\tNEG_" + String.format("%04X", labelIndex + 1),
                                        "NEG_" + String.format("%04X", labelIndex) + "\t\tMOVE\t%D 1, R0",
                                        "NEG_" + String.format("%04X", labelIndex + 1)
                                ));
                                Tablice.labelCounter += 2;
                                break;
                            default:
                                System.err.println("Neispravno dijete cvora <unarni_operator>: " + znak.djeca.get(0).djeca.get(0).ime + " umjesto PLUS, MINUS, OP_TILDA ili OP_NEG");
                                System.exit(1);
                                return false;
                        }
                        strpajKod(znak, op);

                        break;
                    default:
                        System.err.println("Neispravno dijete cvora <unarni_izraz>: " + znak.djeca.get(1).ime + " umjesto <unarni_izraz> ili <cast_izraz>");
                        System.exit(1);
                }
                break;
            default:
                System.err.println("Neispravan broj djece cvora <unarni_izraz>: " + znak.djeca.size() + " umjesto 1 ili 2");
                System.exit(1);
        }
        return true;
    }
}
