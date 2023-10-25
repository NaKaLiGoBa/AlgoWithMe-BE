package com.nakaligoba.backend.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProjectFile {

    private final String name;
    private final String directory;
    private final Language language;

    @Builder
    public ProjectFile(String name, String directory, Language language) {
        this.name = name;
        this.directory = directory;
        this.language = language;
    }

    public String getCommand() {
        return language.getCommand(name);
    }

    public String getRunner() {
        return language.getRunner();
    }
}
