import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import skeleton.answers.Question4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
public class Q4 {

    private List<TestCase> testCases;
    private List<Answer> answers = new ArrayList<>();
    private TestRunner testRunner = new TestRunner();
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Before
    public void setUp() throws Exception {
        testCases = testRunner.getTests(4);
    }

    @After
    public void tearDown() throws Exception {
        testRunner.submitAnswers(answers, 4);
    }

    @Test
    public void testQuestion4() {
        for (int i=1; i<=testCases.size(); i++) {
            try {
                TestCase testCase = testCases.get(i-1);
                int finalI = i;
                Future<Answer> future = executorService.submit(() -> {
                    Q4Object input =
                            new ObjectMapper().readValue(testCase.getInput(),
                                    Q4Object.class);
                    long startTime = System.nanoTime();
                    Integer answer = Question4.choosingWisely(input.v,
                            input.c, input.vc);
                    long endTime = System.nanoTime();
                    return new Answer(4, finalI, correct(answer,
                            testCase.getOutput()),
                            endTime-startTime);
                });

                Answer answer = future.get(1, TimeUnit.SECONDS);
                answers.add(answer);

            } catch (TimeoutException e) {
                System.out.println("A Question 4 test timed out. Tests must " +
                        "complete within one second.");
                answers.add(new Answer(4, i, "TIMED_OUT", -1));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private String correct(int actual, int expected) {
        return actual == expected ? "CORRECT" : "INCORRECT";
    }

    private class Q4Object {
        int[] v;
        int[] c;
        int vc;

        public Q4Object() {
        }

        public int[] getV() {
            return v;
        }

        public void setV(int[] v) {
            this.v = v;
        }

        public int[] getC() {
            return c;
        }

        public void setC(int[] c) {
            this.c = c;
        }

        public int getVc() {
            return vc;
        }

        public void setVc(int vc) {
            this.vc = vc;
        }
    }


}
