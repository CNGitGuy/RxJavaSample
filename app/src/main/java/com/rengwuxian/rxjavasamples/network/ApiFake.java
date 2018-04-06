// (c)2016 Flipboard Inc, All Rights Reserved.

package com.rengwuxian.rxjavasamples.network;

import android.support.annotation.NonNull;

import com.rengwuxian.rxjavasamples.model.FakeThing;
import com.rengwuxian.rxjavasamples.model.FakeToken;

import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class ApiFake {
    Random random = new Random();

    /**
     * 获取包含伪造Token的Observable
     *
     * @param fakeAuth
     * @return
     */
    public Observable<FakeToken> getFakeTokenObservable(@NonNull String fakeAuth) {
        return Observable.just(fakeAuth)
                .map(new Function<String, FakeToken>() {
                    @Override
                    public FakeToken apply(String fakeAuth) {
                        // Add some random delay to mock the network delay
                        int fakeNetworkTimeCost = random.nextInt(500) + 500;
                        try {
                            Thread.sleep(fakeNetworkTimeCost);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        FakeToken fakeToken = new FakeToken();
                        fakeToken.setToken(createToken());
                        return fakeToken;
                    }
                });
    }

    /**
     * 生成token
     *
     * @return
     */
    private static String createToken() {
        return "fake_token_" + System.currentTimeMillis() % 10000;
    }

    /**
     * 获取包含伪造数据Observable
     *
     * @param fakeToken
     * @return
     */
    public Observable<FakeThing> getFakeDataObservable(FakeToken fakeToken) {
        return Observable.just(fakeToken)
                .map(new Function<FakeToken, FakeThing>() {
                    @Override
                    public FakeThing apply(FakeToken fakeToken) {
                        // Add some random delay to mock the network delay
                        int fakeNetworkTimeCost = random.nextInt(500) + 500;
                        try {
                            Thread.sleep(fakeNetworkTimeCost);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (fakeToken.isExpired()) {
                            throw new IllegalArgumentException("Token expired!");
                        }

                        FakeThing fakeData = new FakeThing();
                        fakeData.id = (int) (System.currentTimeMillis() % 1000);
                        fakeData.name = "FAKE_USER_" + fakeData.id;
                        return fakeData;
                    }
                });
    }
}
