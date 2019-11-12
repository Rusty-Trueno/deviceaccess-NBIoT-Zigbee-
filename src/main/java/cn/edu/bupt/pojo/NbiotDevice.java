package cn.edu.bupt.pojo;

import lombok.Data;

/**
 * Created by rongshuai on 2019/11/12 13:01
 */
@Data
public class NbiotDevice {
    private String mac;
    private String name;
    private String deviceId;
    private Integer tenantId;
    private String manufacture;
    private String deviceType;
    private String model;
    private String parentDeviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getParentDeviceId() {
        return parentDeviceId;
    }

    public void setParentDeviceId(String parentDeviceId) {
        this.parentDeviceId = parentDeviceId;
    }

    public String toString(){
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"mac\":")
                .append("\""+mac+"\"");
        sb.append(",\"name\":\"")
                .append(name).append("\"");
        sb.append(",\"deviceId\":\"")
                .append(deviceId).append("\"");
        sb.append(",\"tenantId\":\"")
                .append(tenantId).append("\"");
        sb.append(",\"manufacture\":\"")
                .append(manufacture).append("\"");
        sb.append(",\"deviceType\":\"")
                .append(deviceType).append("\"");
        sb.append(",\"model\":\"")
                .append(model).append("\"");
        sb.append(",\"parentDeviceId\":\"")
                .append(parentDeviceId).append("\"");
        sb.append('}');
        return sb.toString();
    }
}
