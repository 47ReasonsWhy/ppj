package izrazi;

import znakovi.Deklaracija;
import znakovi.Znak;

public class ListaArgumenata {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<lista_argumenata>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <lista_argumenata>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak izrazPridruzivanja = znak.djeca.get(0);
                if (!izrazPridruzivanja.ime.equals("<izraz_pridruzivanja>")) {
                    System.err.println("Neispravno dijete cvora <lista_argumenata>: " + izrazPridruzivanja.ime + " umjesto <izraz_pridruzivanja>");
                    System.exit(1);
                }
                if (!IzrazPridruzivanja.obradi(izrazPridruzivanja)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija("[" + izrazPridruzivanja.deklaracija.tip + "]", false);
                break;
            case 3:
                Znak listaArgumenata = znak.djeca.get(0);
                if (!listaArgumenata.ime.equals("<lista_argumenata>")) {
                    System.err.println("Neispravno dijete cvora <lista_argumenata>: " + listaArgumenata.ime + " umjesto <lista_argumenata>");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("ZAREZ")) {
                    System.err.println("Neispravno dijete cvora <lista_argumenata>: " + znak.djeca.get(1).ime + " umjesto ZAREZ");
                    System.exit(1);
                }
                Znak izrazPridruzivanja1 = znak.djeca.get(2);
                if (!izrazPridruzivanja1.ime.equals("<izraz_pridruzivanja>")) {
                    System.err.println("Neispravno dijete cvora <lista_argumenata>: " + izrazPridruzivanja1.ime + " umjesto <izraz_pridruzivanja>");
                    System.exit(1);
                }
                if (!ListaArgumenata.obradi(listaArgumenata)) {
                    return false;
                }
                if (!IzrazPridruzivanja.obradi(izrazPridruzivanja1)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(listaArgumenata.deklaracija.tip.substring(0, listaArgumenata.deklaracija.tip.length() - 1) + ", " + izrazPridruzivanja1.deklaracija.tip + "]", false);
                break;
            default:
                System.err.println("Neispravan broj djece cvora <lista_argumenata>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
