package h10;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Defines the JUnit test cases related to the class defined in the task H2.2.
 *
 * @author Arianne Roselina Prananto
 */
@TestForSubmission("h10")
@DisplayName("Criterion: Class Traits")
public final class TutorTest_H2_2 {

    /* *********************************************************************
     *                               H2.2                                  *
     **********************************************************************/

    @Test
    public void testMixinMethodsExist() {
        Class<?> classH2 = null;
        try {
            classH2 = Class.forName("h10.MyLinkedList");
        } catch (ClassNotFoundException e) {
            fail("Class MyLinkedList does not exist");
        }

        int found = 0;
        for (Method m : classH2.getDeclaredMethods()) {
            if (!m.getName().equals("mixinIteratively") && !m.getName().equals("mixinRecursively") &&
                !m.getName().equals("mixinRecursivelyHelper"))
                continue;
            found++;
        }
        // methods are found
        assertEquals(found, 3, "At least one mixin*-method does not exist");
    }

    @Test
    public void testMixinMethodsSignatures() {
        Class<?> classH2 = null;
        try {
            classH2 = Class.forName("h10.MyLinkedList");
        } catch (ClassNotFoundException e) {
            fail("Class MyLinkedList does not exist");
        }

        for (Method m : classH2.getDeclaredMethods()) {
            if (!m.getName().equals("mixinIteratively") && !m.getName().equals("mixinRecursively"))
                continue;

            // TODO : check method's generic type

            // is public
            assertTrue(isPublic(m.getModifiers()));

            // all params are found
            var params = m.getParameters();
            assertEquals(params.length, 4, "Parameters in mixin*-methods are not complete");

            // param types are correct
            var paramTypes = Arrays.stream(params).map(x -> x.getType().toGenericString()).collect(Collectors.toList());
            assertTrue(paramTypes.contains("MyLinkedList<U>") &&
                       paramTypes.contains("BiPredicate<? super T,? super U>") &&
                       paramTypes.contains("Function<? super U,? extends T>") &&
                       paramTypes.contains("Predicate<? super U>"),
                       "Parameters in mixin*-methods are incorrect");

            // param names are correct
            var paramNames = Arrays.stream(params).map(Parameter::getName).collect(Collectors.toList());
            assertTrue(paramNames.contains("otherList") &&
                       paramNames.contains("biPred") &&
                       paramNames.contains("fct") &&
                       paramNames.contains("predU"), "Parameters in mixin*-methods are incorrect");

            // return type is correct
            assertEquals(m.getReturnType(), void.class,
                         "Return type in mixin*-methods is incorrect");

            // thrown exception type is correct
            assertEquals(m.getExceptionTypes()[0], MyLinkedListException.class,
                         "Thrown exception in mixin*-methods is incorrect");
        }
    }

    @Test
    public void testMixinHelperMethod() {
        Class<?> classH2 = null;
        try {
            classH2 = Class.forName("h10.MyLinkedList");
        } catch (ClassNotFoundException e) {
            fail("Class MyLinkedList does not exist");
        }

        for (Method m : classH2.getDeclaredMethods()) {
            if (!m.getName().equals("mixinRecursivelyHelper")) continue;

            // TODO : check method's generic type

            // is public
            assertTrue(isPrivate(m.getModifiers()));

            // all params are found
            var params = m.getParameters();
            assertEquals(params.length, 6, "Parameters in mixinRecursivelyHelper method are not complete");

            // param types are correct
            var paramTypes = Arrays.stream(params).map(x -> x.getType().toGenericString()).collect(Collectors.toList());
            assertTrue(paramTypes.contains("MyLinkedList<U>") &&
                       paramTypes.contains("BiPredicate<? super T,? super U>") &&
                       paramTypes.contains("Function<? super U,? extends T>") &&
                       paramTypes.contains("Predicate<? super U>") &&
                       paramTypes.contains("ListItem<T>") &&
                       paramTypes.contains("int"),
                       "Parameters in mixinRecursivelyHelper method are incorrect");

            // param names are correct
            var paramNames = Arrays.stream(params).map(Parameter::getName).collect(Collectors.toList());
            assertTrue(paramNames.contains("otherList") &&
                       paramNames.contains("biPred") &&
                       paramNames.contains("fct") &&
                       paramNames.contains("predU") &&
                       paramNames.contains("pSrc") &&
                       paramNames.contains("index"), "Parameters in mixinRecursivelyHelper method are incorrect");

            // return type is correct
            assertEquals(m.getReturnType().toGenericString(), "MyLinkedList<U>",
                         "Return type in mixinRecursivelyHelper method is incorrect");

            // thrown exception type is correct
            assertEquals(m.getExceptionTypes()[0], MyLinkedListException.class,
                         "Thrown exception in mixinRecursivelyHelper method is incorrect");
        }
    }

    @Test
    public void testMixinIteratively() {
        // TODO : test implementation
    }

    @Test
    public void testMixinIterativelyException() {
        // TODO : test implementation with exception
    }

    @Test
    public void testMixinRecursively() {
        // TODO : test implementation
    }

    @Test
    public void testMixinRecursivelyException() {
        // TODO : test implementation with exception
    }

    @Test
    public void testMixinNoOtherMethods() {
        // TODO : test no other methods
    }

    @Test
    public void testMixinReallyIteratively() {
        // TODO : test really iteratively
    }

    @Test
    public void testMixinReallyRecursively() {
        // TODO : test really recursively
    }

}
