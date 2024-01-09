import naredbe.PrijevodnaJedinica;
import util.Util;
import znakovi.Deklaracija;
import znakovi.Tablice;
import znakovi.Zavrsnost;
import znakovi.Znak;

import java.util.*;

public class GeneratorKoda {
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
        korijen.tablice = new Tablice(new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), null);
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
                znak.tablice = new Tablice(new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>(), trenutniRoditelj.tablice);
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

        // generiraj kod
        System.out.println("\t\t\tMOVE\t40000, R7");

        if (!korijen.tablice.kodGlobalnihInicijalizacija.isEmpty()) {
            System.out.println();
        }
        for (String s : korijen.tablice.kodGlobalnihInicijalizacija) {
            System.out.println(s);
        }

        System.out.println("\n\t\t\tCALL\tF_MAIN");
        System.out.println("\t\t\tHALT");
        for (String s : korijen.tablice.generiraniKod) {
            System.out.println(s);
        }
        if (!korijen.tablice.kodGlobalnihDeklaracija.isEmpty()) {
            System.out.println();
        }
        for (String s : korijen.tablice.kodGlobalnihDeklaracija) {
            System.out.println(s);
        }

        if (korijen.tablice.usingMUL) {
            mul.forEach(System.out::println);
        }
        if (korijen.tablice.usingDIV) {
            div.forEach(System.out::println);
        }
        if (korijen.tablice.usingMOD) {
            mod.forEach(System.out::println);
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

    private final static List<String> mul = List.of(
            "MUL\t\t\tADD\t\tR7, %D 4, R7",
            "\t\t\tPOP\t\tR2",
            "\t\t\tPOP\t\tR1",
            "\t\t\tSUB\t\tR7, %D 12, R7",

            "\t\t\tMOVE\t%D 0, R0",
            "\t\t\tSUB\t\tR0, %D 1, R0",
            "\t\t\tMOVE\t%D 1, R6",
            "\t\t\tROTR\tR6, %D 1, R6",

            "\t\t\tAND\t\tR1, R6, R3",
            "\t\t\tJP_Z\tMUL_1",
            "\t\t\tXOR\t\tR1, R0, R1",
            "\t\t\tADD\t\tR1, %D 1, R1",
            "MUL_1\t\tAND\t\tR2, R6, R4",
            "\t\t\tJP_Z\tMUL_2",
            "\t\t\tXOR\t\tR2, R0, R2",
            "\t\t\tADD\t\tR2, %D 1, R2",
            "MUL_2\t\tMOVE\t%D 0, R6",

            "\t\t\tCMP\t\tR1, 0",
            "\t\t\tJP_Z\tMUL_END",
            "\t\t\tCMP\t\tR2, 0",
            "\t\t\tJP_Z\tMUL_END",

            "MUL_LOOP\tADD\t\tR6, R2, R6",
            "\t\t\tSUB\t\tR1, %D 1, R1",
            "\t\t\tJP_NZ\tMUL_LOOP",

            "\t\t\tCMP\t\tR3, R4",
            "\t\t\tJP_Z\tMUL_END",
            "\t\t\tXOR\t\tR6, R0, R6",
            "\t\t\tADD\t\tR6, %D 1, R6",

            "MUL_END\t\tRET"
    );

    private final static List<String> div = List.of(
            "DIV\t\t\tADD\t\tR7, %D 4, R7",
            "\t\t\tPOP\t\tR2",
            "\t\t\tPOP\t\tR1",
            "\t\t\tSUB\t\tR7, %D 12, R7",

            "\t\t\tMOVE\t%D 0, R0",
            "\t\t\tSUB\t\tR0, %D 1, R0",
            "\t\t\tMOVE\t%D 1, R6",
            "\t\t\tROTR\tR6, %D 1, R6",

            "\t\t\tAND\t\tR1, R6, R3",
            "\t\t\tJP_Z\tDIV_1",
            "\t\t\tXOR\t\tR1, R0, R1",
            "\t\t\tADD\t\tR1, %D 1, R1",
            "DIV_1\t\tAND\t\tR2, R6, R4",
            "\t\t\tJP_Z\tDIV_2",
            "\t\t\tXOR\t\tR2, R0, R2",
            "\t\t\tADD\t\tR2, %D 1, R2",
            "DIV_2\t\tMOVE\t%D 0, R6",

            "\t\t\tCMP\t\tR1, R2",
            "\t\t\tJP_N\tDIV_END",
            "\t\t\tCMP\t\tR2, 0",
            "\t\t\tJP_Z\tDIV_END",

            "DIV_LOOP\tADD\t\tR6, %D 1, R6",
            "\t\t\tSUB\t\tR1, R2, R1",
            "\t\t\tCMP\t\tR1, R2",
            "\t\t\tJP_P\tDIV_LOOP",

            "\t\t\tCMP\t\tR3, R4",
            "\t\t\tJP_Z\tDIV_END",
            "\t\t\tXOR\t\tR6, R0, R6",
            "\t\t\tADD\t\tR6, %D 1, R6",

            "DIV_END\t\tRET"
    );

    private final static List<String> mod = List.of(
            "MOD\t\t\tADD\t\tR7, %D 4, R7",
            "\t\t\tPOP\t\tR2",
            "\t\t\tPOP\t\tR1",
            "\t\t\tSUB\t\tR7, %D 12, R7",

            "\t\t\tMOVE\t%D 0, R0",
            "\t\t\tSUB\t\tR0, %D 1, R0",
            "\t\t\tMOVE\t%D 1, R6",
            "\t\t\tROTR\tR6, %D 1, R6",

            "\t\t\tAND\t\tR1, R6, R3",
            "\t\t\tJP_Z\tMOD_1",
            "\t\t\tXOR\t\tR1, R0, R1",
            "\t\t\tADD\t\tR1, %D 1, R1",
            "MOD_1\t\tAND\t\tR2, R6, R4",
            "\t\t\tJP_Z\tMOD_2",
            "\t\t\tXOR\t\tR2, R0, R2",
            "\t\t\tADD\t\tR2, %D 1, R2",
            "MOD_2\t\tMOVE\t%D 0, R6",

            "\t\t\tCMP\t\tR1, R2",
            "\t\t\tJP_N\tMOD_END",
            "\t\t\tCMP\t\tR2, 0",
            "\t\t\tJP_Z\tMOD_END",

            "MOD_LOOP\tSUB\t\tR1, R2, R1",
            "\t\t\tCMP\t\tR1, R2",
            "\t\t\tJP_P\tMOD_LOOP",

            "\t\t\tCMP\t\tR3, R4",
            "\t\t\tJP_Z\tMOD_END",
            "\t\t\tXOR\t\tR1, R0, R1",
            "\t\t\tADD\t\tR1, %D 1, R1",

            "MOD_END\t\tMOVE\tR1, R6",
            "\t\t\tRET"
    );
}
