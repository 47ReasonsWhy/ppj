import java.util.*;

public class DKA {
    private final List<String> zavrsniZnakovi;
    private final List<String> nezavrsniZnakovi;
    private final Map<List<String>, List<String>> produkcijeInverzno;
    private final Tablica<Stanje,String,Stanje> prijelazi;
    private final Tablica<Integer,String,String> akcije = new Tablica<>(new LinkedHashMap<>());
    private final Tablica<Integer,String,Integer> novoStanje = new Tablica<>(new LinkedHashMap<>());

    static final class Stanje {
        private final Set<Stavka> stavke;
        private final int n;

        Stanje(Set<Stavka> stavke, int n) {
            this.stavke = stavke;
            this.n = n;
        }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Stanje stanje = (Stanje) o;

                return stavke.equals(stanje.stavke);
            }

            @Override
            public int hashCode() {
                return stavke.hashCode();
            }

        public Set<Stavka> stavke() {
            return stavke;
        }

        public int n() {
            return n;
        }

        @Override
        public String toString() {
            return "Stanje[" +
                    "stavke=" + stavke + ", " +
                    "n=" + n + ']';
        }

    }

    public DKA(ENKA enka, Map<List<String>, List<String>> produkcijeInverzno) {
        this.zavrsniZnakovi = enka.getZavrsniZnakovi();
        this.nezavrsniZnakovi = enka.getNezavrsniZnakovi();
        this.produkcijeInverzno = produkcijeInverzno;

        Map<Stanje,Map<String,Stanje>> prijelazi = new LinkedHashMap<>();
        int n = 0;

        Set<Stavka> pocetneStavke = new LinkedHashSet<>(Set.of(enka.getPocetnoStanje()));
        pocetneStavke = izracunajEpsOkruzenje(pocetneStavke, enka.getPrijelazi());
        Stanje pocetnoStanje = new Stanje(pocetneStavke, n++);
        prijelazi.put(pocetnoStanje, new LinkedHashMap<>());

        List<Stanje> red = new LinkedList<>();
        red.add(pocetnoStanje);
        Set<String> sviZnakovi = new LinkedHashSet<>(zavrsniZnakovi);
        sviZnakovi.addAll(nezavrsniZnakovi);

        while (!red.isEmpty()) {
            Stanje stanje = red.remove(0);
            // za svaki znak pronadji sve moguce prijelaze iz trenutnog stanja
            for (String znak : sviZnakovi) {
                Set<Stavka> noveStavke = new LinkedHashSet<>();
                for (Stavka stavka : stanje.stavke) {
                    if (enka.getPrijelazi().get(stavka, znak) != null) {
                        noveStavke.addAll(enka.getPrijelazi().get(stavka, znak));
                    }
                }
                // ako je novi skup stavki prazan, preskoci ga
                if (noveStavke.isEmpty()) continue;
                // inace izracunaj eps okruzenje
                noveStavke = izracunajEpsOkruzenje(noveStavke, enka.getPrijelazi());
                // ako vec postoji stanje s tim skupom stavki, dodaj prijelaz u njega i idi dalje
                Stanje novoStanje = new Stanje(noveStavke, n);
                if (prijelazi.containsKey(novoStanje)) {
                    Stanje postojeceStanje = prijelazi.keySet().stream()
                            .filter(s -> s.equals(novoStanje))
                            .findFirst()
                            .orElseThrow();
                    prijelazi.get(stanje).put(znak, postojeceStanje);
                    continue;
                }
                // inace dodaj novo stanje u prijelaze i red
                prijelazi.put(novoStanje, new LinkedHashMap<>());
                prijelazi.get(stanje).put(znak, novoStanje);
                red.add(novoStanje);
                n++;
            }
        }

        this.prijelazi = new Tablica<>(prijelazi);
        izradiTabliceAkcijeINovoStanje();
    }

    private Set<Stavka> izracunajEpsOkruzenje(Set<Stavka> trenutnaStanja, Tablica<Stavka,String,Set<Stavka>> prijelazi) {
        if (prijelazi == null || trenutnaStanja == null || trenutnaStanja.isEmpty()) return trenutnaStanja;

        boolean notDone = true;
        while (notDone) {
            Set<Stavka> epsOkrStanja = new LinkedHashSet<>(trenutnaStanja);
            for (Stavka stanje : trenutnaStanja) {
                if (prijelazi.get(stanje, "$") != null) {
                    epsOkrStanja.addAll(prijelazi.get(stanje, "$"));
                }
            }
            if (epsOkrStanja.equals(trenutnaStanja)) notDone = false;
            else trenutnaStanja = epsOkrStanja;
        }
        return trenutnaStanja;
    }

    private void izradiTabliceAkcijeINovoStanje() {
        // idemo po stanjima
        for (Stanje stanje : prijelazi.getKeys()) {
            // po svakoj stavci u stanju
            for (Stavka stavka : stanje.stavke) {
                // ako je tocka na kraju, reduciramo
                if (stavka.tockaJeNaKraju()) {
                    if (stavka.getLijevaStrana().equals("<%>")) {
                        akcije.set(stanje.n, "#", "prihvati()");
                        continue;
                    }
                    for (String znak : stavka.getSkupSlijedi()) {
                        // ako vec postoji akcija za to stanje i taj znak, dajemo prednost:
                        // 1. akciji pomakni
                        // 2. akciji reduciraj koja je ranije zadana
                        if (akcije.get(stanje.n, znak) != null) {
                            if (akcije.get(stanje.n, znak).startsWith("pomakni")) continue;
                            String postojeciNZ = akcije.get(stanje.n, znak).split("\\(")[1].split(" ->")[0];
                            String noviNZ = stavka.getLijevaStrana();
                            for (String NZ : produkcijeInverzno.get(stavka.getDesnaStrana())) {
                                if (NZ.equals(noviNZ)) {
                                    akcije.set(stanje.n, znak, "reduciraj(" + stavka.ispisiSamoProdukciju() + ")");
                                    break;
                                } else if (NZ.equals(postojeciNZ)) {
                                    break;
                                }
                            }
                        } else {
                            akcije.set(stanje.n, znak, "reduciraj(" + stavka.ispisiSamoProdukciju() + ")");
                        }
                    }
                }
            }
            // po svakom znaku u prijelazima iz stanja
            for (String znak : prijelazi.getKeys(stanje)) {
                // ako je znak zavrsni, pomakni
                if (zavrsniZnakovi.contains(znak)) {
                    Stanje novoStanje = prijelazi.get(stanje, znak);
                    akcije.set(stanje.n, znak, "pomakni(" + novoStanje.n + ")");
                }
                // ako je znak nezavrsni, dodaj u novoStanje
                else if (nezavrsniZnakovi.contains(znak)) {
                    Stanje novoStanje = prijelazi.get(stanje, znak);
                    this.novoStanje.set(stanje.n, znak, novoStanje.n);
                }
            }
        }
        // sve nepopunjene akcije postavljamo na odbaci
        zavrsniZnakovi.add(0, "#");
        for (Stanje stanje : prijelazi.getKeys()) {
            for (String znak : zavrsniZnakovi) {
                if (akcije.get(stanje.n, znak) == null) {
                    akcije.set(stanje.n, znak, "odbaci()");
                }
            }
        }
        for (Stanje stanje : prijelazi.getKeys()) {
            for (String znak : nezavrsniZnakovi) {
                if (novoStanje.get(stanje.n, znak) == null) {
                    novoStanje.set(stanje.n, znak, -1);
                }
            }
        }
    }

    public void ispisiStanja() {
        for (Stanje stanje : prijelazi.getKeys()) {
            System.out.println("Stanje " + stanje.n + ":");
            for (Stavka stavka : stanje.stavke) {
                System.out.println("\t" + stavka.toString());
            }
        }
    }

    public void ispisiPrijelaze() {
        for (Stanje stanje : prijelazi.getKeys()) {
            for (String znak : prijelazi.getKeys(stanje)) {
                System.out.println(stanje.n + " + " + znak + " -> " + prijelazi.get(stanje, znak).n);
            }
        }
    }

    public void ispisiTablice() {
        System.out.println("Akcije:");
        akcije.print();
        System.out.println();
        System.out.println("Novo stanje:");
        novoStanje.print();
    }

    public Tablica<Integer,String,String> getAkcije() {
        return akcije;
    }

    public Tablica<Integer,String,Integer> getNovoStanje() {
        return novoStanje;
    }
}
