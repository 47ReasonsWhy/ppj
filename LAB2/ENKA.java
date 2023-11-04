import java.util.*;

public class ENKA {
    private final List<String> zavrsniZnakovi;
    private final List<String> nezavrsniZnakovi;
    private final Map<String, Set<String>> skupZapocinjeNZ;
    private final Map<List<String>, Set<String>> skupZapocinjeZaSveProdukcijeISufikse;
    private final Map<String, List<List<String>>> produkcije;
    private final Tablica<Stavka,String,Set<Stavka>> prijelazi;
    private final Stavka pocetnoStanje;

    public ENKA(List<String> nezavrsniZnakovi, List<String> zavrsniZnakovi, Map<String, List<List<String>>> produkcije) {
        this.zavrsniZnakovi = zavrsniZnakovi;
        this.nezavrsniZnakovi = nezavrsniZnakovi;
        this.produkcije = produkcije;
        this.skupZapocinjeNZ = izracunajSkupZapocinjeZaNZ();
        this.skupZapocinjeZaSveProdukcijeISufikse = izracunajSkupZapocinjeZaSveProdukcijeISufikse();

        Map<Stavka,Map<String,Set<Stavka>>> prijelazi = new LinkedHashMap<>();

        // inicijalizirajmo pocetnu stavku (stanje)
        String pocetniNezavrsniZnak = nezavrsniZnakovi.get(0);
        List<String> pocetnaDesnaStrana = produkcije.get(pocetniNezavrsniZnak).get(0);
        pocetnoStanje = new Stavka(pocetniNezavrsniZnak, pocetnaDesnaStrana, 0);
        Set<String> skupSlijedi = new TreeSet<>(Collections.singleton("#"));
        pocetnoStanje.getSkupSlijedi().addAll(skupSlijedi);

        // izracunaj prijelaze
        List<Stavka> stog = new LinkedList<>();
        stog.add(pocetnoStanje);
        while (!stog.isEmpty()) {
            Stavka stavka = stog.remove(0);
            if (stavka.tockaJeNaKraju()) continue;
            String sljedeciZnak = stavka.sljedeciZnak();

            // dodaj prijelaz za sljedeci znak
            Stavka novaStavka = stavka.pomakniTocku();
            inicijalizirajStavku(stavka, sljedeciZnak, prijelazi);
            prijelazi.get(stavka).get(sljedeciZnak).add(novaStavka);
            // ako nove stavke nema u prijelazima, dodaj ju na stog
            if (!prijelazi.containsKey(novaStavka)) stog.add(novaStavka);

            // ako je sljedeci znak nezavrsni, dodaj epsilon prijelaze u stavke obrade tog znaka
            if (nezavrsniZnakovi.contains(sljedeciZnak)) {
                for (List<String> desnaStrana : produkcije.get(sljedeciZnak)) {
                    novaStavka = new Stavka(sljedeciZnak, desnaStrana, 0);
                    // izracunaj skup slijedi za novu stavku
                    Set<String> noviSkupSlijedi;
                    if (stavka.getTocka() == stavka.getDesnaStrana().size() - 1) {
                        noviSkupSlijedi = new TreeSet<>(stavka.getSkupSlijedi());
                    } else {
                        List<String> sufiks = stavka.getDesnaStrana().subList(stavka.getTocka() + 1, stavka.getDesnaStrana().size());
                        noviSkupSlijedi = new TreeSet<>(skupZapocinjeZaSveProdukcijeISufikse.get(sufiks));
                        if (prazniZnakovi().containsAll(sufiks)) {
                            noviSkupSlijedi.addAll(stavka.getSkupSlijedi());
                        }
                    }
                    novaStavka.getSkupSlijedi().addAll(noviSkupSlijedi);
                    // dodaj epsilon prijelaz iz trenutne stavke u novu stavku
                    inicijalizirajStavku(stavka, "$", prijelazi);
                    prijelazi.get(stavka).get("$").add(novaStavka);
                    // ako nove stavke nema u prijelazima, dodaj ju na stog
                    if (!prijelazi.containsKey(novaStavka)) stog.add(novaStavka);
                }
            }
        }

        this.prijelazi = new Tablica<>(prijelazi);
    }

