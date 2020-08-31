package com.lht.base_library.http.download;

import android.text.TextUtils;
import android.util.ArrayMap;

import com.lht.base_library.http.net.ConverterFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class DownLoadManager {

    private static volatile DownLoadManager instance;

    private DownLoadManager() {
        infos = new ArrayList<>();
        listenerMap = new ArrayMap<>();
    }

    public static DownLoadManager getInstance() {
        if (instance == null) {
            synchronized (DownLoadManager.class) {
                if (instance == null) {
                    instance = new DownLoadManager();
                }
            }
        }
        return instance;
    }

    private List<DownLoadInfo> infos;
    private ArrayMap<String, DownLoadSubscriber> listenerMap;

    public void startTask(DownLoadInfo info) {
        if (info == null || TextUtils.isEmpty(info.getUrl()) || listenerMap.get(info.getUrl()) != null) {
            return;
        }
        DownLoadSubscriber subscriber = new DownLoadSubscriber(info);
        listenerMap.put(info.getUrl(), subscriber);
        DownLoadService service;
        if (infos.contains(info)) {
            service = info.getService();
        } else {
            DownLoadInterceptor interceptor = new DownLoadInterceptor(subscriber);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(info.getTimeout(), TimeUnit.MILLISECONDS);
            builder.addInterceptor(interceptor);
            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .addConverterFactory(ConverterFactory.createDefaultConverterFactory())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(info.getBaseUrl())
                    .build();
            service = retrofit.create(DownLoadService.class);
            info.setService(service);
            infos.add(info);
        }
        service.download("bytes=" + info.getReadLength() + "-", info.getUrl())
                .map(new Function<ResponseBody, DownLoadInfo>() {
                    @Override
                    public DownLoadInfo apply(ResponseBody body) throws Exception {
                        writeCaches(body, new File(info.getSavePath()), info);
                        return info;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
//                .retryWhen(new RetryWhenNetworkException())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void removeTask(DownLoadInfo info) {
        listenerMap.remove(info.getUrl());
        infos.remove(info);
    }

    public void stopTask(DownLoadInfo info) {
        if (info == null) {
            return;
        }
        info.setDownState(DownState.STOP);
        if (info.getListener() != null) {
            info.getListener().onDownLoadStop(info);
        }
        if (listenerMap.containsKey(info.getUrl())) {
            DownLoadSubscriber subscriber = listenerMap.get(info.getUrl());
            if (subscriber != null) {
                subscriber.cancel();
            }
            listenerMap.remove(info.getUrl());
        }
    }

    public void pauseTask(DownLoadInfo info) {
        if (info == null) {
            return;
        }
        info.setDownState(DownState.PAUSE);
        if (info.getListener() != null) {
            info.getListener().onDownLoadPause(info);
        }
        if (listenerMap.containsKey(info.getUrl())) {
            DownLoadSubscriber subscriber = listenerMap.get(info.getUrl());
            if (subscriber != null) {
                subscriber.cancel();
            }
            listenerMap.remove(info.getUrl());
        }
    }

    public void stopAllTask() {
        for (DownLoadInfo info : infos) {
            stopTask(info);
        }
        listenerMap.clear();
        infos.clear();
    }

    public void pauseAllTask() {
        for (DownLoadInfo info : infos) {
            pauseTask(info);
        }
        listenerMap.clear();
        infos.clear();
    }

    public List<DownLoadInfo> getDownLoadingTask() {
        return infos;
    }

    public void writeCaches(ResponseBody responseBody, File file, DownLoadInfo info) throws Exception {
        try {
            RandomAccessFile randomAccessFile = null;
            FileChannel channelOut = null;
            InputStream inputStream = null;
            try {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                long allLength = 0 == info.getCountLength() ? responseBody.contentLength() : info.getReadLength() + responseBody.contentLength();
                inputStream = responseBody.byteStream();
                randomAccessFile = new RandomAccessFile(file, "rwd");
                channelOut = randomAccessFile.getChannel();
                MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE,
                        info.getReadLength(), allLength - info.getReadLength());
                byte[] buffer = new byte[1024 * 4];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    mappedBuffer.put(buffer, 0, len);
                }
            } catch (IOException e) {
                throw e;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (channelOut != null) {
                    channelOut.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

}
