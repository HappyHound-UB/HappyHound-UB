package edu.ub.happyhound_app.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;

import edu.ub.happyhound_app.R;

public class DynamicLayout {

    public static int setDynamicLayout(Activity activity) {
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    @SuppressLint("ResourceAsColor")
    public static GradientDrawable changeShape() {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setStroke(2, Color.BLACK);
        shape.setCornerRadius(5);
        shape.setColor(R.color.md_theme_light_inverseOnSurface);
        return shape;
    }
}
