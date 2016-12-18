package com.purestation.retrofitplus.error;

import android.content.Context;

import com.purestation.retrofitplus.R;

import java.io.IOException;
import java.net.UnknownHostException;

public class ThrowableMessageConverter {

    private Context mContext;

    public ThrowableMessageConverter(Context context) {
        mContext = context;
    }

    public String convert(Throwable throwable) {
        if (throwable instanceof UnknownHostException) {
            return mContext.getResources().getString(R.string.unknown_host_exception);
        } else if (throwable instanceof IOException) {
            return mContext.getResources().getString(R.string.io_exception);
        } else {
            return mContext.getResources().getString(R.string.default_exception);
        }
    }

}
