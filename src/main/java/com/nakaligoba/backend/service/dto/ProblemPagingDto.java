package com.nakaligoba.backend.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemPagingDto {

    private Long id;
    private Integer number;
    private String status;
    private String title;
    private BigDecimal acceptance;
    private String difficulty;
    private List<String> tags;
}
