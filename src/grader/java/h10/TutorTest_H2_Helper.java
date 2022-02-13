package h10;

import h10.utils.TutorTest_Messages;
import h10.utils.spoon.LoopsMethodBodyProcessor;
import h10.utils.spoon.MethodCallsProcessor;
import h10.utils.spoon.SpoonUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sourcegrade.jagr.api.testing.TestCycle;
import org.sourcegrade.jagr.api.testing.extension.JagrExecutionCondition;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Define some helper methods for task H2.
 *
 * @author Arianne Roselina Prananto
 */
public final class TutorTest_H2_Helper<T> {

    /**
     * Decide which type of extract/mixin method is used.
     */
    protected enum MethodType {
        ITERATIVE,
        RECURSIVE
    }

    /* *********************************************************************
     *                       Correct expected methods                      *
     **********************************************************************/

    protected <U> MyLinkedList<U> expectedExtract(MyLinkedList<T> sourceList, Predicate<? super T> predT,
                                                  Function<? super T, ? extends U> fct,
                                                  Predicate<? super U> predU) throws MyLinkedListException {
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
                throw new MyLinkedListException(index, mapped);
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

    protected <U> void expectedMixin(MyLinkedList<T> sourceList, MyLinkedList<U> otherList,
                                     BiPredicate<? super T, ? super U> biPred,
                                     Function<? super U, ? extends T> fct,
                                     Predicate<? super U> predU) throws MyLinkedListException {
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
            System.out.println(others.key.getClass());
            if (!predU.test(key)) {
                throw new MyLinkedListException(index, key);
            } else if (current.next != null) {
                T element = current.next.key;
                if (biPred.test(element, key)) {
                    T mapped = fct.apply(key);
                    ListItem<T> item = new ListItem<>(mapped);
                    item.next = current.next;
                    current.next = item;

                    others = others.next;
                    current = current.next;
                }
            } else {
                T mapped = fct.apply(key);
                current.next = new ListItem<>(mapped);
                others = others.next;
            }
            index++;
            current = current.next;
        }
        sourceList.head = result.next;
    }

    /* *********************************************************************
     *                   General extract and mixin tests                   *
     **********************************************************************/

    protected <U> void testGeneralExtract(MyLinkedList<T>[] thisLists, MethodType type,
                                          Predicate<? super T> predT, Function<? super T, ? extends U> fct,
                                          Predicate<? super U> predU) {
        MyLinkedList<U> otherList = new MyLinkedList<>();
        MyLinkedList<U> expectedOtherList = new MyLinkedList<>();
        MyLinkedListException actualExc = null;

        for (var thisList : thisLists) {
            var expectedThisList = copyList(thisList);
            // check actual values
            try {
                if (type == MethodType.ITERATIVE) {
                    otherList = thisList.extractIteratively(predT, fct, predU);
                } else {
                    otherList = thisList.extractRecursively(predT, fct, predU);
                }
            } catch (MyLinkedListException e) {
                actualExc = e;
            }

            // check expected values
            try {
                expectedOtherList = expectedExtract(expectedThisList, predT, fct, predU);
            } catch (MyLinkedListException expectedExc) {
                assertExceptionMessage(expectedExc, actualExc);
            }

            // assert both lists
            assertLinkedList(expectedOtherList, otherList);
            assertLinkedList(expectedThisList, thisList);
        }
    }

    protected <U> void testGeneralMixin(MyLinkedList<T>[] thisLists, MyLinkedList<U>[] otherLists,
                                        MethodType type, BiPredicate<? super T, ? super U> biPred,
                                        Function<? super U, ? extends T> fct, Predicate<? super U> predU) {
        MyLinkedListException actualExc = null;

        // first list
        for (int i = 0; i < thisLists.length; i++) {
            var expectedThisList = copyList(thisLists[i]);
            var expectedOtherList = copyList(otherLists[i]);
            // check actual values
            try {
                if (type == MethodType.ITERATIVE) {
                    thisLists[i].mixinIteratively(otherLists[i], biPred, fct, predU);
                } else {
                    thisLists[i].mixinRecursively(otherLists[i], biPred, fct, predU);
                }
            } catch (MyLinkedListException e) {
                actualExc = e;
            }

            // check expected values
            try {
                expectedMixin(expectedThisList, expectedOtherList, biPred, fct, predU);
            } catch (MyLinkedListException expectedExc) {
                assertExceptionMessage(expectedExc, actualExc);
            }

            // assert both lists
            assertLinkedList(expectedOtherList, otherLists[i]);
            assertLinkedList(expectedThisList, thisLists[i]);
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
        assertEquals(Modifier.PUBLIC, method.getModifiers(), TutorTest_Messages.methodModifierIncorrect(methodName));

        // all params are found
        var params = method.getParameters();
        assertEquals(3, params.length, TutorTest_Messages.methodParamIncomplete(methodName));

        // param types are correct
        var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName()).collect(Collectors.toList());
        assertTrue(paramTypes.contains("java.util.function.Predicate<? super T>")
                   && (paramTypes.contains("java.util.function.Function<? super T, ? extends U>")
                       || paramTypes.contains("java.util.function.Function<? super T,? extends U>"))
                   && paramTypes.contains("java.util.function.Predicate<? super U>"),
                   TutorTest_Messages.methodParamIncorrect(methodName));

        // return type is correct
        assertEquals("h10.MyLinkedList<U>", method.getGenericReturnType().getTypeName(),
                     TutorTest_Messages.methodReturnTypeIncorrect(methodName));

        // thrown exception type is correct
        assertEquals(MyLinkedListException.class, method.getExceptionTypes()[0],
                     TutorTest_Messages.methodExceptionTypeIncorrect(methodName));
    }

