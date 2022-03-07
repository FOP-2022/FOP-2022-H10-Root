package h10;

import h10.utils.ExpectedMyLinkedListException;
import h10.utils.TutorTest_Constants;
import h10.utils.TutorTest_Generators;
import h10.utils.TutorTest_Messages;
import h10.utils.spoon.LoopsMethodBodyProcessor;
import h10.utils.spoon.MethodCallsProcessor;
import h10.utils.spoon.SpoonUtils;
import h10.utils.spoon.StatementsMethodBodyProcessor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sourcegrade.jagr.api.testing.TestCycle;
import org.sourcegrade.jagr.api.testing.extension.JagrExecutionCondition;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Define some helper methods for task H2.
 *
 * @author Arianne Roselina Prananto
 */
public final class TutorTest_H2_Helper<T> {

    /**
     * Decide if we want to check the normal/exception execution.
     */
    protected enum ExecutionType {
        NORMAL,
        EXCEPTION
    }

    /**
     * Decide which type of extract/mixin method is used.
     */
    protected enum MethodType {
        ITERATIVE,
        RECURSIVE
    }

    /**
     * Decide which list (source/target) is checked.
     */
    protected enum ListType {
        SOURCE,
        TARGET
    }

    /**
     * Decide which mixin* type is used, since there are two acceptable answers.
     */
    protected enum MixinType {
        SOLUTION,
        SORTED
    }

    /* *********************************************************************
     *                       Correct expected methods                      *
     **********************************************************************/

    private <U> MyLinkedList<U> expectedExtract(MyLinkedList<T> sourceList, Predicate<? super T> predT,
                                                Function<? super T, ? extends U> fct, Predicate<? super U> predU)
        throws ExpectedMyLinkedListException {
        MyLinkedList<U> removed = new MyLinkedList<>();
        ListItem<U> tail = null;
        if (sourceList.head == null) {
            return removed;
        }

        ListItem<T> current = new ListItem<>();
        current.next = sourceList.head;
        ListItem<T> result = current;

        int index = 0;
        while (current.next != null) {
            T key = current.next.key;
            if (!predT.test(key)) {
                current = current.next;
                index++;
                continue;
            }

            U mapped = fct.apply(key);
            if (!predU.test(mapped)) {
                throw new ExpectedMyLinkedListException(index, mapped);
            }

            current.next = current.next.next;

            ListItem<U> item = new ListItem<>(mapped);
            if (tail == null) {
                removed.head = tail = item;
            } else {
                tail = tail.next = item;
            }

            index++;
        }

        sourceList.head = result.next;
        return removed;
    }

    private <U> void expectedMixin(MyLinkedList<T> sourceList, MyLinkedList<U> otherList,
                                   BiPredicate<? super T, ? super U> biPred, Function<? super U, ? extends T> fct,
                                   Predicate<? super U> predU, MixinType mixinType)
        throws ExpectedMyLinkedListException {
        if (otherList.head == null) {
            return;
        }

        ListItem<U> others = otherList.head;
        ListItem<T> current = new ListItem<>();
        current.next = sourceList.head;
        ListItem<T> result = current;
        int index = 0;

        while (others != null) {
            U key = others.key;
            if (!predU.test(key)) {
                throw new ExpectedMyLinkedListException(index, key);
            } else if (current.next != null) {
                T element = current.next.key;
                if (biPred.test(element, key)) {
                    T mapped = fct.apply(key);
                    ListItem<T> item = new ListItem<>(mapped);
                    item.next = current.next;
                    current.next = item;

                    others = others.next;
                    if (mixinType == MixinType.SOLUTION) {
                        current = current.next;
                    }
                    index++;
                }
            } else {
                T mapped = fct.apply(key);
                current.next = new ListItem<>(mapped);
                others = others.next;
                index++;
            }
            current = current.next;
        }
        sourceList.head = result.next;
    }

    /* *********************************************************************
     *                   General extract and mixin tests                   *
     **********************************************************************/

