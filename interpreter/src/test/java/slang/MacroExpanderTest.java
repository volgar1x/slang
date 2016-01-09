package slang;

import org.junit.Before;
import org.junit.Test;
import slang.interpreter.Interpreter;
import slang.interpreter.MacroExpander;
import slang.parser.Parser;
import slang.tokenizer.Tokenizer;

import java.io.PrintStream;

/**
 * @author Antoine Chauvin
 */
public class MacroExpanderTest {

    private EvaluationContext expander;
    private Interpreter interpreter;

    @Before
    public void setUp() throws Exception {
        MockInputStream stdin = new MockInputStream();
        MockOutputStream stdout = new MockOutputStream();
        MockOutputStream stderr = new MockOutputStream();

        System.setIn(stdin);
        System.setOut(new PrintStream(stdout));
        System.setErr(new PrintStream(stderr));

        interpreter = new Interpreter(getClass().getClassLoader());
        SlangAssert.load(interpreter);

        expander = new MacroExpander(interpreter);
    }

    @Test
    public void test() throws Exception {
        Parser parser = new Parser(new Tokenizer(getClass().getClassLoader().getResourceAsStream("macro-test.slang")));

        while (parser.hasNext()) {
            Object expanded = expander.evaluate(parser.next());
            interpreter.evaluate(expanded);
        }
    }
}