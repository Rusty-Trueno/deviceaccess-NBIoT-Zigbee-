package cn.edu.bupt.actor.actors.tenant;

/**
 * Created by Administrator on 2018/4/24.
 */
public class DeviceTerminationMsg {

    private final String deviceId;

    public DeviceTerminationMsg(String deviceId){
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
