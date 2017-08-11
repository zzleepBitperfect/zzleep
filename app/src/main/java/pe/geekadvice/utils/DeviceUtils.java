package pe.geekadvice.utils;

import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by gerson on 31/12/14.
 */
public class DeviceUtils {

	void DeviceUtils()
	{

	}

	public static String getDeviceID(ContentResolver _contentCesolver)
	{
		return Settings.Secure.getString(_contentCesolver, Settings.Secure.ANDROID_ID);
	}

	public static String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return model;
		} else {
			return (manufacturer) + " " + model;
		}
	}
}
