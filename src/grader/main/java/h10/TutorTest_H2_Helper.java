package h10;

import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

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

    // array from 0 to the given Integer
    protected Function<Integer, Integer[]> fct1 = i -> {
        Integer[] ints = new Integer[i];
        for (int j = 0; j <= i; j++) ints[j] = j;
        return ints;
    };

    // array of each character in the String
    protected Function<String, Integer[]> fct2 = s -> {
        Integer[] ints = new Integer[s.length()];
        for (int i = 0; i < s.length(); i++) {
            ints[i] = (int) s.charAt(i);
        }
        return ints;
    };

    // round down all elements to integer
    protected Function<Object[], Double> fct3 = o -> {
        Double sum = 0.0;
        for (Object value : o) {
            sum += ((Number) value).doubleValue();
        }
        return sum;
    };

    /* *********************************************************************
     *                     Define some predT predicates                    *
     **********************************************************************/

    // is an even number
    protected Predicate<Integer> predT1 = i -> i % 2 == 0;

    // first character is 1
    protected Predicate<String> predT2 = s -> s.charAt(0) == '1';

    // all elements are instance of double
    protected Predicate<Object[]> predT3 = o -> {
        for (Object eachO : o) {
            if (!(eachO instanceof Double)) return false;
        }
        return true;
    };

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

    // has at least one element that shows up at least twice
    protected Predicate<Integer[]> predU2 = i -> {
        for (int k = 0; k < i.length; k++) {
            for (int j = k + 1; j < i.length; j++) {
                if (i[k].equals(i[j])) return true;
            }
        }
        return false;
    };

    // has at least 4 digits (x.xxx)
    protected Predicate<Double> predU3 = d -> d.toString().length() >= 5;

    /* *********************************************************************
     *                     Define some biPred predicates                   *
     **********************************************************************/

    // have the same values
    protected BiPredicate<Number, String> biPred1 = (n, s) -> n.doubleValue() == Double.parseDouble(s);

    /* *********************************************************************
     *                        Define lists generator                       *
     **********************************************************************/

    protected MyLinkedList<Integer>[] generateMyLinkedList1WithoutExc(int numElems, int numArrays) {
        @SuppressWarnings("unchecked")
        MyLinkedList<Integer>[] sourceLists = new MyLinkedList[numArrays];
        sourceLists[0] = new MyLinkedList<>(); // null list

        for (int i = 1; i < numArrays; i++) {
            MyLinkedList<Integer> list = new MyLinkedList<>();
            for (int j = 0; j < numElems; j++) {
                // make sure there will be at least 3 elements greater than 10 after all operations despite the choice
                // of numElems (12, 14, 16)
                list.add(j + 16);
            }
            sourceLists[i] = list;
        }
        return sourceLists;
    }

    protected MyLinkedList<Integer>[] generateMyLinkedList1WithExc() {
        @SuppressWarnings("unchecked")
        MyLinkedList<Integer>[] sourceLists = new MyLinkedList[2];

        // make sure there are no 3 elements greater than 10 after all operations
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.add(0);
        sourceLists[0] = list; // {0 -> null}

        list = new MyLinkedList<>();
        for (int j = 0; j < 10; j++) {
            list.add(j);
        }
        sourceLists[1] = list;
        return sourceLists;
    }

    protected MyLinkedList<String>[] generateMyLinkedList2WithoutExc(int numElems, int numArrays) {
        @SuppressWarnings("unchecked")
        MyLinkedList<String>[] sourceLists = new MyLinkedList[numArrays];

        for (int i = 0; i < numArrays; i++) {
            MyLinkedList<String> list = new MyLinkedList<>();
            for (int j = 0; j < numElems; j++) {
                // make sure there are some strings that start with 1
                String str = "0";
                if (j % 2 == 0) str = "1";

                for (int k = 1; k < 9; k++) {
                    str += Integer.toString(new Random().nextInt(10));
                }
                // make sure there will be at least one element that shows up at least twice
                int rn = new Random().nextInt(9);
                str += str.charAt(rn);

                list.add(str);
            }
            sourceLists[i] = list;
        }
        return sourceLists;
    }

    protected MyLinkedList<String>[] generateMyLinkedList2WithExc() {
        @SuppressWarnings("unchecked")
        MyLinkedList<String>[] sourceLists = new MyLinkedList[1];
        MyLinkedList<String> list = new MyLinkedList<>();

        for (int j = 0; j < 5; j++) {
            // make sure there are some strings that start with 1
            String str = "0";
            if (j % 2 == 0) str = "1";

            // make sure there is no element that shows up at least twice
            for (int k = 2; k < 5; k++) {
                str += Integer.toString(k);
            }
            list.add(str);
        }
        sourceLists[0] = list;
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

    protected <U> void assertLinkedList(MyLinkedList<U> expected, MyLinkedList<U> actual) {
        ListItem<U> actualCurrent = actual.head;
        for (ListItem<U> expectedCurrent = expected.head; expectedCurrent != null; expectedCurrent = expectedCurrent.next) {
            assertEquals(expectedCurrent.key, actualCurrent.key,
                         "Assertion fails with an expected key: " + expectedCurrent.key + ", but was: "
                         + actualCurrent.key);
            actualCurrent = actualCurrent.next;
        }
        assertNull(actualCurrent, "Assertion fails with an expected key: null, but was: " + actualCurrent.key);
    }

    protected void assertExceptionMessage(MyLinkedListException expected, MyLinkedListException actual) {
        assertEquals(expected.getMessage(), actual.getMessage(),
                     "Assertion fails with an expected message: " + expected.getMessage() + ", but was: "
                     + actual);
    }
}
