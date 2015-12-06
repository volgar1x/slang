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

}
