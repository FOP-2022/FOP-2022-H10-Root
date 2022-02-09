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
@DisplayName("Criterion: H1")
public final class TutorTest_H1 {
    static final String className = "MyLinkedListException";

    /* *********************************************************************
     *                                 H1                                  *
     **********************************************************************/

    @Test
    public void testClassSignatures() {
        Class<?> classH1 = null;
        try {
            classH1 = Class.forName("h10." + className);
        } catch (ClassNotFoundException e) {
            fail(String.format("Class %s does not exist", className));
        }

        // is not abstract
        assertFalse(isAbstract(classH1.getModifiers()), String.format("Class %s is abstract", className));
        // is public
        assertTrue(isPublic(classH1.getModifiers()), String.format("Class %s is not public", className));
        // is a direct extension from Exception
        assertEquals(Exception.class, classH1.getSuperclass(),
                     String.format("Class %s is not a direct extension from Exception", className));
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
                   String.format("Constructor %s does not exist or is incorrect", className));
        // is public
        assertTrue(isPublic(constructor.getModifiers()),
                   String.format("Constructor %s is not public", className));
    }

    @Test
    public void testMessage() {
        int numOfTests = 100;
        var nums = createManyRandomIntegers(numOfTests);
        var objs = createManyRandomObjects(numOfTests);

        for (int i = 0; i < numOfTests; i++) {
            assertEquals(new MyLinkedListException(nums[i], objs[i]).getMessage(),
                         createCorrectMessage(nums[i], objs[i]),
                         String.format("Constructor %s returns wrong message", className));
        }
    }

    /* *********************************************************************
     *                        Some random generators                       *
     **********************************************************************/

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
            if (i % 2 == 0) {
                objs[i] = Integer.toHexString(rn.nextInt());
            } else {
                objs[i] = rn.nextInt();
            }
        }
        return objs;
    }
}
