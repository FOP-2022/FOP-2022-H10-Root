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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Defines the JUnit test cases related to the class defined in the task H2.2.
 *
 * @author Arianne Roselina Prananto
 */
@TestForSubmission("h10")
@DisplayName("Criterion: H2.2")
public final class TutorTest_H2_2 {
    TutorTest_H2_Helper<Integer> helper1 = new TutorTest_H2_Helper<>();
    TutorTest_H2_Helper<String> helper2 = new TutorTest_H2_Helper<>();
    final static String className = "MyLinkedList";

    /* *********************************************************************
     *                               H2.2                                  *
     **********************************************************************/

    @Test
    public void testMixinMethodsExist() {
        Class<?> classH2 = null;
        String methodName = "mixin*";
        try {
            classH2 = Class.forName("h10." + className);
        } catch (ClassNotFoundException e) {
            fail(TutorTest_Messages.classNotFound(className));
        }

        int found = 0;
        for (Method m : classH2.getDeclaredMethods()) {
            if (!m.getName().equals("mixinIteratively")
                && !m.getName().equals("mixinRecursively")
                && !m.getName().equals("mixinRecursivelyHelper")) {
                continue;
            }
            found++;
        }
        // methods are found
        assertEquals(3, found, TutorTest_Messages.methodNotFound(methodName));
    }

    @Test
    public void testMixinMethodsSignatures() {
        Class<?> classH2 = null;
        String methodName = "mixin*";
        try {
            classH2 = Class.forName("h10." + className);
        } catch (ClassNotFoundException e) {
            fail(TutorTest_Messages.classNotFound(className));
        }

        for (Method m : classH2.getDeclaredMethods()) {
            if (!m.getName().equals("mixinIteratively")
                && !m.getName().equals("mixinRecursively")) {
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
            assertEquals(4, params.length, TutorTest_Messages.methodParamIncomplete(methodName));

            // param types are correct
            var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName())
                .collect(Collectors.toList());
            assertTrue(paramTypes.contains("h10.MyLinkedList<U>")
                       && (paramTypes.contains("java.util.function.BiPredicate<? super T, ? super U>")
                           || paramTypes.contains("java.util.function.BiPredicate<? super T,? super U>"))
                       && (paramTypes.contains("java.util.function.Function<? super U, ? extends T>")
                           || paramTypes.contains("java.util.function.Function<? super U,? extends T>"))
                       && paramTypes.contains("java.util.function.Predicate<? super U>"),
                       TutorTest_Messages.methodParamIncorrect(methodName));

            // return type is correct
            assertEquals(void.class, m.getReturnType(),
                         TutorTest_Messages.methodReturnTypeIncorrect(methodName));

            // thrown exception type is correct
            assertEquals(MyLinkedListException.class, m.getExceptionTypes()[0],
                         TutorTest_Messages.methodExceptionTypeIncorrect(methodName));
        }
    }

    @Test
    public void testMixinHelperMethod() {
        Class<?> classH2 = null;
        String methodName = "mixinRecursivelyHelper";
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
            assertEquals(7, params.length, TutorTest_Messages.methodParamIncomplete(methodName));

            // param types are correct
            var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName())
                .collect(Collectors.toList());
            assertTrue(paramTypes.contains("h10.MyLinkedList<U>")
                       && (paramTypes.contains("java.util.function.BiPredicate<? super T, ? super U>")
                           || paramTypes.contains("java.util.function.BiPredicate<? super T,? super U>"))
                       && (paramTypes.contains("java.util.function.Function<? super U, ? extends T>")
                           || paramTypes.contains("java.util.function.Function<? super U,? extends T>"))
                       && paramTypes.contains("java.util.function.Predicate<? super U>")
                       && paramTypes.contains("h10.ListItem<T>")
                       && paramTypes.contains("int"),
                       TutorTest_Messages.methodParamIncorrect(methodName));

            // return type is correct
            assertEquals(void.class, m.getReturnType(),
                         TutorTest_Messages.methodReturnTypeIncorrect(methodName));

