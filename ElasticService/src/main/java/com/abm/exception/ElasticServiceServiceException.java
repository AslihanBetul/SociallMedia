package com.abm.exception;

import lombok.Getter;


@Getter
public class ElasticServiceServiceException extends RuntimeException {
    private ErrorType errorType;

    public ElasticServiceServiceException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public ElasticServiceServiceException(ErrorType errorType, String customMessage) {
        super(customMessage);
        this.errorType = errorType;
    }


}
