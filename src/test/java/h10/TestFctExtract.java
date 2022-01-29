package h10;

import java.util.function.Function;

/**
 * Defines the function predicate used for testing purposes (extract methods).
 *
 * @author Nhan Huynh
 */
public class TestFctExtract implements Function<Integer[], Integer> {

    @Override
    public Integer apply(Integer[] integers) {
        int sum = 0;
        for (int i : integers) {
            sum += i;
        }
        return sum;
    }
}
