package com.lht.base_library.http.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public class RetryWhenFunction implements Function<Observable<Throwable>, ObservableSource<?>> {

    private int maxCount;
    private int retryCount;
    private long waitTime = 1000;
    private List<Class> throwables;

    private RetryWhenFunction(Builder builder) {
        this.maxCount = builder.maxCount;
        this.retryCount = builder.retryCount;
        this.waitTime = builder.waitTime;
        this.throwables = builder.throwables;
    }

    @Override
    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable
                .zipWith(Observable.range(1, maxCount + 1), new BiFunction<Throwable, Integer, Wrapper>() {
                    @Override
                    public Wrapper apply(Throwable throwable, Integer integer) throws Exception {
                        return new Wrapper(throwable, integer);
                    }
                })
                .flatMap(new Function<Wrapper, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Wrapper throwable) throws Exception {
                        if (throwables != null && throwables.contains(throwable.getClass())) {
                            if (retryCount < maxCount) {
                                waitTime += 500 * retryCount;
                                retryCount++;
                                return Observable.timer(waitTime, TimeUnit.MILLISECONDS);
                            } else {
                                return Observable.error(throwable.getThrowable());
                            }
                        } else {
                            return Observable.error(throwable.getThrowable());
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

        public RetryWhenFunction build() {
            return new RetryWhenFunction(this);
        }

    }

    private class Wrapper {

        private int index;
        private Throwable throwable;

        public Wrapper(Throwable throwable, int index) {
            this.index = index;
            this.throwable = throwable;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public void setThrowable(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public String toString() {
            return "Wrapper{" +
                    "index=" + index +
                    ", throwable=" + throwable +
                    '}';
        }

    }

}
