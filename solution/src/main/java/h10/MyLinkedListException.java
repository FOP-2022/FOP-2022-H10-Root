package h10;

/**
 * This class is used for creating Exceptions for Objects of type MyLinkedList.
 */
public class MyLinkedListException extends Exception {

    /**
     * This method creates an object of type MyLinkedListException with the message (i, x).
     *
     * @param i index of element in MyLinkedList
     * @param x object at index i in MyLinkedList
     */
    public MyLinkedListException(Integer i, Object x) {
        super("(" + i.intValue() + "," + x.toString() + ")");
    }
}
