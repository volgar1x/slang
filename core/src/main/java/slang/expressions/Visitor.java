package slang.expressions;

import java.util.function.Function;

/**
 * @author Antoine Chauvin
 */
public interface Visitor<R> extends Function<ExpressionInterface, R> {
    R visitAtom(AtomExpression atom);
    R visitDecimal(DecimalExpression decimal);
    R visitFunction(FunctionInterface function);
    R visitInteger(IntegerExpression integer);
    R visitList(ListExpression list);
    R visitNil(NilExpression nil);
    R visitQuote(QuoteExpression quote);
    R visitSet(SetExpression set);
    R visitString(StringExpression string);
    R visitUnquote(UnquoteExpression unquote);
    R visitVector(VectorExpression vector);

    @Override
    default R apply(ExpressionInterface expression) {
        return expression.visit(this);
    }
}
