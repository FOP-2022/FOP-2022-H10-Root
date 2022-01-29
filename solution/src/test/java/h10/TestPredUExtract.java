package h10;

import java.util.function.Predicate;

/**
 * Defines the test predicate used for testing purposes (extract methods).
 *
 * @author Nhan Huynh
 */
public class TestPredUExtract implements Predicate<Integer> {

    @Override
    public boolean test(Integer integer) {
        return integer >= 0;
    }
}
