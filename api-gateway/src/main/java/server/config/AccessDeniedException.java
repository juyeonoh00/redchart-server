package server.config;

import org.springframework.security.core.AuthenticationException;

public class AccessDeniedException extends AuthenticationException {
    public AccessDeniedException(String msg) {
        super(msg);
    }
}