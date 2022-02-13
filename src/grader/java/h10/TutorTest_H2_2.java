package h10;

import h10.utils.TutorTest_Generators;
import h10.utils.TutorTest_Helper;
import h10.utils.TutorTest_Messages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.sourcegrade.jagr.api.testing.TestCycle;
import org.sourcegrade.jagr.api.testing.extension.JagrExecutionCondition;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;

import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Defines the JUnit test cases related to the class defined in the task H2.2.
 *
 * @author Arianne Roselina Prananto
 */
@TestForSubmission("h10")
@DisplayName("Criterion: H2.2")
public final class TutorTest_H2_2 {
    static final String className = "MyLinkedList";
    static final String methodNameIt = "mixinIteratively";
    static final String methodNameRec = "mixinRecursively";
    static final String methodNameRecHelp = "mixinRecursivelyHelper";
    static final String classNameExc = "MyLinkedListException";

    /* *********************************************************************
     *                               H2.2                                  *
     **********************************************************************/

    @Test
    public void testMixinMethodsExist() {
        var classH2 = TutorTest_Helper.getClass(className);
        TutorTest_Helper.getMethod(methodNameIt, classH2, classH2, BiPredicate.class, Function.class, Predicate.class);
        TutorTest_Helper.getMethod(methodNameRec, classH2, classH2, BiPredicate.class, Function.class, Predicate.class);
        TutorTest_Helper.getMethod(methodNameRecHelp, classH2, classH2, BiPredicate.class, Function.class,
                                   Predicate.class, ListItem.class, ListItem.class, int.class);
    }

    @Test
    public void testMixinIterativelyMethodSignatures() {
        var classH2 = TutorTest_Helper.getClass(className);
        var method = TutorTest_Helper.getMethod(methodNameIt, classH2, classH2, BiPredicate.class, Function.class,
                                                Predicate.class);
        TutorTest_H2_Helper.assertMixinMethodsSignatures(method, methodNameIt);
    }

    @Test
    public void testMixinRecursivelyMethodSignatures() {
        var classH2 = TutorTest_Helper.getClass(className);
        var method = TutorTest_Helper.getMethod(methodNameRec, classH2, classH2, BiPredicate.class, Function.class,
                                                Predicate.class);
        TutorTest_H2_Helper.assertMixinMethodsSignatures(method, methodNameRec);
    }

    @Test
    public void testMixinHelperMethod() {
        var classH2 = TutorTest_Helper.getClass(className);
        var method = TutorTest_Helper.getMethod(methodNameRecHelp, classH2, classH2, BiPredicate.class, Function.class,
                                                Predicate.class, ListItem.class, ListItem.class, int.class);

        // is generic with type U
        assertEquals(1, method.getTypeParameters().length, TutorTest_Messages.methodNotGeneric(methodNameRecHelp));
        assertEquals("U", method.getTypeParameters()[0].getTypeName(),
                     TutorTest_Messages.methodGenericTypeIncorrect(methodNameRecHelp));

        // is private (Transformer changed this to public)
        // assertTrue(isPrivate(m.getModifiers()), TutorTest_Messages.methodModifierIncorrect(methodName));

        // all params are found
        var params = method.getParameters();
        assertEquals(7, params.length, TutorTest_Messages.methodParamIncomplete(methodNameRecHelp));

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
                   TutorTest_Messages.methodParamIncorrect(methodNameRecHelp));

        // return type is correct
        assertEquals(void.class, method.getReturnType(),
                     TutorTest_Messages.methodReturnTypeIncorrect(methodNameRecHelp));

