package cn.edu.bupt.common.security;


/**
 * Created by Administrator on 2018/4/13.
 */
public class DeviceAuthResult {

    private final boolean success;
    private final String deviceId;
    private final String errorMsg;

    public static DeviceAuthResult of(boolean success, String deviceId, String errormsg) {
        return new DeviceAuthResult(success, deviceId, errormsg);
    }

//   public static DeviceAuthResult of(String errorMsg) {
//        return new DeviceAuthResult(false, null, errorMsg);
//    }

    private DeviceAuthResult(boolean success, String deviceId, String errorMsg) {
        super();
        this.success = success;
        this.deviceId = deviceId;
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String toString() {
        return "DeviceAuthResult [success=" + success + ", deviceId=" + deviceId + ", errorMsg=" + errorMsg + "]";
    }
}

