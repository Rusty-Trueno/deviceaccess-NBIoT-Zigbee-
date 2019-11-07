package cn.edu.bupt.dao.tenant;

import cn.edu.bupt.pojo.Tenant;
import cn.edu.bupt.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by CZX on 2018/4/9.
 */
@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private DeviceService deviceService;

    @Override
    public Tenant findTenantById(Integer tenantId){
        return tenantRepository.findOne(tenantId);
    }

    @Override
    public Tenant saveTenant(Tenant tenant){
//        tenantValidator.validate(tenant);
        return tenantRepository.save(tenant);
    }

    @Override
    public void deleteTenant(Integer tenantId){
//        deviceService.deleteDevicesByTenantId(tenantId);
        tenantRepository.delete(tenantId);
    }

    @Override
    public Page<Tenant> findTenants(Integer page, Integer size){
        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
        Page<Tenant> tenantPage = tenantRepository.findAll(pageable);
        return tenantPage;
    }
}