    protected static void testExtract(ExecutionType executionType, MethodType methodType, ListType listType) {
        TutorTest_H2_Helper<Integer> helper1 = new TutorTest_H2_Helper<>();
        TutorTest_H2_Helper<String> helper2 = new TutorTest_H2_Helper<>();

        if (executionType == ExecutionType.NORMAL) {
            var thisLists1 = TutorTest_Generators.generateThisListExtract1WithoutExc();
            var thisLists2 = TutorTest_Generators.generateThisListExtract2WithoutExc();

            // call test for the first list type (Integer, Integer[])
            helper1.testExtractNormal(thisLists1, methodType, TutorTest_Generators.predT1,
                TutorTest_Generators.fctExtract1, TutorTest_Generators.predU1, listType);
            // call test for the second list type (String, Double)
            helper2.testExtractNormal(thisLists2, methodType, TutorTest_Generators.predT2,
                TutorTest_Generators.fctExtract2, TutorTest_Generators.predU2, listType);
        } else {
            var thisLists1 = TutorTest_Generators.generateThisListExtract1WithExc();
            var thisLists2 = TutorTest_Generators.generateThisListExtract2WithExc();

            // call test for the first list type (Integer, Integer[])
            helper1.testExtractException(thisLists1, methodType, TutorTest_Generators.predT1,
                TutorTest_Generators.fctExtract1, TutorTest_Generators.predU1);
            // call test for the second list type (String, Double)
            helper2.testExtractException(thisLists2, methodType, TutorTest_Generators.predT2,
                TutorTest_Generators.fctExtract2, TutorTest_Generators.predU2);
        }
    }

    private <U> void testExtractNormal(MyLinkedList<T>[] thisLists, MethodType methodType,
                                       Predicate<? super T> predT, Function<? super T, ? extends U> fct,
                                       Predicate<? super U> predU, ListType listType) {
        MyLinkedList<U> otherList = new MyLinkedList<>();
        MyLinkedList<U> expectedOtherList = new MyLinkedList<>();

        for (var thisList : thisLists) {
            var expectedThisList = copyList(thisList);
            // check actual values
            try {
                if (methodType == MethodType.ITERATIVE) {
                    otherList = thisList.extractIteratively(predT, fct, predU);
                } else {
                    otherList = thisList.extractRecursively(predT, fct, predU);
                }
            } catch (MyLinkedListException e) {
                assertNull(e.getMessage(), (TutorTest_Messages.exceptionMessageIncorrect()));
            } catch (Exception e) {
                // ignore
            }

            // check expected values
            try {
                expectedOtherList = expectedExtract(expectedThisList, predT, fct, predU);
            } catch (ExpectedMyLinkedListException expectedExc) {
                // will never happen
            }

            // assert lists
            if (listType == ListType.SOURCE) {
                assertLinkedList(expectedThisList, thisList);
            } else {
                assertLinkedList(expectedOtherList, otherList);
            }
        }
    }

    private <U> void testExtractException(MyLinkedList<T>[] thisLists, MethodType methodType,
                                          Predicate<? super T> predT, Function<? super T, ? extends U> fct,
                                          Predicate<? super U> predU) {
        Exception actualExc = null;

        for (var thisList : thisLists) {
            var expectedThisList = copyList(thisList);
            // check actual values
            try {
                if (methodType == MethodType.ITERATIVE) {
                    thisList.extractIteratively(predT, fct, predU);
                } else {
                    thisList.extractRecursively(predT, fct, predU);
                }
            } catch (MyLinkedListException e) {
                actualExc = e;
            } catch (Exception e) {
                // ignore
            }

            // check expected values
            try {
                expectedExtract(expectedThisList, predT, fct, predU);
            } catch (ExpectedMyLinkedListException expectedExc) {
                assertExceptionMessage(expectedExc, actualExc);
            }
        }
    }

