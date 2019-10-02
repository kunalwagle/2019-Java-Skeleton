import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class TestRunner {

    private final String uuid = System.getenv("travis_uuid");

    protected List<TestCase> getTests(int questionNumber) {
        String url =
                "https://cscc-gl.herokuapp.com/tests/run/" + questionNumber;
        if (uuid != null) {
            url += "/" + uuid;
        }
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<TestCaseList> responseEntity =
                restTemplate.getForEntity(url,
                TestCaseList.class);
        return responseEntity.getBody().getTestCases();
    }

    protected void submitAnswers(List<Answer> answers, int questionNumber) {
        if (uuid != null) {
            String url =
                    "https://cscc-gl.herokuapp.com/answer/contestant/" + uuid +
                            "/" + questionNumber;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity(url, answers, Void.class);
        }
    }

}
