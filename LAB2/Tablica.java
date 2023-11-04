import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Tablica<R,S,T> {
    Map<R, Map<S, T>> tablica;

    public Tablica(Map<R, Map<S, T>> tablica) {
        this.tablica = tablica;
    }

    public T get(R lijevaStrana, S desnaStrana) {
        if (!tablica.containsKey(lijevaStrana)) return null;
        return tablica.get(lijevaStrana).get(desnaStrana);
    }

    public void set(R lijevaStrana, S desnaStrana, T vrijednost) {
        if (!tablica.containsKey(lijevaStrana)) tablica.put(lijevaStrana, new LinkedHashMap<>());
        tablica.get(lijevaStrana).put(desnaStrana, vrijednost);
    }

    public void set(R lijevaStrana, Map<S, T> vrijednosti) {
        tablica.put(lijevaStrana, new LinkedHashMap<>(vrijednosti));
    }

    public Map<S, T> get(R lijevaStrana) {
        return tablica.get(lijevaStrana);
    }

    public Set<R> getKeys() {
        return tablica.keySet();
    }

    public Set<S> getKeys(R lijevaStrana) {
        if (!tablica.containsKey(lijevaStrana)) return null;
        return tablica.get(lijevaStrana).keySet();
    }

    public void print() {
        for (R lijevaStrana : tablica.keySet()) {
            System.out.println(lijevaStrana);
            for (S desnaStrana : tablica.get(lijevaStrana).keySet()) {
                System.out.println("  " + desnaStrana + " => " + tablica.get(lijevaStrana).get(desnaStrana));
            }
        }
    }
}
