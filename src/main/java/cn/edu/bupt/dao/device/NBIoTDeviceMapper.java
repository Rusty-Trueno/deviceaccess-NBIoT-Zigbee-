package cn.edu.bupt.dao.device;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by rongshuai on 2019/11/12 13:03
 */
@Mapper
public interface NBIoTDeviceMapper {
    @Update("UPDATE nbiotdevice SET parentDeviceId = #{parentDeviceId} WHERE mac = #{mac}")
    Boolean updateNbiotDevice(@Param("parentDeviceId") String parentDeviceId,@Param("mac") String mac);

    @Select("SELECT deviceId FROM nbiotdevice WHERE mac = #{mac}")
    String selectNbDeviceIdByMac(String mac);
}
