package cn.edu.bupt.service;

import cn.edu.bupt.dao.device.DeviceByGroupIdDao;
import cn.edu.bupt.dao.device.DeviceDao;
import cn.edu.bupt.dao.device.GroupDao;
import cn.edu.bupt.dao.exception.DataValidationException;
import cn.edu.bupt.dao.page.TextPageData;
import cn.edu.bupt.dao.page.TextPageLink;
import cn.edu.bupt.dao.util.DataValidator;
import cn.edu.bupt.dao.util.PaginatedRemover;
import cn.edu.bupt.pojo.Device;
import cn.edu.bupt.pojo.DeviceByGroupId;
import cn.edu.bupt.pojo.DeviceCredentials;
//import cn.edu.bupt.security.HttpUtil;
import com.google.gson.*;
import okhttp3.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static cn.edu.bupt.dao.util.Validator.*;

/**
 * Created by Administrator on 2018/4/14.
 */
@Service
public class DeviceServiceImpl implements DeviceService, InitializingBean{

    public static final String INCORRECT_TENANT_ID = "Incorrect tenantId ";
    public static final String INCORRECT_PAGE_LINK = "Incorrect page link ";
    public static final String INCORRECT_CUSTOMER_ID = "Incorrect customerId ";
    public static final String INCORRECT_DEVICE_ID = "Incorrect deviceId ";
    public static final String INCORRECT_GROUP_ID = "Incorrect groupId ";
    public static final String INCORRECT_MANUFACTURE = "Incorrect manufacture ";
    public static final String INCORRECT_DEVICE_TYPE = "Incorrect device type ";
    public static final String INCORRECT_MODEL = "Incorrect model ";
    public static final String INCORRECT_SITE_ID = "Incorrect siteId ";
    public static final String INCORRECT_TEXT_SEARCH = "Incorrect textSearch ";
    @Autowired
    private DeviceDao deviceDao;

    //private HttpUtil httpUtil = new HttpUtil();
//
//    @Autowired
//    private TenantDao tenantDao;
//
//    @Autowired
//    private CustomerDao customerDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private DeviceCredentialsService deviceCredentialsService;

    @Autowired
    private DeviceByGroupIdDao deviceByGroupIdDao;

    @Override
    public Long findDevicesCountWithTextSearch(Integer tenantId, TextPageLink pageLink){
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
//        validateString(pageLink.getTextSearch(), INCORRECT_TEXT_SEARCH + pageLink.getTextSearch());
        Long count = deviceDao.findDevicesCountByTenantId(tenantId, pageLink);
        return count;
    }

