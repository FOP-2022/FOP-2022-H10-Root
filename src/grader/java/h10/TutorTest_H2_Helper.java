package h10;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.Math.max;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Define some helper attributes and methods for task H2.
 *
 * @author Arianne Roselina Prananto
 */
public final class TutorTest_H2_Helper<T> {

    /* *********************************************************************
     *                      Define some fct functions                      *
     **********************************************************************/

    // listElem from 0 to the given Integer
    protected Function<Integer, Integer[]> fctExtract1 = i -> {
        Integer[] ints = new Integer[i];
        for (int j = 0; j < i; j++) ints[j] = j;
        return ints;
    };

    // string to double
    protected Function<String, Double> fctExtract2 = s -> Double.valueOf(s);

    // max number
    protected Function<Integer[], Integer> fctMixin1 = ints -> {
        Integer max = ints[0];
        for (int i = 1; i < ints.length; i++) max = max(ints[i], max);
        return max;
    };

    // listElem of each character in the String
    protected Function<Double, String> fctMixin2 = d -> d.toString();

    /* *********************************************************************
     *                     Define some predT predicates                    *
     **********************************************************************/

    // is an even number
    protected Predicate<Integer> predT1 = i -> i % 2 == 0;

    // first character is 1
    protected Predicate<String> predT2 = s -> s.charAt(0) == '1';

    /* *********************************************************************
     *                     Define some predU predicates                    *
     **********************************************************************/

    // has at least three elements greater than 10
    protected Predicate<Integer[]> predU1 = i -> {
        int count = 0;
        for (Integer integer : i) {
            if (integer > 10) count++;
        }
        return count >= 3;
    };

    // is a multiple of 3
    protected Predicate<Double> predU2 = d -> d % 3 == 0;

    /* *********************************************************************
     *                     Define some biPred predicates                   *
     **********************************************************************/

    // have the same values
    protected BiPredicate<Integer, Integer[]> biPred1 = (i, a) -> i > a.length;

    // have the same values
    protected BiPredicate<String, Double> biPred2 = (s, d) -> Double.parseDouble(s) == d;

    /* *********************************************************************
     *                   Define lists generator for extract                *
     **********************************************************************/

    protected MyLinkedList<Integer>[] generateThisListExtract1WithoutExc() {
        @SuppressWarnings("unchecked")
        MyLinkedList<Integer>[] sourceLists = new MyLinkedList[10];
        sourceLists[0] = new MyLinkedList<>(); // null list

        for (int i = 1; i < 10; i++) {
            MyLinkedList<Integer> list = new MyLinkedList<>();
            for (int j = 0; j < 10; j++) {
                // make sure there will be at least 3 elements greater than 10 after all operations despite the choice
                // of numElems (12, 14, 16)
                list.add(j + 16);
            }
            sourceLists[i] = list;
        }
        return sourceLists;
    }

    protected MyLinkedList<Integer>[] generateThisListExtract1WithExc() {
        @SuppressWarnings("unchecked")
        MyLinkedList<Integer>[] sourceLists = new MyLinkedList[3];

        // make sure there are no 3 elements greater than 10 after all operations
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.add(0);
        sourceLists[0] = list; // {0 -> null}

        list = new MyLinkedList<>();
        for (int i = 0; i < 10; i++) list.add(0);
        sourceLists[1] = list; // {0 -> 0 -> ... -> null}

        list = new MyLinkedList<>();
        for (int j = 0; j < 10; j++) {
            list.add(j);
        }
        sourceLists[2] = list;
        return sourceLists;
    }

    protected MyLinkedList<String>[] generateThisListExtract2WithoutExc() {
        @SuppressWarnings("unchecked")
        MyLinkedList<String>[] sourceLists = new MyLinkedList[10];

        for (int i = 0; i < 10; i++) {
            MyLinkedList<String> list = new MyLinkedList<>();
            for (int j = 0; j < 10; j++) {
                // make sure there are some strings that start with 1
                String str = "0";
                if (j % 2 == 0) str = "1";
                str += Integer.toString(new Random().nextInt(1000));

                // make sure it is a multiple of 3
                list.add((Double.parseDouble(str) % 3 != 0) ?
                             String.valueOf(Double.parseDouble(str) * 3) : str);
            }
            sourceLists[i] = list;
        }
        return sourceLists;
    }

