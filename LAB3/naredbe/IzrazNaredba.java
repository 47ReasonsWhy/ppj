package naredbe;

import izrazi.Izraz;
import znakovi.Deklaracija;
import znakovi.Znak;

public class IzrazNaredba {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<izraz_naredba>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <izraz_naredba>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                if (!znak.djeca.get(0).ime.equals("TOCKAZAREZ")) {
                    System.err.println("Neispravno dijete cvora <izraz_naredba>: " + znak.djeca.get(0).ime + " umjesto TOCKAZAREZ");
                    System.exit(1);
                }
                znak.deklaracija = new Deklaracija("int", false);
                break;
            case 2:
                Znak izraz = znak.djeca.get(0);
                if (!izraz.ime.equals("<izraz>")) {
                    System.err.println("Neispravno dijete cvora <izraz_naredba>: " + izraz.ime + " umjesto <izraz>");
                    System.exit(1);
                }
                if (!znak.djeca.get(1).ime.equals("TOCKAZAREZ")) {
                    System.err.println("Neispravno dijete cvora <izraz_naredba>: " + znak.djeca.get(1).ime + " umjesto TOCKAZAREZ");
                    System.exit(1);
                }
                if (!Izraz.obradi(izraz)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(izraz.deklaracija.tip, false);
                break;
            default:
                System.err.println("Neispravan broj djece cvora <lista_argumenata>: " + znak.djeca.size() + " umjesto 1 ili 2");
                System.exit(1);
        }
        return true;
    }
}
