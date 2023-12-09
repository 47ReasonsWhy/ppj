package deklaracije_i_definicije;

import znakovi.Znak;

public class ListaDeklaracija {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<lista_deklaracija>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <lista_deklaracija>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak deklaracija = znak.djeca.get(0);
                if (!deklaracija.ime.equals("<deklaracija>")) {
                    System.err.println("Neispravno ime djeteta cvora <lista_deklaracija>: " + deklaracija.ime + " umjesto <deklaracija>");
                    System.exit(1);
                }
                if (!Deklaracija.obradi(deklaracija)) {
                    return false;
                }
                break;
            case 2:
                Znak lista_deklaracija = znak.djeca.get(0);
                if (!lista_deklaracija.ime.equals("<lista_deklaracija>")) {
                    System.err.println("Neispravno ime djeteta cvora <lista_deklaracija>: " + lista_deklaracija.ime + " umjesto <lista_deklaracija>");
                    System.exit(1);
                }
                Znak deklaracija2 = znak.djeca.get(1);
                if (!deklaracija2.ime.equals("<deklaracija>")) {
                    System.err.println("Neispravno ime djeteta cvora <lista_deklaracija>: " + deklaracija2.ime + " umjesto <deklaracija>");
                    System.exit(1);
                }
                if (!ListaDeklaracija.obradi(lista_deklaracija)) {
                    return false;
                }
                if (!Deklaracija.obradi(deklaracija2)) {
                    return false;
                }
                break;
            default:
                System.err.println("Neispravan broj djece cvora <lista_deklaracija>: " + znak.djeca.size() + " umjesto 1 ili 2");
                System.exit(1);
        }
        return true;
    }
}
