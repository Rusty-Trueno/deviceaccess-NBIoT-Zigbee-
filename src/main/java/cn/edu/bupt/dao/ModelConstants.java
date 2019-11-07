package cn.edu.bupt.dao;

import cn.edu.bupt.pojo.kv.Aggregation;
import com.datastax.driver.core.utils.UUIDs;
import org.apache.commons.lang3.ArrayUtils;

import java.util.UUID;

/**
 * Created by CZX on 2018/3/23.
 */
public class ModelConstants {

    public static final UUID NULL_UUID = UUIDs.startOf(0);

    /**
     * Generic constants.
     */
    public static final String ID_PROPERTY = "id";
    public static final String USER_ID_PROPERTY = "user_id";
    public static final String TENANT_ID_PROPERTY = "tenant_id";
    public static final String CUSTOMER_ID_PROPERTY = "customer_id";
    public static final String ADDITIONAL_INFO_PROPERTY = "additional_info";
    public static final String DEVICE_ID_PROPERTY = "device_id";
    public static final String SEARCH_TEXT_PROPERTY = "search_text";
    /**
     * Cassandra contact constants.
     */
    public static final String ADDRESS_PROPERTY = "address";
    public static final String PHONE_PROPERTY = "phone";
    public static final String EMAIL_PROPERTY = "email";

    /**
     * Cassandra user constants.
     */
    public static final String USER_COLUMN_FAMILY_NAME = "user";
    public static final String USER_TENANT_ID_PROPERTY = TENANT_ID_PROPERTY;
    public static final String USER_CUSTOMER_ID_PROPERTY = CUSTOMER_ID_PROPERTY;
    public static final String USER_EMAIL_PROPERTY = "email";
    public static final String USER_AUTHORITY_PROPERTY = "authority";
    public static final String USER_NAME_PROPERTY = "name";
    public static final String USER_ADDITIONAL_INFO_PROPERTY = ADDITIONAL_INFO_PROPERTY;

    public static final String USER_BY_EMAIL_COLUMN_FAMILY_NAME = "user_by_email";
//    public static final String USER_BY_TENANT_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "user_by_tenant_and_search_text";
//    public static final String USER_BY_CUSTOMER_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "user_by_customer_and_search_text";

    /**
     * Cassandra tenant constants.
     */
    public static final String TENANT_COLUMN_FAMILY_NAME = "tenant";
    public static final String TENANT_NAME_PROPERTY = "name";
    public static final String TENANT_ADDITIONAL_INFO_PROPERTY = ADDITIONAL_INFO_PROPERTY;

//    public static final String TENANT_BY_REGION_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "tenant_by_region_and_search_text";

    /**
     * Cassandra customer constants.
     */
    public static final String CUSTOMER_COLUMN_FAMILY_NAME = "customer";
    public static final String CUSTOMER_TENANT_ID_PROPERTY = TENANT_ID_PROPERTY;
    public static final String CUSTOMER_NAME_PROPERTY = "name";
    public static final String CUSTOMER_ADDITIONAL_INFO_PROPERTY = ADDITIONAL_INFO_PROPERTY;

//    public static final String CUSTOMER_BY_TENANT_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "customer_by_tenant_and_search_text";
//    public static final String CUSTOMER_BY_TENANT_AND_TITLE_VIEW_NAME = "customer_by_tenant_and_title";

    /**
     * Cassandra device constants.
     */
    public static final String DEVICE_COLUMN_FAMILY_NAME = "device";
    public static final String DEVICE_TENANT_ID_PROPERTY = TENANT_ID_PROPERTY;
    public static final String DEVICE_CUSTOMER_ID_PROPERTY = CUSTOMER_ID_PROPERTY;
    public static final String DEVICE_GROUP_PROPERTY = "group_id";
    public static final String DEVICE_NAME_PROPERTY = "name";
    public static final String DEVICE_PARENT_DEVICE_ID_PROPERTY = "parent_device_id";
    public static final String DEVICE_MANUFACTURE_PROPERTY = "manufacture";
    public static final String DEVICE_MODEL_PROPERTY = "model";
    public static final String DEVICE_DEVICE_TYPE_PROPERTY = "device_type";
    public static final String DEVICE_STATUS_PROPERTY = "status";
    public static final String DEVICE_LOCATION_PROPERTY = "location";
    public static final String DEVICE_SITE_ID_PROPERTY = "site_id";
    public static final String DEVICE_LIFE_TIME_PROPERTY = "life_time";
    public static final String DEVICE_NICKNAME_PROPERTY = "nickname";
    public static final String DEVICE_ADDITIONAL_INFO_PROPERTY = ADDITIONAL_INFO_PROPERTY;

