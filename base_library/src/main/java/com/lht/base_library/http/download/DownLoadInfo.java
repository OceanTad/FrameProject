package com.lht.base_library.http.download;

public class DownLoadInfo {

    //保存地址
    private String savePath;
    //下载地址
    private String url;
    //总长度
    private long countLength;
    //下载长度
    private long readLength;
    //下载状态
    private String downState;
    private long timeout = 10 * 1000;
    private String baseUrl;


    private transient DownLoadService service;
    private transient DownLoadListener listener;

    private transient boolean updateProgress;

    public DownLoadInfo(String baseUrl, String url, DownLoadListener listener) {
        setBaseUrl(baseUrl);
        setUrl(url);
        setListener(listener);
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getCountLength() {
        return countLength;
    }

    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }

    public String getDownState() {
        return downState;
    }

    public void setDownState(String downState) {
        this.downState = downState;
    }

    public DownLoadService getService() {
        return service;
    }

    public void setService(DownLoadService service) {
        this.service = service;
    }

    public DownLoadListener getListener() {
        return listener;
    }

    public void setListener(DownLoadListener listener) {
        this.listener = listener;
    }

    public boolean isUpdateProgress() {
        return updateProgress;
    }

    public void setUpdateProgress(boolean updateProgress) {
        this.updateProgress = updateProgress;
    }

    public long getReadLength() {
        return readLength;
    }

    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String toString() {
        return "DownLoadInfo{" +
                "savePath='" + savePath + '\'' +
                ", url='" + url + '\'' +
                ", countLength=" + countLength +
                ", readLength=" + readLength +
                ", downState='" + downState + '\'' +
                ", service=" + service +
                ", listener=" + listener +
                ", updateProgress=" + updateProgress +
                '}';
    }

}
