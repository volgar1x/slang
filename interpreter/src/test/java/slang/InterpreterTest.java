package slang;

import org.junit.Before;
import org.junit.Test;
import slang.interpreter.Interpreter;
import slang.parser.Parser;
import slang.tokenizer.Tokenizer;

import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**
 * @author Antoine Chauvin
 */
public class InterpreterTest {

    private Parser parser;
    private Interpreter interpreter;
    private MockInputStream stdin;
    private MockOutputStream stdout;
    private MockOutputStream stderr;

    @Before
    public void setUp() throws Exception {
        stdin = new MockInputStream();
        stdout = new MockOutputStream();
        stderr = new MockOutputStream();

        parser = new Parser(new Tokenizer(getClass().getClassLoader().getResourceAsStream("interpreter-test.slang")));

        interpreter = new Interpreter(stdin, new PrintStream(stdout), new PrintStream(stderr));
        SlangAssert.load(interpreter);
    }

    @Test
    public void testEvaluate() throws Exception {
        assertEquals(SList.nil, interpreter.evaluate(parser.next()));
        assertEquals("Hello, World!\n", stdout.clear());

        assertEquals(SList.nil, interpreter.evaluate(parser.next()));
        assertEquals("Hello, Monde!\nHello, World!\n", stdout.clear());

        stdin.append("Joe\n");
        assertEquals(SList.nil, interpreter.evaluate(parser.next()));
        assertEquals("whats your name?Hi Joe !\n", stdout.clear());

        while (parser.hasNext()) {
            interpreter.evaluate(parser.next());
        }
    }
}