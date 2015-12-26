package slang.repl;

import slang.expressions.ExpressionInterface;
import slang.expressions.FunctionInterface;
import slang.expressions.NilExpression;
import slang.expressions.SlangException;
import slang.expressions.visitors.Inspector;
import slang.interpreter.Interpreter;
import slang.parser.MacroExpander;
import slang.parser.Parser;
import slang.tokenizer.Tokenizer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Antoine Chauvin
 */
public final class REPL {
    private final Interpreter interpreter;
    private final MacroExpander macroExpander;
    private final Parser parser;
    private final boolean verbose;
    private boolean alive;

    private REPL(Interpreter interpreter, MacroExpander macroExpander, Parser parser, boolean verbose) {
        this.interpreter = interpreter;
        this.macroExpander = macroExpander;
        this.parser = parser;
        this.verbose = verbose;

        this.interpreter.register("kill", (FunctionInterface) (context, arguments) -> {
            this.alive = false;
            return NilExpression.NIL;
        });
    }

    public static REPL mount(Interpreter interpreter) {
        InputStream stdin = new Pretty(interpreter.getStandardOutput(), interpreter.getStandardInput());

        return new REPL(interpreter,
                new MacroExpander(interpreter),
                new Parser(new Tokenizer(stdin)), /*verbose*/true);
    }

    public static REPL fromFile(Path path, boolean verbose) throws IOException {
        Interpreter interpreter = new Interpreter(System.in, System.out, System.err);

        return new REPL(interpreter,
                new MacroExpander(interpreter),
                new Parser(new Tokenizer(Files.newInputStream(path))),
                verbose);
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public ExpressionInterface run() {
        this.alive = true;
        ExpressionInterface result = NilExpression.NIL;
        while (this.alive && parser.hasNext()) {
            try {
                result = interpreter.evaluate(macroExpander.evaluate(parser.next()));

                if (verbose) {
                    interpreter.getStandardOutput().println(Inspector.inspect(result));
                }
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
        boolean eof;

        Pretty(PrintStream stdout, InputStream stdin) {
            this.stdout = stdout;
            this.stdin = stdin;
        }

        @Override
        public int available() throws IOException {
            return eof ? -1 : Integer.MAX_VALUE;
        }

        @Override
        public int read() throws IOException {
            if (buf == null || pos >= buf.length()) {
                stdout.print("> ");
                BufferedReader reader = new BufferedReader(new InputStreamReader(stdin));

                buf = reader.readLine();
                pos = 0;
                eof = buf == null;
            }

            return !eof && pos < buf.length() ? buf.codePointAt(pos++) : -1;
        }
    }
}
