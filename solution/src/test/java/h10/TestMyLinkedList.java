package h10;

import org.junit.jupiter.api.Test;

import static h10.TestHelpers.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestMyLinkedList {

    @Test
    public void testExtract() {
        TestExtractPredU predU = new TestExtractPredU();
        TestExtractFct fct = new TestExtractFct();
        TestExtractPredT predT = new TestExtractPredT();

        //create all lists used for the tests
        MyLinkedList<int[]> testList1 = createList1();
        MyLinkedList<int[]> testList2 = createList2();
        MyLinkedList<int[]> testList3 = createList3();
        MyLinkedList<Integer> destinationList1 = null;
        MyLinkedList<Integer> destinationList2 = null;
        MyLinkedList<int[]> sourceResult = createSourceResult();
        MyLinkedList<Integer> destinationResult = createDestinationResult();

        //apply extractIteratively to test lists 1 and 2 and fail test if there is an unexpected exception thrown
        try {
            destinationList1 = testList1.extractIteratively(predT, fct, predU);
        } catch (MyLinkedListException e) {
            //fail test if exception is thrown
            fail();
        }

        MyLinkedList<Integer> lst = null;
        try {
           destinationList2 = testList2.extractIteratively(predT, fct, predU);
        } catch (MyLinkedListException e) {
            //fail test if exception is thrown
            fail();
        }

        //test first list

        //set up pointers
        ListItem<int[]> pSource1 = testList1.head;
        ListItem<int[]> pSource2 = sourceResult.head;
        ListItem<Integer> pDestination1 = destinationList1.head;
        ListItem<Integer> pDestination2 = destinationResult.head;

        //test if source list is created correctly
        while (pSource2 != null || pSource1 != null) {
            //check if extractIteratively produced the desired result at current position
            assertArrayEquals(pSource1.key, pSource2.key);

            //iterate
            pSource1 = pSource1.next;
            pSource2 = pSource2.next;
        }

        //test if destination list is created correctly
        while (pDestination1 != null || pDestination2 != null) {
            //check if extractIteratively produced the desired result at current position
            assertEquals(pDestination1.key, pDestination2.key);

            //iterate
            pDestination1 = pDestination1.next;
            pDestination2 = pDestination2.next;
        }

        //test second list
        //set up pointers
        pSource1 = testList2.head;
        pSource2 = sourceResult.head;
        pDestination1 = destinationList2.head;
        pDestination2 = destinationResult.head;

        //test if source list is created correctly
        while (pSource2 != null || pSource1 != null) {
            //check if extractIteratively produced the desired result at current position
            assertArrayEquals(pSource1.key, pSource2.key);

            //iterate
            pSource1 = pSource1.next;
            pSource2 = pSource2.next;
        }

        //test if destination list is created correctly
        while (pDestination1 != null || pDestination2 != null) {
            //check if extractIteratively produced the desired result at current position
            assertEquals(pDestination1.key, pDestination2.key);

            //iterate
            pDestination1 = pDestination1.next;
            pDestination2 = pDestination2.next;
        }

        //test if exception is thrown correctly in third list
        MyLinkedListException exc = assertThrows(MyLinkedListException.class, () -> testList3.extractRecursively(predT, fct, predU), "no exception thrown");
        //check if exception message is correct (correct index)
        assertEquals(exc.getMessage().charAt(1), '4');

    }


}
