package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.domain.*;
import com.nakaligoba.backend.exception.UserCodeCompileErrorException;
import com.nakaligoba.backend.exception.UserCodeRuntimeErrorException;
import com.nakaligoba.backend.service.CheckTestcasesUseCase;
import com.nakaligoba.backend.service.SubmitUseCase;
import com.nakaligoba.backend.service.dto.CheckTestcaseResult;
import com.nakaligoba.backend.service.dto.InputDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RunCodeService implements CheckTestcasesUseCase, SubmitUseCase {

    private final MemberService memberService;
    private final ProblemService problemService;
    private final TestcaseService testcaseService;
    private final SubmitService submitService;

    @Override
    public List<CheckTestcaseResult> checkTestcases(Long problemId, String language, String code) {
        log.info("checkTestcases method call");
        Problem problem = problemService.getProblem(problemId);
        Language programmingLanguage = Language.findByName(language)
                .orElseThrow(EntityNotFoundException::new);

        File mainFile = null;
        File solutionFile = null;
        switch (programmingLanguage) {
            case JAVA:
                mainFile = createFile("Main", programmingLanguage, programmingLanguage.getMainCode());
                log.info("Create Main.java");

                solutionFile = createFile("Solution", programmingLanguage, code);
                log.info("Create Solution.java");
                break;
            case JAVASCRIPT:
                solutionFile = createFile("solution", programmingLanguage, code);
                log.info("Create solution.js");
                break;
            default:
                log.warn("There is no suitable process to run code: {}", programmingLanguage.getName());
                break;
        }

        try {
            log.info("Compile start");
            compileIfNeeded(programmingLanguage);
            log.info("Compile end");

            List<Testcase> testcases = problem.getTestcases()
                    .stream()
                    .filter(Testcase::isTesting)
                    .collect(Collectors.toList());

            log.info("Run code start");
            String[] outputs = runSolution(programmingLanguage, testcases);
            log.info("Run code end");

            return getCheckTestcaseResults(testcases, outputs);
        } finally {
            cleanFiles(mainFile, solutionFile);
        }
    }

    @Override
    public boolean isAnswer(String memberEmail, Long problemId, String language, String code) {
        Member member = memberService.getMemberByEmail(memberEmail);
        Problem problem = problemService.getProblem(problemId);
        Language programmingLanguage = Language.findByName(language)
                .orElseThrow(EntityNotFoundException::new);

        File mainFile = null;
        File solutionFile = null;
        switch (programmingLanguage) {
            case JAVA:
                mainFile = createFile("Main", programmingLanguage, programmingLanguage.getMainCode());
                log.info("Create Main.java");

                solutionFile = createFile("Solution", programmingLanguage, code);
                log.info("Create Solution.java");
                break;
            case JAVASCRIPT:
                solutionFile = createFile("solution", programmingLanguage, code);
                log.info("Create solution.js");
                break;
            default:
                log.warn("There is no suitable process to run code: {}", programmingLanguage.getName());
                break;
        }

        try {
            boolean isAnswer = true;

            try {
                log.info("Compile start");
                compileIfNeeded(programmingLanguage);
                log.info("Compile end");
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
                log.info("Run code start");
                outputs = runSolution(programmingLanguage, answerCase);
                log.info("Run code end");
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

            if (ArrayUtils.isEmpty(outputs)) {
                isAnswer = false;
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
        programmingLanguage.getCompileCommand()
                .map(c -> c.split(" "))
                .ifPresent(this::compile);
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

        if (ArrayUtils.isEmpty(outputs)) {
            for (int i = 0; i < expecteds.size(); i++) {
                List<InputDto> testcaseInput = testcaseInputs.get(i);
                String expected = expecteds.get(i);
                CheckTestcaseResult checkTestcaseResult = CheckTestcaseResult.builder()
                        .number(i + 1)
                        .isAnswer(false)
                        .inputs(testcaseInput)
                        .output("")
                        .expected(expected)
                        .build();
                checkTestcaseResults.add(checkTestcaseResult);
            }
        }

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
        if (mainFile != null) {
            mainFile.delete();
        }
        if (solutionFile != null) {
            solutionFile.delete();
        }
        //new File("Main.class").delete();
        //new File("Solution.class").delete();
    }
}
