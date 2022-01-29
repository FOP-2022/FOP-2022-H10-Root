package h10;

import java.util.function.Predicate;

/**
 * Defines the test predicate used for testing purposes (extract methods).
 *
 * @author Nhan Huynh
 */
public class TestPredTExtract implements Predicate<Integer[]> {

    @Override
    public boolean test(Integer[] integers) {
        for (int i = 0; i < integers.length; i++) {
            for (int j = 0; j < integers.length; j++) {
                if (i != j && integers[i] % integers[j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
