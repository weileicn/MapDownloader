package com.baidumap.downloader.network;

/**
 * Created by weilei04 on 2017/2/13.
 */
public class HttpUtil {
    private static HttpUtil sInstance;

    public static HttpUtil getInstance() {
        if (sInstance == null) {
            sInstance = new HttpUtil();
        }
        return sInstance;
    }

//    String call(String url) {
//        //创建okHttpClient对象
//        OkHttpClient mOkHttpClient = new OkHttpClient();
//        //创建一个Request
//        final Request request = new Request.Builder()
//                .url("https://github.com/hongyangAndroid")
//                .build();
//        //new call
//        Call call = mOkHttpClient.newCall(request);
//        //请求加入调度
//        call.enqueue(new Callback()
//        {
//            @Override
//            public void onFailure(Request request, IOException e)
//            {
//            }
//
//            @Override
//            public void onResponse(final Response response) throws IOException
//            {
//                //String htmlStr =  response.body().string();
//            }
//        });
//
//    }
}
