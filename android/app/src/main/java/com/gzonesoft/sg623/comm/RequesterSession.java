package com.gzonesoft.sg623.comm;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gzonesoft.sg623.domain.CommType;
import com.gzonesoft.sg623.util.Loggers;
import com.gzonesoft.sg623.util.Svc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.EventListener;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;


/**
 * 서버 통신요청 관리
 */
public class RequesterSession extends EventListener {

    private String tag = "RequesterSession";

    public static final int REQ_LOGIN_OK = 00;
    public static final int REQ_ERR_NOT_RESPONSE = 10;
    public static final int REQ_OK = 100;
    public static final int REQ_OK_MESSAGE = 110;
    public static final int REQ_NOK = 200;
    public static final int REQ_OK_MESSAGE_TO_NEXT = 201;
    public static final int REQ_FAIL = -10;
    public static final int NO_DATA_FOUND = 900;


    OkHttpClient client = new OkHttpClient().newBuilder()
            //.addNetworkInterceptor(new StethoInterceptor()) // Setho 코드..
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();


    private static RequesterSession mRequestSession;
    private RequesterSession() {
    }
    private RequesterSession(Context ctx) {
    }
    public static RequesterSession with() {
        if (mRequestSession == null) {
            Loggers.d("---------[[RequesterSession with new]]--------");
            mRequestSession = new RequesterSession();
        }
        return mRequestSession;
    }

    /**
     * POST 요청객체 얻기
     * @param body
     * @param path
     * @return
     */
    private Request getRequestInstancePost(RequestBody body, String path) {

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(Svc.SCHEME_HTTPS)
                .host(Svc.SERVER_ADDR)
                .port(Svc.SERVICE_PORT)
                .addPathSegments(path)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .post(body)
                //.addHeader("Accept", "application/json")
                .build();

        Loggers.d("[getRequestInstancePost] httpUrl===>" + httpUrl);

        return request;
    }


    /**
     * GET 요청객체 얻기 - 서버고정
     * @param path
     * @return
     */
    private Request getRequestInstance_Get(String path) {

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(Svc.SCHEME_HTTPS)
                .host(Svc.SERVER_ADDR)
                .port(Svc.SERVICE_PORT)
                .addPathSegments(path)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .build();

        Loggers.d("[getRequestInstance_Get] httpUrl===>" + httpUrl);

        return request;
    }

    /**
     * POST 요청객체 얻기(With 세션값을 해더에 추가하는 경우) - 서버고정
     * @param body
     * @param path
     * @param sessionValue
     * @return
     */
    private Request getRequestInstancePostWithHeader(RequestBody body, String path, String sessionValue) {

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(Svc.SCHEME_HTTPS)
                .host(Svc.SERVER_ADDR)
                .port(Svc.SERVICE_PORT)
                .addPathSegments(path)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .post(body)
                .addHeader("Cookie", sessionValue)
                .build();

        Loggers.d("[getRequestInstancePostWithHeader] httpUrl===>" + httpUrl);

        return request;
    }

    /**
     * GET 요청객체 얻기(With 세션값을 해더에 추가하는 경우) - 서버고정 안함
     * @param URLstring
     * @param sessionValue
     * @return
     */
    private Request getRequestInstanceGetWithHeader(String URLstring, String sessionValue) {

        Request request = new Request.Builder()
                .url(URLstring)
                .addHeader("Cookie", sessionValue)
                .build();

        Loggers.d("[getRequestInstanceGetWithHeader] httpUrl===>" + URLstring);

        return request;
    }


    // -------------------------------------------------------------------------------------------
    // 서버 서비스요청,
    // -------------------------------------------------------------------------------------------
    // 1.formBuilder = 요청입력 파라미터
    // 2.serviceName = 서버 서비스 URL
    // 3.callback = 콜백함수
    // -------------------------------------------------------------------------------------------
//    public void commServiceCall(Activity activity, FormBody.Builder formBuilder, String serviceName, final Callback cb) {
//
//        RequestBody formBody = formBuilder.build();
//
//        Request request = getRequestInstance(formBody, serviceName);
//
//        try {
//
//            Loggers.d("----------------------------------------------------------------------");
//            Loggers.d("[Method] = " + request.method().toString());
//            Loggers.d("[ServiceName] = " + serviceName);
//            Loggers.d("[Request Body] = " + bodyToString(request));
//            Loggers.d("[Length] = " + formBody.contentLength());
//            Loggers.d("----------------------------------------------------------------------");
//
//        } catch (IOException e) {
//            Loggers.e("Error " + e.toString(), e);
//        }
//        Loggers.d("000 - 호출전 ================================================= ");
//        client.newCall(request).enqueue(cb);
//        Loggers.d("000 - 호출후 ================================================= ");
//
//
//    }

