package slang.interpreter;

import slang.expressions.*;
import slang.expressions.visitors.Inspector;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Antoine Chauvin
 */
public class Coercion {
    public final static class ToJava implements Visitor<Object> {
        private final Class<?> expected;

        public ToJava(Class<?> expected) {
            this.expected = expected;
        }

        public static Object coerce(ExpressionInterface expression, Class<?> expected) {
            return expression.visit(new ToJava(expected));
        }

        @Override
        public Object otherwise(ExpressionInterface expression) {
            throw new SlangException("cannot coerce " + Inspector.inspect(expression));
        }

        @Override
        public Object visitAtom(AtomExpression atom) {
            if (expected == boolean.class || expected == Boolean.class) {
                return true;
            }
            return atom;
        }

        @Override
        public Object visitDecimal(DecimalExpression decimal) {
            if (expected == float.class || expected == Float.class) {
                return decimal.asDecimal().floatValue();
            }
            if (expected == double.class || expected == Double.class) {
                return decimal.asDecimal().doubleValue();
            }
            return decimal.asDecimal();
        }

        @Override
        public Object visitInteger(IntegerExpression integer) {
            if (expected == byte.class || expected == Byte.class) {
                return integer.asInteger().byteValueExact();
            }
            if (expected == short.class || expected == Short.class) {
                return integer.asInteger().shortValueExact();
            }
            if (expected == int.class || expected == Integer.class) {
                return integer.asInteger().intValueExact();
            }
            if (expected == long.class || expected == Long.class) {
                return integer.asInteger().longValueExact();
            }
            return integer.asInteger();
        }

        @Override
        public Object visitNil(NilExpression nil) {
            if (expected == boolean.class || expected == Boolean.class) {
                return false;
            }
            return null;
        }

        @Override
        public Object visitQuote(QuoteExpression quote) {
            return quote.getExpression();
        }

        @Override
        public Object visitString(StringExpression string) {
            return string.getString();
        }

        @Override
        public Object visitList(ListExpression list) {
            List<Object> result = new ArrayList<>();

            ListExpression cur = list;
            while (!cur.isEmpty()) {
                result.add(cur.getHead().visit(this));
                cur = cur.getTail();
            }

            return result;
        }

        @Override
        public Object visitSet(SetExpression set) {
            Set<Object> result = new HashSet<>();

            for (ExpressionInterface expression : set.getExpressions()) {
                result.add(expression.visit(this));
            }

            return result;
        }

        @Override
        public Object visitVector(VectorExpression vector) {
            Object[] result = new Object[vector.length()];

            vector.iterate((i, expression) -> {
                result[i] = expression.visit(this);
            });

            return result;
        }
    }

    public static class FromJava {
        public static ExpressionInterface coerce(Object object) {
            if (object == null) {
                return NilExpression.NIL;
            }
            if (object instanceof Boolean) {
                return BooleanExpression.from((Boolean) object);
            }
            if (object instanceof AtomExpression) {
                return (AtomExpression) object;
            }
            if (object instanceof ExpressionInterface) {
                return new QuoteExpression((ExpressionInterface) object);
            }
            if (object instanceof BigDecimal) {
                return new DecimalExpression((BigDecimal) object);
            }
            if (object instanceof BigInteger) {
                return new IntegerExpression(((BigInteger) object));
            }
            if (object instanceof Float || object instanceof Double) {
                return new DecimalExpression(new BigDecimal(object.toString()));
            }
            if (object instanceof Byte || object instanceof Short || object instanceof Integer || object instanceof Long) {
                return new IntegerExpression(new BigInteger(object.toString()));
            }
            if (object instanceof String) {
                return new StringExpression((String) object);
            }
            if (object instanceof List) {
                ListExpression.Builder builder = new ListExpression.Builder();

                for (Object o : ((List) object)) {
                    builder.add(coerce(o));
                }

                return builder.build();
            }
            if (object instanceof Set) {
                SetExpression.Builder builder = new SetExpression.Builder();

                for (Object o : ((Set) object)) {
                    builder.add(coerce(o));
                }

                return builder.build();
            }
            if (object instanceof Object[]) {
                VectorExpression.Builder builder = new VectorExpression.Builder();

                for (Object o : ((Object[]) object)) {
                    builder.add(coerce(o));
                }

                return builder.build();
            }

            throw new SlangException("cannot coerce from java " + object);
        }
    }
}
