package cn.edu.bupt.message;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

public class BasicFromServerMsg {
    @Getter
    private final String tenantId;
    @Getter
    private final List<String> deviceIds;
    @Getter
    private final DeferredResult<ResponseEntity> res;

    public BasicFromServerMsg(String tenantId,List<String> deviceIds, DeferredResult<ResponseEntity> res)
    {
        this.tenantId = tenantId;
        this.deviceIds = deviceIds;
        this.res = res;
    }
}
