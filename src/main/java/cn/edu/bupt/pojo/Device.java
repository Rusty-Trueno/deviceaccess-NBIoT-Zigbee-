package cn.edu.bupt.pojo;

import cn.edu.bupt.dao.SearchTextBased;
import cn.edu.bupt.dao.SearchTextEntity;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.UUID;

import static cn.edu.bupt.dao.ModelConstants.*;

/**
 * Created by Administrator on 2018/4/13.
 */
@Table(name = DEVICE_COLUMN_FAMILY_NAME)
public class Device extends SearchTextBased implements SearchTextEntity,Serializable {

    private static final long serialVersionUID = 1L;

    @PartitionKey(value = 0)
    @Column(name = ID_PROPERTY)
    @Expose
    private UUID id;

    @PartitionKey(value = 1)
    @Column(name = DEVICE_TENANT_ID_PROPERTY)
    private Integer tenantId;

    @PartitionKey(value = 2)
    @Column(name = DEVICE_CUSTOMER_ID_PROPERTY)
    private Integer customerId;

    @Column(name = DEVICE_NAME_PROPERTY)
    private String name;

    @Column(name = SEARCH_TEXT_PROPERTY)
    private String searchText;

    @Column(name  = DEVICE_PARENT_DEVICE_ID_PROPERTY )
    private String parentDeviceId;

    @PartitionKey(value = 4)
    @Column(name  = DEVICE_DEVICE_TYPE_PROPERTY)
    private String deviceType; //设备

    @PartitionKey(value = 3)
    @Column(name = DEVICE_MANUFACTURE_PROPERTY )
    private String manufacture;//厂商

    @PartitionKey(value = 5)
    @Column(name  = DEVICE_MODEL_PROPERTY )
    private String model;//设备型号

    @Column(name  = DEVICE_STATUS_PROPERTY )
    private String status;//运行状态

    @Column(name  = DEVICE_LOCATION_PROPERTY )
    private String location;

    @Column(name  = DEVICE_SITE_ID_PROPERTY )
    private Integer siteId;

    @Column(name  = DEVICE_LIFE_TIME_PROPERTY )
    private Long lifeTime;

    @Column(name = DEVICE_NICKNAME_PROPERTY)
    private String nickname;

//    public Device(Device device) {
//        this.id = id;
//        this.tenantId = tenantId;
//        this.customerId = customerId;
//        this.name = name;
//        this.searchText = searchText;
//        this.parentDeviceId = parentDeviceId;
//        this.deviceType = deviceType;
//        this.manufacture = manufacture;
//        this.model = model;
//        this.status = status;
//        this.location = location;
//    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentDeviceId() {
        return parentDeviceId;
    }

    public void setParentDeviceId(String parentDeviceId) {
        this.parentDeviceId = parentDeviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSearchText() {
        return searchText;
    }

    @Override
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public String getSearchTextSource() {
        return getName();
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Long getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(Long lifeTime) {
        this.lifeTime = lifeTime;
    }

    public String getNickname(){
        return nickname;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append("\""+id+"\"");
        sb.append(",\"tenantId\":")
                .append(tenantId);
        sb.append(",\"customerId\":")
                .append(customerId);
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"searchText\":\"")
                .append(searchText).append('\"');
        sb.append(",\"parentDeviceId\":\"")
                .append(parentDeviceId).append('\"');
        sb.append(",\"deviceType\":\"")
                .append(deviceType).append('\"');
        sb.append(",\"manufacture\":\"")
                .append(manufacture).append('\"');
        sb.append(",\"model\":\"")
                .append(model).append('\"');
        sb.append(",\"status\":\"")
                .append(status).append('\"');
        sb.append(",\"location\":\"")
                .append(location).append('\"');
        sb.append(",\"siteId\":\"")
                .append(siteId).append('\"');
        sb.append(",\"lifeTime\":\"")
                .append(lifeTime).append('\"');
        sb.append(",\"nickname\":\"")
                .append(nickname).append('\"');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Device device = (Device) o;

        if (lifeTime != device.lifeTime) return false;
        if (id != null ? !id.equals(device.id) : device.id != null) return false;
        if (tenantId != null ? !tenantId.equals(device.tenantId) : device.tenantId != null) return false;
        if (customerId != null ? !customerId.equals(device.customerId) : device.customerId != null) return false;
        if (name != null ? !name.equals(device.name) : device.name != null) return false;
        if (searchText != null ? !searchText.equals(device.searchText) : device.searchText != null) return false;
        if (parentDeviceId != null ? !parentDeviceId.equals(device.parentDeviceId) : device.parentDeviceId != null)
            return false;
        if (deviceType != null ? !deviceType.equals(device.deviceType) : device.deviceType != null) return false;
        if (manufacture != null ? !manufacture.equals(device.manufacture) : device.manufacture != null) return false;
        if (model != null ? !model.equals(device.model) : device.model != null) return false;
        if (status != null ? !status.equals(device.status) : device.status != null) return false;
        if (location != null ? !location.equals(device.location) : device.location != null) return false;
        if (nickname != null ? !nickname.equals(device.nickname) : device.nickname != null) return false;
        return siteId != null ? siteId.equals(device.siteId) : device.siteId == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (tenantId != null ? tenantId.hashCode() : 0);
        result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (searchText != null ? searchText.hashCode() : 0);
        result = 31 * result + (parentDeviceId != null ? parentDeviceId.hashCode() : 0);
        result = 31 * result + (deviceType != null ? deviceType.hashCode() : 0);
        result = 31 * result + (manufacture != null ? manufacture.hashCode() : 0);
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (siteId != null ? siteId.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (int) (lifeTime ^ (lifeTime >>> 32));
        return result;
    }

    @Override
    public long getCreatedTime(){
        Long createdTime = id.timestamp();
        createdTime = createdTime/10000000L - 12219292800L;
        return createdTime;
    }
}