    protected static void testMixin(ExecutionType executionType, MethodType methodType, ListType listType) {
        TutorTest_H2_Helper<Integer> helper1 = new TutorTest_H2_Helper<>();
        TutorTest_H2_Helper<String> helper2 = new TutorTest_H2_Helper<>();

        if (executionType == ExecutionType.NORMAL) {
            var thisLists1 = TutorTest_Generators.generateThisListMixin1();
            var otherLists1 = TutorTest_Generators.generateOtherListMixin1WithoutExc();
            var thisLists2 = TutorTest_Generators.generateThisListMixin2();
            var otherLists2 = TutorTest_Generators.generateOtherListMixin2WithoutExc();

            // call test for the first list type (Integer, Integer[])
            helper1.testMixinNormal(thisLists1, otherLists1, methodType, TutorTest_Generators.biPred1,
                TutorTest_Generators.fctMixin1, TutorTest_Generators.predU1, listType);
            // call test for the second list type (String, Double)
            helper2.testMixinNormal(thisLists2, otherLists2, methodType, TutorTest_Generators.biPred2,
                TutorTest_Generators.fctMixin2, TutorTest_Generators.predU2, listType);
        } else {
            var thisLists1 = TutorTest_Generators.generateThisListMixin1();
            var otherLists1 = TutorTest_Generators.generateOtherListMixin1WithExc();
            var thisLists2 = TutorTest_Generators.generateThisListMixin2();
            var otherLists2 = TutorTest_Generators.generateOtherListMixin2WithExc();

            // call test for the first list type (Integer, Integer[])
            helper1.testMixinException(thisLists1, otherLists1, methodType, TutorTest_Generators.biPred1,
                TutorTest_Generators.fctMixin1, TutorTest_Generators.predU1);
            // call test for the second list type (String, Double)
            helper2.testMixinException(thisLists2, otherLists2, methodType, TutorTest_Generators.biPred2,
                TutorTest_Generators.fctMixin2, TutorTest_Generators.predU2);
        }
    }

    private <U> void testMixinNormal(MyLinkedList<T>[] thisLists, MyLinkedList<U>[] otherLists, MethodType methodType,
                                     BiPredicate<? super T, ? super U> biPred, Function<? super U, ? extends T> fct,
                                     Predicate<? super U> predU, ListType listType) {
        // first list
        for (int i = 0; i < thisLists.length; i++) {
            var expectedThisList1 = copyList(thisLists[i]);
            var expectedThisList2 = copyList(thisLists[i]);
            var expectedOtherList1 = copyList(otherLists[i]);
            var expectedOtherList2 = copyList(otherLists[i]);

            // check actual values
            try {
                if (methodType == MethodType.ITERATIVE) {
                    thisLists[i].mixinIteratively(otherLists[i], biPred, fct, predU);
                } else {
                    thisLists[i].mixinRecursively(otherLists[i], biPred, fct, predU);
                }
            } catch (MyLinkedListException e) {
                assertNull(e.getMessage(), (TutorTest_Messages.exceptionMessageIncorrect()));
            } catch (Exception e) {
                // ignore
            }

            // check expected values
            try {
                expectedMixin(expectedThisList1, expectedOtherList1, biPred, fct, predU, MixinType.SOLUTION);
                expectedMixin(expectedThisList2, expectedOtherList2, biPred, fct, predU, MixinType.SORTED);
            } catch (ExpectedMyLinkedListException expectedExc) {
                // will never happen
            }

            // assert lists
            if (listType == ListType.SOURCE) {
                assertTwoLinkedLists(expectedOtherList1, expectedOtherList2, otherLists[i]);
            } else {
                assertTwoLinkedLists(expectedThisList1, expectedThisList2, thisLists[i]);
            }
        }
    }

