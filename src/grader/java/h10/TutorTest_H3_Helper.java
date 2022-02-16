package h10;

import h10.utils.TutorTest_Messages;
import h10.utils.spoon.LambdaExpressionsFieldProcessor;
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

    protected static Number[] generateManyNumbers() {
        Number[] ints = new Number[50];
        for (int i = 0; i < 50; i++) {
            ints[i] = new Random().nextInt();
        }
        return ints;
    }

    protected static Integer[][] generateManyIntegerArrays() {
        Integer[][] intArrays = new Integer[50][10];
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 10; j++) {
                intArrays[i][j] = new Random().nextInt();
            }
        }
        return intArrays;
    }

    protected static String[] generateManyStrings() {
        String[] strings = new String[50];
        for (int i = 0; i < 50; i++) {
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

    protected static Integer expectedFct(Integer[] array) {
        Integer result = 0;
        for (Integer a : array) {
            result += a;
        }
        return result;
    }

    protected static boolean expectedPredT(Integer[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                if (i != j && array[i] % array[j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    protected static boolean expectedPredU(Integer integer) {
        return integer >= 0;
    }

    /* *********************************************************************
     *                           Assertion methods                         *
     **********************************************************************/

    protected static void assertObjects(Object expected, Object actual) {
        assertEquals(expected, actual, "Assertion failed");
    }

    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    protected static void assertLambdas(final TestCycle testCycle, Class<?> classType, String fieldName, String expected) {
        var path = String.format("%s.java", classType.getCanonicalName().replaceAll("\\.", "/"));
        var processor = SpoonUtils.process(testCycle, path,
                                           new LambdaExpressionsFieldProcessor(fieldName));
        var allLambdas = processor.getLambdas();
        var allMethodReferences = processor.getReferences();

        // check both normal lambdas and method reference lambdas
        assertEquals(1, allLambdas.size() + allMethodReferences.size(),
                     TutorTest_Messages.assertLambdaFailed(fieldName));

        var actual = ((allLambdas.size() == 0) ? allMethodReferences : allLambdas).stream()
            .map(CtTypedElement::getType)
            .collect(Collectors.toList())
            .get(0)
            .toString();
        assertEquals(expected, actual, TutorTest_Messages.assertLambdaFailed(fieldName));
    }

    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    protected static void assertNoLambda(final TestCycle testCycle, Class<?> classType, String methodName) {
        var path = String.format("%s.java", classType.getCanonicalName().replaceAll("\\.", "/"));
        var processor = SpoonUtils.process(testCycle, path,
                                           new LambdaExpressionsMethodBodyProcessor(methodName));
        var allLambdas = processor.getLambdas();
        var allMethodReferences = processor.getReferences();
        assertEquals(0, allLambdas.size() + allMethodReferences.size(),
                     TutorTest_Messages.assertLambdaFailed(methodName));
    }
}
