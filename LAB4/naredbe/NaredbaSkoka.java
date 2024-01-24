package naredbe;

import izrazi.Izraz;
import util.Util;
import znakovi.Znak;

import java.util.Map;

import static util.Util.implicitnoKastabilan;
import static util.Util.ispisiGresku;

public class NaredbaSkoka {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<naredba_skoka>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <naredba_skoka>");
            System.exit(1);
        }
        boolean ispravno = false;
        Znak trenutni = znak;
        switch (znak.djeca.size()) {
            case 2:
                switch (znak.djeca.get(0).ime) {
                    case "KR_CONTINUE":
                    case "KR_BREAK":
                        if (!znak.djeca.get(1).ime.equals("TOCKAZAREZ")) {
                            System.err.println("Neispravno dijete cvora <naredba_skoka>: " + znak.djeca.get(1).ime + " umjesto TOCKAZAREZ");
                            System.exit(1);
                        }
                        while (trenutni != null) {
                            if (trenutni.ime.equals("<naredba_petlje>")) {
                                ispravno = true;
                                break;
                            }
                            trenutni = trenutni.roditelj;
                        }
                        if (!ispravno) {
                            ispisiGresku(znak);
                            return false;
                        }
                        break;
                    case "KR_RETURN":
                        if (!znak.djeca.get(1).ime.equals("TOCKAZAREZ")) {
                            System.err.println("Neispravno dijete cvora <naredba_skoka>: " + znak.djeca.get(1).ime + " umjesto TOCKAZAREZ");
                            System.exit(1);
                        }
                        while (trenutni != null) {
                            if (trenutni.ime.equals("<definicija_funkcije>")) {
                                if (trenutni.djeca.get(0).ime.equals("<ime_tipa>") &&
                                    trenutni.djeca.get(0).deklaracija.tip.equals("void")) {
                                        ispravno = true;
                                }
                                break;
                            }
                            trenutni = trenutni.roditelj;
                        }
                        if (!ispravno) {
                            ispisiGresku(znak);
                            return false;
                        }
                        znak.tablice.generiraniKod.add("\t\t\tMOVE\tR5, R7");
                        znak.tablice.generiraniKod.add("\t\t\tPOP\t\tR5");
                        znak.tablice.generiraniKod.add("\t\t\tRET");
                        break;
                    default:
                        System.err.println("Neispravno dijete cvora <naredba_skoka>: " + znak.djeca.get(0).ime + " umjesto KR_CONTINUE, KR_BREAK ili KR_RETURN");
                        System.exit(1);
                }
                break;
            case 3:
                if (!znak.djeca.get(0).ime.equals("KR_RETURN")) {
                    System.err.println("Neispravno dijete cvora <naredba_skoka>: " + znak.djeca.get(0).ime + " umjesto KR_RETURN");
                    System.exit(1);
                }
                Znak izraz = znak.djeca.get(1);
                if (!izraz.ime.equals("<izraz>")) {
                    System.err.println("Neispravno dijete cvora <naredba_skoka>: " + izraz.ime + " umjesto <izraz>");
                    System.exit(1);
                }
                if (!znak.djeca.get(2).ime.equals("TOCKAZAREZ")) {
                    System.err.println("Neispravno dijete cvora <naredba_skoka>: " + znak.djeca.get(2).ime + " umjesto TOCKAZAREZ");
                    System.exit(1);
                }
                if (!Izraz.obradi(izraz)) {
                    return false;
                }
                while (trenutni != null) {
                    if (trenutni.ime.equals("<definicija_funkcije>")) {
                        if (trenutni.djeca.get(0).ime.equals("<ime_tipa>") &&
                            implicitnoKastabilan(izraz.deklaracija.tip, trenutni.djeca.get(0).deklaracija.tip)) {
                                ispravno = true;
                        }
                        break;
                    }
                    trenutni = trenutni.roditelj;
                }
                if (!ispravno) {
                    ispisiGresku(znak);
                    return false;
                }

                znak.tablice.generiraniKod.add("\t\t\tPOP\t\tR6");
                znak.tablice.generiraniKod.add("\t\t\tMOVE\tR5, R7");
                znak.tablice.generiraniKod.add("\t\t\tPOP\t\tR5");
                znak.tablice.generiraniKod.add("\t\t\tRET");
                break;
            default:
                System.err.println("Neispravan broj djece cvora <naredba_skoka>: " + znak.djeca.size() + " umjesto 2 ili 3");
                System.exit(1);
        }
        return true;
    }
}
