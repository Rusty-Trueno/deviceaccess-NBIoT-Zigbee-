package cn.edu.bupt.service;

import cn.edu.bupt.pojo.kv.AttributeKvEntry;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by CZX on 2018/4/24.
 */
public interface AttributesService {

    ListenableFuture<Optional<AttributeKvEntry>> find(UUID entityId, String attributeKey);

    ListenableFuture<List<AttributeKvEntry>> find(UUID entityId, Collection<String> attributeKeys);

    ListenableFuture<List<AttributeKvEntry>> findAll(UUID entityId);

    ListenableFuture<List<Void>> save(UUID entityId, List<AttributeKvEntry> attributes);

    ListenableFuture<List<Void>> removeAll(UUID entityId, List<String> attributeKeys);

}
