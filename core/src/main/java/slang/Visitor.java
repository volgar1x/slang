package slang;

import java.util.function.Function;

import static slang.Nil.nil;

/**
 * @author Antoine Chauvin
 */
public interface Visitor<R> extends Function<Object, R> {
    R otherwise(Object expression);

    default R visitAtom(SAtom atom) {
        return otherwise(atom);
    }
    default R visitDecimal(double decimal) {
        return otherwise(decimal);
    }
    default R visitFunction(SFunction function) {
        return otherwise(function);
    }
    default R visitInteger(long integer) {
        return otherwise(integer);
    }
    default R visitNil(Nil nil) {
        return otherwise(nil);
    }
    default R visitQuote(SQuote quote) {
        return otherwise(quote);
    }
    default R visitString(String string) {
        return otherwise(string);
    }
    default R visitUnquote(SUnquote unquote) {
        return otherwise(unquote);
    }
    default R visitMany(SMany many) {
        return otherwise(many);
    }
    default R visitList(SList list) {
        return visitMany(list);
    }
    default R visitSet(SSet set) {
        return visitMany(set);
    }
    default R visitVector(SVector vector) {
        return visitMany(vector);
    }

    @Override
    default R apply(Object expression) {
        if (expression == null) {
            return visitNil(Nil.NIL);
        }
        if (expression instanceof Boolean) {
            if ((Boolean) expression) {
                return visitAtom(SAtom.of("true"));
            } else {
                return visitNil(Nil.NIL);
            }
        }
        if (expression instanceof SAtom) {
            return visitAtom((SAtom) expression);
        }
        if (expression instanceof Float || expression instanceof Double) {
            return visitDecimal(((Number) expression).doubleValue());
        }
        if (expression instanceof SFunction) {
            return visitFunction((SFunction) expression);
        }
        if (expression instanceof Byte || expression instanceof Short || expression instanceof Integer || expression instanceof Long) {
            return visitInteger(((Number) expression).longValue());
        }
        if (expression == nil) {
            return visitNil(nil);
        }
        if (expression instanceof SQuote) {
            return visitQuote((SQuote) expression);
        }
        if (expression instanceof String) {
            return visitString((String) expression);
        }
        if (expression instanceof SUnquote) {
            return visitUnquote((SUnquote) expression);
        }
        if (expression instanceof SList) {
            return visitList((SList) expression);
        }
        if (expression instanceof SSet) {
            return visitSet((SSet) expression);
        }
        if (expression instanceof SVector) {
            return visitVector((SVector) expression);
        }
        return otherwise(expression);
    }
}
