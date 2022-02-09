package h10;

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
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Defines the JUnit test cases related to the class defined in the task H3.
 *
 * @author Arianne Roselina Prananto
 */
@TestForSubmission("h10")
@DisplayName("Criterion: H3")
public final class TutorTest_H3 {
    TutorTest_H3_Helper helper = new TutorTest_H3_Helper();
    static final String className = "TestMyLinkedList";

    /* *********************************************************************
     *                               H3.1                                  *
     **********************************************************************/

    @Test
    public void testExtractTestSignatures() {
        Class<?> classH3 = null;
        String methodName = "testExtract";
        try {
            classH3 = Class.forName("h10." + className);
        } catch (ClassNotFoundException e) {
            fail(TutorTest_Messages.classNotFound(className));
        }

        // is public
        assertEquals(Modifier.PUBLIC, classH3.getModifiers(), TutorTest_Messages.classModifierIncorrect(className));
        boolean found = false;

        for (Method m : classH3.getDeclaredMethods()) {
            if (!m.getName().equals(methodName)) {
                continue;
            }

            found = true;

            // is not generic
            assertEquals(0, m.getTypeParameters().length, TutorTest_Messages.methodGeneric(methodName));

            // is public
            assertEquals(Modifier.PUBLIC, m.getModifiers(), TutorTest_Messages.methodModifierIncorrect(methodName));

            // has no parameter
            assertEquals(0, m.getParameterCount(), TutorTest_Messages.methodParamIncomplete(methodName));

            // return type is correct
            assertEquals(void.class, m.getReturnType(),
                         TutorTest_Messages.methodReturnTypeIncorrect(methodName));

            // thrown exception type is correct
            assertEquals(0, m.getExceptionTypes().length,
                         TutorTest_Messages.methodExceptionTypeIncorrect(methodName));
        }
        // method is found
        assertTrue(found, TutorTest_Messages.methodNotFound(methodName));
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testParameterClasses(TestCycle testCycle) {
        var allClassesNamesSet = testCycle.getSubmission().getClassNames();
        var allClassesNames = allClassesNamesSet.stream()
            .filter(x -> !(x.equals("ListItem")
                           || x.equals("MyLinkedList")
                           || x.equals("MyLinkedListException")
                           || x.equals("TestMyLinkedList")))
            .collect(Collectors.toList());

        // at least three other classes are found
        assertTrue(allClassesNames.size() >= 3,
                   TutorTest_Messages.classNotFound("fct/predT/predU"));

        int found = 0;
        for (String className : allClassesNames) {
            Class<?> classH3 = null;
            try {
                classH3 = Class.forName(className);
            } catch (ClassNotFoundException e) {
                fail(TutorTest_Messages.classNotFound(className));
            }

            var interfacesArray = classH3.getGenericInterfaces();
            if (interfacesArray.length < 1) {
                continue;
            }

            // check if this class implements one of the interfaces
            var interfaces = Arrays.stream(interfacesArray).map(Type::getTypeName).collect(Collectors.toList());
            boolean fct = interfaces.contains("java.util.function.Function<java.lang.Integer[], java.lang.Integer>")
                          || interfaces.contains("java.util.function.Function<java.lang.Integer[],java.lang.Integer>");
            boolean predT = interfaces.contains("java.util.function.Predicate<java.lang.Integer[]>");
            boolean predU = interfaces.contains("java.util.function.Predicate<java.lang.Integer>");

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
                        helper.assertNoLambda(testCycle, classH3, m.getName());

                        // random inputs
                        Integer[][] intArrays = helper.generateManyIntegerArrays();

                        // check the method in fct class with many Integers Arrays
                        for (Integer[] ints : intArrays) {
                            Integer expected = helper.expectedFct(ints);
                            Integer actual = null;
                            try {
                                var object = classH3.getDeclaredConstructor().newInstance();
                                actual = (Integer) m.invoke(object, new Object[]{ints});
                            } catch (Exception e) {
                                fail(TutorTest_Messages.testingPredicates(className));
                            }
                            helper.assertObjects(expected, actual);
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
                        helper.assertNoLambda(testCycle, classH3, m.getName());

                        // random inputs
                        Integer[][] intArrays = helper.generateManyIntegerArrays();

                        // check the method in predT class with many Integer Arrays
                        for (Integer[] ints : intArrays) {
                            boolean expected = helper.expectedPredT(ints);
                            boolean actual = false;
                            try {
                                var object = classH3.getDeclaredConstructor().newInstance();
                                actual = (boolean) m.invoke(object, new Object[]{ints});
                            } catch (Exception e) {
                                fail(TutorTest_Messages.testingPredicates(className));
                            }
                            helper.assertObjects(expected, actual);
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
                        helper.assertNoLambda(testCycle, classH3, m.getName());

                        // random inputs
                        Number[] ints = helper.generateManyNumbers();

                        // check the method in predU class with many Integers
                        for (Number i : ints) {
                            boolean expected = helper.expectedPredU((Integer) i);
                            boolean actual = false;
                            try {
                                var object = classH3.getDeclaredConstructor().newInstance();
                                actual = (boolean) m.invoke(object, i);
                            } catch (Exception e) {
                                fail(TutorTest_Messages.testingPredicates(className));
                            }
                            helper.assertObjects(expected, actual);
                        }
                    }
                }
            }
        }
        // all three param classes are found
        assertEquals(3, found, TutorTest_Messages.classNotFound("fct/predT/predU"));
    }

