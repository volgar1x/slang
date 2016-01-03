package slang.visitors;

import slang.*;

import java.util.stream.Collectors;

/**
 * @author Antoine Chauvin
 */
public enum Inspector implements Visitor<String> {
    INSTANCE;

    public static String inspect(Object expression) {
        return INSTANCE.apply(expression);
    }

    @Override
    public String otherwise(Object expression) {
        return expression.toString();
    }

    @Override
    public String visitQuote(SQuote quote) {
        return "'" + apply(quote.quoted);
    }

    @Override
    public String visitUnquote(SUnquote unquote) {
        return "#" + unquote.name;
    }

    @Override
    public String visitList(SList list) {
        return list.stream().map(this).collect(Collectors.joining(" ", "(", ")"));
    }

    @Override
    public String visitSet(SSet set) {
        return set.stream().map(this).collect(Collectors.joining(" ", "#{", "}"));
    }

    @Override
    public String visitVector(SVector vector) {
        return vector.stream().map(this).collect(Collectors.joining(" ", "[", "]"));
    }

}
