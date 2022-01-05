package h10;

import java.util.function.Predicate;

public class TestExtractPredU implements Predicate<Integer> {

    @Override
    public boolean test(Integer integer) {
        if (integer < 0) {
            return false;
        } else {
            return true;
        }
    }
}
