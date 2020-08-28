package com.lht.base_library.http.modle;

public class BaseResponseCode {

    public static final int SUCCESS = 0;
    public static final int NO_ACTION = -1;

    public static final int TIME_OUT_ERROR = 6001;
    public static final int CONNECT_ERROR = 6002;
    public static final int SSL_ERROR = 6003;
    public static final int DNS_ERROR = 6004;
    public static final int UNKNOWN_ERROR = 6005;

    public static final String TIMEOUT_MSG = "请求超时，请稍后重试";
    public static final String CONNECT_MSG = "连接超时，请检查网络状态";
    public static final String SSL_MSG = "安全证书异常";
    public static final String DNS_MSG = "域名解析失败";
    public static final String UNKNOWN_MSG = "未知错误";

    public static final int NET_ERROR = 504;
    public static final int URL_ERROR = 404;
    public static final int HTTP_ERROR = 6006;

    public static final String NET_MSG = "网络异常，请检查网络状态";
    public static final String URL_MSG = "请求地址错误";
    public static final String HTTP_MSG = "请求失败";

}
