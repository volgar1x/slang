package slang;

import slang.expressions.*;

/**
 * @author Antoine Chauvin
 */
public final class Stdlib {
    private Stdlib() {}

    public static void load(EvaluationContextInterface interpreter) {
        interpreter.register("print", eval(IO::print));
        interpreter.register("println", eval(IO::println));
        interpreter.register("readln", eval(IO::readln));
        interpreter.register("inspect", eval(Core::inspect));
        interpreter.register("car", eval(Lists::car));
        interpreter.register("cdr", eval(Lists::cdr));
        interpreter.register("let", uneval(Core::let));
        interpreter.register("case", uneval(Core::case_));
        interpreter.register("def", uneval(Core::def));
        interpreter.register("not", eval(Booleans::not));
        interpreter.register("=", eval(Booleans::equals));
        interpreter.register("!=", eval(Booleans::nequals));
        interpreter.register(">", eval(Booleans::gt));
        interpreter.register(">=", eval(Booleans::gte));
        interpreter.register("<", eval(Booleans::lt));
        interpreter.register("<=", eval(Booleans::lte));
        interpreter.register("+", eval(Numbers::plus));
        interpreter.register("-", eval(Numbers::minus));
        interpreter.register("*", eval(Numbers::times));
        interpreter.register("**", eval(Numbers::pow));
        interpreter.register("/", eval(Numbers::div));
        interpreter.register("%", eval(Numbers::rem));
        interpreter.register("sqrt", eval(Numbers::sqrt));
//        interpreter.register("cos", eval(Stdlib::cos));
//        interpreter.register("sin", eval(Stdlib::sin));
//        interpreter.register("tan", eval(Stdlib::tan));
    }
    
    private static FunctionInterface uneval(FunctionInterface function) {
        return function;
    }
    
    private static FunctionInterface eval(FunctionInterface function) {
        return (context, arguments) -> function.call(context, arguments.map(context));
    }

}
