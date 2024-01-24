package deklaracije_i_definicije;

import izrazi.ImeTipa;
import naredbe.SlozenaNaredba;
import util.Util;
import znakovi.Deklaracija;
import znakovi.Znak;

import java.util.List;

import static util.Util.ispisiGresku;
import static util.Util.strpajKod;

public class DefinicijaFunkcije {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<definicija_funkcije>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <definicija_funkcije>");
            System.exit(1);
        }
        if (znak.djeca.size() != 6) {
            System.err.println("Neispravan broj djece cvora <definicija_funkcije>: " + znak.djeca.size() + " umjesto 6");
            System.exit(1);
        }
        Znak imeTipa = znak.djeca.get(0);
        if (!imeTipa.ime.equals("<ime_tipa>")) {
            System.err.println("Neispravno ime djeteta cvora <definicija_funkcije>: " + imeTipa.ime + " umjesto <ime_tipa>");
            System.exit(1);
        }
        Znak idn = znak.djeca.get(1);
        if (!idn.ime.equals("IDN")) {
            System.err.println("Neispravno ime djeteta cvora <definicija_funkcije>: " + idn.ime+ " umjesto IDN");
            System.exit(1);
        }
        znak.tablice.generiraniKod.addAll(List.of(
                "\nF_" + idn.jedinka.toUpperCase() + " ".repeat(10 - idn.jedinka.length()) + "PUSH\tR5",
                "\t\t\tMOVE\tR7, R5"
        ));
        if (!znak.djeca.get(2).ime.equals("L_ZAGRADA")) {
            System.err.println("Neispravno ime djeteta cvora <definicija_funkcije>: " + znak.djeca.get(2).ime + " umjesto L_ZAGRADA");
            System.exit(1);
        }
        if (!List.of("KR_VOID", "<lista_parametara>").contains(znak.djeca.get(3).ime)) {
            System.err.println("Neispravno ime djeteta cvora <definicija_funkcije>: " + znak.djeca.get(3).ime + " umjesto KR_VOID ili <lista_parametara>");
            System.exit(1);
        }
        if (!znak.djeca.get(4).ime.equals("D_ZAGRADA")) {
            System.err.println("Neispravno ime djeteta cvora <definicija_funkcije>: " + znak.djeca.get(4).ime + " umjesto D_ZAGRADA");
            System.exit(1);
        }
        Znak slozenaNaredba = znak.djeca.get(5);
        if (!slozenaNaredba.ime.equals("<slozena_naredba>")) {
            System.err.println("Neispravno ime djeteta cvora <definicija_funkcije>: " + slozenaNaredba.ime + " umjesto <slozena_naredba>");
            System.exit(1);
        }

        if (!ImeTipa.obradi(imeTipa)) {
            return false;
        }
        if (!List.of("int", "char", "void").contains(imeTipa.deklaracija.tip)) {
            ispisiGresku(znak);
            return false;
        }
        if (znak.tablice.tablicaDefiniranihFunkcija.containsKey(idn.jedinka)) {
            ispisiGresku(znak);
            return false;
        }
        String tip;
        switch (znak.djeca.get(3).ime) {
            case "KR_VOID":
                tip = "funkcija(void -> " + imeTipa.deklaracija.tip + ")";
                if (znak.tablice.tablicaDeklaracija.containsKey(idn.jedinka) &&
                    !znak.tablice.tablicaDeklaracija.get(idn.jedinka).tip.equals(tip)) {
                        ispisiGresku(znak);
                        return false;
                }
                znak.tablice.tablicaDeklaracija.put(idn.jedinka, new Deklaracija(tip, false));
                znak.tablice.tablicaDefiniranihFunkcija.put(idn.jedinka, tip);
                if (!SlozenaNaredba.obradi(slozenaNaredba)) {
                    return false;
                }
                break;
            case "<lista_parametara>":
                znak.tablice.stackOffset.clear();
                Znak listaParametara = znak.djeca.get(3);
                if (!ListaParametara.obradi(listaParametara)) {
                    return false;
                }
                tip = "funkcija(" + listaParametara.deklaracija.tip + " -> " + imeTipa.deklaracija.tip + ")";
                if (znak.tablice.tablicaDeklaracija.containsKey(idn.jedinka) &&
                    !znak.tablice.tablicaDeklaracija.get(idn.jedinka).tip.equals(tip)) {
                        ispisiGresku(znak);
                        return false;
                }
                znak.tablice.tablicaDeklaracija.put(idn.jedinka, new Deklaracija(tip, false));
                znak.tablice.tablicaDefiniranihFunkcija.put(idn.jedinka, tip);

                String[] tipovi = listaParametara.deklaracija.tip.substring(1, listaParametara.deklaracija.tip.length() - 1).split(", ");
                String[] imena = listaParametara.jedinka.substring(1, listaParametara.jedinka.length() - 1).split(", ");
                if (tipovi.length != imena.length) {
                    System.err.println("Ne bi se trebalo nikada dogoditi");
                    System.exit(1);
                }
                for (int i = 0; i < tipovi.length; i++) {
                    slozenaNaredba.tablice.tablicaDeklaracija.put(imena[i], new Deklaracija(tipovi[i], List.of("char", "int").contains(tipovi[i])));
                }
                if (!SlozenaNaredba.obradi(slozenaNaredba)) {
                    return false;
                }
                break;
            default:
                System.err.println("Neispravno ime djeteta cvora <definicija_funkcije>: " + znak.djeca.get(3).ime + " umjesto KR_VOID ili <lista_parametara>");
                System.exit(1);
        }
        znak.tablice.stackOffset.clear();

        if (!znak.tablice.generiraniKod.get(znak.tablice.generiraniKod.size() - 1).equals("\t\t\tRET")) {
            strpajKod(znak, List.of(
                "\t\t\tMOVE\tR5, R7",
                "\t\t\tPOP\t\tR5",
                "\t\t\tRET"
            ));
        }

        return true;
    }
}
