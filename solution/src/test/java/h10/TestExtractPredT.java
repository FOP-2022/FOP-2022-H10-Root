package h10;

import java.util.function.Predicate;

/**
 * This predicate is used to check whether at least one component is divisible by at least one other component in an array of 3.
 */
public class TestExtractPredT implements Predicate<int[]> {

    /**
     * This method tests if at least one component of an array with 3 integers is divisible by at least one other component.
     *
     * @param arr array that is to be checked
     * @return true, if at least one component of the given array is divisible by at least one other component
     */
    @Override
    public boolean test(int[] arr) {
        //check if you can divide one of the array components by at least one other component
        if (arr[0] % arr[1] == 0 || arr[0] % arr[2] == 0
            || arr[1] % arr[0] == 0 || arr[1] % arr[2] == 0
            || arr[2] % arr[0] == 0 || arr[2] % arr[1] == 0) {
            return true;
        } else {
            return false;
        }
    }
}
