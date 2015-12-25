package slang.repl;

import slang.SlangAssert;
import slang.interpreter.Interpreter;

/**
 * @author Antoine Chauvin
 */
public final class App {
    private App() {}

    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter(System.in, System.out, System.err);
        SlangAssert.load(interpreter);
        new REPL(interpreter).run();
    }
}
