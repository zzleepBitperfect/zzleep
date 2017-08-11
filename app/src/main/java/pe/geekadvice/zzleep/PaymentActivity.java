package pe.geekadvice.zzleep;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

//import com.culqi.checkout.Card;
//import com.culqi.checkout.Token;

import com.google.android.gms.common.api.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import pe.geekadvice.Culqi.*;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import pe.geekadvice.utils.ApiPost;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener
{
	Boolean cardIsBack = false;
	String userid ="";
	String tokenCode="";
	String nombre="";
	String precio="";
	String userToken="";
	Integer id;
	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);

		id=(getIntent().getIntExtra("id",0));
		nombre=getIntent().getStringExtra("nombre");
		precio=getIntent().getStringExtra("precio");
		userid=getIntent().getStringExtra("userid");
		userToken=getIntent().getStringExtra("userToken");
		//((TextView)(findViewById(R.id.txTituloPayment))).setText("Comprar Alarma: "+nombre+"(S./ "+precio+")");
		((Button)(findViewById(R.id.btnPay))).setText("PAGAR S./ "+precio);
		init();
	}

	private void init()
	{
//		creditCardView = (CreditCardView) findViewById(R.id.card);

		findViewById(R.id.card_number).setOnKeyListener(this);
		findViewById(R.id.card_name).setOnKeyListener(this);
		findViewById(R.id.card_last_name).setOnKeyListener(this);
		findViewById(R.id.card_cvv).setOnKeyListener(this);
		findViewById(R.id.card_month).setOnKeyListener(this);
		findViewById(R.id.card_year).setOnKeyListener(this);
		findViewById(R.id.btnPay).setOnClickListener(this);

//		creditCardView.setCVV("5458");
//		creditCardView.showBack();
	}

	private void loadToken()
	{
		Card card = new Card("4111111111111111", "123", 9, 2020, "wm@wm.com");
		TokenAux tokenAux= new TokenAux();
		/*card.setApellido("Doe");
		card.setNombre("Jon");
		card.setCorreo_electronico("jon@culqi.com");
		card.setCvv(123);
		card.setNumero(4111111111111111L);
		card.setA_exp(9);
		card.setM_exp(2020);
		*/
		Token token = new Token("pk_test_QhKHP0LiB6rLOr81");
		Log.v("GA", "Llamando al servidor");

		token.createToken(getApplicationContext(), card, new TokenCallback() {
			@Override
			public void onSuccess(JSONObject token) {
				try {
					Log.v("GA", token.get("id").toString());
					tokenCode=token.get("id").toString();
					procesarCompra();
				} catch (JSONException e) {
					e.printStackTrace();e.printStackTrace();
				}
			}
			@Override
			public void onError(Exception error) {
				Toast.makeText(getApplicationContext(), "Error al generar el token", Toast.LENGTH_LONG).show();
			}
		});



	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.btnPay:
				loadToken();
				break;
		}
	}

	@Override
	public boolean onKey(View view, int i, KeyEvent keyEvent)
	{
		String text = ((EditText) view).getText().toString();

		/*switch (view.getId())
		{
			case R.id.card_number:
				if(cardIsBack)
				{
					creditCardView.showFront();
					cardIsBack = false;
				}
				creditCardView.setCardNumber(text);
				break;
			case R.id.card_name:
				if(cardIsBack)
				{
					creditCardView.showFront();
					cardIsBack = false;
				}
				creditCardView.setCardHolderName(text);
				break;
			case R.id.card_last_name:
				if(cardIsBack)
				{
					creditCardView.showFront();
					cardIsBack = false;
				}
				creditCardView.setCardHolderName(text);
				break;
			case R.id.card_cvv:
				if(!cardIsBack)
				{
					creditCardView.showBack();
					cardIsBack = true;
				}
				creditCardView.setCVV(text);
				break;
		}*/
//		Log.v("GA", "KEY: " + text);
		return false;
	}

	public void enviarData(){
		String queryJSON="{";
		queryJSON=queryJSON+"token_id:"+tokenCode+",";
		queryJSON=queryJSON+"user_id:"+userid+",user_address:'address',user_ciry:'pe',user_phone:'900000000'}";
		ApiPost apiPost = new ApiPost(this,1);
		apiPost.execute("http://clientes.geekadvice.pe/zzleep-server/public/api/v1/users/add-card",queryJSON);
	}
	public void procesarCompra(){
		String queryJSON="{";
		queryJSON=queryJSON+"user_token:'"+userToken+"',";
		queryJSON=queryJSON+"token_id:'"+tokenCode+"',";
		queryJSON=queryJSON+"user_id:'"+userid+"',product_id:'"+this.id+"'}";
		ApiPost apiPost = new ApiPost(this,2);
		apiPost.execute("http://clientes.geekadvice.pe/zzleep-server/public/api/v1/products/buy",queryJSON);
	}
	public void exitoCompra(){
		Toast.makeText(getApplicationContext(), "Se generó el token de Compra con el token : "+tokenCode, Toast.LENGTH_LONG).show();
		finish();
	}
}
