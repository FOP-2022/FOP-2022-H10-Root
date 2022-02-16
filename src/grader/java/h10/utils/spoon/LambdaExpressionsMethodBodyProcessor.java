package h10.utils.spoon;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtExecutableReferenceExpression;
import spoon.reflect.code.CtLambda;
import spoon.reflect.declaration.CtMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a processor that scans lambda exressions in a method.
 *
 * @author Nhan Huynh, Darya Nikitina
 */
public class LambdaExpressionsMethodBodyProcessor extends AbstractProcessor<CtMethod<?>> {

    /**
     * The method name to look for constructor calls instantiation.
     */
    private final String methodName;
    /**
     * Contains all lambda expressions.
     */
    private final List<CtLambda<?>> lambdas;
    /**
     * Contains all method references.
     */
    private final List<CtExecutableReferenceExpression<?, ?>> references;

    /**
     * Constructs and initializes a processor which scans all meth lambda expressions in the specified method.
     *
     * @param methodName the name of the method that should be processed
     */
    public LambdaExpressionsMethodBodyProcessor(final String methodName) {
        this.methodName = methodName;
        this.lambdas = new ArrayList<>();
        this.references = new ArrayList<>();
    }

    @Override
    public void process(final CtMethod<?> method) {
        lambdas.addAll(
            method.getElements(
                (CtLambda<?> lambda) -> method.getSimpleName().equals(methodName)
            )
        );
        references.addAll(
            method.getElements(
                (CtExecutableReferenceExpression<?, ?> methodReference) -> method.getSimpleName().equals(methodName)
            )
        );
    }

    /**
     * Returns the scanned lambda expressionss so far. If this processor does not process any method yet, the content will be
     * empty.
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
