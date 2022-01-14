package h10;

import org.junit.jupiter.api.Test;

import static h10.TestHelpers.createExtractDestinationResult;
import static h10.TestHelpers.createExtractList1;
import static h10.TestHelpers.createExtractList2;
import static h10.TestHelpers.createExtractList3;
import static h10.TestHelpers.createExtractSourceResult;
import static h10.TestHelpers.createMixinResult;
import static h10.TestHelpers.createMixinSourceList1;
import static h10.TestHelpers.createMixinSourceList2;
import static h10.TestHelpers.createMixinSourceList3;
import static h10.TestHelpers.createMixinTargetList1;
import static h10.TestHelpers.createMixinTargetList2;
import static h10.TestHelpers.createMixinTargetList3;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is for testing the MyLinkedList class.
 */
public class MyLinkedListTest {

    /**
     * This method tests if extract* methods of MyLinkedList work properly.
     */
    @Test
    public void testExtract() {
        //set up necessary predicates and functions
        TestExtractPredU predU = new TestExtractPredU();
        TestExtractFct fct = new TestExtractFct();
        TestExtractPredT predT = new TestExtractPredT();

        //create all lists used for the tests
        MyLinkedList<int[]> testList1Iterative = createExtractList1();
        MyLinkedList<int[]> testList2Iterative = createExtractList2();
        MyLinkedList<int[]> testList1Recursive = createExtractList1();
        MyLinkedList<int[]> testList2Recursive = createExtractList2();
        MyLinkedList<Integer> destinationList1Iterative = null;
        MyLinkedList<Integer> destinationList1Recursive = null;
        MyLinkedList<Integer> destinationList2Iterative = null;
        MyLinkedList<Integer> destinationList2Recursive = null;
        MyLinkedList<int[]> sourceResult = createExtractSourceResult();
        MyLinkedList<Integer> destinationResult = createExtractDestinationResult();

        //apply extractIteratively to test lists 1 and 2 and fail test if there is an unexpected exception thrown
        try {
            destinationList1Iterative = testList1Iterative.extractIteratively(predT, fct, predU);
            destinationList1Recursive = testList1Recursive.extractRecursively(predT, fct, predU);
        } catch (MyLinkedListException e) {
            //fail test if exception is thrown
            fail();
        }

        MyLinkedList<Integer> lst = null;

        try {
            destinationList2Iterative = testList2Iterative.extractIteratively(predT, fct, predU);
            destinationList2Recursive = testList2Recursive.extractRecursively(predT, fct, predU);
        } catch (MyLinkedListException e) {
            //fail test if exception is thrown
            fail();
        }

        //test first list
        //set up pointers
        ListItem<int[]> pSource1Iterative = testList1Iterative.head;
        ListItem<int[]> pSource1Recursive = testList1Recursive.head;
        ListItem<int[]> pSource2 = sourceResult.head;
        ListItem<Integer> pDestination1Iterative = destinationList1Iterative.head;
        ListItem<Integer> pDestination1Recursive = destinationList1Recursive.head;
        ListItem<Integer> pDestination2 = destinationResult.head;

        //test if source list is created correctly
        while (pSource2 != null || pSource1Iterative != null || pSource1Recursive != null) {
            //check if extractIteratively produced the desired result at current position
            assertArrayEquals(pSource1Iterative.key, pSource2.key);
            //check if extractRecursively produced the desired result at current position
            assertArrayEquals(pSource1Recursive.key, pSource2.key);

            //iterate
            pSource1Iterative = pSource1Iterative.next;
            pSource1Recursive = pSource1Recursive.next;
            pSource2 = pSource2.next;
        }

        //test if destination list is created correctly
        while (pDestination1Iterative != null || pDestination1Recursive != null || pDestination2 != null) {
            //check if extractIteratively produced the desired result at current position
            assertEquals(pDestination1Iterative.key, pDestination2.key);
            //check if extractRecursively produced the desired result at current position
            assertEquals(pDestination1Recursive.key, pDestination2.key);

            //iterate
            pDestination1Iterative = pDestination1Iterative.next;
            pDestination1Recursive = pDestination1Recursive.next;
            pDestination2 = pDestination2.next;
        }

        //test second list
        //set up pointers
        pSource1Iterative = testList2Iterative.head;
        pSource1Recursive = testList2Recursive.head;
        pSource2 = sourceResult.head;
        pDestination1Iterative = destinationList2Iterative.head;
        pDestination1Recursive = destinationList2Recursive.head;
        pDestination2 = destinationResult.head;

        //test if source list is created correctly
        while (pSource2 != null || pSource1Iterative != null || pSource1Recursive != null) {
            //check if extractIteratively produced the desired result at current position
            assertArrayEquals(pSource1Iterative.key, pSource2.key);
            //check if extractRecursively produced the desired result at current position
            assertArrayEquals(pSource1Recursive.key, pSource2.key);

            //iterate
            pSource1Iterative = pSource1Iterative.next;
            pSource1Recursive = pSource1Recursive.next;
            pSource2 = pSource2.next;
        }

        //test if destination list is created correctly
        while (pDestination1Iterative != null || pDestination1Recursive != null || pDestination2 != null) {
            //check if extractIteratively produced the desired result at current position
            assertEquals(pDestination1Iterative.key, pDestination2.key);
            //check if extractRecursively produced the desired result at current position
            assertEquals(pDestination1Recursive.key, pDestination2.key);

            //iterate
            pDestination1Iterative = pDestination1Iterative.next;
            pDestination1Recursive = pDestination1Recursive.next;
            pDestination2 = pDestination2.next;
        }

        MyLinkedList<int[]> testList3Iterative = createExtractList3();
        MyLinkedList<int[]> testList3Recursive = createExtractList3();


        //test if exception is thrown correctly in third list by extractIteratively
        MyLinkedListException excIterative = assertThrows(MyLinkedListException.class,
            () -> testList3Iterative.extractIteratively(predT, fct, predU),
            "no exception thrown");

        //test if exception is thrown correctly in third list by extractRecursively
        MyLinkedListException excRecursive = assertThrows(MyLinkedListException.class,
            () -> testList3Recursive.extractRecursively(predT, fct, predU),
            "no exception thrown");

        //check if exception message is correct (correct index) by extractIteratively
        assertEquals('4', excIterative.getMessage().charAt(1));

        //check if exception message is correct (correct index) by extractRecursively
        assertEquals('4', excRecursive.getMessage().charAt(1));
    }

