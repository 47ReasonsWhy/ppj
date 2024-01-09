package deklaracije_i_definicije;

import izrazi.ImeTipa;
import znakovi.Deklaracija;
import znakovi.Znak;

import static util.Util.ispisiGresku;

public class DeklaracijaParametra {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<deklaracija_parametra>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <deklaracija_parametra>");
            System.exit(1);
        }
        Znak imeTipa;
        Znak idn;
        switch (znak.djeca.size()) {
            case 2:
                imeTipa = znak.djeca.get(0);
                if (!imeTipa.ime.equals("<ime_tipa>")) {
                    System.err.println("Neispravno ime djeteta cvora <deklaracija_parametra>: " + imeTipa.ime + " umjesto <ime_tipa>");
                    System.exit(1);
                }
                idn = znak.djeca.get(1);
                if (!idn.ime.equals("IDN")) {
                    System.err.println("Neispravno ime djeteta cvora <deklaracija_parametra>: " + idn.ime + " umjesto IDN");
                    System.exit(1);
                }
                if (!ImeTipa.obradi(imeTipa)) {
                    return false;
                }
                if (imeTipa.deklaracija.tip.equals("void")) {
                    ispisiGresku(znak);
                    return false;
                }
                znak.deklaracija = new Deklaracija(imeTipa.deklaracija.tip, false);
                znak.jedinka = idn.jedinka;
                break;
            case 4:
                imeTipa = znak.djeca.get(0);
                if (!imeTipa.ime.equals("<ime_tipa>")) {
                    System.err.println("Neispravno ime djeteta cvora <deklaracija_parametra>: " + imeTipa.ime + " umjesto <ime_tipa>");
                    System.exit(1);
                }
                idn = znak.djeca.get(1);
                if (!idn.ime.equals("IDN")) {
                    System.err.println("Neispravno ime djeteta cvora <deklaracija_parametra>: " + idn.ime + " umjesto IDN");
                    System.exit(1);
                }
                if (!znak.djeca.get(2).ime.equals("L_UGL_ZAGRADA")) {
                    System.err.println("Neispravno ime djeteta cvora <deklaracija_parametra>: " + znak.djeca.get(2).ime + " umjesto L_UGL_ZAGRADA");
                    System.exit(1);
                }
                if (!znak.djeca.get(3).ime.equals("D_UGL_ZAGRADA")) {
                    System.err.println("Neispravno ime djeteta cvora <deklaracija_parametra>: " + znak.djeca.get(3).ime + " umjesto D_UGL_ZAGRADA");
                    System.exit(1);
                }
                if (!ImeTipa.obradi(imeTipa)) {
                    return false;
                }
                if (imeTipa.deklaracija.tip.equals("void")) {
                    ispisiGresku(znak);
                    return false;
                }
                znak.deklaracija = new Deklaracija("niz(" + imeTipa.deklaracija.tip + ")", false);
                znak.jedinka = idn.jedinka;
                break;
            default:
                System.err.println("Neispravan broj djece cvora <deklaracija_parametra>: " + znak.djeca.size() + " umjesto 2 ili 4");
                System.exit(1);
                return false;
        }
        boolean unutarDefinicijeFunkcije = false;
        Znak trenutni = znak;
        while (trenutni != null) {
            if (trenutni.ime.equals("<definicija_funkcije>")) {
                unutarDefinicijeFunkcije = true;
                break;
            }
            trenutni = trenutni.roditelj;
        }
        if (unutarDefinicijeFunkcije) {
            for (String var : znak.tablice.stackOffset.keySet()) {
                znak.tablice.stackOffset.replace(var, znak.tablice.stackOffset.get(var) + 4);
            }
            znak.tablice.stackOffset.put(idn.jedinka, 4);
        }
        return true;
    }
}