    public static final String DEVICE_BY_TENANT_AND_PARENT_DEVICE_ID_COLUMN_FAMILY_NAME = "device_by_tenant_and_parent_device_id";
    public static final String DEVICE_BY_GROUP_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "device_by_group_and_search_text";
    public static final String DEVICE_BY_TENANT_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "device_by_tenant_and_search_text";
    public static final String DEVICE_BY_TENANT_BY_TYPE_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "device_by_tenant_by_type_and_search_text";
    public static final String DEVICE_BY_CUSTOMER_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "device_by_customer_and_search_text";
    public static final String DEVICE_BY_CUSTOMER_BY_TYPE_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "device_by_customer_by_type_and_search_text";
    public static final String DEVICE_BY_TENANT_AND_NAME_VIEW_NAME = "device_by_tenant_and_name";
    public static final String DEVICE_TYPES_BY_TENANT_VIEW_NAME = "device_types_by_tenant";
    public static final String DEVICE_BY_MANUFACTURE_AND_DEVICE_TYPE_AND_MODEL="device_by_manufacture_and_device_type_and_model";
    public static final String DEVICE_BY_DEVICE_TYPE_AND_CUSTOMER = "device_by_device_type_and_customer_id";
    public static final String DEVICE_BY_TENANT_AND_SITE = "device_by_tenant_and_site";

    /**
     * Cassandra device group constants.
     */
    public static final String GROUP_COLUMN_FAMILY_NAME = "group";
    public static final String GROUP_NAME_PROPERTY = "name";
    public static final String GROUP_TENANT_ID_PROPERTY = TENANT_ID_PROPERTY;
    public static final String GROUP_CUSTOMER_ID_PROPERTY = CUSTOMER_ID_PROPERTY;

    public static final String GROUP_BY_TENANT_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "group_by_tenant_and_search_text";
    public static final String GROUP_BY_CUSTOMER_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "group_by_customer_and_search_text";
    public static final String GROUP_BY_TENANT_AND_CUSTOMER_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME = "group_by_tenant_and_customer_and_search_text";
    public static final String GROUP_BY_CUSTOMER_AND_NAME_COLUMN_FAMILY_NAME = "group_by_customer_and_name";
    public static final String GROUP_BY_TENANT_AND_CUSTOMER_AND_NAME_COLUMN_FAMILY_NAME = "group_by_tenant_and_customer_and_name";

    /**
     * Cassandra deviceByGroupId constants.
     */
    public static final String DEVICE_BY_GROUP_ID_COLUMN_FAMILY_NAME = "device_by_group_id";
    public static final String DEVICE_BY_GROUP_ID_DEVICE_ID_PROPERTY = "device_id";
    public static final String DEVICE_BY_GROUP_ID_GROUP_ID_PROPERTY = "group_id";

    public static final String GROUP_BY_DEVICE_ID_COLUMN_FAMILY_NAME = "group_by_device_id";

    /**
     * Cassandra device_credentials constants.
     */
    public static final String DEVICE_CREDENTIALS_COLUMN_FAMILY_NAME = "device_credentials";
    public static final String DEVICE_CREDENTIALS_DEVICE_ID_PROPERTY = DEVICE_ID_PROPERTY;
    public static final String DEVICE_CREDENTIALS_TOKEN_PROPERTY = "device_token";
    public static final String DEVICE_CREDENTIALS_SUSPENDED_PROPERTY = "suspended";

    public static final String DEVICE_CREDENTIALS_BY_DEVICE_COLUMN_FAMILY_NAME = "device_credentials_by_device";
    public static final String DEVICE_CREDENTIALS_BY_DEVICE_TOKEN_COLUMN_FAMILY_NAME = "device_credentials_by_device_token";

