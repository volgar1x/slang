package slang.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import slang.tokenizer.Tokenizer;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static slang.expressions.Expressions.*;

/**
 * @author Antoine Chauvin
 */
public class ParserTest {
    private InputStream file;
    private Parser parser;

    @Before
    public void setUp() throws Exception {
        file = getClass().getClassLoader().getResourceAsStream("tokenizer-test.slang");
        parser = new Parser(new Tokenizer(file));
    }

    @After
    public void tearDown() throws Exception {
        file.close();
    }

    @Test
    public void test() throws Exception {
        assertEquals(string("hello"), parser.next());
        assertEquals(integer(123456), parser.next());
        assertEquals(decimal(3.14), parser.next());
        assertEquals(atom("pi"), parser.next());
        assertEquals(list(atom("hello"), string("world")), parser.next());
        assertEquals(list(atom("*"), decimal(3.14), list(atom("+"), integer(1), integer(1))), parser.next());
        assertEquals(list(atom("some"), unquote("macro")), parser.next());
        assertEquals(quote(atom("quoted")), parser.next());
        assertEquals(set(atom("some"), atom("set")), parser.next());
        assertEquals(vector(atom("some"), atom("vector")), parser.next());
        assertEquals(set(string("")), parser.next());
        assertEquals(vector(string("")), parser.next());
    }
}