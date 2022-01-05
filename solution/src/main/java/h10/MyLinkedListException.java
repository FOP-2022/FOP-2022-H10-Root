package h10;

public class MyLinkedListException extends Exception{
    public MyLinkedListException(Integer i, Object x) {
        super("(" +i.intValue() +"," +x.toString() +")");
    }
}
