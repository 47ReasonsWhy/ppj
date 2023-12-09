package izrazi;

import znakovi.Deklaracija;
import znakovi.Znak;

import java.util.List;

import static util.Util.implicitnoKastabilan;
import static util.Util.ispisiGresku;

public class UnarniIzraz {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<unarni_izraz>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <unarni_izraz>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak postfiksIzraz = znak.djeca.get(0);
                if (!postfiksIzraz.ime.equals("<postfiks_izraz>")) {
                    System.err.println("Neispravno dijete cvora <unarni_izraz>: " + postfiksIzraz.ime + " umjesto <postfiks_izraz>");
                    System.exit(1);
                }
                if (!PostfiksIzraz.obradi(postfiksIzraz)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(postfiksIzraz.deklaracija);
                break;
            case 2:
                switch (znak.djeca.get(1).ime) {
                    case "<unarni_izraz>":
                        if (!List.of("OP_INC", "OP_DEC").contains(znak.djeca.get(0).ime)) {
                            System.err.println("Neispravno dijete cvora <unarni_izraz>: " + znak.djeca.get(0).ime + " umjesto OP_INC ili OP_DEC");
                            System.exit(1);
                        }
                        Znak unarniIzraz = znak.djeca.get(1);
                        if (!UnarniIzraz.obradi(unarniIzraz)) {
                            return false;
                        }
                        Deklaracija deklaracija = unarniIzraz.deklaracija;
                        if (!deklaracija.l_izraz || !implicitnoKastabilan(deklaracija.tip, "int")) {
                            ispisiGresku(znak);
                            return false;
                        }
                        znak.deklaracija = new Deklaracija("int", false);
                        break;
                    case "<cast_izraz>":
                        if (!znak.djeca.get(0).ime.equals("<unarni_operator>")) {
                            System.err.println("Neispravno dijete cvora <unarni_izraz>: " + znak.djeca.get(0).ime + " umjesto <unarni_operator>");
                            System.exit(1);
                        }
                        Znak castIzraz = znak.djeca.get(1);
                        if (!CastIzraz.obradi(castIzraz)) {
                            return false;
                        }
                        Deklaracija deklaracija1 = castIzraz.deklaracija;
                        if (!implicitnoKastabilan(deklaracija1.tip, "int")) {
                            ispisiGresku(znak);
                            return false;
                        }
                        znak.deklaracija = new Deklaracija("int", false);
                        break;
                    default:
                        System.err.println("Neispravno dijete cvora <unarni_izraz>: " + znak.djeca.get(1).ime + " umjesto <unarni_izraz> ili <cast_izraz>");
                        System.exit(1);
                }
                break;
            default:
                System.err.println("Neispravan broj djece cvora <unarni_izraz>: " + znak.djeca.size() + " umjesto 1 ili 2");
                System.exit(1);
        }
        return true;
    }
}
