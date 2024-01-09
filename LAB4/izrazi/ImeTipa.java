package izrazi;

import znakovi.Deklaracija;
import znakovi.Znak;

import static util.Util.ispisiGresku;

public class ImeTipa {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<ime_tipa>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <ime_tipa>");
            System.exit(1);
        }
        switch (znak.djeca.size()) {
            case 1:
                Znak specifikatorTipa = znak.djeca.get(0);
                if (!specifikatorTipa.ime.equals("<specifikator_tipa>")) {
                    System.err.println("Neispravno dijete cvora <ime_tipa>: " + specifikatorTipa.ime + " umjesto <specifikator_tipa>");
                    System.exit(1);
                }
                if (!SpecifikatorTipa.obradi(specifikatorTipa)) {
                    return false;
                }
                znak.deklaracija = new Deklaracija(specifikatorTipa.deklaracija.tip, false);
                break;
            case 2:
                if (!znak.djeca.get(0).ime.equals("KR_CONST")) {
                    System.err.println("Neispravno dijete cvora <ime_tipa>: " + znak.djeca.get(0).ime + " umjesto KR_CONST");
                    System.exit(1);
                }
                Znak specifikatorTipa1 = znak.djeca.get(1);
                if (!specifikatorTipa1.ime.equals("<specifikator_tipa>")) {
                    System.err.println("Neispravno dijete cvora <ime_tipa>: " + specifikatorTipa1.ime + " umjesto <specifikator_tipa>");
                    System.exit(1);
                }
                if (!SpecifikatorTipa.obradi(specifikatorTipa1)) {
                    return false;
                }
                if (specifikatorTipa1.deklaracija.tip.equals("void")) {
                    ispisiGresku(znak);
                    return false;
                }
                znak.deklaracija = new Deklaracija("const(" + specifikatorTipa1.deklaracija.tip + ")", false);
                break;
            default:
                System.err.println("Neispravan broj djece cvora <ime_tipa>: " + znak.djeca.size() + " umjesto 1 ili 2");
                System.exit(1);
        }
        return true;
    }
}
