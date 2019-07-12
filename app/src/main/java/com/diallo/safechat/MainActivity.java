package com.diallo.safechat;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton button;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //cacher les bar de l'app
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    // cacher la navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        //changer la couleur de la navigation bar
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGoogleSingIn();
    }

    private void initGoogleSingIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        button = findViewById(R.id.sign_in_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });
    }

    public void connect() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account != null) {
            Intent fragmentActivity = new Intent(MainActivity.this, FragmentActivity.class);
            startActivity(fragmentActivity);
        } else {
            forceSignIn();
        }
    }

    private void forceSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_SIGN_IN) {
            Intent fragmentActivity = new Intent(MainActivity.this, FragmentActivity.class);
            startActivity(fragmentActivity);
        }
    }
}
