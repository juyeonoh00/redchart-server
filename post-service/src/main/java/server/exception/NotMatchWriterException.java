package server.exception;

import server.exception.common.BusinessException;
import server.exception.common.ErrorCode;

public class NotMatchWriterException extends BusinessException {
    public NotMatchWriterException() {
        super(ErrorCode.MATCH_USER_EXCEPTION);
    }
}
