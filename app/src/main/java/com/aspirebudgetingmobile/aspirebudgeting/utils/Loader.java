package com.aspirebudgetingmobile.aspirebudgeting.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.aspirebudgetingmobile.aspirebudgeting.R;

import java.util.Objects;

public class Loader {

    View view;
    Dialog dialog;
    Context context;

    public Loader(Context context) {
        this.context = context;
    }

    public void show() {
        view = LayoutInflater.from(context).inflate(R.layout.loading_layout, null);
        ImageView loadingImage = view.findViewById(R.id.loaderImageView);

        Animation fadeInOutAnim = new AlphaAnimation(0, 1);
        fadeInOutAnim.setDuration(500);
        fadeInOutAnim.setRepeatCount(Animation.INFINITE);
        fadeInOutAnim.setRepeatMode(Animation.REVERSE);

        loadingImage.setImageResource(R.drawable.currency_icons);
        AnimationDrawable currencyIcons = (AnimationDrawable) loadingImage.getDrawable();
        currencyIcons.start();
        loadingImage.setAnimation(fadeInOutAnim);

        dialog = new Dialog(context);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        Objects.requireNonNull(dialog.getWindow()).setLayout(-1, -1);
        dialog.show();
    }

    public void hide() {
        if (dialog != null)
            dialog.dismiss();
    }
}
