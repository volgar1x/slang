package slang.parser;

import slang.expressions.*;

/**
 * @author Antoine Chauvin
 */
public final class MacroExpander extends EvaluationContext implements Visitor<ExpressionInterface> {
    public MacroExpander(EvaluationContextInterface parent) {
        super(parent);

        register("defmacro", new NativeFunction("defmacro", FunctionInterface.Definition.COMPILE_TIME, (context, arguments) -> {
            SlangFunction function = SlangFunction.fromList(arguments, FunctionInterface.Definition.COMPILE_TIME);
            register(function.getFunctionName(), function);
            return NilExpression.NIL;
        }));
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
    public ExpressionInterface visitList(ListExpression list) {
        if (!(list.getHead() instanceof AtomExpression)) {
            return visitMany(list);
        }

        String functionName = ((AtomExpression) list.getHead()).getAtom();

        ExpressionInterface maybe = readMaybe(functionName);
        if (maybe == null || !(maybe instanceof FunctionInterface) || ((FunctionInterface) maybe).getDefinition() != FunctionInterface.Definition.COMPILE_TIME) {
            return visitMany(list);
        }
        FunctionInterface function = (FunctionInterface) maybe;

        EvaluationContextInterface interpretContext = getParent().link();
        ExpressionInterface result = function.call(interpretContext, list.getTail());
        ExpressionInterface unquoted = unquote(interpretContext, result);
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
            public ExpressionInterface visitQuote(QuoteExpression quote) {
                return new QuoteExpression(quote.getExpression().visit(this));
            }

            @Override
            public ExpressionInterface otherwise(ExpressionInterface expression) {
                return expression;
            }
        });
    }
}
