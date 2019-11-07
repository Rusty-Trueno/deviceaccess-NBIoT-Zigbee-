package cn.edu.bupt.dao.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by CZX on 2018/4/20.
 */
@Component
public class IOTErrorResponseHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException,
            ServletException {
        if (!response.isCommitted()) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            mapper.writeValue(response.getWriter(),
                    IOTErrorResponse.of("You don't have permission to perform this operation!",
                            IOTErrorCode.PERMISSION_DENIED, HttpStatus.FORBIDDEN));
        }
    }

    public void handle(Exception exception, HttpServletResponse response) {
        if (!response.isCommitted()) {
            try {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                if (exception instanceof IOTException) {
                    handleIOTException((IOTException) exception, response);
                } else if (exception instanceof AccessDeniedException) {
                    handleAccessDeniedException(response);
                } else if (exception instanceof AuthenticationException) {
                    handleAuthenticationException((AuthenticationException) exception, response);
                } else {
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    mapper.writeValue(response.getWriter(), IOTErrorResponse.of(exception.getMessage(),
                            IOTErrorCode.GENERAL, HttpStatus.INTERNAL_SERVER_ERROR));
                }
            } catch (IOException e) {
            }
        }
    }

    private void handleIOTException(IOTException iOTException, HttpServletResponse response) throws IOException {

        IOTErrorCode errorCode = iOTException.getErrorCode();
        HttpStatus status;

        switch (errorCode) {
            case AUTHENTICATION:
                status = HttpStatus.UNAUTHORIZED;
                break;
            case PERMISSION_DENIED:
                status = HttpStatus.FORBIDDEN;
                break;
            case INVALID_ARGUMENTS:
                status = HttpStatus.BAD_REQUEST;
                break;
            case ITEM_NOT_FOUND:
                status = HttpStatus.NOT_FOUND;
                break;
            case BAD_REQUEST_PARAMS:
                status = HttpStatus.BAD_REQUEST;
                break;
            case GENERAL:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }

        response.setStatus(status.value());
        mapper.writeValue(response.getWriter(), IOTErrorResponse.of(iOTException.getMessage(), errorCode, status));
    }

    private void handleAccessDeniedException(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        mapper.writeValue(response.getWriter(),
                IOTErrorResponse.of("You don't have permission to perform this operation!",
                        IOTErrorCode.PERMISSION_DENIED, HttpStatus.FORBIDDEN));

    }

    private void handleAuthenticationException(AuthenticationException authenticationException, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        if (authenticationException instanceof BadCredentialsException) {
            mapper.writeValue(response.getWriter(), IOTErrorResponse.of("Invalid username or password", IOTErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
        }
//        else if (authenticationException instanceof JwtExpiredTokenException) {
//            mapper.writeValue(response.getWriter(), IOTErrorResponse.of("Token has expired", IOTErrorCode.JWT_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED));
//        } else if (authenticationException instanceof AuthMethodNotSupportedException) {
//            mapper.writeValue(response.getWriter(), IOTErrorResponse.of(authenticationException.getMessage(), IOTErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
//        }
        mapper.writeValue(response.getWriter(), IOTErrorResponse.of("Authentication failed", IOTErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
    }
}
