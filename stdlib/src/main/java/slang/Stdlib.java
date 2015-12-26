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
        interpreter.register("cons", eval(Lists::cons));
        interpreter.register("len", eval(Lists::len));
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
        interpreter.register("atom?", eval(Booleans::atom));
        interpreter.register("dec?", eval(Booleans::decimal));
        interpreter.register("fun?", eval(Booleans::function));
        interpreter.register("int?", eval(Booleans::integer));
        interpreter.register("list?", eval(Booleans::list));
        interpreter.register("many?", eval(Booleans::many));
        interpreter.register("nil?", eval(Booleans::nil));
        interpreter.register("num?", eval(Booleans::num));
        interpreter.register("set?", eval(Booleans::set));
        interpreter.register("str?", eval(Booleans::string));
        interpreter.register("vec?", eval(Booleans::vector));
        interpreter.register("+", eval(Numbers::plus));
        interpreter.register("-", eval(Numbers::minus));
        interpreter.register("*", eval(Numbers::times));
        interpreter.register("**", eval(Numbers::pow));
        interpreter.register("/", eval(Numbers::div));
        interpreter.register("%", eval(Numbers::rem));
        interpreter.register("sqrt", eval(Numbers::sqrt));
        interpreter.register("round", eval(Numbers::round));
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
