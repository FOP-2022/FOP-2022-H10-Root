package h10.utils.spoon;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a processor that scans statements in a method.
 *
 * @author Arianne Roselina Prananto
 */
public class StatementsMethodBodyProcessor extends AbstractProcessor<CtMethod<?>> {

    /**
     * The method name to look for constructor calls instantiation.
     */
    private final String methodName;
    /**
     * Contains all statements.
     */
    private final List<CtStatement> statements;

    /**
     * Constructs and initializes a processor which scans all statements in the specified method.
     *
     * @param methodName the name of the method that should be processed
     */
    public StatementsMethodBodyProcessor(final String methodName) {
        this.methodName = methodName;
        this.statements = new ArrayList<>();
    }

    @Override
    public void process(final CtMethod<?> method) {
        statements.addAll(
            method.getElements(
                (CtStatement statement) -> method.getSimpleName().equals(methodName)
            )
        );
    }

    /**
     * Returns the scanned statements so far. If this processor does not process any method yet, the content will be
     * empty.
     *
     * @return the scanned constructor calls so far
     */
    public List<CtStatement> getStatements() {
        return statements;
    }
}
