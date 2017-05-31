package com.baidumap.downloader.models.response;

import java.util.List;

/**
 * Created by weilei04 on 17/2/19.
 */
public class ArrayResponse<T> extends BaseResponse {
    private List<T> objList;

    public List<T> getObjList() {
        return objList;
    }

    public void setObjList(List<T> objList) {
        this.objList = objList;
    }
}
