package deklaracije_i_definicije;

import izrazi.IzrazPridruzivanja;
import znakovi.Deklaracija;
import znakovi.Znak;

import java.util.LinkedList;

public class Inicijalizator {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<inicijalizator>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <inicijalizator>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak izrazPridruzivanja = znak.djeca.get(0);
                if (!izrazPridruzivanja.ime.equals("<izraz_pridruzivanja>")) {
                    System.err.println("Neispravno ime djeteta cvora <inicijalizator>: " + izrazPridruzivanja.ime + " umjesto <izraz_pridruzivanja>");
                    System.exit(1);
                }
                if (!IzrazPridruzivanja.obradi(izrazPridruzivanja)) {
                    return false;
                }
                Znak nizZnakova = null;
                LinkedList<Znak> djecaZaProvjeru = new LinkedList<>(znak.djeca);
                while (!djecaZaProvjeru.isEmpty()) {
                    Znak dijete = djecaZaProvjeru.removeFirst();
                    if (dijete.ime.equals("NIZ_ZNAKOVA")) {
                        nizZnakova = dijete;
                        break;
                    }
                    djecaZaProvjeru.addAll(dijete.djeca);
                }
                if (nizZnakova != null) {
                    znak.br_elem = nizZnakova.jedinka.length() - 1;
                    znak.deklaracija = new Deklaracija("[" + "char, ".repeat(Math.max(0, znak.br_elem - 1)) + "char]", false);
                } else {
                    znak.deklaracija = new Deklaracija(izrazPridruzivanja.deklaracija.tip, false);
                }
                znak.jedinka = izrazPridruzivanja.jedinka;
                break;
            case 3:
                if (!znak.djeca.get(0).ime.equals("L_VIT_ZAGRADA")) {
                    System.err.println("Neispravno ime djeteta cvora <inicijalizator>: " + znak.djeca.get(0).ime + " umjesto L_VIT_ZAGRADA");
                    System.exit(1);
                }
                Znak listaIzrazaPridruzivanja = znak.djeca.get(1);
                if (!listaIzrazaPridruzivanja.ime.equals("<lista_izraza_pridruzivanja>")) {
                    System.err.println("Neispravno ime djeteta cvora <inicijalizator>: " + listaIzrazaPridruzivanja.ime + " umjesto <lista_izraza_pridruzivanja>");
                    System.exit(1);
                }
                if (!znak.djeca.get(2).ime.equals("D_VIT_ZAGRADA")) {
                    System.err.println("Neispravno ime djeteta cvora <inicijalizator>: " + znak.djeca.get(2).ime + " umjesto D_VIT_ZAGRADA");
                    System.exit(1);
                }
                if (!ListaIzrazaPridruzivanja.obradi(listaIzrazaPridruzivanja)) {
                    return false;
                }
                znak.br_elem = listaIzrazaPridruzivanja.br_elem;
                znak.deklaracija = new Deklaracija(listaIzrazaPridruzivanja.deklaracija.tip, false);
                znak.jedinka = listaIzrazaPridruzivanja.jedinka;
                break;
        }
        return true;
    }
}
