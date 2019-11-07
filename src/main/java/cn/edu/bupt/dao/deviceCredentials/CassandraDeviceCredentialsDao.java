package cn.edu.bupt.dao.deviceCredentials;

import cn.edu.bupt.dao.Cassandra.CassandraAbstractModelDao;
import cn.edu.bupt.dao.ModelConstants;
import cn.edu.bupt.pojo.DeviceCredentials;
import com.datastax.driver.core.querybuilder.Select;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

/**
 * Created by CZX on 2018/4/17.
 */
@Component
public class CassandraDeviceCredentialsDao extends CassandraAbstractModelDao<DeviceCredentials> implements DeviceCredentialsDao{

    @Override
    protected Class<DeviceCredentials> getColumnFamilyClass() {
        return DeviceCredentials.class;
    }

    @Override
    protected String getColumnFamilyName() {
        return ModelConstants.DEVICE_CREDENTIALS_COLUMN_FAMILY_NAME;
    }

    @Override
    public DeviceCredentials findByDeviceId(UUID deviceId) {
        Select.Where query = select().from(ModelConstants.DEVICE_CREDENTIALS_BY_DEVICE_COLUMN_FAMILY_NAME)
                .where(eq(ModelConstants.DEVICE_CREDENTIALS_DEVICE_ID_PROPERTY, deviceId));
        DeviceCredentials deviceCredentials = findOneByStatement(query);
        return deviceCredentials;
    }

    @Override
    public DeviceCredentials findByToken(String token) {
        Select.Where query = select().from(ModelConstants.DEVICE_CREDENTIALS_BY_DEVICE_TOKEN_COLUMN_FAMILY_NAME)
                .where(eq(ModelConstants.DEVICE_CREDENTIALS_TOKEN_PROPERTY, token));
        DeviceCredentials deviceCredentials = findOneByStatement(query);
        return deviceCredentials;
    }
}
