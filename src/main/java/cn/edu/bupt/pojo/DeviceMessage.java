package cn.edu.bupt.pojo;

import lombok.Data;

/**
 * Created by rongshuai on 2019/11/5 20:21
 */
@Data
public class DeviceMessage {
    private int id;
    private String deviceId;
    private String phoneNumber;

    public DeviceMessage(Integer id,String deviceId, String phoneNumber){
        this.id = id;
        this.deviceId = deviceId;
        this.phoneNumber = phoneNumber;
    }

    public DeviceMessage(String deviceId, String phoneNumber) {
        this.deviceId = deviceId;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
