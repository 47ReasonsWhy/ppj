package deklaracije_i_definicije;

import izrazi.IzrazPridruzivanja;
import znakovi.Znak;

import java.util.List;

import static util.Util.strpajKod;

public class ListaIzrazaPridruzivanja {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<lista_izraza_pridruzivanja>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <lista_izraza_pridruzivanja>");
            System.exit(1);
        }
        Znak izrazPridruzivanja;
        switch (znak.djeca.size()) {
            case 1:
                izrazPridruzivanja = znak.djeca.get(0);
                if (!izrazPridruzivanja.ime.equals("<izraz_pridruzivanja>")) {
                    System.err.println("Neispravno ime djeteta cvora <lista_izraza_pridruzivanja>: " + izrazPridruzivanja.ime + " umjesto <izraz_pridruzivanja>");
                    System.exit(1);
                }
                if (!IzrazPridruzivanja.obradi(izrazPridruzivanja)) {
                    return false;
                }
                znak.deklaracija = new znakovi.Deklaracija("[" + izrazPridruzivanja.deklaracija.tip + "]", false);
                znak.br_elem = 1;
                break;
            case 3:
                Znak listaIzrazaPridruzivanja = znak.djeca.get(0);
                if (!listaIzrazaPridruzivanja.ime.equals("<lista_izraza_pridruzivanja>")) {
                    System.err.println("Neispravno ime djeteta cvora <lista_izraza_pridruzivanja>: " + listaIzrazaPridruzivanja.ime + " umjesto <lista_izraza_pridruzivanja>");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("ZAREZ")) {
                    System.err.println("Neispravno ime djeteta cvora <lista_izraza_pridruzivanja>: " + znak.djeca.get(1).ime + " umjesto ZAREZ");
                    System.exit(1);
                }
                izrazPridruzivanja = znak.djeca.get(2);
                if (!izrazPridruzivanja.ime.equals("<izraz_pridruzivanja>")) {
                    System.err.println("Neispravno ime djeteta cvora <lista_izraza_pridruzivanja>: " + izrazPridruzivanja.ime + " umjesto <izraz_pridruzivanja>");
                    System.exit(1);
                }
                if (!ListaIzrazaPridruzivanja.obradi(listaIzrazaPridruzivanja)) {
                    return false;
                }
                if (!IzrazPridruzivanja.obradi(izrazPridruzivanja)) {
                    return false;
                }
                znak.deklaracija = new znakovi.Deklaracija(listaIzrazaPridruzivanja.deklaracija.tip.substring(0, listaIzrazaPridruzivanja.deklaracija.tip.length() - 1) + ", " + izrazPridruzivanja.deklaracija.tip + "]", false);
                znak.br_elem = listaIzrazaPridruzivanja.br_elem + 1;
                break;
        }

        strpajKod(znak, List.of(
                ""
        ));

        return true;
    }
}
