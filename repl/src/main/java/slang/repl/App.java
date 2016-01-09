package slang.repl;

import slang.SlangAssert;
import slang.interpreter.Interpreter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @author Antoine Chauvin
 */
public final class App {
    private App() {}

    public static void main(String[] args_) throws IOException {
        List<String> args = Arrays.asList(args_);

        REPL repl;

        if (args.contains("-i")) {
            // read code from file
            String filename = args.get(args.indexOf("-i") + 1);
            repl = REPL.fromFile(Paths.get(filename), args.contains("-v"));
        } else {
            // read code from stdin and provide some niceties over it
            repl = REPL.mount(new Interpreter(App.class.getClassLoader()));
        }

        SlangAssert.load(repl.getInterpreter());

        repl.run();
    }
}
