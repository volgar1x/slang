package slang.tokenizer;

import java.util.Objects;

/**
 * @author Antoine Chauvin
 */
public final class Token implements TokenInterface {
    private final String value;

    private Token(String value) {
        this.value = Objects.requireNonNull(value, "token value must not be null");
    }

    public static Token of(String value) {
        return new Token(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        return value.equals(token.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