    protected static void assertMixinMethodsSignatures(Method method, String methodName) {
        // is generic with type U
        assertEquals(1, method.getTypeParameters().length, TutorTest_Messages.methodNotGeneric(methodName));
        assertEquals("U", method.getTypeParameters()[0].getTypeName(),
                     TutorTest_Messages.methodGenericTypeIncorrect(methodName));

        // is public
        assertEquals(Modifier.PUBLIC, method.getModifiers(), TutorTest_Messages.methodModifierIncorrect(methodName));

        // all params are found
        var params = method.getParameters();
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
        assertEquals(void.class, method.getReturnType(),
                     TutorTest_Messages.methodReturnTypeIncorrect(methodName));

        // thrown exception type is correct
        assertEquals(MyLinkedListException.class, method.getExceptionTypes()[0],
                     TutorTest_Messages.methodExceptionTypeIncorrect(methodName));
    }

    protected static <U> void assertLinkedList(MyLinkedList<U> expected, MyLinkedList<U> actual) {
        // one of the list is null
        if (expected.head == null || actual.head == null) {
            assertEquals(expected.head, actual.head, "Assertion for MyLinkedList failed");
            return;
        }

        ListItem<U> actualCurrent = actual.head;
        for (ListItem<U> expectedCurrent = expected.head; expectedCurrent != null; expectedCurrent = expectedCurrent.next) {
            // actual mustn't end before expected
            assertNotNull(actualCurrent, "Assertion for MyLinkedList failed");

            // if it is an array, check each item
            if (expectedCurrent.key.toString().startsWith("[")) {
                // actual is also an array
                assertTrue(actualCurrent.key.toString().startsWith("["), "Assertion for MyLinkedList failed");

                @SuppressWarnings("unchecked")
                var expectedArray = (U[]) expectedCurrent.key;
                @SuppressWarnings("unchecked")
                var actualArray = (U[]) expectedCurrent.key;

                // make sure both have the same lengths
                assertEquals(expectedArray.length, actualArray.length, "Assertion for MyLinkedList failed");

                for (int i = 0; i < expectedArray.length; i++) {
                    assertEquals(expectedArray[i], actualArray[i], "Assertion for MyLinkedList failed");
                }

                actualCurrent = actualCurrent.next;
                continue;
            }

            // if it's not an array
            assertEquals(expectedCurrent.key, actualCurrent.key, "Assertion for MyLinkedList failed");
            actualCurrent = actualCurrent.next;
        }
        // actual must end when expected does
        assertNull(actualCurrent, "Assertion for MyLinkedList failed");
    }

    protected static void assertExceptionMessage(MyLinkedListException expected, MyLinkedListException actual) {
        assertNotNull(actual, "Assertion for MyLinkedListException Message failed");

        // ignore the object's name
        int indexObject = expected.getMessage().indexOf('@');
        int maxIndex = (indexObject == -1) ? expected.getMessage().length() : indexObject;
        assertEquals(expected.getMessage().substring(0, maxIndex), actual.getMessage().substring(0, maxIndex),
                     "Assertion for MyLinkedListException Message failed");
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
        var path = String.format("%s.java", classType.getCanonicalName().replaceAll("\\.", "/"));
        var processor = SpoonUtils.process(testCycle, path, new MethodCallsProcessor(methodName));

        final Set<String> canBeCalled = Set.of("extractIteratively", "extractRecursively", "extractRecursivelyHelper",
                                               "mixinIteratively", "mixinRecursively", "mixinRecursivelyHelper", "add");

        for (var callee : processor.getCallees()) {
            var name = callee.getExecutable().getSimpleName();
            assertFalse(canBeCalled.contains(name), String.format("Another newly implemented method %s is used",
                                                                  name));
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
        var path = String.format("%s.java", classType.getCanonicalName().replaceAll("\\.", "/"));
        var processor = SpoonUtils.process(testCycle, path,
                                           new LoopsMethodBodyProcessor(methodName));

        var actual = processor.getForeachLoops().size()
                     + processor.getForLoops().size()
                     + processor.getWhileLoops().size()
                     + processor.getDoWhileLoops().size();
        assertEquals(expected, actual, "Numbers of required loop do not match");
    }

    /* *********************************************************************
     *                          Other helper methods                       *
     **********************************************************************/

    protected static <U> MyLinkedList<U> copyList(MyLinkedList<U> list) {
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
