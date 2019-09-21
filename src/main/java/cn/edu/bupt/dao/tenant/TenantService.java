package cn.edu.bupt.dao.tenant;

import cn.edu.bupt.pojo.Tenant;
import org.springframework.data.domain.Page;

/**
 * Created by CZX on 2018/4/9.
 */
public interface TenantService {

    Tenant findTenantById(Integer tenantId);

    Tenant saveTenant(Tenant tenant);

    void deleteTenant(Integer tenantId);

    Page<Tenant> findTenants(Integer page, Integer size);

}
