package com.nakaligoba.backend.testcase.application;

import com.nakaligoba.backend.problem.domain.Problem;
import com.nakaligoba.backend.testcase.application.dto.InputDto;
import com.nakaligoba.backend.testcase.domain.Testcase;
import com.nakaligoba.backend.testcase.domain.TestcaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public List<String> getInputNames(Testcase testcase) {
        return Arrays.asList(testcase.getInputNames().split(INPUT_DELIMITER));
    }

    public List<InputDto> getInputs(Testcase testcase) {
        List<InputDto> inputs = new ArrayList<>();
        String[] inputNames = testcase.getInputNames().split(INPUT_DELIMITER);
        String[] inputValues = testcase.getInputValues().split(INPUT_DELIMITER);

        for (int i = 0; i < inputNames.length; i++) {
            String inputName = inputNames[i];
            String inputValue = inputValues[i];
            InputDto input = new InputDto(inputName, inputValue);
            inputs.add(input);
        }

        return inputs;
    }
}
