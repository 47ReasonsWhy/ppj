package deklaracije_i_definicije;

import znakovi.Deklaracija;
import znakovi.Znak;

import static util.Util.ispisiGresku;

public class ListaParametara {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<lista_parametara>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <lista_parametara>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak deklaracijaParametra = znak.djeca.get(0);
                if (!deklaracijaParametra.ime.equals("<deklaracija_parametra>")) {
                    System.err.println("Neispravno ime djeteta cvora <lista_parametara>: " + deklaracijaParametra.ime + " umjesto <deklaracija_parametra>");
                    System.exit(1);
                }
                if (!DeklaracijaParametra.obradi(deklaracijaParametra)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija("[" + deklaracijaParametra.deklaracija.tip + "]", false);
                znak.jedinka = "[" + deklaracijaParametra.jedinka + "]";
                break;
            case 3:
                Znak listaParametara = znak.djeca.get(0);
                if (!listaParametara.ime.equals("<lista_parametara>")) {
                    System.err.println("Neispravno ime djeteta cvora <lista_parametara>: " + listaParametara.ime + " umjesto <lista_parametara>");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("ZAREZ")) {
                    System.err.println("Neispravno ime djeteta cvora <lista_parametara>: " + znak.djeca.get(1).ime + " umjesto ZAREZ");
                    System.exit(1);
                }
                Znak deklaracijaParametra1 = znak.djeca.get(2);
                if (!deklaracijaParametra1.ime.equals("<deklaracija_parametra>")) {
                    System.err.println("Neispravno ime djeteta cvora <lista_parametara>: " + deklaracijaParametra1.ime + " umjesto <deklaracija_parametra>");
                    System.exit(1);
                }
                if (!ListaParametara.obradi(listaParametara)) {
                    return false;
                }
                if (!DeklaracijaParametra.obradi(deklaracijaParametra1)) {
                    return false;
                }
                String[] imena = listaParametara.jedinka.substring(1, listaParametara.jedinka.length() - 1).split(", ");
                for (String ime : imena) {
                    if (ime.equals(deklaracijaParametra1.jedinka)) {
                        ispisiGresku(znak);
                        return false;
                    }
                }
                znak.deklaracija = new Deklaracija(listaParametara.deklaracija.tip.substring(0, listaParametara.deklaracija.tip.length() - 1) + ", " + deklaracijaParametra1.deklaracija.tip + "]", false);
                znak.jedinka = listaParametara.jedinka.substring(0, listaParametara.jedinka.length() - 1) + ", " + deklaracijaParametra1.jedinka + "]";
                break;
        }
        return true;
    }
}
