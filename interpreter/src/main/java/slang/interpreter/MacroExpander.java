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
            SFunction function = SFn.tailCallOptimized(macro);
            register(function.getFunctionName(), function);
            return SList.nil;
        }

        if (!hasOwn(functionName)) {
            return list.map(this);
        }

        SFunction macro = (SFunction) read(functionName);

        return macro.call(new UnquotingInterpreter(this), list.tail());
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

    private static class UnquotingInterpreter extends Interpreter {

        private final MacroExpander expander;

        UnquotingInterpreter(EvaluationContextInterface parent, MacroExpander expander) {
            super(parent);
            this.expander = expander;
        }

        UnquotingInterpreter(MacroExpander expander) {
            this(expander, expander);
        }

        @Override
        public EvaluationContextInterface link() {
            return new UnquotingInterpreter(this, expander);
        }

        @Override
        public Object visitQuote(SQuote quote) {
            return expander.apply(expander.unquote(this, quote.quoted));
        }
    }
}