    private void inicijalizirajStavku(Stavka stavka, String znak, Map<Stavka,Map<String,Set<Stavka>>> prijelazi) {
        if (!prijelazi.containsKey(stavka)) prijelazi.put(stavka, new LinkedHashMap<>());
        if (!prijelazi.get(stavka).containsKey(znak)) prijelazi.get(stavka).put(znak, new LinkedHashSet<>());
    }

    private TreeSet<String> prazniZnakovi() {
        TreeSet<String> prazniZnakovi = new TreeSet<>();
        for (String lijevaStrana : produkcije.keySet()) {
            for (List<String> desnaStrana : produkcije.get(lijevaStrana)) {
                if (desnaStrana.size() == 1 && desnaStrana.get(0).equals("$")) {
                    prazniZnakovi.add(lijevaStrana);
                }
            }
        }
        boolean notDone = true;
        while (notDone) {
            TreeSet<String> noviPrazniZnakovi = new TreeSet<>(prazniZnakovi);
            for (String lijevaStrana : produkcije.keySet()) {
                for (List<String> desnaStrana : produkcije.get(lijevaStrana)) {
                    boolean sviPrazni = true;
                    for (String znak : desnaStrana) {
                        if (!prazniZnakovi.contains(znak)) {
                            sviPrazni = false;
                            break;
                        }
                    }
                    if (sviPrazni) noviPrazniZnakovi.add(lijevaStrana);
                }
            }
            if (noviPrazniZnakovi.equals(prazniZnakovi)) notDone = false;
            else prazniZnakovi = noviPrazniZnakovi;
        }
        return prazniZnakovi;
    }

    private Tablica<String, String, Boolean> zapocinjeIzravnoZnakom() {
        // inicijaliziraj
        Map<String, Map<String, Boolean>> zapocinjeIzravnoZnakom = new LinkedHashMap<>();
        List<String> sviZnakovi = new LinkedList<>();
        sviZnakovi.addAll(zavrsniZnakovi);
        sviZnakovi.addAll(nezavrsniZnakovi);
        TreeSet<String> prazniZnakovi = prazniZnakovi();
        // dodaj prijelaze
        for (String Z1 : sviZnakovi) {
            zapocinjeIzravnoZnakom.put(Z1, new LinkedHashMap<>());
            for (String Z2 : sviZnakovi) {
                zapocinjeIzravnoZnakom.get(Z1).put(Z2, false);
            }
        }
        // oznaci kao true sve prijelaze koji su izravni
        for (String NZ : nezavrsniZnakovi) {
            for (List<String> desnaStrana : produkcije.get(NZ)) {
                for (int i = 0; i < desnaStrana.size(); i++) {
                    if (i == 0) {
                        String znak = desnaStrana.get(0);
                        if (znak.equals("$")) continue;
                        zapocinjeIzravnoZnakom.get(NZ).put(desnaStrana.get(i), true);
                    } else {
                        if (prazniZnakovi.contains(desnaStrana.get(i - 1))) {
                            zapocinjeIzravnoZnakom.get(NZ).put(desnaStrana.get(i), true);
                        } else break;
                    }
                }
            }
        }
        return new Tablica<>(zapocinjeIzravnoZnakom);
    }

    private Tablica<String, String, Boolean> zapocinjeZnakom() {
        Set<String> sviZnakovi = new LinkedHashSet<>();
        sviZnakovi.addAll(zavrsniZnakovi);
        sviZnakovi.addAll(nezavrsniZnakovi);
        Graph<String> graf = new Graph<>(sviZnakovi);
        Tablica<String, String, Boolean> zapocinjeIzravnoZnakom = zapocinjeIzravnoZnakom();
        for (String NZ : nezavrsniZnakovi) {
            for (String Z : sviZnakovi) {
                if (zapocinjeIzravnoZnakom.get(NZ, Z)) {
                    graf.addEdge(NZ, Z);
                }
            }
        }
        // izracunaj refleksivno tranzitivno okruzenje relacije zapocinjeIzravnoZnakom
        for (String Z : sviZnakovi) {
            zapocinjeIzravnoZnakom.set(Z, Z, true);
        }
        // BFS za svaki znak i azuriraj tablicu zapocinjeIzravnoZnakom
        // ona nam vise ne treba pa cemo jednostavnu nju azuriranu vratiti kao novu tablicu
        for (String Z1 : sviZnakovi) {
            Map<String, Boolean> visited = graf.BFS(Z1);
            for (String Z2 : sviZnakovi) {
                if (visited.get(Z2)) {
                    zapocinjeIzravnoZnakom.set(Z1, Z2, true);
                }
            }
        }

        return zapocinjeIzravnoZnakom;
    }

