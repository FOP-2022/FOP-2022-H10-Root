package h10;

import h10.utils.TutorTest_Constants;
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

    /* *********************************************************************
     *                               H2.1                                  *
     **********************************************************************/

    @Test
    public void testExtractMethodsExist() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_EXT_IT, classH2, Predicate.class, Function.class,
                                   Predicate.class);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_EXT_REC, classH2, Predicate.class, Function.class,
                                   Predicate.class);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_EXT_HELP, classH2, Predicate.class, Function.class,
                                   Predicate.class, ListItem.class, int.class);
    }

    @Test
    public void testExtractIterativelyMethodSignatures() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        var method = TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_EXT_IT, classH2, Predicate.class,
                                                Function.class, Predicate.class);
        TutorTest_H2_Helper.assertExtractMethodsSignatures(method, TutorTest_Constants.METHOD_EXT_IT);
    }

    @Test
    public void testExtractRecursivelyMethodSignatures() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        var method = TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_EXT_REC, classH2, Predicate.class,
                                                Function.class, Predicate.class);
        TutorTest_H2_Helper.assertExtractMethodsSignatures(method, TutorTest_Constants.METHOD_EXT_REC);
    }

    @Test
    public void testExtractHelperMethod() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        var method = TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_EXT_HELP, classH2, Predicate.class,
                                                Function.class, Predicate.class, ListItem.class, int.class);

        // is generic with type U
        assertEquals(1, method.getTypeParameters().length,
                     TutorTest_Messages.methodNotGeneric(TutorTest_Constants.METHOD_EXT_HELP));
        assertEquals("U", method.getTypeParameters()[0].getTypeName(),
                     TutorTest_Messages.methodGenericTypeIncorrect(TutorTest_Constants.METHOD_EXT_HELP));

        // is private (Transformer changed this to public)
        // assertTrue(isPrivate(m.getModifiers()), TutorTest_Messages.methodModifierIncorrect(methodName));

        // all params are found
        var params = method.getParameters();
        assertEquals(5, params.length, TutorTest_Messages.methodParamIncomplete(TutorTest_Constants.METHOD_EXT_HELP));

        // param types are correct
        var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName())
            .collect(Collectors.toList());
        assertTrue(paramTypes.contains(TutorTest_Constants.PRED + "<? super T>")
                   && (paramTypes.contains(TutorTest_Constants.FCT + "<? super T, ? extends U>")
                       || paramTypes.contains(TutorTest_Constants.FCT + "<? super T,? extends U>"))
                   && paramTypes.contains(TutorTest_Constants.PRED + "<? super U>")
                   && paramTypes.contains(TutorTest_Constants.CLASS_ITEM + "<T>")
                   && paramTypes.contains("int"),
                   TutorTest_Messages.methodParamIncorrect(TutorTest_Constants.METHOD_EXT_HELP));

        // return type is correct
        assertEquals(TutorTest_Constants.CLASS_LIST + "<U>", method.getGenericReturnType().getTypeName(),
                     TutorTest_Messages.methodReturnTypeIncorrect(TutorTest_Constants.METHOD_EXT_HELP));

        // thrown exception type is correct
        assertEquals(TutorTest_Constants.CLASS_EXC.substring(4), method.getExceptionTypes()[0].getSimpleName(),
                     TutorTest_Messages.methodExceptionTypeIncorrect(TutorTest_Constants.METHOD_EXT_HELP));

    }

    @Test
    public void testExtractIterativelySource() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_EXT_IT, classH2, Predicate.class, Function.class,
                                   Predicate.class);
        TutorTest_H2_Helper.testExtract(TutorTest_H2_Helper.ExecutionType.NORMAL,
                                        TutorTest_H2_Helper.MethodType.ITERATIVE, TutorTest_H2_Helper.ListType.SOURCE);
    }

    @Test
    public void testExtractIterativelyTarget() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_EXT_IT, classH2, Predicate.class, Function.class,
                                   Predicate.class);
        TutorTest_H2_Helper.testExtract(TutorTest_H2_Helper.ExecutionType.NORMAL,
                                        TutorTest_H2_Helper.MethodType.ITERATIVE, TutorTest_H2_Helper.ListType.TARGET);
    }

    @Test
    public void testExtractIterativelyExceptionSource() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_EXT_IT, classH2, Predicate.class, Function.class,
                                   Predicate.class);
        TutorTest_Helper.getClass(TutorTest_Constants.CLASS_EXC);
        TutorTest_H2_Helper.testExtract(TutorTest_H2_Helper.ExecutionType.EXCEPTION,
                                        TutorTest_H2_Helper.MethodType.ITERATIVE, TutorTest_H2_Helper.ListType.SOURCE);
    }

    @Test
    public void testExtractIterativelyExceptionTarget() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_EXT_IT, classH2, Predicate.class, Function.class,
                                   Predicate.class);
        TutorTest_Helper.getClass(TutorTest_Constants.CLASS_EXC);
        TutorTest_H2_Helper.testExtract(TutorTest_H2_Helper.ExecutionType.EXCEPTION,
                                        TutorTest_H2_Helper.MethodType.ITERATIVE, TutorTest_H2_Helper.ListType.TARGET);
    }

    @Test
    public void testExtractRecursivelySource() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_EXT_REC, classH2, Predicate.class, Function.class,
                                   Predicate.class);
        TutorTest_H2_Helper.testExtract(TutorTest_H2_Helper.ExecutionType.NORMAL,
                                        TutorTest_H2_Helper.MethodType.RECURSIVE, TutorTest_H2_Helper.ListType.SOURCE);
    }

    @Test
    public void testExtractRecursivelyTarget() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_EXT_REC, classH2, Predicate.class, Function.class,
                                   Predicate.class);
        TutorTest_H2_Helper.testExtract(TutorTest_H2_Helper.ExecutionType.NORMAL,
                                        TutorTest_H2_Helper.MethodType.RECURSIVE, TutorTest_H2_Helper.ListType.TARGET);
    }

    @Test
    public void testExtractRecursivelyExceptionSource() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_EXT_REC, classH2, Predicate.class, Function.class,
                                   Predicate.class);
        TutorTest_Helper.getClass(TutorTest_Constants.CLASS_EXC);
        TutorTest_H2_Helper.testExtract(TutorTest_H2_Helper.ExecutionType.EXCEPTION,
                                        TutorTest_H2_Helper.MethodType.RECURSIVE, TutorTest_H2_Helper.ListType.SOURCE);
    }

    @Test
    public void testExtractRecursivelyExceptionTarget() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_EXT_REC, classH2, Predicate.class, Function.class,
                                   Predicate.class);
        TutorTest_Helper.getClass(TutorTest_Constants.CLASS_EXC);
        TutorTest_H2_Helper.testExtract(TutorTest_H2_Helper.ExecutionType.EXCEPTION,
                                        TutorTest_H2_Helper.MethodType.RECURSIVE, TutorTest_H2_Helper.ListType.TARGET);
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testExtractNoOtherMethods(final TestCycle testCycle) {
        var classH2 = TutorTest_Helper.getClassDontFail(TutorTest_Constants.CLASS_LIST);
        if (classH2 == null) {
            // do not take other points
            return;
        }

        var method = TutorTest_Helper.getMethodDontFail(TutorTest_Constants.METHOD_EXT_IT, classH2,
                                                        Predicate.class, Function.class, Predicate.class);
        if (method != null) {
            TutorTest_H2_Helper.assertNoOtherMethod(testCycle, classH2, TutorTest_Constants.METHOD_EXT_IT);
        }
        method = TutorTest_Helper.getMethodDontFail(TutorTest_Constants.METHOD_EXT_REC, classH2, Predicate.class,
                                                    Function.class, Predicate.class);
        if (method != null) {
            TutorTest_H2_Helper.assertNoOtherMethod(testCycle, classH2, TutorTest_Constants.METHOD_EXT_REC);
        }
        method = TutorTest_Helper.getMethodDontFail(TutorTest_Constants.METHOD_EXT_HELP, classH2, Predicate.class,
                                                    Function.class, Predicate.class, ListItem.class, int.class);
        if (method != null) {
            TutorTest_H2_Helper.assertNoOtherMethod(testCycle, classH2, TutorTest_Constants.METHOD_EXT_HELP);
        }
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testExtractReallyIteratively(final TestCycle testCycle) {
        var classH2 = TutorTest_Helper.getClassDontFail(TutorTest_Constants.CLASS_LIST);
        if (classH2 == null) {
            // do not take other points
            return;
        }

        var method = TutorTest_Helper.getMethodDontFail(TutorTest_Constants.METHOD_EXT_IT, classH2, Predicate.class,
                                                        Function.class, Predicate.class);
        if (method != null) {
            TutorTest_H2_Helper.assertNumberOfLoop(testCycle, classH2, TutorTest_Constants.METHOD_EXT_IT, 1);
        }
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testExtractReallyRecursively(final TestCycle testCycle) {
        var classH2 = TutorTest_Helper.getClassDontFail(TutorTest_Constants.CLASS_LIST);
        if (classH2 == null) {
            // do not take other points
            return;
        }

        var methodRec = TutorTest_Helper.getMethodDontFail(TutorTest_Constants.METHOD_EXT_REC, classH2,
                                                           Predicate.class, Function.class, Predicate.class);
        var methodRecHelp = TutorTest_Helper.getMethodDontFail(TutorTest_Constants.METHOD_EXT_HELP, classH2,
                                                               Predicate.class, Function.class, Predicate.class,
                                                               ListItem.class, int.class);
        if (methodRec == null || methodRecHelp == null) {
            // do not take other points
            return;
        }

        // not iterative
        TutorTest_H2_Helper.assertNumberOfLoop(testCycle, classH2, TutorTest_Constants.METHOD_EXT_REC, 0);

        if (TutorTest_H2_Helper.methodIsEmpty(testCycle, classH2, TutorTest_Constants.METHOD_EXT_REC)
            || TutorTest_H2_Helper.methodIsEmpty(testCycle, classH2, TutorTest_Constants.METHOD_EXT_HELP)) {
            // do not take other points
            return;
        }

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
            fail(TutorTest_Messages.methodNoRecursion(TutorTest_Constants.METHOD_EXT_REC));
        }
    }
}
