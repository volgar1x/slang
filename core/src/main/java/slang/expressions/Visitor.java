package slang.expressions;

import java.util.function.Function;

/**
 * @author Antoine Chauvin
 */
public interface Visitor<R> extends Function<ExpressionInterface, R> {
    R otherwise(ExpressionInterface expression);

    default R visitAtom(AtomExpression atom) {
        return otherwise(atom);
    }
    default R visitDecimal(DecimalExpression decimal) {
        return otherwise(decimal);
    }
    default R visitFunction(FunctionInterface function) {
        return otherwise(function);
    }
    default R visitInteger(IntegerExpression integer) {
        return otherwise(integer);
    }
    default R visitNil(NilExpression nil) {
        return otherwise(nil);
    }
    default R visitQuote(QuoteExpression quote) {
        return otherwise(quote);
    }
    default R visitString(StringExpression string) {
        return otherwise(string);
    }
    default R visitUnquote(UnquoteExpression unquote) {
        return otherwise(unquote);
    }
    default R visitMany(ManyExpressionInterface many) {
        return otherwise(many);
    }
    default R visitList(ListExpression list) {
        return visitMany(list);
    }
    default R visitSet(SetExpression set) {
        return visitMany(set);
    }
    default R visitVector(VectorExpression vector) {
        return visitMany(vector);
    }

    @Override
    default R apply(ExpressionInterface expression) {
        return expression.visit(this);
    }
}
