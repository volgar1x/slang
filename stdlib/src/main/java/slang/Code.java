package slang;

import slang.expressions.*;
import slang.expressions.visitors.Truthy;
import slang.parser.MacroExpander;
import slang.parser.Parser;
import slang.tokenizer.Tokenizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Antoine Chauvin
 */
class Code {
    public static ExpressionInterface eval(EvaluationContextInterface context, ListExpression arguments) {
        return arguments.map(context);
    }

    public static ExpressionInterface load(EvaluationContextInterface context, ListExpression arguments) {
        String filename = ((StringExpression) arguments.getHead()).getString();
        Path path = Paths.get(filename);
        try {
            Parser parser = new Parser(new Tokenizer(Files.newInputStream(path)));
            MacroExpander expander = new MacroExpander(context);

            ListExpression.Builder builder = new ListExpression.Builder();
            while (parser.hasNext()) {
                ExpressionInterface result = expander.evaluate(parser.next());
                if (Truthy.truthy(result)) {
                    builder.add(result);
                }
            }

            return builder.build();
        } catch (IOException e) {
            throw new SlangException("cannot read from `" + path + "'", e);
        }
    }
}
