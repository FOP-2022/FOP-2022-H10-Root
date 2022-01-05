package h10;

import java.util.function.Predicate;

public class TestMixinPredU implements Predicate<String> {
    @Override
    public boolean test(String s) {
        try{
            Double.valueOf(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
