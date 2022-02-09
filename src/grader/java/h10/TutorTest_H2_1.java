package h10;

import h10.utils.TutorTest_Generators;
import h10.utils.TutorTest_Messages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.sourcegrade.jagr.api.testing.TestCycle;
import org.sourcegrade.jagr.api.testing.extension.JagrExecutionCondition;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Defines the JUnit test cases related to the class defined in the task H2.1.
 *
 * @author Arianne Roselina Prananto
 */
@TestForSubmission("h10")
@DisplayName("Criterion: H2.1")
public final class TutorTest_H2_1 {
    TutorTest_H2_Helper<Integer> helper1 = new TutorTest_H2_Helper<>();
    TutorTest_H2_Helper<String> helper2 = new TutorTest_H2_Helper<>();
    final static String className = "MyLinkedList";

    /* *********************************************************************
     *                               H2.1                                  *
     **********************************************************************/

    @Test
    public void testExtractMethodsExist() {
        Class<?> classH2 = null;
        String methodName = "extract*";
        try {
            classH2 = Class.forName("h10." + className);
        } catch (ClassNotFoundException e) {
            fail(TutorTest_Messages.classNotFound(className));
        }

        int found = 0;
        for (Method m : classH2.getDeclaredMethods()) {
            if (!m.getName().equals("extractIteratively")
                && !m.getName().equals("extractRecursively")
                && !m.getName().equals("extractRecursivelyHelper")) {
                continue;
            }
            found++;
        }
        // methods are found
        assertEquals(3, found, TutorTest_Messages.methodNotFound(methodName));
    }

