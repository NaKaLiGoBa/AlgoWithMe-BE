package com.nakaligoba.backend.programminglanguage.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum Language {

    JAVA(
            "java",
            List.of("javac Solution.java", "java Solution"),
            "class Solution {\n    public String solve(%s) {\n        String answer = \"\";\n        return answer;\n    }\n}",
            "String %s");

    private final String name;
    private final List<String> runCommand;
    private final String defaultCodeFormat;
    private final String argumentFormat;

    Language(String name, List<String> runCommand, String defaultCodeBase, String argumentFormat) {
        this.name = name;
        this.runCommand = runCommand;
        this.defaultCodeFormat = defaultCodeBase;
        this.argumentFormat = argumentFormat;
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

    public String getDefaultCode(String... args) {
        String parameter = Arrays.stream(args)
                .map(arg -> String.format(argumentFormat, arg))
                .collect(Collectors.joining(ARGUMENT_DELIMITER));
        return String.format(defaultCodeFormat, parameter);
    }
}
