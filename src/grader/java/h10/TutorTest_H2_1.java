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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Defines the JUnit test cases related to the class defined in the task H2.1.
 *
 * @author Arianne Roselina Prananto
 */
@TestForSubmission("h10")
@DisplayName("Criterion: H2.1")
public final class TutorTest_H2_1 {
    static final String className = "MyLinkedList";
    static final String methodNameIt = "extractIteratively";
    static final String methodNameRec = "extractRecursively";
    static final String methodNameRecHelp = "extractRecursivelyHelper";
    static final String classNameExc = "MyLinkedListException";

    /* *********************************************************************
     *                               H2.1                                  *
     **********************************************************************/

    @Test
    public void testExtractMethodsExist() {
        var classH2 = TutorTest_Helper.getClass(className);
        TutorTest_Helper.getMethod(methodNameIt, classH2, Predicate.class, Function.class, Predicate.class);
        TutorTest_Helper.getMethod(methodNameRec, classH2, Predicate.class, Function.class, Predicate.class);
        TutorTest_Helper.getMethod(methodNameRecHelp, classH2, Predicate.class, Function.class, Predicate.class,
                                   ListItem.class, int.class);
    }

    @Test
    public void testExtractIterativelyMethodSignatures() {
        var classH2 = TutorTest_Helper.getClass(className);
        var method = TutorTest_Helper.getMethod(methodNameIt, classH2, Predicate.class, Function.class, Predicate.class);
        TutorTest_H2_Helper.assertExtractMethodsSignatures(method, methodNameIt);
    }

    @Test
    public void testExtractRecursivelyMethodSignatures() {
        var classH2 = TutorTest_Helper.getClass(className);
        var method = TutorTest_Helper.getMethod(methodNameRec, classH2, Predicate.class, Function.class, Predicate.class);
        TutorTest_H2_Helper.assertExtractMethodsSignatures(method, methodNameRec);
    }

    @Test
    public void testExtractHelperMethod() {
        var classH2 = TutorTest_Helper.getClass(className);
        var method = TutorTest_Helper.getMethod(methodNameRecHelp, classH2, Predicate.class, Function.class,
                                                Predicate.class, ListItem.class, int.class);

        // is generic with type U
        assertEquals(1, method.getTypeParameters().length, TutorTest_Messages.methodNotGeneric(methodNameRecHelp));
        assertEquals("U", method.getTypeParameters()[0].getTypeName(),
                     TutorTest_Messages.methodGenericTypeIncorrect(methodNameRecHelp));

        // is private (Transformer changed this to public)
        // assertTrue(isPrivate(m.getModifiers()), TutorTest_Messages.methodModifierIncorrect(methodName));

        // all params are found
        var params = method.getParameters();
        assertEquals(5, params.length, TutorTest_Messages.methodParamIncomplete(methodNameRecHelp));

        // param types are correct
        var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName())
            .collect(Collectors.toList());
        assertTrue(paramTypes.contains("java.util.function.Predicate<? super T>")
                   && (paramTypes.contains("java.util.function.Function<? super T, ? extends U>")
                       || paramTypes.contains("java.util.function.Function<? super T,? extends U>"))
                   && paramTypes.contains("java.util.function.Predicate<? super U>")
                   && paramTypes.contains("h10.ListItem<T>")
                   && paramTypes.contains("int"),
                   TutorTest_Messages.methodParamIncorrect(methodNameRecHelp));

        // return type is correct
        assertEquals("h10.MyLinkedList<U>", method.getGenericReturnType().getTypeName(),
                     TutorTest_Messages.methodReturnTypeIncorrect(methodNameRecHelp));

