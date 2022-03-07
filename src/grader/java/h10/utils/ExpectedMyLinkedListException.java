package h10.utils;


/**
 * Thrown to indicate that an illegal operation with an element of the list.
 *
 * @author Nhan Huynh
 */
public class ExpectedMyLinkedListException extends Exception {

    /**
     * Constructs and initializes a {@code MyLinkedListException} with two argument indicating element and the index of
     * it where the illegal operation occurred.
     *
     * @param i the index of the element
     * @param x th element at the specified element
     */
    public ExpectedMyLinkedListException(Integer i, Object x) {
        super("(" + i + "," + x.toString() + ")");
    }
}
