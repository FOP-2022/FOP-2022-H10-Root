package h10;

import org.sourcegrade.docwatcher.api.grading.DocumentationCriterion;
import org.sourcegrade.docwatcher.api.grading.DocumentationGrader;
import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.Grader;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricForSubmission;
import org.sourcegrade.jagr.api.rubric.RubricProvider;
import org.sourcegrade.jagr.api.testing.RubricConfiguration;
import org.sourcegrade.jagr.api.testing.TestCycle;

/**
 * Specifies the criteria of the rubric.
 *
 * @author Arianne Roselina Prananto
 */
@RubricForSubmission("h10")
public final class H10_RubricProvider implements RubricProvider {

    /* *********************************************************************
     *                                H1                                   *
     **********************************************************************/

    public static final Criterion H1_1 = Criterion.builder()
        .shortDescription("Die Klassensignatur ist vollständig und korrekt.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H1.class.getMethod("testClassSignatures")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H1_2 = Criterion.builder()
        .shortDescription("Die Konstruktor ist vollständig und korrekt.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H1.class.getMethod("testConstructor")))
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H1.class.getMethod("testMessage")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H1 = Criterion.builder()
        .shortDescription("H1: Klasse MyLinkedListException")
        .maxPoints(2)
        .minPoints(0)
        .addChildCriteria(H1_1, H1_2)
        .build();

    /* *********************************************************************
     *                                H2.1                                 *
     **********************************************************************/

    public static final Criterion H2_1_1 = Criterion.builder()
        .shortDescription("Die extract*-Methoden existieren.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_1.class.getMethod("testExtractMethodsExist")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_1_2 = Criterion.builder()
        .shortDescription("Die Signatur der extractIteratively-Methode ist vollständig und korrekt.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_1.class
                    .getMethod("testExtractIterativelyMethodSignatures")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_1_3 = Criterion.builder()
        .shortDescription("Die Signatur der extractRecursively-Methode ist vollständig und korrekt.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_1.class
                    .getMethod("testExtractRecursivelyMethodSignatures")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_1_4 = Criterion.builder()
        .shortDescription("Die Signatur der extractHelper-Methode ist vollständig und korrekt.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_1.class.getMethod("testExtractHelperMethod")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_1_5_1 = Criterion.builder()
        .shortDescription("Die Quellliste in Methode extractIteratively ist beim Normalfall korrekt implementiert.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_1.class
                    .getMethod("testExtractIterativelySource")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_1_5_2 = Criterion.builder()
        .shortDescription("Die Zielliste in Methode extractIteratively ist beim Normalfall korrekt implementiert.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_1.class
                    .getMethod("testExtractIterativelyTarget")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_1_5_3 = Criterion.builder()
        .shortDescription("Die Quellliste in Methode extractIteratively ist beim Exceptionfall korrekt implementiert.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_1.class
                    .getMethod("testExtractIterativelyExceptionSource")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_1_5_4 = Criterion.builder()
        .shortDescription("Die Zielliste in Methode extractIteratively ist beim Exceptionfall korrekt implementiert.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_1.class
                    .getMethod("testExtractIterativelyExceptionTarget")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_1_5 = Criterion.builder()
        .shortDescription("H2.1.1: extractIteratively")
        .maxPoints(4)
        .minPoints(0)
        .addChildCriteria(H2_1_5_1, H2_1_5_2, H2_1_5_3, H2_1_5_4)
        .build();

    public static final Criterion H2_1_6_1 = Criterion.builder()
        .shortDescription("Die Quellliste in Methode extractRecursively ist beim Normalfall korrekt implementiert.")
        .maxPoints(2)
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_1.class
                    .getMethod("testExtractRecursivelySource")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_1_6_2 = Criterion.builder()
        .shortDescription("Die Zielliste in Methode extractRecursively ist beim Normalfall korrekt implementiert.")
        .maxPoints(2)
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_1.class
                    .getMethod("testExtractRecursivelyTarget")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_1_6_3 = Criterion.builder()
        .shortDescription("Die Quellliste in Methode extractRecursively ist beim Exceptionfall korrekt implementiert.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_1.class
                    .getMethod("testExtractRecursivelyExceptionSource")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_1_6_4 = Criterion.builder()
        .shortDescription("Die Zielliste in Methode extractRecursively ist beim Exceptionfall korrekt implementiert.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_1.class
                    .getMethod("testExtractRecursivelyExceptionTarget")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_1_6 = Criterion.builder()
        .shortDescription("H2.1.2: extractRecursively")
        .maxPoints(6)
        .minPoints(0)
        .addChildCriteria(H2_1_6_1, H2_1_6_2, H2_1_6_3, H2_1_6_4)
        .build();

    public static final Criterion H2_1_7 = Criterion.builder()
        .shortDescription("extract*-Methoden verwenden keine nicht bereits implementierten methoden.")
        .maxPoints(0)
        .minPoints(-1)
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_1.class.getMethod("testExtractNoOtherMethods",
                                                                                        TestCycle.class)))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_1_8 = Criterion.builder()
        .shortDescription("extractIteratively hat genau eine Schleife und keine Rekursion.")
        .maxPoints(0)
        .minPoints(-2)
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_1.class.getMethod("testExtractReallyIteratively",
                                                                                        TestCycle.class)))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_1_9 = Criterion.builder()
        .shortDescription("extractRecursively benutzt Rekursion und hat keine Schleife.")
        .maxPoints(0)
        .minPoints(-2)
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_1.class.getMethod("testExtractReallyRecursively",
                                                                                        TestCycle.class)))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_1 = Criterion.builder()
        .shortDescription("H2.1: extract*-Methoden")
        .maxPoints(14)
        .minPoints(0)
        .addChildCriteria(H2_1_1, H2_1_2, H2_1_3, H2_1_4, H2_1_5, H2_1_6, H2_1_7, H2_1_8, H2_1_9)
        .build();

    /* *********************************************************************
     *                                H2.2                                 *
     **********************************************************************/

    public static final Criterion H2_2_1 = Criterion.builder()
        .shortDescription("Die mixin*-Methoden existieren.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_2.class.getMethod("testMixinMethodsExist")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_2_2 = Criterion.builder()
        .shortDescription("Die Signatur der mixinIteratively-Methode ist vollständig und korrekt.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_2.class.getMethod("testMixinIterativelyMethodSignatures")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_2_3 = Criterion.builder()
        .shortDescription("Die Signatur der mixinRecursively-Methode ist vollständig und korrekt.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_2.class.getMethod("testMixinRecursivelyMethodSignatures")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_2_4 = Criterion.builder()
        .shortDescription("Die Signatur der mixinHelper-Methode ist vollständig und korrekt.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_2.class.getMethod("testMixinHelperMethod")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_2_5_1 = Criterion.builder()
        .shortDescription("Die Quellliste in Methode mixinIteratively ist beim Normalfall korrekt implementiert.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_2.class
                    .getMethod("testMixinIterativelySource")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_2_5_2 = Criterion.builder()
        .shortDescription("Die Zielliste in Methode mixinIteratively ist beim Normalfall korrekt implementiert.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_2.class
                    .getMethod("testMixinIterativelyTarget")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_2_5_3 = Criterion.builder()
        .shortDescription("Die Quellliste in Methode mixinIteratively ist beim Exceptionfall korrekt implementiert.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_2.class
                    .getMethod("testMixinIterativelyExceptionSource")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_2_5_4 = Criterion.builder()
        .shortDescription("Die Zielliste in Methode mixinIteratively ist beim Exceptionfall korrekt implementiert.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_2.class
                    .getMethod("testMixinIterativelyExceptionTarget")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_2_5 = Criterion.builder()
        .shortDescription("H2.2.1: mixinIteratively")
        .maxPoints(4)
        .minPoints(0)
        .addChildCriteria(H2_2_5_1, H2_2_5_2, H2_2_5_3, H2_2_5_4)
        .build();

    public static final Criterion H2_2_6_1 = Criterion.builder()
        .shortDescription("Die Quellliste in Methode mixinRecursively ist beim Normalfall korrekt implementiert.")
        .maxPoints(2)
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_2.class
                    .getMethod("testMixinRecursivelySource")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_2_6_2 = Criterion.builder()
        .shortDescription("Die Zielliste in Methode mixinRecursively ist beim Normalfall korrekt implementiert.")
        .maxPoints(2)
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_2.class
                    .getMethod("testMixinRecursivelyTarget")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_2_6_3 = Criterion.builder()
        .shortDescription("Die Quellliste in Methode mixinRecursively ist beim Exceptionfall korrekt implementiert.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_2.class
                    .getMethod("testMixinRecursivelyExceptionSource")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_2_6_4 = Criterion.builder()
        .shortDescription("Die Zielliste in Methode mixinRecursively ist beim Exceptionfall korrekt implementiert.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_2.class
                    .getMethod("testMixinRecursivelyExceptionTarget")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_2_6 = Criterion.builder()
        .shortDescription("H2.2.2: mixinRecursively")
        .maxPoints(6)
        .minPoints(0)
        .addChildCriteria(H2_2_6_1, H2_2_6_2, H2_2_6_3, H2_2_6_4)
        .build();

    public static final Criterion H2_2_7 = Criterion.builder()
        .shortDescription("mixin*-Methoden verwenden keine nicht bereits implementierten methoden.")
        .maxPoints(0)
        .minPoints(-1)
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_2.class.getMethod("testMixinNoOtherMethods",
                                                                                        TestCycle.class)))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_2_8 = Criterion.builder()
        .shortDescription("mixinIteratively hat genau eine Schleife und keine Rekursion.")
        .maxPoints(0)
        .minPoints(-2)
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_2.class.getMethod("testMixinReallyIteratively",
                                                                                        TestCycle.class)))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_2_9 = Criterion.builder()
        .shortDescription("mixinRecursively benutzt Rekursion und hat keine Schleife.")
        .maxPoints(0)
        .minPoints(-2)
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H2_2.class.getMethod("testMixinReallyRecursively",
                                                                                        TestCycle.class)))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H2_2 = Criterion.builder()
        .shortDescription("H2.2: mixin*-Methoden")
        .maxPoints(14)
        .minPoints(0)
        .addChildCriteria(H2_2_1, H2_2_2, H2_2_3, H2_2_4, H2_2_5, H2_2_6, H2_2_7, H2_2_8, H2_2_9)
        .build();

    /* *********************************************************************
     *                                 H2                                  *
     **********************************************************************/

    public static final Criterion H2 = Criterion.builder()
        .shortDescription("H2: extract*- und mixin*-Methoden")
        .maxPoints(28)
        .minPoints(0)
        .addChildCriteria(H2_1, H2_2)
        .build();

    /* *********************************************************************
     *                                H3.1                                 *
     **********************************************************************/

    public static final Criterion H3_1_1 = Criterion.builder()
        .shortDescription("TestMyLinkedList Klasse und testExtract Methode korrekt existieren.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H3.class.getMethod("testExtractTestSignatures")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H3_1_2 = Criterion.builder()
        .shortDescription("Die drei Klassen für die Parameters existieren und sind korrekt implementiert.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H3.class.getMethod("testParameterClasses",
                                                                                      TestCycle.class)))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H3_1_3 = Criterion.builder()
        .shortDescription("TestExtract testet extract*-Methoden richtig.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H3.class.getMethod("testExtractTest")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H3_1 = Criterion.builder()
        .shortDescription("H3.1: extract*-Tests")
        .maxPoints(3)
        .minPoints(0)
        .addChildCriteria(H3_1_1, H3_1_2, H3_1_3)
        .build();

    /* *********************************************************************
     *                                H3.2                                 *
     **********************************************************************/

    public static final Criterion H3_2_1 = Criterion.builder()
        .shortDescription("TestMyLinkedList Klasse und testMixin Methode korrekt existieren.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H3.class.getMethod("testMixinTestSignatures")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H3_2_2 = Criterion.builder()
        .shortDescription("Die Konstanten werden richtig durch lambda-Ausdrücke eingesetzt.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H3.class.getMethod("testParameterConstants",
                                                                                      TestCycle.class)))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H3_2_3 = Criterion.builder()
        .shortDescription("TestMixin testet mixin*-Methoden richtig.")
        .grader(
            Grader.testAwareBuilder()
                .requirePass(JUnitTestRef.ofMethod(() -> TutorTest_H3.class.getMethod("testMixinTest")))
                .pointsPassedMax()
                .pointsFailedMin()
                .build())
        .build();

    public static final Criterion H3_2 = Criterion.builder()
        .shortDescription("H3.2: mixin*-Tests")
        .maxPoints(3)
        .minPoints(0)
        .addChildCriteria(H3_2_1, H3_2_2, H3_2_3)
        .build();

    /* *********************************************************************
     *                                 H3                                  *
     **********************************************************************/

    public static final Criterion H3 = Criterion.builder()
        .shortDescription("H3: extract*- und mixin*-Tests")
        .maxPoints(6)
        .minPoints(0)
        .addChildCriteria(H3_1, H3_2)
        .build();

    /* *********************************************************************
     *                              JavaDoc                                 *
     **********************************************************************/

    //public static Criterion JAVADOC;

    static {
        try {
            Class.forName("org.sourcegrade.docwatcher.DocWatcherModule");
        } catch (ClassNotFoundException e) {
            // ignore
        }

        // TODO : javaDoc
        /*JAVADOC = DocumentationCriterion.forGrader(
            DocumentationGrader.builder()
                .addJavaDoc(TutorTest_H1::getConstructorDocumentation, H1_2)
                .addJavaDoc(TutorTest_H2_1::getMethodDocumentation, H2_1)
                .addJavaDoc(TutorTest_H2_2::getMethodDocumentation, H2_1)
                .addJavaDoc(
                    new MethodTester(TutorClassTesters.H1_1, TutorConstants.H1_1_METHOD_NAME_2)::getMethodDocumentation, H1_1
                           )
                .addJavaDoc(
                    new MethodTester(TutorClassTesters.H1_1, TutorConstants.H1_1_METHOD_NAME_3)::getMethodDocumentation, H1_1
                           )
                .build());*/
    }

    /* *********************************************************************
     *                              Rubric                                 *
     **********************************************************************/

    public static final Rubric RUBRIC = Rubric.builder()
        .title("h10")
        .addChildCriteria(H1, H2, H3)
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }

    @Override
    public void configure(final RubricConfiguration configuration) {
        configuration.addTransformer(Global.TRANSFORMER);
    }
}