    protected MyLinkedList<String>[] generateThisListExtract2WithExc() {
        @SuppressWarnings("unchecked")
        MyLinkedList<String>[] sourceLists = new MyLinkedList[10];

        for (int i = 0; i < 10; i++) {
            MyLinkedList<String> list = new MyLinkedList<>();
            for (int j = 0; j < 10; j++) {
                // make sure there are some strings that start with 1
                String str = "0";
                if (j % 2 == 0) str = "1";

                // make sure there are some elements that are not multiples of 3
                var rn = new Random().nextInt(1000);
                str += Integer.toString((j % 4 == 0 && rn % 3 == 0) ? ++rn : rn);
                list.add(str);
            }
            sourceLists[i] = list;
        }
        return sourceLists;
    }

    /* *********************************************************************
     *                    Define lists generator for mixin                 *
     **********************************************************************/

    protected MyLinkedList<Integer>[] generateThisListMixin1() {
        @SuppressWarnings("unchecked")
        MyLinkedList<Integer>[] targetLists = new MyLinkedList[10];
        targetLists[0] = new MyLinkedList<>(); // null list

        for (int i = 1; i < 10; i++) {
            MyLinkedList<Integer> list = new MyLinkedList<>();
            for (int j = 0; j < 10; j++) {
                list.add(new Random().nextInt(100));
            }
            targetLists[i] = list;
        }
        return targetLists;
    }

    protected MyLinkedList<String>[] generateThisListMixin2() {
        @SuppressWarnings("unchecked")
        MyLinkedList<String>[] targetLists = new MyLinkedList[10];
        targetLists[0] = new MyLinkedList<>(); // null list

        for (int i = 1; i < 10; i++) {
            MyLinkedList<String> list = new MyLinkedList<>();
            for (int j = 0; j < 10; j++) {
                list.add(String.valueOf(new Random().nextDouble() * 1000));
            }
            targetLists[i] = list;
        }
        return targetLists;
    }

    protected MyLinkedList<Integer[]>[] generateOtherListMixin1WithoutExc() {
        @SuppressWarnings("unchecked")
        MyLinkedList<Integer[]>[] sourceLists = new MyLinkedList[10];
        sourceLists[0] = new MyLinkedList<>(); // null list

        for (int i = 1; i < 10; i++) {
            MyLinkedList<Integer[]> list = new MyLinkedList<>();
            for (int j = 0; j < 10; j++) {
                Integer[] listElem = new Integer[10];
                for (int k = 0; k < 10; k++) {
                    // make sure there will be at least 3 elements greater than 10 after all operations
                    listElem[k] = new Random().nextInt(100);
                    if (k % 3 == 0) listElem[k] += 11;
                }
                list.add(listElem);
            }
            sourceLists[i] = list;
        }
        return sourceLists;
    }

    protected MyLinkedList<Integer[]>[] generateOtherListMixin1WithExc() {
        @SuppressWarnings("unchecked")
        MyLinkedList<Integer[]>[] sourceLists = new MyLinkedList[10];

        // make sure there are no 3 elements greater than 10 after all operations
        MyLinkedList<Integer[]> list = new MyLinkedList<>();
        list.add(new Integer[]{0, 0, 0});
        sourceLists[0] = list; // {{0,0,0} -> null}

        list = new MyLinkedList<>();
        for (int i = 0; i < 10; i++) list.add(new Integer[]{0, 0, 0});
        sourceLists[1] = list; // {{0,0,0} -> {0,0,0} -> ... -> null}

        for (int i = 2; i < 10; i++) {
            list = new MyLinkedList<>();
            for (int j = 0; j < 10; j++) {
                Integer[] listElem = new Integer[10];
                for (int k = 0; k < 10; k++) listElem[k] = new Random().nextInt(10);
                list.add(listElem);
            }
            sourceLists[i] = list;
        }
        return sourceLists;
    }

