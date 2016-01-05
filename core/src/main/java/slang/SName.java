package slang;

/**
 * @author Antoine Chauvin
 */
public final class SName {
    private final String string;

    private SName(String string) {
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

        SName atom = (SName) o;
        return string.equals(atom.string);
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    public static SName of(String string) {
        return new SName(string);
    }
}
