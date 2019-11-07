package cn.edu.bupt.dao.Cassandra;

import cn.edu.bupt.dao.BaseEntity;
import cn.edu.bupt.dao.ModelConstants;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Result;
import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

/**
 * Created by CZX on 2018/3/23.
 */
public abstract class CassandraAbstractModelDao<E extends BaseEntity> extends CassandraAbstractDao {

    protected abstract Class<E> getColumnFamilyClass();

    protected abstract String getColumnFamilyName();

    protected E updateSearchTextIfPresent(E entity) {
        return entity;
    }

    protected Mapper<E> getMapper() {
        return cluster.getMapper(getColumnFamilyClass());
    }

    protected List<E> findListByStatement(Statement statement) {
        List<E> list = Collections.emptyList();
        if (statement != null) {
            statement.setConsistencyLevel(cluster.getDefaultReadConsistencyLevel());
            ResultSet resultSet = getSession().execute(statement);
            Result<E> result = getMapper().map(resultSet);
            if (result != null) {
                list = result.all();
            }
        }
        return list;
    }

    protected E findOneByStatement(Statement statement) {
        E object = null;
        if (statement != null) {
            statement.setConsistencyLevel(cluster.getDefaultReadConsistencyLevel());
            ResultSet resultSet = getSession().execute(statement);
            Result<E> result = getMapper().map(resultSet);
            if (result != null) {
                object = result.one();
            }
        }
        return object;
    }

    protected Statement getSaveQuery(E dto) {
        return getMapper().saveQuery(dto);
    }

    protected E saveWithResult(E entity) {
        if (entity.getId() == null) {
            entity.setId(UUIDs.timeBased());
        } else if (isDeleteOnSave()) {
            removeById(entity.getId());
        }
        Statement saveStatement = getSaveQuery(entity);
        saveStatement.setConsistencyLevel(cluster.getDefaultWriteConsistencyLevel());
        executeWrite(saveStatement);
        return entity;
    }

    protected boolean isDeleteOnSave() {
        return true;
    }

    public E save(E domain) {
        E entity;

//        try {
//            entity = getColumnFamilyClass().getConstructor(domain.getClass()).newInstance(domain);
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Can't create entity for domain object {" + domain + "}", e);
//        }

        entity = updateSearchTextIfPresent(domain);
        entity = saveWithResult(entity);
        return entity;
    }

    public E findById(UUID key) {
        Select.Where query = select().from(getColumnFamilyName()).where(eq(ModelConstants.ID_PROPERTY, key));
        E entity = findOneByStatement(query);
        return entity;
    }

//    protected ListenableFuture<List<E>> findListByStatementAsync(Statement statement) {
//        if (statement != null) {
//            statement.setConsistencyLevel(cluster.getDefaultReadConsistencyLevel());
//            ResultSetFuture resultSetFuture = getSession().executeAsync(statement);
//            return Futures.transform(resultSetFuture, new Function<ResultSet, List<E>>() {
//                @Nullable
//                @Override
//                public List<E> apply(@Nullable ResultSet resultSet) {
//                    Result<E> result = getMapper().map(resultSet);
//                    if (result != null) {
//                        List<E> entities = result.all();
//                        return entities;
//                    } else {
//                        return Collections.emptyList();
//                    }
//                }
//            });
//        }
//        return Futures.immediateFuture(Collections.emptyList());
//    }

    protected ListenableFuture<E> findOneByStatementAsync(Statement statement) {
        if (statement != null) {
            statement.setConsistencyLevel(cluster.getDefaultReadConsistencyLevel());
            ResultSetFuture resultSetFuture = getSession().executeAsync(statement);
            return Futures.transform(resultSetFuture, new Function<ResultSet, E>() {
                @Nullable
                @Override
                public E apply(@Nullable ResultSet resultSet) {
                    Result<E> result = getMapper().map(resultSet);
                    if (result != null) {
                        E entity = result.one();
                        return entity;
                    } else {
                        return null;
                    }
                }
            });
        }
        return Futures.immediateFuture(null);
    }

    public ListenableFuture<E> findByIdAsync(UUID key) {
        Select.Where query = select().from(getColumnFamilyName()).where(eq(ModelConstants.ID_PROPERTY, key));
        return findOneByStatementAsync(query);
    }

    public boolean removeById(UUID key) {
        Statement delete = QueryBuilder.delete().all().from(getColumnFamilyName()).where(eq(ModelConstants.ID_PROPERTY, key));
        return getSession().execute(delete).wasApplied();
    }

    public List<E> find() {
        List<E> entities = findListByStatement(QueryBuilder.select().all().from(getColumnFamilyName()).setConsistencyLevel(cluster.getDefaultReadConsistencyLevel()));
        return entities;
    }

//    protected static <T> Function<BaseEntity<T>, T> toDataFunction() {
//        return new Function<BaseEntity<T>, T>() {
//            @Nullable
//            @Override
//            public T apply(@Nullable BaseEntity<T> entity) {
//                return entity != null ? entity.toData() : null;
//            }
//        };
//    }
}
