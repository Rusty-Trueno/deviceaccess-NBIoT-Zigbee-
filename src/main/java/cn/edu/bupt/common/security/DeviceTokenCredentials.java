package cn.edu.bupt.common.security;

import cn.edu.bupt.pojo.DeviceCredentals;

/**
 * Created by Administrator on 2018/4/13.
 */
public class DeviceTokenCredentials implements DeviceCredentals {

    private final String token;

    public DeviceTokenCredentials(String token) {
        this.token = token;
    }

    @Override
    public String getCredentialsId() {
        return token;
    }

    @Override
    public DeviceCredentialsType getCredentialsType() {
        return null;
    }
}
