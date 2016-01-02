package slang.parser;

import slang.*;

/**
 * @author Antoine Chauvin
 */
public final class MacroExpander extends EvaluationContext implements Visitor<Object> {
    public MacroExpander(EvaluationContextInterface parent) {
        super(parent);
    }

    @Override
    public EvaluationContextInterface link() {
        return new MacroExpander(this);
    }

    @Override
    public Object evaluate(Object expression) {
        return Visitor.super.apply(expression);
    }

    @Override
    public Object apply(Object expression) {
        return Visitor.super.apply(expression);
    }

    @Override
    public Object otherwise(Object expression) {
        return expression;
    }

    @Override
    public Object visitUnquote(SUnquote unquote) {
        return read(unquote.name);
    }

    @Override
    public Object visitMany(SMany many) {
        return many.map(this);
    }

    @Override
    public Object visitList(SList list) {
        if (list.isEmpty() || !(list.head() instanceof SAtom)) {
            return list.map(this);
        }

        SAtom functionName = (SAtom) list.head();

        if (functionName.equals(SAtom.of("defmacro"))) {
            SFn macro = SFn.fromList(list.tail());
            SFn newMacro = macro.map(x -> x.map(this));
            SFunction function = SFn.tailCallOptimized(newMacro);
            register(function.getFunctionName(), function);
            return SList.nil;
        }

        if (!present(functionName)) {
            return list.map(this);
        }

        SFunction macro = (SFunction) read(functionName);

        return evaluate(macro.call(getParent().link(), list.tail()));
    }
}
