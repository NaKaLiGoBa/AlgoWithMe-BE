package com.nakaligoba.backend.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public enum Language {

    JAVA(
            "java",
            "java",
            "javac Main.java Solution.java",
            "java Main",
            "class Solution {\n    public String solve(%s) {\n        String answer = \"\";\n        return answer;\n    }\n}",
            "String %s",
            "import java.lang.reflect.InvocationTargetException;\n" +
                    "import java.lang.reflect.Method;\n" +
                    "import java.util.Arrays;\n" +
                    "\n" +
                    "public class Main {\n" +
                    "    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {\n" +
                    "        StringBuilder sb = new StringBuilder();\n" +
                    "        Solution object = new Solution();\n" +
                    "        for (String arg : args) {\n" +
                    "            String[] split = arg.split(\" \");\n" +
                    "            Method[] methods = Solution.class.getMethods();\n" +
                    "            Method solveMethod = Arrays.stream(methods)\n" +
                    "                    .filter(m -> m.getName().equals(\"solve\"))\n" +
                    "                    .findAny()\n" +
                    "                    .orElseThrow(NoSuchMethodException::new);\n" +
                    "            Object output = solveMethod.invoke(object, (Object[]) split);\n" +
                    "            sb.append((String) output).append(\"\\n\");\n" +
                    "        }\n" +
                    "        System.out.println(sb);\n" +
                    "    }\n" +
                    "\n" +
                    "}"
            ),
    JAVASCRIPT(
            "javascript",
            "js",
            "",
            "node solution.js",
            "function solution(%s) {\n    var answer = 0;\n    return answer;\n}",
            "%s",
            ""
    )
    ;

    private final String name;
    private final String ext;

    private final String compileCommand;
    private final String runCommand;
    private final String defaultCodeFormat;
    private final String argumentFormat;
    private final String mainCode;

    Language(String name, String ext, String compileCommands, String runCommand, String defaultCodeBase, String argumentFormat, String mainCode) {
        this.name = name;
        this.ext = ext;
        this.compileCommand = compileCommands;
        this.runCommand = runCommand;
        this.defaultCodeFormat = defaultCodeBase;
        this.argumentFormat = argumentFormat;
        this.mainCode = mainCode;
    }

    private static final String ARGUMENT_DELIMITER = ", ";

    public static Optional<Language> findByName(String name) {
        return Arrays.stream(Language.values())
                .filter(l -> l.name.equals(name.toLowerCase()))
                .findAny();
    }

    public String getName() {
        return name;
    }

    public String getExt() {
        return ext;
    }

    public Optional<String> getCompileCommand() {
        if (Objects.isNull(compileCommand)) {
            return Optional.empty();
        }
        return Optional.of(compileCommand);
    }

    public String getRunCommand() {
        return runCommand;
    }

    public String getDefaultCode(String... args) {
        String parameter = Arrays.stream(args)
                .map(arg -> String.format(argumentFormat, arg))
                .collect(Collectors.joining(ARGUMENT_DELIMITER));
        return String.format(defaultCodeFormat, parameter);
    }

    public String getMainCode() {
        return mainCode;
    }
}