    @Override
    public Long findDevicesCountWithTextSearch(Integer tenantId, Integer customerId, TextPageLink pageLink){
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);
//        validateString(pageLink.getTextSearch(), INCORRECT_TEXT_SEARCH + pageLink.getTextSearch());
        Long count = deviceDao.findDevicesCountByCustomerId(tenantId, customerId, pageLink);
        return count;
    }

    @Override
    public Long findDevicesCount(Integer tenantId){
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        return deviceDao.findDevicesCount(tenantId);
    }

    @Override
    public Long findCustomerDevicesCount(Integer customerId){
        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);
        return deviceDao.findCustomerDevicesCount(customerId);
    }

    @Override
    public Device updateDeviceSiteId(UUID deviceId, Integer siteId){
        validateId(deviceId, INCORRECT_DEVICE_ID + deviceId);
        validateId(siteId, INCORRECT_SITE_ID + siteId);
        Device device = findDeviceById(deviceId);
        device.setSiteId(siteId);
        return deviceDao.save(device);
    }

    //*********查找组中设备**********
    @Override
    public TextPageData<Device> findDevicesByGroupId(UUID groupId, TextPageLink pageLink) {
        validateId(groupId, INCORRECT_GROUP_ID + groupId);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Device> devices = new ArrayList<Device>();
        List<UUID> deviceIds = deviceByGroupIdDao.findDevicesByGroupId(groupId);
        for(UUID deviceId : deviceIds){
            devices.add(deviceDao.findById(deviceId));
        }
        return new TextPageData<>(devices, pageLink);
    }

    //******分配相应设备到对应的设备组******
    @Override
    public void assignDeviceToGroup(UUID deviceId, UUID groupId) {
        DeviceByGroupId deviceByGroupId = new DeviceByGroupId(groupId,deviceId);
        deviceByGroupIdDao.save(deviceByGroupId);
    }

    @Override
    public void unassignDeviceFromGroup(UUID deviceId , UUID groupId){
        validateId(deviceId, INCORRECT_DEVICE_ID + deviceId);
        validateId(groupId, INCORRECT_GROUP_ID + groupId);
        deviceByGroupIdDao.delete(new DeviceByGroupId(groupId,deviceId));
    }

    //******Unassign设备组中的所有设备******
    @Override
    public void unassignDevicesByGroupId(UUID groupId) {
        validateId(groupId, INCORRECT_GROUP_ID + groupId);
        deviceByGroupIdDao.deleteAllByGroupId(groupId);
    }

    //******根据parentDeviceId寻找设备******
    @Override
    public List<Device> findDeviceByParentDeviceId(String parentDeviceId, TextPageLink pageLink) {
        return deviceDao.findDevicesByParentDeviceId(parentDeviceId,pageLink);
    }



    @Override
    public Device findDeviceById(UUID deviceId) {
        validateId(deviceId, INCORRECT_DEVICE_ID + deviceId);
        return deviceDao.findById(deviceId);
    }

    @Override
    public Optional<Device> findDeviceByTenantIdAndName(Integer tenantId, String name) {
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        Optional<Device> deviceOpt = deviceDao.findDeviceByTenantIdAndName(tenantId, name);
        if (deviceOpt.isPresent()) {
            return Optional.of(deviceOpt.get());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Device saveDevice(Device device) {
        deviceValidator.validate(device);
        UUID deviceId = device.getId();
        Device savedDevice = deviceDao.save(device);
        if (deviceId == null) {
            DeviceCredentials deviceCredentials = new DeviceCredentials();
            deviceCredentials.setDeviceId(savedDevice.getId());
            deviceCredentials.setDeviceToken(RandomStringUtils.randomAlphanumeric(20));
            deviceCredentialsService.createDeviceCredentials(deviceCredentials);
        }
        return savedDevice;
    }

    @Override
    public Device assignDeviceToCustomer(UUID deviceId, Integer customerId) {
        Device device = findDeviceById(deviceId);
        device.setCustomerId(customerId);
        return saveDevice(device);
    }

    @Override
    public Device unassignDeviceFromCustomer(UUID deviceId) {
        Device device = findDeviceById(deviceId);
        device.setCustomerId(null);
        return saveDevice(device);
    }

    @Override
    public void deleteDevice(UUID deviceId) {
        validateId(deviceId, INCORRECT_DEVICE_ID + deviceId);
        DeviceCredentials deviceCredentials = deviceCredentialsService.findDeviceCredentialsByDeviceId(deviceId);
        if (deviceCredentials != null) {
            deviceCredentialsService.deleteDeviceCredentials(deviceCredentials);
        }
        List<UUID> groupIds = deviceByGroupIdDao.findGroupsByDeviceId(deviceId);
        for(UUID groupId : groupIds){
            deviceByGroupIdDao.delete(new DeviceByGroupId(groupId,deviceId));
        }
        deviceDao.removeById(deviceId);
    }

    @Override
    public TextPageData<Device> findDevicesByTenantId(Integer tenantId, TextPageLink pageLink) {
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Device> devices = deviceDao.findDevicesByTenantId(tenantId, pageLink);
        return new TextPageData<>(devices, pageLink);
    }

    @Override
    public void deleteDevicesByTenantId(Integer tenantId) {
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        tenantDevicesRemover.removeEntities(tenantId);
    }

    @Override
    public TextPageData<Device> findDevicesByTenantIdAndCustomerId(Integer tenantId, Integer customerId, TextPageLink pageLink) {
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Device> devices = deviceDao.findDevicesByTenantIdAndCustomerId(tenantId, customerId, pageLink);
        return new TextPageData<>(devices, pageLink);
    }


    //TODO: 加入tenantId和CustomerId作为参数
    @Override
    public TextPageData<Device> findDevicesByManufactureAndDeviceTypeAndModel(String manufacture, String deviceType, String model, TextPageLink pageLink){
        validateString(manufacture,INCORRECT_MANUFACTURE);
        validateString(deviceType,INCORRECT_DEVICE_TYPE);
        validateString(model,INCORRECT_MODEL);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Device> devices = deviceDao.findDevicesByManufactureAndDeviceTypeAndModel(manufacture,deviceType,model,pageLink);
        return new TextPageData<>(devices, pageLink);
    }

    @Override
    public TextPageData<Device> findDevicesByDeviceType(String deviceType, TextPageLink pageLink){
        validateString(deviceType,INCORRECT_DEVICE_TYPE);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Device> devices = deviceDao.findDevicesByDeviceType(deviceType,pageLink);
        return new TextPageData<>(devices, pageLink);
    }

    @Override
    public TextPageData<Device> findAllAssignGateways(String manufacture, String deviceType, TextPageLink pageLink) {
        validateString(manufacture, INCORRECT_MANUFACTURE);
        validateString(deviceType, INCORRECT_DEVICE_TYPE);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Device> devices = deviceDao.findDevicesByManufactureAndDeviceType(manufacture, deviceType, pageLink);
        List<Device> result = new ArrayList<>();
        for(Device device : devices) {
            if(device.getCustomerId() != 1){
                result.add(device);
            }
        }
        return new TextPageData<>(result, pageLink);
    }


    @Override
    public void unassignCustomerDevices(Integer tenantId, Integer customerId) {
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateId(customerId, INCORRECT_CUSTOMER_ID + customerId);

        new CustomerDevicesUnassigner(tenantId).removeEntities(customerId);
    }

    @Override
    public TextPageData<Device> findDevicesByTenantIdAndSiteId(Integer tenantId, Integer siteId, TextPageLink pageLink){
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validateId(siteId, INCORRECT_SITE_ID + siteId);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Device> devices = deviceDao.findDevicesByTenantIdAndSiteId(tenantId,siteId,pageLink);
        return new TextPageData<>(devices, pageLink);
    }

    @Override
    public TextPageData<Device> findDevices(Integer tenantId, TextPageLink pageLink){
        validateId(tenantId, INCORRECT_TENANT_ID + tenantId);
        validatePageLink(pageLink, INCORRECT_PAGE_LINK + pageLink);
        List<Device> devices = deviceDao.findDevices(tenantId,pageLink);
        return new TextPageData<>(devices, pageLink);
    }

    @Override
    public void suspendedDeviceByTenantId(Integer tenantId) {
        new TenantDevicesSuspended().removeEntities(tenantId);
    }

    @Override
    public void activatedDeviceByTenantId(Integer tenantId) {
        new TenantDevicesActivated().removeEntities(tenantId);
    }

    private DataValidator<Device> deviceValidator =
            new DataValidator<Device>() {

                @Override
                protected void validateCreate(Device device) {
                    deviceDao.findDeviceByTenantIdAndName(device.getTenantId(), device.getName()).ifPresent(
                            d -> {
                                throw new DataValidationException("Device with such name already exists!");
                            }
                    );
                }

                @Override
                protected void validateUpdate(Device device) {
                    deviceDao.findDeviceByTenantIdAndName(device.getTenantId(), device.getName()).ifPresent(
                            d -> {
                                if (!d.getId().equals(device.getId())) {
                                    throw new DataValidationException("Device with such name already exists!");
                                }
                            }
                    );
                    device.setSiteId(deviceDao.findById(device.getId()).getSiteId());
                }

                @Override
                protected void validateDataImpl(Device device) {
                    if (StringUtils.isEmpty(device.getName())) {
                        throw new DataValidationException("Device name should be specified!");
                    }
                    if (device.getTenantId() == null || device.getTenantId()==1) {
                        throw new DataValidationException("Device should be assigned to tenant!");
                    }
                    if (device.getCustomerId() == null) {
                        device.setCustomerId(1);
                    }
                    if (StringUtils.isEmpty(device.getManufacture())) {
                        device.setManufacture("default");
//                        throw new DataValidationException("Device manufacture should be specified!");
                    }
                    if (StringUtils.isEmpty(device.getDeviceType())) {
                        device.setDeviceType("default");
//                        throw new DataValidationException("Device type should be specified!");
                    }
                    if (StringUtils.isEmpty(device.getModel())) {
                        device.setModel("default");
//                        throw new DataValidationException("Device model should be specified!");
                    }
                    if (device.getLifeTime() == null) {
                        device.setLifeTime(0L);
//                        throw new DataValidationException("Device model should be specified!");
                    }
                }
            };

    private PaginatedRemover<Integer,Device> tenantDevicesRemover =
            new PaginatedRemover<Integer,Device>() {

                @Override
                protected List<Device> findEntities(Integer id, TextPageLink pageLink) {
                    return deviceDao.findDevicesByTenantId(id, pageLink);
                }

                @Override
                protected void removeEntity(Device entity) {
                    deleteDevice(entity.getId());
                }
            };

    private class CustomerDevicesUnassigner extends PaginatedRemover<Integer, Device> {

        private Integer tenantId;

        CustomerDevicesUnassigner(Integer tenantId) {
            this.tenantId = tenantId;
        }

        @Override
        protected List<Device> findEntities(Integer id, TextPageLink pageLink) {
            return deviceDao.findDevicesByTenantIdAndCustomerId(tenantId, id, pageLink);
        }

        @Override
        protected void removeEntity(Device entity) {
            unassignDeviceFromCustomer(entity.getId());
        }

    }

    private class TenantDevicesSuspended extends PaginatedRemover<Integer, Device> {

        @Override
        protected List<Device> findEntities(Integer id, TextPageLink pageLink) {
            return deviceDao.findDevices(id,pageLink);
        }

        @Override
        protected void removeEntity(Device entity) {
            DeviceCredentials deviceCredentials = deviceCredentialsService.findDeviceCredentialsByDeviceId(entity.getId());
            deviceCredentials.setSuspended(Boolean.TRUE);
            deviceCredentialsService.updateDeviceCredentials(deviceCredentials);
        }

    }

    private class TenantDevicesActivated extends PaginatedRemover<Integer, Device> {

        @Override
        protected List<Device> findEntities(Integer id, TextPageLink pageLink) {
            return deviceDao.findDevices(id,pageLink);
        }

        @Override
        protected void removeEntity(Device entity) {
            DeviceCredentials deviceCredentials = deviceCredentialsService.findDeviceCredentialsByDeviceId(entity.getId());
            deviceCredentials.setSuspended(Boolean.FALSE);
            deviceCredentialsService.updateDeviceCredentials(deviceCredentials);
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception{
        String textSearch = null;
        String idOffset = null;
        String textOffset = null;
        TextPageData pageData;

        Observable
                .interval(1, TimeUnit.DAYS)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        try {
                            //List<Tenant> tenants= getTenants();
                            TextPageData pageData;
//                            for (Tenant tenant: tenants){
//                                TextPageLink pageLink = new TextPageLink(1000, textSearch,idOffset==null?null:UUID.fromString(idOffset), textOffset);
//                                pageData = findDevices(tenant.getId() , pageLink);
//                                checkData(pageData);
//
//                                while(pageData.hasNext()) {
//                                    pageData = findDevices(tenant.getId() , pageData.getNextPageLink());
//                                    checkData(pageData);
//                                }
//                            }
                        }  catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

//    private List<Tenant> getTenants() throws Exception {
//        //Gson gs = new Gson();
//        List<Tenant> tenants = new ArrayList<>();
//        int page = 0;
//
//        while(true) {
//            String response = httpUtil.sendGet("http://account:8400/api/v1/account/tenants?limit=1000&page="+page,null);
//
//            if (response!=null) {
//                JsonArray jsonArray = new JsonParser().parse(response).getAsJsonArray();
//
//                if(jsonArray.size()==0){
//                    return tenants;
//                }
//
//                for(JsonElement jsonElement:jsonArray){
//                    JsonObject jsonObject = jsonElement.getAsJsonObject();
//                    tenants.add(JSON.parseObject(jsonObject.toString(), Tenant.class));
//                }
//                page++;
//            } else {
//                throw new IOException("Unexpected code " + response);
//            }
//        }
//    }

    public String sendMessage(Device device, String message){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message",message);
        jsonObject.addProperty("messageType", "fromModule");
        jsonObject.addProperty("ts", System.currentTimeMillis());
        jsonObject.addProperty("tenantId",device.getTenantId());


        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , jsonObject.toString());

        System.out.println(requestBody);

        Request request = new Request.Builder()
                .url("http://127.0.0.1:8900/api/v1/updatemessageplugin/updateMessage/insert")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    System.out.println("Success");
                }
            }
        });
        return "Success";
    }

    public void checkData(TextPageData pageData){
        String message = null;
        for(Object object:pageData.getData())
        {
            Device device = (Device)object;
            if((device.getLifeTime()-System.currentTimeMillis())<=15552000000L && (device.getLifeTime()-System.currentTimeMillis())>15465600000L ){
                message = device.getName()+"设备距离检修年限不足6个月";
                sendMessage(device,message);
            }
            if((device.getLifeTime()-System.currentTimeMillis())<=2592000000L && (device.getLifeTime()-System.currentTimeMillis())>2505600000L ){
                message = device.getName()+"设备距离检修年限不足1个月";
                sendMessage(device,message);
            }
        }
    }

}
