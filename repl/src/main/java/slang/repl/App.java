package slang.repl;

import slang.interpreter.Interpreter;

/**
 * @author Antoine Chauvin
 */
public final class App {
    private App() {}

    public static void main(String[] args) {
        new REPL(new Interpreter(System.in, System.out, System.err)).run();
    }
}
