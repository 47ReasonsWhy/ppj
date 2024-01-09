package znakovi;

public class Deklaracija {
    public String tip;
    public boolean l_izraz;

    public Deklaracija(String tip, boolean l_izraz) {
        this.tip = tip;
        this.l_izraz = l_izraz;
    }

    public Deklaracija(Deklaracija deklaracija) {
        this.tip = deklaracija.tip;
        this.l_izraz = deklaracija.l_izraz;
    }
}