    @Test
    public void testExtractTest() {
        // TODO : how to test what inputs are used and what is tested?
        TestMyLinkedList test = new TestMyLinkedList();
        test.testExtract();
    }

    /* *********************************************************************
     *                               H3.2                                  *
     **********************************************************************/

    @Test
    public void testMixinTestSignatures() {
        Class<?> classH3 = null;
        String methodName = "testMixin";
        try {
            classH3 = Class.forName("h10." + className);
        } catch (ClassNotFoundException e) {
            fail(TutorTest_Messages.classNotFound(className));
        }

        // is public
        assertEquals(Modifier.PUBLIC, classH3.getModifiers(), TutorTest_Messages.classModifierIncorrect(className));
        boolean found = false;

        for (Method m : classH3.getDeclaredMethods()) {
            if (!m.getName().equals(methodName)) {
                continue;
            }

            found = true;

            // is not generic
            assertEquals(0, m.getTypeParameters().length, TutorTest_Messages.methodGeneric(methodName));

            // is public
            assertEquals(Modifier.PUBLIC, m.getModifiers(), TutorTest_Messages.methodModifierIncorrect(methodName));

            // has no parameter
            assertEquals(0, m.getParameterCount(), TutorTest_Messages.methodParamIncomplete(methodName));

            // return type is correct
            assertEquals(void.class, m.getReturnType(),
                         TutorTest_Messages.methodReturnTypeIncorrect(methodName));

            // thrown exception type is correct
            assertEquals(0, m.getExceptionTypes().length,
                         TutorTest_Messages.methodExceptionTypeIncorrect(methodName));
        }
        // method is found
        assertTrue(found, TutorTest_Messages.methodNotFound(methodName));
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testParameterConstants(final TestCycle testCycle) {
        Class<?> classH3 = null;
        try {
            classH3 = Class.forName("h10." + className);
        } catch (ClassNotFoundException e) {
            fail(TutorTest_Messages.classNotFound(className));
        }

        var fields = classH3.getDeclaredFields();
        // at least three fields are found
        assertTrue(fields.length >= 3, TutorTest_Messages.fieldNotFound("biPred/predU/fct"));

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
            assertTrue(Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers()));

            if (biPred) {
                // one lambda is used
                helper.assertLambdas(testCycle, classH3, "BI_PRED_MIXIN",
                                     "java.util.function.BiPredicate<java.lang.Number, java.lang.String>");

                // random inputs
                Number[] nums = helper.generateManyNumbers();
                String[] strings = Arrays.stream(helper.generateManyNumbers())
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
                    helper.assertObjects(expected, actual);
                }
            } else if (predU) {
                // one lambda is used
                helper.assertLambdas(testCycle, classH3, "PRED_U_MIXIN",
                                     "java.util.function.Predicate<java.lang.String>");

                // random inputs
                String[] strings = helper.generateManyStrings();

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
                    helper.assertObjects(expected, actual);
                }
            } else {
                // one lambda is used
                helper.assertLambdas(testCycle, classH3, "FCT_MIXIN",
                                     "java.util.function.Function<java.lang.String, java.lang.Number>");

                // random inputs
                Number[] nums = helper.generateManyNumbers();

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
                    helper.assertObjects(expected, actual);
                }
            }
        }
        // all three param constants are found
        assertEquals(3, found, TutorTest_Messages.fieldNotFound("biPred/predU/fct"));
    }

    @Test
    public void testMixinTest() {
        // TODO : how to test what inputs are used and what is tested?
        TestMyLinkedList test = new TestMyLinkedList();
        test.testMixin();
    }
}
