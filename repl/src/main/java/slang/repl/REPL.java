package slang.repl;

import slang.expressions.ExpressionInterface;
import slang.expressions.FunctionInterface;
import slang.expressions.NilExpression;
import slang.expressions.SlangException;
import slang.expressions.visitors.Inspector;
import slang.interpreter.Interpreter;
import slang.interpreter.MacroExpander;
import slang.parser.Parser;
import slang.tokenizer.Tokenizer;

import java.io.*;

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
        this.parser = new Parser(new Tokenizer(new Pretty(interpreter.getStandardOutput(), interpreter.getStandardInput())));

        this.interpreter.register("kill", (FunctionInterface) (context, arguments) -> {
            this.alive = false;
            return NilExpression.NIL;
        });
    }

    public ExpressionInterface run() {
        this.alive = true;
        ExpressionInterface result = NilExpression.NIL;
        while (this.alive) {
            try {
                result = interpreter.evaluate(macroExpander.evaluate(parser.next()));
                interpreter.getStandardOutput().println(Inspector.inspect(result));
            } catch (SlangException e) {
                interpreter.getStandardError().println(e.getMessage());
            }
        }
        return result;
    }

    private static class Pretty extends InputStream {
        final PrintStream stdout;
        final InputStream stdin;

        String buf;
        int pos;

        Pretty(PrintStream stdout, InputStream stdin) {
            this.stdout = stdout;
            this.stdin = stdin;
        }

        @Override
        public int read() throws IOException {
            if (buf == null || pos >= buf.length()) {
                stdout.print("> ");
                BufferedReader reader = new BufferedReader(new InputStreamReader(stdin));

                buf = reader.readLine();
                pos = 0;
            }
            return buf.codePointAt(pos++);
        }
    }
}
