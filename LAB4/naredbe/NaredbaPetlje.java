package naredbe;

import izrazi.Izraz;
import znakovi.Tablice;
import znakovi.Znak;

import java.util.ArrayList;
import java.util.List;

import static util.Util.*;

public class NaredbaPetlje {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<naredba_petlje>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <naredba_petlje>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 5:
                if (!znak.djeca.get(0).ime.equals("KR_WHILE")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + znak.djeca.get(0).ime + " umjesto KR_WHILE");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("L_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + znak.djeca.get(1).ime + " umjesto L_ZAGRADA");
                    System.exit(1);
                }
                Znak izraz = znak.djeca.get(2);
                if (!izraz.ime.equals("<izraz>")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + izraz.ime + " umjesto <izraz>");
                    System.exit(1);
                }
                if (!znak.djeca.get(3).ime.equals("D_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + znak.djeca.get(3).ime + " umjesto D_ZAGRADA");
                    System.exit(1);
                }
                Znak naredba = znak.djeca.get(4);
                if (!naredba.ime.equals("<naredba>")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + naredba.ime + " umjesto <naredba>");
                    System.exit(1);
                }

                long startWhile = Tablice.labelCounter;
                long endWhile = Tablice.labelCounter + 1;
                Tablice.labelCounter += 2;

                strpajKod(znak, List.of(
                        "L_" + String.format("%04X", startWhile)
                ));

                if (!Izraz.obradi(izraz)) {
                    return false;
                }

                strpajKod(znak, List.of(
                        "\t\t\tPOP\t\tR0",
                        "\t\t\tCMP\t\tR0, 0",
                        "\t\t\tJP_EQ\tL_" + String.format("%04X", endWhile)
                ));

                if (!implicitnoKastabilan(izraz.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                if (!Naredba.obradi(naredba)) {
                    return false;
                }

                strpajKod(znak, List.of(
                        "\t\t\tJP\t\tL_" + String.format("%04X", startWhile),
                        "L_" + String.format("%04X", endWhile)
                ));

                break;
            case 6:
                if (!znak.djeca.get(0).ime.equals("KR_FOR")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + znak.djeca.get(0).ime + " umjesto KR_FOR");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("L_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + znak.djeca.get(1).ime + " umjesto L_ZAGRADA");
                    System.exit(1);
                }
                Znak izrazNaredba11 = znak.djeca.get(2);
                if (!izrazNaredba11.ime.equals("<izraz_naredba>")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + izrazNaredba11.ime + " umjesto <izraz_naredba>");
                    System.exit(1);
                }
                Znak izrazNaredba12 = znak.djeca.get(3);
                if (!izrazNaredba12.ime.equals("<izraz_naredba>")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + izrazNaredba12.ime + " umjesto <izraz_naredba>");
                    System.exit(1);
                }
                if (!znak.djeca.get(4).ime.equals("D_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + znak.djeca.get(4).ime + " umjesto D_ZAGRADA");
                    System.exit(1);
                }
                Znak naredba1 = znak.djeca.get(5);
                if (!naredba1.ime.equals("<naredba>")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + naredba1.ime + " umjesto <naredba>");
                    System.exit(1);
                }
                if (!IzrazNaredba.obradi(izrazNaredba11)) {
                    return false;
                }

                long startFor1 = Tablice.labelCounter;
                long endFor1 = Tablice.labelCounter + 1;
                Tablice.labelCounter += 2;

                strpajKod(znak, List.of(
                        "L_" + String.format("%04X", startFor1)
                ));

                if (!IzrazNaredba.obradi(izrazNaredba12)) {
                    return false;
                }
                if (!implicitnoKastabilan(izrazNaredba12.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }

                strpajKod(znak, List.of(
                        "\t\t\tPOP\t\tR0",
                        "\t\t\tCMP\t\tR0, 0",
                        "\t\t\tJP_EQ\tL_" + String.format("%04X", endFor1)
                ));

                if (!Naredba.obradi(naredba1)) {
                    return false;
                }

                strpajKod(znak, List.of(
                        "\t\t\tJP\t\tL_" + String.format("%04X", startFor1),
                        "L_" + String.format("%04X", endFor1)
                ));

                break;
            case 7:
                if (!znak.djeca.get(0).ime.equals("KR_FOR")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + znak.djeca.get(0).ime + " umjesto KR_FOR");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("L_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + znak.djeca.get(1).ime + " umjesto L_ZAGRADA");
                    System.exit(1);
                }
                Znak izrazNaredba21 = znak.djeca.get(2);
                if (!izrazNaredba21.ime.equals("<izraz_naredba>")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + izrazNaredba21.ime + " umjesto <izraz_naredba>");
                    System.exit(1);
                }
                Znak izrazNaredba22 = znak.djeca.get(3);
                if (!izrazNaredba22.ime.equals("<izraz_naredba>")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + izrazNaredba22.ime + " umjesto <izraz_naredba>");
                    System.exit(1);
                }
                Znak izraz2 = znak.djeca.get(4);
                if (!izraz2.ime.equals("<izraz>")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + izraz2.ime + " umjesto <izraz>");
                    System.exit(1);
                }
                if (!znak.djeca.get(5).ime.equals("D_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + znak.djeca.get(5).ime + " umjesto D_ZAGRADA");
                    System.exit(1);
                }
                Znak naredba2 = znak.djeca.get(6);
                if (!naredba2.ime.equals("<naredba>")) {
                    System.err.println("Neispravno dijete cvora <naredba_petlje>: " + naredba2.ime + " umjesto <naredba>");
                    System.exit(1);
                }
                if (!IzrazNaredba.obradi(izrazNaredba21)) {
                    return false;
                }

                long startFor2 = Tablice.labelCounter;
                long endFor2 = Tablice.labelCounter + 1;
                Tablice.labelCounter += 2;

                strpajKod(znak, List.of(
                        "L_" + String.format("%04X", startFor2)
                ));

                if (!IzrazNaredba.obradi(izrazNaredba22)) {
                    return false;
                }
                if (!implicitnoKastabilan(izrazNaredba22.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }

                strpajKod(znak, List.of(
                        "\t\t\tPOP\t\tR0",
                        "\t\t\tCMP\t\tR0, 0",
                        "\t\t\tJP_EQ\tL_" + String.format("%04X", endFor2)
                ));

                int startIndex = znak.tablice.generiraniKod.size();
                if (!Izraz.obradi(izraz2)) {
                    return false;
                }
                List<String> kod = new ArrayList<>(znak.tablice.generiraniKod.subList(startIndex, znak.tablice.generiraniKod.size()));
                List<String> retain = new ArrayList<>(znak.tablice.generiraniKod.subList(0, startIndex));
                znak.tablice.generiraniKod.clear();
                znak.tablice.generiraniKod.addAll(retain);

                if (!Naredba.obradi(naredba2)) {
                    return false;
                }

                strpajKod(znak, kod);
                strpajKod(znak, List.of(
                        "\t\t\tJP\t\tL_" + String.format("%04X", startFor2),
                        "L_" + String.format("%04X", endFor2)
                ));

                break;
            default:
                System.err.println("Neispravan broj djece cvora <naredba_petlje>: " + znak.djeca.size() + " umjesto 5, 6 ili 7");
                System.exit(1);
        }
        return true;
    }
}
