package h10;

import java.util.function.Function;

/**
 * This function is used to sum up all integers in an array.
 */
public class TestExtractFct implements Function<int[], Integer> {

    /**
     * This method calculates the sum of all ints in an array.
     *
     * @param ints array of which the sum is to be calculated
     * @return sum of all integers in array "ints"
     */
    @Override
    public Integer apply(int[] ints) {
        int sum = 0;

        for (int i : ints) {
            sum += i;
        }

        return sum;
    }
}
