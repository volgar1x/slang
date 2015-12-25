package slang.repl;

import slang.expressions.ExpressionInterface;
import slang.expressions.FunctionInterface;
import slang.expressions.NilExpression;
import slang.expressions.visitors.Inspector;
import slang.interpreter.Interpreter;
import slang.interpreter.MacroExpander;
import slang.parser.Parser;
import slang.tokenizer.Tokenizer;

/**
 * @author Antoine Chauvin
 */
public final class REPL {
    private final Interpreter interpreter;
    private final MacroExpander macroExpander;
    private final Parser parser;
    private boolean alive;

    public REPL(Interpreter interpreter) {
        this.interpreter = interpreter;
        this.macroExpander = new MacroExpander(this.interpreter);
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
            try {
                result = interpreter.evaluate(macroExpander.evaluate(parser.next()));
                interpreter.getStandardOutput().println(Inspector.inspect(result));
            } catch (Exception e) {
                e.printStackTrace(interpreter.getStandardError());
            }
        }
        return result;
    }
}