    private <U> void testMixinException(MyLinkedList<T>[] thisLists, MyLinkedList<U>[] otherLists,
                                        MethodType methodType, BiPredicate<? super T, ? super U> biPred,
                                        Function<? super U, ? extends T> fct, Predicate<? super U> predU) {
        Exception actualExc = null;
        ExpectedMyLinkedListException expectedExc1 = null;
        ExpectedMyLinkedListException expectedExc2 = null;

        // first list
        for (int i = 0; i < thisLists.length; i++) {
            var expectedThisList1 = copyList(thisLists[i]);
            var expectedThisList2 = copyList(thisLists[i]);
            var expectedOtherList1 = copyList(otherLists[i]);
            var expectedOtherList2 = copyList(otherLists[i]);

            // check actual values
            try {
                if (methodType == MethodType.ITERATIVE) {
                    thisLists[i].mixinIteratively(otherLists[i], biPred, fct, predU);
                } else {
                    thisLists[i].mixinRecursively(otherLists[i], biPred, fct, predU);
                }
            } catch (MyLinkedListException e) {
                actualExc = e;
            } catch (Exception e) {
                // ignore
            }

            // check expected values
            try {
                expectedMixin(expectedThisList1, expectedOtherList1, biPred, fct, predU, MixinType.SOLUTION);
            } catch (ExpectedMyLinkedListException e) {
                expectedExc1 = e;
            }

            try {
                expectedMixin(expectedThisList2, expectedOtherList2, biPred, fct, predU, MixinType.SORTED);
            } catch (ExpectedMyLinkedListException e) {
                expectedExc2 = e;
            }

            assertTwoExceptionMessages(expectedExc1, expectedExc2, actualExc);
        }
    }

    /* *********************************************************************
     *                           Assertion methods                         *
     **********************************************************************/

    protected static void assertExtractMethodsSignatures(Method method, String methodName) {
        // is generic with type U
        assertEquals(1, method.getTypeParameters().length, TutorTest_Messages.methodNotGeneric(methodName));
        assertEquals("U", method.getTypeParameters()[0].getTypeName(),
            TutorTest_Messages.methodGenericTypeIncorrect(methodName));

        // is public
        assertTrue(isPublic(method.getModifiers()), TutorTest_Messages.methodModifierIncorrect(methodName));

        // all params are found
        var params = method.getParameters();
        assertEquals(3, params.length, TutorTest_Messages.methodParamIncomplete(methodName));

        // param types are correct
        var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName()).collect(Collectors.toList());
        assertTrue(paramTypes.contains(TutorTest_Constants.PRED + "<? super T>")
                && (paramTypes.contains(TutorTest_Constants.FCT + "<? super T, ? extends U>")
                || paramTypes.contains(TutorTest_Constants.FCT + "<? super T,? extends U>"))
                && paramTypes.contains(TutorTest_Constants.PRED + "<? super U>"),
            TutorTest_Messages.methodParamIncorrect(methodName));

        // return type is correct
        assertEquals(TutorTest_Constants.CLASS_LIST + "<U>", method.getGenericReturnType().getTypeName(),
            TutorTest_Messages.methodReturnTypeIncorrect(methodName));

