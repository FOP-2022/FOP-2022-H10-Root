package h10;

import java.util.function.Function;

public class TestMixinFct implements Function<String, Number> {
    /**
     * This method converts a String to a Double
     * @param s String that is to be converted
     * @return Double representation of parameter s
     */
    @Override
    public Number apply(String s) {
        return Double.valueOf(s);
    }
}