    @Test
    public void testExtractMethodsSignatures() {
        Class<?> classH2 = null;
        String methodName = "extract*";
        try {
            classH2 = Class.forName("h10." + className);
        } catch (ClassNotFoundException e) {
            fail(TutorTest_Messages.classNotFound(className));
        }

        for (Method m : classH2.getDeclaredMethods()) {
            if (!m.getName().equals("extractIteratively")
                && !m.getName().equals("extractRecursively")) {
                continue;
            }

            // is generic with type U
            assertEquals(1, m.getTypeParameters().length, TutorTest_Messages.methodNotGeneric(methodName));
            assertEquals("U", m.getTypeParameters()[0].getTypeName(),
                         TutorTest_Messages.methodGenericTypeIncorrect(methodName));

            // is public
            assertEquals(Modifier.PUBLIC, m.getModifiers(), TutorTest_Messages.methodModifierIncorrect(methodName));

            // all params are found
            var params = m.getParameters();
            assertEquals(3, params.length, TutorTest_Messages.methodParamIncomplete(methodName));

            // param types are correct
            var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName()).collect(Collectors.toList());
            assertTrue(paramTypes.contains("java.util.function.Predicate<? super T>")
                       && (paramTypes.contains("java.util.function.Function<? super T, ? extends U>")
                           || paramTypes.contains("java.util.function.Function<? super T,? extends U>"))
                       && paramTypes.contains("java.util.function.Predicate<? super U>"),
                       TutorTest_Messages.methodParamIncorrect(methodName));

            // return type is correct
            assertEquals("h10.MyLinkedList<U>", m.getGenericReturnType().getTypeName(),
                         TutorTest_Messages.methodReturnTypeIncorrect(methodName));

            // thrown exception type is correct
            assertEquals(MyLinkedListException.class, m.getExceptionTypes()[0],
                         TutorTest_Messages.methodExceptionTypeIncorrect(methodName));
        }
    }

    @Test
    public void testExtractHelperMethod() {
        Class<?> classH2 = null;
        String methodName = "extractRecursivelyHelper";
        try {
            classH2 = Class.forName("h10." + className);
        } catch (ClassNotFoundException e) {
            fail(TutorTest_Messages.classNotFound(className));
        }

        for (Method m : classH2.getDeclaredMethods()) {
            if (!m.getName().equals(methodName)) {
                continue;
            }

            // is generic with type U
            assertEquals(1, m.getTypeParameters().length, TutorTest_Messages.methodNotGeneric(methodName));
            assertEquals("U", m.getTypeParameters()[0].getTypeName(),
                         TutorTest_Messages.methodGenericTypeIncorrect(methodName));

            // is private
            assertEquals(Modifier.PRIVATE, m.getModifiers(), TutorTest_Messages.methodModifierIncorrect(methodName));

            // all params are found
            var params = m.getParameters();
            assertEquals(5, params.length, TutorTest_Messages.methodParamIncomplete(methodName));

            // param types are correct
            var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName())
                .collect(Collectors.toList());
            assertTrue(paramTypes.contains("java.util.function.Predicate<? super T>")
                       && (paramTypes.contains("java.util.function.Function<? super T, ? extends U>")
                           || paramTypes.contains("java.util.function.Function<? super T,? extends U>"))
                       && paramTypes.contains("java.util.function.Predicate<? super U>")
                       && paramTypes.contains("h10.ListItem<T>")
                       && paramTypes.contains("int"),
                       TutorTest_Messages.methodParamIncorrect(methodName));

            // return type is correct
            assertEquals("h10.MyLinkedList<U>", m.getGenericReturnType().getTypeName(),
                         TutorTest_Messages.methodReturnTypeIncorrect(methodName));

            // thrown exception type is correct
            assertEquals(MyLinkedListException.class, m.getExceptionTypes()[0],
                         TutorTest_Messages.methodExceptionTypeIncorrect(methodName));
        }
    }

    @Test
    public void testExtractIteratively() {
        var thisLists1 = TutorTest_Generators.generateThisListExtract1WithoutExc();
        var thisLists2 = TutorTest_Generators.generateThisListExtract2WithoutExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralExtract(thisLists1, TutorTest_H2_Helper.MethodType.ITERATIVE, TutorTest_Generators.predT1,
                                   TutorTest_Generators.fctExtract1, TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralExtract(thisLists2, TutorTest_H2_Helper.MethodType.ITERATIVE, TutorTest_Generators.predT2,
                                   TutorTest_Generators.fctExtract2, TutorTest_Generators.predU2);
    }

    @Test
    public void testExtractIterativelyException() {
        var thisLists1 = TutorTest_Generators.generateThisListExtract1WithExc();
        var thisLists2 = TutorTest_Generators.generateThisListExtract2WithExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralExtract(thisLists1, TutorTest_H2_Helper.MethodType.ITERATIVE, TutorTest_Generators.predT1,
                                   TutorTest_Generators.fctExtract1, TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralExtract(thisLists2, TutorTest_H2_Helper.MethodType.ITERATIVE, TutorTest_Generators.predT2,
                                   TutorTest_Generators.fctExtract2, TutorTest_Generators.predU2);
    }

    @Test
    public void testExtractRecursively() {
        var thisLists1 = TutorTest_Generators.generateThisListExtract1WithoutExc();
        var thisLists2 = TutorTest_Generators.generateThisListExtract2WithoutExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralExtract(thisLists1, TutorTest_H2_Helper.MethodType.RECURSIVE, TutorTest_Generators.predT1,
                                   TutorTest_Generators.fctExtract1, TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralExtract(thisLists2, TutorTest_H2_Helper.MethodType.RECURSIVE, TutorTest_Generators.predT2,
                                   TutorTest_Generators.fctExtract2, TutorTest_Generators.predU2);
    }

    @Test
    public void testExtractRecursivelyException() {
        var thisLists1 = TutorTest_Generators.generateThisListExtract1WithExc();
        var thisLists2 = TutorTest_Generators.generateThisListExtract2WithExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralExtract(thisLists1, TutorTest_H2_Helper.MethodType.RECURSIVE, TutorTest_Generators.predT1,
                                   TutorTest_Generators.fctExtract1, TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralExtract(thisLists2, TutorTest_H2_Helper.MethodType.RECURSIVE, TutorTest_Generators.predT2,
                                   TutorTest_Generators.fctExtract2, TutorTest_Generators.predU2);
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testExtractNoOtherMethods(final TestCycle testCycle) {
        helper1.assertNoOtherMethod(testCycle, MyLinkedList.class, "extractIteratively");
        helper1.assertNoOtherMethod(testCycle, MyLinkedList.class, "extractRecursively");
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testExtractReallyIteratively(final TestCycle testCycle) {
        helper1.assertNumberOfLoop(testCycle, MyLinkedList.class, "extractIteratively", 1);
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testExtractReallyRecursively(final TestCycle testCycle) {
        String methodName = "extractRecursively";
        // not iterative
        helper1.assertNumberOfLoop(testCycle, MyLinkedList.class, methodName, 0);

        var thisList = TutorTest_Generators.generateThisListExtractMockito();
        ListItem<Integer> dummy = new ListItem<>();
        dummy.next = thisList.head;

        // TODO
        /*try {
            var m = MyLinkedList.class.getDeclaredMethod("extractRecursivelyHelper", Predicate.class, Function.class,
                                                         Predicate.class, ListItem.class, int.class);
            m.setAccessible(true);
            var recursion = m.invoke(thisList, Mockito.any(Predicate.class), Mockito.any(Function.class),
                                     Mockito.any(Predicate.class), dummy, Mockito.any(int.class));
            Mockito.verify(recursion, Mockito.atLeast(1));
        } catch (Exception e) {
            // MyLinkedListException will never be thrown
            fail(TutorTest_Messages.methodNoRecursion(methodName));
        }*/
    }
}
