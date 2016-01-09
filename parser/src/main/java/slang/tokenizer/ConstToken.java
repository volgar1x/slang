package slang.tokenizer;

/**
 * @author Antoine Chauvin
 */
public final class ConstToken extends Token {
    public enum Type {
        START_LIST,
        END_LIST,
        START_SET,
        END_SET,
        START_MAP,
        END_MAP,
        START_VECTOR,
        END_VECTOR,
        QUOTE,
        UNQUOTE,
        DOUBLE_QUOTE,
        EOF,
    }
    private final Type type;

    public ConstToken(int line, int column, Type type) {
        super(line, column);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String getValue() {
        return "Const(" + type.name() + ")";
    }

    @Override
    public boolean test(Object value) {
        return value == type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConstToken that = (ConstToken) o;

        return type == that.type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public String toString() {
        return getValue();
    }
}
