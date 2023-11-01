package com.nakaligoba.backend.problem.controller.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@Builder
public class CustomPageResponse<T> {
    private List<T> problems;
    private int pageNumber;     // 현재 페이지
    private int totalPages;     // 모든 페이지의 수
    private long totalElements; // content의 총 개수
    private int size;           // 페이지당 크기
    private int numberOfElements; // 현재 페이지의 요소 수
    private boolean first;
    private boolean last;

    public CustomPageResponse(List<T> problems, int pageNumber, int totalPages, long totalElements, int size, int numberOfElements, boolean first, boolean last) {
        this.problems = problems;
        this.pageNumber = pageNumber;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.numberOfElements = numberOfElements;
        this.first = first;
        this.last = last;
    }
}
