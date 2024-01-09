package deklaracije_i_definicije;

import znakovi.Deklaracija;
import znakovi.Tablice;
import znakovi.Znak;

import java.util.List;

import static util.Util.ispisiGresku;

public class IzravniDeklarator {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<izravni_deklarator>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <izravni_deklarator>");
            System.exit(1);
        }
        Znak idn;
        String ntip, tip;
        Deklaracija deklaracija;
        switch (znak.djeca.size()) {
            case 1:
                idn = znak.djeca.get(0);
                if (!idn.ime.equals("IDN")) {
                    System.err.println("Neispravno ime djeteta cvora <izravni_deklarator>: " + idn.ime + " umjesto IDN");
                    System.exit(1);
                }
                if (znak.deklaracija.tip.equals("void")) {
                    ispisiGresku(znak);
                    return false;
                }
                if (znak.tablice.tablicaDeklaracija.getOrDefault(idn.jedinka, null) != null) {
                    ispisiGresku(znak);
                    return false;
                }

                znak.tablice.tablicaDeklaracija.put(idn.jedinka, new Deklaracija(znak.deklaracija.tip, List.of("char", "int").contains(znak.deklaracija.tip)));
                znak.tablice.tablicaIndeksaVarijabli.put(idn.jedinka, Tablice.varCounter.intValue());

                znak.tablice.kodGlobalnihDeklaracija.add("G_" + String.format("%04X", (Tablice.varCounter++).intValue())  + "\t\tDW\t\t0");
                break;
            case 4:
                idn = znak.djeca.get(0);
                if (!idn.ime.equals("IDN")) {
                    System.err.println("Neispravno ime djeteta cvora <izravni_deklarator>: " + idn.ime + " umjesto IDN");
                    System.exit(1);
                }
                switch (znak.djeca.get(2).ime) {
                    case "BROJ":
                        if (!znak.djeca.get(1).ime.equals("L_UGL_ZAGRADA")) {
                            System.err.println("Neispravno ime djeteta cvora <izravni_deklarator>: " + znak.djeca.get(1).ime + " umjesto L_UGL_ZAGRADA");
                            System.exit(1);
                        }
                        if (!znak.djeca.get(3).ime.equals("D_UGL_ZAGRADA")) {
                            System.err.println("Neispravno ime djeteta cvora <izravni_deklarator>: " + znak.djeca.get(3).ime + " umjesto D_UGL_ZAGRADA");
                            System.exit(1);
                        }
                        if (znak.deklaracija.tip.equals("void")) {
                            ispisiGresku(znak);
                            return false;
                        }
                        if (znak.tablice.tablicaDeklaracija.getOrDefault(idn.jedinka, null) != null) {
                            ispisiGresku(znak);
                            return false;
                        }
                        Znak broj = znak.djeca.get(2);
                        znak.deklaracija = new Deklaracija("niz(" + znak.deklaracija.tip + ")", false);
                        znak.br_elem = Integer.parseInt(broj.jedinka);
                        if (znak.br_elem <= 0 || znak.br_elem > 1024) {
                            ispisiGresku(znak);
                            return false;
                        }
                        znak.tablice.tablicaDeklaracija.put(idn.jedinka, znak.deklaracija);
                        znak.tablice.tablicaIndeksaVarijabli.put(idn.jedinka, (Tablice.varCounter++).intValue());
                        break;
                    case "KR_VOID":
                        if (!znak.djeca.get(1).ime.equals("L_ZAGRADA")) {
                            System.err.println("Neispravno ime djeteta cvora <izravni_deklarator>: " + znak.djeca.get(1).ime + " umjesto L_ZAGRADA");
                            System.exit(1);
                        }
                        if (!znak.djeca.get(3).ime.equals("D_ZAGRADA")) {
                            System.err.println("Neispravno ime djeteta cvora <izravni_deklarator>: " + znak.djeca.get(3).ime + " umjesto D_ZAGRADA");
                            System.exit(1);
                        }
                        ntip = znak.deklaracija.tip;
                        tip = "funkcija(void -> " + ntip + ")";
                        deklaracija = znak.tablice.tablicaDeklaracija.getOrDefault(idn.jedinka, null);
                        if (deklaracija != null && !deklaracija.tip.equals(tip)) {
                                ispisiGresku(znak);
                                return false;
                        }
                        if (deklaracija == null) {
                            znak.tablice.tablicaDeklaracija.put(idn.jedinka, new Deklaracija(tip, false));
                            znak.tablice.tablicaIndeksaVarijabli.put(idn.jedinka, (Tablice.varCounter++).intValue());
                        }
                        znak.deklaracija = new Deklaracija(tip, false);
                        break;
                    case "<lista_parametara>":
                        if (!znak.djeca.get(1).ime.equals("L_ZAGRADA")) {
                            System.err.println("Neispravno ime djeteta cvora <izravni_deklarator>: " + znak.djeca.get(1).ime + " umjesto L_ZAGRADA");
                            System.exit(1);
                        }
                        Znak listaParametara = znak.djeca.get(2);
                        if (!znak.djeca.get(3).ime.equals("D_ZAGRADA")) {
                            System.err.println("Neispravno ime djeteta cvora <izravni_deklarator>: " + znak.djeca.get(3).ime + " umjesto D_ZAGRADA");
                            System.exit(1);
                        }
                        if (!ListaParametara.obradi(listaParametara)) {
                            return false;
                        }
                        ntip = znak.deklaracija.tip;
                        tip = "funkcija(" + listaParametara.deklaracija.tip + " -> " + ntip + ")";
                        deklaracija = znak.tablice.tablicaDeklaracija.getOrDefault(idn.jedinka, null);
                        if (deklaracija != null && !deklaracija.tip.equals(tip)) {
                            ispisiGresku(znak);
                            return false;
                        }
                        if (deklaracija == null) {
                            znak.tablice.tablicaDeklaracija.put(idn.jedinka, new Deklaracija(tip, false));
                            znak.tablice.tablicaIndeksaVarijabli.put(idn.jedinka, (Tablice.varCounter++).intValue());
                        }
                        break;
                    default:
                        System.err.println("Neispravno ime djeteta cvora <izravni_deklarator>: " + znak.djeca.get(2).ime + " umjesto BROJ, KR_VOID ili <lista_parametara>");
                        System.exit(1);
                }
                break;
            default:
                System.err.println("Neispravan broj djece cvora <izravni_deklarator>: " + znak.djeca.size() + " umjesto 1 ili 4");
                System.exit(1);
                return false;
        }
        znak.jedinka = idn.jedinka;
        return true;
    }
}
