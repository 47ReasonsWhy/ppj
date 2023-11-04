import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.*;

public class GSA {
    private static Map<String, List<List<String>>> produkcije;

    public static void main(String[] args) {
        // read input
        Scanner scanner = new Scanner(System.in);
        LinkedList<String> input = new LinkedList<>();
        while (scanner.hasNextLine()) input.add(scanner.nextLine());

        // %V
        List<String> nezavrsniZnakovi = new LinkedList<>(Arrays.asList(
                Arrays.stream(input
                                .removeFirst()
                                .split(" "))
                        .skip(1) // %V
                        .toArray(String[]::new))
        );
        nezavrsniZnakovi.add(0, "<%>");

        // %T
        List<String> zavrsniZnakovi = new LinkedList<>(Arrays.asList(
                Arrays.stream(input
                                .removeFirst()
                                .split(" "))
                        .skip(1) // %T
                        .toArray(String[]::new)
        ));

        // %Syn
        List<String> sinkronizacijskiZnakovi = new LinkedList<>(Arrays.asList(
                Arrays.stream(input
                                .removeFirst()
                                .split(" "))
                        .skip(1) // %Syn
                        .toArray(String[]::new)
        ));

        produkcije = new LinkedHashMap<>();
        Map<List<String>, List<String>> produkcijeInverzno = new LinkedHashMap<>();
        for (String znak : nezavrsniZnakovi) {
            produkcije.put(znak, new LinkedList<>());
        }
        produkcije.get("<%>").add(Collections.singletonList(nezavrsniZnakovi.get(1)));
        produkcijeInverzno.put(Collections.singletonList(nezavrsniZnakovi.get(1)), Collections.singletonList("<%>"));

        while (!input.isEmpty()) {
            String lijevaStrana = input.removeFirst();
            if (!(lijevaStrana.charAt(0) == '<') || !nezavrsniZnakovi.contains(lijevaStrana)) {
                throw new RuntimeException("Neispravan nezavrsni znak s lijeve strane produkcije: " + lijevaStrana);
            }
            while (!input.isEmpty() && input.getFirst().charAt(0) == ' ') {
                List<String> desnaStrana = Arrays.asList(
                        Arrays.stream(input.removeFirst().split(" "))
                                .skip(1)
                                .toArray(String[]::new)
                );
                produkcije.get(lijevaStrana).add(desnaStrana);
                if (!produkcijeInverzno.containsKey(desnaStrana)) {
                    produkcijeInverzno.put(desnaStrana, new LinkedList<>());
                }
                produkcijeInverzno.get(desnaStrana).add(lijevaStrana);
            }
        }

        // inicijalizirajmo ENKA
        ENKA enka = new ENKA(nezavrsniZnakovi, zavrsniZnakovi, produkcije);

        // inicijalizirajmo DKA
        DKA dka = new DKA(enka, produkcijeInverzno);
        Tablica<Integer,String,String> akcije = dka.getAkcije();
        Tablica<Integer,String,Integer> novoStanje = dka.getNovoStanje();

        // dka.ispisiTablice();

        // kopirajmo Tablica.java u analizator/Tablica.java
        File file = new File("analizator/Tablica.java");
        try {
            if (!file.createNewFile()) {
                System.err.println("Tablica.java already exists.");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        List<String> lines;
        try {
            lines = Files.readAllLines(new File("Tablica.txt").toPath());
        } catch (Exception e) {
            throw new RuntimeException("Error reading Tablica.txt");
        }
        writeToFile(lines, file);

        // generirajmo SA.java
        file = new File("analizator/SA.java");
        try {
            if (!file.createNewFile()) {
                System.err.println("SA.java already exists.");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        try {
            lines = Files.readAllLines(new File("SA.txt").toPath());
        } catch (Exception e) {
            throw new RuntimeException("Error reading SA.txt");
        }
        // add tablicaInit to SA.java
        lines.add("\n\tstatic void tabliceInit() {");
        lines.add("\t\t// akcije");
        for (Integer stanje : akcije.getKeys()) {
            for (String znak : akcije.getKeys(stanje)) {
                lines.add("\t\takcije.set(" + stanje + ", \"" + znak + "\", \"" + akcije.get(stanje, znak) + "\");");
            }
        }
        lines.add("\t\t// novoStanje");
        for (Integer stanje : novoStanje.getKeys()) {
            for (String znak : novoStanje.getKeys(stanje)) {
                lines.add("\t\tnovoStanje.set(" + stanje + ", \"" + znak + "\", " + novoStanje.get(stanje, znak) + ");");
            }
        }
        lines.add("\t}");
        lines.add("}");

        writeToFile(lines, file);
    }

    private static void ispisiProdukcije() {
        for (String lijevaStrana : produkcije.keySet()) {
            for (List<String> desnaStrana : produkcije.get(lijevaStrana)) {
                System.out.print(lijevaStrana + " -> ");
                for (String znak : desnaStrana) {
                    System.out.print(znak + " ");
                }
                System.out.println();
            }
        }
    }

    private static void writeToFile(List<String> lines, File file) {
        FileWriter fw;
        try {
            fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String line : lines) {
                bw.write(line + "\n");
            }
            bw.close();
            fw.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
