package slang.tokenizer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static slang.tokenizer.ConstToken.Type.*;

/**
 * @author Antoine Chauvin
 */
public class TokenizerTest {
    private InputStream file;
    private Tokenizer tokenizer;

    @Before
    public void setUp() throws Exception {
        System.out.println();
        file = TokenizerTest.class.getClassLoader().getResourceAsStream("tokenizer-test.slang");
        tokenizer = new Tokenizer(file);
    }

    @After
    public void tearDown() throws Exception {
        file.close();
    }

    private void assertToken(int line, int column, Object value) {
        Token token = tokenizer.next();

        if (!token.test(value)) {
            throw new AssertionError(String.format("expected <%s> but got <%s>", value, token));
        }

        if (token.getLine() != line) {
            throw new AssertionError(String.format("expected <%s> token to be on line <%s> but was <%s>",
                    token, line, token.getLine()));
        }

        if (token.getColumn() != column) {
            throw new AssertionError(String.format("expected <%s> token to be on column <%s> but was <%s>",
                    token, column, token.getColumn()));
        }
    }

    @Test
    public void test() throws Exception {
        // got one line of comment, not being tokenized

        assertToken(2, 1, DOUBLE_QUOTE);
        assertToken(2, 2, "hello");
        assertToken(2, 7, DOUBLE_QUOTE);

        assertToken(3, 1, "123456");

        assertToken(4, 1, "3.14");

        assertToken(5, 1, "pi");

        assertToken(6, 1, START_LIST);
        assertToken(6, 2, "hello");
        assertToken(6, 8, DOUBLE_QUOTE);
        assertToken(6, 9, "world");
        assertToken(6, 14, DOUBLE_QUOTE);
        assertToken(6, 15, END_LIST);

        assertToken(7, 1, START_LIST);
        assertToken(7, 2, "*");
        assertToken(7, 4, "3.14");
        assertToken(7, 9, START_LIST);
        assertToken(7, 10, "+");
        assertToken(7, 12, "1");
        assertToken(7, 14, "1");
        assertToken(7, 15, END_LIST);
        assertToken(7, 16, END_LIST);

        assertToken(8, 1, START_LIST);
        assertToken(8, 2, "some");
        assertToken(8, 7, UNQUOTE);
        assertToken(8, 8, "macro");
        assertToken(8, 13, END_LIST);

        // got two lines of comments, not being tokenized

        assertToken(11, 1, QUOTE);
        assertToken(11, 2, "quoted");

        assertToken(12, 1, START_MAP);
        assertToken(12, 2, "some");
        assertToken(12, 7, "map");
        assertToken(12, 10, END_MAP);

        assertToken(13, 1, START_SET);
        assertToken(13, 3, "some");
        assertToken(13, 8, "set");
        assertToken(13, 11, END_SET);

        assertToken(14, 1, START_VECTOR);
        assertToken(14, 2, "some");
        assertToken(14, 7, "vector");
        assertToken(14, 13, END_VECTOR);

        assertToken(15, 1, START_SET);
        assertToken(15, 3, DOUBLE_QUOTE);
        assertToken(15, 4, "");
        assertToken(15, 4, DOUBLE_QUOTE);
        assertToken(15, 5, END_SET);

        assertToken(16, 1, START_VECTOR);
        assertToken(16, 2, DOUBLE_QUOTE);
        assertToken(16, 3, "");
        assertToken(16, 3, DOUBLE_QUOTE);
        assertToken(16, 4, END_VECTOR);

        assertToken(16, 5, EOF);
    }
}