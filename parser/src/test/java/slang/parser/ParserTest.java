package slang.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import slang.*;
import slang.tokenizer.Tokenizer;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

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
        assertEquals("hello", parser.next());
        assertEquals(123456L, parser.next());
        assertEquals(3.14, parser.next());
        assertEquals(SName.of("pi"), parser.next());
        assertEquals(SList.of(SName.of("hello"), "world"), parser.next());
        assertEquals(SList.of(SName.of("*"), 3.14, SList.of(SName.of("+"), 1L, 1L)), parser.next());
        assertEquals(SList.of(SName.of("some"), new SUnquote("macro")), parser.next());
        assertEquals(new SQuote(SName.of("quoted")), parser.next());
        assertEquals(SHashMap.of(SName.of("some"), SName.of("map")), parser.next());
        assertEquals(SSet.of(SName.of("some"), SName.of("set")), parser.next());
        assertEquals(SVector.of(SName.of("some"), SName.of("vector")), parser.next());
        assertEquals(SSet.of(""), parser.next());
        assertEquals(SVector.of(""), parser.next());
    }
}