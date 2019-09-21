package cn.edu.bupt.dao.attributes;

import cn.edu.bupt.pojo.kv.AttributeKvEntry;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by CZX on 2018/4/24.
 */
public interface AttributesDao {

    ListenableFuture<Optional<AttributeKvEntry>> find(UUID entityId, String attributeKey);

    ListenableFuture<List<AttributeKvEntry>> find(UUID entityId, Collection<String> attributeKey);

    ListenableFuture<List<AttributeKvEntry>> findAll(UUID entityId);

    ListenableFuture<Void> save(UUID entityId, AttributeKvEntry attribute);

    ListenableFuture<List<Void>> removeAll(UUID entityId, List<String> keys);

}
