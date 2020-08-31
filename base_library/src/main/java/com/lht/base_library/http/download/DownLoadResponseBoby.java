package com.lht.base_library.http.download;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class DownLoadResponseBoby extends ResponseBody {

    private ResponseBody body;
    private IDownLoadProgressListener listener;
    private BufferedSource source;

    public DownLoadResponseBoby(ResponseBody body, IDownLoadProgressListener listener) {
        this.body = body;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return body.contentType();
    }

    @Override
    public long contentLength() {
        return body.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (source == null) {
            source = Okio.buffer(createSource(body.source()));
        }
        return source;
    }

    private Source createSource(Source source) {
        return new ForwardingSource(source) {

            private long totalRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long read = super.read(sink, byteCount);
                totalRead += read != -1 ? read : 0;
                if (null != listener) {
                    listener.update(totalRead, body.contentLength(), read == -1);
                }
                return read;
            }
        };
    }

}
