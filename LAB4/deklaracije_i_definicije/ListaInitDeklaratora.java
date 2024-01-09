package deklaracije_i_definicije;

import znakovi.Deklaracija;
import znakovi.Znak;

public class ListaInitDeklaratora {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<lista_init_deklaratora>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <lista_init_deklaratora>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak initDeklarator = znak.djeca.get(0);
                if (!initDeklarator.ime.equals("<init_deklarator>")) {
                    System.err.println("Neispravno ime djeteta cvora <lista_init_deklaratora>: " + initDeklarator.ime + " umjesto <init_deklarator>");
                    System.exit(1);
                }
                initDeklarator.deklaracija = new Deklaracija(znak.deklaracija.tip, false);
                if (!InitDeklarator.obradi(initDeklarator)) {
                    return false;
                }
                break;
            case 3:
                Znak listaInitDeklaratora = znak.djeca.get(0);
                if (!listaInitDeklaratora.ime.equals("<lista_init_deklaratora>")) {
                    System.err.println("Neispravno ime djeteta cvora <lista_init_deklaratora>: " + listaInitDeklaratora.ime + " umjesto <lista_init_deklaratora>");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("ZAREZ")) {
                    System.err.println("Neispravno ime djeteta cvora <lista_init_deklaratora>: " + znak.djeca.get(1).ime + " umjesto ZAREZ");
                    System.exit(1);
                }
                Znak initDeklarator2 = znak.djeca.get(2);
                if (!initDeklarator2.ime.equals("<init_deklarator>")) {
                    System.err.println("Neispravno ime djeteta cvora <lista_init_deklaratora>: " + initDeklarator2.ime + " umjesto <init_deklarator>");
                    System.exit(1);
                }
                listaInitDeklaratora.deklaracija = new Deklaracija(znak.deklaracija.tip, false);
                if (!ListaInitDeklaratora.obradi(listaInitDeklaratora)) {
                    return false;
                }
                initDeklarator2.deklaracija = new Deklaracija(znak.deklaracija.tip, false);
                if (!InitDeklarator.obradi(initDeklarator2)) {
                    return false;
                }
                break;
            default:
                System.err.println("Neispravan broj djece cvora <lista_init_deklaratora>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
