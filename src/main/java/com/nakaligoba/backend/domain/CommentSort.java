package com.nakaligoba.backend.domain;

public enum CommentSort {
    RECENT(1, "recent"), // 최신 순
    LIKE(2, "like"); // 좋아요 순

    private final int id;
    private final String name;

    CommentSort(int id, String name) {
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
