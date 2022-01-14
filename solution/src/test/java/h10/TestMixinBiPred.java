package h10;

import java.util.function.BiPredicate;

/**
 * This predicate checks whether the second parameter is smaller than the first parameter.
 */
public class TestMixinBiPred implements BiPredicate<Number, String> {
    /**
     * This method tests if the Double representation of parameter "s" is smaller than the Double value of "number".
     *
     * @param number Number that is to be compared
     * @param s String that is to be converted to Double and compared
     * @return true, if Double representation of parameter s is smaller than Double value of parameter number
     */
    @Override
    public boolean test(Number number, String s) {
        return Double.valueOf(s) < number.doubleValue();
    }
}
