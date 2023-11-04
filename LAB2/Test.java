import java.util.List;
import java.util.Set;

public class Test {
    record Stanje(Set<Stavka> stavke, int n) {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Stanje stanje)) return false;
            return stavke.equals(stanje.stavke);
        }

    }

    public static void main(String[] args) {

    }
}
