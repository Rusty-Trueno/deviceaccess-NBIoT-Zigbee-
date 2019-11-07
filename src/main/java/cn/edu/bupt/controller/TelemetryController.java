package cn.edu.bupt.controller;


import cn.edu.bupt.pojo.kv.Aggregation;
import cn.edu.bupt.pojo.kv.BaseTsKvQuery;
import cn.edu.bupt.pojo.kv.TsKvEntry;
import cn.edu.bupt.pojo.kv.TsKvQuery;
import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/deviceaccess/data")
public class TelemetryController extends BaseController {

    //通过设备ID和查询内容获取所有历史数据
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value="/alldata/{deviceId}",method = RequestMethod.GET)
    public List<TsKvEntry> getAllData(@PathVariable("deviceId") String deviceId,
                                      @RequestParam String key,
                                      @RequestParam String startTs,
                                      @RequestParam String endTs,
                                      @RequestParam int interval,
                                      @RequestParam int limit,
                                      @RequestParam String aggregation
                                      ) throws Exception {
        try{
            List<TsKvQuery> queries = new ArrayList<>();
            TsKvQuery tsKvQuery = new BaseTsKvQuery(key, Long.parseLong(startTs), Long.parseLong(endTs), interval, limit, Aggregation.valueOf(aggregation));
            queries.add(tsKvQuery);
            ListenableFuture<List<TsKvEntry>> listListenableFuture = baseTimeseriesService.findAll(toUUID(deviceId),queries);
            List<TsKvEntry> ls = listListenableFuture.get();
            return ls;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //通过设备ID获取所有键的最新数据
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/alllatestdata/{deviceId}", method = RequestMethod.GET)
    public List<TsKvEntry> getlatestData(@PathVariable("deviceId") String deviceId)
    throws Exception{
        try{
            ListenableFuture<List<TsKvEntry>> tskventry = baseTimeseriesService.findAllLatest(toUUID(deviceId));
            List<TsKvEntry> ls = tskventry.get();
            return ls;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    //设备ID和键获取最新数据
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/latestdata/{deviceId}/{keys}", method = RequestMethod.GET)
    public List<TsKvEntry> getlatestData(@PathVariable("deviceId") String deviceId
    ,@PathVariable("keys") Collection<String> keys)
            throws Exception{
        try{
            ListenableFuture<List<TsKvEntry>> tskventry = baseTimeseriesService.findLatest(toUUID(deviceId), keys);
            List<TsKvEntry> ls = tskventry.get();
            return ls;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //获取所有的数据的键类型
    //@PreAuthorize("#oauth2.hasScope('all') OR hasAnyAuthority('TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/allKeys/{deviceId}", method = RequestMethod.GET)
    public List<String> findAllKeys(@PathVariable("deviceId") String deviceId) throws Exception{
        try{
            ListenableFuture<List<String>> listListenableFuture = baseTimeseriesService.findAllKeys(toUUID(deviceId));
            List<String> ls = listListenableFuture.get();
            return ls;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}