package h10;

import java.util.function.BiPredicate;

public class TestMixinBiPred implements BiPredicate<Number, String> {
    @Override
    public boolean test(Number number, String s) {
        return Double.valueOf(s) < number.doubleValue();
    }
}
