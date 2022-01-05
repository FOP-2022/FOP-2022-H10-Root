package h10;

import java.util.function.Predicate;

public class TestExtractPredT implements Predicate<int[]> {

    @Override
    public boolean test(int[] arr) {
        //check if you can divide one of the array components by at least one other component
        if (arr[0] % arr[1] == 0 || arr[0] % arr[2] == 0 ||
            arr[1] % arr[0] == 0 || arr[1] % arr[2] == 0 ||
            arr[2] % arr[0] == 0 || arr[2] % arr[1] == 0) {
            return true;
        } else {
            return false;
        }
    }
}
