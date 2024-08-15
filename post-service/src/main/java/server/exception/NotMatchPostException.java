package server.exception;

import server.exception.common.BusinessException;
import server.exception.common.ErrorCode;

public class NotMatchPostException extends BusinessException {
    public NotMatchPostException() {
        super(ErrorCode.MATCH_POST_EXCEPTION);
    }
}
