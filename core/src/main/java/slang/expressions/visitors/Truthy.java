package slang.expressions.visitors;

import slang.expressions.*;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Antoine Chauvin
 */
public enum Truthy implements Visitor<Boolean> {
    INSTANCE;

    public static boolean truthy(ExpressionInterface expression) {
        return INSTANCE.apply(expression);
    }

    @Override
    public Boolean visitAtom(AtomExpression atom) {
        return true;
    }

    @Override
    public Boolean visitDecimal(DecimalExpression decimal) {
        return decimal.asDecimal().compareTo(BigDecimal.ZERO) != 0;
    }

    @Override
    public Boolean visitFunction(FunctionInterface function) {
        return true;
    }

    @Override
    public Boolean visitInteger(IntegerExpression integer) {
        return integer.asInteger().compareTo(BigInteger.ZERO) != 0;
    }

    @Override
    public Boolean visitList(ListExpression list) {
        return !list.isEmpty();
    }

    @Override
    public Boolean visitNil(NilExpression nil) {
        return false;
    }

    @Override
    public Boolean visitQuote(QuoteExpression quote) {
        return true;
    }

    @Override
    public Boolean visitSet(SetExpression set) {
        return !set.isEmpty();
    }

    @Override
    public Boolean visitString(StringExpression string) {
        return !string.getString().isEmpty();
    }

    @Override
    public Boolean visitUnquote(UnquoteExpression unquote) {
        return true;
    }

    @Override
    public Boolean visitVector(VectorExpression vector) {
        return !vector.isEmpty();
    }
}
