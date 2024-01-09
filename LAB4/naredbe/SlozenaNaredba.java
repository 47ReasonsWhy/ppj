package naredbe;

import deklaracije_i_definicije.ListaDeklaracija;
import znakovi.Znak;

public class SlozenaNaredba {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<slozena_naredba>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <slozena_naredba>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 3:
                if (!znak.djeca.get(0).ime.equals("L_VIT_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <slozena_naredba>: " + znak.djeca.get(0).ime + " umjesto L_VIT_ZAGRADA");
                    System.exit(1);
                }
                Znak listaNaredbi = znak.djeca.get(1);
                if (!listaNaredbi.ime.equals("<lista_naredbi>")) {
                    System.err.println("Neispravno dijete cvora <slozena_naredba>: " + listaNaredbi.ime + " umjesto <lista_naredbi>");
                    System.exit(1);
                }
                if (!znak.djeca.get(2).ime.equals("D_VIT_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <slozena_naredba>: " + znak.djeca.get(2).ime + " umjesto D_VIT_ZAGRADA");
                    System.exit(1);
                }
                if (!ListaNaredbi.obradi(listaNaredbi)) {
                    return false;
                }
                break;
            case 4:
                if (!znak.djeca.get(0).ime.equals("L_VIT_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <slozena_naredba>: " + znak.djeca.get(0).ime + " umjesto L_VIT_ZAGRADA");
                    System.exit(1);
                }
                Znak listaDeklaracija = znak.djeca.get(1);
                if (!listaDeklaracija.ime.equals("<lista_deklaracija>")) {
                    System.err.println("Neispravno dijete cvora <slozena_naredba>: " + listaDeklaracija.ime + " umjesto <lista_deklaracija>");
                    System.exit(1);
                }
                Znak listaNaredbi1 = znak.djeca.get(2);
                if (!listaNaredbi1.ime.equals("<lista_naredbi>")) {
                    System.err.println("Neispravno dijete cvora <slozena_naredba>: " + listaNaredbi1.ime + " umjesto <lista_naredbi>");
                    System.exit(1);
                }
                if (!znak.djeca.get(3).ime.equals("D_VIT_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <slozena_naredba>: " + znak.djeca.get(2).ime + " umjesto D_VIT_ZAGRADA");
                    System.exit(1);
                }
                if (!ListaDeklaracija.obradi(listaDeklaracija)) {
                    return false;
                }
                if (!ListaNaredbi.obradi(listaNaredbi1)) {
                    return false;
                }
                break;
            default:
                System.err.println("Neispravan broj djece cvora <lista_argumenata>: " + znak.djeca.size() + " umjesto 3 ili 4");
                System.exit(1);
        }
        return true;
    }
}
