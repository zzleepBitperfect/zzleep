package pe.geekadvice.zzleep;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Gerson on 17/06/16.
 */
public class DefaultApplication extends Application
{
	@Override
	public void onCreate() {
		super.onCreate();
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath("fonts/RobotoCondensed-Regular.ttf")
				.setFontAttrId(R.attr.fontPath)
				.build()
		);
	}
}
