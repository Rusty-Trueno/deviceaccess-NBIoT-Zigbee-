package cn.edu.bupt.dao.Cassandra;

import cn.edu.bupt.dao.ModelConstants;
import cn.edu.bupt.dao.SearchTextEntity;
import cn.edu.bupt.dao.page.TextPageLink;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.Where;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

@Slf4j
public abstract class CassandraAbstractSearchTextDao<E extends SearchTextEntity> extends CassandraAbstractModelDao<E> {

    @Override
    protected E updateSearchTextIfPresent(E entity) {
        if (entity.getSearchTextSource() != null) {
            entity.setSearchText(entity.getSearchTextSource().toLowerCase());
        } else {
            log.trace("Entity [{}] has null SearchTextSource", entity);
        }
        return entity;
    }

    protected List<E> findPageWithTextSearch(String searchView, List<Clause> clauses, TextPageLink pageLink) {
        Select select = select().from(searchView);
        Where query = select.where();
        for (Clause clause : clauses) {
            query.and(clause);
        }
        query.limit(pageLink.getLimit());
        if (!StringUtils.isEmpty(pageLink.getTextOffset())) {
            query.and(eq(ModelConstants.SEARCH_TEXT_PROPERTY, pageLink.getTextOffset()));
            query.and(QueryBuilder.lt(ModelConstants.ID_PROPERTY, pageLink.getIdOffset()));
            List<E> result = findListByStatement(query);
            if (result.size() < pageLink.getLimit()) {
                select = select().from(searchView);
                query = select.where();
                for (Clause clause : clauses) {
                    query.and(clause);
                }
                query.and(QueryBuilder.gt(ModelConstants.SEARCH_TEXT_PROPERTY, pageLink.getTextOffset()));
                if (!StringUtils.isEmpty(pageLink.getTextSearch())) {
                    query.and(QueryBuilder.lt(ModelConstants.SEARCH_TEXT_PROPERTY, pageLink.getTextSearchBound()));
                }
                int limit = pageLink.getLimit() - result.size();
                query.limit(limit);
                result.addAll(findListByStatement(query));
            }
            return result;
        } else if (!StringUtils.isEmpty(pageLink.getTextSearch())) {
            query.and(QueryBuilder.gte(ModelConstants.SEARCH_TEXT_PROPERTY, pageLink.getTextSearch()));
            query.and(QueryBuilder.lt(ModelConstants.SEARCH_TEXT_PROPERTY, pageLink.getTextSearchBound()));
            return findListByStatement(query);
        } else {
            return findListByStatement(query);
        }
    }

    protected Long findCountWithTextSearch(String searchView, List<Clause> clauses, TextPageLink pageLink) {
//        Select select = select().from(searchView);
        Select.SelectionOrAlias selectionOrAlias = new Select.SelectionOrAlias();
        Where query = selectionOrAlias.countAll().from(searchView).where();
        for (Clause clause : clauses) {
            query.and(clause);
        }
        query.and(QueryBuilder.gte(ModelConstants.SEARCH_TEXT_PROPERTY, pageLink.getTextSearch()));
        query.and(QueryBuilder.lt(ModelConstants.SEARCH_TEXT_PROPERTY, pageLink.getTextSearchBound()));
        ResultSet resultSet = executeRead(query);
        return resultSet.one().getLong(0);
    }

    protected List<E> findPageWithIdDesc(String searchView, List<Clause> clauses, TextPageLink pageLink){
        Select select = select().from(searchView);
        Where query = select.where();
        List<E> result = new ArrayList<>();
        for (Clause clause : clauses) {
            query.and(clause);
        }
        query.and(QueryBuilder.lt(ModelConstants.ID_PROPERTY, pageLink.getIdOffset()));
        int limit = pageLink.getLimit();
        query.limit(limit);
        result.addAll(findListByStatement(query));
        return result;
    }
}

