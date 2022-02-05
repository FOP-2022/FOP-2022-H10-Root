package h10;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
@DisplayName("Criterion: Class Traits")
public final class TutorTest_H2_1 {

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
        assertEquals(found, 3, "At least one extract*-method does not exist");
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
            assertEquals(params.length, 3, "Parameters in extract*-methods are not complete");

            // param types are correct
            var paramTypes = Arrays.stream(params).map(x -> x.getType().toGenericString()).collect(Collectors.toList());
            assertTrue(paramTypes.contains("Predicate<? super T>") &&
                       paramTypes.contains("Function<? super T,? extends U>") &&
                       paramTypes.contains("Predicate<? super U>"),
                       "Parameters in extract*-methods are incorrect");

            // param names are correct
            var paramNames = Arrays.stream(params).map(Parameter::getName).collect(Collectors.toList());
            assertTrue(paramNames.contains("predT") &&
                       paramNames.contains("fct") &&
                       paramNames.contains("predU"), "Parameters in extract*-methods are incorrect");

            // return type is correct
            assertEquals(m.getGenericReturnType().toString(), "MyLinkedList<U>",
                         "Return type in extract*-methods is incorrect");

            // thrown exception type is correct
            assertEquals(m.getExceptionTypes()[0], MyLinkedListException.class,
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
            assertEquals(params.length, 5, "Parameters in extractRecursivelyHelper method are not complete");

            // param types are correct
            var paramTypes = Arrays.stream(params).map(x -> x.getType().toGenericString()).collect(Collectors.toList());
            assertTrue(paramTypes.contains("Predicate<? super T>") &&
                       paramTypes.contains("Function<? super T,? extends U>") &&
                       paramTypes.contains("Predicate<? super U>") &&
                       paramTypes.contains("ListItem<T>") &&
                       paramTypes.contains("int"),
                       "Parameters in extractRecursivelyHelper method are incorrect");

            // param names are correct
            var paramNames = Arrays.stream(params).map(Parameter::getName).collect(Collectors.toList());
            assertTrue(paramNames.contains("predT") &&
                       paramNames.contains("fct") &&
                       paramNames.contains("predU") &&
                       paramNames.contains("pSrc") &&
                       paramNames.contains("index"), "Parameters in extractRecursivelyHelper method are incorrect");

            // return type is correct
            assertEquals(m.getReturnType().toGenericString(), "MyLinkedList<U>",
                         "Return type in extractRecursivelyHelper method is incorrect");

            // thrown exception type is correct
            assertEquals(m.getExceptionTypes()[0], MyLinkedListException.class,
                         "Thrown exception in extractRecursivelyHelper method is incorrect");
        }
    }

    @Test
    public void testExtractIteratively() {
        TutorTest_H2_Helper<Integer> t1 = new TutorTest_H2_Helper<>();
        TutorTest_H2_Helper<String> t2 = new TutorTest_H2_Helper<>();
        var list1WithoutExc = t1.generateMyLinkedList1WithoutExc(10, 10);
        var list2WithoutExc = t2.generateMyLinkedList2WithoutExc(10, 10);
        MyLinkedList<Integer[]> actualList = null;

        // first list
        for (MyLinkedList<Integer> i : list1WithoutExc) {
            try {
                actualList = i.extractIteratively(t1.predT1, t1.fct1, t1.predU1);
            } catch (MyLinkedListException e) {
                fail("extractIteratively method fails with exception: " + e.getMessage());
            }
            try {
                t1.assertLinkedList(t1.expectedExtract(i, t1.predT1, t1.fct1, t1.predU1), actualList);
            } catch (MyLinkedListException e) {
                // never going to happen
            }
        }

        // second list
        for (MyLinkedList<String> i : list2WithoutExc) {
            try {
                actualList = i.extractIteratively(t2.predT2, t2.fct2, t2.predU2);
            } catch (MyLinkedListException e) {
                fail("extractIteratively method fails with exception: " + e.getMessage());
            }
            try {
                t2.assertLinkedList(t2.expectedExtract(i, t2.predT2, t2.fct2, t2.predU2), actualList);
            } catch (MyLinkedListException e) {
                // never going to happen
            }
        }
    }

    @Test
    public void testExtractIterativelyException() {
        TutorTest_H2_Helper<Integer> t1 = new TutorTest_H2_Helper<>();
        TutorTest_H2_Helper<String> t2 = new TutorTest_H2_Helper<>();
        var list1WithExc = t1.generateMyLinkedList1WithExc();
        var list2WithExc = t2.generateMyLinkedList2WithExc();
        MyLinkedList<Integer[]> actualList = null;
        MyLinkedListException actualExc = null;

        // first list
        for (MyLinkedList<Integer> i : list1WithExc) {
            try {
                actualList = i.extractIteratively(t1.predT1, t1.fct1, t1.predU1);
            } catch (MyLinkedListException e) {
                actualExc = e;
            }
            try {
                t1.assertLinkedList(t1.expectedExtract(i, t1.predT1, t1.fct1, t1.predU1), actualList);
            } catch (MyLinkedListException e) {
                t1.assertExceptionMessage(e, actualExc);
            }
        }

        // second list
        for (MyLinkedList<String> i : list2WithExc) {
            try {
                actualList = i.extractIteratively(t2.predT2, t2.fct2, t2.predU2);
            } catch (MyLinkedListException e) {
                actualExc = e;
            }
            try {
                t2.assertLinkedList(t2.expectedExtract(i, t2.predT2, t2.fct2, t2.predU2), actualList);
            } catch (MyLinkedListException e) {
                t2.assertExceptionMessage(e, actualExc);
            }
        }
    }

    @Test
    public void testExtractRecursively() {
        TutorTest_H2_Helper<Integer> t1 = new TutorTest_H2_Helper<>();
        TutorTest_H2_Helper<String> t2 = new TutorTest_H2_Helper<>();
        var list1WithoutExc = t1.generateMyLinkedList1WithoutExc(10, 10);
        var list2WithoutExc = t2.generateMyLinkedList2WithoutExc(10, 10);
        MyLinkedList<Integer[]> actualList = null;

        // first list
        for (MyLinkedList<Integer> i : list1WithoutExc) {
            try {
                actualList = i.extractRecursively(t1.predT1, t1.fct1, t1.predU1);
            } catch (MyLinkedListException e) {
                fail("extractIteratively method fails with exception: " + e.getMessage());
            }
            try {
                t1.assertLinkedList(t1.expectedExtract(i, t1.predT1, t1.fct1, t1.predU1), actualList);
            } catch (MyLinkedListException e) {
                // never going to happen
            }
        }

        // second list
        for (MyLinkedList<String> i : list2WithoutExc) {
            try {
                actualList = i.extractRecursively(t2.predT2, t2.fct2, t2.predU2);
            } catch (MyLinkedListException e) {
                fail("extractIteratively method fails with exception: " + e.getMessage());
            }
            try {
                t2.assertLinkedList(t2.expectedExtract(i, t2.predT2, t2.fct2, t2.predU2), actualList);
            } catch (MyLinkedListException e) {
                // never going to happen
            }
        }
    }

    @Test
    public void testExtractRecursivelyException() {
        TutorTest_H2_Helper<Integer> t1 = new TutorTest_H2_Helper<>();
        TutorTest_H2_Helper<String> t2 = new TutorTest_H2_Helper<>();
        var list1WithExc = t1.generateMyLinkedList1WithExc();
        var list2WithExc = t2.generateMyLinkedList2WithExc();
        MyLinkedList<Integer[]> actualList = null;
        MyLinkedListException actualExc = null;

        // first list
        for (MyLinkedList<Integer> i : list1WithExc) {
            try {
                actualList = i.extractRecursively(t1.predT1, t1.fct1, t1.predU1);
            } catch (MyLinkedListException e) {
                actualExc = e;
            }
            try {
                t1.assertLinkedList(t1.expectedExtract(i, t1.predT1, t1.fct1, t1.predU1), actualList);
            } catch (MyLinkedListException e) {
                t1.assertExceptionMessage(e, actualExc);
            }
        }

        // second list
        for (MyLinkedList<String> i : list2WithExc) {
            try {
                actualList = i.extractRecursively(t2.predT2, t2.fct2, t2.predU2);
            } catch (MyLinkedListException e) {
                actualExc = e;
            }
            try {
                t2.assertLinkedList(t2.expectedExtract(i, t2.predT2, t2.fct2, t2.predU2), actualList);
            } catch (MyLinkedListException e) {
                t2.assertExceptionMessage(e, actualExc);
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
