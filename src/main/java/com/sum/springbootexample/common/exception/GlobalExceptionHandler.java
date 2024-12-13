package com.sum.springbootexample.common.exception;

import com.sum.springbootexample.common.exception.dto.ErrorResponseDTO;
import com.sum.springbootexample.common.exception.dto.InvalidParameterDTO;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.sum.springbootexample.common.exception.util.ErrorResponseUtil.build;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

/**
 * Global API exception handler responsible of catching any uncaught {@link Exception} and converting it into a prettier
 * JSON response.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Handles the uncaught {@link Exception} exceptions and returns a JSON formatted response.
     *
     * @param ex      the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleUncaughtException(final Exception ex, final ServletWebRequest request) {
        log(ex, request);
        final ErrorResponseDTO errorResponseDTO = build(Exception.class.getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDTO);
    }

    /**
     * Handles the uncaught {@link BusinessException} exceptions and returns a JSON formatted response.
     *
     * @param ex      the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<Object> handleCustomUncaughtBusinessException(final BusinessException ex, final ServletWebRequest request) {
        log(ex, request);
        final ErrorResponseDTO errorResponseDTO = build(ex.getSource(), ex.getMessage(), ex.getHttpStatus());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponseDTO);
    }

    /**
     * Handles the uncaught {@link ApplicationException} exceptions and returns a JSON formatted response.
     *
     * @param ex      the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<Object> handleCustomUncaughtApplicationException(final ApplicationException ex, final ServletWebRequest request) {
        log(ex, request);
        final ErrorResponseDTO errorResponseDTO = build(ex.getSource(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDTO);
    }

    /**
     * Handles the uncaught {@link ConstraintViolationException} exceptions and returns a JSON formatted response.
     *
     * @param ex      the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException ex,
                                                                     final ServletWebRequest request) {
        log(ex, request);
        final List<InvalidParameterDTO> invalidParameters = new ArrayList<>();
        ex.getConstraintViolations().forEach(constraintViolation -> {
            final Iterator<Path.Node> it = constraintViolation.getPropertyPath().iterator();
            if (it.hasNext()) {
                try {
                    it.next();
                    final Path.Node n = it.next();
                    final InvalidParameterDTO invalidParameter = new InvalidParameterDTO();
                    invalidParameter.setParameter(n.getName());
                    invalidParameter.setMessage(constraintViolation.getMessage());
                    invalidParameters.add(invalidParameter);
                } catch (final Exception e) {
                    log.warn("[Advocatus] Can't extract the information about constraint violation");
                }
            }
        });

        final ErrorResponseDTO errorResponseDTO = build(ConstraintViolationException.class.getSimpleName(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST, invalidParameters);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    /**
     * Handles the uncaught {@link HttpMessageNotReadableException} exceptions and returns a JSON formatted response.
     *
     * @param ex      the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
                                                                  final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log(ex, (ServletWebRequest) request);
        final ErrorResponseDTO errorResponseDTO = build(HttpMessageNotReadableException.class.getSimpleName(), ex.getMessage(),
                HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    /**
     * Handles the uncaught {@link HttpRequestMethodNotSupportedException} exceptions and returns a JSON formatted
     * response.
     *
     * @param ex      the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        log(ex, (ServletWebRequest) request);
        final ErrorResponseDTO errorResponseDTO = build(HttpRequestMethodNotSupportedException.class.getSimpleName(), ex.getMessage(),
                HttpStatus.METHOD_NOT_ALLOWED);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponseDTO);
    }

    /**
     * Handles the uncaught {@link MethodArgumentNotValidException} exceptions and returns a JSON formatted response.
     *
     * @param ex      the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        log(ex, (ServletWebRequest) request);
        final List<InvalidParameterDTO> invalidParameters = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> InvalidParameterDTO.builder()
                        .parameter(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build()).collect(toList());

        final ErrorResponseDTO errorResponseDTO = build(MethodArgumentNotValidException.class.getSimpleName(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST, invalidParameters);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    /**
     * Handles the uncaught {@link ServletRequestBindingException} exceptions and returns a JSON formatted response.
     *
     * @param ex      the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    protected ResponseEntity<Object> handleServletRequestBindingException(final ServletRequestBindingException ex,
                                                                          final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log(ex, (ServletWebRequest) request);

        final String missingParameter;
        final String missingParameterType;

        if (ex instanceof MissingRequestHeaderException) {
            missingParameter = ((MissingRequestHeaderException) ex).getHeaderName();
            missingParameterType = "header";
        } else if (ex instanceof MissingServletRequestParameterException) {
            missingParameter = ((MissingServletRequestParameterException) ex).getParameterName();
            missingParameterType = "query";
        } else if (ex instanceof MissingPathVariableException) {
            missingParameter = ((MissingPathVariableException) ex).getVariableName();
            missingParameterType = "path";
        } else {
            missingParameter = "unknown";
            missingParameterType = "unknown";
        }

        final InvalidParameterDTO missingParameterDTO = InvalidParameterDTO.builder()
                .parameter(missingParameter)
                .message(format("Missing %s parameter with name '%s'", missingParameterType, missingParameter))
                .build();

        final ErrorResponseDTO errorResponseDTO = build(
                ServletRequestBindingException.class.getSimpleName(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST,
                singletonList(missingParameterDTO));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    /**
     * Handles the uncaught {@link TypeMismatchException} exceptions and returns a JSON formatted response.
     *
     * @param ex      the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers,
                                                        final HttpStatus status, final WebRequest request) {
        log(ex, (ServletWebRequest) request);

        String parameter = ex.getPropertyName();
        if (ex instanceof MethodArgumentTypeMismatchException) {
            parameter = ((MethodArgumentTypeMismatchException) ex).getName();
        }

        final ErrorResponseDTO errorResponseDTO = build(TypeMismatchException.class.getSimpleName(),
                format("Unexpected type specified for '%s' parameter. Required '%s'", parameter, ex.getRequiredType()),
                HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    /**
     * Handles the uncaught {@link MissingPathVariableException} exceptions and returns a JSON formatted response.
     *
     * @param ex      the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    protected ResponseEntity<Object> handleMissingPathVariable(final MissingPathVariableException ex,
                                                               final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        return handleServletRequestBindingException(ex, headers, status, request);
    }

    /**
     * Handles the uncaught {@link MissingServletRequestParameterException} exceptions and returns a JSON formatted
     * response.
     *
     * @param ex      the ex
     * @param request the request on which the ex occurred
     * @return a JSON formatted response containing the ex details and additional fields
     */
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {
        log(ex, (ServletWebRequest) request);
        return handleServletRequestBindingException(ex, headers, status, request);
    }

    private void log(final Exception ex, final ServletWebRequest request) {
        Optional<HttpMethod> httpMethod = Optional.empty();
        Optional<String> requestUrl = Optional.empty();

        final Optional<ServletWebRequest> possibleIncomingNullRequest = Optional.ofNullable(request);
        if (possibleIncomingNullRequest.isPresent()) {
            ServletWebRequest servletWebRequest = possibleIncomingNullRequest.get();
            httpMethod = Optional.of(servletWebRequest.getHttpMethod());
            requestUrl = Optional.of(servletWebRequest.getRequest()).map(httpServletRequest -> httpServletRequest.getRequestURL().toString());
        }

        Object method = httpMethod.isPresent() ? httpMethod.get() : "'null'";
        String url = requestUrl.orElse("'null'");
        log.error("Request {} {} failed with exception reason: {}", method, url, ex.getMessage(), ex);
    }
}
