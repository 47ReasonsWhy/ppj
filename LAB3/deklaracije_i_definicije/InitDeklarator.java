package deklaracije_i_definicije;

import znakovi.Deklaracija;
import znakovi.Znak;

import static util.Util.implicitnoKastabilan;
import static util.Util.ispisiGresku;

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
                        break;
                    case "niz(char)":
                    case "niz(int)":
                    case "niz(const(char))":
                    case "niz(const(int))":
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
