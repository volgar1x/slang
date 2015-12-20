package slang.tokenizer;

/**
 * @author Antoine Chauvin
 */
public enum ConstToken implements TokenInterface {
    START_LIST,
    END_LIST,
    START_SET,
    END_SET,
    START_VECTOR,
    END_VECTOR,
    QUOTE,
    UNQUOTE,
    DOUBLE_QUOTE,
    EOF,
    ;

    @Override
    public String getValue() {
        return name();
    }

    @Override
    public void expect(TokenInterface token) {
        if (token != this) {
            throw new RuntimeException("unexpected token `" + name() + "'");
        }
    }
}
