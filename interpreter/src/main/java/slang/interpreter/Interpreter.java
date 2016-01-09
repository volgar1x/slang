package slang.interpreter;

import slang.*;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Antoine Chauvin
 */
public class Interpreter extends EvaluationContext implements Visitor<Object> {
    public Interpreter(ClassLoader classLoader) {
        super(classLoader);
        Stdlib.load(this);
    }

    Interpreter(EvaluationContextInterface parent) {
        super(parent);
    }

    @Override
    public EvaluationContextInterface linkTo(EvaluationContextInterface parent) {
        return new Interpreter(parent);
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
    public Object visitName(SName name) {
        if (isJavaStaticFieldCall(name)) {
            return doJavaStaticFieldCall(name);
        }
        return read(name);
    }

    @Override
    public Object visitQuote(SQuote quote) {
        return quote.quoted;
    }

    @Override
    public Object visitUnquote(SUnquote unquote) {
        throw new SException("tried to unquote while interpreting");
    }

    @Override
    public Object visitList(SList list) {
        if (list.stream().allMatch(x -> x instanceof SList)) {
            return list.map(this);
        }

        SFunction function;

        if (list.head() instanceof SFunction) {
            function = (SFunction) list.head();
        } else if (list.head() instanceof SName) {
            SName functionName = (SName) list.head();

            if (isJavaConstructor(functionName)) {
                return doJavaConstructor(functionName, list.tail().map(this));
            } else if (isJavaInstanceMethodCall(functionName)) {
                return doJavaInstanceMethodCall(functionName, list.tail().map(this));
            } else if (isJavaStaticFunctionCall(functionName)) {
                return doJavaStaticMethodCall(functionName, list.tail().map(this));
            } else if (isJavaStaticFieldCall(functionName)) {
                return doJavaStaticFieldCall(functionName);
            }

            if (!present(functionName)) {
                throw new SException(String.format("undefined function `%s'", functionName));
            }

            Object var = read(functionName);

            if (!(var instanceof SFunction)) {
                throw new SException(String.format("function `%s':%s is not callable", functionName, var.getClass().getSimpleName()));
            }

            function = (SFunction) var;
        } else {
            throw new IllegalArgumentException(String.format("%s is not a function", list.head()));
        }

        if (function.evaluateArguments()) {
            return function.call(link(), list.tail().map(this));
        } else {
            return function.call(this, list.tail());
        }
    }

    private boolean isJavaConstructor(SName functionName) {
        String string = functionName.toString();

        if (string.charAt(string.length() - 1) != '.') {
            return false;
        }

        String className = string.substring(0, string.length() - 1);

        try {
            getClassLoader().loadClass(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private boolean isJavaInstanceMethodCall(SName functionName) {
        String string = functionName.toString();

        if (string.charAt(0) != '.') {
            return false;
        }

        for (int i = 1; i < string.length(); i++) {
            if (!Character.isJavaIdentifierPart(string.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    private boolean isJavaStaticFunctionCall(SName functionName) {
        String string = functionName.toString();

        int sep = string.indexOf('/');
        if (sep < 0) {
            return false;
        }

        String className = string.substring(0, sep);
        String methodName = string.substring(sep + 1);

        try {
            Class<?> klass = getClassLoader().loadClass(className);

            for (Method method : klass.getMethods()) {
                if (method.getName().equals(methodName)) {
                    return true;
                }
            }

            return false;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private boolean isJavaStaticFieldCall(SName functionName) {
        String string = functionName.toString();

        int sep = string.indexOf('/');
        if (sep < 0) {
            return false;
        }

        String className = string.substring(0, sep);
        String fieldName = string.substring(sep + 1);

        try {
            Class<?> klass = getClassLoader().loadClass(className);
            Field field = klass.getField(fieldName);
            return (field.getModifiers() & Modifier.STATIC) != 0;
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            return false;
        }
    }

    private Object doJavaConstructor(SName functionName, SList arguments) {
        String className = functionName.toString();
        className = className.substring(0, className.length() - 1);

        try {
            Class<?> klass = getClassLoader().loadClass(className);
            Constructor<?> constructor = findByArguments(klass.getConstructors(), arguments, className, false);
            if (constructor == null) {
                throw new SException(String.format("`%s' constructor not found", functionName));
            }

            Object[] parameters = asParameterArray(constructor, arguments);

            return constructor.newInstance(parameters);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new Error(e);
        } catch (InvocationTargetException e) {
            throw new SException(e.getCause());
        }
    }

    private Object doJavaInstanceMethodCall(SName functionName, SList arguments) {
        String methodName = functionName.toString().substring(1);

        Object receiver = arguments.head();
        Class<?> klass = receiver.getClass();

        Method method = findByArguments(klass.getDeclaredMethods(), arguments.tail(), methodName, false);
        if (method == null) {
            method = findByArguments(klass.getMethods(), arguments.tail(), methodName, false);
        }
        if (method == null) {
            throw new SException(String.format("`%s' instance method not found on `%s'",
                    methodName + arguments.tail().stream().map(x -> x.getClass().getName()).collect(Collectors.joining(", ", "(", ")")),
                    klass.getName()));
        }

        Object[] parameters = asParameterArray(method, arguments.tail());

        try {
            method.setAccessible(true);
            return method.invoke(receiver, parameters);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        } catch (InvocationTargetException e) {
            throw new SException(e.getCause());
        }
    }

    private Object doJavaStaticMethodCall(SName functionName, SList arguments) {
        String string = functionName.toString();
        int sep = string.indexOf('/');
        String className = string.substring(0, sep);
        String methodName = string.substring(sep + 1);

        try {
            Class<?> klass = getClassLoader().loadClass(className);

            Method method = findByArguments(klass.getDeclaredMethods(), arguments, methodName, true);
            if (method == null) {
                throw new SException(String.format("`%s' static method not found on `%s'", methodName, className));
            }

            Object[] parameters = asParameterArray(method, arguments);

            return method.invoke(null, parameters);
        } catch (ClassNotFoundException | IllegalAccessException e) {
            throw new Error(e);
        } catch (InvocationTargetException e) {
            throw new SException(e.getCause());
        }
    }

    private Object doJavaStaticFieldCall(SName functionName) {
        String string = functionName.toString();
        int sep = string.indexOf('/');
        String className = string.substring(0, sep);
        String fieldName = string.substring(sep + 1);

        try {
            Class<?> klass = getClassLoader().loadClass(className);

            Field field = klass.getField(fieldName);
            return field.get(null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            throw new SException(e);
        }
    }

    private <T extends Executable> T findByArguments(T[] executables, SList arguments, String name, boolean statik) {
        int arity = arguments.size();

        for (T executable : executables) {
            if ((executable.getModifiers() & Modifier.STATIC) == 0 && statik) {
                continue;
            }

            if (!executable.getName().equals(name)) {
                continue;
            }

            if (executable.getParameterCount() != arity) {
                continue;
            }

            SList it = arguments;
            boolean parametersCoerceable = true;
            for (Class<?> parameterType : executable.getParameterTypes()) {
                if (!canCoerce(it.head(), parameterType)) {
                    parametersCoerceable = false;
                    break;
                }
                it = it.tail();
            }

            if (parametersCoerceable) {
                return executable;
            }
        }

        return null;
    }

    private Object[] asParameterArray(Executable executable, SList arguments) {
        Object[] parameters = new Object[arguments.size()];
        int i = 0;
        for (Object argument : arguments) {
            Class<?> parameterType = executable.getParameterTypes()[i];
            parameters[i++] = coerce(argument, parameterType);
        }
        return parameters;
    }

    private boolean canCoerce(Object from, Class<?> to) {
        return new Visitor<Boolean>() {
            @Override
            public Boolean visitAtom(SAtom atom) {
                if (atom.equals(SAtom.of("true")) && (Boolean.class.isAssignableFrom(to) || boolean.class.isAssignableFrom(to))) {
                    return true;
                }
                return otherwise(atom);
            }

            @Override
            public Boolean visitDecimal(double decimal) {
                if (Float.class.isAssignableFrom(to) || float.class.isAssignableFrom(to)) {
                    return true;
                }
                if (double.class.isAssignableFrom(to)) {
                    return true;
                }
                if (BigDecimal.class.isAssignableFrom(to)) {
                    return true;
                }
                return otherwise(decimal);
            }

            @Override
            public Boolean visitInteger(long integer) {
                if (Byte.class.isAssignableFrom(to) || byte.class.isAssignableFrom(to)) {
                    return true;
                }
                if (Short.class.isAssignableFrom(to) || short.class.isAssignableFrom(to)) {
                    return true;
                }
                if (Integer.class.isAssignableFrom(to) || int.class.isAssignableFrom(to)) {
                    return true;
                }
                if (long.class.isAssignableFrom(to)) {
                    return true;
                }
                if (BigInteger.class.isAssignableFrom(to)) {
                    return true;
                }
                return otherwise(integer);
            }

            @Override
            public Boolean visitNil(Nil nil) {
                // can coerce nil to a boolean
                if (boolean.class.isAssignableFrom(to) || Boolean.class.isAssignableFrom(to)) {
                    return true;
                }

                // cannot coerce nil to a value type
                if (char.class.isAssignableFrom(to) || byte.class.isAssignableFrom(to)
                        || short.class.isAssignableFrom(to) || int.class.isAssignableFrom(to)
                        || long.class.isAssignableFrom(to) || float.class.isAssignableFrom(to)
                        || double.class.isAssignableFrom(to)) {
                    return false;
                }
                // can coerce nil to reference type
                return true;
            }

            @Override
            public Boolean visitFunction(SFunction function) {
                return to.isInterface() &&
                        Stream.of(to.getDeclaredMethods())
                                .filter(x -> Modifier.isAbstract(x.getModifiers()))
                                .count() == 1L;
            }

            @Override
            public Boolean otherwise(Object expression) {
                return to.isInstance(expression);
            }
        }.apply(from);
    }

    private Object coerce(Object from, Class<?> to) {
        return new Visitor<Object>() {
            @Override
            public Object visitAtom(SAtom atom) {
                if (atom.equals(SAtom.of("true")) && (Boolean.class.isAssignableFrom(to) || boolean.class.isAssignableFrom(to))) {
                    return true;
                }
                return otherwise(atom);
            }

            @Override
            public Object visitDecimal(double decimal) {
                if (Float.class.isAssignableFrom(to)) {
                    return true;
                }
                if (BigDecimal.class.isAssignableFrom(to)) {
                    return true;
                }
                return otherwise(decimal);
            }

            @Override
            public Object visitInteger(long integer) {
                if (Byte.class.isAssignableFrom(to) || byte.class.isAssignableFrom(to)) {
                    return (byte) integer;
                }
                if (Short.class.isAssignableFrom(to) || short.class.isAssignableFrom(to)) {
                    return (short) integer;
                }
                if (Integer.class.isAssignableFrom(to) || int.class.isAssignableFrom(to)) {
                    return (int) integer;
                }
                if (BigInteger.class.isAssignableFrom(to)) {
                    return BigInteger.valueOf(integer);
                }
                return otherwise(integer);
            }

            @Override
            public Object visitNil(Nil nil) {
                if (Boolean.class.isAssignableFrom(to) || boolean.class.isAssignableFrom(to)) {
                    return Boolean.FALSE;
                }
                if (Iterable.class.isAssignableFrom(to)) {
                    return nil;
                }
                return null;
            }

            @Override
            public Object visitFunction(SFunction function) {
                return Proxy.newProxyInstance(getClassLoader(), new Class<?>[]{to},
                        (proxy, method, args) -> {
                            Object result = function.call(Interpreter.this, SList.of(args));
                            return coerce(result, method.getReturnType());
                        });
            }

            @Override
            public Object otherwise(Object expression) {
                return expression;
            }
        }.apply(from);
    }
}
