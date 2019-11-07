package cn.edu.bupt.dao.Cassandra;

import cn.edu.bupt.pojo.event.EntityTypeCodec;
import com.datastax.driver.core.*;
import com.datastax.driver.core.exceptions.CodecNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by CZX on 2018/3/23.
 */
public class CassandraAbstractDao {
    @Autowired
    protected CassandraCluster cluster;

    private Session session;

    private ConsistencyLevel defaultReadLevel;
    private ConsistencyLevel defaultWriteLevel;

    protected Session getSession() {
        if (session == null) {
            session = cluster.getSession();
            defaultReadLevel = cluster.getDefaultReadConsistencyLevel();
            defaultWriteLevel = cluster.getDefaultWriteConsistencyLevel();
            CodecRegistry registry = session.getCluster().getConfiguration().getCodecRegistry();
            registerCodecIfNotFound(registry, new EntityTypeCodec());
        }
        return session;
    }

    protected ResultSet executeRead(Statement statement) {
        return execute(statement, defaultReadLevel);
    }

    protected ResultSet executeWrite(Statement statement) {
        return execute(statement, defaultWriteLevel);
    }

    protected ResultSetFuture executeAsyncRead(Statement statement) {
        return executeAsync(statement, defaultReadLevel);
    }

    protected ResultSetFuture executeAsyncWrite(Statement statement) {
        return executeAsync(statement, defaultWriteLevel);
    }

    private ResultSet execute(Statement statement, ConsistencyLevel level) {
        if (statement.getConsistencyLevel() == null) {
            statement.setConsistencyLevel(level);
        }
        return getSession().execute(statement);
    }

    private ResultSetFuture executeAsync(Statement statement, ConsistencyLevel level) {
        if (statement.getConsistencyLevel() == null) {
            statement.setConsistencyLevel(level);
        }
        return getSession().executeAsync(statement);
    }

    private void registerCodecIfNotFound(CodecRegistry registry, TypeCodec<?> codec) {
        try {
            registry.codecFor(codec.getCqlType(), codec.getJavaType());
        } catch (CodecNotFoundException e) {
            registry.register(codec);
        }
    }
}
