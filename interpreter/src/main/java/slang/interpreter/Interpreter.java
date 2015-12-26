package slang.interpreter;

import slang.Stdlib;
import slang.expressions.*;

import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Antoine Chauvin
 */
public final class Interpreter extends EvaluationContext implements Visitor<ExpressionInterface> {
    public Interpreter(InputStream stdin, PrintStream stdout, PrintStream stderr) {
        super(stdin, stdout, stderr);
        Stdlib.load(this);
    }

    public Interpreter(EvaluationContextInterface parent) {
        super(parent);
    }

    @Override
    public EvaluationContextInterface link() {
        return new Interpreter(this);
    }

    @Override
    public ExpressionInterface evaluate(ExpressionInterface expression) {
        return expression.visit(this);
    }

    @Override
    public ExpressionInterface apply(ExpressionInterface expression) {
        return expression.visit(this);
    }

    @Override
    public ExpressionInterface visitQuote(QuoteExpression quote) {
        return quote.getExpression();
    }

    @Override
    public ExpressionInterface visitUnquote(UnquoteExpression unquote) {
        throw new IllegalStateException();
    }

    @Override
    public ExpressionInterface visitAtom(AtomExpression atom) {
        return atom.isValue() ? atom : read(atom.getAtom());
    }

    @Override
    public ExpressionInterface visitList(ListExpression list) {
        if (!(list.getHead() instanceof AtomExpression)) {
            return visitMany(list);
        }

        String identifier = ((AtomExpression) list.getHead()).getAtom();
        ListExpression functionArguments = list.getTail();

        FunctionInterface function = (FunctionInterface) readMaybe(identifier);
        if (function != null) {
            if (function instanceof SlangFunction) {
                return function.call(link(), functionArguments.map(this));
            }
            return function.call(this, functionArguments);
        } else if (identifier.startsWith(".")) {
            String methodName = identifier.substring(1);

            ExpressionInterface receiver = evaluate(functionArguments.getHead());
            ListExpression arguments = functionArguments.getTail().map(this);
            int arity = arguments.length();

            Object javaReceiver = Coercion.ToJava.coerce(receiver, null);
            Class<?> klass = javaReceiver.getClass();

            List<Method> methods = Stream.concat(Stream.of(klass.getDeclaredMethods()),
                    Stream.of(klass.getMethods()))
                    .distinct()
                    .filter(x -> (x.getModifiers() & Modifier.STATIC) == 0)
                    .filter(x -> x.getName().equals(methodName))
                    .filter(x -> !x.isVarArgs() && x.getParameterCount() == arity)
                    .collect(Collectors.toList());

            if (methods.isEmpty()) {
                throw new SlangException(String.format(
                        "undefined function %s#%s/%d",
                        klass.getName(),
                        methodName,
                        arity
                ));
            } else if (methods.size() > 1) {
                throw new SlangException(String.format(
                        "ambiguous function call to %s#%s/%d",
                        klass.getName(),
                        methodName,
                        arity
                ));
            }

            Method method = methods.get(0);

            Object[] javaArguments = new Object[arity];
            ListExpression cur = arguments;
            int i = 0;
            while (!cur.isEmpty()) {
                javaArguments[i] = Coercion.ToJava.coerce(cur.getHead(), method.getParameterTypes()[i]);
                cur = cur.getTail();
            }

            Object result;
            try {
                result = method.invoke(javaReceiver, javaArguments);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new SlangException(e.getMessage(), e);
            }

            return Coercion.FromJava.coerce(result);
        } else {
            int sep = identifier.indexOf('/');
            if (sep < 0) {
                throw new SlangException("undefined function `" + identifier + "'");
            }
            String fullClassName = identifier.substring(0, sep);
            String methodName = identifier.substring(sep + 1);
            int arity = functionArguments.length();

            try {
                Class<?> klass = getClass().getClassLoader().loadClass(fullClassName);
                List<Method> methods = Stream.concat(Stream.of(klass.getDeclaredMethods()),
                        Stream.of(klass.getMethods()))
                        .distinct()
                        .filter(x -> (x.getModifiers() & Modifier.STATIC) != 0)
                        .filter(x -> x.getName().equals(methodName))
                        .filter(x -> !x.isVarArgs() && x.getParameterCount() == arity)
                        .collect(Collectors.toList());

                if (methods.isEmpty()) {
                    throw new SlangException(String.format(
                            "undefined function %s#%s/%d",
                            fullClassName,
                            methodName,
                            arity
                    ));
                } else if (methods.size() > 1) {
                    throw new SlangException(String.format(
                            "ambiguous function call to %s#%s/%d",
                            fullClassName,
                            methodName,
                            arity
                    ));
                }

                Method method = methods.get(0);

                Object[] arguments = new Object[arity];
                ListExpression cur = functionArguments;
                int i = 0;
                while (!cur.isEmpty()) {
                    arguments[i] = Coercion.ToJava.coerce(evaluate(cur.getHead()), method.getParameterTypes()[i]);
                    cur = cur.getTail();
                }

                Object result = method.invoke(null, arguments);

                return Coercion.FromJava.coerce(result);
            } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                throw new SlangException(String.format(
                        "undefined function %s#%s/%d",
                        fullClassName,
                        methodName,
                        arity
                ), e);
            }
        }
    }

    @Override
    public ExpressionInterface visitMany(ManyExpressionInterface many) {
        return many.map(this);
    }

    @Override
    public ExpressionInterface otherwise(ExpressionInterface expression) {
        return expression;
    }
}
