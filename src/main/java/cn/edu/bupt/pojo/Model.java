package cn.edu.bupt.pojo;

import lombok.Data;

@Data
public class Model {
    Integer model_id;
    Integer manufacturer_id;
    Integer device_type;
    String model_name;
    String device_icon;
    Long limit_lifetime;

    public Model(Integer model_id, Integer manufacturer_id, Integer device_type, String model_name, String device_icon, String limit_lifetime){
        this.model_id = model_id;
        this.manufacturer_id = manufacturer_id;
        this.device_type = device_type;
        this.model_name = model_name;
        try{
            this.device_icon = device_icon;
        }catch (Exception e){
            this.device_icon = "";
        }

        try {
            this.limit_lifetime = Long.parseLong(limit_lifetime);
        }catch (Exception e){
            this.limit_lifetime = 60000L;
        }
    }

}
