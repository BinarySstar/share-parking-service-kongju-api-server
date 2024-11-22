package univ.goormthon.kongju.global.exception;

import univ.goormthon.kongju.global.exception.dto.ErrorCode;

public class InvalidDataException extends KongjuException {
    public InvalidDataException(ErrorCode errorCode) {
        super(errorCode);
    }
}