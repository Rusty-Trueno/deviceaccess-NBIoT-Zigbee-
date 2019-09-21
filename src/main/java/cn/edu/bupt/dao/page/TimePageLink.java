package cn.edu.bupt.dao.page;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by CZX on 2018/5/28.
 */
@ToString
public class TimePageLink extends BasePageLink implements Serializable {

    private static final long serialVersionUID = -4189954843653250480L;

    @Getter
    private final Long startTime;
    @Getter private final Long endTime;
    @Getter private final boolean ascOrder;

    public TimePageLink(int limit) {
        this(limit, null, null, false, null);
    }

    public TimePageLink(int limit, Long startTime) {
        this(limit, startTime, null, false, null);
    }

    public TimePageLink(int limit, Long startTime, Long endTime) {
        this(limit, startTime, endTime, false, null);
    }

    public TimePageLink(int limit, Long startTime, Long endTime, boolean ascOrder) {
        this(limit, startTime, endTime, ascOrder, null);
    }

    @JsonCreator
    public TimePageLink(@JsonProperty("limit") int limit,
                        @JsonProperty("startTime") Long startTime,
                        @JsonProperty("endTime") Long endTime,
                        @JsonProperty("ascOrder") boolean ascOrder,
                        @JsonProperty("idOffset") UUID idOffset) {
        super(limit, idOffset);
        this.startTime = startTime;
        this.endTime = endTime;
        this.ascOrder = ascOrder;
    }

}
