package com.nakaligoba.backend.domain;

public enum SolutionSort {
    HOT(1, "hot"), // 조회 수 순
    RECENT(2, "recent"), // 최신 순
    LIKE(3, "like"); // 좋아요 순

    private final int id;
    private final String name;

    SolutionSort(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
