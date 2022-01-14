package h10;

import java.util.function.Predicate;

/**
 * This predicate tests if a String object can be represented as a Double object.
 */
public class TestMixinPredU implements Predicate<String> {
    /**
     * This method tests if String s can be represented as a valid Double value.
     *
     * @param s String that is to be tested
     * @return true, if String s can be represented as a valid Double value
     */
    @Override
    public boolean test(String s) {
        try {
            Double.valueOf(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
