package pe.geekadvice.zzleep;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import pe.geekadvice.utils.JsonTask;
import pe.geekadvice.zzleep.components.InputLabel;
import pe.geekadvice.zzleep.utils.ErrorHandler;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener
{


	private static final int SELECT_PICTURE = 100;

    private AppConfig config;
	private ImageView imageAvatar;
	private InputLabel regName;
	private InputLabel regLastName;
	private InputLabel regEmail;
	private InputLabel regPass;
	private TextView labelBtnFcebook;

	private CallbackManager callbackManager;
	private Profile profile;
	private AuthCredential fbCredential = null;
	private AccessToken token = null;
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;
	private ProgressDialog pDialog = null;
    private Uri selectedImageUri;
private boolean llaveLogin=true;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        init();
    }
    public void init()
    {

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        imageAvatar = (ImageView) findViewById(R.id.imageAvatar);
        config = new AppConfig(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        regName = (InputLabel) findViewById(R.id.regName);
        regLastName = (InputLabel) findViewById(R.id.regLastName);
        regEmail = (InputLabel) findViewById(R.id.regEmail);
        regPass = (InputLabel) findViewById(R.id.regPassword);
        labelBtnFcebook = (TextView) findViewById(R.id.labelBtnFcebook);
        pDialog = new ProgressDialog(getApplicationContext());
        pDialog.setIndeterminate(true);


        /*Aqui están los controladores de los botones creados para welcom activity*/
        findViewById(R.id.loginFacebook).setOnClickListener(this);
        findViewById(R.id.btnRegisterNext).setOnClickListener(this);
        imageAvatar.setOnClickListener(this);
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                /*PROFILE ES EL PERFIL DEL USUARIO OBTENIDO*/
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && llaveLogin) {
                    llaveLogin=false;
                    Log.v("GA", "UPDATE onAuthStateChanged:signed_in:" + user.getUid());
                    UserProfileChangeRequest profileUpdates;
                    Log.v("Usuario Result: ", regName.getText() + "|" + regLastName.getText());
                    if(profile != null && profile.getLinkUri() != null) {
                        profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(regName.getText() +"|"+ regLastName.getText()).setPhotoUri(selectedImageUri).build();
                    } else {
                        profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(regName.getText()  + "|" + regLastName.getText()).setPhotoUri(selectedImageUri).build();
                    }
                    /*Registra el ususario pero no inicia el main activity*/
                    user.getToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                String auxiliar = "http://clientes.geekadvice.pe/" +
                                        "zzleep-server/public/api/v1/users?user_token="+idToken+"&name="+regName.getText()+"&last_name="
                                        +regLastName.getText();
                                new JsonTask(RegisterActivity.this,4).execute(auxiliar);
                                Log.v("GA-ZZ", "TOKEN: " + idToken);
                            } else {
                            }
                        }
                    });
                    /*Inicia el main activity pero solo si profile es distinto de null*/
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful()) {
                                        linkFacebookAccount();
                                        Log.d("GA", "User profile updated.");
                                    }
                                }
                            });
                } else {
                    Log.d("GA", "onAuthStateChanged:signed_out");
                }
            }
        };

        initFacebook();
    }
    private void initFacebook()
    {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult)
            {
                token = loginResult.getAccessToken();
                fbCredential = FacebookAuthProvider.getCredential(token.getToken());

				//pDialog = ProgressDialog.show(getApplicationContext(), "Titulo", "Cargando información", true, false);

                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker()
                    {
                        @Override
                        protected void onCurrentProfileChanged(Profile _profile, Profile _profile2)
                        {
                            mProfileTracker.stopTracking();
                            profile = Profile.getCurrentProfile();
                            completeForm();
                        }
                    };
                } else {
                    profile = Profile.getCurrentProfile();
                    completeForm();
                }
            }
            /*VACIO*/
            @Override
            public void onCancel()
            {
                Toast.makeText(RegisterActivity.this,"Error conexión Facebook",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error)
            {
                Toast.makeText(RegisterActivity.this,"Error conexión Facebook",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void completeForm()
    {
        regName.setText(profile.getFirstName());
        regLastName.setText(profile.getLastName());
        labelBtnFcebook.setText("Estás conectado como: " + profile.getFirstName());
        selectedImageUri=profile.getProfilePictureUri(200, 200);
        loadImageAvatar(profile.getProfilePictureUri(200, 200));
        GraphRequest request = GraphRequest.newMeRequest(token,
                new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response)
                    {
                        Log.v("LoginActivity", response.toString());
                        try {
                            regEmail.setText(object.getString("email"));
                            //String birthday = object.getString("birthday"); // 01/31/1980 format
                        } catch (JSONException e) {
                            Log.v("GA", "Error al parsear facebook");
                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void linkFacebookAccount()
    {

        if (fbCredential == null) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("displayName", regName.getText() + "|" + regLastName.getText());
            startActivity(i);
            return;
        }

        mAuth.getCurrentUser().linkWithCredential(fbCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        Log.d("GA", "FB linkWithCredential:onComplete:" + task.isSuccessful());
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("displayName", regName.getText() + "|" + regLastName.getText());
                        startActivity(i);
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.loginFacebook:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                break;
            case R.id.btnRegisterNext:
                registerUser();
                break;
            case R.id.imageAvatar:
                openImageChooser();
                break;
        }
    }


	private void registerUser()
	{
		if (regPass.getText().length() < 6) {
			Toast.makeText(getApplicationContext(), "Por favor ingresa una contraseña de al menos 6 dígitos", Toast.LENGTH_LONG).show();
			return;
		}
		if (regEmail.getText().length() < 3) {
			Toast.makeText(getApplicationContext(), "Por favor ingresa un tu e-mail", Toast.LENGTH_LONG).show();
			return;
		}

		pDialog = ProgressDialog.show(this, "Titulo", "Registrando usuario, por favor espera", true, false);
		mAuth.createUserWithEmailAndPassword(regEmail.getText(), regPass.getText()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
		{
			@Override
			public void onComplete(@NonNull Task<AuthResult> task)
			{
				pDialog.dismiss();
				Log.v("GA", "Usuario creado createUserWithEmail:onComplete:" + task.isSuccessful());
				if (!task.isSuccessful()) {
					ErrorHandler.showMessage(getApplicationContext(), task.getException());
				}
			}
		});

	}


    /*Setea la imagen*/
	private void loadImageAvatar(Uri _uri)
	{
		Transformation transformation = new RoundedTransformationBuilder()
				.scaleType(ImageView.ScaleType.CENTER_CROP)
				.borderColor(0xFFFFFFFF)
				.borderWidthDp(3)
				.cornerRadiusDp(10)
				.oval(false)
				.build();

		Picasso.with(getApplicationContext()).load(_uri)
				.resize(200, 200)
				.centerCrop()
				.transform(transformation)
				.placeholder(R.mipmap.icon_menu)
				.error(R.mipmap.icon_menu)
				.into(imageAvatar, new Callback()
				{
					@Override
					public void onSuccess()
					{
						if (pDialog != null)
							pDialog.dismiss();
					}

					@Override
					public void onError()
					{
						if (pDialog != null)
							pDialog.dismiss();
					}
				});
	}
    /*Seleccionar imagen*/
    private void openImageChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                loadImageAvatar(selectedImageUri);
            }
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
