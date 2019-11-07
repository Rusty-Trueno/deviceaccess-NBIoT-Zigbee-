package cn.edu.bupt.dao.device;

import cn.edu.bupt.dao.Cassandra.CassandraAbstractSearchTextDao;
import cn.edu.bupt.dao.ModelConstants;
import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.pojo.Device;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.Select;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static cn.edu.bupt.dao.ModelConstants.*;
import static com.datastax.driver.core.querybuilder.QueryBuilder.*;

/**
 * Created by CZX on 2018/4/17.
 */
@Component
public class CassandraDeviceDao extends CassandraAbstractSearchTextDao<Device> implements DeviceDao {

    private PreparedStatement fetchStmt;
    private PreparedStatement fetchCustomerCountStmt;
    public static final String SELECT_PREFIX = "SELECT ";
    public static final String EQUALS_PARAM = " = ? ";

    @Override
    protected Class<Device> getColumnFamilyClass() {
        return Device.class;
    }

    @Override
    protected String getColumnFamilyName() {
        return DEVICE_COLUMN_FAMILY_NAME;
    }

    @Override
    public Device save(Device device) {
        Device savedDevice = super.save(device);
        return savedDevice;
    }

    @Override
    public List<Device> findDevicesByTenantId(Integer tenantId, TextPageLink pageLink) {
        List<Device> devices = findPageWithTextSearch(DEVICE_BY_TENANT_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME,
                Collections.singletonList(eq(DEVICE_TENANT_ID_PROPERTY, tenantId)), pageLink);
        return devices;
    }

    @Override
    public Long findDevicesCountByTenantId(Integer tenantId, TextPageLink pageLink) {
        Long count = findCountWithTextSearch(DEVICE_BY_TENANT_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME,
                Collections.singletonList(eq(DEVICE_TENANT_ID_PROPERTY, tenantId)), pageLink);
        return count;
    }

    @Override
    public Long findDevicesCountByCustomerId(Integer tenantId, Integer customerId, TextPageLink pageLink) {
        Long count = findCountWithTextSearch(DEVICE_BY_CUSTOMER_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME,
                Arrays.asList(eq(DEVICE_CUSTOMER_ID_PROPERTY, customerId),
                        eq(DEVICE_TENANT_ID_PROPERTY, tenantId)), pageLink);
        return count;
    }

    @Override
    public List<Device> findDevicesByParentDeviceId(String parentDeviceId, TextPageLink pageLink) {
        List<Device> devices = findPageWithTextSearch(DEVICE_BY_TENANT_AND_PARENT_DEVICE_ID_COLUMN_FAMILY_NAME,
                Collections.singletonList(eq(DEVICE_PARENT_DEVICE_ID_PROPERTY, parentDeviceId)), pageLink);
        return devices;
    }

    @Override
    public List<Device> findDevicesByTenantIdAndCustomerId(Integer tenantId, Integer customerId, TextPageLink pageLink) {
        List<Device> devices = findPageWithTextSearch(DEVICE_BY_CUSTOMER_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME,
                Arrays.asList(eq(DEVICE_CUSTOMER_ID_PROPERTY, customerId),
                        eq(DEVICE_TENANT_ID_PROPERTY, tenantId)),
                pageLink);
        return devices;
    }

    @Override
    public List<Device> findDevicesByManufactureAndDeviceTypeAndModel(String manufacture, String deviceType, String model, TextPageLink pageLink)
    {
        List<Device> devices = findPageWithTextSearch(DEVICE_BY_MANUFACTURE_AND_DEVICE_TYPE_AND_MODEL,
                Arrays.asList(eq(DEVICE_MANUFACTURE_PROPERTY, manufacture),
                        eq(DEVICE_DEVICE_TYPE_PROPERTY, deviceType),
                        eq(DEVICE_MODEL_PROPERTY, model)),
                pageLink);
        return devices;
    }


    @Override
    public List<Device> findDevicesByManufactureAndDeviceType(String manufacture, String deviceType, TextPageLink pageLink)
    {
        List<Device> devices = findPageWithTextSearch(DEVICE_BY_DEVICE_TYPE_AND_CUSTOMER,
                Arrays.asList(eq(DEVICE_MANUFACTURE_PROPERTY, manufacture),
                        eq(DEVICE_DEVICE_TYPE_PROPERTY, deviceType)),pageLink);
        return devices;
    }

    @Override
    public List<Device> findDevicesByDeviceType(String deviceType, TextPageLink pageLink)
    {
        List<Device> devices = findPageWithTextSearch(DEVICE_BY_MANUFACTURE_AND_DEVICE_TYPE_AND_MODEL,
                Arrays.asList(eq(DEVICE_DEVICE_TYPE_PROPERTY, deviceType)),pageLink);
        return devices;
    }

    @Override
    public Optional<Device> findDeviceByTenantIdAndName(Integer tenantId, String deviceName) {
        Select select = select().from(DEVICE_BY_TENANT_AND_NAME_VIEW_NAME);
        Select.Where query = select.where();
        query.and(eq(DEVICE_TENANT_ID_PROPERTY, tenantId));
        query.and(eq(DEVICE_NAME_PROPERTY, deviceName));
        return Optional.ofNullable(findOneByStatement(query));
    }

    @Override
    public List<Device> findDevicesByTenantIdAndSiteId(int tenantId, int siteId, TextPageLink pageLink){
        List<Device> devices = findPageWithIdDesc(DEVICE_BY_TENANT_AND_SITE,
                Arrays.asList(eq(DEVICE_TENANT_ID_PROPERTY, tenantId), eq(DEVICE_SITE_ID_PROPERTY, siteId)),
                pageLink);
        return devices;
    }

    @Override
    public List<Device> findDevices(int tenantId, TextPageLink pageLink) {
        List<Device> devices = findPageWithTextSearch("device_by_tenant_and_search_text2",
                Collections.singletonList(eq(DEVICE_TENANT_ID_PROPERTY, tenantId)), pageLink);
        return devices;
    }

    @Override
    public Long findDevicesCount(int tenantId) {
        PreparedStatement proto = getCountStmt();
        BoundStatement stmt = proto.bind();
        stmt.setInt(0, tenantId);
        ResultSet resultSet = executeRead(stmt);
        return resultSet.one().getLong(0);
    }

    @Override
    public Long findCustomerDevicesCount(int customerId) {
        PreparedStatement proto = getCustomerCountStmt();
        BoundStatement stmt = proto.bind();
        stmt.setInt(0, customerId);
        ResultSet resultSet = executeRead(stmt);
        return resultSet.one().getLong(0);
    }

    private PreparedStatement getCountStmt() {
        if(fetchStmt==null) {
            fetchStmt = getSession().prepare(SELECT_PREFIX +
                    "count(id)" + " FROM " + ModelConstants.DEVICE_BY_TENANT_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME
                    + " WHERE " + ModelConstants.DEVICE_TENANT_ID_PROPERTY + EQUALS_PARAM);
        }
        return fetchStmt;
    }

    private PreparedStatement getCustomerCountStmt() {
        if(fetchCustomerCountStmt==null) {
            fetchCustomerCountStmt = getSession().prepare(SELECT_PREFIX +
                    "count(id)" + " FROM " + ModelConstants.DEVICE_BY_CUSTOMER_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME
                    + " WHERE " + ModelConstants.DEVICE_CUSTOMER_ID_PROPERTY + EQUALS_PARAM);
        }
        return fetchCustomerCountStmt;
    }
}
