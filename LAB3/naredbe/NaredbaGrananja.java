package naredbe;

import izrazi.Izraz;
import znakovi.Znak;

import static util.Util.implicitnoKastabilan;
import static util.Util.ispisiGresku;

public class NaredbaGrananja {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<naredba_grananja>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <naredba_grananja>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 5:
                if (!znak.djeca.get(0).ime.equals("KR_IF")) {
                    System.err.println("Neispravno dijete cvora <naredba_grananja>: " + znak.djeca.get(0).ime + " umjesto KR_IF");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("L_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <naredba_grananja>: " + znak.djeca.get(1).ime + " umjesto L_ZAGRADA");
                    System.exit(1);
                }
                Znak izraz = znak.djeca.get(2);
                if (!izraz.ime.equals("<izraz>")) {
                    System.err.println("Neispravno dijete cvora <naredba_grananja>: " + izraz.ime + " umjesto <izraz>");
                    System.exit(1);
                }
                if (!znak.djeca.get(3).ime.equals("D_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <naredba_grananja>: " + znak.djeca.get(3).ime + " umjesto D_ZAGRADA");
                    System.exit(1);
                }
                Znak naredba = znak.djeca.get(4);
                if (!naredba.ime.equals("<naredba>")) {
                    System.err.println("Neispravno dijete cvora <naredba_grananja>: " + naredba.ime + " umjesto <naredba>");
                    System.exit(1);
                }
                if (!Izraz.obradi(izraz)) {
                    return false;
                }
                if (!implicitnoKastabilan(izraz.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                if (!Naredba.obradi(naredba)) {
                    return false;
                }
                break;
            case 7:
                if (!znak.djeca.get(0).ime.equals("KR_IF")) {
                    System.err.println("Neispravno dijete cvora <naredba_grananja>: " + znak.djeca.get(0).ime + " umjesto KR_IF");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("L_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <naredba_grananja>: " + znak.djeca.get(1).ime + " umjesto L_ZAGRADA");
                    System.exit(1);
                }
                Znak izraz1 = znak.djeca.get(2);
                if (!izraz1.ime.equals("<izraz>")) {
                    System.err.println("Neispravno dijete cvora <naredba_grananja>: " + izraz1.ime + " umjesto <izraz>");
                    System.exit(1);
                }
                if (!znak.djeca.get(3).ime.equals("D_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <naredba_grananja>: " + znak.djeca.get(3).ime + " umjesto D_ZAGRADA");
                    System.exit(1);
                }
                Znak naredba1 = znak.djeca.get(4);
                if (!naredba1.ime.equals("<naredba>")) {
                    System.err.println("Neispravno dijete cvora <naredba_grananja>: " + naredba1.ime + " umjesto <naredba>");
                    System.exit(1);
                }
                if (!znak.djeca.get(5).ime.equals("KR_ELSE")) {
                    System.err.println("Neispravno dijete cvora <naredba_grananja>: " + znak.djeca.get(5).ime + " umjesto KR_ELSE");
                    System.exit(1);
                }
                Znak naredba2 = znak.djeca.get(6);
                if (!naredba2.ime.equals("<naredba>")) {
                    System.err.println("Neispravno dijete cvora <naredba_grananja>: " + naredba2.ime + " umjesto <naredba>");
                    System.exit(1);
                }
                if (!Izraz.obradi(izraz1)) {
                    return false;
                }
                if (!implicitnoKastabilan(izraz1.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                if (!Naredba.obradi(naredba1)) {
                    return false;
                }
                if (!Naredba.obradi(naredba2)) {
                    return false;
                }
                break;
            default:
                System.err.println("Neispravan broj djece cvora <naredba_grananja>: " + znak.djeca.size() + " umjesto 5 ili 7");
                System.exit(1);
        }
        return true;
    }
}
