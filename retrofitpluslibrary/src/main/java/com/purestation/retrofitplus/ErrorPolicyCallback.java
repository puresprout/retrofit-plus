package com.purestation.retrofitplus;

import android.content.Context;

import com.purestation.retrofitplus.error.ErrorHandler;
import com.purestation.retrofitplus.error.ErrorPolicy;
import com.purestation.retrofitplus.error.ThrowableMessageConverter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 내장된 ErrorHandler를 사용한다.
 */
public abstract class ErrorPolicyCallback<T> extends BaseCallback<T> {

    public static final int STATUS_CODE_NOT_AUTHORIZED = 401;

    private ErrorPolicy exceptionPolicy;
    private ErrorPolicy unsuccessfulStatusPolicy;

    public ErrorPolicyCallback(Context context) {
        this(ErrorPolicy.createToast(context), ErrorPolicy.createToast(context));
    }

    public ErrorPolicyCallback(ErrorPolicy exceptionPolicy, ErrorPolicy unsuccessfulStatusPolicy) {
        this.exceptionPolicy = exceptionPolicy;
        this.unsuccessfulStatusPolicy = unsuccessfulStatusPolicy;
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        ErrorHandler errorHandler = new ErrorHandler(exceptionPolicy);
        ThrowableMessageConverter converter = new ThrowableMessageConverter(exceptionPolicy.getContext());
        errorHandler.setMessage(converter.convert(t));
        errorHandler.show();
    }

    @Override
    public abstract void onSuccessfulStatus(Call<T> call, T response);

    @Override
    public void onUnsuccessfulStatus(Call<T> call, int code, ResponseBody errorBody) {
        if (code == STATUS_CODE_NOT_AUTHORIZED) {
            if (handleNotAuthorizedStatus()) {
                return;
            }
        }

        try {
            String errorMessage = getErrorMessageFrom(errorBody);

            ErrorHandler errorHandler = new ErrorHandler(unsuccessfulStatusPolicy);
            errorHandler.setMessage(errorMessage);
            errorHandler.show();
        } catch (IOException e) {
            onFailure(call, e);
        }
    }

    protected boolean handleNotAuthorizedStatus() {
        return false;
    }

    protected String getErrorMessageFrom(ResponseBody errorBody) throws IOException {
        return errorBody.string();
    }

}
