package com.project.repositoryscoring.exception.handling;

import com.project.repositoryscoring.exception.GithubApiClientException;
import com.project.repositoryscoring.exception.GithubApiServerException;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.MissingRequestValueException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(MissingRequestValueException.class)
    public ProblemDetail handleMissingRequestValueException(MissingRequestValueException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create("https://website.com/problem-definitions/validation-error"));

        return problemDetail;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail handleMethodValidationException(HandlerMethodValidationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create("https://website.com/problem-definitions/validation-error"));

        return problemDetail;
    }

    @ExceptionHandler(GithubApiClientException.class)
    public ProblemDetail handle(GithubApiClientException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create("https://website.com/problem-definitions/github-api-error"));

        return problemDetail;
    }

    @ExceptionHandler({Exception.class, GithubApiServerException.class})
    public ProblemDetail handle(Exception ex) {
        log.error(ex.getMessage(), ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        problemDetail.setType(URI.create("https://website.com/support"));
        return problemDetail;
    }
}
