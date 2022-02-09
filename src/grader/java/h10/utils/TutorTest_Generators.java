package h10.utils;

import h10.MyLinkedList;
import org.mockito.Mockito;

import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.Math.max;

/**
 * Define two types of functions, predicates, and list generators for extract and mixin (H2).
 * The first type is defined by T = Integer and U = Integer[].
 * The second type is defined by T = String and U = Double.
 * The implementation for each function/predicate/method is randomly chosen and (should) cover all input possibilities.
 *
 * @author Arianne Roselina Prananto
 */
public final class TutorTest_Generators {

    /* *********************************************************************
     *                      Define some fct functions                      *
     **********************************************************************/

    // listElem from 0 to the given Integer
    public static Function<Integer, Integer[]> fctExtract1 = i -> {
        Integer[] ints = new Integer[i];
        for (int j = 0; j < i; j++) {
            ints[j] = j;
        }
        return ints;
    };

    // string to double
    public static Function<String, Double> fctExtract2 = Double::valueOf;

    // max number
    public static Function<Integer[], Integer> fctMixin1 = ints -> {
        Integer max = ints[0];
        for (int i = 1; i < ints.length; i++) {
            max = max(ints[i], max);
        }
        return max;
    };

    // listElem of each character in the String
    public static Function<Double, String> fctMixin2 = Object::toString;

    /* *********************************************************************
     *                     Define some predT predicates                    *
     **********************************************************************/

    // is an even number
    public static Predicate<Integer> predT1 = i -> i % 2 == 0;

    // first character is 1
    public static Predicate<String> predT2 = s -> s.charAt(0) == '1';

    /* *********************************************************************
     *                     Define some predU predicates                    *
     **********************************************************************/

    // has at least three elements greater than 10
    public static Predicate<Integer[]> predU1 = i -> {
        int count = 0;
        for (Integer integer : i) {
            if (integer > 10) {
                count++;
            }
        }
        return count >= 3;
    };

    // is a multiple of 3
    public static Predicate<Double> predU2 = d -> d % 3 == 0;

    /* *********************************************************************
     *                      Define some biPred predicates                  *
     **********************************************************************/

    // have the same values
    public static BiPredicate<Integer, Integer[]> biPred1 = (i, a) -> i > a.length;

    // have the same values
    public static BiPredicate<String, Double> biPred2 = (s, d) -> Double.parseDouble(s) == d;

    /* *********************************************************************
     *                   Define lists generators for extract               *
     **********************************************************************/

    /**
     * Generate random lists with type Integer for extract*-methods, so that it does not throw any exception.
     * @return the generated lists
     */
    public static MyLinkedList<Integer>[] generateThisListExtract1WithoutExc() {
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

    /**
     * Generate random lists with type Integer for extract*-methods, so that it throws exception.
     * @return the generated lists
     */
    public static MyLinkedList<Integer>[] generateThisListExtract1WithExc() {
        @SuppressWarnings("unchecked")
        MyLinkedList<Integer>[] sourceLists = new MyLinkedList[3];

        // make sure there are no 3 elements greater than 10 after all operations
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.add(0);
        sourceLists[0] = list; // {0 -> null}

        list = new MyLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(0);
        }
        sourceLists[1] = list; // {0 -> 0 -> ... -> null}

        list = new MyLinkedList<>();
        for (int j = 0; j < 10; j++) {
            list.add(j);
        }
        sourceLists[2] = list;
        return sourceLists;
    }

    /**
     * Generate random lists with type String for extract*-methods, so that it does not throw any exception.
     * @return the generated lists
     */
    public static MyLinkedList<String>[] generateThisListExtract2WithoutExc() {
        @SuppressWarnings("unchecked")
        MyLinkedList<String>[] sourceLists = new MyLinkedList[10];

        for (int i = 0; i < 10; i++) {
            MyLinkedList<String> list = new MyLinkedList<>();
            for (int j = 0; j < 10; j++) {
                // make sure there are some strings that start with 1
                String str = "0";
                if (j % 2 == 0) {
                    str = "1";
                }
                str += Integer.toString(new Random().nextInt(1000));

                // make sure it is a multiple of 3
                list.add((Double.parseDouble(str) % 3 != 0)
                             ? String.valueOf(Double.parseDouble(str) * 3) : str);
            }
            sourceLists[i] = list;
        }
        return sourceLists;
    }

