package izrazi;

import znakovi.Deklaracija;
import znakovi.Znak;

public class SpecifikatorTipa {
    public static boolean obradi(Znak znak) {
        if (!znak.ime.equals("<specifikator_tipa>")) {
            System.err.println("Pokrenuta obrada pogresnog cvora: " + znak.ime + " umjesto <specifikator_tipa>");
            System.exit(1);
        }
        if (znak.djeca.size() != 1) {
            System.err.println("Neispravan broj djece cvora <specifikator_tipa>: " + znak.djeca.size() + " umjesto 1");
            System.exit(1);
        }
        Znak dijete = znak.djeca.get(0);
        switch (dijete.ime) {
            case "KR_VOID":
                znak.deklaracija = new Deklaracija("void", false);
                break;
            case "KR_CHAR":
                znak.deklaracija = new Deklaracija("char", false);
                break;
            case "KR_INT":
                znak.deklaracija = new Deklaracija("int", false);
                break;
            default:
                System.err.println("Neispravno dijete cvora <specifikator_tipa>: " + dijete.ime + " umjesto KR_VOID, KR_CHAR ili KR_INT");
                System.exit(1);
        }
        return true;
    }
}
