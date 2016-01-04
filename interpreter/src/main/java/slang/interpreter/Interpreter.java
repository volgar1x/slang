package slang.interpreter;

import slang.*;

import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Collectors;

/**
 * @author Antoine Chauvin
 */
public class Interpreter extends EvaluationContext implements Visitor<Object> {
    public Interpreter(ClassLoader classLoader, InputStream stdin, PrintStream stdout, PrintStream stderr) {
        super(classLoader, stdin, stdout, stderr);
        Stdlib.load(this);
    }

    Interpreter(EvaluationContextInterface parent) {
        super(parent);
    }

    @Override
    public EvaluationContextInterface link() {
        return new Interpreter(this);
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
    public Object visitAtom(SAtom atom) {
        return read(atom);
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
        SAtom functionName = (SAtom) list.head();

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

        SFunction function = (SFunction) var;

        if (function.evaluateArguments()) {
            return function.call(link(), list.tail().map(this));
        } else {
            return function.call(this, list.tail());
        }
    }

    private boolean isJavaConstructor(SAtom functionName) {
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

    private boolean isJavaInstanceMethodCall(SAtom functionName) {
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

    private boolean isJavaStaticFunctionCall(SAtom functionName) {
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

    private boolean isJavaStaticFieldCall(SAtom functionName) {
        String string = functionName.toString();

        int sep = string.indexOf('#');
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

    private Object doJavaConstructor(SAtom functionName, SList arguments) {
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
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new SException(e);
        }
    }

    private Object doJavaInstanceMethodCall(SAtom functionName, SList arguments) {
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
            return method.invoke(receiver, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new SException(e);
        }
    }

    private Object doJavaStaticMethodCall(SAtom functionName, SList arguments) {
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
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            throw new SException(e);
        }
    }

    private Object doJavaStaticFieldCall(SAtom functionName) {
        String string = functionName.toString();
        int sep = string.indexOf('#');
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
                if (atom.equals(SAtom.of("true")) && Boolean.class.isAssignableFrom(to)) {
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
                // can always coerce nil to null
                return true;
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
                if (atom.equals(SAtom.of("true")) && Boolean.class.isAssignableFrom(to)) {
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
                if (Iterable.class.isAssignableFrom(to)) {
                    return nil;
                }
                return null;
            }

            @Override
            public Object otherwise(Object expression) {
                return expression;
            }
        }.apply(from);
    }
}
