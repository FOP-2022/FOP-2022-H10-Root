package h10;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Constructor;
import java.util.Random;

import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Defines the JUnit test cases related to the class defined in the task H1.
 *
 * @author Arianne Roselina Prananto
 */
@TestForSubmission("h10")
@DisplayName("Criterion: Class Traits")
public final class TutorTest_H1 {

    /* *********************************************************************
     *                                 H1                                  *
     **********************************************************************/

    @Test
    public void testClassSignatures() {
        Class<?> classH1 = null;
        try {
            classH1 = Class.forName("h10.MyLinkedListException");
        } catch (ClassNotFoundException e) {
            fail("Class MyLinkedListException does not exist");
        }

        // is not abstract
        assertFalse(isAbstract(classH1.getModifiers()), "Class MyLinkedListException is abstract");
        // is public
        assertTrue(isPublic(classH1.getModifiers()), "Class MyLinkedListException is not public");
        // is a direct extension from Exception
        assertEquals(classH1.getSuperclass(), Exception.class,
                     "Class TimeStamp is not a direct extension from Exception");
    }

    @Test
    public void testConstructor() {
        int exceptions = 0;
        Constructor<?> constructor = null;
        try {
            constructor = MyLinkedListException.class.getDeclaredConstructor(Integer.class, Object.class);
        } catch (NoSuchMethodException e) {
            exceptions++;
        }
        try {
            constructor = MyLinkedListException.class.getDeclaredConstructor(Object.class, Integer.class);
        } catch (NoSuchMethodException e) {
            exceptions++;
        }

        // does not exist
        assertTrue(exceptions < 2,
                   "Constructor MyLinkedListException does not exist or is not correct");
        // is public
        assertTrue(isPublic(constructor.getModifiers()),
                   "Constructor MyLinkedListException is not public");
    }

    @Test
    public void testMessage() {
        int numOfTests = 100;
        var nums = createManyRandomIntegers(numOfTests);
        var objs = createManyRandomObjects(numOfTests);

        for (int i = 0; i < numOfTests; i++) {
            assertTrue(new MyLinkedListException(nums[i], objs[i]).getMessage().equals(createCorrectMessage(nums[i], objs[i])),
                       "Constructor MyLinkedListException returns wrong message");
        }
    }

    String createCorrectMessage(Integer num, Object obj) {
        return "(" + num + "," + obj.toString() + ")";
    }

    Integer[] createManyRandomIntegers(int numOfTests) {
        Integer[] nums = new Integer[numOfTests];
        Random rn = new Random();
        for (int i = 0; i < numOfTests; i++) {
            nums[i] = rn.nextInt();
        }
        return nums;
    }

    Object[] createManyRandomObjects(int numOfTests) {
        Object[] objs = new Object[numOfTests];
        Random rn = new Random();
        for (int i = 0; i < numOfTests; i++) {
            if (i % 2 == 0) objs[i] = Integer.toHexString(rn.nextInt());
            else objs[i] = rn.nextInt();
        }
        return objs;
    }
}
