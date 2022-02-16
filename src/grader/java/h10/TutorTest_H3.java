package h10;

import h10.utils.TutorTest_Constants;
import h10.utils.TutorTest_Helper;
import h10.utils.TutorTest_Messages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.sourcegrade.jagr.api.testing.TestCycle;
import org.sourcegrade.jagr.api.testing.extension.JagrExecutionCondition;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Defines the JUnit test cases related to the class defined in the task H3.
 *
 * @author Arianne Roselina Prananto
 */
@TestForSubmission("h10")
@DisplayName("Criterion: H3")
public final class TutorTest_H3 {

    /* *********************************************************************
     *                               H3.1                                  *
     **********************************************************************/

    @Test
    public void testExtractTestSignatures() {
        var classH3 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_TEST);
        var method = TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_TEST_EXT, classH3);

        // class is public
        assertTrue(isPublic(classH3.getModifiers()),
                   TutorTest_Messages.classModifierIncorrect(TutorTest_Constants.CLASS_TEST));

        // is not generic
        assertEquals(0, method.getTypeParameters().length,
                     TutorTest_Messages.methodGeneric(TutorTest_Constants.METHOD_TEST_EXT));

        // method is public
        assertTrue(isPublic(method.getModifiers()),
                   TutorTest_Messages.methodModifierIncorrect(TutorTest_Constants.METHOD_TEST_EXT));

        // has no parameter
        assertEquals(0, method.getParameterCount(),
                     TutorTest_Messages.methodParamIncomplete(TutorTest_Constants.METHOD_TEST_EXT));

        // return type is correct
        assertEquals(void.class, method.getReturnType(),
                     TutorTest_Messages.methodReturnTypeIncorrect(TutorTest_Constants.METHOD_TEST_EXT));

        // thrown exception type is correct
        assertEquals(0, method.getExceptionTypes().length,
                     TutorTest_Messages.methodExceptionTypeIncorrect(TutorTest_Constants.METHOD_TEST_EXT));
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testParameterClasses(TestCycle testCycle) {
        var allClassesNamesSet = testCycle.getSubmission().getClassNames();
        var allClassesNames = allClassesNamesSet.stream()
            .filter(x -> !(x.equals(TutorTest_Constants.CLASS_LIST)
                           || x.equals(TutorTest_Constants.CLASS_ITEM)
                           || x.equals(TutorTest_Constants.CLASS_EXC)
                           || x.equals(TutorTest_Constants.CLASS_TEST)))
            .collect(Collectors.toList());

        // at least three other classes are found
        assertTrue(allClassesNames.size() >= 3,
                   TutorTest_Messages.classNotFound(TutorTest_Constants.ONE_OF_EXT));

        int found = 0;
        for (String className : allClassesNames) {
            var classH3 = TutorTest_Helper.getClass(className);

            var interfacesArray = classH3.getGenericInterfaces();
            if (interfacesArray.length < 1) {
                continue;
            }

            // check if this class implements one of the interfaces
            var interfaces = Arrays.stream(interfacesArray).map(Type::getTypeName).collect(Collectors.toList());
            boolean fct = interfaces.contains(TutorTest_Constants.FCT + "<java.lang.Integer[], java.lang.Integer>")
                          || interfaces.contains(TutorTest_Constants.FCT + "<java.lang.Integer[],java.lang.Integer>");
            boolean predT = interfaces.contains(TutorTest_Constants.PRED + "<java.lang.Integer[]>");
            boolean predU = interfaces.contains(TutorTest_Constants.PRED + "<java.lang.Integer>");

            if (!(fct || predT || predU)) {
                continue;
            }

            found++;

            if (fct) {
                // check if this method does the required implementation
                for (Method m : classH3.getDeclaredMethods()) {
                    if (m.getReturnType().equals(Integer.class)
                        && m.getParameterCount() == 1
                        && m.getParameters()[0].getType().equals(Integer[].class)) {
                        m.setAccessible(true);

                        // no lambda is used
                        TutorTest_H3_Helper.assertNoLambda(testCycle, classH3, m.getName());

                        // random inputs
                        Integer[][] intArrays = TutorTest_H3_Helper.generateManyIntegerArrays();

                        // check the method in fct class with many Integers Arrays
                        for (Integer[] ints : intArrays) {
                            Integer expected = TutorTest_H3_Helper.expectedFct(ints);
                            Integer actual = null;
                            try {
                                var object = classH3.getDeclaredConstructor().newInstance();
                                actual = (Integer) m.invoke(object, new Object[]{ints});
                            } catch (Exception e) {
                                fail(TutorTest_Messages.testingPredicates(className));
                            }
                            TutorTest_H3_Helper.assertObjects(expected, actual);
                        }
                    }
                }
            } else if (predT) {
                // check if this method does the required implementation
                for (Method m : classH3.getDeclaredMethods()) {
                    if (m.getReturnType().equals(boolean.class)
                        && m.getParameterCount() == 1
                        && m.getParameters()[0].getType().equals(Integer[].class)) {
                        m.setAccessible(true);

                        // no lambda is used
                        TutorTest_H3_Helper.assertNoLambda(testCycle, classH3, m.getName());

                        // random inputs
                        Integer[][] intArrays = TutorTest_H3_Helper.generateManyIntegerArrays();

                        // check the method in predT class with many Integer Arrays
                        for (Integer[] ints : intArrays) {
                            boolean expected = TutorTest_H3_Helper.expectedPredT(ints);
                            boolean actual = false;
                            try {
                                var object = classH3.getDeclaredConstructor().newInstance();
                                actual = (boolean) m.invoke(object, new Object[]{ints});
                            } catch (Exception e) {
                                fail(TutorTest_Messages.testingPredicates(className));
                            }
                            TutorTest_H3_Helper.assertObjects(expected, actual);
                        }
                    }
                }
            } else {
                for (Method m : classH3.getDeclaredMethods()) {
                    // check if this method does the required implementation
                    if (m.getReturnType().equals(boolean.class)
                        && m.getParameterCount() == 1
                        && m.getParameters()[0].getType().equals(Integer.class)) {
                        m.setAccessible(true);

                        // no lambda is used
                        TutorTest_H3_Helper.assertNoLambda(testCycle, classH3, m.getName());

                        // random inputs
                        Number[] ints = TutorTest_H3_Helper.generateManyNumbers();

                        // check the method in predU class with many Integers
                        for (Number i : ints) {
                            boolean expected = TutorTest_H3_Helper.expectedPredU((Integer) i);
                            boolean actual = false;
                            try {
                                var object = classH3.getDeclaredConstructor().newInstance();
                                actual = (boolean) m.invoke(object, i);
                            } catch (Exception e) {
                                fail(TutorTest_Messages.testingPredicates(className));
                            }
                            TutorTest_H3_Helper.assertObjects(expected, actual);
                        }
                    }
                }
            }
        }
        // all three param classes are found
        assertEquals(3, found, TutorTest_Messages.classNotFound(TutorTest_Constants.ONE_OF_EXT));
    }

    @Test
    public void testExtractTest() {
        var classH3 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_TEST);
        try {
            var object = classH3.getDeclaredConstructor().newInstance();
            var method = TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_TEST_EXT, classH3);
            method.invoke(object);
        } catch (Exception e) {
            fail(TutorTest_Messages.cannotCreateObject(TutorTest_Constants.CLASS_TEST));
        }
    }

    /* *********************************************************************
     *                               H3.2                                  *
     **********************************************************************/

    @Test
    public void testMixinTestSignatures() {
        var classH3 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_TEST);
        var method = TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_TEST_MIX, classH3);

        // class is public
        assertTrue(isPublic(classH3.getModifiers()),
                   TutorTest_Messages.classModifierIncorrect(TutorTest_Constants.CLASS_TEST));

        // is not generic
        assertEquals(0, method.getTypeParameters().length,
                     TutorTest_Messages.methodGeneric(TutorTest_Constants.METHOD_TEST_MIX));

        // method is public
        assertTrue(isPublic(method.getModifiers()),
                   TutorTest_Messages.methodModifierIncorrect(TutorTest_Constants.METHOD_TEST_MIX));

        // has no parameter
        assertEquals(0, method.getParameterCount(),
                     TutorTest_Messages.methodParamIncomplete(TutorTest_Constants.METHOD_TEST_MIX));

        // return type is correct
        assertEquals(void.class, method.getReturnType(),
                     TutorTest_Messages.methodReturnTypeIncorrect(TutorTest_Constants.METHOD_TEST_MIX));

        // thrown exception type is correct
        assertEquals(0, method.getExceptionTypes().length,
                     TutorTest_Messages.methodExceptionTypeIncorrect(TutorTest_Constants.METHOD_TEST_MIX));
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testParameterConstants(final TestCycle testCycle) {
        var classH3 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_TEST);

        var fields = classH3.getDeclaredFields();
        // at least three fields are found
        assertTrue(fields.length >= 3, TutorTest_Messages.fieldNotFound(TutorTest_Constants.ONE_OF_MIX));

        int found = 0;
        for (Field f : fields) {
            boolean biPred = f.getType().equals(BiPredicate.class);
            boolean predU = f.getType().equals(Predicate.class);
            boolean fct = f.getType().equals(Function.class);

            if (!(biPred || predU || fct)) {
                continue;
            }

            found++;

            // is a constant
            assertTrue(isStatic(f.getModifiers()) && isFinal(f.getModifiers()),
                       TutorTest_Messages.fieldNotConstant(f.getName()));

            if (biPred) {
                // one lambda is used
                TutorTest_H3_Helper.assertLambdas(testCycle, classH3, TutorTest_Constants.FIELD_BI_PRED,
                                                  TutorTest_Constants.BI_PRED +
                                                  "<java.lang.Number, java.lang.String>");

                // random inputs
                Number[] nums = TutorTest_H3_Helper.generateManyNumbers();
                String[] strings = Arrays.stream(TutorTest_H3_Helper.generateManyNumbers())
                    .map(Object::toString)
                    .toArray(String[]::new);

                // check the biPred constant with many inputs
                for (int i = 0; i < nums.length; i++) {
                    boolean expected = nums[i].doubleValue() > Double.parseDouble(strings[i]);
                    boolean actual = false;
                    try {
                        f.setAccessible(true);
                        @SuppressWarnings("unchecked")
                        BiPredicate<Number, String> biPredImpl = (BiPredicate<Number, String>) f.get(null);
                        actual = biPredImpl.test(nums[i], strings[i]);
                    } catch (Exception e) {
                        fail(TutorTest_Messages.testingPredicates(f.getName()));
                    }
                    TutorTest_H3_Helper.assertObjects(expected, actual);
                }
            } else if (predU) {
                // one lambda is used
                TutorTest_H3_Helper.assertLambdas(testCycle, classH3, TutorTest_Constants.FIELD_PRED,
                                                  TutorTest_Constants.PRED + "<java.lang.String>");

                // random inputs
                String[] strings = TutorTest_H3_Helper.generateManyStrings();

                // check the predU constant with many inputs
                for (String string : strings) {
                    boolean expected;
                    try {
                        Double.valueOf(string);
                        expected = true;
                    } catch (NumberFormatException e) {
                        expected = false;
                    }

                    boolean actual = false;
                    try {
                        f.setAccessible(true);
                        @SuppressWarnings("unchecked")
                        Predicate<String> predUImpl = (Predicate<String>) f.get(null);
                        actual = predUImpl.test(string);
                    } catch (IllegalAccessException e) {
                        fail(TutorTest_Messages.testingPredicates(f.getName()));
                    }
                    TutorTest_H3_Helper.assertObjects(expected, actual);
                }
            } else {
                // one lambda is used
                TutorTest_H3_Helper.assertLambdas(testCycle, classH3, TutorTest_Constants.FIELD_FCT,
                                                  TutorTest_Constants.FCT +
                                                  "<java.lang.String, java.lang.Number>");

                // random inputs
                Number[] nums = TutorTest_H3_Helper.generateManyNumbers();

                // check the fct constant with many inputs
                for (Number num : nums) {
                    Number expected = num.doubleValue();
                    Number actual = 0;
                    try {
                        f.setAccessible(true);
                        @SuppressWarnings("unchecked")
                        Function<String, Number> fctImpl = (Function<String, Number>) f.get(null);
                        actual = fctImpl.apply(String.valueOf(num));
                    } catch (IllegalAccessException e) {
                        fail(TutorTest_Messages.testingPredicates(f.getName()));
                    }
                    TutorTest_H3_Helper.assertObjects(expected, actual);
                }
            }
        }
        // all three param constants are found
        assertEquals(3, found, TutorTest_Messages.fieldNotFound(TutorTest_Constants.ONE_OF_MIX));
    }

    @Test
    public void testMixinTest() {
        var classH3 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_TEST);
        try {
            var object = classH3.getDeclaredConstructor().newInstance();
            var method = TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_TEST_MIX, classH3);
            method.invoke(object);
        } catch (Exception e) {
            fail(TutorTest_Messages.cannotCreateObject(TutorTest_Constants.CLASS_TEST));
        }
    }
}
