package com.nakaligoba.backend.testcase.application;

import com.nakaligoba.backend.problem.domain.Problem;
import com.nakaligoba.backend.testcase.domain.Testcase;
import com.nakaligoba.backend.testcase.domain.TestcaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class TestcaseService {

    private static final String INPUT_DELIMITER = " ";
    private static final Integer ANSWER_CASE_NUMBER = 1;

    private final TestcaseRepository testcaseRepository;

    @Transactional
    public Testcase createTestcase(Integer number, List<String> parameters, String inputs, String output, Problem problem) {
        String inputNames = String.join(INPUT_DELIMITER, parameters);
        Testcase testcase = Testcase.builder()
                .number(number)
                .inputNames(inputNames)
                .inputValues(inputs)
                .output(output)
                .isGrading(false)
                .problem(problem)
                .build();
        return testcaseRepository.save(testcase);
    }

    @Transactional
    public Testcase createAnswerCase(List<String> parameters, String inputs, String output, Problem problem) {
        String inputNames = String.join(INPUT_DELIMITER, parameters);
        Testcase answerCase = Testcase.builder()
                .number(ANSWER_CASE_NUMBER)
                .inputNames(inputNames)
                .inputValues(inputs)
                .output(output)
                .isGrading(true)
                .problem(problem)
                .build();
        return testcaseRepository.save(answerCase);
    }


}
