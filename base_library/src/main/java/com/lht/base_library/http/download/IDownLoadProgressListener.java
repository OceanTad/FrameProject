package com.lht.base_library.http.download;

public interface IDownLoadProgressListener {

    void update(long read, long count, boolean done);

}
