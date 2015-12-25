package slang.interpreter;

import slang.expressions.*;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author Antoine Chauvin
 */
public final class MacroExpander extends EvaluationContext implements Visitor<ExpressionInterface> {
    public MacroExpander(InputStream stdin, PrintStream stdout, PrintStream stderr) {
        super(stdin, stdout, stderr);

        register("defmacro", (FunctionInterface) (context, arguments) -> {
            SlangFunction function = SlangFunction.fromList(arguments);
            register(function.getFunctionName(), function);
            return NilExpression.NIL;
        });
    }

    public MacroExpander(EvaluationContextInterface parent) {
        super(parent);
    }

    @Override
    public EvaluationContextInterface link() {
        return new MacroExpander(this);
    }

    @Override
    public ExpressionInterface apply(ExpressionInterface expression) {
        return evaluate(expression);
    }

    @Override
    public ExpressionInterface evaluate(ExpressionInterface expression) {
        return expression.visit(this);
    }

    @Override
    public ExpressionInterface otherwise(ExpressionInterface expression) {
        return expression;
    }

    @Override
    public ExpressionInterface visitMany(ManyExpressionInterface many) {
        return many.map(this);
    }

    @Override
    public ExpressionInterface visitUnquote(UnquoteExpression unquote) {
        return read(unquote.getName());
    }

    @Override
    public ExpressionInterface visitFunction(FunctionInterface function) {
        register(function.getFunctionName(), function);
        return NilExpression.NIL;
    }

    @Override
    public ExpressionInterface visitList(ListExpression list) {
        if (!(list.getHead() instanceof AtomExpression)) {
            return visitMany(list);
        }

        String functionName = ((AtomExpression) list.getHead()).getAtom();

        ExpressionInterface maybe = readMaybe(functionName);
        if (maybe == null || !(maybe instanceof FunctionInterface)) {
            return visitMany(list);
        }
        FunctionInterface function = (FunctionInterface) maybe;

        Interpreter interpreter = new Interpreter(this);
        ExpressionInterface result = function.call(interpreter, list.getTail());
        ExpressionInterface unquoted = unquote(interpreter, result);
        return evaluate(unquoted);
    }

    private ExpressionInterface unquote(EvaluationContextInterface context, ExpressionInterface exp) {
        return exp.visit(new Visitor<ExpressionInterface>() {
            @Override
            public ExpressionInterface visitUnquote(UnquoteExpression unquote) {
                return context.read(unquote.getName());
            }

            @Override
            public ExpressionInterface visitMany(ManyExpressionInterface many) {
                return many.map(this);
            }

            @Override
            public ExpressionInterface otherwise(ExpressionInterface expression) {
                return expression;
            }
        });
    }
}
