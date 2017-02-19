package com.baidumap.downloader.models;

import java.io.Serializable;

/**
 * Created by weilei04 on 2017/2/19.
 */
public class VersionModel implements Serializable{
    private String version;

    public VersionModel(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
