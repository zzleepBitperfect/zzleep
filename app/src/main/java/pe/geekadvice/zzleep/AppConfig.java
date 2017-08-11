package pe.geekadvice.zzleep;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Gerson on 1/01/15.
 */
public class AppConfig {

	public static String USER_PHONE = "u_p";
	public static String TUTORIAL = "tutorial";
	public static String USER_PHONE_AREA = "u_p_a";
	public static String SHARED_PREFERENCES_NAME = "AppSurveyPreferences";

	private SharedPreferences sp;
	private SharedPreferences.Editor sharedEditor;

	public String userPhone = "";
	public Boolean hasPhone = false;
	private Context currentContext;

	AppConfig(Context context)
	{
		sp = context.getSharedPreferences(AppConfig.SHARED_PREFERENCES_NAME, context.MODE_PRIVATE);
		sharedEditor = sp.edit();
		currentContext = context;
		updateData();
	}

	public SharedPreferences getSharedPreferences()
	{
		return sp;
	}
	public SharedPreferences.Editor getSharedEditor()
	{
		return sharedEditor;
	}

	public void updateData()
	{
		if(sp.contains(AppConfig.USER_PHONE))
		{
			this.userPhone = sp.getString(AppConfig.USER_PHONE, "No definido");
			hasPhone = true;
		}
	}

	public void removeData(String _code)
	{
		sharedEditor.remove(_code);
		sharedEditor.apply();
		updateData();
	}

	public void saveUserPhone(String _phone, String _area)
	{
		sharedEditor.putString(AppConfig.USER_PHONE_AREA, _area);
		sharedEditor.putString(AppConfig.USER_PHONE, _phone);
		sharedEditor.apply();
		updateData();
	}
	public void saveTutorial(Boolean _taken)
	{
		sharedEditor.putBoolean(AppConfig.TUTORIAL, _taken);
		sharedEditor.apply();
	}

	public boolean tutorialTaken()
	{
		return sp.getBoolean(AppConfig.TUTORIAL, false);
	}
}
