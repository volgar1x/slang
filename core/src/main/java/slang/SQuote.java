package slang;

/**
 * @author Antoine Chauvin
 */
public final class SQuote {
    public final Object quoted;

    public SQuote(Object quoted) {
        this.quoted = quoted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SQuote sQuote = (SQuote) o;

        return quoted.equals(sQuote.quoted);
    }

    @Override
    public int hashCode() {
        return quoted.hashCode();
    }

    @Override
    public String toString() {
        return "Quote(" + quoted + ")";
    }
}
