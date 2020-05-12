package com.commonsware.cwac.camera.demo.other;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.claimmate.R;

public class FocusMarkerLayout extends FrameLayout {
    private FrameLayout mFocusMarkerContainer;
    private ImageView mFill;
    private FrameLayout frameLayout;

    public FocusMarkerLayout(@NonNull Context context) {
        this(context, (AttributeSet)null);
    }

    public FocusMarkerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(this.getContext()).inflate(R.layout.layout_focus_marker, this);
        this.frameLayout = this.findViewById(R.id.root);
        this.mFocusMarkerContainer = this.findViewById(R.id.focusMarkerContainer);
        this.mFill = this.findViewById(R.id.fill);
        this.mFocusMarkerContainer.setAlpha(0.0F);

    }

    public void focus(float mx, float my) {

//        mx *= (float)this.getWidth();
//        my *= (float)this.getHeight();
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(55,55);
        int x = (int)(mx - (float)(this.mFocusMarkerContainer.getWidth() / 2));
        int y = (int)(my - (float)(this.mFocusMarkerContainer.getWidth() / 2));

        Log.e("app_res", "focus x = "+x+", y = "+y+", w"+frameLayout.getWidth());
        this.mFocusMarkerContainer.setTranslationX((float)x);
        this.mFocusMarkerContainer.setTranslationY((float)y);
//        mFocusMarkerContainer.
//        mFocusMarkerContainer.setX(x);
//        mFocusMarkerContainer.setY(y);
        this.mFocusMarkerContainer.animate().setListener((Animator.AnimatorListener)null).cancel();
        this.mFill.animate().setListener((Animator.AnimatorListener)null).cancel();
        this.mFill.setScaleX(0.0F);
        this.mFill.setScaleY(0.0F);
        this.mFill.setAlpha(1.0F);
        this.mFocusMarkerContainer.setScaleX(1.36F);
        this.mFocusMarkerContainer.setScaleY(1.36F);
        this.mFocusMarkerContainer.setAlpha(1.0F);
        this.mFocusMarkerContainer.animate().scaleX(1.0F).scaleY(1.0F).setStartDelay(0L).setDuration(330L).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                FocusMarkerLayout.this.mFocusMarkerContainer.animate().alpha(0.0F).setStartDelay(750L).setDuration(800L).setListener((Animator.AnimatorListener)null).start();
            }
        }).start();
        this.mFill.animate().scaleX(1.0F).scaleY(1.0F).setDuration(330L).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                FocusMarkerLayout.this.mFill.animate().alpha(0.0F).setDuration(800L).setListener((Animator.AnimatorListener)null).start();
            }
        }).start();
    }
}
