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
        file = TokenizerTest.class.getClassLoader().getResourceAsStream("tokenizer-test.slang");
        tokenizer = new Tokenizer(file);
    }

    @After
    public void tearDown() throws Exception {
        file.close();
    }

    @Test
    public void test() throws Exception {
        tokenizer.next().expect(DOUBLE_QUOTE);
        tokenizer.next().expect("hello");
        tokenizer.next().expect(DOUBLE_QUOTE);

        tokenizer.next().expect("123456");

        tokenizer.next().expect("3.14");

        tokenizer.next().expect("pi");

        tokenizer.next().expect(START_LIST);
        tokenizer.next().expect("hello");
        tokenizer.next().expect(DOUBLE_QUOTE);
        tokenizer.next().expect("world");
        tokenizer.next().expect(DOUBLE_QUOTE);
        tokenizer.next().expect(END_LIST);

        tokenizer.next().expect(START_LIST);
        tokenizer.next().expect("*");
        tokenizer.next().expect("3.14");
        tokenizer.next().expect(START_LIST);
        tokenizer.next().expect("+");
        tokenizer.next().expect("1");
        tokenizer.next().expect("1");
        tokenizer.next().expect(END_LIST);
        tokenizer.next().expect(END_LIST);

        tokenizer.next().expect(START_LIST);
        tokenizer.next().expect("some");
        tokenizer.next().expect(UNQUOTE);
        tokenizer.next().expect("macro");
        tokenizer.next().expect(END_LIST);

        tokenizer.next().expect(QUOTE);
        tokenizer.next().expect("quoted");

        tokenizer.next().expect(START_MAP);
        tokenizer.next().expect("some");
        tokenizer.next().expect("map");
        tokenizer.next().expect(END_MAP);

        tokenizer.next().expect(START_SET);
        tokenizer.next().expect("some");
        tokenizer.next().expect("set");
        tokenizer.next().expect(END_SET);

        tokenizer.next().expect(START_VECTOR);
        tokenizer.next().expect("some");
        tokenizer.next().expect("vector");
        tokenizer.next().expect(END_VECTOR);

        tokenizer.next().expect(START_SET);
        tokenizer.next().expect(DOUBLE_QUOTE);
        tokenizer.next().expect("");
        tokenizer.next().expect(DOUBLE_QUOTE);
        tokenizer.next().expect(END_SET);

        tokenizer.next().expect(START_VECTOR);
        tokenizer.next().expect(DOUBLE_QUOTE);
        tokenizer.next().expect("");
        tokenizer.next().expect(DOUBLE_QUOTE);
        tokenizer.next().expect(END_VECTOR);

        tokenizer.next().expect(EOF);
    }
}