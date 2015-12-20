package slang.expressions.visitors;

import slang.expressions.*;

/**
 * @author Antoine Chauvin
 */
public abstract class BaseVisitor<R> implements Visitor<R> {
    public abstract R otherwise(ExpressionInterface expression);

    @Override
    public R visitAtom(AtomExpression atom) {
        return otherwise(atom);
    }

    @Override
    public R visitDecimal(DecimalExpression decimal) {
        return otherwise(decimal);
    }

    @Override
    public R visitFunction(FunctionInterface function) {
        return otherwise(function);
    }

    @Override
    public R visitInteger(IntegerExpression integer) {
        return otherwise(integer);
    }

    @Override
    public R visitList(ListExpression list) {
        return otherwise(list);
    }

    @Override
    public R visitNil(NilExpression nil) {
        return otherwise(nil);
    }

    @Override
    public R visitQuote(QuoteExpression quote) {
        return otherwise(quote);
    }

    @Override
    public R visitSet(SetExpression set) {
        return otherwise(set);
    }

    @Override
    public R visitString(StringExpression string) {
        return otherwise(string);
    }

    @Override
    public R visitUnquote(UnquoteExpression unquote) {
        return otherwise(unquote);
    }

    @Override
    public R visitVector(VectorExpression vector) {
        return otherwise(vector);
    }
}
