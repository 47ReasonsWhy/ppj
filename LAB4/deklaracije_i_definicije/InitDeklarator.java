package deklaracije_i_definicije;

import znakovi.Deklaracija;
import znakovi.Znak;

import java.util.LinkedList;
import java.util.List;

import static util.Util.*;

public class InitDeklarator {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<init_deklarator>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <init_deklarator>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak izravniDeklarator = znak.djeca.get(0);
                if (!izravniDeklarator.ime.equals("<izravni_deklarator>")) {
                    System.err.println("Neispravno ime djeteta cvora <init_deklarator>: " + izravniDeklarator.ime + " umjesto <izravni_deklarator>");
                    System.exit(1);
                }
                izravniDeklarator.deklaracija = new Deklaracija(znak.deklaracija.tip, false);
                if (!IzravniDeklarator.obradi(izravniDeklarator)) {
                    return false;
                }
                if (izravniDeklarator.deklaracija.tip.startsWith("const(") ||
                        izravniDeklarator.deklaracija.tip.startsWith("niz(const(")) {
                    ispisiGresku(znak);
                    return false;
                }
                break;
            case 3:
                Znak izravniDeklarator2 = znak.djeca.get(0);
                if (!izravniDeklarator2.ime.equals("<izravni_deklarator>")) {
                    System.err.println("Neispravno ime djeteta cvora <init_deklarator>: " + izravniDeklarator2.ime + " umjesto <izravni_deklarator>");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("OP_PRIDRUZI")) {
                    System.err.println("Neispravno ime djeteta cvora <init_deklarator>: " + znak.djeca.get(1).ime + " umjesto OP_PRIDRUZI");
                    System.exit(1);
                }
                Znak inicijalizator = znak.djeca.get(2);
                if (!inicijalizator.ime.equals("<inicijalizator>")) {
                    System.err.println("Neispravno ime djeteta cvora <init_deklarator>: " + inicijalizator.ime + " umjesto <inicijalizator>");
                    System.exit(1);
                }
                izravniDeklarator2.deklaracija = new Deklaracija(znak.deklaracija.tip, false);
                if (!IzravniDeklarator.obradi(izravniDeklarator2)) {
                    return false;
                }
                if (!Inicijalizator.obradi(inicijalizator)) {
                    return false;
                }
                String t;

                // find index of global variable
                Znak trenutni = znak;
                int index;
                while (trenutni != null && trenutni.tablice.tablicaIndeksaVarijabli.get(izravniDeklarator2.jedinka) == null) {
                    trenutni = trenutni.roditelj;
                }
                if (trenutni == null) {
                    System.err.println("Nije pronadjen indeks varijable " + izravniDeklarator2.jedinka);
                    System.exit(1);
                    return false;
                }
                index = trenutni.tablice.tablicaIndeksaVarijabli.get(izravniDeklarator2.jedinka);

                switch (izravniDeklarator2.deklaracija.tip) {
                    case "char":
                    case "int":
                    case "const(char)":
                    case "const(int)":
                        t = izravniDeklarator2.deklaracija.tip.startsWith("const(") ?
                                izravniDeklarator2.deklaracija.tip.substring(6, izravniDeklarator2.deklaracija.tip.length() - 1) :
                                izravniDeklarator2.deklaracija.tip;
                        if (!implicitnoKastabilan(inicijalizator.deklaracija.tip, t)) {
                            ispisiGresku(znak);
                            return false;
                        }

                        strpajKod(znak, List.of(
                                "\t\t\tPOP\t\tR0",
                                "\t\t\tSTORE\tR0, (G_" + String.format("%04X", index) + ")"
                        ));

                        break;
                    case "niz(char)":
                    case "niz(int)":
                    case "niz(const(char))":
                    case "niz(const(int))":
                        if (!inicijalizator.deklaracija.tip.startsWith("[") ||
                                !inicijalizator.deklaracija.tip.endsWith("]")) {
                            ispisiGresku(znak);
                            return false;
                        }
                        if (izravniDeklarator2.deklaracija.tip.startsWith("niz(const(")) {
                            t = izravniDeklarator2.deklaracija.tip.substring(10, izravniDeklarator2.deklaracija.tip.length() - 2);
                        } else {
                            t = izravniDeklarator2.deklaracija.tip.substring(4, izravniDeklarator2.deklaracija.tip.length() - 1);
                        }
                        if (inicijalizator.br_elem > izravniDeklarator2.br_elem) {
                            ispisiGresku(znak);
                            return false;
                        }
                        String[] tipovi = inicijalizator.deklaracija.tip.substring(1, inicijalizator.deklaracija.tip.length() - 1).split(", ");
                        for (int i = 0; i < inicijalizator.br_elem; i++) {
                            if (!implicitnoKastabilan(tipovi[i], t)) {
                                ispisiGresku(znak);
                                return false;
                            }
                        }

                        List<String> kodZaStrpat = new LinkedList<>(List.of(
                                "\t\t\tMOVE\tG_" + String.format("%04X", index) + ", R1"
                        ));
                        int br_elem = inicijalizator.br_elem - 1;
                        for (int i = br_elem; i >= 0; i--) {
                            String hex = String.format("%02X", i*4);
                            kodZaStrpat.add("\t\t\tPOP\t\tR0");
                            kodZaStrpat.add("\t\t\tSTORE\tR0, (R1+" + hex + ")");
                        }
                        strpajKod(znak, kodZaStrpat);

                        break;
                    default:
                        ispisiGresku(znak);
                        return false;
                }
                break;
            default:
                System.err.println("Neispravan broj djece cvora <init_deklarator>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
