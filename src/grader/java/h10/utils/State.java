
package h10.utils;

import java.util.Objects;


/**
 * State class.
 */

public class State {
    private Object state = 0;

    public <T> boolean is(T toCompare) {
        return Objects.equals(toCompare, state);
    }

    public <T> T get() {
        return (T) state;
    }

    public int getInt() {
        return (int) state;
    }

    public int incInt() {
        state = ((int) state) + 1;
        return (int) state - 1;
    }

    public <T> void set(T state) {
        this.state = state;
    }
}

