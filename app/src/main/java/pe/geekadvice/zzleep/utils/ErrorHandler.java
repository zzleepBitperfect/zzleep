package pe.geekadvice.zzleep.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthException;

/**
 * Created by gerson on 3/10/16.
 */

public class ErrorHandler
{
	Context ctx;

	public ErrorHandler(Context _ctx)
	{
		ctx = _ctx;
	}

	public static void showMessage(Context _ctx, Exception e)
	{
		FirebaseAuthException fba = (FirebaseAuthException) e;

		String msg = "";

		switch (fba.getErrorCode())
		{
			case "ERROR_EMAIL_ALREADY_IN_USE":
				msg = "Esta cuenta de correo ya se encuentra registrada";
				break;
			case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
				msg = "Ya registrada con credenciales distintas";
				break;
			case "ERROR_CREDENTIAL_ALREADY_IN_USE":
				msg = "Estas credenciales ya fueron usadas";
				break;
			case "ERROR_USER_DISABLED":
				msg = "Tu usuario está deshabilitado";
				break;
			case "ERROR_USER_TOKEN_EXPIRED":
				msg = "Token de usuario expirado";
				break;
			case "ERROR_INVALID_USER_TOKEN":
				msg = "Token invalido";
				break;
			case "ERROR_USER_NOT_FOUND":
				msg = "No encontramos un usuario con esos datos";
				break;
			case "ERROR_WRONG_PASSWORD":
				msg = "Al parecer tu contraseña con es correcta";
				break;
			default:
				msg = "Error no controlado";
				break;
		}

		Log.v("GA", "ERROR: " + fba.getErrorCode());
		Toast.makeText(_ctx, msg, Toast.LENGTH_LONG).show();
	}
}
