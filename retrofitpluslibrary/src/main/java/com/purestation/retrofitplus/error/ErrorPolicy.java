package com.purestation.retrofitplus.error;

import android.content.Context;
import android.view.View;

public class ErrorPolicy {

    public enum ViewType {
        TOAST,
        SNACKBAR,
        DIALOG,
        INVIEW
    }

    private ViewType viewType;

    private Context contextForToast;
    private View viewForSnackbar;   // snackbar를 붙일 뷰. CoordinatorLayout이나 윈도우 decor의 컨텐트 뷰. 찾을때까지 올라감.
    private Context contextForDialog;

    private View showingView;
    private View hidingView;
    private int messageTextViewId;

    private ErrorPolicy(Builder builder) {
        viewType = builder.viewType;

        contextForToast = builder.contextForToast;
        viewForSnackbar = builder.viewForSnackbar;
        contextForDialog = builder.contextForDialog;

        showingView = builder.showingView;
        hidingView = builder.hidingView;
        messageTextViewId = builder.messageTextViewId;
    }

    public static ErrorPolicy createToast(Context context) {
        return new Builder().viewTypeToast(context).build();
    }

    public static ErrorPolicy createSnackbar(View view) {
        return new Builder().viewTypeSnackbar(view).build();
    }

    public static ErrorPolicy createDialog(Context context) {
        return new Builder().viewTypeDialog(context).build();
    }

    public static ErrorPolicy createInview(View showingView, View hidingView, int messageTextViewId) {
        return new Builder().viewTypeInview(showingView, hidingView, messageTextViewId).build();
    }

    public ViewType getViewType() {
        return viewType;
    }

    public Context getContextForToast() {
        return contextForToast;
    }

    public View getViewForSnackbar() {
        return viewForSnackbar;
    }

    public Context getContextForDialog() {
        return contextForDialog;
    }

    public View getShowingView() {
        return showingView;
    }

    public View getHidingView() {
        return hidingView;
    }

    public int getMessageTextViewId() {
        return messageTextViewId;
    }

    public Context getContext() {
        switch (viewType) {
            case TOAST:
                return contextForToast;
            case SNACKBAR:
                return viewForSnackbar.getContext();
            case DIALOG:
                return contextForDialog;
            case INVIEW:
                if (showingView != null) {
                    return showingView.getContext();
                } else if (hidingView != null) {
                    return hidingView.getContext();
                }
                break;
        }

        return null;
    }

    public static class Builder {
        private ViewType viewType;

        // NOTICE 아래 네가지중 하나만 사용해야 한다.
        private Context contextForToast;
        private View viewForSnackbar;
        private Context contextForDialog;

        private View showingView;
        private View hidingView;
        private int messageTextViewId;

        public Builder viewTypeToast(Context contextForToast) {
            this.viewType = ViewType.TOAST;
            this.contextForToast = contextForToast;
            return this;
        }

        public Builder viewTypeSnackbar(View viewForSnackbar) {
            this.viewType = ViewType.SNACKBAR;
            this.viewForSnackbar = viewForSnackbar;
            return this;
        }

        public Builder viewTypeDialog(Context contextForDialog) {
            this.viewType = ViewType.DIALOG;
            this.contextForDialog = contextForDialog;
            return this;
        }

        public Builder viewTypeInview(View showingView, View hidingView, int messageTextViewId) {
            this.viewType = ViewType.INVIEW;
            this.showingView = showingView;
            this.hidingView = hidingView;
            this.messageTextViewId = messageTextViewId;
            return this;
        }

        public ErrorPolicy build() {
            validate();
            return new ErrorPolicy(this);
        }

        private void validate() {
            switch (viewType) {
                case TOAST:
                    if (contextForToast == null) {
                        throw new RuntimeException("If you specified ViewType as TOAST, must set contextForToast.");
                    }
                    break;
                case SNACKBAR:
                    if (viewForSnackbar == null) {
                        throw new RuntimeException("If you specified ViewType as SNACKBAR, must set viewForSnackbar.");
                    }
                    break;
                case DIALOG:
                    if (contextForDialog == null) {
                        throw new RuntimeException("If you specified ViewType as DIALOG, must set activityForDialog.");
                    }
                    break;
                case INVIEW:
                    if (showingView == null || hidingView == null || messageTextViewId == 0) {
                        throw new RuntimeException("If you specified ViewType as INVIEW, must set showingView and hidingView and messageTextViewId.");
                    }
                    break;
            }
        }
    }

}
