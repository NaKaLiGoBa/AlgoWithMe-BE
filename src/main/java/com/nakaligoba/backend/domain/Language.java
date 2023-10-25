package com.nakaligoba.backend.domain;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum Language {

    JAVA("java", "javac %s.java && java %s", "java-runner"),
    PYTHON3("py", "python %s.py", "python3-runner"),
    NODE14("js", "node %s.js", "node14-runner")
    ;

    private final String ext;
    private final String commandFormat;
    private final String runner;

    Language(String ext, String commandFormat, String runner) {
        this.ext = ext;
        this.commandFormat = commandFormat;
        this.runner = runner;
    }

    public static Language getByExt(String ext) {
        return Arrays.stream(Language.values())
                .filter((language) -> ext.equals(language.ext))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }

    public String getCommand(String filename) {
        return commandFormat.replace("%s", filename);
    }

    public String getRunner() {
        return runner;
    }

    public String getExt() {
        return ext;
    }
}
