package com.nakaligoba.backend.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum MiniQuizType {
    CHOICE((ci) -> Arrays.stream(ci.split(",")).collect(Collectors.toList())),
    OX((ci) -> Collections.emptyList()),
    INITIAL((ci) -> Arrays.stream(ci.split(",")).collect(Collectors.toList()));

    private final Function<String, List<String>> mapToList;

    MiniQuizType(Function<String, List<String>> mapToList) {
        this.mapToList = mapToList;
    }

    public List<String> mapToList(String choiceOrInitials) {
        return mapToList.apply(choiceOrInitials);
    }
}
