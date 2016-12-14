package com.example.gs.mvpdemo.model;

import android.support.annotation.NonNull;

import com.example.gs.mvpdemo.ProApplication;
import com.example.gs.mvpdemo.base.BaseModel;
import com.example.gs.mvpdemo.bean.LoginBean;
import com.example.gs.mvpdemo.exception.ApiException;
import com.example.gs.mvpdemo.subscriber.CommonSubscriber;
import com.example.gs.mvpdemo.transformer.CommonTransformer;

/**
 * Created by GaoSheng on 2016/11/26.
 * 20:53
 *
 * @VERSION V1.4
 * com.example.gs.mvpdemo.model
 * 主要做一些数据处理呀,网路请求呀
 */

public class LoginModel extends BaseModel {

    public boolean login(@NonNull String username, @NonNull String pwd, @NonNull final InfoHint
            infoHint) {
        boolean isLogin = false;

        if (infoHint == null)
            throw new RuntimeException("InfoHint不能为空");

        httpService.login(username, pwd)
                .compose(new CommonTransformer<LoginBean>())
                .subscribe(new CommonSubscriber<LoginBean>(ProApplication.getmContext()) {
                    @Override
                    public void onNext(LoginBean loginBean) {
                        infoHint.successInfo(loginBean.getToken());
                    }

                    @Override
                    protected void onError(ApiException e) {
                        super.onError(e);
                        infoHint.failInfo(e.message);
                    }
                });


        if ("gs".equals(username) && "123".equals(pwd)) {
            isLogin = true;
        } else {
            isLogin = false;
        }
        return isLogin;
    }

    //通过接口产生信息回调
    public interface InfoHint {
        void successInfo(String str);

        void failInfo(String str);

    }

}
