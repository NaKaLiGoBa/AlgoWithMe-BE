package com.nakaligoba.backend.problem.application;

import com.nakaligoba.backend.availablelanguage.application.AvailableLanguageService;
import com.nakaligoba.backend.problem.application.dto.CheckTestcaseResult;
import com.nakaligoba.backend.problem.application.usecase.CheckTestcasesUseCase;
import com.nakaligoba.backend.problem.controller.dto.CreateProblemRequest;
import com.nakaligoba.backend.problem.domain.Problem;
import com.nakaligoba.backend.problemtag.application.ProblemTagService;
import com.nakaligoba.backend.programminglanguage.application.ProgrammingLanguageService;
import com.nakaligoba.backend.programminglanguage.domain.Language;
import com.nakaligoba.backend.programminglanguage.domain.ProgrammingLanguage;
import com.nakaligoba.backend.tag.application.TagService;
import com.nakaligoba.backend.tag.domain.Tag;
import com.nakaligoba.backend.testcase.application.TestcaseService;
import com.nakaligoba.backend.testcase.application.dto.InputDto;
import com.nakaligoba.backend.testcase.domain.Testcase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemFacade implements CheckTestcasesUseCase {

    private final ProblemService problemService;
    private final TestcaseService testcaseService;
    private final TagService tagService;
    private final ProblemTagService problemTagService;
    private final ProgrammingLanguageService programmingLanguageService;
    private final AvailableLanguageService availableLanguageService;

    @Transactional
    public Long createProblem(CreateProblemRequest dto) {
        Problem problem = problemService.createProblem(dto.getTitle(), dto.getDescription(), dto.getDifficulty());
        setTestcases(dto.getTestcases(), dto.getParameters(), problem);
        setAnswerCases(dto.getAnswerCases(), dto.getParameters(), problem);
        setTags(dto.getTags(), problem);
        setProgrammingLanguages(problem);
        return problem.getId();
    }

    private void setTestcases(String testcases, List<String> parameters, Problem problem) {
        String[] testInputsAndOutputs = testcases.split("\n");
        for (int i = 0; i < testInputsAndOutputs.length; i += 2) {
            String inputs = testInputsAndOutputs[i];
            String output = testInputsAndOutputs[i + 1];
            int number = i / 2 + 1;
            Testcase testcase = testcaseService.createTestcase(number, parameters, inputs, output, problem);
            problem.addTestcase(testcase);
        }
    }

    private void setAnswerCases(String answerCases, List<String> parameters, Problem problem) {
        String[] answerInputsAndOutputs = answerCases.split("\n");
        for (int i = 0; i < answerInputsAndOutputs.length; i += 2) {
            String inputs = answerInputsAndOutputs[i];
            String output = answerInputsAndOutputs[i + 1];
            Testcase answerCase = testcaseService.createAnswerCase(parameters, inputs, output, problem);
            problem.addTestcase(answerCase);
        }
    }

    private void setTags(List<String> tags, Problem problem) {
        for (String name : tags) {
            Tag tag = tagService.findOrCreateTag(name);
            problem.addProblemTag(tag);
            problemTagService.tag(problem, tag);
        }
    }

    private void setProgrammingLanguages(Problem problem) {
        List<ProgrammingLanguage> programmingLanguages = programmingLanguageService.findAll();
        programmingLanguages.forEach(problem::addAvailableLanguage);
        programmingLanguages.forEach(p -> availableLanguageService.avail(problem, p));
    }

    @Override
    public List<CheckTestcaseResult> checkTestcases(Long problemId, String language, String code) {

        // ToDo 현재는 Java만 지원
        Language programmingLanguage = Language.findByName(language)
                .orElse(Language.JAVA);
        //.orElseThrow(NoSuchElementException::new);

        File mainFile = createFile("Main", programmingLanguage, programmingLanguage.getMainCode());
        File solutionFile = createFile("Solution", programmingLanguage, code);

        compileIfNeeded(programmingLanguage);

        List<Testcase> testcases = problemService.getProblem(problemId)
                .orElseThrow(NoSuchElementException::new)
                .getTestcases();

        String[] outputs = runSolution(programmingLanguage, testcases);

        List<CheckTestcaseResult> checkTestcaseResults = getCheckTestcaseResults(testcases, outputs);

        mainFile.delete();
        solutionFile.delete();
        new File("Main.class").delete();
        new File("Solution.class").delete();

        return checkTestcaseResults;
    }


    private File createFile(String fileName, Language programmingLanguage, String code) {
        File solutionFile = new File(fileName + "." + programmingLanguage.getExt());
        try (FileWriter writer = new FileWriter(solutionFile)) {
            writer.write(code);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return solutionFile;
    }

    private void compileIfNeeded(Language programmingLanguage) {
        programmingLanguage.getCompileCommand()
                .map(c -> c.split(" "))
                .ifPresent(this::runProcess);
    }

    private String[] runSolution(Language programmingLanguage, List<Testcase> testcases) {
        List<String> runCommand = getRunCommand(programmingLanguage, testcases);
        return runProcess(runCommand).split(System.lineSeparator());
    }


    private List<String> getRunCommand(Language programmingLanguage, List<Testcase> testcases) {
        String runCommand = programmingLanguage.getRunCommand();
        List<String> split = Arrays.asList(runCommand.split(" "));
        List<String> arguments = testcases
                .stream()
                .filter(tc -> !tc.getIsGrading())
                .map(Testcase::getInputValues)
                .collect(Collectors.toList());
        List<String> runCommandAndArgs = new ArrayList<>();
        runCommandAndArgs.addAll(split);
        runCommandAndArgs.addAll(arguments);
        return runCommandAndArgs;
    }

    private String runProcess(List<String> command) {
        ProcessBuilder pb = new ProcessBuilder(command);
        log.info("command: {}", pb.command());
        return runProcess(pb);
    }

    private String runProcess(String... command) {
        ProcessBuilder pb = new ProcessBuilder(command);
        return runProcess(pb);
    }

    private String runProcess(ProcessBuilder pb) {
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            process.waitFor();
            return getResult(process.getInputStream());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getResult(InputStream inputStream) {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            while (true) {
                String line;
                try {
                    line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    result.append(line).append(System.lineSeparator());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("result: {}", result);
        return result.toString();
    }


    private List<CheckTestcaseResult> getCheckTestcaseResults(List<Testcase> testcases, String[] outputs) {
        List<CheckTestcaseResult> checkTestcaseResults = new ArrayList<>();
        List<String> expecteds = testcases.stream()
                .map(Testcase::getOutput)
                .collect(Collectors.toList());

        List<List<InputDto>> testcaseInputs = testcases.stream()
                .map(testcaseService::getInputs)
                .collect(Collectors.toList());
        for (int i = 0; i < outputs.length; i++) {
            List<InputDto> testcaseInput = testcaseInputs.get(i);
            String output = outputs[i];
            String expected = expecteds.get(i);
            boolean isAnswer = output.equals(expected);
            CheckTestcaseResult checkTestcaseResult = CheckTestcaseResult.builder()
                    .number(i + 1)
                    .isAnswer(isAnswer)
                    .inputs(testcaseInput)
                    .output(output)
                    .expected(expected)
                    .build();
            checkTestcaseResults.add(checkTestcaseResult);
        }
        return checkTestcaseResults;
    }
}
