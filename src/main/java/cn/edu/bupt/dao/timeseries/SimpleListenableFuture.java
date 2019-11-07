package cn.edu.bupt.dao.timeseries;

import com.google.common.util.concurrent.AbstractFuture;

/**
 * Created by ashvayka on 21.02.17.
 */
public class SimpleListenableFuture<V> extends AbstractFuture<V> {

    public boolean set(V value) {
        return super.set(value);
    }

}