        // thrown exception type is correct
        assertEquals(classNameExc, method.getExceptionTypes()[0].getSimpleName(),
                     TutorTest_Messages.methodExceptionTypeIncorrect(methodNameRecHelp));
    }

    @Test
    public void testMixinIteratively() {
        var classH2 = TutorTest_Helper.getClass(className);
        TutorTest_Helper.getMethod(methodNameIt, classH2, classH2, BiPredicate.class, Function.class, Predicate.class);

        TutorTest_H2_Helper<Integer> helper1 = new TutorTest_H2_Helper<>();
        TutorTest_H2_Helper<String> helper2 = new TutorTest_H2_Helper<>();

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
        var classH2 = TutorTest_Helper.getClass(className);
        TutorTest_Helper.getClass(classNameExc);
        TutorTest_Helper.getMethod(methodNameIt, classH2, classH2, BiPredicate.class, Function.class, Predicate.class);

        TutorTest_H2_Helper<Integer> helper1 = new TutorTest_H2_Helper<>();
        TutorTest_H2_Helper<String> helper2 = new TutorTest_H2_Helper<>();

        var thisLists1 = TutorTest_Generators.generateThisListMixin1();
        var otherLists1 = TutorTest_Generators.generateOtherListMixin1WithExc();
        var thisLists2 = TutorTest_Generators.generateThisListMixin2();
        var otherLists2 = TutorTest_Generators.generateOtherListMixin2WithExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralMixinException(thisLists1, otherLists1, TutorTest_H2_Helper.MethodType.ITERATIVE,
                                          TutorTest_Generators.biPred1, TutorTest_Generators.fctMixin1,
                                          TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralMixinException(thisLists2, otherLists2, TutorTest_H2_Helper.MethodType.ITERATIVE,
                                          TutorTest_Generators.biPred2, TutorTest_Generators.fctMixin2,
                                          TutorTest_Generators.predU2);
    }

    @Test
    public void testMixinRecursively() {
        var classH2 = TutorTest_Helper.getClass(className);
        TutorTest_Helper.getMethod(methodNameRec, classH2, classH2, BiPredicate.class, Function.class, Predicate.class);

        TutorTest_H2_Helper<Integer> helper1 = new TutorTest_H2_Helper<>();
        TutorTest_H2_Helper<String> helper2 = new TutorTest_H2_Helper<>();

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
        var classH2 = TutorTest_Helper.getClass(className);
        TutorTest_Helper.getClass(classNameExc);
        TutorTest_Helper.getMethod(methodNameRec, classH2, classH2, BiPredicate.class, Function.class, Predicate.class);

        TutorTest_H2_Helper<Integer> helper1 = new TutorTest_H2_Helper<>();
        TutorTest_H2_Helper<String> helper2 = new TutorTest_H2_Helper<>();

        var thisLists1 = TutorTest_Generators.generateThisListMixin1();
        var otherLists1 = TutorTest_Generators.generateOtherListMixin1WithExc();
        var thisLists2 = TutorTest_Generators.generateThisListMixin2();
        var otherLists2 = TutorTest_Generators.generateOtherListMixin2WithExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralMixinException(thisLists1, otherLists1, TutorTest_H2_Helper.MethodType.RECURSIVE,
                                          TutorTest_Generators.biPred1, TutorTest_Generators.fctMixin1,
                                          TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralMixinException(thisLists2, otherLists2, TutorTest_H2_Helper.MethodType.RECURSIVE,
                                          TutorTest_Generators.biPred2, TutorTest_Generators.fctMixin2,
                                          TutorTest_Generators.predU2);
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testMixinNoOtherMethods(final TestCycle testCycle) {
        var classH2 = TutorTest_Helper.getClassDontFail(className);
        if (classH2 == null) {
            // do not take other points
            return;
        }
        var method = TutorTest_Helper.getMethodDontFail(methodNameIt, classH2, classH2, BiPredicate.class,
                                                        Function.class, Predicate.class);
        if (method != null) {
            TutorTest_H2_Helper.assertNoOtherMethod(testCycle, classH2, methodNameIt);
        }
        method = TutorTest_Helper.getMethodDontFail(methodNameRec, classH2, classH2, BiPredicate.class, Function.class,
                                                    Predicate.class);
        if (method != null) {
            TutorTest_H2_Helper.assertNoOtherMethod(testCycle, classH2, methodNameRec);
        }
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testMixinReallyIteratively(final TestCycle testCycle) {
        var classH2 = TutorTest_Helper.getClassDontFail(className);
        if (classH2 == null) {
            // do not take other points
            return;
        }

        var method = TutorTest_Helper.getMethodDontFail(methodNameIt, classH2, classH2, BiPredicate.class,
                                                        Function.class, Predicate.class);
        if (method != null) {
            TutorTest_H2_Helper.assertNumberOfLoop(testCycle, classH2, methodNameIt, 1);
        }
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testMixinReallyRecursively(final TestCycle testCycle) {
        var classH2 = TutorTest_Helper.getClassDontFail(className);
        if (classH2 == null) {
            // do not take other points
            return;
        }

        var method = TutorTest_Helper.getMethodDontFail(methodNameRec, classH2, classH2, BiPredicate.class,
                                                        Function.class, Predicate.class);
        if (method == null) {
            // do not take other points
            return;
        }

        // not iterative
        TutorTest_H2_Helper.assertNumberOfLoop(testCycle, classH2, methodNameRec, 0);

        var thisList = TutorTest_Generators.generateThisListMixinMockito();
        var otherList = TutorTest_Generators.generateOtherListMixinMockito();

        try {
            thisList.mixinRecursively(otherList, TutorTest_Generators.biPred1, TutorTest_Generators.fctMixin1,
                                      TutorTest_Generators.predU1);
        } catch (Exception e) {
            // do not take other points
            return;
        }

        // mixinRecursivelyHelper is set to public for this test
        try {
            Mockito.verify(thisList, Mockito.atLeast(2))
                .mixinRecursivelyHelper(ArgumentMatchers.any(), ArgumentMatchers.any(),
                                        ArgumentMatchers.any(), ArgumentMatchers.any(),
                                        ArgumentMatchers.any(), ArgumentMatchers.any(), Mockito.anyInt());
        } catch (Exception e) {
            // MyLinkedListException will never be thrown
            fail(TutorTest_Messages.methodNoRecursion(methodNameRec));
        }
    }
}
