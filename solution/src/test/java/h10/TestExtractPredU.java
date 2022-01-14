package h10;

import java.util.function.Predicate;

/**
 * This predicate checks whether an in is non-negative.
 */
public class TestExtractPredU implements Predicate<Integer> {

    /**
     * This method tests if an integer is non-negative.
     *
     * @param integer Integer that is to be tested
     * @return true, if integer is non-negative
     */
    @Override
    public boolean test(Integer integer) {
        if (integer < 0) {
            return false;
        } else {
            return true;
        }
    }
}
