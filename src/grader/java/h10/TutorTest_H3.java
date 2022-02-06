package h10;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.*;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Defines the JUnit test cases related to the class defined in the task H3.
 *
 * @author Arianne Roselina Prananto
 */
@TestForSubmission("h10")
@DisplayName("Criterion: H3")
public final class TutorTest_H3 {
    TutorTest_H3_Helper helper = new TutorTest_H3_Helper();

    /* *********************************************************************
     *                               H3.1                                  *
     **********************************************************************/

    @Test
    public void testExtractTestSignatures() {
        Class<?> classH3 = null;
        try {
            classH3 = Class.forName("h10.TestMyLinkedList");
        } catch (ClassNotFoundException e) {
            fail("Class TestMyLinkedList does not exist");
        }

        // is public
        assertTrue(isPublic(classH3.getModifiers()), "Class TestMyLinkedList is not public");
        boolean found = false;

        for (Method m : classH3.getDeclaredMethods()) {
            if (!m.getName().equals("testExtract")) continue;

            found = true;

            // TODO : check method's generic type

            // is public
            assertTrue(isPublic(m.getModifiers()), "testExtract method is not public");

            // has no parameter
            assertEquals(m.getParameterCount(), 0, "textExtract method has parameter(s)");

            // return type is correct
            assertEquals(m.getReturnType(), void.class,
                         "Return type in textExtract method is incorrect");

            // thrown exception type is correct
            assertEquals(m.getExceptionTypes().length, 0,
                         "textExtract method throws exception(s)");
        }
        // method is found
        assertTrue(found, "testExtract method does not exist");
    }

    @Test
    public void testParameterClasses() {
        Reflections reflections = new Reflections("h10");
        Set<Class<?>> allClasses =
            reflections.getSubTypesOf(Object.class);

        allClasses = allClasses.stream()
            .filter(x -> !x.getSimpleName().equals("ListItem")
                         && !x.getSimpleName().equals("MyLinkedList")
                         && !x.getSimpleName().equals("MyLinkedListException")
                         && !x.getSimpleName().equals("TestMyLinkedList")).collect(Collectors.toSet());
        // at least three other classes are found
        assertTrue(allClasses.size() >= 3, "At least one class for the three parameters do not exist");

        int found = 0;
        for (Class c : allClasses) {
            var interfaces = c.getInterfaces();
            boolean fct = interfaces[0].getSimpleName().equals("Function"),
                predT = interfaces[0].getTypeName().equals("Predicate<Integer[]>"),
                predU = interfaces[0].getTypeName().equals("Predicate<Integer>");

            if (!(fct || predT || predU)) continue;
            found++;

            // TODO : how to check if lambdas are used?
            if (fct) {
                for (Method m : c.getDeclaredMethods()) {
                    if (m.getReturnType().equals(Integer.class)
                        && m.getParameterCount() == 1
                        && m.getParameters()[0].getType().equals(Integer[].class)) {
                        Integer[][] intArrays = helper.generateManyIntegerArrays(10, 50);

                        // check the method in fct class with many Integers Arrays
                        for (var a : intArrays) {
                            Integer expected = helper.expectedFct(a);
                            Integer actual = 0; // TODO : how to use this class' method to get actual result?
                            assertEquals(expected, actual, "fct Class is incorrect");
                        }
                    }
                }
            } else if (predT) {
                for (Method m : c.getDeclaredMethods()) {
                    if (m.getReturnType().equals(boolean.class)
                        && m.getParameterCount() == 1
                        && m.getParameters()[0].getType().equals(Integer[].class)) {
                        Integer[][] intArrays = helper.generateManyIntegerArrays(10, 50);

                        // check the method in predT class with many Integer Arrays
                        for (var a : intArrays) {
                            boolean expected = helper.expectedPredT(a);
                            boolean actual = false; // TODO : how to use this class' method to get actual result?
                            assertEquals(expected, actual, "predT Class is incorrect");
                        }
                    }
                }
            } else {
                for (Method m : c.getDeclaredMethods()) {
                    if (m.getReturnType().equals(boolean.class)
                        && m.getParameterCount() == 1
                        && m.getParameters()[0].getType().equals(Integer.class)) {
                        Integer[] ints = (Integer[]) helper.generateManyNumbers(50);

                        // check the method in predU class with many Integers
                        for (var a : ints) {
                            boolean expected = helper.expectedPredU(a);
                            boolean actual = false; // TODO : how to use this class' method to get actual result?
                            assertEquals(expected, actual, "predU Class is incorrect");
                        }
                    }
                }
            }
        }
        // all three param classes are found
        assertEquals(found, 3, "At least one class for the three parameters does not exist");
    }

