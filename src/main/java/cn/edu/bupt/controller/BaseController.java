package cn.edu.bupt.controller;

import cn.edu.bupt.actor.service.DefaultActorService;
import cn.edu.bupt.dao.exception.*;
//import cn.edu.bupt.security.model.SecurityUser;
import cn.edu.bupt.service.*;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class BaseController {

    @Autowired
    private IOTErrorResponseHandler errorResponseHandler;

    @Autowired
    protected DeviceService deviceService;

    @Autowired
    protected GroupService groupService;

    @Autowired
    DefaultActorService actorService;

    @Autowired
    BaseAttributesService baseAttributesService;

    @Autowired
    BaseTimeseriesService baseTimeseriesService;

    @Autowired
    DeviceCredentialsService deviceCredentialsService;

    @Autowired
    BaseEventService baseEventService;

    @ExceptionHandler(IOTException.class)
    public void handleIOTException(IOTException ex, HttpServletResponse response) {
        errorResponseHandler.handle(ex, response);
    }

    IOTException handleException(Exception exception) {
        return handleException(exception, true);
    }

    private IOTException handleException(Exception exception, boolean logException) {
        if (logException) {
            log.error("Error [{}]", exception.getMessage());
        }

        String cause = "";
        if (exception.getCause() != null) {
            cause = exception.getCause().getClass().getCanonicalName();
        }

        if (exception instanceof IOTException) {
            return (IOTException) exception;
        } else if (exception instanceof IllegalArgumentException || exception instanceof IncorrectParameterException
                || exception instanceof DataValidationException || cause.contains("IncorrectParameterException")) {
            return new IOTException(exception.getMessage(), IOTErrorCode.BAD_REQUEST_PARAMS);
        }  else {
            return new IOTException(exception.getMessage(), IOTErrorCode.GENERAL);
        }
    }

    /*protected SecurityUser getCurrentUser() throws IOTException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (SecurityUser) authentication.getPrincipal();
        } else {
            throw new IOTException("You aren't authorized to perform this operation!", IOTErrorCode.AUTHENTICATION);
        }
    }*/

    UUID toUUID(String id) {
        if(id==null) {
            return null;
        }else {
            return UUID.fromString(id);
        }
    }


    <T> T checkNotNull(T reference) throws Exception{
        if (reference == null) {
            throw new Exception("Requested item wasn't found!");
        }
        return reference;
    }

    void checkParameter(String name, String param) throws Exception {
        if (StringUtils.isEmpty(param)) {
            throw new Exception("Parameter '" + name + "' can't be empty!");
        }
    }

    public String onFail(Exception exception) {
        return this.onFail(exception.toString()) ;
    }

    public String onFail(String msg) {
        JsonObject InfoJson = new JsonObject() ;
//        InfoJson.addProperty("id", "");
//        InfoJson.addProperty("response_code", 1);
        InfoJson.addProperty("response_msg", msg);
//        InfoJson.addProperty("info", "名称重复，创建失败");
        return InfoJson.toString() ;
    }

}
