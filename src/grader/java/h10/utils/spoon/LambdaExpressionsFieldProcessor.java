package h10.utils.spoon;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtExecutableReferenceExpression;
import spoon.reflect.code.CtLambda;
import spoon.reflect.declaration.CtField;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a processor that scans lambda exressions in a field.
 *
 * @author Arianne Roselina Prananto
 */
public class LambdaExpressionsFieldProcessor extends AbstractProcessor<CtField<?>> {

    /**
     * The field name to look for constructor calls instantiation.
     */
    private final String fieldName;
    /**
     * Contains all lambda expressions.
     */
    private final List<CtLambda<?>> lambdas;
    /**
     * Contains all method references.
     */
    private final List<CtExecutableReferenceExpression<?, ?>> references;

    /**
     * Constructs and initializes a processor which scans all meth lambda expressions in the specified field.
     *
     * @param fieldName the name of the field that should be processed
     */
    public LambdaExpressionsFieldProcessor(final String fieldName) {
        this.fieldName = fieldName;
        this.lambdas = new ArrayList<>();
        this.references = new ArrayList<>();
    }

    @Override
    public void process(final CtField<?> field) {
        lambdas.addAll(
            field.getElements(
                (CtLambda<?> lambda) -> field.getSimpleName().equals(fieldName)
                             )
                      );
        references.addAll(
            field.getElements(
                (CtExecutableReferenceExpression<?, ?> methodReference) -> field.getSimpleName().equals(fieldName)
                             )
                         );
    }

    /**
     * Returns the scanned lambda expressions so far. If this processor does not process any field yet, the content will
     * be empty.
     *
     * @return the scanned constructor calls  so far
     */
    public List<CtLambda<?>> getLambdas() {
        return lambdas;
    }

    /**
     * Returns the scanned method references so far. If this processor does not process any field yet, the content will
     * be  empty.
     *
     * @return the scanned constructor calls  so far
     */
    public List<CtExecutableReferenceExpression<?, ?>> getReferences() {
        return references;
    }
}
