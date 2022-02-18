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

    /* *********************************************************************
     *                               H2.2                                  *
     **********************************************************************/

    @Test
    public void testMixinMethodsExist() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_MIX_IT, classH2, classH2, BiPredicate.class,
                                   Function.class, Predicate.class);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_MIX_REC, classH2, classH2, BiPredicate.class,
                                   Function.class, Predicate.class);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_MIX_HELP, classH2, classH2, BiPredicate.class,
                                   Function.class, Predicate.class, ListItem.class, ListItem.class, int.class);
    }

    @Test
    public void testMixinIterativelyMethodSignatures() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        var method = TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_MIX_IT, classH2, classH2,
                                                BiPredicate.class, Function.class, Predicate.class);
        TutorTest_H2_Helper.assertMixinMethodsSignatures(method, TutorTest_Constants.METHOD_MIX_IT);
    }

    @Test
    public void testMixinRecursivelyMethodSignatures() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        var method = TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_MIX_REC, classH2, classH2,
                                                BiPredicate.class, Function.class, Predicate.class);
        TutorTest_H2_Helper.assertMixinMethodsSignatures(method, TutorTest_Constants.METHOD_MIX_REC);
    }

    @Test
    public void testMixinHelperMethod() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        var method = TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_MIX_HELP, classH2, classH2,
                                                BiPredicate.class, Function.class, Predicate.class, ListItem.class,
                                                ListItem.class, int.class);

        // is generic with type U
        assertEquals(1, method.getTypeParameters().length,
                     TutorTest_Messages.methodNotGeneric(TutorTest_Constants.METHOD_MIX_HELP));
        assertEquals("U", method.getTypeParameters()[0].getTypeName(),
                     TutorTest_Messages.methodGenericTypeIncorrect(TutorTest_Constants.METHOD_MIX_HELP));

        // is private (Transformer changed this to public)
        // assertTrue(isPrivate(m.getModifiers()), TutorTest_Messages.methodModifierIncorrect(methodName));

        // all params are found
        var params = method.getParameters();
        assertEquals(7, params.length, TutorTest_Messages.methodParamIncomplete(TutorTest_Constants.METHOD_MIX_HELP));

        // param types are correct
        var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName())
            .collect(Collectors.toList());
        assertTrue(paramTypes.contains(TutorTest_Constants.CLASS_LIST + "<U>")
                   && (paramTypes.contains(TutorTest_Constants.BI_PRED + "<? super T, ? super U>")
                       || paramTypes.contains(TutorTest_Constants.BI_PRED + "<? super T,? super U>"))
                   && (paramTypes.contains(TutorTest_Constants.FCT + "<? super U, ? extends T>")
                       || paramTypes.contains(TutorTest_Constants.FCT + "<? super U,? extends T>"))
                   && paramTypes.contains(TutorTest_Constants.PRED + "<? super U>")
                   && paramTypes.contains(TutorTest_Constants.CLASS_ITEM + "<T>")
                   && paramTypes.contains("int"),
                   TutorTest_Messages.methodParamIncorrect(TutorTest_Constants.METHOD_MIX_HELP));

        // return type is correct
        assertEquals(void.class, method.getReturnType(),
                     TutorTest_Messages.methodReturnTypeIncorrect(TutorTest_Constants.METHOD_MIX_HELP));

        // thrown exception type is correct
        assertEquals(TutorTest_Constants.CLASS_EXC.substring(4), method.getExceptionTypes()[0].getSimpleName(),
                     TutorTest_Messages.methodExceptionTypeIncorrect(TutorTest_Constants.METHOD_MIX_HELP));
    }

    @Test
    public void testMixinIterativelySource() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_MIX_IT, classH2, classH2, BiPredicate.class,
                                   Function.class, Predicate.class);
        TutorTest_H2_Helper.testMixin(TutorTest_H2_Helper.ExecutionType.NORMAL,
                                      TutorTest_H2_Helper.MethodType.ITERATIVE, TutorTest_H2_Helper.ListType.SOURCE);
    }

    @Test
    public void testMixinIterativelyTarget() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_MIX_IT, classH2, classH2, BiPredicate.class,
                                   Function.class, Predicate.class);
        TutorTest_H2_Helper.testMixin(TutorTest_H2_Helper.ExecutionType.NORMAL,
                                      TutorTest_H2_Helper.MethodType.ITERATIVE, TutorTest_H2_Helper.ListType.TARGET);
    }

    @Test
    public void testMixinIterativelyExceptionSource() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getClass(TutorTest_Constants.CLASS_EXC);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_MIX_IT, classH2, classH2, BiPredicate.class,
                                   Function.class, Predicate.class);
        TutorTest_H2_Helper.testMixin(TutorTest_H2_Helper.ExecutionType.EXCEPTION,
                                      TutorTest_H2_Helper.MethodType.ITERATIVE, TutorTest_H2_Helper.ListType.SOURCE);
    }

    @Test
    public void testMixinIterativelyExceptionTarget() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getClass(TutorTest_Constants.CLASS_EXC);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_MIX_IT, classH2, classH2, BiPredicate.class,
                                   Function.class, Predicate.class);
        TutorTest_H2_Helper.testMixin(TutorTest_H2_Helper.ExecutionType.EXCEPTION,
                                      TutorTest_H2_Helper.MethodType.ITERATIVE, TutorTest_H2_Helper.ListType.TARGET);
    }

    @Test
    public void testMixinRecursivelySource() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_MIX_REC, classH2, classH2, BiPredicate.class,
                                   Function.class, Predicate.class);
        TutorTest_H2_Helper.testMixin(TutorTest_H2_Helper.ExecutionType.NORMAL,
                                      TutorTest_H2_Helper.MethodType.RECURSIVE, TutorTest_H2_Helper.ListType.SOURCE);
    }

    @Test
    public void testMixinRecursivelyTarget() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_MIX_REC, classH2, classH2, BiPredicate.class,
                                   Function.class, Predicate.class);
        TutorTest_H2_Helper.testMixin(TutorTest_H2_Helper.ExecutionType.NORMAL,
                                      TutorTest_H2_Helper.MethodType.RECURSIVE, TutorTest_H2_Helper.ListType.TARGET);
    }

    @Test
    public void testMixinRecursivelyExceptionSource() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getClass(TutorTest_Constants.CLASS_EXC);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_MIX_REC, classH2, classH2, BiPredicate.class,
                                   Function.class, Predicate.class);
        TutorTest_H2_Helper.testMixin(TutorTest_H2_Helper.ExecutionType.EXCEPTION,
                                      TutorTest_H2_Helper.MethodType.RECURSIVE, TutorTest_H2_Helper.ListType.SOURCE);
    }

    @Test
    public void testMixinRecursivelyExceptionTarget() {
        var classH2 = TutorTest_Helper.getClass(TutorTest_Constants.CLASS_LIST);
        TutorTest_Helper.getClass(TutorTest_Constants.CLASS_EXC);
        TutorTest_Helper.getMethod(TutorTest_Constants.METHOD_MIX_REC, classH2, classH2, BiPredicate.class,
                                   Function.class, Predicate.class);
        TutorTest_H2_Helper.testMixin(TutorTest_H2_Helper.ExecutionType.EXCEPTION,
                                      TutorTest_H2_Helper.MethodType.RECURSIVE, TutorTest_H2_Helper.ListType.TARGET);
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testMixinNoOtherMethods(final TestCycle testCycle) {
        var classH2 = TutorTest_Helper.getClassDontFail(TutorTest_Constants.CLASS_LIST);
        if (classH2 == null) {
            // do not take other points
            return;
        }

        var method = TutorTest_Helper.getMethodDontFail(TutorTest_Constants.METHOD_MIX_IT, classH2, classH2,
                                                        BiPredicate.class, Function.class, Predicate.class);
        if (method != null) {
            TutorTest_H2_Helper.assertNoOtherMethod(testCycle, classH2, TutorTest_Constants.METHOD_MIX_IT);
        }
        method = TutorTest_Helper.getMethodDontFail(TutorTest_Constants.METHOD_MIX_REC, classH2, classH2,
                                                    BiPredicate.class, Function.class, Predicate.class);
        if (method != null) {
            TutorTest_H2_Helper.assertNoOtherMethod(testCycle, classH2, TutorTest_Constants.METHOD_MIX_REC);
        }
        method = TutorTest_Helper.getMethodDontFail(TutorTest_Constants.METHOD_MIX_HELP, classH2, classH2,
                                                    BiPredicate.class, Function.class, Predicate.class, ListItem.class,
                                                    ListItem.class, int.class);
        if (method != null) {
            TutorTest_H2_Helper.assertNoOtherMethod(testCycle, classH2, TutorTest_Constants.METHOD_MIX_HELP);
        }
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testMixinReallyIteratively(final TestCycle testCycle) {
        var classH2 = TutorTest_Helper.getClassDontFail(TutorTest_Constants.CLASS_LIST);
        if (classH2 == null) {
            // do not take other points
            return;
        }

        var method = TutorTest_Helper.getMethodDontFail(TutorTest_Constants.METHOD_MIX_IT, classH2, classH2,
                                                        BiPredicate.class, Function.class, Predicate.class);
        if (method != null) {
            TutorTest_H2_Helper.assertNumberOfLoop(testCycle, classH2, TutorTest_Constants.METHOD_MIX_IT, 1);
        }
    }

    @Test
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    public void testMixinReallyRecursively(final TestCycle testCycle) {
        var classH2 = TutorTest_Helper.getClassDontFail(TutorTest_Constants.CLASS_LIST);
        if (classH2 == null) {
            // do not take other points
            return;
        }

        var methodRec = TutorTest_Helper.getMethodDontFail(TutorTest_Constants.METHOD_MIX_REC, classH2, classH2,
                                                           BiPredicate.class, Function.class, Predicate.class);
        var methodRecHelp = TutorTest_Helper.getMethodDontFail(TutorTest_Constants.METHOD_MIX_HELP, classH2,
                                                               classH2, BiPredicate.class, Function.class,
                                                               Predicate.class, ListItem.class, ListItem.class,
                                                               int.class);
        if (methodRec == null || methodRecHelp == null) {
            // do not take other points
            return;
        }

        // not iterative
        TutorTest_H2_Helper.assertNumberOfLoop(testCycle, classH2, TutorTest_Constants.METHOD_MIX_REC, 0);

        if (TutorTest_H2_Helper.methodIsEmpty(testCycle, classH2, TutorTest_Constants.METHOD_MIX_REC)
            || TutorTest_H2_Helper.methodIsEmpty(testCycle, classH2, TutorTest_Constants.METHOD_MIX_HELP)) {
            // do not take other points
            return;
        }

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
            fail(TutorTest_Messages.methodNoRecursion(TutorTest_Constants.METHOD_MIX_REC));
        }
    }
}
