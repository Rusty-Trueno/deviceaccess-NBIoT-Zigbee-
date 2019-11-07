package cn.edu.bupt.dao.tenant;

import cn.edu.bupt.pojo.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by CZX on 2018/4/8.
 */
public interface TenantRepository extends JpaRepository<Tenant, Integer>,JpaSpecificationExecutor<Tenant> {

}
