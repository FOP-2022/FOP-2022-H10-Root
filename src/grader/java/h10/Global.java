package h10;

import h10.utils.State;
import h10.utils.transformer.TutorTransformer;

import java.util.Random;

/**
 * Global interface for transformer.
 */
public interface Global {
    Random RANDOM = new Random(42);
    double SIMILARITY = 0.8;
    int T = 10;
    //Logger LOGGER = LogManager.getLogger("Global");
    TutorTransformer TRANSFORMER = new TutorTransformer();
    State MODE = new State();
}
