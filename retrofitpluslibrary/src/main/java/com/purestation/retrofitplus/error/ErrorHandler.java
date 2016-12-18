package com.purestation.retrofitplus.error;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.purestation.retrofitplus.R;

public class ErrorHandler {

    private ErrorPolicy errorPolicy;
    private String message;

    public ErrorHandler(ErrorPolicy errorPolicy) {
        this.errorPolicy = errorPolicy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void show() {
        switch (errorPolicy.getViewType()) {
            case TOAST:
                Toast.makeText(errorPolicy.getContextForToast(), message, Toast.LENGTH_SHORT).show();
                break;
            case SNACKBAR:
                Snackbar.make(errorPolicy.getViewForSnackbar(), message, Snackbar.LENGTH_SHORT).show();
                break;
            case DIALOG:
                new AlertDialog.Builder(errorPolicy.getContextForDialog())
                        .setTitle(R.string.alert)
                        .setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show();
                break;
            case INVIEW:
                ((TextView) errorPolicy.getShowingView().findViewById(errorPolicy.getMessageTextViewId())).setText(message);
                errorPolicy.getShowingView().setVisibility(View.VISIBLE);
                errorPolicy.getHidingView().setVisibility(View.GONE);
                break;
        }
    }

}