        // thrown exception type is correct
        assertEquals(TutorTest_Constants.CLASS_EXC.substring(4), method.getExceptionTypes()[0].getSimpleName(),
            TutorTest_Messages.methodExceptionTypeIncorrect(methodName));
    }

    protected static void assertMixinMethodsSignatures(Method method, String methodName) {
        // is generic with type U
        assertEquals(1, method.getTypeParameters().length, TutorTest_Messages.methodNotGeneric(methodName));
        assertEquals("U", method.getTypeParameters()[0].getTypeName(),
            TutorTest_Messages.methodGenericTypeIncorrect(methodName));

        // is public
        assertTrue(isPublic(method.getModifiers()), TutorTest_Messages.methodModifierIncorrect(methodName));

        // all params are found
        var params = method.getParameters();
        assertEquals(4, params.length, TutorTest_Messages.methodParamIncomplete(methodName));

        // param types are correct
        var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName())
            .collect(Collectors.toList());
        assertTrue(paramTypes.contains(TutorTest_Constants.CLASS_LIST + "<U>")
                && (paramTypes.contains(TutorTest_Constants.BI_PRED + "<? super T, ? super U>")
                || paramTypes.contains(TutorTest_Constants.BI_PRED + "<? super T,? super U>"))
                && (paramTypes.contains(TutorTest_Constants.FCT + "<? super U, ? extends T>")
                || paramTypes.contains(TutorTest_Constants.FCT + "<? super U,? extends T>"))
                && paramTypes.contains(TutorTest_Constants.PRED + "<? super U>"),
            TutorTest_Messages.methodParamIncorrect(methodName));

        // return type is correct
        assertEquals(void.class, method.getReturnType(),
            TutorTest_Messages.methodReturnTypeIncorrect(methodName));

        // thrown exception type is correct
        assertEquals(TutorTest_Constants.CLASS_EXC.substring(4), method.getExceptionTypes()[0].getSimpleName(),
            TutorTest_Messages.methodExceptionTypeIncorrect(methodName));
    }

    private static <U> void assertTwoLinkedLists(MyLinkedList<U> expected1, MyLinkedList<U> expected2,
                                                 MyLinkedList<U> actual) {
        int count = 0;
        AssertionError fail = null;

        try {
            assertLinkedList(expected1, actual);
        } catch (AssertionError e) {
            count++;
            fail = e;
        }

        try {
            assertLinkedList(expected2, actual);
        } catch (AssertionError e) {
            count++;
            fail = e;
        }

        if (count == 2) {
            fail(fail.getMessage());
        }
    }

    private static <U> void assertLinkedList(MyLinkedList<U> expected, MyLinkedList<U> actual) {
        // one of the list is null
        if (expected == null || actual == null) {
            assertEquals(expected, actual, TutorTest_Messages.assertListFailed());
            return;
        }

        // if one of the head is null
        if (expected.head == null || actual.head == null) {
            assertEquals(expected.head, actual.head, TutorTest_Messages.assertListFailed());
            return;
        }

        ListItem<U> actualCurrent = actual.head;
        for (ListItem<U> expectedCurrent = expected.head; expectedCurrent != null; expectedCurrent = expectedCurrent.next) {
            // actual mustn't end before expected
            assertNotNull(actualCurrent, TutorTest_Messages.assertListFailed());

            // if it is an array, check each item
            if (expectedCurrent.key.toString().startsWith("[")) {
                // actual is also an array
                assertTrue(actualCurrent.key.toString().startsWith("["), TutorTest_Messages.assertListFailed());

                @SuppressWarnings("unchecked")
                var expectedArray = (U[]) expectedCurrent.key;
                @SuppressWarnings("unchecked")
                var actualArray = (U[]) expectedCurrent.key;

                // make sure both have the same lengths
                assertEquals(expectedArray.length, actualArray.length, TutorTest_Messages.assertListFailed());

                for (int i = 0; i < expectedArray.length; i++) {
                    assertEquals(expectedArray[i], actualArray[i], TutorTest_Messages.assertListFailed());
                }

                actualCurrent = actualCurrent.next;
                continue;
            }

            // if it's not an array
            assertEquals(expectedCurrent.key, actualCurrent.key, TutorTest_Messages.assertListFailed());
            actualCurrent = actualCurrent.next;
        }
        // actual must end when expected does
        assertNull(actualCurrent, TutorTest_Messages.assertListFailed());
    }

    private static void assertTwoExceptionMessages(ExpectedMyLinkedListException expected1,
                                                   ExpectedMyLinkedListException expected2, Exception actual) {
        int count = 0;
        AssertionError fail = null;

        try {
            assertExceptionMessage(expected1, actual);
        } catch (AssertionError e) {
            count++;
            fail = e;
        }

        try {
            assertExceptionMessage(expected2, actual);
        } catch (AssertionError e) {
            count++;
            fail = e;
        }

        if (count == 2) {
            fail(fail.getMessage());
        }
    }

    private static void assertExceptionMessage(ExpectedMyLinkedListException expected, Exception actual) {
        if (expected == null) {
            assertNull(actual, TutorTest_Messages.exceptionMessageIncorrect());
            return;
        } else {
            assertNotNull(actual, TutorTest_Messages.exceptionMessageIncorrect());
            assertNotNull(actual.getMessage(), TutorTest_Messages.exceptionMessageIncorrect());
        }

        // ignore the object's name
        int indexObject = expected.getMessage().indexOf('@');
        int maxIndex = (indexObject == -1) ? expected.getMessage().length() : indexObject;
        assertEquals(expected.getMessage().substring(0, maxIndex), actual.getMessage().substring(0, maxIndex),
            TutorTest_Messages.exceptionMessageIncorrect());
    }

    /**
     * make sure no other newly implemented or unimplemented methods are used, unless explicitly stated.
     *
     * @param testCycle  the test cycle
     * @param classType  the class to be checked
     * @param methodName the method to be checked
     */
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    protected static void assertNoOtherMethod(final TestCycle testCycle, Class<?> classType, String methodName) {
        if (methodIsEmpty(testCycle, classType, methodName)) {
            // do not take other points
            return;
        }

        var path = String.format("%s.java", classType.getCanonicalName().replaceAll("\\.", "/"));
        var processor = SpoonUtils.process(testCycle, path, new MethodCallsProcessor(methodName));

        final Set<String> canBeCalled = Set.of(TutorTest_Constants.METHOD_EXT_IT, TutorTest_Constants.METHOD_EXT_REC,
            TutorTest_Constants.METHOD_EXT_HELP, TutorTest_Constants.METHOD_MIX_IT,
            TutorTest_Constants.METHOD_MIX_REC, TutorTest_Constants.METHOD_MIX_HELP,
            TutorTest_Constants.METHOD_ADD, TutorTest_Constants.METHOD_TEST,
            TutorTest_Constants.METHOD_APPLY);

        for (var callee : processor.getCallees()) {
            var name = callee.getExecutable().getSimpleName();
            assertTrue(canBeCalled.contains(name), TutorTest_Messages.methodUseOtherMethod(methodName, name));
        }
    }

    /**
     * make sure number of loops used are as required (none or only one).
     *
     * @param testCycle  the test cycle
     * @param classType  the class to be checked
     * @param methodName the method to be checked
     * @param expected   the expected number of loops
     */
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    protected static void assertNumberOfLoop(final TestCycle testCycle, Class<?> classType, String methodName, int expected) {
        if (methodIsEmpty(testCycle, classType, methodName)) {
            // do not take other points
            return;
        }

        var path = String.format("%s.java", classType.getCanonicalName().replaceAll("\\.", "/"));
        var processor = SpoonUtils.process(testCycle, path,
            new LoopsMethodBodyProcessor(methodName));

        var actual = processor.getForeachLoops().size()
            + processor.getForLoops().size()
            + processor.getWhileLoops().size()
            + processor.getDoWhileLoops().size();
        assertEquals(expected, actual, TutorTest_Messages.methodFalseNumberOfLoop(methodName));
    }

    /**
     * check whether the method is empty.
     *
     * @param testCycle  the test cycle
     * @param classType  the class to be checked
     * @param methodName the method to be checked
     */
    @ExtendWith({TestCycleResolver.class, JagrExecutionCondition.class})
    protected static boolean methodIsEmpty(final TestCycle testCycle, Class<?> classType, String methodName) {
        var path = String.format("%s.java", classType.getCanonicalName().replaceAll("\\.", "/"));
        var processor = SpoonUtils.process(testCycle, path,
            new StatementsMethodBodyProcessor(methodName));
        return processor.getStatements().get(0).toString().length() <= 10;
    }

    /* *********************************************************************
     *                          Other helper methods                       *
     **********************************************************************/

    private static <U> MyLinkedList<U> copyList(MyLinkedList<U> list) {
        MyLinkedList<U> copyList = new MyLinkedList<>();
        if (list.head == null) {
            copyList.head = null;
            return copyList;
        }

        copyList.head = new ListItem<>(list.head.key);
        ListItem<U> currentCopy = copyList.head;
        for (ListItem<U> current = list.head.next; current != null; current = current.next) {
            currentCopy.next = new ListItem<>(current.key);
            currentCopy = currentCopy.next;
        }
        return copyList;
    }
}