        // thrown exception type is correct
        assertEquals(classNameExc, method.getExceptionTypes()[0].getSimpleName(),
                     TutorTest_Messages.methodExceptionTypeIncorrect(methodNameRecHelp));

    }

    @Test
    public void testExtractIteratively() {
        var classH2 = TutorTest_Helper.getClass(className);
        TutorTest_Helper.getMethod(methodNameIt, classH2, Predicate.class, Function.class, Predicate.class);

        TutorTest_H2_Helper<Integer> helper1 = new TutorTest_H2_Helper<>();
        TutorTest_H2_Helper<String> helper2 = new TutorTest_H2_Helper<>();

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
        var classH2 = TutorTest_Helper.getClass(className);
        TutorTest_Helper.getClass(classNameExc);
        TutorTest_Helper.getMethod(methodNameIt, classH2, Predicate.class, Function.class, Predicate.class);

        TutorTest_H2_Helper<Integer> helper1 = new TutorTest_H2_Helper<>();
        TutorTest_H2_Helper<String> helper2 = new TutorTest_H2_Helper<>();

        var thisLists1 = TutorTest_Generators.generateThisListExtract1WithExc();
        var thisLists2 = TutorTest_Generators.generateThisListExtract2WithExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralExtractException(thisLists1, TutorTest_H2_Helper.MethodType.ITERATIVE,
                                            TutorTest_Generators.predT1, TutorTest_Generators.fctExtract1,
                                            TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralExtractException(thisLists2, TutorTest_H2_Helper.MethodType.ITERATIVE,
                                            TutorTest_Generators.predT2, TutorTest_Generators.fctExtract2,
                                            TutorTest_Generators.predU2);
    }

    @Test
    public void testExtractRecursively() {
        var classH2 = TutorTest_Helper.getClass(className);
        TutorTest_Helper.getMethod(methodNameRec, classH2, Predicate.class, Function.class, Predicate.class);

        TutorTest_H2_Helper<Integer> helper1 = new TutorTest_H2_Helper<>();
        TutorTest_H2_Helper<String> helper2 = new TutorTest_H2_Helper<>();

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
        var classH2 = TutorTest_Helper.getClass(className);
        TutorTest_Helper.getClass(classNameExc);
        TutorTest_Helper.getMethod(methodNameRec, classH2, Predicate.class, Function.class, Predicate.class);

        TutorTest_H2_Helper<Integer> helper1 = new TutorTest_H2_Helper<>();
        TutorTest_H2_Helper<String> helper2 = new TutorTest_H2_Helper<>();

        var thisLists1 = TutorTest_Generators.generateThisListExtract1WithExc();
        var thisLists2 = TutorTest_Generators.generateThisListExtract2WithExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralExtractException(thisLists1, TutorTest_H2_Helper.MethodType.RECURSIVE,
                                            TutorTest_Generators.predT1, TutorTest_Generators.fctExtract1,
                                            TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralExtractException(thisLists2, TutorTest_H2_Helper.MethodType.RECURSIVE,
                                            TutorTest_Generators.predT2, TutorTest_Generators.fctExtract2,
                                            TutorTest_Generators.predU2);
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testExtractNoOtherMethods(final TestCycle testCycle) {
        var classH2 = TutorTest_Helper.getClassDontFail(className);
        if (classH2 == null) {
            // do not take other points
            return;
        }

        var method = TutorTest_Helper.getMethodDontFail(methodNameIt, classH2, Predicate.class, Function.class,
                                                        Predicate.class);
        if (method != null) {
            TutorTest_H2_Helper.assertNoOtherMethod(testCycle, classH2, methodNameIt);
        }
        method = TutorTest_Helper.getMethodDontFail(methodNameRec, classH2, Predicate.class, Function.class,
                                                    Predicate.class);
        if (method != null) {
            TutorTest_H2_Helper.assertNoOtherMethod(testCycle, classH2, methodNameRec);
        }
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testExtractReallyIteratively(final TestCycle testCycle) {
        var classH2 = TutorTest_Helper.getClassDontFail(className);
        if (classH2 == null) {
            // do not take other points
            return;
        }

        var method = TutorTest_Helper.getMethodDontFail(methodNameIt, classH2, Predicate.class, Function.class,
                                                        Predicate.class);
        if (method != null) {
            TutorTest_H2_Helper.assertNumberOfLoop(testCycle, classH2, methodNameIt, 1);
        }
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testExtractReallyRecursively(final TestCycle testCycle) {
        var classH2 = TutorTest_Helper.getClassDontFail(className);
        if (classH2 == null) {
            // do not take other points
            return;
        }

        var method = TutorTest_Helper.getMethodDontFail(methodNameRec, classH2, Predicate.class, Function.class,
                                                        Predicate.class);
        if (method == null) {
            // do not take other points
            return;
        }

        // not iterative
        TutorTest_H2_Helper.assertNumberOfLoop(testCycle, classH2, methodNameRec, 0);

        var thisList = TutorTest_Generators.generateThisListExtractMockito();
        ListItem<Integer> dummy = new ListItem<>();
        dummy.next = thisList.head;

        try {
            thisList.extractRecursively(TutorTest_Generators.predT1, TutorTest_Generators.fctExtract1,
                                        TutorTest_Generators.predU1);
        } catch (Exception e) {
            // do not take other points
            return;
        }

        // extractRecursivelyHelper is set to public for this test
        try {
            Mockito.verify(thisList, Mockito.atLeast(2))
                .extractRecursivelyHelper(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(),
                                          ArgumentMatchers.any(), Mockito.anyInt());
        } catch (Exception e) {
            // MyLinkedListException will never be thrown
            fail(TutorTest_Messages.methodNoRecursion(methodNameRec));
        }
    }
}
