package h10;

public class ListItem <T> {
    public ListItem<T> next;
    public T key;

    public ListItem (T key) {
        this.key = key;
    }
}


