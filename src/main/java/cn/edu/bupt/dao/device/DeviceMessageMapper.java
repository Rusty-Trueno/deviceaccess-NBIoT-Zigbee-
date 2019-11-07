package cn.edu.bupt.dao.device;

import cn.edu.bupt.pojo.DeviceMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by rongshuai on 2019/11/5 20:26
 */
@Mapper
public interface DeviceMessageMapper {
    @Insert("INSERT INTO devicemessage(deviceId,phoneNumber) VALUES (#{deviceId},#{phoneNumber})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    Integer addDeviceMessage(DeviceMessage deviceMessage);//设备报警电话的插入

    @Select("SELECT * FROM devicemessage WHERE deviceId = #{deviceId}")
    List<DeviceMessage> getDeviceMessageById(@Param("deviceId")String deviceId);

    @Delete("DELETE FROM devicemessage WHERE deviceId = #{deviceId} AND phoneNumber = #{phoneNumber}")
    Integer deleteDeviceMessage(@Param("deviceId") String deviceId,@Param("phoneNumber") String phoneNumber);
}
