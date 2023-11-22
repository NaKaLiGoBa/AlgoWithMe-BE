package com.nakaligoba.backend.controller.payload.response;

import com.nakaligoba.backend.service.dto.SubmitDto;
import lombok.Data;

import java.util.Collection;

@Data
public class SubmitsResponse {
    private final Integer totalCount;
    private final Collection<SubmitDto> submits;
}
