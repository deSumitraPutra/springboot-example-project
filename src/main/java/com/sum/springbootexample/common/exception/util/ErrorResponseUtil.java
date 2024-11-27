package com.sum.springbootexample.common.exception.util;

import com.sum.springbootexample.common.exception.dto.ErrorResponseDTO;
import com.sum.springbootexample.common.exception.dto.InvalidParameterDTO;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public final class ErrorResponseUtil {
    private ErrorResponseUtil() {
    }

    public static ErrorResponseDTO build(final String code, final String message, final HttpStatus status) {
        return buildDetails(code, message, status);
    }

    public static ErrorResponseDTO build(
        final String code,
        final String message,
        final HttpStatus status,
        final List<InvalidParameterDTO> invalidParameters
    ) {
        return buildDetails(code, message, status, invalidParameters);
    }


    private static ErrorResponseDTO buildDetails(final String code, final String message, final HttpStatus status) {
        return buildDetails(code, message, status, null);
    }

    private static ErrorResponseDTO buildDetails(
        final String code,
        final String message,
        final HttpStatus status,
        final List<InvalidParameterDTO> invalidParameters
    ) {
        final ErrorResponseDTO errorResponseDetails = ErrorResponseDTO.builder()
            .code(code)
            .message(message)
            .timestamp(LocalDateTime.now())
            .build();

        if (!Objects.isNull(status)) {
            errorResponseDetails.setStatus(status.value());
        }
        if (!CollectionUtils.isEmpty(invalidParameters)) {
            errorResponseDetails.setInvalidParameters(invalidParameters);
        }
        return errorResponseDetails;
    }

}
