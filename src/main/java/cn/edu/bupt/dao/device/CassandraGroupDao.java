package cn.edu.bupt.dao.device;

import cn.edu.bupt.dao.Cassandra.CassandraAbstractSearchTextDao;
import cn.edu.bupt.dao.ModelConstants;
import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.pojo.Group;
import com.datastax.driver.core.querybuilder.Select;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

/**
 * Created by CZX on 2018/4/18.
 */
@Component
public class CassandraGroupDao extends CassandraAbstractSearchTextDao<Group> implements GroupDao{

    @Override
    protected Class<Group> getColumnFamilyClass() {
        return Group.class;
    }

    @Override
    protected String getColumnFamilyName() {
        return ModelConstants.GROUP_COLUMN_FAMILY_NAME;
    }

    @Override
    public List<Group> findGroupsByTenantId(Integer tenantId, TextPageLink pageLink) {
        List<Group> groups = findPageWithTextSearch(ModelConstants.GROUP_BY_TENANT_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME,
                Arrays.asList(eq(ModelConstants.GROUP_TENANT_ID_PROPERTY, tenantId)),
                pageLink);
        return groups;
    }

    @Override
    public List<Group> findGroupsByTenantIdAndCustomerId(Integer tenantId, Integer customerId, TextPageLink pageLink) {
        List<Group> groups = findPageWithTextSearch(ModelConstants.GROUP_BY_TENANT_AND_CUSTOMER_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME,
                Arrays.asList(eq(ModelConstants.GROUP_TENANT_ID_PROPERTY, tenantId),eq(ModelConstants.GROUP_CUSTOMER_ID_PROPERTY, customerId)), pageLink);
        return groups;
    }

    @Override
    public List<Group> findGroupsByCustomerId(Integer customerId, TextPageLink pageLink) {
        List<Group> groups = findPageWithTextSearch(ModelConstants.GROUP_BY_CUSTOMER_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME,
                Arrays.asList(eq(ModelConstants.GROUP_CUSTOMER_ID_PROPERTY, customerId)), pageLink);
        return groups;
    }

    @Override
    public Optional<Group> findGroupByTenantAndCustomerIdAndName(Integer tenantId, Integer customerId, String name){
        Select select = select().from(ModelConstants.GROUP_BY_TENANT_AND_CUSTOMER_AND_NAME_COLUMN_FAMILY_NAME);
        Select.Where query = select.where();
        query.and(eq(ModelConstants.GROUP_TENANT_ID_PROPERTY, tenantId));
        query.and(eq(ModelConstants.GROUP_CUSTOMER_ID_PROPERTY, customerId));
        query.and(eq(ModelConstants.GROUP_NAME_PROPERTY, name));
        return Optional.ofNullable(findOneByStatement(query));
    }
}
