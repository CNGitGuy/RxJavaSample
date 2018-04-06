// (c)2016 Flipboard Inc, All Rights Reserved.

package com.rengwuxian.rxjavasamples.model;

/**
 * 伪造的Token
 */
public class FakeToken {
    /**
     * 取到的数据
     */
    private String token;
    private boolean expired;

    public FakeToken() {
    }

    public FakeToken(boolean expired) {
        this.expired = expired;
    }

    public String getToken() {
        return token;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

}
