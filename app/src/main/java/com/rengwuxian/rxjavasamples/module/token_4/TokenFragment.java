// (c)2016 Flipboard Inc, All Rights Reserved.

package com.rengwuxian.rxjavasamples.module.token_4;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.rxjavasamples.view.BaseFragment;
import com.rengwuxian.rxjavasamples.R;
import com.rengwuxian.rxjavasamples.model.FakeThing;
import com.rengwuxian.rxjavasamples.model.FakeToken;
import com.rengwuxian.rxjavasamples.presenter.network.ApiFake;
import com.rengwuxian.rxjavasamples.presenter.network.Network;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TokenFragment extends BaseFragment {

    @BindView(R.id.tokenTv)
    TextView tokenTv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @OnClick(R.id.requestBt)
    void upload() {
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        final ApiFake apiFake = Network.getApiFake();
        disposable = apiFake.getFakeTokenObservable("fake_auth_code")
                .flatMap(new Function<FakeToken, Observable<FakeThing>>() {
                    @Override
                    public Observable<FakeThing> apply(FakeToken fakeToken) {
                        return apiFake.getFakeDataObservable(fakeToken);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FakeThing>() {
                    @Override
                    public void accept(FakeThing fakeData) {//返回Disposable对象，在界面退出或者变换的时候需要dispose工作流
                        swipeRefreshLayout.setRefreshing(false);
                        tokenTv.setText(getString(R.string.got_data, fakeData.id, fakeData.name));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token, container, false);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_token;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_token;
    }
}
