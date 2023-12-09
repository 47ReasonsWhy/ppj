package izrazi;

import znakovi.Deklaracija;
import znakovi.Znak;

import java.util.List;

import static util.Util.implicitnoKastabilan;
import static util.Util.ispisiGresku;

public class PostfiksIzraz {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<postfiks_izraz>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <postfiks_izraz>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                if (!znak.djeca.get(0).ime.equals("<primarni_izraz>")) {
                    System.err.println("Neispravno dijete cvora <postfiks_izraz>: " + znak.djeca.get(0).ime + " umjesto <primarni_izraz>");
                    System.exit(1);
                }
                if (!PrimarniIzraz.obradi(znak.djeca.get(0))) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(znak.djeca.get(0).deklaracija);
                break;
            case 2:
                Znak postfiksIzraz1 = znak.djeca.get(0);
                if (!postfiksIzraz1.ime.equals("<postfiks_izraz>")) {
                    System.err.println("Neispravno dijete cvora <postfiks_izraz>: " + postfiksIzraz1.ime + " umjesto <postfiks_izraz>");
                    System.exit(1);
                }
                if (!List.of("OP_INC", "OP_DEC").contains(znak.djeca.get(1).ime)) {
                    System.err.println("Neispravno dijete cvora <postfiks_izraz>: " + znak.djeca.get(1).ime + " umjesto OP_INC ili OP_DEC");
                    System.exit(1);
                }
                if (!PostfiksIzraz.obradi(postfiksIzraz1)) {
                    return false;
                }
                if (!postfiksIzraz1.deklaracija.l_izraz || !implicitnoKastabilan(postfiksIzraz1.deklaracija.tip, "int")) {
                    System.err.println("Neispravan tip u izrazu za " + znak.djeca.get(1).ime + " na liniji " + znak.djeca.get(1).linija);
                    System.exit(1);
                }
                znak.deklaracija = new Deklaracija("int", false);
                break;
            case 3:
                Znak postfiksIzraz2 = znak.djeca.get(0);
                if (!postfiksIzraz2.ime.equals("<postfiks_izraz>")) {
                    System.err.println("Neispravno dijete cvora <postfiks_izraz>: " + postfiksIzraz2.ime + " umjesto <postfiks_izraz>");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("L_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <postfiks_izraz>: " + znak.djeca.get(1).ime + " umjesto L_ZAGRADA");
                    System.exit(1);
                }
                if (!znak.djeca.get(2).ime.equals("D_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <postfiks_izraz>: " + znak.djeca.get(2).ime + " umjesto D_ZAGRADA");
                    System.exit(1);
                }
                if (!PostfiksIzraz.obradi(postfiksIzraz2)) {
                    return false;
                }
                if (!postfiksIzraz2.deklaracija.tip.startsWith("funkcija(void -> ")) {
                        ispisiGresku(znak);
                        return false;
                }
                znak.deklaracija = new Deklaracija(postfiksIzraz2.deklaracija.tip.substring(17, postfiksIzraz2.deklaracija.tip.length() - 1), false);
                break;
            case 4:
                switch (znak.djeca.get(1).ime) {
                    case "L_UGL_ZAGRADA":
                        Znak postfiksIzraz3 = znak.djeca.get(0);
                        if (!postfiksIzraz3.ime.equals("<postfiks_izraz>")) {
                            System.err.println("Neispravno dijete cvora <postfiks_izraz>: " + postfiksIzraz3.ime + " umjesto <postfiks_izraz>");
                            System.exit(1);
                        }
                        Znak izraz = znak.djeca.get(2);
                        if (!izraz.ime.equals("<izraz>")) {
                            System.err.println("Neispravno dijete cvora <postfiks_izraz>: " + izraz.ime + " umjesto <izraz>");
                            System.exit(1);
                        }
                        if (!znak.djeca.get(3).ime.equals("D_UGL_ZAGRADA")) {
                            System.err.println("Neispravno dijete cvora <postfiks_izraz>: " + znak.djeca.get(3).ime + " umjesto D_UGL_ZAGRADA");
                            System.exit(1);
                        }
                        if (!PostfiksIzraz.obradi(postfiksIzraz3)) {
                            return false;
                        }
                        if (!postfiksIzraz3.deklaracija.tip.startsWith("niz(")) {
                            ispisiGresku(znak);
                            return false;
                        }
                        String X = postfiksIzraz3.deklaracija.tip.substring(4, postfiksIzraz3.deklaracija.tip.length() - 1);
                        if (!List.of("char", "int", "const(char)", "const(int)").contains(X)) {
                            ispisiGresku(znak);
                            return false;
                        }
                        if (!Izraz.obradi(izraz)) {
                            return false;
                        }
                        if (!implicitnoKastabilan(izraz.deklaracija.tip, "int")) {
                            ispisiGresku(znak);
                            return false;
                        }
                        znak.deklaracija = new Deklaracija(X, !X.startsWith("const("));
                        break;
                    case "L_ZAGRADA":
                        Znak postfiksIzraz4 = znak.djeca.get(0);
                        if (!postfiksIzraz4.ime.equals("<postfiks_izraz>")) {
                            System.err.println("Neispravno dijete cvora <postfiks_izraz>: " + postfiksIzraz4.ime + " umjesto <postfiks_izraz>");
                            System.exit(1);
                        }
                        Znak listaArgumenata = znak.djeca.get(2);
                        if (!listaArgumenata.ime.equals("<lista_argumenata>")) {
                            System.err.println("Neispravno dijete cvora <postfiks_izraz>: " + listaArgumenata.ime + " umjesto <lista_argumenata>");
                            System.exit(1);
                        }
                        if (!znak.djeca.get(3).ime.equals("D_ZAGRADA")) {
                            System.err.println("Neispravno dijete cvora <postfiks_izraz>: " + znak.djeca.get(3).ime + " umjesto D_ZAGRADA");
                            System.exit(1);
                        }
                        if (!PostfiksIzraz.obradi(postfiksIzraz4)) {
                            return false;
                        }
                        if (!ListaArgumenata.obradi(listaArgumenata)) {
                            return false;
                        }
                        if (!postfiksIzraz4.deklaracija.tip.startsWith("funkcija([")) {
                                ispisiGresku(znak);
                                return false;
                        }
                        String[] params = postfiksIzraz4.deklaracija.tip.split("] -> ")[0].substring(10).split(", ");
                        String[] tipovi = listaArgumenata.deklaracija.tip.substring(1, listaArgumenata.deklaracija.tip.length() - 1).split(", ");
                        if (params.length != tipovi.length) {
                            ispisiGresku(znak);
                            return false;
                        }
                        for (int i = 0; i < params.length; i++) {
                            if (!implicitnoKastabilan(tipovi[i], params[i])) {
                                ispisiGresku(znak);
                                return false;
                            }
                        }
                        znak.deklaracija = new Deklaracija(postfiksIzraz4.deklaracija.tip.substring(0, postfiksIzraz4.deklaracija.tip.length() - 1).split("] -> ")[1], false);
                        break;
                    default:
                        System.err.println("Neispravno dijete cvora <postfiks_izraz>: " + znak.djeca.get(1).ime + " umjesto L_ZAGRADA ili L_UGL_ZAGRADA");
                        System.exit(1);
                }
                break;
            default:
                System.err.println("Neispravan broj djece cvora <postfiks_izraz>: " + znak.djeca.size() + " umjesto 1, 2, 3 ili 4");
                System.exit(1);
        }
        return true;
    }
}