    @Test
    public void testExtractTest() {
        // TODO : test implementation
    }

    /* *********************************************************************
     *                               H3.2                                  *
     **********************************************************************/

    @Test
    public void testMixinTestSignatures() {
        Class<?> classH3 = null;
        try {
            classH3 = Class.forName("h10.TestMyLinkedList");
        } catch (ClassNotFoundException e) {
            fail("Class TestMyLinkedList does not exist");
        }

        // is public
        assertTrue(isPublic(classH3.getModifiers()), "Class TestMyLinkedList is not public");
        boolean found = false;

        for (Method m : classH3.getDeclaredMethods()) {
            if (!m.getName().equals("testMixin")) continue;

            found = true;

            // TODO : check method's generic type

            // is public
            assertTrue(isPublic(m.getModifiers()), "testMixin method is not public");

            // has no parameter
            assertEquals(m.getParameterCount(), 0, "textMixin method has parameter(s)");

            // return type is correct
            assertEquals(m.getReturnType(), void.class,
                         "Return type in textMixin method is incorrect");

            // thrown exception type is correct
            assertEquals(m.getExceptionTypes().length, 0,
                         "textMixin method throws exception(s)");
        }
        // method is found
        assertTrue(found, "testMixin method does not exist");
    }

    @Test
    public void testParametersConstants() {
        Class<?> classH3 = null;
        try {
            classH3 = Class.forName("h10.TestMyLinkedList");
        } catch (ClassNotFoundException e) {
            fail("Class TestMyLinkedList does not exist");
        }

        var fields = classH3.getDeclaredFields();
        // at least three fields are found
        assertTrue(fields.length >= 3, "At least one constant for the three parameters does not exist");

        int found = 0;
        for (Field f : fields) {
            boolean biPred = f.getType().equals(BiPredicate.class),
                predU = f.getType().equals(Predicate.class),
                fct = f.getType().equals(Function.class);

            if (!(biPred || predU || fct)) continue;
            found++;

            // TODO : how to check if lambdas are used?
            if (biPred) {
                Number[] nums = helper.generateManyNumbers(50);
                String[] strings = Arrays.stream(helper.generateManyNumbers(50))
                    .map(Object::toString)
                    .toArray(String[]::new);

                // check the biPred constant with many inputs
                for (int i = 0; i < 50; i++) {
                    boolean expected = nums[i].doubleValue() > Double.parseDouble(strings[i]);
                    boolean actual = false; // TODO : how to use this constant to get actual result?
                    assertEquals(expected, actual, "biPred constant is incorrect");
                }
            } else if (predU) {
                String[] strings = helper.generateManyStrings(50);

                // check the predU constant with many inputs
                for (int i = 0; i < 50; i++) {
                    boolean expected;
                    try {
                        Double.valueOf(strings[i]);
                        expected = true;
                    } catch (NumberFormatException e) {
                        expected = false;
                    }

                    boolean actual = false; // TODO : how to use this constant to get actual result?
                    assertEquals(expected, actual, "predU constant is incorrect");
                }
            } else {
                Number[] nums = helper.generateManyNumbers(50);

                // check the fct constant with many inputs
                for (int i = 0; i < 50; i++) {
                    double expected = nums[i].doubleValue();
                    double actual = 0; // TODO : how to use this constant to get actual result?
                    assertEquals(expected, actual, "fct constant is incorrect");
                }
            }
        }
        // all three param constants are found
        assertEquals(found, 3, "At least one constant for the three parameters does not exist");
    }

    @Test
    public void testMixinTest() {
        // TODO : test implementation
    }
}
