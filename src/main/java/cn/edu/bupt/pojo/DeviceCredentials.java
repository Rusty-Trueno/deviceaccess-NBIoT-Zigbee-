package cn.edu.bupt.pojo;

import cn.edu.bupt.dao.BaseEntity;
import cn.edu.bupt.dao.SearchTextBased;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

import static cn.edu.bupt.dao.ModelConstants.*;

/**
 * Created by CZX on 2018/4/17.
 */
@Table(name = DEVICE_CREDENTIALS_COLUMN_FAMILY_NAME)
public class DeviceCredentials extends SearchTextBased implements BaseEntity {

    @PartitionKey(value = 0)
    @Column(name = ID_PROPERTY)
    private UUID id;

    @Column(name = DEVICE_CREDENTIALS_DEVICE_ID_PROPERTY)
    private UUID deviceId;

    @Column(name = DEVICE_CREDENTIALS_TOKEN_PROPERTY )
    private String deviceToken;

    @Column(name  = DEVICE_CREDENTIALS_SUSPENDED_PROPERTY )
    private Boolean suspended = Boolean.FALSE;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getSearchText() {
        return "";
    }

    public Boolean getSuspended() {
        return suspended;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append("\""+id+"\"");
        sb.append(",\"deviceId\":")
                .append(deviceId);
        sb.append(",\"deviceToken\":\"")
                .append(deviceToken).append('\"');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceCredentials that = (DeviceCredentials) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (deviceId != null ? !deviceId.equals(that.deviceId) : that.deviceId != null) return false;
        return deviceToken != null ? deviceToken.equals(that.deviceToken) : that.deviceToken == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (deviceId != null ? deviceId.hashCode() : 0);
        result = 31 * result + (deviceToken != null ? deviceToken.hashCode() : 0);
        return result;
    }
}
