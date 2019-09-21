package cn.edu.bupt.service;

import cn.edu.bupt.dao.ModelMapper;
import cn.edu.bupt.pojo.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelService {

    @Autowired
    private ModelMapper modelMapper;

    public Model getModel(String manufacturer_name, String device_type_name, String model_name){
        return modelMapper.getModel(manufacturer_name, device_type_name, model_name);
    }
}
