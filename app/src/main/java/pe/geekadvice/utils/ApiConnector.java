package pe.geekadvice.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Gerson on 19/10/2014.
 */
public class ApiConnector extends AsyncTask<String, String, String>
{

	//static String PATH = "http://192.168.1.54/knowitv2/api/v1/";
	//static String PATH = "http://clientes.geekadvice.pe/knowitv2/public/api/v1/";
	static String PATH = "http://sodexo.geekadvice.pe/api/v1/";
//	static String PATH = "http://192.168.0.108:8000/api/v1/";
	public static String GET_DEVICE = "devices/find";
	public static String SYNC_DEVICE = "devices/sync";
	public static String LOAD_QUESTIONS = "questions";

	public ApiConnector()
	{

	}

	@Override
	protected String doInBackground(String... var)
	{
		MediaType JSON = MediaType.parse("application/json; charset=utf-8");

		OkHttpClient client = new OkHttpClient();
		RequestBody body = RequestBody.create(JSON, var[0]);
		Request request = new Request.Builder()
				.addHeader("Accept", "application/json")
				.addHeader("Content-type", "application/json")
				.addHeader("Authorization", "Bearer " + var[2])
				.url(var[1])
				.post(body)
				.build();


		client.newCall(request).enqueue(new Callback()
		{
			@Override
			public void onFailure(Call call, IOException e)
			{
				Log.v("GA", e.getMessage());
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException
			{
				if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

				Log.v("GA", response.body().string());
			}
		});

		return "ok";
	}

	public static String getUrl(String _method, String _segment)
	{
		String url = PATH + _method;

		if(_segment != null)
			url += ("/" + _segment);

		Log.v("GA", "Conectando a: " + PATH + _method + " / " + _segment);

		return url;
	}
}