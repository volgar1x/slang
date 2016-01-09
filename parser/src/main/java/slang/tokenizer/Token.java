package slang.tokenizer;

/**
 * @author Antoine Chauvin
 */
public abstract class Token {
    private final int line, column;

    protected Token(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public abstract String getValue();
    public abstract boolean test(Object value);

    public void expect(Object value) {
        if (!test(value)) {
            throw new IllegalArgumentException("unexpected token `" + value + "'");
        }
    }
}
