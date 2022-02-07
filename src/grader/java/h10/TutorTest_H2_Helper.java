package h10;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

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

    protected <U> void assertLinkedList(MyLinkedList<U> expected, MyLinkedList<U> actual) {
        // one of the list is null
        if (expected.head == null || actual.head == null) {
            assertEquals(expected.head, actual.head, "Assertion fails with an expected list: " + expected.head
                                                     + ", but was: " + actual.head);
            return;
        }

        ListItem<U> actualCurrent = actual.head;
        for (ListItem<U> expectedCurrent = expected.head; expectedCurrent != null; expectedCurrent = expectedCurrent.next) {
            // actual mustn't end before expected
            assertNotNull(actualCurrent, "Assertion fails with an expected key: "
                                         + expectedCurrent.key
                                         + ", but was: null");

            // if it is an array, check each item
            if (expectedCurrent.key.toString().startsWith("[")) {
                // actual is also an array
                assertTrue(actualCurrent.key.toString().startsWith("["), "Assertion fails with an expected key: "
                                                                         + expectedCurrent.key + ", but was: "
                                                                         + actualCurrent.key);

                var expectedArray = (U[]) expectedCurrent.key;
                var actualArray = (U[]) expectedCurrent.key;
                // make sure both have the same lengths
                assertEquals(expectedArray.length, actualArray.length,
                             "Assertion fails with an expected array length: " + expectedArray.length + ", but was: "
                             + actualArray.length);
                for (int i = 0; i < expectedArray.length; i++) {
                    assertEquals(expectedArray[i], actualArray[i],
                                 "Assertion fails with an expected listElem element: " + expectedArray[i]
                                 + ", but was: " + actualArray[i]);
                }
                actualCurrent = actualCurrent.next;
                continue;
            }

            // if it's not an array
            assertEquals(expectedCurrent.key, actualCurrent.key,
                         "Assertion fails with an expected key: " + expectedCurrent.key + ", but was: "
                         + actualCurrent.key);
            actualCurrent = actualCurrent.next;
        }
        // actual must end when expected does
        assertNull(actualCurrent, "Assertion fails with an expected key: null, but was: " + actualCurrent);
    }

    protected void assertExceptionMessage(MyLinkedListException expected, MyLinkedListException actual) {
        assertNotNull(actual, "Assertion fails with an expected message: " + expected.getMessage()
                              + ", but was: " + actual);

        // ignore the object's name
        int indexObject = expected.getMessage().indexOf('@');
        int maxIndex = (indexObject == -1) ? expected.getMessage().length() : indexObject;
        assertEquals(expected.getMessage().substring(0, maxIndex), actual.getMessage().substring(0, maxIndex),
                     "Assertion fails with an expected message: " + expected.getMessage() + ", but was: "
                     + actual.getMessage());
    }

    /* *********************************************************************
     *                          Other helper methods                       *
     **********************************************************************/

    protected <U> MyLinkedList<U> copyList(MyLinkedList<U> list) {
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
