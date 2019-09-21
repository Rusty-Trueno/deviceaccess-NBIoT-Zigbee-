package cn.edu.bupt.dao.device;

import cn.edu.bupt.dao.Cassandra.CassandraAbstractAsyncDao;
import cn.edu.bupt.dao.ModelConstants;
import cn.edu.bupt.pojo.DeviceByGroupId;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by CZX on 2018/4/18.
 */
@Component
public class CassandraDeviceByGroupIdDao extends CassandraAbstractAsyncDao implements DeviceByGroupIdDao{
    private PreparedStatement saveStmt;
    private PreparedStatement findAllByGroupIdStmt;
    private PreparedStatement findAllByDeviceIdStmt;
    private PreparedStatement deleteStmt;
    private PreparedStatement deleteAllStmt;

    @Override
    public boolean save(DeviceByGroupId deviceByGroupId){
        BoundStatement stmt = getSaveStatement(deviceByGroupId);
        ResultSet rs = executeWrite(stmt);
        return rs.wasApplied();
    }

    @Override
    public boolean delete(DeviceByGroupId deviceByGroupId){
        BoundStatement stmt = getDeleteStatement(deviceByGroupId);
        ResultSet rs = executeWrite(stmt);
        return rs.wasApplied();
    }

    @Override
    public boolean deleteAllByGroupId(UUID groupId){
        BoundStatement stmt = getDeleteAllStatement(groupId);
        ResultSet rs = executeWrite(stmt);
        return rs.wasApplied();
    }

    @Override
    public List<UUID> findDevicesByGroupId(UUID groupId){
        BoundStatement stmt = getFindAllByGroupIdStmt().bind()
                .setUUID(0, groupId);
        ResultSet resultSet = executeRead(stmt);
        return getDeviceId(resultSet);
    }

    @Override
    public List<UUID> findGroupsByDeviceId(UUID deviceId){
        BoundStatement stmt = getFindAllByDeviceIdStmt().bind()
                .setUUID(0, deviceId);
        ResultSet resultSet = executeRead(stmt);
        return getGroupId(resultSet);
    }

    private BoundStatement getSaveStatement(DeviceByGroupId deviceByGroupId) {
        BoundStatement stmt = getSaveStmt().bind()
                .setUUID(0, deviceByGroupId.getGroupId())
                .setUUID(1, deviceByGroupId.getDeviceId());
        return stmt;
    }

    private BoundStatement getDeleteStatement(DeviceByGroupId deviceByGroupId) {
        BoundStatement stmt = getDeleteStmt().bind()
                .setUUID(0, deviceByGroupId.getGroupId())
                .setUUID(1,deviceByGroupId.getDeviceId());
        return stmt;
    }

    private BoundStatement getDeleteAllStatement(UUID groupId) {
        BoundStatement stmt = getDeleteAllStmt().bind()
                .setUUID(0, groupId);
        return stmt;
    }

    private PreparedStatement getSaveStmt() {
        if (saveStmt == null) {
            saveStmt = getSession().prepare("INSERT INTO " + ModelConstants.DEVICE_BY_GROUP_ID_COLUMN_FAMILY_NAME + " " +
                    "(" + ModelConstants.DEVICE_BY_GROUP_ID_GROUP_ID_PROPERTY +
                    "," + ModelConstants.DEVICE_BY_GROUP_ID_DEVICE_ID_PROPERTY + ")" +
                    " VALUES(?, ?)");
        }
        return saveStmt;
    }

    private PreparedStatement getFindAllByGroupIdStmt() {
        if (findAllByGroupIdStmt == null) {
            findAllByGroupIdStmt = getSession().prepare("SELECT "+ ModelConstants.DEVICE_BY_GROUP_ID_DEVICE_ID_PROPERTY +" FROM "+ ModelConstants.DEVICE_BY_GROUP_ID_COLUMN_FAMILY_NAME+ " where "
                    + ModelConstants.DEVICE_BY_GROUP_ID_GROUP_ID_PROPERTY +" = ?" );
        }
        return findAllByGroupIdStmt;
    }

    private PreparedStatement getFindAllByDeviceIdStmt() {
        if (findAllByDeviceIdStmt == null) {
            findAllByDeviceIdStmt = getSession().prepare("SELECT "+ ModelConstants.DEVICE_BY_GROUP_ID_GROUP_ID_PROPERTY +" FROM "+ ModelConstants.GROUP_BY_DEVICE_ID_COLUMN_FAMILY_NAME+ " where "
                    + ModelConstants.DEVICE_BY_GROUP_ID_DEVICE_ID_PROPERTY +" = ?" );
        }
        return findAllByDeviceIdStmt;
    }

    private PreparedStatement getDeleteStmt() {
        if (deleteStmt == null) {
            deleteStmt = getSession().prepare("DELETE FROM " + ModelConstants.DEVICE_BY_GROUP_ID_COLUMN_FAMILY_NAME +
                    " WHERE " + ModelConstants.DEVICE_BY_GROUP_ID_GROUP_ID_PROPERTY + " = ?" +
                    " AND " + ModelConstants.DEVICE_BY_GROUP_ID_DEVICE_ID_PROPERTY + " = ?" );
        }
        return deleteStmt;
    }

    private PreparedStatement getDeleteAllStmt() {
        if (deleteAllStmt == null) {
            deleteAllStmt = getSession().prepare("DELETE FROM " + ModelConstants.DEVICE_BY_GROUP_ID_COLUMN_FAMILY_NAME +
                    " WHERE " + ModelConstants.DEVICE_BY_GROUP_ID_GROUP_ID_PROPERTY + " = ?" );
        }
        return deleteAllStmt;
    }

    private List<UUID> getDeviceId(ResultSet rs) {
        List<Row> rows = rs.all();
        List<UUID> entries = new ArrayList<>(rows.size());
        if (!rows.isEmpty()) {
            rows.forEach(row -> {
                entries.add(row.getUUID(ModelConstants.DEVICE_BY_GROUP_ID_DEVICE_ID_PROPERTY));
            });
        }
        return entries;
    }

    private List<UUID> getGroupId(ResultSet rs) {
        List<Row> rows = rs.all();
        List<UUID> entries = new ArrayList<>(rows.size());
        if (!rows.isEmpty()) {
            rows.forEach(row -> {
                entries.add(row.getUUID(ModelConstants.DEVICE_BY_GROUP_ID_GROUP_ID_PROPERTY));
            });
        }
        return entries;
    }
}
