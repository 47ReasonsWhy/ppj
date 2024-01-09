package naredbe;

import znakovi.Znak;

public class ListaNaredbi {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<lista_naredbi>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <lista_naredbi>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak naredba = znak.djeca.get(0);
                if (!naredba.ime.equals("<naredba>")) {
                    System.err.println("Neispravno dijete cvora <lista_naredbi>: " + naredba.ime + " umjesto <naredba>");
                    System.exit(1);
                }
                if (!Naredba.obradi(naredba)) {
                    return false;
                }
                break;
            case 2:
                Znak listaNaredbi = znak.djeca.get(0);
                if (!listaNaredbi.ime.equals("<lista_naredbi>")) {
                    System.err.println("Neispravno dijete cvora <lista_naredbi>: " + listaNaredbi.ime + " umjesto <lista_naredbi>");
                    System.exit(1);
                }
                Znak naredba1 = znak.djeca.get(1);
                if (!naredba1.ime.equals("<naredba>")) {
                    System.err.println("Neispravno dijete cvora <lista_naredbi>: " + naredba1.ime + " umjesto <naredba>");
                    System.exit(1);
                }
                if (!ListaNaredbi.obradi(listaNaredbi)) {
                    return false;
                }
                if (!Naredba.obradi(naredba1)) {
                    return false;
                }
                break;
            default:
                System.err.println("Neispravan broj djece cvora <lista_argumenata>: " + znak.djeca.size() + " umjesto 1 ili 2");
                System.exit(1);
        }
        return true;
    }
}
