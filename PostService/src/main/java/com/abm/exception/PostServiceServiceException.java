package com.abm.exception;

import lombok.Getter;


@Getter
public class PostServiceServiceException extends RuntimeException {
    private ErrorType errorType;

    public PostServiceServiceException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public PostServiceServiceException(ErrorType errorType, String customMessage) {
        super(customMessage);
        this.errorType = errorType;
    }


}
