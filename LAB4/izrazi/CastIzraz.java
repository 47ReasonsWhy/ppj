package izrazi;

import znakovi.Deklaracija;
import znakovi.Znak;

import static util.Util.*;

public class CastIzraz {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<cast_izraz>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <cast_izraz>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak unarniIzraz = znak.djeca.get(0);
                if (!unarniIzraz.ime.equals("<unarni_izraz>")) {
                    System.err.println("Neispravno dijete cvora <cast_izraz>: " + unarniIzraz.ime + " umjesto <unarni_izraz>");
                    System.exit(1);
                }
                if (!UnarniIzraz.obradi(unarniIzraz)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(unarniIzraz.deklaracija);
                znak.jedinka = unarniIzraz.jedinka;
                break;
            case 4:
                if (!znak.djeca.get(0).ime.equals("L_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <cast_izraz>: " + znak.djeca.get(0).ime + " umjesto L_ZAGRADA");
                    System.exit(1);
                }
                Znak imeTipa = znak.djeca.get(1);
                if (!imeTipa.ime.equals("<ime_tipa>")) {
                    System.err.println("Neispravno dijete cvora <cast_izraz>: " + imeTipa.ime + " umjesto <ime_tipa>");
                    System.exit(1);
                }
                if (!znak.djeca.get(2).ime.equals("D_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <cast_izraz>: " + znak.djeca.get(2).ime + " umjesto D_ZAGRADA");
                    System.exit(1);
                }
                Znak castIzraz = znak.djeca.get(3);
                if (!castIzraz.ime.equals("<cast_izraz>")) {
                    System.err.println("Neispravno dijete cvora <cast_izraz>: " + castIzraz.ime + " umjesto <cast_izraz>");
                    System.exit(1);
                }
                if (!ImeTipa.obradi(imeTipa)) {
                    return false;
                }
                if (!CastIzraz.obradi(castIzraz)) {
                    return false;
                }
                if (!eksplicitnoKastabilan(castIzraz.deklaracija.tip, imeTipa.deklaracija.tip)) {
                    ispisiGresku(znak);
                    return false;
                }
                znak.deklaracija = new Deklaracija(imeTipa.deklaracija.tip, false);
                break;
            default:
                System.err.println("Neispravan broj djece cvora <cast_izraz>: " + znak.djeca.size() + " umjesto 1 ili 4");
                System.exit(1);
        }
        return true;
    }
}
