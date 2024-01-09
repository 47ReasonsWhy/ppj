package izrazi;

import util.Util;
import znakovi.Deklaracija;
import znakovi.Znak;

import java.util.List;
import java.util.Map;

import static util.Util.*;

public class PrimarniIzraz {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<primarni_izraz>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <primarni_izraz>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak dijete = znak.djeca.get(0);
                switch (dijete.ime) {
                    case "IDN":
                        Deklaracija deklaracija = pronadjiDeklaraciju(znak, dijete.jedinka);
                        if (deklaracija == null) {
                            ispisiGresku(znak);
                            return false;
                        }
                        znak.deklaracija = new Deklaracija(deklaracija);
                        // znak.jedinka = dijete.jedinka;

                        if (deklaracija.tip.startsWith("funkcija")) {
                            strpajKod(znak, List.of(
                                    "\t\t\tCALL\tF_" + dijete.jedinka.toUpperCase(),
                                    "\t\t\tPUSH\tR6"
                            ));
                        } else if (znak.tablice.stackOffset.get(dijete.jedinka) == null) {
                            Znak trenutni = znak;
                            int index;
                            while (trenutni != null && trenutni.tablice.tablicaIndeksaVarijabli.get(dijete.jedinka) == null) {
                                trenutni = trenutni.roditelj;
                            }
                            if (trenutni == null) {
                                System.err.println("Nije pronadjen indeks varijable " + dijete.jedinka);
                                System.exit(1);
                                return false;
                            }
                            index = trenutni.tablice.tablicaIndeksaVarijabli.get(dijete.jedinka);
                            strpajKod(znak, List.of(
                                    "\t\t\tLOAD\tR0, (G_" + String.format("%04X", index) + ")",
                                    "\t\t\tPUSH\tR0"
                            ));
                        } else {
                            strpajKod(znak, List.of(
                                    "\t\t\tLOAD\tR0, (R5+" + String.format("%04X", znak.tablice.stackOffset.get(dijete.jedinka)) + ")",
                                    "\t\t\tPUSH\tR0"
                            ));
                        }
                        break;
                    case "BROJ":
                        try {
                            Integer.parseInt(dijete.jedinka);
                        } catch (NumberFormatException e) {
                            ispisiGresku(znak);
                            return false;
                        }
                        znak.deklaracija = new Deklaracija("int", false);
                        znak.jedinka = dijete.jedinka;

                        Map.Entry<Integer, Integer> x = Util.splitIntConst(dijete.jedinka);
                        strpajKod(znak, List.of(
                                "\t\t\tMOVE\t%D " + x.getKey() + ", R0",
                                "\t\t\tSHL\t\tR0, %D 20, R0",
                                "\t\t\tADD\t\tR0, %D " + x.getValue() + ", R0",
                                "\t\t\tPUSH\tR0"
                        ));
                        break;
                    case "ZNAK":
                        if (dijete.jedinka.charAt(0) != '\'' || dijete.jedinka.charAt(dijete.jedinka.length() - 1) != '\'') {
                            ispisiGresku(znak);
                            return false;
                        }
                        if (dijete.jedinka.length() == 3) {
                            char c = dijete.jedinka.charAt(1);
                            if (c > 255) {
                                ispisiGresku(znak);
                                return false;
                            }
                        } else if (dijete.jedinka.length() == 4) {
                            char c1 = dijete.jedinka.charAt(1);
                            char c2 = dijete.jedinka.charAt(2);
                            if (c1 != '\\') {
                                ispisiGresku(znak);
                                return false;
                            }
                            if (c2 != 't' && c2 != 'n' && c2 != '0' && c2 != '\'' && c2 != '\"' && c2 != '\\') {
                                ispisiGresku(znak);
                                return false;
                            }
                        } else {
                            ispisiGresku(znak);
                            return false;
                        }
                        znak.deklaracija = new Deklaracija("char", false);

                        char c;
                        if (dijete.jedinka.length() == 3) {
                            c = dijete.jedinka.charAt(1);
                        } else {
                            char c1 = dijete.jedinka.charAt(2);
                            switch (c1) {
                                case 't':
                                    c = '\t';
                                    break;
                                case 'n':
                                    c = '\n';
                                    break;
                                case '0':
                                    c = '\0';
                                    break;
                                case '\'':
                                    c = '\'';
                                    break;
                                case '\"':
                                    c = '\"';
                                    break;
                                case '\\':
                                    c = '\\';
                                    break;
                                default:
                                    ispisiGresku(znak);
                                    return false;
                            }
                        }
                        strpajKod(znak, List.of(
                                "\t\t\tMOVE\t%D " + (int) c + ", R0",
                                "\t\t\tPUSH\tR0"
                        ));

                        break;
                    case "NIZ_ZNAKOVA":
                        if (dijete.jedinka.charAt(0) != '\"' || dijete.jedinka.charAt(dijete.jedinka.length() - 1) != '\"') {
                            ispisiGresku(znak);
                            return false;
                        }
                        String niz = dijete.jedinka.substring(1, dijete.jedinka.length() - 1);
                        for (int i = 0; i < niz.length(); i++) {
                            char c1 = niz.charAt(i);
                            if (c1 > 255) {
                                ispisiGresku(znak);
                                return false;
                            }
                            if (c1 == '\\') {
                                i++;
                                if (i == niz.length()) {
                                    ispisiGresku(znak);
                                    return false;
                                }
                                char c2 = niz.charAt(i);
                                if (c2 != 't' && c2 != 'n' && c2 != '0' && c2 != '\'' && c2 != '\"' && c2 != '\\') {
                                    ispisiGresku(znak);
                                    return false;
                                }
                            }
                        }
                        znak.deklaracija = new Deklaracija("niz(const(char))", false);
                        break;
                    default:
                        System.err.println("Neispravno dijete cvora <primarni_izraz>: " + znak.djeca.get(0).ime);
                        System.exit(1);
                }
                break;
            case 3:
                if (!znak.djeca.get(0).ime.equals("L_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <primarni_izraz>: " + znak.djeca.get(0).ime + " umjesto L_ZAGRADA");
                    System.exit(1);
                }
                Znak izraz = znak.djeca.get(1);
                if (!izraz.ime.equals("<izraz>")) {
                    System.err.println("Neispravno dijete cvora <primarni_izraz>: " + izraz.ime + " umjesto <izraz>");
                    System.exit(1);
                }
                if (!znak.djeca.get(2).ime.equals("D_ZAGRADA")) {
                    System.err.println("Neispravno dijete cvora <primarni_izraz>: " + znak.djeca.get(2).ime + " umjesto D_ZAGRADA");
                    System.exit(1);
                }
                if (!Izraz.obradi(izraz)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(znak.djeca.get(1).deklaracija);
                break;
            default:
                System.err.println("Neispravan broj djece cvora <primarni_izraz>: " + znak.djeca.size() + " umjesto 1 ili 3");
                System.exit(1);
        }
        return true;
    }
}
