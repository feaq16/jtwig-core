package org.jtwig.model.expression;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import org.jtwig.context.RenderContext;
import org.jtwig.exceptions.CalculationException;
import org.jtwig.model.position.Position;
import org.jtwig.util.ErrorMessageFormatter;
import org.jtwig.value.JtwigValue;
import org.jtwig.value.JtwigValueFactory;

import java.util.Collection;

public class ComprehensionListExpression extends Expression {
    private final Expression start;
    private final Expression end;

    public ComprehensionListExpression(Position position, Expression start, Expression end) {
        super(position);
        this.start = start;
        this.end = end;
    }



    @Override
    public JtwigValue calculate(final RenderContext context) {
        final JtwigValue startValue = start.calculate(context);
        final JtwigValue endValue = end.calculate(context);
        return context.configuration().enumerationStrategy().enumerate(startValue, endValue)
                .transform(new Function<Collection<Object>, JtwigValue>() {
                    @Override
                    public JtwigValue apply(Collection<Object> input) {
                        return JtwigValueFactory.value(input, context.configuration().valueConfiguration());
                    }
                })
                .or(new Supplier<JtwigValue>() {
                    @Override
                    public JtwigValue get() {
                        throw new CalculationException(ErrorMessageFormatter.errorMessage(getPosition(), String.format("Unable to calculate a list from a comprehension list starting with '%s' and ending with '%s'", startValue.asObject(), endValue.asObject())));
                    }
                });
    }
}
