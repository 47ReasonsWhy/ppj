import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

public class Stavka {
    private final String lijevaStrana;
    private final List<String> desnaStrana;
    private final int tocka;
    private TreeSet<String> skupSlijedi;

    public Stavka(String lijevaStrana, List<String> desnaStrana, int tocka) {
        if (lijevaStrana == null || lijevaStrana.isEmpty()) {
            throw new RuntimeException("Lijeva strana ne smije biti prazna");
        }
        if (desnaStrana == null || desnaStrana.isEmpty()) {
            throw new RuntimeException("Desna strana ne smije biti prazna");
        }
        if (tocka < 0 || tocka > desnaStrana.size()) {
            throw new RuntimeException("Tocka mora biti izmedu 0 i " + desnaStrana.size());
        }
        if (desnaStrana.size() == 1 && desnaStrana.get(0).equals("$") && tocka != 0) {
            throw new RuntimeException("Tocka mora biti na pocetku ako je desna strana samo $");
        }
        this.lijevaStrana = lijevaStrana;
        this.desnaStrana = desnaStrana;
        this.tocka = tocka;
        this.skupSlijedi = new TreeSet<>();
    }

    public String getLijevaStrana() {
        return lijevaStrana;
    }

    public List<String> getDesnaStrana() {
        return desnaStrana;
    }

    public int getTocka() {
        return tocka;
    }

    public TreeSet<String> getSkupSlijedi() {
        return skupSlijedi;
    }

    public List<String> getDesnaStranaNakonTocke() {
        return desnaStrana.subList(tocka, desnaStrana.size());
    }

    public Stavka pomakniTocku() {
        if (tocka == desnaStrana.size() || (desnaStrana.size() == 1 && desnaStrana.get(0).equals("$"))) {
            throw new RuntimeException("Tocka je vec na kraju desne strane");
        }
        Stavka stavka =  new Stavka(lijevaStrana, desnaStrana, tocka + 1);
        stavka.skupSlijedi = skupSlijedi;
        return stavka;
    }

    public boolean tockaJeNaKraju() {
        return tocka == desnaStrana.size() || (desnaStrana.size() == 1 && desnaStrana.get(0).equals("$"));
    }

    public String sljedeciZnak() {
        if (tocka == desnaStrana.size()) {
            throw new RuntimeException("Tocka je vec na kraju desne strane");
        }
        if (desnaStrana.size() == 1 && desnaStrana.get(0).equals("$")) {
            throw new RuntimeException("Desna strana je samo $");
        }
        return desnaStrana.get(tocka);
    }

    public String ispisiSamoProdukciju() {
        StringBuilder sb = new StringBuilder();
        sb.append(lijevaStrana).append(" -> ");
        for (String s : desnaStrana) {
            sb.append(s).append(" ");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lijevaStrana).append(" -> ");
        for (int i = 0; i < desnaStrana.size(); i++) {
            if (i == tocka) sb.append("*");
            sb.append(desnaStrana.get(i)).append(" ");
        }
        if (tocka == desnaStrana.size()) sb.append("*");
        sb.append(" { ");
        for (String znak : skupSlijedi) {
            sb.append(znak).append(" ");
        }
        sb.append("}");
        return sb.toString().replace("$ ", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stavka stavka = (Stavka) o;

        if (tocka != stavka.tocka) return false;
        if (!lijevaStrana.equals(stavka.lijevaStrana)) return false;
        if (!desnaStrana.equals(stavka.desnaStrana)) return false;
        return skupSlijedi.equals(stavka.skupSlijedi);
    }

    @Override
    public int hashCode() {
        int result = lijevaStrana.hashCode();
        result = 31 * result + desnaStrana.hashCode();
        result = 31 * result + tocka;
        result = 31 * result + skupSlijedi.hashCode();
        return result;
    }
}
