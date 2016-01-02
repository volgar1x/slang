package slang;

/**
 * @author Antoine Chauvin
 */
public final class SUnquote {
    public final String name;

    public SUnquote(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SUnquote sUnquote = (SUnquote) o;

        return name.equals(sUnquote.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Unquote(" + name + ")";
    }
}
