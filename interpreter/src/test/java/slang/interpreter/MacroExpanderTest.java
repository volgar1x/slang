package slang.interpreter;

import org.junit.Before;
import org.junit.Test;
import slang.expressions.ExpressionInterface;
import slang.expressions.EvaluationContext;
import slang.parser.Parser;
import slang.tokenizer.Tokenizer;

import java.io.PrintStream;

/**
 * @author Antoine Chauvin
 */
public class MacroExpanderTest {

    private MockInputStream stdin;
    private MockOutputStream stdout;
    private MockOutputStream stderr;
    private EvaluationContext expander;

    @Before
    public void setUp() throws Exception {
        stdin = new MockInputStream();
        stdout = new MockOutputStream();
        stderr = new MockOutputStream();

        expander = new MacroExpander(stdin, new PrintStream(stdout), new PrintStream(stderr));
    }

    @Test
    public void test() throws Exception {
        Parser parser = new Parser(new Tokenizer(getClass().getClassLoader().getResourceAsStream("macro-test.slang")));
        Interpreter interpreter = new Interpreter(stdin, new PrintStream(stdout), new PrintStream(stderr));
        SlangAssert.load(interpreter);

        while (parser.hasNext()) {
            ExpressionInterface expanded = expander.evaluate(parser.next());
            interpreter.evaluate(expanded);
        }
    }
}