    /**
     * This method tests if mixin* methods of MyLinkedList work properly.
     */
    @Test
    public void testMixin() {
        //set up necessary predicates and functions
        TestMixinBiPred biPred = new TestMixinBiPred();
        TestMixinFct fct = new TestMixinFct();
        TestMixinPredU predU = new TestMixinPredU();

        //set up necessary lists for the tests
        MyLinkedList<Number> targetList1Iterative = createMixinTargetList1();
        MyLinkedList<Number> targetList1Recursive = createMixinTargetList1();
        MyLinkedList<Number> targetList2Iterative = createMixinTargetList2();
        MyLinkedList<Number> targetList2Recursive = createMixinTargetList2();
        MyLinkedList<String> sourceList1 = createMixinSourceList1();
        MyLinkedList<String> sourceList2 = createMixinSourceList2();
        MyLinkedList<Number> resultList = createMixinResult();

        //run mixin* on first lists
        try {
            targetList1Iterative.mixinIteratively(sourceList1, biPred, fct, predU);
            targetList1Recursive.mixinRecursively(sourceList1, biPred, fct, predU);
        } catch (MyLinkedListException e) {
            //fail tests if exception is thrown unexpectedly
            fail();
        }

        try {
            targetList2Iterative.mixinIteratively(sourceList2, biPred, fct, predU);
            targetList2Recursive.mixinRecursively(sourceList2, biPred, fct, predU);
        } catch (MyLinkedListException e) {
            //fail tests if exception is thrown unexpectedly
            fail();
        }


        //test first lists
        //set up pointers
        ListItem<Number> pIterative = targetList1Iterative.head;
        ListItem<Number> pRecursive = targetList1Recursive.head;
        ListItem<Number> pResult = resultList.head;

        //check if lists are equal at each position
        while (pIterative != null || pRecursive != null || pResult != null) {
            //test if iterative list matches the desired result
            assertEquals(pIterative.key, pResult.key);

            //test if recursive list matches the desired result
            assertEquals(pRecursive.key, pResult.key);

            //iterate
            pIterative = pIterative.next;
            pRecursive = pRecursive.next;
            pResult = pResult.next;
        }

        //test second lists
        //set up pointers
        pIterative = targetList2Iterative.head;
        pRecursive = targetList2Recursive.head;
        pResult = resultList.head;

        //check if lists are equal at each position
        while (pIterative != null || pRecursive != null  || pResult != null) {
            //test if iterative list matches the desired result
            assertEquals(pIterative.key, pResult.key);

            //test if recursive list matches the desired result
            assertEquals(pRecursive.key, pResult.key);

            //iterate
            pIterative = pIterative.next;
            pRecursive = pRecursive.next;
            pResult = pResult.next;
        }

        MyLinkedList<Number> targetList3Iterative = createMixinTargetList3();
        MyLinkedList<Number> targetList3Recursive = createMixinTargetList3();
        MyLinkedList<String> sourceList3 = createMixinSourceList3();

        //test if exception is thrown correctly in third list by mixinIteratively
        MyLinkedListException excIterative = assertThrows(MyLinkedListException.class,
            () -> targetList3Iterative.mixinIteratively(sourceList3, biPred, fct, predU),
            "no exception thrown");

        //test if exception is thrown correctly in third list by mixinRecursively
        MyLinkedListException excRecursive = assertThrows(MyLinkedListException.class,
            () -> targetList3Recursive.mixinRecursively(sourceList3, biPred, fct, predU),
            "no exception thrown");

        //check if exception message is correct (correct index) by extractIteratively
        assertEquals('2', excIterative.getMessage().charAt(1));

        //check if exception message is correct (correct index) by extractRecursively
        assertEquals('2', excRecursive.getMessage().charAt(1));

    }

}
