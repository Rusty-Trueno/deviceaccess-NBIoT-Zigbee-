package cn.edu.bupt.dao.util;

import cn.edu.bupt.dao.exception.IncorrectParameterException;
import cn.edu.bupt.dao.page.TextPageLink;

import java.util.List;
import java.util.UUID;

public class Validator {

    /**
     * This method validate <code>EntityId</code> entity id. If entity id is invalid than throw
     * <code>IncorrectParameterException</code> exception
     *
     * @param entityId          the entityId
     * @param errorMessage the error message for exception
     */
    public static void validateEntityId(UUID entityId, String errorMessage) {
        if (entityId == null) {
            throw new IncorrectParameterException(errorMessage);
        }
    }

    /**
     * This method validate <code>String</code> string. If string is invalid than throw
     * <code>IncorrectParameterException</code> exception
     *
     * @param val          the val
     * @param errorMessage the error message for exception
     */
    public static void validateString(String val, String errorMessage) {
        if (val == null || val.isEmpty()) {
            throw new IncorrectParameterException(errorMessage);
        }
    }


    /**
     * This method validate <code>String</code> string. If string is invalid than throw
     * <code>IncorrectParameterException</code> exception
     *
     * @param val          the val
     * @param errorMessage the error message for exception
     */
    public static void validatePositiveNumber(long val, String errorMessage) {
        if (val <= 0) {
            throw new IncorrectParameterException(errorMessage);
        }
    }

    /**
     * This method validate <code>UUID</code> id. If id is null than throw
     * <code>IncorrectParameterException</code> exception
     *
     * @param id           the id
     * @param errorMessage the error message for exception
     */
    public static void validateId(UUID id, String errorMessage) {
        if (id == null) {
            throw new IncorrectParameterException(errorMessage);
        }
    }

    public static void validateId(Integer id, String errorMessage) {
        if (id == null) {
            throw new IncorrectParameterException(errorMessage);
        }
    }

    /**
     * This method validate list of <code>UUIDBased</code> ids. If at least one of the ids is null than throw
     * <code>IncorrectParameterException</code> exception
     *
     * @param ids          the list of ids
     * @param errorMessage the error message for exception
     */
    public static void validateIds(List<UUID> ids, String errorMessage) {
        if (ids == null || ids.isEmpty()) {
            throw new IncorrectParameterException(errorMessage);
        } else {
            for (UUID id : ids) {
                validateId(id, errorMessage);
            }
        }
    }

    /**
     * This method validate <code>PageLink</code> page link. If pageLink is invalid than throw
     * <code>IncorrectParameterException</code> exception
     *
     * @param pageLink     the page link
     * @param errorMessage the error message for exception
     */
    public static void validatePageLink(TextPageLink pageLink, String errorMessage) {
        if (pageLink == null || pageLink.getLimit() < 1 || (pageLink.getIdOffset() != null && pageLink.getIdOffset().version() != 1)) {
            throw new IncorrectParameterException(errorMessage);
        }
    }

}
