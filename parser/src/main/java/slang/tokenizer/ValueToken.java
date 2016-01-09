package slang.tokenizer;

/**
 * @author Antoine Chauvin
 */
public final class ValueToken extends Token {
    private final String value;

    public ValueToken(int line, int column, String value) {
        super(line, column);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean test(Object value) {
        return this.value.equals(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValueToken token = (ValueToken) o;

        return value.equals(token.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
