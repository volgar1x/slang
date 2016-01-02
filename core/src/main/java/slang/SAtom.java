package slang;

/**
 * @author Antoine Chauvin
 */
public final class SAtom {
    private final String string;

    private SAtom(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SAtom atom = (SAtom) o;
        return string.equals(atom.string);
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    public static SAtom of(String string) {
        return new SAtom(string);
    }
}