            // thrown exception type is correct
            assertEquals(MyLinkedListException.class, m.getExceptionTypes()[0],
                         TutorTest_Messages.methodExceptionTypeIncorrect(methodName));
        }
    }

    @Test
    public void testMixinIteratively() {
        var thisLists1 = TutorTest_Generators.generateThisListMixin1();
        var otherLists1 = TutorTest_Generators.generateOtherListMixin1WithoutExc();
        var thisLists2 = TutorTest_Generators.generateThisListMixin2();
        var otherLists2 = TutorTest_Generators.generateOtherListMixin2WithoutExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralMixin(thisLists1, otherLists1, TutorTest_H2_Helper.MethodType.ITERATIVE,
                                 TutorTest_Generators.biPred1, TutorTest_Generators.fctMixin1,
                                 TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralMixin(thisLists2, otherLists2, TutorTest_H2_Helper.MethodType.ITERATIVE,
                                 TutorTest_Generators.biPred2, TutorTest_Generators.fctMixin2,
                                 TutorTest_Generators.predU2);
    }

    @Test
    public void testMixinIterativelyException() {
        var thisLists1 = TutorTest_Generators.generateThisListMixin1();
        var otherLists1 = TutorTest_Generators.generateOtherListMixin1WithExc();
        var thisLists2 = TutorTest_Generators.generateThisListMixin2();
        var otherLists2 = TutorTest_Generators.generateOtherListMixin2WithExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralMixin(thisLists1, otherLists1, TutorTest_H2_Helper.MethodType.ITERATIVE,
                                 TutorTest_Generators.biPred1, TutorTest_Generators.fctMixin1,
                                 TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralMixin(thisLists2, otherLists2, TutorTest_H2_Helper.MethodType.ITERATIVE,
                                 TutorTest_Generators.biPred2, TutorTest_Generators.fctMixin2,
                                 TutorTest_Generators.predU2);
    }

    @Test
    public void testMixinRecursively() {
        var thisLists1 = TutorTest_Generators.generateThisListMixin1();
        var otherLists1 = TutorTest_Generators.generateOtherListMixin1WithoutExc();
        var thisLists2 = TutorTest_Generators.generateThisListMixin2();
        var otherLists2 = TutorTest_Generators.generateOtherListMixin2WithoutExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralMixin(thisLists1, otherLists1, TutorTest_H2_Helper.MethodType.RECURSIVE,
                                 TutorTest_Generators.biPred1, TutorTest_Generators.fctMixin1,
                                 TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralMixin(thisLists2, otherLists2, TutorTest_H2_Helper.MethodType.RECURSIVE,
                                 TutorTest_Generators.biPred2, TutorTest_Generators.fctMixin2,
                                 TutorTest_Generators.predU2);
    }

    @Test
    public void testMixinRecursivelyException() {
        var thisLists1 = TutorTest_Generators.generateThisListMixin1();
        var otherLists1 = TutorTest_Generators.generateOtherListMixin1WithExc();
        var thisLists2 = TutorTest_Generators.generateThisListMixin2();
        var otherLists2 = TutorTest_Generators.generateOtherListMixin2WithExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralMixin(thisLists1, otherLists1, TutorTest_H2_Helper.MethodType.RECURSIVE,
                                 TutorTest_Generators.biPred1, TutorTest_Generators.fctMixin1,
                                 TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralMixin(thisLists2, otherLists2, TutorTest_H2_Helper.MethodType.RECURSIVE,
                                 TutorTest_Generators.biPred2, TutorTest_Generators.fctMixin2,
                                 TutorTest_Generators.predU2);
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testMixinNoOtherMethods(final TestCycle testCycle) {
        helper1.assertNoOtherMethod(testCycle, MyLinkedList.class, "mixinIteratively");
        helper1.assertNoOtherMethod(testCycle, MyLinkedList.class, "mixinRecursively");
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testMixinReallyIteratively(final TestCycle testCycle) {
        helper1.assertNumberOfLoop(testCycle, MyLinkedList.class, "mixinIteratively", 1);
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testMixinReallyRecursively(final TestCycle testCycle) {
        String methodName = "mixinRecursively";
        // not iterative
        helper1.assertNumberOfLoop(testCycle, MyLinkedList.class, methodName, 0);

        var thisList = TutorTest_Generators.generateThisListMixinMockito();
        var otherList = TutorTest_Generators.generateOtherListMixinMockito();

        try {
            thisList.mixinRecursively(otherList, TutorTest_Generators.biPred1, TutorTest_Generators.fctMixin1,
                                      TutorTest_Generators.predU1);
        } catch (MyLinkedListException e) {
            // do not take other points
            return;
        }

        // TODO
        /*try {
            Mockito.verify(thisList, Mockito.atLeast(2))
                .mixinRecursivelyHelper(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                                        Mockito.any(), Mockito.any());
        } catch (Exception e) {
            // MyLinkedListException will never be thrown
            fail(TutorTest_Messages.methodNoRecursion(methodName));
        }*/
    }
}
