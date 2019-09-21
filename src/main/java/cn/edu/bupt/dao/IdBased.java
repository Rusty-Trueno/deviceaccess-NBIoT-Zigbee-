package cn.edu.bupt.dao;

import java.util.UUID;

/**
 * Created by CZX on 2018/5/29.
 */
public abstract class IdBased {

    protected UUID id;

    public IdBased() {
        super();
    }

    public IdBased(UUID id) {
        super();
        this.id = id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IdBased other = (IdBased) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
