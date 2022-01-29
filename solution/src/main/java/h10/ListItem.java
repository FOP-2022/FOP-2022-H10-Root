package h10;

/**
 * An instance of this class represents an item in a {@link MyLinkedList}.
 *
 * @param <T> type of key
 */
public class ListItem<T> {
    /**
     * The successor node of this list item.
     */
    public ListItem<T> next;
    /**
     * The value of this list item.
     */
    public T key;

    /**
     * Constructs and initializes an empty list item.
     */
    public ListItem() {
    }

    /**
     * Constructs and initializes an empty list item with a value.
     *
     * @param key the value of the list item
     */
    public ListItem(T key) {
        this.key = key;
    }
}
