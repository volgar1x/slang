package slang.visitors;

import slang.Nil;
import slang.SMany;
import slang.Visitor;

/**
 * @author Antoine Chauvin
 */
public enum Truth implements Visitor<Boolean> {
    INSTANCE;

    public static boolean truthy(Object o) {
        return INSTANCE.apply(o);
    }

    @Override
    public Boolean otherwise(Object expression) {
        return Boolean.TRUE;
    }

    @Override
    public Boolean visitDecimal(double decimal) {
        return decimal != 0;
    }

    @Override
    public Boolean visitInteger(long integer) {
        return integer != 0;
    }

    @Override
    public Boolean visitNil(Nil nil) {
        return false;
    }

    @Override
    public Boolean visitString(String string) {
        return !string.isEmpty();
    }

    @Override
    public Boolean visitMany(SMany many) {
        return !many.isEmpty();
    }


}
