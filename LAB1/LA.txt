import java.io.File;
import java.nio.file.Files;
import java.util.*;

public class LA {
    private static List<String> parseStringList(String s) {
        List<String> list = new LinkedList<>();
        String[] split = s.substring(1, s.length() - 1).split(", ");
        for (String s1 : split) {
            if (s1.length() > 0) {
                list.add(s1);
            }
        }
        return list;
    }

    private static Map<String, Automat> parseAutomati(List<String> lines) {
        Map<String, Automat> automati = new TreeMap<>();

        while (lines.size() > 0) {
            String stanje = lines.get(0).substring(0, lines.get(0).length() - 1);
            lines.remove(0);
            int br_stanja = Integer.parseInt(lines.remove(0).substring(11));
            int pocetnoStanje = Integer.parseInt(lines.remove(0).substring(16));

            Map<Integer, List<String>> prihvatljiva_stanja = new TreeMap<>();
            String line = lines.remove(0);
            String[] stanjaIAkcije = line.substring(22, line.length() - 1).split(", ");
            for (String stanjeIAkcije : stanjaIAkcije) {
                int prihvatljivoStanje = Integer.parseInt(stanjeIAkcije.split("=")[0]);
                String akcijeSplit = stanjeIAkcije.split("=")[1];
                String[] akcije = akcijeSplit.substring(1, akcijeSplit.length() - 1).split("; ");
                prihvatljiva_stanja.put(prihvatljivoStanje, new LinkedList<>());
                for (String akcija : akcije) {
                    prihvatljiva_stanja.get(prihvatljivoStanje).add(akcija);
                }
            }

            Map<Integer, Map<Character, TreeSet<Integer>>> prijelazi = new TreeMap<>();
            List<String> prijelaziLines = new LinkedList<>();
            while (!lines.get(0).endsWith("}}")) {
                prijelaziLines.add(lines.remove(0));
            }
            prijelaziLines.add(lines.remove(0));
            prijelaziLines.set(0, prijelaziLines.get(0).substring(12));
            prijelaziLines.set(prijelaziLines.size() - 1,
                    prijelaziLines.get(prijelaziLines.size() - 1).substring(0, prijelaziLines.get(prijelaziLines.size() - 1).length() - 3));

            StringBuilder prijelaziLine = new StringBuilder(prijelaziLines.remove(0));
            while (prijelaziLines.size() > 0) {
                prijelaziLine.append("NL").append(prijelaziLines.remove(0));
            }
            String[] split = prijelaziLine.toString().split("\\]\\}, ");
            for (String s : split) {
                int lijevo_stanje = Integer.parseInt(s.split("=\\{")[0]);
                Character znak;
                if (s.split("=\\{")[1].split("->\\[")[0].length() == 1) {
                    znak = s.split("=\\{")[1].charAt(0);
                } else {
                    znak = '\n';
                }
                String desno = s.split("=\\{")[1].split("->\\[")[1];
                String[] desna_stanja_str = desno.split(",");
                prijelazi.put(lijevo_stanje, new TreeMap<>());
                prijelazi.get(lijevo_stanje).put(znak, new TreeSet<>());
                for (String desno_stanje_str : desna_stanja_str) {
                    prijelazi.get(lijevo_stanje).get(znak).add(Integer.parseInt(desno_stanje_str));
                }
            }


            Map<Integer, TreeSet<Integer>> epsilon_prijelazi = new TreeMap<>();
            line = lines.remove(0);
            String[] ss = line.substring(20, line.length() - 1).split(", ");
            for (String s : ss) {
                if (s.length() > 0) {
                    split = s.split("=");
                    int lijevo_stanje = Integer.parseInt(split[0]);
                    String[] desna_stanja = split[1].substring(1, split[1].length() - 1).split(",");
                    epsilon_prijelazi.put(lijevo_stanje, new TreeSet<>());
                    for (String desno_stanje : desna_stanja) {
                        epsilon_prijelazi.get(lijevo_stanje).add(Integer.parseInt(desno_stanje));
                    }
                }
            }

            Automat automat = new Automat();
            automat.br_stanja = br_stanja;
            automat.pocetno_stanje = pocetnoStanje;
            automat.prihvatljiva_stanja = prihvatljiva_stanja;
            automat.prijelazi = prijelazi;
            automat.epsilon_prijelazi = epsilon_prijelazi;

            automati.put(stanje, automat);
        }

        return automati;
    }

