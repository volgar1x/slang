package slang.visitors;

import slang.SList;
import slang.Visitor;

import java.util.stream.Collectors;

/**
 * @author Antoine Chauvin
 */
public enum Printer implements Visitor<String> {
    INSTANCE;

    public static String print(Object object) {
        return INSTANCE.apply(object);
    }



    @Override
    public String otherwise(Object expression) {
        return expression.toString();
    }

    @Override
    public String visitList(SList list) {
        return list.stream().map(this).collect(Collectors.joining(" "));
    }
}
