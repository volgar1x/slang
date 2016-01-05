package slang.visitors;

import slang.*;

import java.util.Map;
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
    public String visitAtom(SAtom atom) {
        String string = atom.toString();
        if (string.equalsIgnoreCase("true")) {
            return "true";
        }
        return ":" + string;
    }

    @Override
    public String visitQuote(SQuote quote) {
        return "'" + apply(quote.quoted);
    }

    @Override
    public String visitString(String string) {
        return "\"" + string + "\"";
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
    public String visitMap(SMap map) {
        StringBuilder builder = new StringBuilder();
        builder.append('{');
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            builder.append(apply(entry.getKey())).append(' ').append(apply(entry.getValue()));
        }
        builder.append('}');
        return builder.toString();
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