    protected MyLinkedList<Double>[] generateOtherListMixin2WithoutExc() {
        @SuppressWarnings("unchecked")
        MyLinkedList<Double>[] sourceLists = new MyLinkedList[10];

        for (int i = 0; i < 10; i++) {
            MyLinkedList<Double> list = new MyLinkedList<>();
            for (int j = 0; j < 10; j++) {
                // make sure it is a multiple of 3
                list.add((double) new Random().nextInt(1000) * 3);
            }
            sourceLists[i] = list;
        }
        return sourceLists;
    }

    protected MyLinkedList<Double>[] generateOtherListMixin2WithExc() {
        @SuppressWarnings("unchecked")
        MyLinkedList<Double>[] sourceLists = new MyLinkedList[10];

        for (int i = 0; i < 10; i++) {
            MyLinkedList<Double> list = new MyLinkedList<>();
            for (int j = 0; j < 10; j++) {
                // make sure there are some elements that are not multiples of 3
                var rn = (double) new Random().nextInt(1000);
                list.add((j % 4 == 0 && rn % 3 == 0) ? ++rn : rn);
            }
            sourceLists[i] = list;
        }
        return sourceLists;
    }

    /* *********************************************************************
     *                         Other helper methods                        *
     **********************************************************************/

    public <U> MyLinkedList<U> expectedExtract(MyLinkedList<T> sourceList, Predicate<? super T> predT,
                                               Function<? super T, ? extends U> fct,
                                               Predicate<? super U> predU) throws MyLinkedListException {
        MyLinkedList<U> removed = new MyLinkedList<>();
        ListItem<U> tail = null;
        if (sourceList.head == null) return removed;

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
            if (!predU.test(mapped)) throw new MyLinkedListException(index, mapped);

            current.next = current.next.next;

            ListItem<U> item = new ListItem<>(mapped);
            if (tail == null) removed.head = tail = item;
            else tail = tail.next = item;

            index++;
        }

        sourceList.head = result.next;
        return removed;
    }

    protected <U> void expectedMixin(MyLinkedList<T> sourceList, MyLinkedList<U> otherList,
                                     BiPredicate<? super T, ? super U> biPred,
                                     Function<? super U, ? extends T> fct,
                                     Predicate<? super U> predU) throws MyLinkedListException {
        if (otherList.head == null) return;

        ListItem<U> others = otherList.head;
        ListItem<T> current = new ListItem<>();
        current.next = sourceList.head;
        ListItem<T> result = current;
        int index = 0;

        while (others != null) {
            U key = others.key;
            System.out.println(others.key.getClass());
            if (!predU.test((U) key)) {
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
            assertNotNull(actualCurrent, "Assertion fails with an expected key: " + expectedCurrent.key
                                         + ", but was: null");

            // if it is an arraay, check each item
            if (expectedCurrent.key.toString().startsWith("[")) {
                // actual is also an array
                assertTrue(actualCurrent.key.toString().startsWith("["), "Assertion fails with an expected key: "
                                                                         + expectedCurrent.key + ", but was: "
                                                                         + actualCurrent.key);

                var expectedArray = (U[]) expectedCurrent.key;
                var actualArray = (U[]) expectedCurrent.key;
                // make sure both have the same lengths
                assertEquals(expectedArray.length, actualArray.length, "Assertion fails with an expected arrax length: " +
                                                                       +expectedArray.length + ", but was: "
                                                                       + actualArray.length);
                for (int i = 0; i < expectedArray.length; i++) {
                    assertEquals(expectedArray[i], actualArray[i],
                                 "Assertion fails with an expected listElem element: " + expectedArray[i] + ", but was: "
                                 + actualArray[i]);
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

    protected <T> MyLinkedList<T> copyList(MyLinkedList<T> list) {
        MyLinkedList<T> copyList = new MyLinkedList<>();
        if (list.head == null) {
            copyList.head = null;
            return copyList;
        }

        copyList.head = new ListItem<>(list.head.key);
        ListItem<T> current, currentCopy = copyList.head;
        for (current = list.head.next; current != null; current = current.next) {
            currentCopy.next = new ListItem<>(current.key);
            currentCopy = currentCopy.next;
        }
        return copyList;
    }
}
