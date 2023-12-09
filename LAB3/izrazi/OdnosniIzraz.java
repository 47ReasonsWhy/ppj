package izrazi;

import znakovi.Deklaracija;
import znakovi.Znak;

import java.util.List;

import static util.Util.implicitnoKastabilan;
import static util.Util.ispisiGresku;

public class OdnosniIzraz {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<odnosni_izraz>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <odnosni_izraz>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak aditivniIzraz = znak.djeca.get(0);
                if (!aditivniIzraz.ime.equals("<aditivni_izraz>")) {
                    System.err.println("Neispravno dijete cvora <odnosni_izraz>: " + aditivniIzraz.ime + " umjesto <aditivni_izraz>");
                    System.exit(1);
                }
                if (!AditivniIzraz.obradi(aditivniIzraz)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(aditivniIzraz.deklaracija);
                break;
            case 3:
                Znak odnosniIzraz = znak.djeca.get(0);
                if (!odnosniIzraz.ime.equals("<odnosni_izraz>")) {
                    System.err.println("Neispravno dijete cvora <odnosni_izraz>: " + odnosniIzraz.ime + " umjesto <odnosni_izraz>");
                    System.exit(1);
                }
                if (!List.of("OP_LT", "OP_GT", "OP_LTE", "OP_GTE").contains(znak.djeca.get(1).ime)) {
                    System.err.println("Neispravno dijete cvora <odnosni_izraz>: " + znak.djeca.get(1).ime + " umjesto OP_LT, OP_GT, OP_LTE ili OP_GTE");
                    System.exit(1);
                }
                Znak aditivniIzraz1 = znak.djeca.get(2);
                if (!aditivniIzraz1.ime.equals("<aditivni_izraz>")) {
                    System.err.println("Neispravno dijete cvora <odnosni_izraz>: " + aditivniIzraz1.ime + " umjesto <aditivni_izraz>");
                    System.exit(1);
                }
                if (!OdnosniIzraz.obradi(odnosniIzraz)) {
                    return false;
                }
                if (!implicitnoKastabilan(odnosniIzraz.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                if (!AditivniIzraz.obradi(aditivniIzraz1)) {
                    return false;
                }
                if (!implicitnoKastabilan(aditivniIzraz1.deklaracija.tip, "int")) {
                    ispisiGresku(znak);
                    return false;
                }
                znak.deklaracija = new Deklaracija("int", false);
                break;
            default:
                System.err.println("Neispravan broj djece cvora <odnosni_izraz>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
