package h10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Defines the test cases for the class {@link MyLinkedList}.
 *
 * @author Nhan Huynh
 */
public class TestMyLinkedList {


    /**
     * Defines the test bi predicate used for testing purposes (mixin methods).
     */
    private static final BiPredicate<Number, String> BI_PRED_MIXIN = (number, s) -> number.doubleValue() > Double.parseDouble(s);
    /**
     * Defines the test predicate used for testing purposes (mixin methods).
     */
    private static final Predicate<String> PRED_U_MIXIN = s -> {
        try {
            Double.valueOf(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    };
    /**
     * Defines the test function used for testing purposes (mixin methods).
     */
    private static final Function<String, Number> FCT_MIXIN = Double::valueOf;

    /**
     * Checks whether the list contains the expected elements.
     *
     * @param expected  the expected elements
     * @param actual    the list containing the actual elements
     * @param assertion the criterion how the elements will be checked
     * @param <T>       the type of the elements
     */
    private static <T> void assertLinkedList(T[] expected, MyLinkedList<T> actual, BiConsumer<T, T> assertion) {
        int index = 0;
        for (ListItem<T> current = actual.head; current != null; current = current.next) {
            assertion.accept(expected[index], current.key);
            index++;
        }
    }

    /**
     * Generates a test list for the extract test cases.
     *
     * @param swap the indicator if the order of the elements should be mixed (swap i with i+1)
     *
     * @return the generated test list
     */
    private static MyLinkedList<Integer[]> generateTestListExtract(boolean swap) {
        MyLinkedList<Integer[]> list = new MyLinkedList<>();
        Integer[][] values = {
            {1, 5, 10},
            {2, 3, 5},
            {10, 100, 1000},
            {5, 6, 7},
            {10, 15, 20},
            {7, 8, 9}
        };
        for (int i = 0; i < values.length; i++) {
            if (swap) {
                if (i + 1 >= values.length) {
                    break;
                }
                list.add(values[i + 1]);
                list.add(values[i]);
                i++;
            } else {
                list.add(values[i]);
            }
        }
        return list;
    }

    /**
     * Generates a test target list for the mixin test cases.
     *
     * @param decrement the indicator if the array elements should be decremented by 1
     *
     * @return the generated test list
     */
    private static MyLinkedList<Number> generateTestListTarget(boolean decrement) {
        MyLinkedList<Number> list = new MyLinkedList<>();
        Double[] values = {1.0, 3.0, 5.0, 7.0};
        for (double value : values) {
            if (decrement) {
                list.add(value - 1);
            } else {
                list.add(value);
            }
        }
        return list;
    }

    /**
     * Generates a test target list for the mixin test cases.
     *
     * @param increment the indicator if the array elements should be incremented by 1
     *
     * @return the generated test list
     */
    private static MyLinkedList<String> generateTestListSource(boolean increment) {
        MyLinkedList<String> list = new MyLinkedList<>();
        Double[] values = {0.0, 2.0, 4.0, 6.0};
        for (double value : values) {
            if (increment) {
                list.add(String.valueOf(value + 1));
            } else {
                list.add(String.valueOf(value));
            }
        }
        return list;
    }

    /**
     * Defines the test cases extract the methods.
     *
     * @see MyLinkedList#extractIteratively(Predicate, Function, Predicate)
     * @see MyLinkedList#extractRecursively(Predicate, Function, Predicate)
     */
    @Test
    public void testExtract() {
        Predicate<Integer[]> predT = new TestPredTExtract();
        Function<Integer[], Integer> fct = new TestFctExtract();
        Predicate<Integer> predU = new TestPredUExtract();

        Integer[][] expectedSource = {
            {2, 3, 5},
            {5, 6, 7},
            {7, 8, 9}
        };
        Integer[] expectedDestination = {16, 1110, 45};

        // Test 1 + 2
        for (int i = 0; i < 2; i++) {
            // Change to iterative/recursive
            for (int j = 0; j < 2; j++) {
                try {
                    MyLinkedList<Integer[]> actualSource = generateTestListExtract(j != 0);
                    MyLinkedList<Integer> actualDestination;
                    if (i == 0) {
                        actualDestination = actualSource.extractIteratively(predT, fct, predU);
                    } else {
                        actualDestination = actualSource.extractRecursively(predT, fct, predU);
                    }
                    assertLinkedList(expectedSource, actualSource, Assertions::assertArrayEquals);
                    assertLinkedList(expectedDestination, actualDestination, Assertions::assertEquals);
                } catch (MyLinkedListException e) {
                    Assertions.fail(e.getMessage());
                }
            }
        }

        // Test 3: Change to iterative/recursive
        for (int i = 0; i < 2; i++) {
            MyLinkedList<Integer[]> actualSource = generateTestListExtract(false);
            actualSource.add(new Integer[]{-1, -2, -3});
            Executable executable;
            if (i == 0) {
                executable = () -> actualSource.extractIteratively(predT, fct, predU);
            } else {
                executable = () -> actualSource.extractRecursively(predT, fct, predU);
            }
            Throwable throwable = Assertions.assertThrows(MyLinkedListException.class, executable);
            Assertions.assertEquals("(6,-6)", throwable.getMessage());
        }
    }

    /**
     * Defines the test cases extract the methods.
     *
     * @see MyLinkedList#mixinIteratively(MyLinkedList, BiPredicate, Function, Predicate)
     * @see MyLinkedList#mixinRecursively(MyLinkedList, BiPredicate, Function, Predicate)
     */
    @Test
    public void testMixin() {
        Number[] expected = new Number[8];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = (double) i;
        }

        // Test 1 + 2
        for (int i = 0; i < 2; i++) {
            // Change to iterative/recursive
            for (int j = 0; j < 2; j++) {
                try {
                    MyLinkedList<String> actualSource = generateTestListSource(j != 0);
                    MyLinkedList<Number> actualDestination = generateTestListTarget(j != 0);
                    if (i == 0) {
                        actualDestination.mixinIteratively(actualSource, BI_PRED_MIXIN, FCT_MIXIN, PRED_U_MIXIN);
                    } else {
                        actualDestination.mixinRecursively(actualSource, BI_PRED_MIXIN, FCT_MIXIN, PRED_U_MIXIN);
                    }
                    assertLinkedList(expected, actualDestination, Assertions::assertEquals);
                } catch (MyLinkedListException e) {
                    Assertions.fail(e.getMessage());
                }
            }
        }


        // Test 3: Change to iterative/recursive
        for (int i = 0; i < 2; i++) {
            MyLinkedList<String> actualSource = generateTestListSource(false);
            MyLinkedList<Number> actualDestination = generateTestListTarget(false);
            actualSource.add("Exception");
            Executable executable;
            if (i == 0) {
                executable = () -> actualDestination.mixinIteratively(actualSource, BI_PRED_MIXIN, FCT_MIXIN, PRED_U_MIXIN);
            } else {
                executable = () -> actualDestination.mixinRecursively(actualSource, BI_PRED_MIXIN, FCT_MIXIN, PRED_U_MIXIN);
            }
            Throwable throwable = Assertions.assertThrows(MyLinkedListException.class, executable);
            Assertions.assertEquals("(4,Exception)", throwable.getMessage());
        }
    }
}