    private Map<String, Set<String>> izracunajSkupZapocinjeZaNZ() {
        Map<String, Set<String>> zapocinjeNZ = new LinkedHashMap<>();
        // init
        for (String NZ : nezavrsniZnakovi) {
            zapocinjeNZ.put(NZ, new LinkedHashSet<>());
        }
        Tablica<String, String, Boolean> zapocinjeZnakom = zapocinjeZnakom();
        // izracunaj zapocinjeNZ
        for (String NZ : nezavrsniZnakovi) {
            for (String Z : zavrsniZnakovi) {
                if (zapocinjeZnakom.get(NZ, Z)) {
                    zapocinjeNZ.get(NZ).add(Z);
                }
            }
        }
        return zapocinjeNZ;
    }

    private Set<String> izracunajSkupZapocinjeZaProdukciju(List<String> desnaStrana) {
        Set<String> skupZapocinje = new LinkedHashSet<>();
        if (desnaStrana.size() == 1 && desnaStrana.get(0).equals("$")) return skupZapocinje;
        for (String znak : desnaStrana) {
            if (zavrsniZnakovi.contains(znak)) {
                skupZapocinje.add(znak);
                break;
            } else {
                skupZapocinje.addAll(skupZapocinjeNZ.get(znak));
                if (!prazniZnakovi().contains(znak)) break;
            }
        }
        return skupZapocinje;
    }

    private Map<List<String>, Set<String>> izracunajSkupZapocinjeZaSveProdukcijeISufikse() {
        Map<List<String>, Set<String>> skupZapocinjeZaSveProdukcije = new LinkedHashMap<>();
        for (String lijevaStrana : produkcije.keySet()) {
            for (List<String> desnaStrana : produkcije.get(lijevaStrana)) {
                if (skupZapocinjeZaSveProdukcije.containsKey(desnaStrana)) continue;
                Set<String> skupZapocinje = new TreeSet<>(izracunajSkupZapocinjeZaProdukciju(desnaStrana));
                if (!skupZapocinje.isEmpty()) {
                    skupZapocinjeZaSveProdukcije.put(desnaStrana, new LinkedHashSet<>(skupZapocinje));
                }
                List<String> sufiks = new LinkedList<>(desnaStrana.subList(1, desnaStrana.size()));
                while (!sufiks.isEmpty()) {
                    if (skupZapocinjeZaSveProdukcije.containsKey(sufiks)) break;
                    skupZapocinje = new TreeSet<>(izracunajSkupZapocinjeZaProdukciju(sufiks));
                    if (!skupZapocinje.isEmpty()) {
                        skupZapocinjeZaSveProdukcije.put(sufiks, new LinkedHashSet<>(skupZapocinje));
                    }
                    sufiks = new LinkedList<>(sufiks.subList(1, sufiks.size()));
                }
            }
        }
        return skupZapocinjeZaSveProdukcije;
    }

    public void ispisiSkupZapocinjeZaNZ() {
        for (String NZ : skupZapocinjeNZ.keySet()) {
            System.out.println(NZ + " => " + skupZapocinjeNZ.get(NZ));
        }
    }

    public void ispisiSkupZapocinjeZaSveProdukcijeISufikse() {
        for (List<String> produkcija : skupZapocinjeZaSveProdukcijeISufikse.keySet()) {
            System.out.println(produkcija + " => " + skupZapocinjeZaSveProdukcijeISufikse.get(produkcija));
        }
    }

    public void ispisiPrijelaze() {
        prijelazi.print();
    }

    public Stavka getPocetnoStanje() {
        return pocetnoStanje;
    }

    public Tablica<Stavka,String,Set<Stavka>> getPrijelazi() {
        return prijelazi;
    }

    public List<String> getZavrsniZnakovi() {
        return zavrsniZnakovi;
    }

    public List<String> getNezavrsniZnakovi() {
        return nezavrsniZnakovi;
    }

    public Map<String, List<List<String>>> getProdukcije() {
        return produkcije;
    }
}
