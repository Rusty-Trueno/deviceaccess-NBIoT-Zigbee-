package cn.edu.bupt.dao;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.UUID;

/**
 * Created by CZX on 2018/3/23.
 */
public interface Dao<T> {

    List<T> find();

    T findById(UUID id);

    ListenableFuture<T> findByIdAsync(UUID id);

    T save(T t);

    boolean removeById(UUID id);
}
