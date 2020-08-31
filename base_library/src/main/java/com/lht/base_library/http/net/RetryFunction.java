package com.lht.base_library.http.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class RetryFunction implements Function<Observable<Throwable>, ObservableSource<?>> {

    private int maxCount;
    private int retryCount;
    private long waitTime = 1000;
    private List<Class> throwables;

    private RetryFunction(Builder builder) {
        this.maxCount = builder.maxCount;
        this.retryCount = builder.retryCount;
        this.waitTime = builder.waitTime;
        this.throwables = builder.throwables;
    }

    @Override
    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                if (throwables != null && throwables.contains(throwable.getClass())) {
                    if (retryCount < maxCount) {
                        waitTime += 500 * retryCount;
                        retryCount++;
                        return Observable.just(1).delay(waitTime, TimeUnit.MILLISECONDS);
                    } else {
                        return Observable.error(throwable);
                    }
                } else {
                    return Observable.error(throwable);
                }
            }
        });
    }

    public static class Builder {

        private int maxCount = 3;
        private int retryCount = 0;
        private long waitTime = 1000;
        private List<Class> throwables;

        public Builder setMaxCount(int maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        public Builder setWaitTime(long waitTime) {
            this.waitTime = waitTime;
            return this;
        }

        public Builder addThrowable(Class... classTypes) {
            if (classTypes != null && classTypes.length > 0) {
                if (throwables == null) {
                    throwables = new ArrayList<>();
                }
                Collections.addAll(throwables, classTypes);
            }
            return this;
        }

        public RetryFunction build() {
            return new RetryFunction(this);
        }

    }

}
