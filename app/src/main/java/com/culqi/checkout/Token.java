package com.culqi.checkout;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.GsonBuilder;


import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Token
{

	public String getToken(Card card, final String merchantCode, Callback callback)
	{

		String responseToken;

		Map<String, Object> informacion_pago = new HashMap<String, Object>();
		informacion_pago.put("correo_electronico", card.getCorreo_electronico());
		informacion_pago.put("nombre", card.getNombre());
		informacion_pago.put("apellido", card.getApellido());
		informacion_pago.put("numero", card.getNumero());
		informacion_pago.put("cvv", card.getCvv());
		informacion_pago.put("a_exp", card.getA_exp());
		informacion_pago.put("m_exp", card.getM_exp());

		String json = new GsonBuilder().create().toJson(informacion_pago, Map.class);

		String url = "https://integ-pago.culqi.com/api/v1/tokens";
			/*RequestParams p = new RequestParams();
			p.put("correo_electronico", card.getCorreo_electronico());
			p.put("nombre", card.getNombre());
			p.put("apellido", card.getApellido());
			p.put("numero", card.getNumero());
			p.put("cvv", card.getCvv());
			p.put("a_exp", card.getA_exp());
			p.put("m_exp", card.getM_exp());
			p.setContentEncoding("application/json");
			p.put("Accept", "application/json");
			p.put("Content-type", "application/json");
			p.put("Authorization", "Bearer " + merchantCode);*/

//			AsyncHttpClient response = makeRequest(url, json, merchantCode);
//			AsyncHttpClient client = new AsyncHttpClient();
		Log.v("GA", "iniciando petición");

		MediaType JSON = MediaType.parse("application/json; charset=utf-8");

		OkHttpClient client = new OkHttpClient();
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder()
				.addHeader("Accept", "application/json")
				.addHeader("Content-type", "application/json")
				.addHeader("Authorization", "Bearer " + merchantCode)
				.url(url)
				.post(body)
				.build();


		client.newCall(request).enqueue(callback);

		return "OK";
	}

}
