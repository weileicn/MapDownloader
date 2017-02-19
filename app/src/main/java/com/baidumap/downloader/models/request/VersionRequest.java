package com.baidumap.downloader.models.request;

/**
 * Created by weilei04 on 2017/2/19.
 */
public class VersionRequest extends BaseRequest {
    private String version;

    public VersionRequest(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setImei(String version) {
        this.version = version;
    }

}
