package edu.ub.happyhound_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;

public class DynamicLayout {

    public static int setDynamicLayout(Activity activity) {
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    @SuppressLint("ResourceAsColor")
    private GradientDrawable changeShape() {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
//        shape.setCornerRadius(32);
        shape.setColor(R.color.facebook_blue);
        return shape;
    }
}
