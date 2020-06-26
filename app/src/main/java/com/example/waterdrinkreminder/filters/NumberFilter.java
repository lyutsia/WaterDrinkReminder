package com.example.waterdrinkreminder.filters;

import android.text.InputFilter;
import android.text.Spanned;

public class NumberFilter implements InputFilter {
    private int count;
    public NumberFilter(int count) {
        this.count =count;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (source.length() == 0) {
            return null;// deleting, keep original editing
        }
        String result = "";
        result += dest.toString().substring(0, dstart);
        result += source.toString().substring(start, end);
        result += dest.toString().substring(dend, dest.length());

        boolean allowEdit = true;
        char c;
        if (result.length() > 0) {
            c = result.charAt(0);
            allowEdit &= (c >= '1' && c <= '9');
        }
        if (result.length() > count) {
            allowEdit =false;
        }
        return allowEdit ? null : "";
    }
}
