package naredbe;

import znakovi.Znak;

public class Naredba {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<naredba>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <naredba>");
            System.exit(1);
        }
        if (znak.djeca.size() != 1) {
            System.err.println("Neispravan broj djece cvora <naredba>: " + znak.djeca.size() + " umjesto 1");
            System.exit(1);
        }
        Znak dijete = znak.djeca.get(0);
        switch (dijete.ime) {
            case "<slozena_naredba>":
                if (!SlozenaNaredba.obradi(dijete)) {
                    return false;
                }
                break;
            case "<izraz_naredba>":
                if (!IzrazNaredba.obradi(dijete)) {
                    return false;
                }
                break;
            case "<naredba_grananja>":
                if (!NaredbaGrananja.obradi(dijete)) {
                    return false;
                }
                break;
            case "<naredba_petlje>":
                if (!NaredbaPetlje.obradi(dijete)) {
                    return false;
                }
                break;
            case "<naredba_skoka>":
                if (!NaredbaSkoka.obradi(dijete)) {
                    return false;
                }
                break;
            default:
                System.err.println("Neispravno dijete cvora <naredba>: " + dijete.ime +
                    " umjesto <slozena_naredba>, <izraz_naredba>, <naredba_grananja>, <naredba_petlje> ili <naredba_skoka>");
                System.exit(1);
        }
        return true;
    }
}
