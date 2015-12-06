package slang.tokenizer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * @author Antoine Chauvin
 */
public class TokenizerTest {
    private InputStream file;
    private Tokenizer tokenizer;

    @Before
    public void setUp() throws Exception {
        file = TokenizerTest.class.getClassLoader().getResourceAsStream("tokenizer-test.slang");
        tokenizer = new Tokenizer(file);
    }

    @After
    public void tearDown() throws Exception {
        file.close();
    }

    @Test
    public void test() throws Exception {
        assertEquals(ConstToken.DOUBLE_QUOTE, tokenizer.next());
        assertEquals(Token.of("hello"), tokenizer.next());
        assertEquals(ConstToken.DOUBLE_QUOTE, tokenizer.next());

        assertEquals(Token.of("123456"), tokenizer.next());

        assertEquals(Token.of("3.14"), tokenizer.next());

        assertEquals(Token.of("pi"), tokenizer.next());

        assertEquals(ConstToken.START_LIST, tokenizer.next());
        assertEquals(Token.of("hello"), tokenizer.next());
        assertEquals(ConstToken.DOUBLE_QUOTE, tokenizer.next());
        assertEquals(Token.of("world"), tokenizer.next());
        assertEquals(ConstToken.DOUBLE_QUOTE, tokenizer.next());
        assertEquals(ConstToken.END_LIST, tokenizer.next());

        assertEquals(ConstToken.START_LIST, tokenizer.next());
        assertEquals(Token.of("*"), tokenizer.next());
        assertEquals(Token.of("3.14"), tokenizer.next());
        assertEquals(ConstToken.START_LIST, tokenizer.next());
        assertEquals(Token.of("+"), tokenizer.next());
        assertEquals(Token.of("1"), tokenizer.next());
        assertEquals(Token.of("1"), tokenizer.next());
        assertEquals(ConstToken.END_LIST, tokenizer.next());
        assertEquals(ConstToken.END_LIST, tokenizer.next());

        assertEquals(ConstToken.START_LIST, tokenizer.next());
        assertEquals(Token.of("some"), tokenizer.next());
        assertEquals(ConstToken.UNQUOTE, tokenizer.next());
        assertEquals(Token.of("macro"), tokenizer.next());
        assertEquals(ConstToken.END_LIST, tokenizer.next());

        assertEquals(ConstToken.QUOTE, tokenizer.next());
        assertEquals(Token.of("quoted"), tokenizer.next());

        assertEquals(ConstToken.EOF, tokenizer.next());
    }
}