package pe.geekadvice.zzleep.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by gerson on 27/09/16.
 */

public class Animate
{
	public static void fadeOut(Object _target, long _speed)
	{
		ObjectAnimator anim = ObjectAnimator.ofFloat(_target, "alpha", 1, 0);
		anim.setDuration(_speed);
		anim.start();
	}

	public static void fadeIn(Object _target, long _speed)
	{
		ObjectAnimator anim = ObjectAnimator.ofFloat(_target, "alpha", 0, 1);
		anim.setDuration(_speed);
		anim.start();
	}

	public static void slideUp(final View _target, long _speed)
	{
		_target.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		ObjectAnimator anim = ObjectAnimator.ofFloat(_target, "alpha", 0, 1);
		ObjectAnimator anim2 = ObjectAnimator.ofFloat(_target, "translationY", 50, 0);

		_target.setVisibility(View.VISIBLE);

		anim.addListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator animation)
			{
				Log.v("GA", "Animación terminada");
				_target.setLayerType(View.LAYER_TYPE_NONE, null);
			}
		});

		AnimatorSet as = new AnimatorSet();
		as.play(anim).with(anim2);
		as.setInterpolator(new AccelerateInterpolator(0.1F));
		as.setDuration(_speed);
		as.start();
	}
	public static void slideDown(final View _target, long _speed, AnimatorListenerAdapter _callback)
	{
		_target.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		ObjectAnimator anim = ObjectAnimator.ofFloat(_target, "alpha", 1, 0);
		ObjectAnimator anim2 = ObjectAnimator.ofFloat(_target, "translationY", 0, 50);

		anim.addListener(_callback);

		AnimatorSet as = new AnimatorSet();
		as.play(anim).with(anim2);
		as.setInterpolator(new AccelerateInterpolator(0.8F));
		as.setDuration(_speed);
		as.start();
	}

	public static void animateMargin(final View _target, final int _top, int _left)
	{
		_target.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) _target.getLayoutParams();
		final int marginOriginal = params.topMargin;

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				params.topMargin = marginOriginal + (int)(_top * interpolatedTime);
//				_target.setLayoutParams(params);
			}
		};
		a.setDuration(300); // in ms
		_target.startAnimation(a);
	}
}
