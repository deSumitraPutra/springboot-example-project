package com.sum.springbootexample.common.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDTO {

    private String code;
    private String message;
    private Integer status;
    private LocalDateTime timestamp;
    private List<InvalidParameterDTO> invalidParameters;

}
