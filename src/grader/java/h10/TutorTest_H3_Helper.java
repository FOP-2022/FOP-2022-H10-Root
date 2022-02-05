package h10;

import java.util.Random;

/**
 * Define some helper methods for task H3.
 *
 * @author Arianne Roselina Prananto
 */
public final class TutorTest_H3_Helper {

    /* *********************************************************************
     *                       Define some generators                        *
     **********************************************************************/

    protected Number[] generateManyNumbers(int numElems) {
        Number[] ints = new Number[numElems];
        for (int i = 0; i < numElems; i++) ints[i] = new Random().nextInt();
        return ints;
    }

    protected Integer[][] generateManyIntegerArrays(int numElems, int numArrays) {
        Integer[][] intArrays = new Integer[numArrays][numElems];
        for (int i = 0; i < numArrays; i++) {
            for (int j = 0; j < numElems; j++) {
                intArrays[i][j] = new Random().nextInt();
            }
        }
        return intArrays;
    }

    protected String[] generateManyStrings(int numElems) {
        String[] strings = new String[numElems];
        for (int i = 0; i < numElems; i++) {
            if (i % 2 == 0) strings[i] = String.valueOf(new Random().nextInt());
            else strings[i] = "hello";
        }
        return strings;
    }

    /* *********************************************************************
     *               Define expected functions and predicates              *
     **********************************************************************/

    protected Integer expectedFct(Integer[] array) {
        Integer result = 0;
        for (Integer a : array) result += a;
        return result;
    }

    protected boolean expectedPredT(Integer[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                if (i != j && array[i] % array[j] == 0) return true;
            }
        }
        return false;
    }

    public boolean expectedPredU(Integer integer) {
        return integer >= 0;
    }
}
