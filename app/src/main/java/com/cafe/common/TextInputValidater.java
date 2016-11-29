package com.cafe.common;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.justin.utils.common.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Rocky on 2016/11/28.
 */

public class TextInputValidater {

    private boolean mUseToast = true;
    private boolean mUseSnackbar;
    private View mSnackbarContainer;
    private String mUnifiedPrompt = "";

    private Map<TextView, String> mValidateMap = new LinkedHashMap<>();

    public void setmUnifiedPrompt(String unifiedPrompt) {
        mUnifiedPrompt = unifiedPrompt;
    }

    public void putValidateItem(TextView inputView, String prompt) {
        mValidateMap.put(inputView, prompt);
    }

    public void putValidateItem(TextView inputView) {
        mValidateMap.put(inputView, mUnifiedPrompt);
    }

    public void isUseSnackBar(boolean useSnackbar, View snackbarContainer) {
        mUseSnackbar = useSnackbar;
        mSnackbarContainer = snackbarContainer;
    }

    public boolean validate() {

        Iterator<Map.Entry<TextView, String>> iter = mValidateMap.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<TextView, String> entry = iter.next();

            CharSequence text = entry.getKey().getText();

            if (text == null || isEmpty(text.toString())) {
                showPrompt(entry.getKey(), entry.getValue());
                return false;
            }
        }

        return true;

    }

    private void showPrompt(View view, String prompt) {
        if (mUseSnackbar) {
            Snackbar.make(mSnackbarContainer, prompt, Snackbar.LENGTH_SHORT).show();
        } else if (mUseToast) {
            Toast.makeText(view.getContext(), prompt, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.trim().isEmpty());
    }
}
