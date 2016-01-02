package slang.parser;

import org.junit.Before;
import org.junit.Test;
import slang.EvaluationContext;
import slang.MockInputStream;
import slang.MockOutputStream;
import slang.SlangAssert;
import slang.interpreter.Interpreter;
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

        interpreter = new Interpreter(stdin, new PrintStream(stdout), new PrintStream(stderr));
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