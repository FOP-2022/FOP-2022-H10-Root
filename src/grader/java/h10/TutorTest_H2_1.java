package h10;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Defines the JUnit test cases related to the class defined in the task H2.1.
 *
 * @author Arianne Roselina Prananto
 */
@TestForSubmission("h10")
@DisplayName("Criterion: H2.1")
public final class TutorTest_H2_1 {
    TutorTest_H2_Helper<Integer> helper1 = new TutorTest_H2_Helper<>();
    TutorTest_H2_Helper<String> helper2 = new TutorTest_H2_Helper<>();

    /* *********************************************************************
     *                               H2.1                                  *
     **********************************************************************/

    @Test
    public void testExtractMethodsExist() {
        Class<?> classH2 = null;
        try {
            classH2 = Class.forName("h10.MyLinkedList");
        } catch (ClassNotFoundException e) {
            fail("Class MyLinkedList does not exist");
        }

        int found = 0;
        for (Method m : classH2.getDeclaredMethods()) {
            if (!m.getName().equals("extractIteratively") && !m.getName().equals("extractRecursively") &&
                !m.getName().equals("extractRecursivelyHelper"))
                continue;
            found++;
        }
        // methods are found
        assertEquals(3, found, "At least one extract*-method does not exist");
    }

    @Test
    public void testExtractMethodsSignatures() {
        Class<?> classH2 = null;
        try {
            classH2 = Class.forName("h10.MyLinkedList");
        } catch (ClassNotFoundException e) {
            fail("Class MyLinkedList does not exist");
        }

        for (Method m : classH2.getDeclaredMethods()) {
            if (!m.getName().equals("extractIteratively") && !m.getName().equals("extractRecursively"))
                continue;

            // TODO : check method's generic type

            // is public
            assertTrue(isPublic(m.getModifiers()));

            // all params are found
            var params = m.getParameters();
            assertEquals(3, params.length, "Parameters in extract*-methods are not complete");

            // param types are correct
            var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName()).collect(Collectors.toList());
            assertTrue(paramTypes.contains("java.util.function.Predicate<? super T>") &&
                       (paramTypes.contains("java.util.function.Function<? super T, ? extends U>") ||
                        paramTypes.contains("java.util.function.Function<? super T,? extends U>")) &&
                       paramTypes.contains("java.util.function.Predicate<? super U>"),
                       "Parameters in extract*-methods are incorrect");

            // return type is correct
            assertEquals(MyLinkedList.class, m.getReturnType(),
                         "Return type in extract*-methods is incorrect");

            // thrown exception type is correct
            assertEquals(MyLinkedListException.class, m.getExceptionTypes()[0],
                         "Thrown exception in extract*-methods is incorrect");
        }
    }

    @Test
    public void testExtractHelperMethod() {
        Class<?> classH2 = null;
        try {
            classH2 = Class.forName("h10.MyLinkedList");
        } catch (ClassNotFoundException e) {
            fail("Class MyLinkedList does not exist");
        }

        for (Method m : classH2.getDeclaredMethods()) {
            if (!m.getName().equals("extractRecursivelyHelper")) continue;

            // TODO : check method's generic type?

            // is public
            assertTrue(isPrivate(m.getModifiers()));

            // all params are found
            var params = m.getParameters();
            assertEquals(5, params.length, "Parameters in extractRecursivelyHelper method are not complete");

            // param types are correct
            var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName())
                .collect(Collectors.toList());
            assertTrue(paramTypes.contains("java.util.function.Predicate<? super T>") &&
                       (paramTypes.contains("java.util.function.Function<? super T, ? extends U>") ||
                        paramTypes.contains("java.util.function.Function<? super T,? extends U>")) &&
                       paramTypes.contains("java.util.function.Predicate<? super U>") &&
                       paramTypes.contains("h10.ListItem<T>") &&
                       paramTypes.contains("int"),
                       "Parameters in extractRecursivelyHelper method are incorrect");

            // return type is correct
            assertEquals(MyLinkedList.class, m.getReturnType(),
                         "Return type in extractRecursivelyHelper method is incorrect");

            // thrown exception type is correct
            assertEquals(MyLinkedListException.class, m.getExceptionTypes()[0],
                         "Thrown exception in extractRecursivelyHelper method is incorrect");
        }
    }

    @Test
    public void testExtractIteratively() {
        var thisList1 = helper1.generateThisListExtract1WithoutExc();
        var thisList2 = helper2.generateThisListExtract2WithoutExc();
        MyLinkedList<Integer[]> actualList1 = new MyLinkedList<>();
        MyLinkedList<Double> actualList2 = new MyLinkedList<>();

        // first list
        for (MyLinkedList<Integer> list : thisList1) {
            var copy = helper1.copyList(list);
            try {
                actualList1 = list.extractIteratively(helper1.predT1, helper1.fctExtract1, helper1.predU1);
            } catch (MyLinkedListException e) {
                fail("extractIteratively method fails with exception: " + e.getMessage());
            }
            try {
                helper1.assertLinkedList(helper1.expectedExtract(copy, helper1.predT1, helper1.fctExtract1, helper1.predU1),
                                         actualList1);
                helper1.assertLinkedList(list, copy);
            } catch (MyLinkedListException e) {
                // never going to happen
            }
        }

        // second list
        for (MyLinkedList<String> list : thisList2) {
            var copy = helper2.copyList(list);
            try {
                actualList2 = list.extractIteratively(helper2.predT2, helper2.fctExtract2, helper2.predU2);
            } catch (MyLinkedListException e) {
                fail("extractIteratively method fails with exception: " + e.getMessage());
            }
            try {
                helper2.assertLinkedList(helper2.expectedExtract(copy, helper2.predT2, helper2.fctExtract2, helper2.predU2),
                                         actualList2);
                helper2.assertLinkedList(list, copy);
            } catch (MyLinkedListException e) {
                // never going to happen
            }
        }
    }

    @Test
    public void testExtractIterativelyException() {
        var list1WithExc = helper1.generateThisListExtract1WithExc();
        var list2WithExc = helper2.generateThisListExtract2WithExc();
        MyLinkedListException actualExc = null;

        // first list
        for (MyLinkedList<Integer> list : list1WithExc) {
            var copy = helper1.copyList(list);
            try {
                list.extractIteratively(helper1.predT1, helper1.fctExtract1, helper1.predU1);
            } catch (MyLinkedListException e) {
                actualExc = e;
            }
            try {
                helper1.expectedExtract(copy, helper1.predT1, helper1.fctExtract1, helper1.predU1);
            } catch (MyLinkedListException e) {
                helper1.assertExceptionMessage(e, actualExc);
            }
        }

        // second list
        for (MyLinkedList<String> list : list2WithExc) {
            var copy = helper2.copyList(list);
            try {
                list.extractIteratively(helper2.predT2, helper2.fctExtract2, helper2.predU2);
            } catch (MyLinkedListException e) {
                actualExc = e;
            }
            try {
                helper2.expectedExtract(copy, helper2.predT2, helper2.fctExtract2, helper2.predU2);
            } catch (MyLinkedListException e) {
                helper2.assertExceptionMessage(e, actualExc);
            }
        }
    }

    @Test
    public void testExtractRecursively() {
        var thisList1 = helper1.generateThisListExtract1WithoutExc();
        var thisList2 = helper2.generateThisListExtract2WithoutExc();
        MyLinkedList<Integer[]> actualList1 = new MyLinkedList<>();
        MyLinkedList<Double> actualList2 = new MyLinkedList<>();

        // first list
        for (MyLinkedList<Integer> list : thisList1) {
            var copy = helper1.copyList(list);
            try {
                actualList1 = list.extractRecursively(helper1.predT1, helper1.fctExtract1, helper1.predU1);
            } catch (MyLinkedListException e) {
                fail("extractIteratively method fails with exception: " + e.getMessage());
            }
            try {
                helper1.assertLinkedList(helper1.expectedExtract(copy, helper1.predT1, helper1.fctExtract1, helper1.predU1),
                                         actualList1);
                helper1.assertLinkedList(list, copy);
            } catch (MyLinkedListException e) {
                // never going to happen
            }
        }

        // second list
        for (MyLinkedList<String> list : thisList2) {
            var copy = helper2.copyList(list);
            try {
                actualList2 = list.extractRecursively(helper2.predT2, helper2.fctExtract2, helper2.predU2);
            } catch (MyLinkedListException e) {
                fail("extractIteratively method fails with exception: " + e.getMessage());
            }
            try {
                helper2.assertLinkedList(helper2.expectedExtract(copy, helper2.predT2, helper2.fctExtract2, helper2.predU2),
                                         actualList2);
                helper2.assertLinkedList(list, copy);
            } catch (MyLinkedListException e) {
                // never going to happen
            }
        }
    }

    @Test
    public void testExtractRecursivelyException() {
        var list1WithExc = helper1.generateThisListExtract1WithExc();
        var list2WithExc = helper2.generateThisListExtract2WithExc();
        MyLinkedListException actualExc = null;

        // first list
        for (MyLinkedList<Integer> list : list1WithExc) {
            var copy = helper1.copyList(list);
            try {
                list.extractRecursively(helper1.predT1, helper1.fctExtract1, helper1.predU1);
            } catch (MyLinkedListException e) {
                actualExc = e;
            }
            try {
                helper1.expectedExtract(copy, helper1.predT1, helper1.fctExtract1, helper1.predU1);
            } catch (MyLinkedListException e) {
                helper1.assertExceptionMessage(e, actualExc);
            }
        }

        // second list
        for (MyLinkedList<String> list : list2WithExc) {
            var copy = helper2.copyList(list);
            try {
                list.extractRecursively(helper2.predT2, helper2.fctExtract2, helper2.predU2);
            } catch (MyLinkedListException e) {
                actualExc = e;
            }
            try {
                helper2.expectedExtract(copy, helper2.predT2, helper2.fctExtract2, helper2.predU2);
            } catch (MyLinkedListException e) {
                helper2.assertExceptionMessage(e, actualExc);
            }
        }
    }

    @Test
    public void testExtractNoOtherMethods() {
        // TODO : test no other methods
    }

    @Test
    public void testExtractReallyIteratively() {
        // TODO : test really iteratively
    }

    @Test
    public void testExtractReallyRecursively() {
        // TODO : test really recursively
    }
}