    /**
     * Generate random lists with type String for extract*-methods, so that it throws exception.
     * @return the generated lists
     */
    public static MyLinkedList<String>[] generateThisListExtract2WithExc() {
        @SuppressWarnings("unchecked")
        MyLinkedList<String>[] sourceLists = new MyLinkedList[10];

        for (int i = 0; i < 10; i++) {
            MyLinkedList<String> list = new MyLinkedList<>();
            for (int j = 0; j < 10; j++) {
                // make sure there are some strings that start with 1
                String str = "0";
                if (j % 2 == 0) {
                    str = "1";
                }

                // make sure there are some elements that are not multiples of 3
                var rn = new Random().nextInt(1000);
                str += Integer.toString((j % 4 == 0 && rn % 3 == 0) ? ++rn : rn);
                list.add(str);
            }
            sourceLists[i] = list;
        }
        return sourceLists;
    }

    /**
     * Generate a random list with type Integer as an input while spying extractRecursively, so that it does not throw
     * any exception.
     * @return the generated list
     */
    public static MyLinkedList<Integer> generateThisListExtractMockito() {
        @SuppressWarnings("unchecked")
        MyLinkedList<Integer> list = Mockito.spy(MyLinkedList.class);
        for (int j = 0; j < 10; j++) {
            list.add(new Random().nextInt(100) + 10);
        }
        return list;
    }

    /* *********************************************************************
     *                    Define lists generators for mixin                *
     **********************************************************************/

    /**
     * Generate random lists with type Integer as thisList for mixin*-methods, so that it does not throw any exception.
     * @return the generated lists
     */
    public static MyLinkedList<Integer>[] generateThisListMixin1() {
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

    /**
     * Generate random lists with type String as thisList for mixin*-methods, so that it does not throw any exception.
     * @return the generated lists
     */
    public static MyLinkedList<String>[] generateThisListMixin2() {
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

    /**
     * Generate random lists with type Integer[] as otherList for mixin*-methods, so that it does not throw any exception.
     * @return the generated lists
     */
    public static MyLinkedList<Integer[]>[] generateOtherListMixin1WithoutExc() {
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
                    if (k % 3 == 0) {
                        listElem[k] += 11;
                    }
                }
                list.add(listElem);
            }
            sourceLists[i] = list;
        }
        return sourceLists;
    }

    /**
     * Generate random lists with type Integer[] as otherList for mixin*-methods, so that it throws exception.
     * @return the generated lists
     */
    public static MyLinkedList<Integer[]>[] generateOtherListMixin1WithExc() {
        @SuppressWarnings("unchecked")
        MyLinkedList<Integer[]>[] sourceLists = new MyLinkedList[10];

        // make sure there are no 3 elements greater than 10 after all operations
        MyLinkedList<Integer[]> list = new MyLinkedList<>();
        list.add(new Integer[]{0, 0, 0});
        sourceLists[0] = list; // {{0,0,0} -> null}

        list = new MyLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Integer[]{0, 0, 0});
        }
        sourceLists[1] = list; // {{0,0,0} -> {0,0,0} -> ... -> null}

        for (int i = 2; i < 10; i++) {
            list = new MyLinkedList<>();
            for (int j = 0; j < 10; j++) {
                Integer[] listElem = new Integer[10];
                for (int k = 0; k < 10; k++) {
                    listElem[k] = new Random().nextInt(10);
                }
                list.add(listElem);
            }
            sourceLists[i] = list;
        }
        return sourceLists;
    }

    /**
     * Generate random lists with type Double as otherList for mixin*-methods, so that it does not throw any exception.
     * @return the generated lists
     */
    public static MyLinkedList<Double>[] generateOtherListMixin2WithoutExc() {
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

    /**
     * Generate random lists with type Double as otherList for mixin*-methods, so that it throws exception.
     * @return the generated lists
     */
    public static MyLinkedList<Double>[] generateOtherListMixin2WithExc() {
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

    /**
     * Generate a random list with type Integer as an input for thisList while spying extractRecursively, so that it
     * does not throw any exception.
     * @return the generated list
     */
    public static MyLinkedList<Integer> generateThisListMixinMockito() {
        @SuppressWarnings("unchecked")
        MyLinkedList<Integer> list = Mockito.spy(MyLinkedList.class);
        for (int j = 0; j < 10; j++) {
            list.add(new Random().nextInt(100));
        }
        return list;
    }

    /**
     * Generate a random list with type Integer[] as an input for otherList while spying extractRecursively, so that it
     * does not throw any exception.
     * @return the generated list
     */
    public static MyLinkedList<Integer[]> generateOtherListMixinMockito() {
        @SuppressWarnings("unchecked")
        MyLinkedList<Integer[]> list = Mockito.spy(MyLinkedList.class);
        for (int j = 0; j < 10; j++) {
            Integer[] listElem = new Integer[10];
            for (int k = 0; k < 10; k++) {
                listElem[k] = new Random().nextInt(100);
                if (k % 3 == 0) {
                    listElem[k] += 11;
                }
            }
            list.add(listElem);
        }
        return list;
    }
}
