package cn.edu.bupt.pojo;

import java.util.UUID;

/**
 * Created by CZX on 2018/4/18.
 */
public class DeviceByGroupId {
    private UUID groupId;
    private UUID deviceId;

    public DeviceByGroupId(UUID groupId, UUID deviceId) {
        this.groupId = groupId;
        this.deviceId = deviceId;
    }

    public DeviceByGroupId(DeviceByGroupId deviceByGroupId) {
        this.groupId = deviceByGroupId.getGroupId();
        this.deviceId = deviceByGroupId.getDeviceId();
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceByGroupId that = (DeviceByGroupId) o;

        if (!groupId.equals(that.groupId)) return false;
        return deviceId.equals(that.deviceId);
    }

    @Override
    public int hashCode() {
        int result = groupId.hashCode();
        result = 31 * result + deviceId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"groupId\":")
                .append(groupId);
        sb.append(",\"deviceId\":")
                .append(deviceId);
        sb.append('}');
        return sb.toString();
    }
}
