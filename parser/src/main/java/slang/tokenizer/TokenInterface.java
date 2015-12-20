package slang.tokenizer;

/**
 * @author Antoine Chauvin
 */
public interface TokenInterface {
    String getValue();
    void expect(TokenInterface token);
}
