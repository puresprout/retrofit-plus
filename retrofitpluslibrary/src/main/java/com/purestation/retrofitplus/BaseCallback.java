package com.purestation.retrofitplus;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public abstract class BaseCallback<T> implements retrofit2.Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        try {
            // NOTICE 상태코드가 성공이든 실패이던 파싱중 예외가 발생할 수 있다.
            if (response.isSuccessful()) {
                onSuccessfulStatus(call, response.body());
            } else {
                onUnsuccessfulStatus(call, response.code(), response.errorBody());
            }
        } catch (Exception e) {
            onFailure(call, e);
        }
    }

    @Override
    public abstract void onFailure(Call<T> call, Throwable t);

    public abstract void onSuccessfulStatus(Call<T> call, T response);

    public abstract void onUnsuccessfulStatus(Call<T> call, int code, ResponseBody errorBody);

}
