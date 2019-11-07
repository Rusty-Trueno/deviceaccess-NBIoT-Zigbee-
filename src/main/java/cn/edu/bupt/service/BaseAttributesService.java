package cn.edu.bupt.service;

import cn.edu.bupt.dao.attributes.AttributesDao;
import cn.edu.bupt.dao.exception.IncorrectParameterException;
import cn.edu.bupt.dao.util.Validator;
import cn.edu.bupt.pojo.kv.AttributeKvEntry;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by CZX on 2018/4/24.
 */
@Service
public class BaseAttributesService implements AttributesService{

    @Autowired
    private AttributesDao attributesDao;

    @Override
    public ListenableFuture<Optional<AttributeKvEntry>> find(UUID entityId, String attributeKey) {
        validate(entityId);
        Validator.validateString(attributeKey, "Incorrect attribute key " + attributeKey);
        return attributesDao.find(entityId, attributeKey);
    }

    @Override
    public ListenableFuture<List<AttributeKvEntry>> find(UUID entityId, Collection<String> attributeKeys) {
        validate(entityId);
        attributeKeys.forEach(attributeKey -> Validator.validateString(attributeKey, "Incorrect attribute key " + attributeKey));
        return attributesDao.find(entityId, attributeKeys);
    }

    @Override
    public ListenableFuture<List<AttributeKvEntry>> findAll(UUID entityId) {
        validate(entityId);
        return attributesDao.findAll(entityId);
    }

    @Override
    public ListenableFuture<List<Void>> save(UUID entityId, List<AttributeKvEntry> attributes) {
        validate(entityId);
        attributes.forEach(attribute -> validate(attribute));
        List<ListenableFuture<Void>> futures = Lists.newArrayListWithExpectedSize(attributes.size());
        for (AttributeKvEntry attribute : attributes) {
            futures.add(attributesDao.save(entityId, attribute));
        }
        return Futures.allAsList(futures);
    }

    @Override
    public ListenableFuture<List<Void>> removeAll(UUID entityId, List<String> keys) {
        validate(entityId);
        return attributesDao.removeAll(entityId,keys);
    }

    private static void validate(UUID id) {
        Validator.validateId(id, "Incorrect id " + id);
    }

    private static void validate(AttributeKvEntry kvEntry) {
        if (kvEntry == null) {
            throw new IncorrectParameterException("Key value entry can't be null");
        } else if (kvEntry.getDataType() == null) {
            throw new IncorrectParameterException("Incorrect kvEntry. Data type can't be null");
        } else {
            Validator.validateString(kvEntry.getKey(), "Incorrect kvEntry. Key can't be empty");
            Validator.validatePositiveNumber(kvEntry.getLastUpdateTs(), "Incorrect last update ts. Ts should be positive");
        }
    }

}
