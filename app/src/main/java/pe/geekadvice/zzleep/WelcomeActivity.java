package pe.geekadvice.zzleep;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener
{

	private AppConfig config;
	private Spinner spinnerListAreas;
	private EditText inputNumber;
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        init();
    }
    public void init()
    {
        setContentView(R.layout.activity_welcome);

        mAuth = FirebaseAuth.getInstance();
        config = new AppConfig(getApplicationContext());
        spinnerListAreas = (Spinner) findViewById(R.id.listAreas);
        inputNumber = (EditText) findViewById(R.id.inputNumber);

        /*INICIO DE AUTENTIFICACION CON FIREBASE*/
        mAuth = FirebaseAuth.getInstance();
        /*INICIO DE LOS LISTENER INICIALES*/
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                checkSession();
            }
        };
        findViewById(R.id.btnWelcomeNext).setOnClickListener(this);
        findViewById(R.id.btnWelcomeJump).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);


        spinnerListAreas.requestFocus();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.areas, android.R.layout.simple_spinner_dropdown_item);
        spinnerListAreas.setAdapter(adapter);


        initTextEvents();
    }
    private void initTextEvents()
    {
        inputNumber.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
            @Override
            public void afterTextChanged(Editable s)
            {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() > 8)
                    findViewById(R.id.btnWelcomeNext).setEnabled(true);
                else
                    findViewById(R.id.btnWelcomeNext).setEnabled(false);
            }
        });
    }

    /*CONTROLADOR DE LOS BOTONES DE LOGIN*/
    @Override
    public void onClick(View view)
    {

        switch (view.getId()) {
            case R.id.btnWelcomeNext:
                startActivity(new Intent(this, RegisterActivity.class));
                config.saveUserPhone(inputNumber.getText().toString(), spinnerListAreas.getSelectedItem().toString());
                break;
            case R.id.btnWelcomeJump:
                startAnonimus();
                break;
            case R.id.btnLogin:
                startActivity(new Intent(this, LoginActivity.class));
                break;

        }
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


	private void checkSession()
	{
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		if (user != null) {
			Toast.makeText(this, "Hola anónimo", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(this, MainActivity.class));
		} else {
			Log.v("GA", "Usuario no logueado");
		}
	}

	private void startAnonimus()
	{
		mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
		{
			@Override
			public void onComplete(@NonNull Task<AuthResult> task)
			{
				Log.d("GA", "signInAnonymously:onComplete:" + task.isSuccessful());

				// If sign in fails, display a message to the user. If sign in succeeds
				// the auth state listener will be notified and logic to handle the
				// signed in user can be handled in the listener.
				if (!task.isSuccessful()) {
					Log.v("GA", "signInAnonymously", task.getException());
					Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
				}

				// ...
			}
		});
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus){}
}
