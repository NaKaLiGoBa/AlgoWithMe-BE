package com.nakaligoba.backend.problem.application;

import com.nakaligoba.backend.member.domain.Member;
import com.nakaligoba.backend.member.domain.MemberRepository;
import com.nakaligoba.backend.problem.application.dto.CheckTestcaseResult;
import com.nakaligoba.backend.problem.application.usecase.CheckTestcasesUseCase;
import com.nakaligoba.backend.problem.application.usecase.SubmitUseCase;
import com.nakaligoba.backend.problem.domain.Problem;
import com.nakaligoba.backend.problem.exception.UserCodeCompileErrorException;
import com.nakaligoba.backend.problem.exception.UserCodeRuntimeErrorException;
import com.nakaligoba.backend.programminglanguage.domain.Language;
import com.nakaligoba.backend.submit.application.SubmitService;
import com.nakaligoba.backend.submit.domain.Result;
import com.nakaligoba.backend.testcase.application.TestcaseService;
import com.nakaligoba.backend.testcase.application.dto.InputDto;
import com.nakaligoba.backend.testcase.domain.Testcase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RunCodeService implements CheckTestcasesUseCase, SubmitUseCase {

    private final MemberRepository memberRepository;
    private final ProblemService problemService;
    private final TestcaseService testcaseService;
    private final SubmitService submitService;

    @Override
    public List<CheckTestcaseResult> checkTestcases(Long problemId, String language, String code) {
        Problem problem = problemService.getProblem(problemId)
                .orElseThrow(NoSuchElementException::new);
        Language programmingLanguage = Language.findByName(language)
                .orElse(Language.JAVA);
        //.orElseThrow(NoSuchElementException::new);

        File mainFile = createFile("Main", programmingLanguage, programmingLanguage.getMainCode());
        File solutionFile = createFile("Solution", programmingLanguage, code);
        try {

            compileIfNeeded(programmingLanguage);

            List<Testcase> testcases = problem.getTestcases()
                    .stream()
                    .filter(Testcase::isTesting)
                    .collect(Collectors.toList());

            String[] outputs = runSolution(programmingLanguage, testcases);

            List<CheckTestcaseResult> checkTestcaseResults = getCheckTestcaseResults(testcases, outputs);
            return checkTestcaseResults;
        } finally {
            cleanFiles(mainFile, solutionFile);
        }
    }

    @Override
    public boolean isAnswer(String memberEmail, Long problemId, String language, String code) {
        Member member = memberRepository.findByEmail(memberEmail);
        Problem problem = problemService.getProblem(problemId)
                .orElseThrow(NoSuchElementException::new);
        Language programmingLanguage = Language.findByName(language)
                .orElse(Language.JAVA);
        //.orElseThrow(NoSuchElementException::new);

        File mainFile = createFile("Main", programmingLanguage, programmingLanguage.getMainCode());
        File solutionFile = createFile("Solution", programmingLanguage, code);

        try {
            boolean isAnswer = true;

            try {
                compileIfNeeded(programmingLanguage);
            } catch (UserCodeCompileErrorException e) {
                submitService.save(code, Result.COMPILE_ERROR, problem, member);
                return false;
            }

            List<Testcase> answerCase = problem.getTestcases()
                    .stream()
                    .filter(Testcase::isGrading)
                    .collect(Collectors.toList());

            String[] outputs;

            try {
                outputs = runSolution(programmingLanguage, answerCase);
            } catch (UserCodeRuntimeErrorException e) {
                submitService.save(code, Result.RUNTIME_ERROR, problem, member);
                return false;
            }

            List<String> expecteds = answerCase.stream()
                    .map(Testcase::getOutput)
                    .collect(Collectors.toList());

            for (int i = 0; i < outputs.length; i++) {
                String output = outputs[i];
                String expected = expecteds.get(i);
                isAnswer &= output.equals(expected);
            }

            submitService.save(code, Result.isResolved(isAnswer), problem, member);

            return isAnswer;
        } finally {
            cleanFiles(mainFile, solutionFile);
        }

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

    private void compileIfNeeded(Language programmingLanguage) throws UserCodeCompileErrorException {
        Optional<String[]> compileCommand = programmingLanguage.getCompileCommand()
                .map(c -> c.split(" "));
        if (compileCommand.isEmpty()) {
            return;
        }

        compile(compileCommand.get());
    }

    private void compile(String... command) throws UserCodeCompileErrorException {
        ProcessBuilder pb = new ProcessBuilder(command);
        Process process = getProcess(pb);

        if (process.exitValue() != 0) {
            throw new UserCodeCompileErrorException(getResult(process.getInputStream()));
        }
    }

    private String[] runSolution(Language programmingLanguage, List<Testcase> testcases) {
        String[] runCommand = getRunCommand(programmingLanguage, testcases);
        return run(runCommand);
    }

    private String[] run(String... command) {
        ProcessBuilder pb = new ProcessBuilder(command);
        Process process = getProcess(pb);

        if (process.exitValue() != 0) {
            throw new UserCodeRuntimeErrorException(getResult(process.getInputStream()));
        }

        return getResult(process.getInputStream()).split(System.lineSeparator());
    }

    private String[] getRunCommand(Language programmingLanguage, List<Testcase> testcases) {
        String runCommand = programmingLanguage.getRunCommand();
        List<String> split = Arrays.asList(runCommand.split(" "));
        List<String> arguments = testcases
                .stream()
                .map(Testcase::getInputValues)
                .collect(Collectors.toList());
        List<String> runCommandAndArgs = new ArrayList<>();
        runCommandAndArgs.addAll(split);
        runCommandAndArgs.addAll(arguments);
        return runCommandAndArgs.toArray(String[]::new);
    }

    private static Process getProcess(ProcessBuilder pb) {
        pb.redirectErrorStream(true);
        Process process;
        try {
            process = pb.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return process;
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

    private static void cleanFiles(File mainFile, File solutionFile) {
        mainFile.delete();
        solutionFile.delete();
        new File("Main.class").delete();
        new File("Solution.class").delete();
    }
}