    public static void main(String[] args) {

        /* debug System.err.println("LA.java started"); */

        // read tablica.txt and copy to LA.java
        List<String> lines;
        try {
            lines = Files.readAllLines(new File("tablica.txt").toPath());
        } catch (Exception e) {
            throw new RuntimeException("Error reading tablica.txt");
        }

        // debug
        // List<String> output = new LinkedList<>();

        // init
        List<String> stanja = parseStringList(lines.remove(0));
        String trenutnoStanje = stanja.get(0);
        List<String> leksickeJedinke = parseStringList(lines.remove(0));
        Map<String, Automat> automati = parseAutomati(lines);
        Automat automat = automati.get(trenutnoStanje);
        TreeSet<Integer> automatTrenutnaStanja = new TreeSet<>(Collections.singleton(automat.pocetno_stanje));
        automatTrenutnaStanja = SimEnka.calcEpsEnv(automat.epsilon_prijelazi, automatTrenutnaStanja);

        int redak = 1;
        StringBuilder procitano = new StringBuilder();
        StringBuilder lexJedinka = new StringBuilder();
        String lexKlasa = "";
        List<String> dodatneAkcije = new LinkedList<>();
        boolean josJednom = true;   // zubima se za me drzii
        // prvi puuut da nekoog voliim
        // PRVI PUUT AA VEEC SE BOOJIIIIIMM

        // citaj ulaznu datoteku
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine() || josJednom) {
            StringBuilder line = new StringBuilder();
            if (!scanner.hasNextLine()) {
                josJednom = false;
            } else {
                line = new StringBuilder(scanner.nextLine());
            }

            boolean odiUNoviRedak = false;
            while (!odiUNoviRedak) {

                // jesmo li u nekom prihvatljivom stanju?
                TreeSet<Integer> prihvatljivaStanja = new TreeSet<>();
                for (Integer automatTrenutnoStanje : automatTrenutnaStanja) {
                    if (automat.prihvatljiva_stanja.containsKey(automatTrenutnoStanje)) {
                        prihvatljivaStanja.add(automatTrenutnoStanje);
                    }
                }

                // ako smo u barem jednom prihvatljivom stanju,
                // dohvati leksicku klasu i dodatne akcije i zapamti njih te leksicku jedinku koju smo do sada izgradili
                if (prihvatljivaStanja.size() > 0) {
                    Integer prihvatljivoStanje = prihvatljivaStanja.first();
                    lexJedinka = new StringBuilder(procitano.toString());
                    lexKlasa = automat.prihvatljiva_stanja.get(prihvatljivoStanje).get(0);
                    dodatneAkcije = automat.prihvatljiva_stanja.get(prihvatljivoStanje)
                            .subList(1, automat.prihvatljiva_stanja.get(prihvatljivoStanje).size());
                }

                // ako smo u bilo kojem stanju (skup stanja je neprazan), citaj sljedeci znak i izracunaj nova stanja
                if (automatTrenutnaStanja.size() > 0) {
                    Character c;
                    if (line.length() > 0) {
                        c = line.charAt(0);
                        line = new StringBuilder(line.substring(1));
                    } else {
                        c = '\n';
                        odiUNoviRedak = true;
                    }

                    // dodaj procitani znak u procitano
                    procitano.append(c);

                    // debug
                    // System.err.println("Procitani znak: " + c);

                    // izracunaj nova stanja...
                    TreeSet<Integer> automatNovaStanja = new TreeSet<>();
                    for (Integer automatTrenutnoStanje : automatTrenutnaStanja) {
                        if (automat.prijelazi.containsKey(automatTrenutnoStanje) &&
                                automat.prijelazi.get(automatTrenutnoStanje).containsKey(c)) {
                            automatNovaStanja.addAll(automat.prijelazi.get(automatTrenutnoStanje).get(c));
                        }
                    }
                    // ... i njihovo epsilon okruzenje
                    automatTrenutnaStanja = SimEnka.calcEpsEnv(automat.epsilon_prijelazi, automatNovaStanja);

                    // i idemo dalje
                    continue;
                }

                // ako smo u praznom skupu stanja, ne citaj iduci znak vec u ovisnosti o leksickoj klasi odluci sto dalje
                if (lexKlasa.equals("")) { // doslo je do pogreske
                    // ispisi prvi znak procitanog na stderr i nastavi od drugog nadalje
                    System.err.print(procitano.charAt(0));
                    procitano = new StringBuilder(procitano.substring(1));
                } else { // nije doslo do pogreske
                    // napravi dodatne akcije
                    boolean povecajRedak = false;
                    for (String akcija : dodatneAkcije) {
                        String naredba = akcija.split(" ")[0];
                        switch (naredba) {
                            case "UDJI_U_STANJE":
                                trenutnoStanje = akcija.split(" ")[1];
                                automat = automati.get(trenutnoStanje);
                                break;
                            case "VRATI_SE":
                                int n = Integer.parseInt(akcija.split(" ")[1]);
                                lexJedinka = new StringBuilder(lexJedinka.substring(0, n));
                                break;
                            case "NOVI_REDAK":
                                povecajRedak = true;
                                break;
                            default:
                                throw new RuntimeException("Nepoznata akcija: " + naredba);
                        }
                    }

                    if (!lexKlasa.equals("-")) { // ako ne odbacujemo leksicku jedinku
                        // ispisi leksicku klasu, redak i leksicku jedinku na stdout
                        System.out.println(lexKlasa + " " + redak + " " + lexJedinka);
                        // debug: dodaj u output
                        // output.add(lexKlasa + " " + redak + " " + lexJedinka);
                    }

                    if (povecajRedak) {
                        redak++;
                    }
                }
                // vrati u line ostatak procitanog, onaj koji nije bio dio leksicke jedinke
                line.insert(0, procitano.substring(lexJedinka.length()));

                // svakako resetiraj procitano, leksicku jedinku, leksicku klasu i stanje automata
                procitano = new StringBuilder();
                lexJedinka = new StringBuilder();
                lexKlasa = "";
                automatTrenutnaStanja = new TreeSet<>(Collections.singleton(automat.pocetno_stanje));
                automatTrenutnaStanja = SimEnka.calcEpsEnv(automat.epsilon_prijelazi, automatTrenutnaStanja);

                // provjeri jesmo li mozda vratili nesto u liniju dok je bila prazna
                if (line.length() > 0) odiUNoviRedak = false;
            }
        }

        // debug: test if output is equal to expected output
        /*List<String> expectedOutput;
        try {
            expectedOutput = Files.readAllLines(new File("svaki_drugi_a2.out").toPath());
        } catch (Exception e) {
            throw new RuntimeException("Error reading file.out");
        }
        if (output.equals(expectedOutput)) {
            System.err.println("\nOK");
        } else {
            System.err.println("\nNOT OK");
        }*/
    }
}
