package cn.edu.bupt.dao.util;

import cn.edu.bupt.dao.SearchTextBased;
import cn.edu.bupt.dao.page.TextPageLink;

import java.util.List;
import java.util.UUID;

public abstract class PaginatedRemover<I, D extends SearchTextBased> {

    private static final int DEFAULT_LIMIT = 100;

    public void removeEntities(I id) {
        TextPageLink pageLink = new TextPageLink(DEFAULT_LIMIT);
        boolean hasNext = true;
        while (hasNext) {
            List<D> entities = findEntities(id, pageLink);
            for (D entity : entities) {
                removeEntity(entity);
            }
            hasNext = entities.size() == pageLink.getLimit();
            if (hasNext) {
                int index = entities.size() - 1;
                UUID idOffset = entities.get(index).getId();
                pageLink.setIdOffset(idOffset);
            }
        }
    }

    protected abstract List<D> findEntities(I id, TextPageLink pageLink);

    protected abstract void removeEntity(D entity);

}
