package cn.edu.bupt.pojo;

import cn.edu.bupt.dao.SearchTextBased;
import cn.edu.bupt.dao.SearchTextEntity;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.UUID;

import static cn.edu.bupt.dao.ModelConstants.*;

/**
 * Created by CZX on 2018/4/18.
 */
@Table(name = GROUP_COLUMN_FAMILY_NAME)
public class Group extends SearchTextBased implements SearchTextEntity {

    @PartitionKey(value = 0)
    @Column(name = ID_PROPERTY)
    private UUID id;

    @PartitionKey(value = 1)
    @Column(name = GROUP_TENANT_ID_PROPERTY)
    private Integer tenantId;

    @PartitionKey(value = 2)
    @Column(name = GROUP_CUSTOMER_ID_PROPERTY)
    private Integer customerId;

    @Column(name = GROUP_NAME_PROPERTY)
    private String name;

    @Column(name = SEARCH_TEXT_PROPERTY)
    private String searchText;

    public Group() {
        super();
    }

    public Group(Group group) {
        if (group.getId() != null) {
            this.id = group.getId();
        }
        if (group.getTenantId() != null) {
            this.tenantId = group.getTenantId();
        }
        if (group.getCustomerId() != null) {
            this.customerId = group.getCustomerId();
        }
        this.name = group.getName();
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (id != null ? !id.equals(group.id) : group.id != null) return false;
        if (tenantId != null ? !tenantId.equals(group.tenantId) : group.tenantId != null) return false;
        if (customerId != null ? !customerId.equals(group.customerId) : group.customerId != null) return false;
        if (name != null ? !name.equals(group.name) : group.name != null) return false;
        return searchText != null ? searchText.equals(group.searchText) : group.searchText == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tenantId != null ? tenantId.hashCode() : 0);
        result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (searchText != null ? searchText.hashCode() : 0);
        return result;
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
        sb.append('}');
        return sb.toString();
    }
}
