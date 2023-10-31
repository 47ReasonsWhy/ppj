import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GLA {
    /* debug */
    /*private static void getAllFiles(File curDir) {
        File[] filesList = curDir.listFiles();
        if (filesList == null) {
            System.err.println("filesList is null");
        } else {
            for (File f : filesList) {
                if (f.isDirectory()) {
                    System.err.println("In directory " + f.getName() + ":");
                    getAllFiles(f);
                    System.err.println("Done with directory " + f.getName() + ".");
                } else if (f.isFile()) {
                    System.err.println(f.getName());
                }
            }
        }
    }*/

    // generate analizator
    private static void generateAnalizator(List<String> tablica) {
        File file = new File("analizator/LA.java");
        try {
            if (!file.createNewFile()) {
                System.err.println("LA.java already exists.");
            }
            /* debug System.err.println("LA.java created."); */
        } catch (Exception e) {
            e.printStackTrace();
        }

        // read LA.txt and copy to LA.java
        List<String> lines;
        try {
            lines = Files.readAllLines(new File("LA.txt").toPath());
            /* debug System.err.println("LA.txt read."); */
        } catch (Exception e) {
            throw new RuntimeException("Error reading LA.txt");
        }

        // write LA.txt to LA.java
        writeToFile(lines, file);

        // write tablica to tablica.txt
        file = new File("analizator/tablica.txt");
        try {
            if (!file.createNewFile()) {
                System.err.println("tablica.txt already exists.");
            }
            /* debug System.err.println("tablica.txt created."); */
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeToFile(tablica, file);
        /* debug System.err.println("tablica.txt written."); */

        // now do the same with Automat
        file = new File("analizator/Automat.java");
        try {
            if (!file.createNewFile()) {
                System.err.println("Automat.java already exists.");
            }
            /* debug System.err.println("Automat.java created."); */
        } catch (Exception e) {
            e.printStackTrace();
        }
        // read Automat.txt
        try {
            lines = Files.readAllLines(new File("Automat.txt").toPath());
            /* debug System.err.println("Automat.txt read."); */
        } catch (Exception e) {
            throw new RuntimeException("Error reading Automat.txt");
        }
        // write Automat.txt to Automat.java
        writeToFile(lines, file);
        /* debug System.err.println("Automat.txt written."); */

        // compile
        /*try {
            Process process = Runtime.getRuntime().exec("javac analizator/LA.java");
            process.waitFor();
            *//* debug *//* System.err.println("javac analizator/LA.java executed.");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /* debug: list all files in current directory */
        /*File curDir = new File(".");
        System.err.println("Files in current directory: ");
        getAllFiles(curDir);*/
    }

    private static void writeToFile(List<String> tablica, File file) {
        FileWriter fw;
        try {
            fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String line : tablica) {
                bw.write(line + "\n");
            }
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void derefRegDefs(Map<String, String> refRegDefs) {
        Set<String> refs = refRegDefs.keySet();
        for (String ref1 : refs) {
            for (String ref2 : refs) {
                if (refRegDefs.get(ref1).contains(ref2)) {
                    String newDef1 = refRegDefs.get(ref1).replaceAll(
                            Pattern.quote(ref2),
                            Matcher.quoteReplacement("(" + refRegDefs.get(ref2) + ")")
                    );
                    refRegDefs.put(ref1, newDef1);
                }
            }
        }
    }

    private static Boolean je_operator(String izraz, Integer i) {
        int br = 0;
        while (i - 1 >= 0 && izraz.charAt(i - 1) == '\\') {
            br++;
            i--;
        }
        return br%2 == 0;
    }

    private static ParStanja pretvori(String izraz, Automat automat) {

        int lijevo_stanje = automat.novo_stanje();
        int desno_stanje = automat.novo_stanje();

        List<String> izbori = new LinkedList<>();
        int br_zagrada = 0;
        for (int i = 0; i < izraz.length(); i++) {
            if (izraz.charAt(i) == '(' && je_operator(izraz, i)) {
                br_zagrada++;
            } else if (izraz.charAt(i) == ')' && je_operator(izraz, i)) {
                br_zagrada--;
            } else if (izraz.charAt(i) == '|' && je_operator(izraz, i) && br_zagrada == 0) {
                izbori.add(izraz.substring(0, i));
                izraz = izraz.substring(i + 1);
                i = -1;
            }
        }

        if (izbori.size() > 0) {
            izbori.add(izraz);
            for (String s : izbori) {
                ParStanja privremeno = pretvori(s, automat);
                automat.dodaj_epsilon_prijelaz(lijevo_stanje, privremeno.lijevo_stanje);
                automat.dodaj_epsilon_prijelaz(privremeno.desno_stanje, desno_stanje);
            }
        } else {
            boolean prefiksirano = false;
            int zadnje_stanje = lijevo_stanje;
            for (int i = 0; i < izraz.length(); i++) {
                int a, b;
                if (prefiksirano) { // slucaj 1
                    prefiksirano = false;
                    char prijelazni_znak;
                    if (izraz.charAt(i) == 't') {
                        prijelazni_znak = '\t';
                    } else if (izraz.charAt(i) == 'n') {
                        prijelazni_znak = '\n';
                    } else if (izraz.charAt(i) == '_') {
                        prijelazni_znak = ' ';
                    } else {
                        prijelazni_znak = izraz.charAt(i);
                    }
                    a = automat.novo_stanje();
                    b = automat.novo_stanje();
                    automat.dodaj_prijelaz(a, b, prijelazni_znak);
                } else { // slucaj 2
                    if (izraz.charAt(i) == '\\') {
                        prefiksirano = true;
                        continue;
                    }
                    if (izraz.charAt(i) != '(') { // slucaj 2a
                        a = automat.novo_stanje();
                        b = automat.novo_stanje();
                        if (izraz.charAt(i) == '$') {
                            automat.dodaj_epsilon_prijelaz(a, b);
                        } else {
                            automat.dodaj_prijelaz(a, b, izraz.charAt(i));
                        }
                    } else { // slucaj 2b
                        int j = i + 1;
                        br_zagrada = 1;
                        while (j < izraz.length() && br_zagrada > 0) {
                            if (izraz.charAt(j) == '(' && je_operator(izraz, j)) {
                                br_zagrada++;
                            } else if (izraz.charAt(j) == ')' && je_operator(izraz, j)) {
                                br_zagrada--;
                            }
                            j++;
                        }
                        j--;
                        ParStanja privremeno = pretvori(izraz.substring(i + 1, j), automat);
                        a = privremeno.lijevo_stanje;
                        b = privremeno.desno_stanje;
                        i = j;
                    }
                }

                // provjera ponavljanja
                if (i + 1 < izraz.length() && izraz.charAt(i + 1) == '*') {
                    int x = a;
                    int y = b;
                    a = automat.novo_stanje();
                    b = automat.novo_stanje();
                    automat.dodaj_epsilon_prijelaz(a, x);
                    automat.dodaj_epsilon_prijelaz(y, b);
                    automat.dodaj_epsilon_prijelaz(a, b);
                    automat.dodaj_epsilon_prijelaz(y, x);
                    i++;
                }

                // povezivanje s prethodnim podizrazom
                automat.dodaj_epsilon_prijelaz(zadnje_stanje, a);
                zadnje_stanje = b;
            }
            automat.dodaj_epsilon_prijelaz(zadnje_stanje, desno_stanje);
        }

        return new ParStanja(lijevo_stanje, desno_stanje);
    }

    public static void main(String[] args) {
        // read input
        Scanner scanner = new Scanner(System.in);
        LinkedList<String> input = new LinkedList<>();
        while (scanner.hasNextLine()) input.add(scanner.nextLine());

        // init
        Map<String, String> refRegDefs = new LinkedHashMap<>();

        // parse input
        while (input.getFirst().charAt(0) == '{') {
            String refRegDef = input.removeFirst();
            String ref = refRegDef.split(" ")[0];
            String regDef = refRegDef.split(" ")[1];
            refRegDefs.put(ref, regDef);
        }
        derefRegDefs(refRegDefs);

        List<String> stanja = Arrays.asList(
                Arrays.stream(input
                        .removeFirst()
                        .split(" "))
                        .skip(1) // %X
                        .toArray(String[]::new)
        );

        List<String> leksickeJedinke = Arrays.asList(
                Arrays.stream(input
                        .removeFirst()
                        .split(" "))
                        .skip(1) // %L
                        .toArray(String[]::new)
        );

        Map<String, Automat> automati = new TreeMap<>();

        // gradnja automata
        while (input.size() > 0) {
            // dodaj regex u automat za trenutno stanje
            String stanjeIRegex = input.removeFirst();
            int delimiter = stanjeIRegex.indexOf(">");
            String stanje = stanjeIRegex.substring(1, delimiter);
            String regex = stanjeIRegex.substring(delimiter + 1);
            for (String ref : refRegDefs.keySet()) {
                regex = regex.replaceAll(
                        Pattern.quote(ref),
                        Matcher.quoteReplacement("(" + refRegDefs.get(ref) + ")")
                );
            }
            if (!automati.containsKey(stanje)) {
                automati.put(stanje, new Automat());
            }
            Automat automat = automati.get(stanje);
            ParStanja parStanja = pretvori(regex, automat);
            automat.dodaj_epsilon_prijelaz(automat.pocetno_stanje, parStanja.lijevo_stanje);
            automat.prihvatljiva_stanja.put(parStanja.desno_stanje, new LinkedList<>());

            // dodatne akcije parsirat ce sam LA
            input.removeFirst();
            String line = input.removeFirst();
            while (!line.equals("}")) {
                automat.prihvatljiva_stanja.get(parStanja.desno_stanje).add(line);
                line = input.removeFirst();
            }
        }

        List<String> output = new LinkedList<>();
        output.add(stanja.toString());
        output.add(leksickeJedinke.toString());
        for (var automat : automati.keySet()) {
            output.add(automat + ":");
            output.add(automati.get(automat).toString());
        }

        generateAnalizator(output);
    }
}
