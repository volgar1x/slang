package slang;

import slang.visitors.Printer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Antoine Chauvin
 */
public final class IO {
    public static void load(EvaluationContextInterface context) {
        Stdlib.loadFn(context, SAtom.of("println"), IO::println, true);
        Stdlib.loadFn(context, SAtom.of("print"), IO::print, true);
        Stdlib.loadFn(context, SAtom.of("readln"), IO::readln, true);
    }

    public static Object println(EvaluationContextInterface context, SList arguments) {
        context.getStandardOutput().println(Printer.print(arguments));
        return SList.nil;
    }

    public static Object print(EvaluationContextInterface context, SList arguments) {
        context.getStandardOutput().print(Printer.print(arguments));
        return SList.nil;
    }

    public static Object readln(EvaluationContextInterface context, SList arguments) {
        print(context, arguments);
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getStandardInput()));
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new SException(e);
        }
    }
}