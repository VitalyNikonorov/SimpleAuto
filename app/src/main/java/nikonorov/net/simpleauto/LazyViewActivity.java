package nikonorov.net.simpleauto;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Vitaly Nikonorov on 24.03.17.
 * email@nikonorov.net
 */

public class LazyViewActivity extends AppCompatActivity implements View.OnTouchListener {

    private View car;
    private float angle = 0.0f;
    private boolean isActive = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lazy_screen);
        car = findViewById(R.id.car);
        findViewById(R.id.field_view).setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP && isActive) {
            float x = event.getX();
            float y = event.getY();
            final float[] newCource = new float[1];

            float calculatedCource = (float) Math.toDegrees(Math.atan( (x - car.getX()) / (car.getY() - y)));
            if (y > car.getY()) {
                calculatedCource -= 180.0f;
            }
            newCource[0] = calculatedCource;

            ObjectAnimator animX = ObjectAnimator.ofFloat(car, "x", x - car.getWidth() / 2);
            ObjectAnimator animY = ObjectAnimator.ofFloat(car, "y", y - car.getHeight() / 2);
            ObjectAnimator animRotating = ObjectAnimator.ofFloat(car, "rotation", angle, newCource[0]);

            AnimatorSet set = new AnimatorSet();
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isActive = false;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isActive = true;
                    angle = newCource[0];
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    isActive = true;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            set.play(animX).with(animY).after(animRotating);
            set.start();
        }
        return true;
    }
}