    // 헤더에 넣을 세션값
    private String sessionValue;

    /**
     * Http 서비스 호출(POST) - 사전에 서버정보가 고정되어있음.
      * @param formBuilder
     * @param serviceName
     * @param callback
     */
    public void HttpServiceCallPost(FormBody.Builder formBuilder, String serviceName, Callback callback) {
        RequestBody formBody = formBuilder.build();

        Request request = null;

        sessionValue = AppSetting.Login.SDS_SESSION;
        if (!TextUtils.isEmpty(sessionValue)) { // 헤더에 넣을 세션값이 있는경우..
            Loggers.d("[HttpServiceCall-sessionValue] = " + sessionValue);
            request = getRequestInstancePostWithHeader(formBody, serviceName, sessionValue);
        } else { // 헤더에 넣을 세션값이 없는경우..
            request = getRequestInstancePost(formBody, serviceName);
        }

        try {

            Loggers.d("----------------------------------------------------------------------");
            Loggers.d("[HttpServiceCallPost-Method] = " + request.method().toString());
            Loggers.d("[HttpServiceCallPost-ServiceName] = " + serviceName);
            Loggers.d("[HttpServiceCallPost-Header String] = " + request.headers().toString());
            Loggers.d("[HttpServiceCallPost-Request Body] = " + bodyToString(request));
            Loggers.d("[HttpServiceCallPost-Content Length] = " + formBody.contentLength());
            Loggers.d("----------------------------------------------------------------------");

        } catch (IOException e) {
            Loggers.e("HttpServiceCallPost-Error " + e.toString(), e);
        }

        client.newCall(request).enqueue(callback);
    }



    /**
     * Http 서비스 호출(GET) - 사전에 서버정보가 고정 안함.
     * @param requestUrl
     * @param callback
     */
    public void HttpServiceCallGet(String requestUrl, Callback callback) {
        Request request = null;

        sessionValue = AppSetting.Login.SDS_SESSION;
        if (!TextUtils.isEmpty(sessionValue)) { // 헤더에 넣을 세션값이 있는경우..
            Loggers.d("[sessionValue] = " + sessionValue);
            request = getRequestInstanceGetWithHeader(requestUrl, sessionValue);
        } else { // 헤더에 넣을 세션값이 없는경우..
            request = getRequestInstance_Get(requestUrl);
        }

        try {

            Loggers.d("----------------------------------------------------------------------");
            Loggers.d("[HttpServiceCallGet-Method] = " + request.method().toString());
            Loggers.d("[HttpServiceCallGet-RequestURL] = " + requestUrl);
            Loggers.d("[HttpServiceCallGet-Headers] = " + request.headers().toString());
            Loggers.d("----------------------------------------------------------------------");

        } catch (Exception e) {
            Loggers.e("HttpServiceCallGet-Error " + e.toString(), e);
        }

        client.newCall(request).enqueue(callback);
    }



    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * Rest API 서비스 호출 - Parnas API용(2019.04.16) - JSON MAP 파라미터 수신용...
     */
    public void RestAPIServiceCall(String path, Map<String, String> jsonMap, Callback callback) {
        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(jsonMap);

        RestAPIServiceCall(path, jsonString, callback);

    }

    /**
     * Rest API 서비스 호출#1
     */
    public void RestAPIServiceCall(String method, String path, Map<String,String> paraMap, Callback userCallback) {

        String pathScm = Svc.API_PKG_PRC_DRIVER;
        RequesterSession.with().RestAPIServiceCall(method, pathScm, path, paraMap, userCallback);
    }
    /**
     * Rest API 서비스 호출#2
     */
    public void RestAPIServiceCall(String method, String pathScm, String path, Map<String,String> paraMap, Callback userCallback) {

        Map<String, String> headerMap = new HashMap<String, String>();
        RequesterSession.with().RestAPIServiceCall(method, pathScm, path, paraMap, userCallback, headerMap);
    }


