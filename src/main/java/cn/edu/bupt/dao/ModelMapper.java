package cn.edu.bupt.dao;

import cn.edu.bupt.pojo.Model;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ModelMapper {

    @Select("SELECT model.* FROM model, manufacturer, device_type " +
            "WHERE model.device_type_id = device_type.device_type_id " +
            "AND device_type.device_type_name = #{device_type_name} " +
            "AND manufacturer.manufacturer_id = model.manufacturer_id " +
            "AND manufacturer.manufacturer_name = #{manufacturer_name} " +
            "AND model.model_name = #{model_name}")
    Model getModel (@Param("manufacturer_name")String manufacturer_name, @Param("device_type_name")String device_type_name, @Param("model_name")String model_name);
}