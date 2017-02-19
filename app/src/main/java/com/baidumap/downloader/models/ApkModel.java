package com.baidumap.downloader.models;

import java.io.Serializable;

/**
 * Created by weilei04 on 2017/2/19.
 */
public class ApkModel implements Serializable {
    private String id;
    private String version;
    private String url;
    private String branch;

    public ApkModel() {
    }

    public ApkModel(String id, String version, String url,String branch) {
        this.id = id;
        this.version = version;
        this.url = url;
        this.branch = branch;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    public String getBranch() {
        return branch;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
