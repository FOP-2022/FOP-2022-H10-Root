package h10;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
            if (!m.getName().equals("extractIteratively")
                && !m.getName().equals("extractRecursively")
                && !m.getName().equals("extractRecursivelyHelper")) {
                continue;
            }
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
            if (!m.getName().equals("extractIteratively")
                && !m.getName().equals("extractRecursively")) {
                continue;
            }

            // is generic with type U
            assertEquals(1, m.getTypeParameters().length, "extract*-method is not generic");
            assertEquals("U", m.getTypeParameters()[0].getTypeName(),
                         "extractRecursivelyHelper method is generic with an incorrect type");

            // is public
            assertTrue(isPublic(m.getModifiers()));

            // all params are found
            var params = m.getParameters();
            assertEquals(3, params.length, "Parameters in extract*-method is not complete");

            // param types are correct
            var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName()).collect(Collectors.toList());
            assertTrue(paramTypes.contains("java.util.function.Predicate<? super T>")
                       && (paramTypes.contains("java.util.function.Function<? super T, ? extends U>")
                           || paramTypes.contains("java.util.function.Function<? super T,? extends U>"))
                       && paramTypes.contains("java.util.function.Predicate<? super U>"),
                       "Parameters in extract*-method are incorrect");

            // return type is correct
            assertEquals("h10.MyLinkedList<U>", m.getGenericReturnType().getTypeName(),
                         "Return type in extract*-method is incorrect");

            // thrown exception type is correct
            assertEquals(MyLinkedListException.class, m.getExceptionTypes()[0],
                         "Thrown exception in extract*-method is incorrect");
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
            if (!m.getName().equals("extractRecursivelyHelper")) {
                continue;
            }

            // is generic with type U
            assertEquals(1, m.getTypeParameters().length,
                         "extractRecursivelyHelper method is not generic");
            assertEquals("U", m.getTypeParameters()[0].getTypeName(),
                         "extractRecursivelyHelper method is generic with an incorrect type");

            // is public
            assertTrue(isPrivate(m.getModifiers()));

            // all params are found
            var params = m.getParameters();
            assertEquals(5, params.length, "Parameters in extractRecursivelyHelper method are not complete");

            // param types are correct
            var paramTypes = Arrays.stream(params).map(x -> x.getParameterizedType().getTypeName())
                .collect(Collectors.toList());
            assertTrue(paramTypes.contains("java.util.function.Predicate<? super T>")
                       && (paramTypes.contains("java.util.function.Function<? super T, ? extends U>")
                           || paramTypes.contains("java.util.function.Function<? super T,? extends U>"))
                       && paramTypes.contains("java.util.function.Predicate<? super U>")
                       && paramTypes.contains("h10.ListItem<T>")
                       && paramTypes.contains("int"),
                       "Parameters in extractRecursivelyHelper method are incorrect");

            // return type is correct
            assertEquals("h10.MyLinkedList<U>", m.getGenericReturnType().getTypeName(),
                         "Return type in extractRecursivelyHelper method is incorrect");

            // thrown exception type is correct
            assertEquals(MyLinkedListException.class, m.getExceptionTypes()[0],
                         "Thrown exception in extractRecursivelyHelper method is incorrect");
        }
    }

    @Test
    public void testExtractIteratively() {
        var thisLists1 = TutorTest_Generators.generateThisListExtract1WithoutExc();
        var thisLists2 = TutorTest_Generators.generateThisListExtract2WithoutExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralExtract(thisLists1, TutorTest_H2_Helper.MethodType.ITERATIVE, TutorTest_Generators.predT1,
                                   TutorTest_Generators.fctExtract1, TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralExtract(thisLists2, TutorTest_H2_Helper.MethodType.ITERATIVE, TutorTest_Generators.predT2,
                                   TutorTest_Generators.fctExtract2, TutorTest_Generators.predU2);
    }

    @Test
    public void testExtractIterativelyException() {
        var thisLists1 = TutorTest_Generators.generateThisListExtract1WithExc();
        var thisLists2 = TutorTest_Generators.generateThisListExtract2WithExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralExtract(thisLists1, TutorTest_H2_Helper.MethodType.ITERATIVE, TutorTest_Generators.predT1,
                                   TutorTest_Generators.fctExtract1, TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralExtract(thisLists2, TutorTest_H2_Helper.MethodType.ITERATIVE, TutorTest_Generators.predT2,
                                   TutorTest_Generators.fctExtract2, TutorTest_Generators.predU2);
    }

    @Test
    public void testExtractRecursively() {
        var thisLists1 = TutorTest_Generators.generateThisListExtract1WithoutExc();
        var thisLists2 = TutorTest_Generators.generateThisListExtract2WithoutExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralExtract(thisLists1, TutorTest_H2_Helper.MethodType.RECURSIVE, TutorTest_Generators.predT1,
                                   TutorTest_Generators.fctExtract1, TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralExtract(thisLists2, TutorTest_H2_Helper.MethodType.RECURSIVE, TutorTest_Generators.predT2,
                                   TutorTest_Generators.fctExtract2, TutorTest_Generators.predU2);
    }

    @Test
    public void testExtractRecursivelyException() {
        var thisLists1 = TutorTest_Generators.generateThisListExtract1WithExc();
        var thisLists2 = TutorTest_Generators.generateThisListExtract2WithExc();

        // call test for the first list type (Integer, Integer[])
        helper1.testGeneralExtract(thisLists1, TutorTest_H2_Helper.MethodType.RECURSIVE, TutorTest_Generators.predT1,
                                   TutorTest_Generators.fctExtract1, TutorTest_Generators.predU1);
        // call test for the second list type (String, Double)
        helper2.testGeneralExtract(thisLists2, TutorTest_H2_Helper.MethodType.RECURSIVE, TutorTest_Generators.predT2,
                                   TutorTest_Generators.fctExtract2, TutorTest_Generators.predU2);
    }

    @Test
    public void testExtractNoOtherMethods() {
        // TODO : test no other (unimplemented / new implemented) methods are used
    }

    @Test
    public void testExtractReallyIteratively() {
        // TODO : test if extractIteratively has one loop only
    }

    @Test
    public void testExtractReallyRecursively() {
        var thisList = TutorTest_Generators.generateThisListExtractMockito();
        try {
            thisList.extractRecursively(TutorTest_Generators.predT1, TutorTest_Generators.fctExtract1,
                                        TutorTest_Generators.predU1);
            // TODO : ClassTransformer to change modifier method, then verify (but how?)
            /*Mockito.verify(thisList, Mockito.atLeast(2))
                .extractRecursivelyHelper(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());*/
        } catch (Exception e) {
            // MyLinkedListException will never be thrown
            fail("extractRecursively does not use recursion");
        }
    }
}
