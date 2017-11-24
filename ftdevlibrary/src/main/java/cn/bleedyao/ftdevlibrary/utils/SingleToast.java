package cn.bleedyao.ftdevlibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SingleToast {
    private static Toast toast;

    private SingleToast() {
    }

    @SuppressLint("ShowToast")
    public static void showToastShort(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
            LinearLayout layout = (LinearLayout) toast.getView();
            TextView tv = (TextView) layout.getChildAt(0);
            tv.setTextSize(16);
        }
//        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.setText(text);
        toast.show();
    }

    @SuppressLint("ShowToast")
    public static void showToastLong(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        }
//        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setText(text);
        toast.show();
    }

    @SuppressLint("ShowToast")
    public static void midToast(Context context, String str) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        }
        toast.setText(str);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.YELLOW);
        toast.show();
    }
}
