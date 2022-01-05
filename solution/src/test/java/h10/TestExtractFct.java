package h10;

import java.util.function.Function;

public class TestExtractFct implements Function<int[], Integer> {

    @Override
    public Integer apply(int[] ints) {
        int sum = 0;

        for (int i : ints) {
            sum += i;
        }

        return sum;    }
}
