package pe.geekadvice.zzleep.utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;

import pe.geekadvice.zzleep.R;

/**
 * Created by gerson on 16/10/16.
 */

public class Metrics
{
	public static int getPixelsFromDP(float _value, DisplayMetrics _metrics)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _value, _metrics);
	}
}
