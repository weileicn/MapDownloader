package com.baidumap.downloader.models;

/**
 * Created by weilei04 on 2017/2/13.
 */
public class Apk {

    private String id;
    private String version;
    private String url;

    public Apk(String id, String version,String url) {
        this.id = id;
        this.version = version;
        this.url = url;
    }

    public String getId() {
        return id;
    }
    public String getVersion() {
        return version;
    }

    public String getUrl (){
        return url;
    }

    public void setId (String id) {
        this.id = id;
    }

    public void setVersion (String version) {
        this.version = version;
    }

    public void setUrl (String url) {
        this.url = url;
    }
}
