package cn.edu.bupt.dao.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rongshuai on 2019/11/12 13:09
 */
@Service
public class NbiotDeviceDao {
    @Autowired
    private NBIoTDeviceMapper nbIoTDeviceMapper;

    public Boolean updateNbiotDevice(String parentDeviceId,String mac){
        return nbIoTDeviceMapper.updateNbiotDevice(parentDeviceId,mac);
    }

    public String selectNbDeviceIdByMac(String mac){
        return nbIoTDeviceMapper.selectNbDeviceIdByMac(mac);
    }
}
