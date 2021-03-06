package h10.utils;

/**
 * Define some fail messages for the whole test.
 *
 * @author Arianne Roselina Prananto
 */
public final class TutorTest_Messages {

    /* *********************************************************************
     *                         Fail messages methods                       *
     **********************************************************************/

    public static String classNotFound(String className) {
        return String.format("Class %s does not exist", className);
    }

    public static String classModifierIncorrect(String className) {
        return String.format("Class %s has incorrect modifier", className);
    }

    public static String classExtendsIncorrect(String className) {
        return String.format("Class %s has incorrect super class", className);
    }

    public static String fieldNotFound(String fieldName) {
        return String.format("Field %s does not exist", fieldName);
    }

    public static String fieldNotConstant(String fieldName) {
        return String.format("Field %s is not a constant", fieldName);
    }

    public static String constructorWrongMessage(String className) {
        return String.format("Constructor %s returns wrong message", className);
    }

    public static String assertListFailed() {
        return "Assertion for MyLinkedList failed";
    }

    public static String assertLambdaFailed(String fieldName) {
        return String.format("Assertion for lambda in %s failed", fieldName);
    }

    public static String exceptionMessageIncorrect() {
        return "Message of MyLinkedListException is incorrect";
    }

    public static String methodNotFound(String methodName) {
        return String.format("%s-method does not exist", methodName);
    }

    public static String methodGeneric(String methodName) {
        return String.format("%s-method is generic", methodName);
    }

    public static String methodNotGeneric(String methodName) {
        return String.format("%s-method is not generic", methodName);
    }

    public static String methodGenericTypeIncorrect(String methodName) {
        return String.format("%s-method is generic with an incorrect type", methodName);
    }

    public static String methodModifierIncorrect(String methodName) {
        return String.format("%s-method has incorrect modifier", methodName);
    }

    public static String methodParamIncomplete(String methodName) {
        return String.format("Parameter count in %s-method is incorrect", methodName);
    }

    public static String methodParamIncorrect(String methodName) {
        return String.format("Parameters in %s-method are incorrect", methodName);
    }

    public static String methodReturnTypeIncorrect(String methodName) {
        return String.format("Return type in %s-method is incorrect", methodName);
    }

    public static String methodExceptionTypeIncorrect(String methodName) {
        return String.format("Thrown exception type in %s-method is incorrect", methodName);
    }

    public static String methodUseOtherMethod(String methodName, String calleeName) {
        return String.format("%s-method uses another newly implemented method %s", calleeName, methodName);
    }

    public static String methodFalseNumberOfLoop(String methodName) {
        return String.format("%s-method has incorrect number of loops", methodName);
    }

    public static String methodNoRecursion(String methodName) {
        return String.format("%s-method does not use recursion", methodName);
    }

    public static String testingPredicates(String fieldName) {
        return String.format("Testing class or field %s failed", fieldName);
    }

    public static String cannotCreateObject(String className) {
        return String.format("Cannot create an object of class %s", className);
    }
}
