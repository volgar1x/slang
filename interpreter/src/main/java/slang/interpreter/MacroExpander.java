package slang.interpreter;

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
    public Object visitQuote(SQuote quote) {
        return new SQuote(evaluate(quote.quoted));
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
            register(macro.getFunctionName(), macro);
            return SList.nil;
        }

        if (!hasOwn(functionName)) {
            return list.map(this);
        }

        SFunction macro = (SFunction) read(functionName);

        class test extends Interpreter {

            test(EvaluationContextInterface parent) {
                super(parent);
            }

            @Override
            public EvaluationContextInterface link() {
                return new test(this);
            }

            @Override
            public Object visitQuote(SQuote quote) {
                return MacroExpander.this.apply(unquote(this, quote.quoted));
            }
        }

        return macro.call(new test(this), list.tail());
    }

    private Object unquote(EvaluationContextInterface context, Object expression) {
        return new Visitor<Object>() {
            @Override
            public Object otherwise(Object expression) {
                return expression;
            }

            @Override
            public Object visitUnquote(SUnquote unquote) {
                return context.read(unquote.name);
            }

            @Override
            public Object visitMany(SMany many) {
                return many.map(this);
            }
        }.apply(expression);
    }
}