    /**
     * Cassandra event constants.
     */
    public static final String EVENT_COLUMN_FAMILY_NAME = "event";
    public static final String EVENT_TENANT_ID_PROPERTY = TENANT_ID_PROPERTY;
    public static final String EVENT_TYPE_PROPERTY = "event_type";
    public static final String EVENT_ENTITY_TYPE_PROPERTY = "entity_type";
    public static final String EVENT_ENTITY_ID_PROPERTY = "entity_id";
    public static final String EVENT_BODY_PROPERTY = "body";

    public static final String EVENT_BY_TYPE_AND_ID_VIEW_NAME = "event_by_type_and_id";
    public static final String EVENT_BY_ID_VIEW_NAME = "event_by_id";

    /**
     * Cassandra attributes and timeseries constants.
     */
    public static final String ATTRIBUTES_KV_CF = "attributes_kv_cf";
    public static final String TS_KV_CF = "ts_kv_cf";
    public static final String TS_KV_PARTITIONS_CF = "ts_kv_partitions_cf";
    public static final String TS_KV_LATEST_CF = "ts_kv_latest_cf";

    public static final String ENTITY_ID_COLUMN = "entity_id";
    public static final String ATTRIBUTE_KEY_COLUMN = "attribute_key";
    public static final String LAST_UPDATE_TS_COLUMN = "last_update_ts";

    public static final String PARTITION_COLUMN = "partition";
    public static final String KEY_COLUMN = "key";
    public static final String TS_COLUMN = "ts";

    /**
     * Main names of cassandra key-value columns storage.
     */
    public static final String BOOLEAN_VALUE_COLUMN = "bool_v";
    public static final String STRING_VALUE_COLUMN = "str_v";
    public static final String LONG_VALUE_COLUMN = "long_v";
    public static final String DOUBLE_VALUE_COLUMN = "dbl_v";

    protected static final String[] NONE_AGGREGATION_COLUMNS = new String[]{LONG_VALUE_COLUMN, DOUBLE_VALUE_COLUMN, BOOLEAN_VALUE_COLUMN, STRING_VALUE_COLUMN, KEY_COLUMN, TS_COLUMN};

    protected static final String[] COUNT_AGGREGATION_COLUMNS = new String[]{count(LONG_VALUE_COLUMN), count(DOUBLE_VALUE_COLUMN), count(BOOLEAN_VALUE_COLUMN), count(STRING_VALUE_COLUMN)};

    protected static final String[] MIN_AGGREGATION_COLUMNS = ArrayUtils.addAll(COUNT_AGGREGATION_COLUMNS,
            new String[]{min(LONG_VALUE_COLUMN), min(DOUBLE_VALUE_COLUMN), min(BOOLEAN_VALUE_COLUMN), min(STRING_VALUE_COLUMN)});
    protected static final String[] MAX_AGGREGATION_COLUMNS = ArrayUtils.addAll(COUNT_AGGREGATION_COLUMNS,
            new String[]{max(LONG_VALUE_COLUMN), max(DOUBLE_VALUE_COLUMN), max(BOOLEAN_VALUE_COLUMN), max(STRING_VALUE_COLUMN)});
    protected static final String[] SUM_AGGREGATION_COLUMNS = ArrayUtils.addAll(COUNT_AGGREGATION_COLUMNS,
            new String[]{sum(LONG_VALUE_COLUMN), sum(DOUBLE_VALUE_COLUMN)});
    protected static final String[] AVG_AGGREGATION_COLUMNS = SUM_AGGREGATION_COLUMNS;

    public static String min(String s) {
        return "min(" + s + ")";
    }

    public static String max(String s) {
        return "max(" + s + ")";
    }

    public static String sum(String s) {
        return "sum(" + s + ")";
    }

    public static String count(String s) {
        return "count(" + s + ")";
    }

    public static String[] getFetchColumnNames(Aggregation aggregation) {
        switch (aggregation) {
            case NONE:
                return NONE_AGGREGATION_COLUMNS;
            case MIN:
                return MIN_AGGREGATION_COLUMNS;
            case MAX:
                return MAX_AGGREGATION_COLUMNS;
            case SUM:
                return SUM_AGGREGATION_COLUMNS;
            case COUNT:
                return COUNT_AGGREGATION_COLUMNS;
            case AVG:
                return AVG_AGGREGATION_COLUMNS;
            default:
                throw new RuntimeException("Aggregation type: " + aggregation + " is not supported!");
        }
    }
}