package slang.interpreter;

import slang.expressions.RootEvaluationContext;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Antoine Chauvin
 */
public final class Interpreter extends RootEvaluationContext {
    public Interpreter(InputStream stdin, PrintStream stdout, PrintStream stderr) {
        super(InterpreterEvaluation.INSTANCE, stdin, stdout, stderr);
        Stdlib.load(this);
    }
}
