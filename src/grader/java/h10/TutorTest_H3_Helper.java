package h10;

import h10.utils.spoon.LambdaExpressionsMethodBodyProcessor;
import h10.utils.spoon.SpoonUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sourcegrade.jagr.api.testing.TestCycle;
import org.sourcegrade.jagr.api.testing.extension.JagrExecutionCondition;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;
import spoon.reflect.declaration.CtTypedElement;

import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Define some helper methods for task H3.
 *
 * @author Arianne Roselina Prananto
 */
public final class TutorTest_H3_Helper {

    /* *********************************************************************
     *                       Define some generators                        *
     **********************************************************************/

    protected Number[] generateManyNumbers(int numElems) {
        Number[] ints = new Number[numElems];
        for (int i = 0; i < numElems; i++) {
            ints[i] = new Random().nextInt();
        }
        return ints;
    }

    protected Integer[][] generateManyIntegerArrays(int numElems, int numArrays) {
        Integer[][] intArrays = new Integer[numArrays][numElems];
        for (int i = 0; i < numArrays; i++) {
            for (int j = 0; j < numElems; j++) {
                intArrays[i][j] = new Random().nextInt();
            }
        }
        return intArrays;
    }

    protected String[] generateManyStrings(int numElems) {
        String[] strings = new String[numElems];
        for (int i = 0; i < numElems; i++) {
            if (i % 2 == 0) {
                strings[i] = String.valueOf(new Random().nextInt());
            } else {
                strings[i] = "hello";
            }
        }
        return strings;
    }

    /* *********************************************************************
     *               Define expected functions and predicates              *
     **********************************************************************/

    protected Integer expectedFct(Integer[] array) {
        Integer result = 0;
        for (Integer a : array) {
            result += a;
        }
        return result;
    }

    protected boolean expectedPredT(Integer[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                if (i != j && array[i] % array[j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean expectedPredU(Integer integer) {
        return integer >= 0;
    }

    /* *********************************************************************
     *                           Assertion methods                         *
     **********************************************************************/

    protected void assertObjects(Object expected, Object actual) {
        assertEquals(expected, actual, "Assertion fails with an expected value: " + expected + ", but was: " + actual);
    }

    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    protected void assertLambdas(final TestCycle testCycle, Class<?> classType, String fieldName, String expected) {
        var path = String.format("%s.java", classType.getCanonicalName().replaceAll("\\.", "/"));
        var processor = SpoonUtils.process(testCycle, path,
                                           new LambdaExpressionsMethodBodyProcessor(fieldName));
        var allLambdas = processor.getLambdas();

        assertEquals(1, allLambdas.size(), "Expected 1 lambda, but was: " + allLambdas.size());

        var actual = allLambdas.stream()
            .map(CtTypedElement::getType)
            .collect(Collectors.toList())
            .get(0)
            .toString();
        assertEquals(expected, actual, "Assertion fails with an expected lambda type: " + expected
                                       + ", but was: " + actual);
    }

    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    protected void assertNoLambda(final TestCycle testCycle, Class<?> classType, String fieldName) {
        var path = String.format("%s.java", classType.getCanonicalName().replaceAll("\\.", "/"));
        var processor = SpoonUtils.process(testCycle, path,
                                           new LambdaExpressionsMethodBodyProcessor(fieldName));
        var allLambdas = processor.getLambdas();
        assertEquals(0, allLambdas.size(),
                     "Assertion fails with expected number of lambdas: 0, but was: " + allLambdas.size());
    }
}
