package pe.geekadvice.zzleep;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserInfo;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PerfilZzleeperActivity extends AppCompatActivity
{
	private ImageView imageAvatar;
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;
	private FirebaseUser user = null;

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	@Override
	public void onStart()
	{
		super.onStart();
		mAuth.addAuthStateListener(mAuthListener);
	}

	@Override
	public void onStop()
	{
		super.onStop();
		if (mAuthListener != null) {
			mAuth.removeAuthStateListener(mAuthListener);
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil_zzleeper);

		mAuth = FirebaseAuth.getInstance();
		mAuthListener = new FirebaseAuth.AuthStateListener()
		{
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
			{
				user = firebaseAuth.getCurrentUser();
			}
		};

		//Verificar sesión
		user = FirebaseAuth.getInstance().getCurrentUser();
		if (user != null) {
			Log.v("GA", "Usuario logueado: " + user.getDisplayName());
			init();
		} else {
			Log.v("GA", "Usuario no logueado");
			gotoLogin();
		}
		Button btBack= (Button) findViewById(R.id.btnBack2);


		btBack.setOnClickListener(new View.OnClickListener() {
									  @Override
									  public void onClick(View v) {
										  finish();
									  }
								  }
		);
	}

	private void init()
	{
		Uri photoUrl = null;
		String name = "";
		String email = "";
		for (UserInfo profile : user.getProviderData()) {
			String providerId = profile.getProviderId();
			String uid = profile.getUid();
			name = profile.getDisplayName();
			email = profile.getEmail();
			photoUrl = profile.getPhotoUrl();
		}

		TextView txtName = (TextView) findViewById(R.id.txtName);
		TextView txtEmail = (TextView) findViewById(R.id.txtEmail);
		imageAvatar = (ImageView) findViewById(R.id.imageAvatar);
		final Intent listaDeParaderos= new Intent(this,ListaParaderos.class);
		findViewById(R.id.btnParaderos).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(listaDeParaderos);
			}
		});
		if(photoUrl != null)
		{
			loadImageAvatar(photoUrl);
		}
		txtEmail.setText(email);
		txtName.setText(name);
	}

	private void loadImageAvatar(Uri _uri)
	{
		/*Transformation transformation = new RoundedTransformationBuilder()
				.scaleType(ImageView.ScaleType.CENTER_CROP)
				.borderColor(0xFFFFFFFF)
				.borderWidthDp(3)
				.cornerRadiusDp(10)
				.oval(false)
				.build();*/

		Picasso.with(getApplicationContext()).load(_uri)
				.resize(200, 200)
				.centerCrop()
				.placeholder(R.mipmap.icon_menu)
				.error(R.mipmap.icon_menu)
				.into(imageAvatar, new Callback()
				{
					@Override
					public void onSuccess()
					{
					}

					@Override
					public void onError()
					{
					}
				});
	}

	public void gotoLogin()
	{
		startActivity(new Intent(this, WelcomeActivity.class));
	}
}
