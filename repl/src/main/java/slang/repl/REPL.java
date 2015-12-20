package slang.repl;

import slang.expressions.ExpressionInterface;
import slang.expressions.FunctionInterface;
import slang.expressions.NilExpression;
import slang.expressions.visitors.Inspector;
import slang.interpreter.Interpreter;
import slang.parser.Parser;
import slang.tokenizer.Tokenizer;

/**
 * @author Antoine Chauvin
 */
public final class REPL {
    private final Interpreter interpreter;
    private final Parser parser;
    private boolean alive;

    public REPL(Interpreter interpreter) {
        this.interpreter = interpreter;
        this.parser = new Parser(new Tokenizer(interpreter.getStandardInput()));

        this.interpreter.register("kill", (FunctionInterface) (context, arguments) -> {
            this.alive = false;
            return NilExpression.NIL;
        });
    }

    public ExpressionInterface run() {
        this.alive = true;
        ExpressionInterface result = NilExpression.NIL;
        while (this.alive) {
            interpreter.getStandardOutput().print("> ");
            result = interpreter.evaluate(parser.next());
            interpreter.getStandardOutput().println(Inspector.inspect(result));
        }
        return result;
    }
}