    /**
     * Rest API 서비스 호출 - Parnas API용(2019.04.16)
     */
    public void RestAPIServiceCall(String path, String jsonString, final Callback userCallback) {
        try {

            RequestBody body = RequestBody.create(JSON, jsonString);
            // 백업 - 19.06.04 - 백업
//            Request request = getRequestInstancePost(body, path);

            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme(Svc.SCHEME_HTTPS)
                    .host(Svc.SERVER_ADDR)
                    .port(Svc.SERVICE_PORT)
                    .addPathSegments(path)
                    .build();

            Request request = new Request.Builder()
                    .url(httpUrl)
                    .post(body)
                    .addHeader(Svc.API_KEY_NAME, Svc.API_KEY)
                    .build();


            Loggers.d("----------------------------------------------------------------------");
            Loggers.d("[RestAPIServiceCall-Method] = " + request.method().toString());
            Loggers.d("[RestAPIServiceCall-Header String] = " + request.headers().toString());
            Loggers.d("[RestAPIServiceCall-url] = " + request.url().toString());
            Loggers.d("[RestAPIServiceCall-Request Body] = " + bodyToString(request));
            Loggers.d("----------------------------------------------------------------------");

            // 이벤트 리스닝시..
            //  client.newBuilder().eventListener(this).build().newCall(request).enqueue(callback);

            // 기존 로직 - 2019.07.23 19시 이전...
            // client.newCall(request).enqueue(userCallback);

            // 신규 로직 - 2019.07.23 19시 이후...(에러코드 처리로직 보완요청)
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    userCallback.onFailure(call, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    try {

                        // 1.먼저 엿본다..
                        ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);  // Response를 그냥 받아서 오픈하면, java.lang.IllegalStateException: closed 오류..그래서 peekBody 처리함...

                        // 2.사용자 콜백으로 리턴...
                        userCallback.onResponse(call, response);

//                        String strJsonOutut = responseBodyCopy.string();
//                        Loggers.d2(this, "나도받았다, 수신데이타:" + strJsonOutut);
//
//                        JsonElement jelement = new JsonParser().parse(strJsonOutut);
//                        JsonObject jObject = jelement.getAsJsonObject();
//
//                        String ResultCode = jObject.get("ResultCode").getAsString();
//                        String ResultMsg = jObject.get("ResultMsg").getAsString();
//
//                        if (!ResultCode.equals("00")) {
//                            // 별도 예외처리 코드 추가 장소...
//                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {

                    }
                }
            });


        } catch (Exception e) {
            Loggers.e("RestAPIServiceCall-Error " + e.toString(), e);
        }
    }

    /**
     * Rest API 서비스 호출 - Hdo API용(2023.07.21)
     */
    public void RestAPIServiceCall(String method, String pathScm, String path, Map<String,String> paraMap, Callback userCallback, Map<String,String> headerMap) {

        try {

            Loggers.d("----------------------------------------------------------------------");

            if(method.equals(Svc.GET)) {

                Iterator<String> keys = paraMap.keySet().iterator();
                int cnt = 0;

                while (keys.hasNext()) {
                    String key = keys.next();
                    Loggers.d(String.format("키:%s, 값:%s", key, paraMap.get(key)));

                    if (cnt == 0) {
                        path = path + "?" + key + "=" + paraMap.get(key);
                    } else {
                        path = path + "&" + key + "=" + paraMap.get(key);
                    }

                    cnt++;
                }
            }

            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme(Svc.SCHEME_HTTP)
                    .host(Svc.SERVER_ADDR)
                    .port(Svc.SERVICE_PORT)
                    .addPathSegments(pathScm + path)
                    .build();

            RequestBody body;
            Request request;

            headerMap.put(Svc.API_KEY_NAME, Svc.API_KEY);
            Headers headerBuild = Headers.of(headerMap);

            if(method.equals(Svc.POST)) {

                Gson gson = new GsonBuilder().create();
                String jsonString = gson.toJson(paraMap);

                body = RequestBody.create(JSON, jsonString);

                request = new Request.Builder()
                        .url(httpUrl)
                        .post(body)
                        .headers(headerBuild)
                        .build();

            }else{

                request = new Request.Builder()
                        .url(httpUrl)
                        .headers(headerBuild)
                        .build();
            }

            Loggers.d("[RestAPIServiceCall-Method] = " + request.method().toString());
            Loggers.d("[RestAPIServiceCall-Header String] = " + request.headers().toString());
            Loggers.d("[RestAPIServiceCall-url] = " + request.url().toString());
            Loggers.d("[RestAPIServiceCall-Request Body] = " + bodyToString(request));
            Loggers.d("----------------------------------------------------------------------");

            // 이벤트 리스닝시
            //  client.newBuilder().eventListener(this).build().newCall(request).enqueue(callback);

            // 기존 로직 - 2019.07.23 19시 이전
            // client.newCall(request).enqueue(userCallback);

            // 신규 로직 - 2019.07.23 19시 이후(에러코드 처리로직 보완)
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    Loggers.d("[RestAPIServiceCall-onFailure] = " + e.getMessage());

                    userCallback.onFailure(call, e);

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    try {

                        // Response를 그냥 받아서 오픈하면, java.lang.IllegalStateException: closed 오류..그래서 peekBody 처리함
                        ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);

                        String strJsonOutut = responseBodyCopy.string();

                        Loggers.d("----------------------------------------------------------------------");
                        Loggers.d("#API [API 응답]");
                        Loggers.d("#API ResponseCode() : " + response.code());
                        Loggers.d("#API strJsonOutut : " + strJsonOutut);

                        /*
                        {
                            "resultCode": "00",
                            "resultMsg": "SUCCESS",
                            "resultData": [
                                {
                                    "UPDATE_YN": "N",
                                    "UPDATE_URL": null,
                                    "OS_TYPE": "ANDROID",
                                    "APP_NAME": "기사모바일-용차",
                                    "LAST_VERSION": "1.0.01"
                                }
                            ]
                        }
                        */
                        if (strJsonOutut.length()>0) {
                            JsonElement jelement = new JsonParser().parse(strJsonOutut);
                            JsonObject jObject = jelement.getAsJsonObject();

                            String resultCode = "";
                            String resultMsg = "";
                            String resultData = "";

                            if ( (jObject.has("resultCode")) && (jObject.has("resultMsg"))) {

                                resultCode = jObject.get("ResultCode").getAsString() == null ? "" : jObject.get("ResultCode").getAsString();
                                Loggers.d("#API resultCode :: " + resultCode);

                                resultMsg = jObject.get("ResultMsg").getAsString() == null ? "" : jObject.get("ResultMsg").getAsString();
                                Loggers.d("#API resultMsg :: " + resultMsg);
                            } else {
                                resultCode = "-1";
                                resultMsg = "ERROR";
                                resultData = "";
                            }
                        }

                        Loggers.d("#API ----------------------------------------------------------------------");

                        userCallback.onResponse(call, response);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Loggers.d("Exception : " + ex.getMessage());
                    } finally {
                    }
                }
            });

        } catch (Exception e) {
            Loggers.e("RestAPIServiceCall - Exception : " + e.toString(), e);
        }
    }

    /**
     * Rest API 서비스 호출
     */
    public void CvoGpsServiceCall(
            URL url,
            String method,
            String path,
            Map<String,String> paraMap,
            Callback userCallback,
            Map<String,String> headerMap,
            boolean arrayYn) {

        try {

            // 관리자모드시 보고하지않음..(2022-09-18,추가)
            if (AppSetting.PLAY_MODE.equals(CommType._관리자))
                return;

            if(method.equals(Svc.GET)) {

                Iterator<String> keys = paraMap.keySet().iterator();
                int cnt = 0;

                while (keys.hasNext()) {
                    String key = keys.next();
                    Loggers.d(String.format("키:%s, 값:%s", key, paraMap.get(key)));

                    if (cnt == 0) {
                        path = path + "?" + key + "=" + paraMap.get(key);
                    } else {
                        path = path + "&" + key + "=" + paraMap.get(key);
                    }

                    cnt++;
                }
            }

            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme(url.toURI().getScheme())
                    .host(url.toURI().getHost())
                    .port(url.toURI().getPort())
                    .addPathSegments(url.toURI().getPath().substring(1)) // 맨앞 슬래시(/) 제거..
                    .build();

            RequestBody body;
            Request request;

//            headerMap.put(Svc.API_KEY_NAME, Svc.API_KEY);
            Headers headerBuild = Headers.of(headerMap);

            if(method.equals(Svc.POST)) {

                Gson gson = new GsonBuilder().create();
                String jsonString = gson.toJson(paraMap);


                if (arrayYn)
                    body = RequestBody.create(JSON, "[" + jsonString + "]");
                else
                    body = RequestBody.create(JSON, jsonString);

                request = new Request.Builder()
                        .url(httpUrl)
                        .post(body)
                        .headers(headerBuild)
                        .build();

            }else{

                request = new Request.Builder()
                        .url(httpUrl)
                        .headers(headerBuild)
                        .build();
            }

            Loggers.d("[API 요청]");
            Loggers.d("[CvoGpsServiceCall-Method] = " + request.method().toString());
            Loggers.d("[CvoGpsServiceCall-Header String] = " + request.headers().toString());
            Loggers.d("[CvoGpsServiceCall-url] = " + request.url().toString());
            Loggers.d("[CvoGpsServiceCall-Request Body] = " + bodyToString(request));
            Loggers.d("----------------------------------------------------------------------");

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    Loggers.d("[RestAPIServiceCall-onFailure] = " + e.getMessage());

                    userCallback.onFailure(call, e);

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    try {

                        // Response를 그냥 받아서 오픈하면, java.lang.IllegalStateException: closed 오류..그래서 peekBody 처리함
                        ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);

                        String strJsonOutut = responseBodyCopy.string();

                        Loggers.d("----------------------------------------------------------------------");
                        Loggers.d("[API 응답]");
                        Loggers.d("[CvoGpsServiceCall-ResponseCode()]" + response.code());
                        Loggers.d("[CvoGpsServiceCall-strJsonOutut]" + strJsonOutut);

//                        if (strJsonOutut.length()>0) {
//                            JsonElement jelement = new JsonParser().parse(strJsonOutut);
//                            JsonObject jObject = jelement.getAsJsonObject();
//
//                            String resultCode = "";
//                            String resultMsg = "";
//                            String resultData = "";
//
//                            resultCode = jObject.get("status").getAsString() == null ? "" : jObject.get("status").getAsString();
//                            Loggers.d("status :: " + resultCode);
//
//                            resultMsg = jObject.get("message").getAsString() == null ? "" : jObject.get("message").getAsString();
//                            Loggers.d("message :: " + resultMsg);
//
//
//                            Loggers.d("----------------------------------------------------------------------");
//                        }

                        userCallback.onResponse(call, response);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Loggers.d("Exception : " + ex.getMessage());
                    } finally {
                    }
                }
            });

        } catch (Exception e) {
            Loggers.e("RestAPIServiceCall - Exception : " + e.toString(), e);
        }
    }



    /**
     * Get 방식 변경 - 2018.06.02
     * @param httpUrl
     * @param callback
     */
    public void commServiceCall_GetUrl(HttpUrl httpUrl, Callback callback) {

        try {
            Request request = null;

            sessionValue = AppSetting.Login.SDS_SESSION;
            if (!TextUtils.isEmpty(sessionValue)) {
                Loggers.d("[sessionValue] = " + sessionValue);


                request = new Request.Builder()
                        .url(httpUrl)
                        .addHeader("Cookie", sessionValue)
                        .build();
            }

            Loggers.d("----------------------------------------------------------------------");
            Loggers.d("[Method] = " + request.method().toString());
            Loggers.d("[url] = " + httpUrl.newBuilder().toString());
            Loggers.d("[Headers-1] = " + request.headers().toString());
            Loggers.d("----------------------------------------------------------------------");

            client.newCall(request).enqueue(callback);

        } catch (Exception e) {
            Loggers.e("Error " + e.toString(), e);
        }


    }


    // -------------------------------------------------------------------------------------------
    // 이미지 파일첨부시 활용..
    // -------------------------------------------------------------------------------------------
    public void commServiceCall(RequestBody formBody, String path, Callback callback) {

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(Svc.SCHEME_HTTPS)
                .host(Svc.SERVER_ADDR)
                .port(Svc.SERVICE_PORT)
                .addPathSegments(path)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .post(formBody)
                .addHeader(Svc.API_KEY_NAME, Svc.API_KEY)
                .build();

        try {

            Loggers.d("----------------------------------------------------------------------");
            Loggers.d("[commServiceCall-Method] = " + request.method().toString());
            Loggers.d("[commServiceCall-ServiceName] = " + path);
            Loggers.d("[commServiceCall-url] = " + request.url().toString());
            Loggers.d("[commServiceCall-Headers-2] = " + request.headers().toString());
            Loggers.d("[commServiceCall-Request Body] = " + bodyToString(request));
            Loggers.d("[commServiceCall-Length] = " + formBody.contentLength());
            Loggers.d("----------------------------------------------------------------------");

        } catch (IOException e) {
            Loggers.e("Error " + e.toString(), e);
        }

        client.newCall(request).enqueue(callback);

    }
    // -------------------------------------------------------------------------------------------
    // CVO이미지 파일첨부시 활용..(2023.09.25 추가..)
    // -------------------------------------------------------------------------------------------
    public void CvoCommServiceCall(
            URL url,
            String method,
            String path,
            RequestBody formBody,
            Callback userCallback,
            Map<String,String> headerMap) {

//        HttpUrl httpUrl = new HttpUrl.Builder()
//                .scheme(Svc.SCHEME_HTTPS)
//                .host(Svc.SERVER_ADDR)
//                .port(Svc.SERVICE_PORT)
//                .addPathSegments(path)
//                .build();
        try {

            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme(url.toURI().getScheme())
                    .host(url.toURI().getHost())
                    .port(url.toURI().getPort())
                    .addPathSegments(path)
                    .build();


            Request request = new Request.Builder()
                    .url(httpUrl)
                    .post(formBody)
                    //.headers(headerBuild)
                    .addHeader(AppSetting.appChkInfo.getCVO_API_KEY_NAME(), AppSetting.appChkInfo.getCVO_API_KEY())
                    .build();

            Loggers.d("----------------------------------------------------------------------");
            Loggers.d("[CvoCommServiceCall-Method] = " + request.method().toString());
            Loggers.d("[CvoCommServiceCall-ServiceName] = " + path);
            Loggers.d("[CvoCommServiceCall-url] = " + request.url().toString());
            Loggers.d("[CvoCommServiceCall-Headers-2] = " + request.headers().toString());
            Loggers.d("[CvoCommServiceCall-Request Body] = " + bodyToString(request));
            Loggers.d("[CvoCommServiceCall-Length] = " + formBody.contentLength());
            Loggers.d("----------------------------------------------------------------------");

            //client.newCall(request).enqueue(userCallback);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    Loggers.d("[RestAPIServiceCall-onFailure] = " + e.getMessage());

                    userCallback.onFailure(call, e);

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    try {

                        // Response를 그냥 받아서 오픈하면, java.lang.IllegalStateException: closed 오류..그래서 peekBody 처리함
                        ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);

                        String strJsonOutut = responseBodyCopy.string();

                        Loggers.d("----------------------------------------------------------------------");
                        Loggers.d("[API 응답]");
                        Loggers.d("[CvoCommServiceCall-ResponseCode()]" + response.code());
                        Loggers.d("[CvoCommServiceCall-strJsonOutut]" + strJsonOutut);

//                        if (strJsonOutut.length()>0) {
//                            JsonElement jelement = new JsonParser().parse(strJsonOutut);
//                            JsonObject jObject = jelement.getAsJsonObject();
//
//                            String resultCode = "";
//                            String resultMsg = "";
//                            String resultData = "";
//
//                            resultCode = jObject.get("status").getAsString() == null ? "" : jObject.get("status").getAsString();
//                            Loggers.d("status :: " + resultCode);
//
//                            resultMsg = jObject.get("message").getAsString() == null ? "" : jObject.get("message").getAsString();
//                            Loggers.d("message :: " + resultMsg);
//
//
//                            Loggers.d("----------------------------------------------------------------------");
//                        }

                        userCallback.onResponse(call, response);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Loggers.d("Exception : " + ex.getMessage());
                    } finally {
                    }
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Loggers.e("Error " + e.toString(), e);
        }


    }


// 미사용 메소드로 간주됨..추후 삭제 요망..

    private static String bodyToString2(final RequestBody request){
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            copy.writeTo(buffer);
            return buffer.readUtf8();
        }
        catch (final IOException e) {
            return "did not work";
        }
    }

    public static String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (Exception e) {
            e.printStackTrace();
            return "did not work";
        }
    }


    private long callStartNanos;

    private void printEvent(String name) {
        long nowNanos = System.nanoTime();
        if (name.equals("callStart")) {
            callStartNanos = nowNanos;
        }
        long elapsedNanos = nowNanos - callStartNanos;
        System.out.printf("=============================>>>>>> %.3f %s%n", elapsedNanos / 1000000000d, name);
    }

    @Override
    public void callStart(Call call) {
        printEvent("callStart");
    }

    @Override
    public void callEnd(Call call) {
        printEvent("callEnd");
    }

    @Override
    public void dnsStart(Call call, String domainName) {
        printEvent("dnsStart");
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        printEvent("dnsEnd");
    }
}




