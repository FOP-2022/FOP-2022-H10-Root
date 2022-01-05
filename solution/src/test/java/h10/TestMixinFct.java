package h10;

import java.util.function.Function;

public class TestMixinFct implements Function<String, Number> {
    @Override
    public Number apply(String s) {
        return Double.valueOf(s);
    }
}
