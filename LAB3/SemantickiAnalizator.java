import naredbe.PrijevodnaJedinica;
import util.Util;
import znakovi.Deklaracija;
import znakovi.Tablice;
import znakovi.Zavrsnost;
import znakovi.Znak;

import java.util.*;

public class SemantickiAnalizator {
    public static void main(String[] args) {
        // imamo garanciju da krecemo sa <prijevodna_jedinica>
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) {
            System.err.println("Neispravan ulaz");
            System.exit(1);
        }
        String line = sc.nextLine();
        if (!line.equals("<prijevodna_jedinica>")) {
            System.err.println("Neispravan ulaz");
            System.exit(1);
        }
        Znak korijen = new Znak(line, Zavrsnost.NEZAVRSNI, null);
        korijen.tablice = new Tablice(new LinkedHashMap<>(), new LinkedHashMap<>(), null);
        Znak trenutniRoditelj = korijen;
        int trenutnaRazina = 1;
        int lineNumber = 1;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            lineNumber++;
            // pronadji odgovarajuceg roditelja
            int novaRazina = 0;
            while (line.startsWith(" ")) {
                line = line.substring(1);
                novaRazina++;
            }
            if (novaRazina > trenutnaRazina + 1) {
                System.err.println("Neispravan ulaz na liniji " + lineNumber);
                System.exit(1);
            } else if (novaRazina == trenutnaRazina + 1) {
                if (trenutniRoditelj == null || trenutniRoditelj.djeca.isEmpty()) {
                    System.err.println("Neispravan ulaz na liniji " + lineNumber);
                    System.exit(1);
                }
                trenutniRoditelj = trenutniRoditelj.djeca.getLast();
                if (trenutniRoditelj.zavrsnost != Zavrsnost.NEZAVRSNI) {
                    System.err.println("Neispravan ulaz na liniji " + lineNumber);
                    System.exit(1);
                }
                trenutnaRazina++;
            }
            while (novaRazina < trenutnaRazina) {
                trenutniRoditelj = trenutniRoditelj.roditelj;
                if (trenutniRoditelj == null) {
                    System.err.println("Neispravan ulaz na liniji " + lineNumber);
                    System.exit(1);
                }
                trenutnaRazina--;
            }
            // dodaj novi znak
            Znak znak = new Znak(line, line.startsWith("<") ? Zavrsnost.NEZAVRSNI : Zavrsnost.ZAVRSNI, trenutniRoditelj);
            if (znak.ime.equals("<slozena_naredba>")) {
                znak.tablice = new Tablice(new LinkedHashMap<>(), new LinkedHashMap<>(), trenutniRoditelj.tablice);
            } else {
                znak.tablice = trenutniRoditelj.tablice;
            }
            trenutniRoditelj.dodajDijete(znak);
        }

        // provjeri da li je sve u redu
        // printTree(korijen);

        // pokreni semanticku analizu
        if (!korijen.ime.equals("<prijevodna_jedinica>")) {
            System.err.println("Korijenski cvor mora biti <prijevodna_jedinica>");
            System.exit(1);
        }

        boolean tocno = PrijevodnaJedinica.obradi(korijen);
        if (!tocno) {
            return;
        }
        Deklaracija main = Util.pronadjiDeklaraciju(korijen, "main");
        if (main == null || !main.tip.equals("funkcija(void -> int)")) {
            System.out.println("main");
            return;
        }
        Map<String, String> deklariraneFunkcije = new LinkedHashMap<>();
        LinkedList<Tablice> stog = new LinkedList<>();
        stog.add(korijen.tablice);
        while (!stog.isEmpty()) {
            Tablice tablice = stog.removeFirst();
            for (String idn : tablice.tablicaDeklaracija.keySet()) {
                Deklaracija deklaracija = tablice.tablicaDeklaracija.get(idn);
                if (deklaracija.tip.startsWith("funkcija(")) {
                    deklariraneFunkcije.put(idn, deklaracija.tip);
                }
            }
            stog.addAll(tablice.tabliceUgnjezdjenihBlokova);
        }

        for (String idn : deklariraneFunkcije.keySet()) {
            if (!korijen.tablice.tablicaDefiniranihFunkcija.containsKey(idn) ||
                !korijen.tablice.tablicaDefiniranihFunkcija.get(idn).equals(deklariraneFunkcije.get(idn))) {
                System.out.println("funkcija");
                return;
            }
        }
    }

    private static void printTree(Znak korijen) {
        LinkedList<Znak> stog = new LinkedList<>();
        stog.add(korijen);
        while (!stog.isEmpty()) {
            Znak trenutni = stog.removeFirst();
            int trenutnaRazina = 0;
            Znak trenutniRoditelj = trenutni.roditelj;
            while (trenutniRoditelj != null) {
                trenutnaRazina++;
                trenutniRoditelj = trenutniRoditelj.roditelj;
            }
            System.out.print(" ".repeat(trenutnaRazina));
            System.out.println(trenutni.ime);
            if (trenutni.zavrsnost == Zavrsnost.NEZAVRSNI && !trenutni.djeca.isEmpty()) {
                LinkedList<Znak> djecaReversed = new LinkedList<>(trenutni.djeca);
                while (!djecaReversed.isEmpty()) {
                    stog.add(djecaReversed.removeLast());
                }
            }
        }
    }
}
