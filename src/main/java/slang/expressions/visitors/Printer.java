package slang.expressions.visitors;

import slang.expressions.*;

/**
 * @author Antoine Chauvin
 */
public enum Printer implements Visitor<String> {
    INSTANCE;

    public static String print(ExpressionInterface expression) {
        return expression.visit(INSTANCE);
    }

    @Override
    public String visitAtom(AtomExpression atom) {
        return "'" + atom.getAtom();
    }

    @Override
    public String visitDecimal(DecimalExpression decimal) {
        return decimal.getDecimal().toString();
    }

    @Override
    public String visitFunction(FunctionInterface function) {
        return function.getFunctionName();
    }

    @Override
    public String visitInteger(IntegerExpression integer) {
        return integer.getInteger().toString(10);
    }

    @Override
    public String visitList(ListExpression list) {
        return list.join(this, "(", " ", ")");
    }

    @Override
    public String visitNil(NilExpression nil) {
        return "nil";
    }

    @Override
    public String visitQuote(QuoteExpression quote) {
        return "'" + quote.visit(this);
    }

    @Override
    public String visitSet(SetExpression set) {
        return set.join(this, "{", " ", "}");
    }

    @Override
    public String visitString(StringExpression string) {
        return string.getString();
    }

    @Override
    public String visitUnquote(UnquoteExpression unquote) {
        return "#" + unquote.getName();
    }

    @Override
    public String visitVector(VectorExpression vector) {
        return vector.join(this, "[", " ", "]");
    }
}
