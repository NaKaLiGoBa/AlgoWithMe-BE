package com.nakaligoba.backend.controller.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class CustomPageResponse<T> {
    private List<T> problems;
    private int totalPages;
    private long totalElements;
    private int number; // 현재 페이지
    private int size; // 페이지당 크기
    private int numberOfElements; // 현재 페이지의 요소 수
    private boolean first;
    private boolean last;

    public CustomPageResponse(Page<T> page) {
        this.problems = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.number = page.getNumber();
        this.size = page.getSize();
        this.numberOfElements = page.getNumberOfElements();
        this.first = page.isFirst();
        this.last = page.isLast();
    }
}
