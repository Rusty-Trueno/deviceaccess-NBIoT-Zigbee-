package cn.edu.bupt.controller;

import cn.edu.bupt.dao.page.TextPageData;
import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.pojo.Group;
import cn.edu.bupt.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/deviceaccess")
public class GroupController extends BaseController {

    public static final String DEVICE_ID = "deviceId";
    public static final String GROUP_ID = "groupId";

    //设备层面的设备组
    //获取设备组下的所有设备
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/group/devices/{groupId}",params = {"limit"}, method = RequestMethod.GET)
    public TextPageData<Device> getDevicesByGroupId(
            @PathVariable(GROUP_ID) String strGroupId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws Exception {
        try {
            checkParameter("groupId", strGroupId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        try{
            TextPageLink pageLink = new TextPageLink(limit, textSearch, idOffset==null?null:toUUID(idOffset), textOffset);
            TextPageData<Device> devices = deviceService.findDevicesByGroupId(toUUID(strGroupId), pageLink);
            return devices;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //分配设备到设备组
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/assign/group/{groupId}/{deviceId}", method = RequestMethod.GET)
    public void assignDeviceToGroup(@PathVariable(GROUP_ID) String groupId, @PathVariable(DEVICE_ID) String deviceId) throws Exception{
        try{
            deviceService.assignDeviceToGroup(UUID.fromString(deviceId),UUID.fromString(groupId));
        }catch(Exception e){
            e.printStackTrace();
            return ;
        }
    }


    //从设备组取消分配所有设备
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/unassign/group/{groupId}", method = RequestMethod.DELETE)
    public void unassignDevicesFromGroup(@PathVariable(GROUP_ID) String groupId) throws Exception{
        try{
            deviceService.unassignDevicesByGroupId(UUID.fromString(groupId));
        }catch(Exception e){
            e.printStackTrace();
            return ;
        }
    }


    //从设备组取消分配某一个设备
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/unassign/group/{groupId}/{deviceId}", method = RequestMethod.DELETE)
    public void unassignDeviceByGroupId(@PathVariable(GROUP_ID) String groupId, @PathVariable(DEVICE_ID) String deviceId) throws Exception{
        try{
            deviceService.unassignDeviceFromGroup(UUID.fromString(deviceId),UUID.fromString(groupId));
        }catch(Exception e){
            e.printStackTrace();
            return ;
        }
    }


    //设备组层面的设备组
    //创建
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public String saveGroup(@RequestBody String group) throws Exception{

        //表单转变为json提交
        try {
            Group group1 = JSON.parseObject(group, Group.class);
            Group savedGroup = checkNotNull(groupService.saveGroup(group1));
            return savedGroup.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return onFail(e.toString());
        }
    }

    //删除
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/group/{groupId}", method = RequestMethod.DELETE)
    public void deleteGroup(@PathVariable(GROUP_ID) String strGroupId) throws Exception{
        if(StringUtil.isEmpty(strGroupId)){
          return ;
        }
        try{
            groupService.deleteGroup(toUUID(strGroupId));
        }catch (Exception e){
            e.printStackTrace();
            return ;
        }
    }

    //查找所有设备组
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/groups/tenant/{tenantId}",params = {"limit"}, method = RequestMethod.GET)
    public TextPageData<Group> getGroupsByTenantId(
            @PathVariable("tenantId") Integer tenantId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws Exception{
        try{
            TextPageLink pageLink = new TextPageLink(limit, textSearch, idOffset==null?null:toUUID(idOffset), textOffset);
            TextPageData<Group> tenantgroups = groupService.findGroupsByTenantId(tenantId, pageLink);
            return tenantgroups;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //获取客户管理的所有设备组
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('CUSTOMER_USER')")
    @RequestMapping(value = "/groups/customer/{customerId}",params = {"limit"}, method = RequestMethod.GET)
    public TextPageData<Group> getGroupsByCustomerId(
            @PathVariable("customerId") Integer customerId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws Exception{
        try{
            TextPageLink pageLink = new TextPageLink(limit, textSearch, idOffset==null?null:toUUID(idOffset), textOffset);
            TextPageData<Group> customergroups = groupService.findGroupsByCustomerId(customerId, pageLink);
            return customergroups;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //通过tenant和customer找group
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAuthority('CUSTOMER_USER')")
    @RequestMapping(value = "/groups/{tenantId}/{customerId}",params = {"limit"}, method = RequestMethod.GET)
    public TextPageData<Group> getGroupsByTenantIdAndCustomerId(
            @PathVariable("tenantId") Integer tenantId,
            @PathVariable("customerId") Integer customerId,
            @RequestParam int limit,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String idOffset,
            @RequestParam(required = false) String textOffset) throws Exception{
        try{
            TextPageLink pageLink = new TextPageLink(limit, textSearch, idOffset==null?null:toUUID(idOffset), textOffset);
            TextPageData<Group> customergroups = groupService.findGroupsByTenantIdAndCustomerId(tenantId, customerId, pageLink);
            return customergroups;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }




}
